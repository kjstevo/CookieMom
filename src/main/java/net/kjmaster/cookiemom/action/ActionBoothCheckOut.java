package net.kjmaster.cookiemom.action;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.BoothCheckOutActivity_;
import net.kjmaster.cookiemom.global.Constants;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.BoothDao;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;

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
public class ActionBoothCheckOut extends ActionContentCard {
    private final Calendar c = Calendar.getInstance();
    private final Date date = new Date(c.getTimeInMillis() + (1000 * 60 * 60 * 24));
    private final List<Booth> boothList = new ArrayList<Booth>();

    public ActionBoothCheckOut(Context context, int layout) {
        super(context, layout);
    }

    public ActionBoothCheckOut(Context context) {
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
                        BoothCheckOutActivity_
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


        List<Booth> cnt = Main.daoSession.getBoothDao().queryBuilder()
                .where(
                        BoothDao.Properties.BoothDate.lt(date),
                        BoothDao.Properties.BoothDate.gt(c.getTime()))
                .list();
        if (cnt != null) {
            for (Booth booth : cnt) {

                Long cnt2 = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()))
                        .count();
                if (cnt2 == 0) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public List<Booth> getActionList() {
        return getBooths();

    }

    private List<Booth> getBooths() {
        return Main.daoSession.getBoothDao().queryBuilder()
                .where(
                        BoothDao.Properties.BoothDate.lt(date),
                        BoothDao.Properties.BoothDate.gt(c.getTime()))
                .orderAsc()
                .list();
    }

    @Override
    public String getActionTitle() {
        return getContext().getString(R.string.booths_req_check_out);
    }

    @Override
    public String getHeaderText() {
        return getContext().getString(R.string.booths);
    }
}
