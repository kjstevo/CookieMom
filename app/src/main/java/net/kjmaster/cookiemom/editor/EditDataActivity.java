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

package net.kjmaster.cookiemom.editor;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell_Owner on 2/14/14.
 */
@EActivity(R.layout.edit_data_layout)
public class EditDataActivity extends FragmentActivity implements ActionMode.Callback {

    @ViewById
    TableLayout edit_data_table;


    private final ArrayList<CookieTransactions> cookieTransactionsList = new ArrayList<CookieTransactions>();

    private boolean isCancel = false;

    private ActionMode mActionMode = null;
    private final ActionMode.Callback actionCall;
    private CookieTransactionsDao dao;

    public EditDataActivity() {
        actionCall = this;

        dao = Main.daoSession.getCookieTransactionsDao();
    }

    @AfterViews
    void afterViews() {


        populateTable(dao.queryBuilder().orderDesc(CookieTransactionsDao.Properties.TransScoutId, CookieTransactionsDao.Properties.TransDate).list(), this);
    }

    private void populateTable(final List<CookieTransactions> cookieTransactionsList, Context mContext) {
        edit_data_table.setShrinkAllColumns(true);
        for (final CookieTransactions cookieTransactions : cookieTransactionsList) {
            if ((cookieTransactions.getTransBoxes() != 0) || (cookieTransactions.getTransCash() != 0)) {
                TableRow tr = new TableRow(mContext);
                tr.setTag(cookieTransactions);

                TextView textView = new TextView(mContext);
                textView.setText(java.text.DateFormat.getInstance().format(cookieTransactions.getTransDate()));
                textView.setEnabled(false);

                tr.addView(textView);

                TextView textView2 = new TextView(mContext);
                try {
                    if (cookieTransactions.getTransScoutId() < 0) {
                        if (cookieTransactions.getTransBoothId() > 0) {
                            textView2.setText(cookieTransactions.getBooth().getBoothLocation());
                        } else {
                            textView2.setText("Cupboard");
                        }
                    } else {
                        textView2.setText(cookieTransactions.getScout().getScoutName());
                    }
                } catch (Exception e) {
                    textView2.setText("");
                }
                textView2.setEnabled(false);
                tr.addView(textView2);

                TextView textView1 = new TextView(mContext);
                textView1.setText(cookieTransactions.getCookieType());
                textView1.setEnabled(false);
                tr.addView(textView1);

                EditText textView3 = new EditText(mContext);
                textView3.setText(cookieTransactions.getTransBoxes().toString());
                textView3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (mActionMode == null) {
                            startActionMode(actionCall);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            cookieTransactions.setTransBoxes(Integer.valueOf(editable.toString()));

                            cookieTransactionsList.add(cookieTransactions);
                            //Main.daoSession.getCookieTransactionsDao().update(cookieTransactions);
                        } catch (Exception ignored) {
                        }
                    }
                });
                tr.addView(textView3);

                EditText textView4 = new EditText(mContext);
                textView4.setText(NumberFormat.getCurrencyInstance().format(cookieTransactions.getTransCash().floatValue()));
                textView4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (mActionMode == null) {
                            startActionMode(actionCall);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {

                            cookieTransactions.setTransCash(Double.valueOf(editable.toString()));
                            cookieTransactionsList.add(cookieTransactions);

                        } catch (Exception ignored) {
                        }

                    }
                });
                tr.addView(textView4);

                edit_data_table.addView(tr);
            }

        }
    }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();

        if (inflater != null) {
            inflater.inflate(R.menu.add_scout, menu);
        }
        mActionMode = actionMode;
        isCancel = false;
        return true;

    }


    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getTitle().equals(getString(R.string.cancel))) {
            cookieTransactionsList.clear();
            actionMode.finish();
            afterViews();
            return true;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (cookieTransactionsList.size() > 0) {

            dao.updateInTx(cookieTransactionsList);
        }
        mActionMode = null;
    }
}

