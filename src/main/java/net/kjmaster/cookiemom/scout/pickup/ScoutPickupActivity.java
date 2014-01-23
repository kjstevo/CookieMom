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

package net.kjmaster.cookiemom.scout.pickup;

import android.annotation.SuppressLint;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.dao.OrderDao.Properties;
import static net.kjmaster.cookiemom.global.Constants.CookieTypes;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class ScoutPickupActivity extends CookieActionActivity {

    @Extra
    long ScoutId;

    @Extra
    boolean isEditable;

    @StringRes(R.string.scout_pickup_order_title)
    String scout_pickup_title;

    @StringRes(R.string.scout_pickup_order)
    String scout_pickup_order;

    @StringRes(R.string.pickup_order)
    String pickup_order;


    @StringRes(R.string.cancel)
    String resCancel;
    private final HashMap<String, String> valuesMap = new HashMap<String, String>();
    private final Calendar c = Calendar.getInstance();
    @Pref
    ISettings_ iSettings;
    ;

    @AfterViews
    void afterViewFrag() {

        createFrag();

    }

    private void createFrag() {
        replaceFrag(
                createFragmentTransaction(
                        scout_pickup_order), CookieAmountsListInputFragment_
                .builder()
                .hideCases(false)
                .showInventory(true)
                .showExpected(true)
                .isEditable(this.isEditable)
                .build(),
                scout_pickup_order
        );

        createActionMode(scout_pickup_title);
    }


    private void savePickupData() {
        if (!isEditable) {
            setResult(RESULT_CANCELED);
            finish();
        } else {

            for (String cookieFlavor : CookieTypes) {

                Integer boxesInInventory = getBoxesInInventory(cookieFlavor);

                Integer requestedBoxes = getRequestedBoxes(cookieFlavor);

                if (boxesInInventory > 0) {
                    if (requestedBoxes <= boxesInInventory) {
                        createTrans(cookieFlavor, requestedBoxes);
                    } else {
                        createTrans(cookieFlavor, boxesInInventory);
                        requestedBoxes = boxesInInventory;
                    }
                } else {
                    markOrdersAsPickedUp(cookieFlavor);

                }


                List<Order> orderList = getOrders(cookieFlavor);

                for (Order order : orderList) {
                    if (requestedBoxes >= order.getOrderedBoxes()) {
                        requestedBoxes = requestedBoxes - order.getOrderedBoxes();
                        orderDao.delete(order);
                    } else {
                        if (requestedBoxes > 0) {
                            Order order2 = createOrderFromOrder(requestedBoxes, order);

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
    }


    private Order createOrderFromOrder(Integer requestedBoxes, Order order) {

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
        return order2;
    }

    private List<Order> getOrders(String cookieFlavor) {

        return orderDao.queryBuilder().where(
                Properties.OrderCookieType.eq(cookieFlavor),
                Properties.OrderScoutId.eq(ScoutId)

        ).orderAsc(
                Properties.OrderDate
        )
                .list();
    }

    private void markOrdersAsPickedUp(String cookieFlavor) {

        List<Order> orders = orderDao.queryBuilder()
                .where(
                        Properties.OrderScoutId.eq(ScoutId),
                        Properties.OrderCookieType.eq(cookieFlavor)
                ).list();
        for (Order order : orders) {
            order.setPickedUpFromCupboard(true);
            orderDao.update(order);
        }
    }

    private void createTrans(String cookieFlavor, Integer requestedBoxes) {

        CookieTransactions cookieTransactions = new CookieTransactions(
                null, ScoutId, -1L, cookieFlavor, requestedBoxes * -1,
                c.getTime(),
                (double) 0
        );
        cookieTransactionsDao.insert(cookieTransactions);
    }

    private Integer getRequestedBoxes(String cookieFlavor) {
        return Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));
    }


    @Override
    protected boolean isEditable() {
        return isEditable;
    }


    @Override
    public HashMap<String, String> getValMap() {

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.OrderScoutId.eq(ScoutId),
                            OrderDao.Properties.OrderCookieType.eq(cookieType))
                    .list();
            if (!orderList.isEmpty()) {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                }
            }
            if (!((ICookieActionFragment) getSupportFragmentManager().findFragmentByTag(scout_pickup_order)).isRefresh()) {

                valuesMap.put(cookieType, String.valueOf(amountExpected));

            }
        }
        return valuesMap;
    }


    @Override
    protected void saveData() {

        savePickupData();
    }


}
