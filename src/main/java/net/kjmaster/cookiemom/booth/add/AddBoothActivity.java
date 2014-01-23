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

package net.kjmaster.cookiemom.booth.add;

//~--- non-JDK imports --------------------------------------------------------

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/28/13
 * Time: 1:26 AM
 */
@SuppressLint("Registered")
@EActivity(R.layout.booth_add_booth_layout)
public class AddBoothActivity extends FragmentActivity implements ActionMode.Callback, ISimpleDialogListener {

    @FragmentByTag("add_booth")
    AddBoothDialogFragment addBoothDialogFragment;


    @AfterViews
    void afterViews() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("add_booth");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.replace(R.id.content, AddBoothDialogFragment_.builder().build(), "add_booth");

        ActionMode actionMode = startActionMode(this);

        actionMode.setTitle("Add Booth");

        int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");

        View doneButton = this.findViewById(doneButtonId);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScoutData();
                finish();
            }
        });

        ft.commit();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();

        inflater.inflate(R.menu.add_scout, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;    // To change body of implemented methods use File | Settings | File Templates.
    }

    private void saveScoutData() {
        AddBoothDialogFragment boothDialogFragment = (AddBoothDialogFragment) getSupportFragmentManager().findFragmentByTag(
                "add_booth");
        String boothLocation;
        String boothAddress;
        String boothDate;
        try {
            boothLocation = boothDialogFragment.editText.getText().toString();
            boothAddress = boothDialogFragment.addressText.getText().toString();
            boothDate = boothDialogFragment.hiddenDateTime.getText().toString();
            setResult(Constants.ADD_BOOTH_RESULT_OK, getIntent()
                    .putExtra("add_booth", boothLocation)
                    .putExtra("address", boothAddress)
                    .putExtra("booth_date", Long.valueOf(boothDate))
            );
        } catch (Exception e) {
            Log.e(getApplicationInfo().name, "error in net.kjmaster.cookiemom.booth.add.AddBoothActivity.saveScoutData");

        }

    }


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        finish();
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
