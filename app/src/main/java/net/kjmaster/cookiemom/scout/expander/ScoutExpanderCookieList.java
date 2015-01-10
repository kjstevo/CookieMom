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


package net.kjmaster.cookiemom.scout.expander;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.global.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

//~--- JDK imports ------------------------------------------------------------

/**
 * This class provides a simple card as Google Now ScoutExpanderValues
 *
 * @author Steven Dees
 */
public class ScoutExpanderCookieList extends Card {
    private final Scout scout;

    public ScoutExpanderCookieList(Context context, Scout scout) {
        this(context, scout, R.layout.ui_cookie_table_content);
    }

    public ScoutExpanderCookieList(Context context, Scout scout, int innerLayout) {
        super(context, innerLayout);
        this.scout = scout;
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {


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
            HashMap<String, String> orderTotals = getTotalOrderPossCashForScoutCookieType(scout, cookieType);
            ScoutExpanderValues scoutExpanderValues = new ScoutExpanderValues();

            scoutExpanderValues.setCode(cookieType);
            try {
                float possTotal = Float.valueOf(orderTotals.get("PossTotal")) * -1;
                float orderTotal = Float.valueOf(orderTotals.get("OrderTotal"));
                float possTotalValue = Float.valueOf(orderTotals.get("PossTotalValue")) * -1;
                scoutExpanderValues.setValue(possTotal);
                scoutExpanderValues.setDelta(orderTotal);
                scoutExpanderValues.setDeltaPerc((possTotal) * Main.getCookieCosts(cookieType));
                cash = orderTotals.get("CashTotal");

                cashTotal = (cashTotal + possTotalValue);
            } catch (Exception e) {
                Log.e("cookiemom", "error in Google ScoutExpanderValues Card");
            }
            list.add(scoutExpanderValues);
        }

        CardHeader header = new CardHeader(getContext());

        header.setButtonExpandVisible(false);
        header.setTitle("$" + cash + "/$" + String.valueOf(cashTotal));    // should use R.string.
        addCardHeader(header);


        return list;
    }


    private HashMap<String, String> getTotalOrderPossCashForScoutCookieType(Scout scout, String cookieType) {

        List<Order> orderListStockAdapter = Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderDao.Properties.OrderScoutId.eq(scout.getId()),
                        OrderDao.Properties.OrderCookieType.eq(cookieType))
                .list();
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("OrderTotal", "0");

        int intVal;
        double dblValue;
        for (Order order : orderListStockAdapter) {
            intVal = order.getOrderedBoxes();
            intVal = intVal + Integer.valueOf(hashMap.get("OrderTotal"));
            hashMap.put("OrderTotal", String.valueOf(intVal));
        }

        List<CookieTransactions> poss = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(
                        CookieTransactionsDao.Properties.TransScoutId.eq(scout.getId()),
                        CookieTransactionsDao.Properties.CookieType.eq(cookieType),
                        CookieTransactionsDao.Properties.TransBoxes.notEq(0))
                .list();
        //noinspection UnusedAssignment
        intVal = 0;
        dblValue = 0;
        hashMap.put("PossTotal", "0");
        hashMap.put("PossTotalValue", "0.0");
        for (CookieTransactions cookieTransactions : poss) {
            intVal = cookieTransactions.getTransBoxes() + Integer.valueOf(hashMap.get("PossTotal"));
            dblValue = intVal;
            hashMap.put("PossTotal", String.valueOf(intVal));
            hashMap.put("PossTotalValue", String.valueOf(dblValue * Main.getCookieCosts(cookieTransactions.getCookieType())));
        }
        List<CookieTransactions> cashs = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(
                        CookieTransactionsDao.Properties.TransScoutId.eq(scout.getId()),
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
