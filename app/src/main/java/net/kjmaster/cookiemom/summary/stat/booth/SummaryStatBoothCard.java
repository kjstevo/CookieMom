/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.kjmaster.cookiemom.summary.stat.booth;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * This class provides a simple card as Google Now SummaryStatBooth
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatBoothCard extends Card {


    public SummaryStatBoothCard(Context context) {
        super(context, R.layout.summary_stat_booth_card_inner_layout);
        init();

    }

    private void init() {
        //Add Header
        CardHeader header = new CardHeader(getContext());
        header.setButtonExpandVisible(false);
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
        float grandDeltaTotal = 0;
        float grandDelttaPercTotal = 0;
        float grandValueTotal = 0;
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
                float possTotalValue = Float.valueOf(orderTotals.get("PossTotalValue")) * -1;
                float orderTotal = Float.valueOf(orderTotals.get("OrderTotal"));
                float orderTotalValue = Float.valueOf(orderTotals.get("OrderTotalValue"));
                summaryStatBoothValues.setValue(possTotal);
                grandValueTotal += possTotal;

                cash = orderTotals.get("CashTotal");
                cashTotal = (cashTotal + possTotalValue);
                summaryStatBoothValues.setDelta(Float.valueOf(cash));
                grandDeltaTotal += Float.valueOf(cash);
                summaryStatBoothValues.setDeltaPerc(((possTotalValue) - Float.valueOf(cash)) * -1);
                grandDelttaPercTotal += ((possTotalValue) - Float.valueOf(cash));
            } catch (Exception e) {
                Log.e("cookiemom", "error in Google BoothExpanderValues Card");
            }

            list.add(summaryStatBoothValues);
        }
        SummaryStatBoothValues summaryStatBoothValues = new SummaryStatBoothValues();
        summaryStatBoothValues.setValue(grandValueTotal);
        summaryStatBoothValues.setDelta(grandDeltaTotal);
        summaryStatBoothValues.setDeltaPerc((grandDelttaPercTotal));
        summaryStatBoothValues.setCode("Total");
        list.add(summaryStatBoothValues);


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
        hashMap.put("OrderTotalValue", "0.00");
        Double dblVal = 0.0;
        int intVal;
        for (CookieTransactions transactions : transactionsList) {
            if (transactions.getTransBoxes() < 0) {

                intVal = transactions.getTransBoxes();
                intVal = intVal + Integer.valueOf(hashMap.get("OrderTotal"));
                try {
                    dblVal += transactions.getTransBoxes() * Main.getCookieCosts(transactions.getCookieType()).doubleValue();
                } catch (Exception e) {
                    Log.d("kjmaster", "no cookie type");
                }
                hashMap.put("OrderTotal", String.valueOf(intVal));
                hashMap.put("OrderTotalValue", String.valueOf(dblVal));
            }
        }

        //noinspection UnusedAssignment
        intVal = 0;
        dblVal = 0.0;
        hashMap.put("PossTotal", "0");
        hashMap.put("PossTotalValue", "0.00");
        for (CookieTransactions cookieTransactions : transactionsList) {

            intVal = cookieTransactions.getTransBoxes() + Integer.valueOf(hashMap.get("PossTotal"));
            try {
                dblVal += Double.valueOf(cookieTransactions.getTransBoxes() * Main.getCookieCosts(cookieTransactions.getCookieType()));
            } catch (Exception e) {
                Log.d("kjmaster", "no cookie type");
            }
            hashMap.put("PossTotal", String.valueOf(intVal));
            hashMap.put("PossTotalValue", String.valueOf(dblVal));
        }
        Double dVal;
        hashMap.put("CashTotal", String.valueOf(0));
        for (CookieTransactions cash : transactionsList) {
            dVal = Double.valueOf(hashMap.get("CashTotal")) + cash.getTransCash();
            hashMap.put("CashTotal", String.valueOf(dVal));
        }

        return hashMap;
    }


}
