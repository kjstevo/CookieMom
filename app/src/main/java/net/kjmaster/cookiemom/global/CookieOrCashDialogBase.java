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

package net.kjmaster.cookiemom.global;

import android.support.v4.app.FragmentActivity;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;

import java.util.Calendar;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/13/13
 * Time: 8:10 PM
 */
public abstract class CookieOrCashDialogBase extends CookieActionActivity implements ISimpleDialogListener {

    private Long mId;

    protected void CreateDialog(Long id, FragmentActivity activity, String title) {

        onPositiveButtonClicked(id.intValue());
        mId = id;
//        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
//                .setTitle(title)
//                .setMessage("Is this for money or cookies?")
//                .setPositiveButtonText("Money")
//                .setNegativeButtonText("Cookies")
//                .setCancelable(true)
//                .setRequestCode(id.intValue())
//                .setTag(id.toString())
//                .show();
    }

    protected abstract void createActionFragment();

    protected abstract boolean isEditableValue();

    protected abstract void saveForemData();

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        final NumberPickerBuilder numberPickerBuilder =
                new NumberPickerBuilder()
                        .setFragmentManager(
                                getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);

        numberPickerBuilder.setReference(-1);
        numberPickerBuilder.show();
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        createActionFragment();
    }

    @Override
    protected boolean isEditable() {
        return isEditableValue();
    }

    @Override
    protected void saveData() {
        saveForemData();

    }

    protected abstract boolean isNegative();

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {

        if (reference == -1) {
            CookieTransactions cookieTransactions;
            if (isNegative()) {

                cookieTransactions = new CookieTransactions(null, -1L, mId, "", 0, Calendar.getInstance().getTime(), fullNumber * -1);
            } else {
                cookieTransactions = new CookieTransactions(null, -1L, mId, "", 0, Calendar.getInstance().getTime(), fullNumber);
            }

            Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);

            finish();

        } else {
            super.onDialogNumberSet(reference, number, decimal, isNegative, fullNumber);
        }
    }
}


