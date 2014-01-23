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

package net.kjmaster.cookiemom.cupboard;

import android.annotation.SuppressLint;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderCookieType;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderDate;
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


    private final HashMap<String, String> valMap = new HashMap<String, String>();
    @Pref
    ISettings_ iSettings;

    @AfterViews
    void afterViewFrag() {

        String fragName = getString(R.string.internal_add_cupboard_order);
        String orderTitle = getString(R.string.order);
        boolean autoFill = iSettings.useAutoFillCases().get();

        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder()
                        .hideCases(false)
                        .showInventory(true)
                        .showExpected(true)
                        .autoFill(autoFill)
                        .isEditable(this.isEditable()).build(),
                fragName);

        createActionMode(orderTitle);

    }


    @Override
    protected boolean isEditable() {
        return true;
    }


    @Override
    public HashMap<String, String> getValMap() {

        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderedFromCupboard.eq(false),
                            OrderCookieType.eq(cookieType),
                            PickedUpFromCupboard.eq(false),
                            OrderedBoxes.gt(0)).list();
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


    @Override
    protected void saveData() {
        Calendar c = Calendar.getInstance();

        OrderDao dao = Main.daoSession.getOrderDao();

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            //calculate the pending order total for each cookie
            String cookieFlavor = Constants.CookieTypes[i];
            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));

            List<Order> orders = dao.queryBuilder()
                    .where(
                            OrderCookieType.eq(cookieFlavor),
                            OrderedFromCupboard.eq(false))
                    .orderAsc(OrderDate)
                    .list();

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
                                -1L,
                                cookieFlavor,
                                Boolean.TRUE,
                                totalBoxes, false
                        ));
            }
        }
    }


}










