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

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

/**
 * Created by Dell_Owner on 2/14/14.
 */
@EActivity(R.layout.edit_data_layout)
public class EditDataActivity extends Activity {

    @ViewById
    TableLayout edit_data_table;

    @AfterViews
    void afterViews() {
        //edit_data_table.setCookieTransactionsList(Main.daoSession.getCookieTransactionsDao().loadAll());
        populateTable(Main.daoSession.getCookieTransactionsDao().queryBuilder().orderDesc(CookieTransactionsDao.Properties.TransScoutId, CookieTransactionsDao.Properties.TransDate).list(), this);
    }

    private void populateTable(List<CookieTransactions> cookieTransactionsList, Context mContext) {
        edit_data_table.setShrinkAllColumns(true);
        for (final CookieTransactions cookieTransactions : cookieTransactionsList) {
            if ((cookieTransactions.getTransBoxes() == 0) && (cookieTransactions.getTransCash() == 0)) {
                continue;
            } else {

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

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            cookieTransactions.setTransBoxes(Integer.valueOf(editable.toString()));
                            Main.daoSession.getCookieTransactionsDao().update(cookieTransactions);
                        } catch (Exception e) {
                        }
                    }
                });
                tr.addView(textView3);

                EditText textView4 = new EditText(mContext);
                textView4.setText(NumberFormat.getCurrencyInstance().format(cookieTransactions.getTransCash().floatValue()));
                textView4.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            cookieTransactions.setTransCash(Double.valueOf(editable.toString()));
                        } catch (Exception e) {
                        }

                    }
                });
                tr.addView(textView4);

                edit_data_table.addView(tr);
            }

        }
    }


}

