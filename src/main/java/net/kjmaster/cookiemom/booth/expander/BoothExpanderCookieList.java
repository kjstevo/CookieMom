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


package net.kjmaster.cookiemom.booth.expander;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.expander.ScoutExpanderListLayout;
import net.kjmaster.cookiemom.scout.expander.ScoutExpanderValues;
import net.kjmaster.cookiemom.scout.expander.ScoutExpanderValuesListAdapter;
import net.kmaster.cookiemom.dao.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * This class provides a simple card as Google Now ScoutExpanderValues
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class BoothExpanderCookieList extends Card {
    private final Booth mBooth;

    public BoothExpanderCookieList(Context context, Booth booth) {
        this(context, booth, R.layout.ui_cookie_table_content);
    }

    public BoothExpanderCookieList(Context context, Booth booth, int innerLayout) {
        super(context, innerLayout);
        this.mBooth = booth;
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

//      TextView textView = (TextView) view.findViewById(R.id.carddemo_googlenow_main_inner_lastupdate);
//      textView.setText("Update 14:57, 16 September"); //should use R.string.
        ScoutExpanderListLayout list = (ScoutExpanderListLayout) view.findViewById(R.id.carddemo_googlenow_main_inner_list);
        ScoutExpanderValuesListAdapter mAdapter = new ScoutExpanderValuesListAdapter(super.getContext(), buildArrayHelper());

        list.setAdapter(mAdapter);
    }

    // ------------------------------------------------------------------------------------------
    ArrayList<ScoutExpanderValues> buildArrayHelper() {
        //DataStore        dataStore = new DataStore(getContext());
        ArrayList<ScoutExpanderValues> list = new ArrayList<ScoutExpanderValues>();
        String cash = "$0.00/$0.00";
        float cashTotal = 0;

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            String cookieType = Constants.CookieTypes[i];
            HashMap<String, String> orderTotals = getTotalOrderPossCashForScoutCookieType(mBooth, cookieType);
            ScoutExpanderValues scoutExpanderValues = new ScoutExpanderValues();

            scoutExpanderValues.setCode(cookieType);
            try {
                float possTotal = Float.valueOf(orderTotals.get("PossTotal")) * -1;
                float orderTotal = Float.valueOf(orderTotals.get("OrderTotal"));

                scoutExpanderValues.setValue(possTotal);
                scoutExpanderValues.setDelta(orderTotal);
                scoutExpanderValues.setDeltaPerc((possTotal) * 4);
                cash = orderTotals.get("CashTotal");
                cashTotal = (cashTotal + (scoutExpanderValues.getValue() * 4));
            } catch (Exception e) {
                Log.e("cookiemom", "error in Google ScoutExpanderValues Card");
            }
            list.add(scoutExpanderValues);
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

    private HashMap<String, String> getTotalOrderPossCashForScoutCookieType(Booth booth, String cookieType) {

        List<Order> orderListStockAdapter = Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderDao.Properties.OrderScoutId.eq((-1 * booth.getId()) - 100),
                        OrderDao.Properties.OrderCookieType.eq(cookieType))
                .list();
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("OrderTotal", "0");

        int intVal;
        for (Order order : orderListStockAdapter) {
            intVal = order.getOrderedBoxes();
            intVal = intVal + Integer.valueOf(hashMap.get("OrderTotal"));
            hashMap.put("OrderTotal", String.valueOf(intVal));
        }

        List<CookieTransactions> poss = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(
                        CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()),
                        CookieTransactionsDao.Properties.CookieType.eq(cookieType),
                        CookieTransactionsDao.Properties.TransBoxes.notEq(0))
                .list();
        //noinspection UnusedAssignment
        intVal = 0;
        hashMap.put("PossTotal", "0");
        for (CookieTransactions cookieTransactions : poss) {
            intVal = cookieTransactions.getTransBoxes() + Integer.valueOf(hashMap.get("PossTotal"));
            hashMap.put("PossTotal", String.valueOf(intVal));
        }
        List<CookieTransactions> cashs = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(
                        CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()),
                        CookieTransactionsDao.Properties.CookieType.eq(cookieType),
                        CookieTransactionsDao.Properties.TransCash.notEq(0))
                .list();
        Double dVal;
        hashMap.put("CashTotal", String.valueOf(0));
        for (CookieTransactions cash : cashs) {
            dVal = Double.valueOf(hashMap.get("CashTotal")) + cash.getTransCash();
            hashMap.put("CashTotal", String.valueOf(dVal));
        }

        return hashMap;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
