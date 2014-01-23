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

package net.kjmaster.cookiemom.scout.edit;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
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

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 1/1/14
 * Time: 9:45 PM
 */
@EActivity(R.layout.scout_order_layout)
public class ScoutEditActivity extends CookieActionActivity {

    @Pref
    ISettings_ iSettings;
    private OrderDao dao = Main.daoSession.getOrderDao();
    private HashMap<String, String> valuesMap;

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Extra
    long ScoutId;

    @Extra
    int requestCode;

    @StringRes(R.string.edit_order)
    String fragTag;

    @StringRes(R.string.edit_order_title)
    String fragTitle;

    @AfterViews
    void AfterViews() {
        createFrag();
        valuesMap = new HashMap<String, String>();
        for (String cookie : Constants.CookieTypes) {
            valuesMap.put(cookie, "0");
        }
    }

    private void createFrag() {
        replaceFrag(
                createFragmentTransaction(
                        fragTag), CookieAmountsListInputFragment_
                .builder()
                .hideCases(true)
                .showExpected(true)
                .showInventory(false)
                .isEditable(true)
                .build(),
                fragTag
        );
        createActionMode(fragTitle);
    }

    @Override
    protected void saveData() {
        List<Order> list = dao.queryBuilder()
                .where(OrderDao.Properties.OrderScoutId.eq(ScoutId),
                        OrderDao.Properties.OrderedFromCupboard.eq(false))
                .list();
        dao.deleteInTx(list);

        OrderDao dao = Main.daoSession.getOrderDao();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            Integer intVal = Integer.valueOf(getFragment()
                    .valuesMap()
                    .get(Constants.CookieTypes[i])
            );

            if (intVal > 0) {
                dao.insert(new Order(
                        null,
                        c.getTime(),
                        ScoutId,
                        Constants.CookieTypes[i],
                        false,
                        intVal,
                        false));
            }
        }
    }


    @Override
    public HashMap<String, String> getValMap() {

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;
            List<Order> orderList = dao.queryBuilder()
                    .where(OrderDao.Properties.OrderScoutId.eq(ScoutId),
                            OrderDao.Properties.OrderedFromCupboard.eq(false),
                            OrderDao.Properties.OrderCookieType.eq(cookieType))
                    .list();
            if (!orderList.isEmpty()) {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                }
            }
            if (!((ICookieActionFragment) getSupportFragmentManager().findFragmentByTag(fragTag)).isRefresh()) {

                valuesMap.put(cookieType, String.valueOf(amountExpected));

            }
        }
        return valuesMap;
    }

}


