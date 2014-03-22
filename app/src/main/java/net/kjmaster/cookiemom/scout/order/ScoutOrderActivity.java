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

package net.kjmaster.cookiemom.scout.order;

import android.annotation.SuppressLint;

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

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class ScoutOrderActivity extends CookieActionActivity {

    @Extra
    long scoutId;

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
        replaceFrag(createFragmentTransaction(fragTag),
                CookieAmountsListInputFragment_.builder()
                        .isEditable(true)
                        .hideCases(true)
                        .showExpected(false)
                        .showInventory(false)
                        .build(), fragTag);
        createActionMode(fragTitle);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }



    protected void saveData() {
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
                        scoutId,
                        Constants.CookieTypes[i],
                        false,
                        intVal,
                        false));
            }
        }
    }


}