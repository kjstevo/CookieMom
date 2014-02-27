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
import com.googlecode.androidannotations.annotations.res.StringRes;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/10/13
 * Time: 8:40 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class BoothCheckInActivity extends CookieActionActivity {


    @StringRes()
    String checkin,booth_checkin_order;

    @Extra
    long BoothId;


    @AfterViews
    void afterViewFrag() {
        replaceFrag(
                createFragmentTransaction(booth_checkin_order),
                CookieAmountsListInputFragment_.builder()
                        .hideCases(false)
                        .showExpected(false)
                        .showInventory(false)
                        .isEditable(this.isEditable()).build(),
                booth_checkin_order);

        createActionMode(checkin);
    }
    private void performCheckIn() {
        CookieAmountsListInputFragment fragment = (CookieAmountsListInputFragment)getSupportFragmentManager().findFragmentByTag(booth_checkin_order);
        Calendar c = Calendar.getInstance();
        if (fragment != null) {
            for (String cookieType : Constants.CookieTypes) {
                int amt = Integer.valueOf(fragment.valuesMap().get(cookieType));
                CookieTransactions cookieTransactions = new CookieTransactions(null, -1L, BoothId, cookieType, amt, c.getTime(), 0.0);
                Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);
            }
        }


    }

    @Override
    protected boolean isEditable() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    protected void saveData() {
        performCheckIn();
    }

    @Override
    protected ICookieActionFragment getFragment() {
        return (ICookieActionFragment) getFragmentManager().findFragmentByTag(booth_checkin_order);
    }



}
