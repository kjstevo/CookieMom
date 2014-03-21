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

package net.kjmaster.cookiemom.scout.add;

//~--- non-JDK imports --------------------------------------------------------

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ISettings_;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/28/13
 * Time: 1:26 AM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_add_scout_layout)
public class AddScoutActivity extends CookieActionActivity {

    @FragmentByTag("add_scout")
    AddScoutDialogFragment addScoutDialogFragment;
    @Pref
    ISettings_ iSettings;
    private String fragTag;
    private boolean isScout = true;


    @AfterViews
    void afterViews() {
        fragTag = getString(R.string.add_scout);
        if (Constants.getADD_SCOUT().equals(getString(R.string.add_scout))) {
            isScout = true;
            replaceFrag(createFragmentTransaction(fragTag), AddScoutDialogFragment_.builder().build(), fragTag);
        } else {
            isScout = false;
            replaceFrag(createFragmentTransaction(fragTag), AddCustomerDialogFragment_.builder().build(), fragTag);
        }

        createActionMode(net.kjmaster.cookiemom.global.Constants.ADD_SCOUT);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {


        if (isScout) {
            AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
            if (fragment != null) {
                fragment.editText.setText(savedInstanceState.getString("scout_name"));
            }
        } else {
            AddCustomerDialogFragment fragment = (AddCustomerDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
            if (fragment != null) {
                fragment.editText.setText(savedInstanceState.getString("scout_name"));
            }

        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void saveData() {
        if (isScout) {
            Main.daoSession.getScoutDao().insert(new Scout(null, getFragment().valuesMap().get(fragTag)));
        } else {
            Main.daoSession.getScoutDao().insert(new Scout(null, getFragment().valuesMap().get(fragTag)
                    + "  "
                    + getFragment().valuesMap().get(getString(R.string.contact_info))));
        }
        setResult(RESULT_OK);
        // ((ScoutFragment)getSupportFragmentManager().findFragmentByTag("Scouts")).refreshView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //net.kjmaster.net.kjmaster.cookiemom.scout.add.AddScoutActivity.onSaveInstanceState returns void
        if (isScout) {
            AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
            if (fragment != null) {
                outState.putString("scout_name", fragment.editText.getText().toString());
            }
        } else {
            AddCustomerDialogFragment fragment = (AddCustomerDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
            if (fragment != null) {
                outState.putString("scout_name", fragment.editText.getText().toString());
            }
        }

        //12/9/13
        super.onSaveInstanceState(outState);
    }
}


