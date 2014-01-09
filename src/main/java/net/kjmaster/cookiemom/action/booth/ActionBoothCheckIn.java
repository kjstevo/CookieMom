package net.kjmaster.cookiemom.action.booth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.action.ActionContentCard;
import net.kjmaster.cookiemom.booth.checking.BoothCheckInActivity_;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothDao;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.global.Constants;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/15/13
 * Time: 3:12 PM
 */
public class ActionBoothCheckIn extends ActionContentCard {
    private final Calendar c = Calendar.getInstance();
    private final Date date = new Date(c.getTimeInMillis() * 1000 * 60 * 60 * 24);
    private final List<Booth> boothList = new ArrayList<Booth>();

    public ActionBoothCheckIn(Context context, int layout) {
        super(context, layout);
    }

    public ActionBoothCheckIn(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.
        final ListView listView = (ListView) parent.findViewById(R.id.action_list);
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Booth booth = (Booth) listView.getAdapter().getItem(i);
                    if (booth != null) {
                        BoothCheckInActivity_
                                .intent(getContext())
                                .BoothId(booth.getId())
                                .startForResult(Constants.BOOTH_ORDER);
                    }
                }
            });
        }
    }


    @Override
    public Boolean isCardVisible() {


        List<Booth> booths = Main.daoSession.getBoothDao().queryBuilder()
                .where(
                        BoothDao.Properties.BoothDate.lt(date),
                        BoothDao.Properties.BoothDate.gt(c.getTime()))
                .list();
        if (booths != null) {
            for (Booth booth : booths) {

                List<CookieTransactions> transactionsList = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()))
                        .list();
                Integer boxes = 0;
                double cash = 0.0;
                for (CookieTransactions transaction : transactionsList) {
                    boxes += transaction.getTransBoxes();
                    cash += transaction.getTransCash();
                }

                if (cash + boxes < 0) {
                    boothList.add(booth);

                }
            }
        }
        return !boothList.isEmpty();

    }


    @Override
    public List<Booth> getActionList() {
        return getBooths();

    }


    private List<Booth> getBooths() {
        return boothList;
    }

    @Override
    public String getActionTitle() {
        return getContext().getString(R.string.booths_req_check_in);
    }

    @Override
    public String getHeaderText() {
        return getContext().getString(R.string.booths);
    }
}
