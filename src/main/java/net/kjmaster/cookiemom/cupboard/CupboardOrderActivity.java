/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.kjmaster.cookiemom.cupboard;

import android.annotation.SuppressLint;
import android.view.ActionMode;
import android.view.MenuItem;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderCookieType;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderDate;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderScoutId;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderedBoxes;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderedFromCupboard;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.PickedUpFromCupboard;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class CupboardOrderActivity extends CookieActionActivity {
    boolean isByScout = false;
    Scout thisScout;
    List<Scout> scoutList = Main.daoSession.getScoutDao().loadAll();
    private final HashMap<String, String> valMap = new HashMap<String, String>();
    @Pref
    ISettings_ iSettings;

    @AfterViews
    void afterViewFrag() {

        String fragName = getString(R.string.internal_add_cupboard_order);
        String orderTitle = getString(R.string.order);
        boolean autoFill = iSettings.useAutoFillCases().get();
        if (scoutList == null) {
            scoutList = Main.daoSession.getScoutDao().loadAll();
        }
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder()
                        .hideCases(false)
                        .showInventory(true)
                        .showExpected(true)
                        .autoFill(autoFill)
                        .isEditable(this.isEditable()).build(),
                fragName);
        if (isByScout) {
            if (thisScout != null) {
                boolean doNext = false;
                for (Scout scout : scoutList) {
                    if (doNext) {
                        thisScout = scout;
                        doNext = false;
                    } else {
                        if (thisScout.equals(scout)) doNext = true;

                    }
                }

            } else {
                thisScout = scoutList.get(0);
            }
            createActionMode(thisScout.getScoutName(), R.menu.cookie_order_by_scout);
        } else {
            createActionMode(orderTitle, R.menu.cookie_order);

        }
    }


    @Override
    protected boolean isEditable() {
        return true;
    }


    @Override
    public HashMap<String, String> getValMap() {
        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList;
            if (isByScout) {
                orderList = getOrdersByScout(cookieType, thisScout);

            } else {

                orderList = getOrders(cookieType);
            }
            int totalOrdered = 0;
            if (orderList != null) {
                for (Order order : orderList) {
                    totalOrdered += order.getOrderedBoxes();
                }


            }
            valMap.put(cookieType, String.valueOf(totalOrdered));

        }
        return this.valMap;
    }

    private List<Order> getOrdersByScout(String cookieType, Scout thisScout) {
        return Main.daoSession.getOrderDao().queryBuilder()
                .where(OrderedFromCupboard.eq(false),
                        OrderCookieType.eq(cookieType),
                        PickedUpFromCupboard.eq(false),
                        OrderScoutId.eq(thisScout.getId()),
                        OrderedBoxes.gt(0)).list();
    }

    private List<Order> getOrders(String cookieType) {
        return Main.daoSession.getOrderDao().queryBuilder()
                .where(OrderedFromCupboard.eq(false),
                        OrderCookieType.eq(cookieType),
                        PickedUpFromCupboard.eq(false),
                        OrderedBoxes.gt(0)).list();
    }


    @Override
    protected void saveData() {
        Calendar c = Calendar.getInstance();

        OrderDao dao = Main.daoSession.getOrderDao();

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            //calculate the pending order total for each cookie
            String cookieFlavor = Constants.CookieTypes[i];
            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));
            List<Order> orders;
            long scoutId = -1;
            if (isByScout) {
                scoutId = thisScout.getId();
                orders = getFinalOrdersByScout(cookieFlavor, thisScout);
            } else {
                orders = getFinalOrders(cookieFlavor);
            }

            for (Order order : orders) {
                //check if we ordered enough boxes and mark it as ordered
                if ((totalBoxes >= order.getOrderedBoxes())) {
                    order.setOrderedFromCupboard(true);
                    totalBoxes = totalBoxes - order.getOrderedBoxes();
                    dao.update(order);

                } else {
                    //otherwise split the order marking only those ordered
                    Integer newTotal = order.getOrderedBoxes() - totalBoxes;

                    dao.insert(
                            new Order(
                                    null,
                                    order.getOrderDate(),
                                    order.getOrderScoutId(),
                                    cookieFlavor,
                                    true,
                                    totalBoxes, false
                            ));

                    order.setOrderedBoxes(newTotal);

                    dao.update(order);

                    totalBoxes = 0;
                }
            }
//Add orders with no scout or booth attached if the value
// ordered exceeds current pending order requests
            if (totalBoxes > 0) {

                dao.insert(
                        new Order(
                                null,
                                c.getTime(),
                                scoutId,
                                cookieFlavor,
                                Boolean.TRUE,
                                totalBoxes, false
                        ));
            }
        }
        if (!isByScout) {
            super.isFinished = true;
        }
    }

    private List<Order> getFinalOrdersByScout(String cookieFlavor, Scout thisScout) {
        OrderDao dao = orderDao;
        return dao.queryBuilder()
                .where(
                        OrderCookieType.eq(cookieFlavor),
                        OrderScoutId.eq(thisScout.getId()),
                        OrderedFromCupboard.eq(false))
                .orderAsc(OrderDate)
                .list();
    }

    private List<Order> getFinalOrders(String cookieFlavor) {
        OrderDao dao = orderDao;
        return dao.queryBuilder()
                .where(
                        OrderCookieType.eq(cookieFlavor),
                        OrderedFromCupboard.eq(false))
                .orderAsc(OrderDate)
                .list();
    }


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        if (item.getTitle().equals(getString(R.string.Scouts))) {
            isByScout = true;
            afterViewFrag();
        }
        if (item.getTitle().equals(getString(R.string.next))) {
            saveData();
            if (scoutList.get(scoutList.size() - 1).equals(thisScout)) {
                super.isFinished = true;
            } else {
                afterViewFrag();
            }
        }
        return super.onActionItemClicked(mode, item);
    }

    @OptionsItem(R.id.menu_switch_scout)
    void orderByScout() {
        isByScout = true;
        afterViewFrag();
    }


    @OptionsItem(R.id.menu_next)
    void onNext() {
        saveData();
        afterViewFrag();
    }

}










