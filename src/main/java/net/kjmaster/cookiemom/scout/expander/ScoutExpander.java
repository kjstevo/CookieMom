package net.kjmaster.cookiemom.scout.expander;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.CardExpand;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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
