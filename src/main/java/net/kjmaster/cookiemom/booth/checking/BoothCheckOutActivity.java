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

package net.kjmaster.cookiemom.booth.checking;

import android.annotation.SuppressLint;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieOrCashDialogBase;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/10/13
 * Time: 8:40 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class BoothCheckOutActivity extends CookieOrCashDialogBase {
    private String fragName;
    private final HashMap<String, String> valuesMap = new HashMap<String, String>();

    @Override
    protected void createActionFragment() {
        createFrag();
    }

    @Override
    protected boolean isEditableValue() {
        return true;
    }


    @Override
    protected void saveForemData() {
        //performCheckOut();
        savePickupData();
    }

    @Override
    protected boolean isEditable() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Extra
    long BoothId;

    @Override
    protected void saveData() {
        performCheckOut();
    }

    @Override
    protected boolean isNegative() {
        return true;
    }

    @AfterViews
    void afterViewFrag() {
        super.CreateDialog(BoothId, this, "Booth Check-out");


    }

    private void createFrag() {
        fragName = getString(R.string.booth_checkout_order);
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder()
                        .hideCases(false)
                        .showInventory(true)
                        .showExpected(true)
                        .isEditable(this.isEditable()).build(),
                fragName);

        createActionMode(getString(R.string.checkout));
    }

    private void savePickupData() {


        Calendar c = Calendar.getInstance();

        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();

        OrderDao orderDao = Main.daoSession.getOrderDao();

        for (String cookieFlavor : CookieTypes) {
            Integer boxesInInventory = 0;
            List<CookieTransactions> transactionsList = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                    .where(
                            CookieTransactionsDao.Properties.CookieType.eq(cookieFlavor)
                    )
                    .list();
            for (CookieTransactions transaction : transactionsList) {
                boxesInInventory += transaction.getTransBoxes();
            }

            Integer requestedBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));
            if (boxesInInventory > 0) {
                if (requestedBoxes <= boxesInInventory) {
                    CookieTransactions cookieTransactions = new CookieTransactions(
                            null, -1L, BoothId, cookieFlavor, requestedBoxes * -1,
                            c.getTime(),
                            (double) 0
                    );
                    dao.insert(cookieTransactions);
                } else {
                    CookieTransactions cookieTransactions = new CookieTransactions(
                            null, -1L, BoothId, cookieFlavor, boxesInInventory * -1,
                            c.getTime(),
                            (double) 0
                    );
                    dao.insert(cookieTransactions);
                    requestedBoxes = boxesInInventory;
                }
            } else {
                List<Order> orders = orderDao.queryBuilder()
                        .where(
                                OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(BoothId)),
                                OrderDao.Properties.OrderCookieType.eq(cookieFlavor)
                        ).list();
                for (Order order : orders) {
                    order.setPickedUpFromCupboard(false);
                    orderDao.update(order);
                }

            }


            List<Order> orderList =
                    orderDao.queryBuilder().where(
                            OrderDao.Properties.OrderCookieType.eq(cookieFlavor),
                            OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(BoothId))

                    ).orderAsc(
                            OrderDao.Properties.OrderDate
                    )
                            .list();

            for (Order order : orderList) {
                if (requestedBoxes >= order.getOrderedBoxes()) {
                    requestedBoxes = requestedBoxes - order.getOrderedBoxes();

                    if (order.getPickedUpFromCupboard()) {
                        orderDao.delete(order);
                    } else {
                        order.setOrderScoutId(-1);
                        orderDao.update(order);
                    }
                } else {
                    if (requestedBoxes > 0) {
                        Order order2 = new Order(
                                null,
                                order.getOrderDate(),
                                order.getOrderScoutId(),
                                order.getOrderCookieType(),
                                false,
                                (order.getOrderedBoxes() - requestedBoxes),
                                false

                        );

                        orderDao.insert(order2);

                        if (order.getPickedUpFromCupboard()) {

                            orderDao.delete(order);
                        } else {
                            order.setOrderScoutId(-1);
                            order.setOrderedBoxes(order.getOrderedBoxes() - order2.getOrderedBoxes());
                            orderDao.update(order);
                            orderDao.delete(order);
                        }

                        requestedBoxes = 0;
                    }
                }

            }

        }
    }


    private void performCheckOut() {
        CookieAmountsListInputFragment fragment = (CookieAmountsListInputFragment) getSupportFragmentManager().findFragmentByTag(fragName);
        Calendar c = Calendar.getInstance();
        if (fragment != null) {
            for (String cookieType : Constants.CookieTypes) {
                int amt = Integer.valueOf(fragment.valuesMap().get(cookieType));
                CookieTransactions cookieTransactions = new CookieTransactions(null, -1L, BoothId, cookieType, amt * -1, c.getTime(), 0.0);
                Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);
                List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                        .where(OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(BoothId)))
                        .list();
                Main.daoSession.getOrderDao().deleteInTx(orderList);
            }
        }


    }


    @Override
    public HashMap<String, String> getValMap() {

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(BoothId)),
                            OrderDao.Properties.OrderCookieType.eq(cookieType))
                    .list();
            if (!orderList.isEmpty()) {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                }
            }
            if (!((ICookieActionFragment) getSupportFragmentManager().findFragmentByTag(fragName)).isRefresh()) {

                valuesMap.put(cookieType, String.valueOf(amountExpected));

            }
        }
        return valuesMap;
    }

}
