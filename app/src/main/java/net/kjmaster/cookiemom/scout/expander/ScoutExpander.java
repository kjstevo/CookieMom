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

package net.kjmaster.cookiemom.scout.expander;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 3:01 AM
 */
public class ScoutExpander extends CardExpand {
    public ScoutExpander(Context context, long scoutId) {
        this(context, R.layout.scout_expander, scoutId);

    }

    public ScoutExpander(Context context, int innerLayout, long scoutId) {
        super(context, innerLayout);
        mScoutId = scoutId;

        //  mContext=context;

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.

        mTableView = (TableLayout) parent.findViewById(R.id.scout_tableview);
        if (mTableView != null) {
            loadData(mScoutId, parent.getContext());
        }

    }

    private void loadData(long mScoutId, Context context) {
        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();
        mTableView.setStretchAllColumns(true);
        mTableView.removeAllViews();
        List<CookieTransactions> cashTrans = dao.queryBuilder().where(CookieTransactionsDao.Properties.TransScoutId.eq(mScoutId), CookieTransactionsDao.Properties.TransCash.notEq(0)).orderAsc(CookieTransactionsDao.Properties.TransDate).list();
        for (CookieTransactions tran : cashTrans) {

            TableRow tableRow = new TableRow(context);

            TextView textView = new TextView(context);
            String date = new SimpleDateFormat("MMM-dd-yyyy").format(tran.getTransDate());
            if (date != null) {
                textView.setText(date);
            }
            //textView.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(tran.getTransDate()));

            TextView textView1 = new TextView(context);
            textView1.setText(tran.getCookieType());

            TextView textView2 = new TextView(context);
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            fmt.setMaximumFractionDigits(0);
            fmt.setMinimumFractionDigits(0);
            textView2.setText(fmt.format(tran.getTransCash()));
            tableRow.addView(textView);
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            mTableView.addView(tableRow);
        }
        List<CookieTransactions> trans = dao.queryBuilder().where(CookieTransactionsDao.Properties.TransScoutId.eq(mScoutId), CookieTransactionsDao.Properties.TransBoxes.notEq(0)).orderAsc(CookieTransactionsDao.Properties.TransDate).list();


        for (CookieTransactions tran : trans) {

            TableRow tableRow = new TableRow(context);

            TextView textView = new TextView(context);
            String date = new SimpleDateFormat("MMM-dd-yyyy").format(tran.getTransDate());
            if (date != null) {
                textView.setText(date);
            }
            //textView.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(tran.getTransDate()));

            TextView textView1 = new TextView(context);
            textView1.setText(tran.getCookieType());

            TextView textView2 = new TextView(context);
            textView2.setText(String.valueOf(tran.getTransBoxes() * -1));

            tableRow.addView(textView);
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            mTableView.addView(tableRow);
        }
        super.setTitle("Transactions");


    }

    private final long mScoutId;
    private TableLayout mTableView;
}
