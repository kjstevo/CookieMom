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

package net.kjmaster.cookiemom.booth.order;

import android.annotation.SuppressLint;

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
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class BoothOrderActivity extends CookieActionActivity {

    @Extra
    long boothId;

    @Extra
    int requestCode;

    @StringRes(R.string.add_order)
    String fragTag;

    @StringRes(R.string.add_order_title)
    String fragTitle;
    @Pref
    ISettings_ iSettings;

    @AfterViews
    void afterViewFrag() {
        replaceFrag(
                createFragmentTransaction(fragTag),
                CookieAmountsListInputFragment_.builder()
                        .isEditable(true)
                        .hideCases(false)
                        .showInventory(true)
                        .showExpected(false)
                        .build(),
                fragTag
        );

        createActionMode(fragTitle);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }


    protected void saveData() {
        OrderDao dao = Main.daoSession.getOrderDao();

        //Get a list of orders whos cookies are in the inventory awaiting
        //pick up so we dont jack 'em
        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList = dao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieType),
                            OrderDao.Properties.PickedUpFromCupboard.eq(true))
                    .list();

            //add the total orders awaiting pickup from scout
            int orderedBoxesWaiting = 0;
            if (orderList != null) {
                for (Order order : orderList) {
                    orderedBoxesWaiting += order.getOrderedBoxes();
                }
            }
            //get current inventory total
            List<CookieTransactions> cookieTransactionsist = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                    .where(CookieTransactionsDao.Properties.CookieType.eq(cookieType)).list();
            if (cookieTransactionsist != null) {
                int boxes = 0;
                for (CookieTransactions cookieTransactions : cookieTransactionsist) {
                    boxes = boxes + cookieTransactions.getTransBoxes();
                }
                //sub the boxes awaiting p/u
                boxes = boxes - orderedBoxesWaiting;
                if (boxes < 0) boxes = 0;

                // get the current booth order num of boxes
                Integer currentOrder = Integer.valueOf(getFragment()
                        .valuesMap()
                        .get(cookieType)
                );

                //number of boxes not in inv need to be ordered
                //the extra are marked awaiting p/u
                Integer currentShortfall = currentOrder - boxes;

                if (currentShortfall <= 0) {
                    currentShortfall = 0;
                    createOrder(cookieType, currentOrder, true);
                }
                if (currentShortfall > 0) {
                    createOrder(cookieType, currentShortfall, false);
                    if (currentOrder > currentShortfall) {
                        createOrder(cookieType, currentOrder - currentShortfall, true);

                    }
                }

            }
        }
    }


    private void createOrder(String cookieType, Integer amountl, boolean pickedUp) {
        Calendar c = Calendar.getInstance();
        OrderDao dao = Main.daoSession.getOrderDao();

        dao.insert(new Order(
                null,
                c.getTime(),
                (boothId * -1) - 100,
                cookieType,
                pickedUp,
                amountl,
                pickedUp));
    }
}
