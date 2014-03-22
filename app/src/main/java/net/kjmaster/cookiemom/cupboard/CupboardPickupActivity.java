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

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
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

import de.greenrobot.dao.Property;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */

@EActivity(R.layout.scout_order_layout)
public class CupboardPickupActivity extends CookieActionActivity {

    @Pref
    ISettings_ iSettings;


    @StringRes
    String pickup_order_title,
            pickup_from_cupboard;


    private final HashMap<String, String> valuesMap = new HashMap<String, String>();

    @AfterViews
    void afterViewFrag() {

        replaceFrag(createFragmentTransaction(pickup_from_cupboard),
                CookieAmountsListInputFragment_.builder()
                        .hideCases(false)
                        .isEditable(true)
                        .showInventory(true)
                        .showExpected(true)
                        .autoFill(iSettings.useAutoFillCases().get())
                        .build(),
                pickup_from_cupboard);
        createActionMode(pickup_order_title);
    }

    @Override
    public HashMap<String, String> getValMap() {

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;

            List<Order> orderList = getOrders(cookieType);

            if (orderList.isEmpty()) {
                valuesMap.put(cookieType, String.valueOf(0));
            } else {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                    if (!((ICookieActionFragment) getSupportFragmentManager().findFragmentByTag(pickup_from_cupboard)).isRefresh()) {
                        valuesMap.put(cookieType, String.valueOf(amountExpected));
                    }
                }
            }
        }
        return valuesMap;
    }

    private List<Order> getOrders(String cookieType) {
        Property orderedFromCupboard = OrderDao.Properties.OrderedFromCupboard,
                orderCookieType = OrderDao.Properties.OrderCookieType,
                pickedUpFromCupboard = OrderDao.Properties.PickedUpFromCupboard;

        return Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        orderedFromCupboard
                                .eq(true),
                        orderCookieType
                                .eq(cookieType),
                        pickedUpFromCupboard
                                .eq(false)
                )
                .list();
    }


    @Extra
    boolean isEditable;

    @Override
    protected boolean isEditable() {
        return isEditable;
    }

    @Override
    protected void saveData() {

        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();

        OrderDao orderDao = Main.daoSession.getOrderDao();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {

            String cookieFlavor = Constants.CookieTypes[i];

            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));

            Calendar c = Calendar.getInstance();

            dao.insert(new CookieTransactions(
                    null,
                    -1L,
                    -1L,
                    cookieFlavor,
                    totalBoxes,
                    c.getTime(),
                    (double) 0
            ));

            List<Order> orderList = orderDao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieFlavor),
                            OrderDao.Properties.OrderedFromCupboard.eq(true),
                            OrderDao.Properties.PickedUpFromCupboard.eq(false))
                    .orderAsc(OrderDao.Properties.OrderDate)
                    .list();

            for (Order order : orderList) {
                if (totalBoxes >= order.getOrderedBoxes()) {
                    totalBoxes = totalBoxes - order.getOrderedBoxes();

                    order.setPickedUpFromCupboard(true);
                } else {
                    if (totalBoxes > 0) {
                        orderDao.insert(new Order(
                                null,
                                order.getOrderDate(),
                                order.getOrderScoutId(),
                                order.getOrderCookieType(),
                                true,
                                (order.getOrderedBoxes() - totalBoxes),
                                false

                        ));

                        order.setOrderedBoxes(totalBoxes);
                        order.setPickedUpFromCupboard(true);
                        totalBoxes = 0;
                    }
                }

                orderDao.update(order);

            }
        }

        setResult(RESULT_OK);

    }


}
