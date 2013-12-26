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

package net.kjmaster.cookiemom.summary.stat.scout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.Scout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides a simple card as Google Now SummaryStatScout
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatScoutCard extends Card {

    public SummaryStatScoutCard(Context context) {
        this(context, R.layout.summary_stat_scout_card_inner_layout);
    }

    public SummaryStatScoutCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();

    }

    private void init() {
        //Add Header
        CardHeader header = new CardHeader(getContext());
        header.setButtonExpandVisible(true);
        header.setTitle(getContext().getString(R.string.Scouts)); //should use R.string.
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

        SummaryStatScoutListLayout list = (SummaryStatScoutListLayout) view.findViewById(R.id.carddemo_googlenow_main_inner_list);
        SummaryStatScoutValuesListAdapter mAdapter =
                new SummaryStatScoutValuesListAdapter(super.getContext(), buildArrayHelper());


        list.setAdapter(mAdapter);

    }


    //------------------------------------------------------------------------------------------


    ArrayList<SummaryStatScoutValues> buildArrayHelper() {
        //DataStore        dataStore = new DataStore(getContext());
        ArrayList<SummaryStatScoutValues> list = new ArrayList<SummaryStatScoutValues>();
        String cash = "$0.00/$0.00";
        float cashTotal = 0;
        List<Scout> scoutList = Main.daoSession.getScoutDao().loadAll();
        for (Scout scout : scoutList) {
            String scoutName = scout.getScoutName();
            HashMap<String, String> orderTotals = getTotalOrderPossCashForScoutCookieType(scout);
            SummaryStatScoutValues summaryStatScoutValues = new SummaryStatScoutValues();

            summaryStatScoutValues.setCode(scoutName);
            try {
                float possTotal = Float.valueOf(orderTotals.get("PossTotal")) * -1;
                float orderTotal = Float.valueOf(orderTotals.get("OrderTotal"));

                summaryStatScoutValues.setValue(possTotal);


                cash = orderTotals.get("CashTotal");
                cashTotal = (cashTotal + (summaryStatScoutValues.getValue() * 4));
                summaryStatScoutValues.setDelta(Float.valueOf(cash));
                summaryStatScoutValues.setDeltaPerc((possTotal * 4) - Float.valueOf(cash));
            } catch (Exception e) {
                Log.e("cookiemom", "error in Google ScoutExpanderValues Card");
            }
            list.add(summaryStatScoutValues);
        }

        CardHeader header = new CardHeader(getContext());

        header.setButtonExpandVisible(false);
        header.setTitle("$" + cash + "/$" + String.valueOf(cashTotal));    // should use R.string.
        addCardHeader(header);

//      ScoutExpanderValues s1 = new ScoutExpanderValues("GOOG", 889.07f, 0.00f, 0.00f);
//      ScoutExpanderValues s2 = new ScoutExpanderValues("AAPL", 404.27f, 0.00f, 0.00f);
//      ScoutExpanderValues s3 = new ScoutExpanderValues("ENI", 17.59f, 0.06f, 0.34f);
//      ScoutExpanderValues s4 = new ScoutExpanderValues("Don Jones", 15.376f, 0.00f, 0.00f);
//
//
//      list.add(s1);
//      list.add(s2);
//      list.add(s3);
//      list.add(s4);
        return list;
    }

    private HashMap<String, String> getTotalOrderPossCashForScoutCookieType(Scout scout) {

        List<CookieTransactions> transactionsList = scout.getScoutsCookieTransactions();

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
