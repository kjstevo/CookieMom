/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package net.kjmaster.cookiemom.summary.stat.booth;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides a simple card as Google Now SummaryStatBooth
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatBoothCard extends Card {

    public SummaryStatBoothCard(Context context) {
        this(context, R.layout.summary_stat_booth_card_inner_layout);
    }

    public SummaryStatBoothCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();

    }

    private void init() {
        //Add Header
        CardHeader header = new CardHeader(getContext());
        header.setButtonExpandVisible(true);
        header.setTitle(getContext().getString(R.string.Booths)); //should use R.string.
        addCardHeader(header);

        //Add expand
//        CardExpand expand = new GoogleNowExpandCard(getContext());
//        addCardExpand(expand);

//        //Add onClick Listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=" , Toast.LENGTH_LONG).show();
//            }
//        });

//        //Add swipe Listener
//        setOnSwipeListener(new OnSwipeListener() {
//            @Override
//            public void onSwipe(Card card) {
//                Toast.makeText(getContext(), "Card removed", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView textView = (TextView) view.findViewById(R.id.carddemo_googlenow_main_inner_lastupdate);
//        textView.setText("Update 14:57, 16 September"); //should use R.string.

        SummaryStatBoothListLayout list = (SummaryStatBoothListLayout) view.findViewById(R.id.carddemo_googlenow_main_inner_list);
        SummaryStatBoothValuesListAdapter mAdapter =
                new SummaryStatBoothValuesListAdapter(super.getContext(), buildArrayHelper());


        list.setAdapter(mAdapter);

    }


    //------------------------------------------------------------------------------------------


    ArrayList<SummaryStatBoothValues> buildArrayHelper() {
        //DataStore        dataStore = new DataStore(getContext());
        ArrayList<SummaryStatBoothValues> list = new ArrayList<SummaryStatBoothValues>();
        String cash = "$0.00/$0.00";
        float cashTotal = 0;
        List<Booth> boothList = Main.daoSession.getBoothDao().loadAll();
        for (Booth booth : boothList) {
            String boothName = booth.getBoothLocation();
            HashMap<String, String> orderTotals = getTotalOrderPossCashForBoothCookieType(booth);
            SummaryStatBoothValues summaryStatBoothValues = new SummaryStatBoothValues();

            summaryStatBoothValues.setCode(boothName);
            try {
                float possTotal = Float.valueOf(orderTotals.get("PossTotal")) * -1;
                float orderTotal = Float.valueOf(orderTotals.get("OrderTotal"));

                summaryStatBoothValues.setValue(possTotal);


                cash = orderTotals.get("CashTotal");
                cashTotal = (cashTotal + (summaryStatBoothValues.getValue() * 4));
                summaryStatBoothValues.setDelta(Float.valueOf(cash));
                summaryStatBoothValues.setDeltaPerc(((possTotal * 4) - Float.valueOf(cash)) * -1);
            } catch (Exception e) {
                Log.e("cookiemom", "error in Google BoothExpanderValues Card");
            }
            list.add(summaryStatBoothValues);
        }

        CardHeader header = new CardHeader(getContext());

        header.setButtonExpandVisible(false);
        header.setTitle("$" + cash + "/$" + String.valueOf(cashTotal));    // should use R.string.
        addCardHeader(header);

        return list;
    }

    private HashMap<String, String> getTotalOrderPossCashForBoothCookieType(Booth booth) {

        List<CookieTransactions> transactionsList = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()))
                .list();

        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("OrderTotal", "0");

        int intVal;
        for (CookieTransactions transactions : transactionsList) {
            if (transactions.getTransBoxes() < 0) {
                intVal = transactions.getTransBoxes();
                intVal = intVal + Integer.valueOf(hashMap.get("OrderTotal"));
                hashMap.put("OrderTotal", String.valueOf(intVal));
            }
        }

        List<CookieTransactions> poss = transactionsList;
        //noinspection UnusedAssignment
        intVal = 0;
        hashMap.put("PossTotal", "0");
        for (CookieTransactions cookieTransactions : poss) {

            intVal = cookieTransactions.getTransBoxes() + Integer.valueOf(hashMap.get("PossTotal"));
            hashMap.put("PossTotal", String.valueOf(intVal));
        }
        List<CookieTransactions> cashs = transactionsList;
        Double dVal;
        hashMap.put("CashTotal", String.valueOf(0));
        for (CookieTransactions cash : cashs) {
            dVal = Double.valueOf(hashMap.get("CashTotal")) + cash.getTransCash();
            hashMap.put("CashTotal", String.valueOf(dVal));
        }

        return hashMap;
    }


}
