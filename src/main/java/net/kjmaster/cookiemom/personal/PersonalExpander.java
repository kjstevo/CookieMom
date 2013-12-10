package net.kjmaster.cookiemom.personal;

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
import net.kmaster.cookiemom.dao.PersonalOrdersDao;
import net.kmaster.cookiemom.dao.PersonalOrders;
import net.kmaster.cookiemom.dao.PersonalOrdersDao;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 3:01 AM
 */
public class PersonalExpander extends CardExpand {
    private final String mCustomer;

    public PersonalExpander(Context context, String customer) {
        this(context, R.layout.scout_expander, customer);

    }

    public PersonalExpander(Context context, int innerLayout, String customer) {
        super(context, innerLayout);
        mCustomer = customer;

        //  mContext=context;

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.

        mTableView = (TableLayout) parent.findViewById(R.id.scout_tableview);
        if (mTableView != null) {
            loadData(mCustomer, parent.getContext());
        }

    }

    private void loadData(String customer, Context context) {
        PersonalOrdersDao dao = Main.daoSession.getPersonalOrdersDao();
        mTableView.setStretchAllColumns(true);
        mTableView.removeAllViews();
        List<PersonalOrders>cashTrans=dao.queryBuilder().where(PersonalOrdersDao.Properties.PersonalCustomer.eq(customer), PersonalOrdersDao.Properties.PersonalCash.notEq(0)).orderAsc(PersonalOrdersDao.Properties.PersonalDate).list();
        for (int i = 0; i < cashTrans.size(); i++) {

        PersonalOrders tran = cashTrans.get(i);

            TableRow tableRow = new TableRow(context);

            TextView textView = new TextView(context);
            String date = new SimpleDateFormat("MMM-dd-yyyy").format(tran.getPersonalDate());
            if (date != null) {
                textView.setText(date);
            }
            //textView.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(tran.getTransDate()));

            TextView textView1 = new TextView(context);
            textView1.setText(tran.getPersonalCookieType());

            TextView textView2 = new TextView(context);
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            textView2.setText(fmt.format(tran.getPersonalCash()));
            tableRow.addView(textView);
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            mTableView.addView(tableRow);
        }
        List<PersonalOrders> trans = dao.queryBuilder().where(PersonalOrdersDao.Properties.PersonalCustomer.eq(customer), PersonalOrdersDao.Properties.PersonalBoxes.notEq(0)).orderAsc(PersonalOrdersDao.Properties.PersonalDate).list();


        for (int i = 0; i < trans.size(); i++) {

         PersonalOrders tran = trans.get(i);

            TableRow tableRow = new TableRow(context);

            TextView textView = new TextView(context);
            String date = new SimpleDateFormat("MMM-dd-yyyy").format(tran.getPersonalDate());
            if (date != null) {
                textView.setText(date);
            }
            //textView.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(tran.getTransDate()));

            TextView textView1 = new TextView(context);
            textView1.setText(tran.getPersonalCookieType());

            TextView textView2 = new TextView(context);
            if (tran.getPersonalDelivered()){
                textView2.setText(String.valueOf(tran.getPersonalBoxes()));
            } else {
                textView2.setText(String.valueOf(tran.getPersonalBoxes())+" (ord)");
            }


            tableRow.addView(textView);
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            mTableView.addView(tableRow);
        }
        super.setTitle("Transactions");


    }


    private TableLayout mTableView;
}
