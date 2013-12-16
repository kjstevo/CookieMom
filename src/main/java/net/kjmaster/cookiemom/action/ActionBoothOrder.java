package net.kjmaster.cookiemom.action;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.order.BoothOrderActivity_;
import net.kjmaster.cookiemom.global.Constants;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.BoothDao;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/15/13
 * Time: 3:08 PM
 */
public class ActionBoothOrder extends ActionContentCard {
    private final List<Booth> boothList = new ArrayList<Booth>();

    public ActionBoothOrder(Context context, int layout) {
        super(context, layout);
    }

    public ActionBoothOrder(Context context) {
        super(context);
    }

    @Override
    public Boolean isCardVisible() {
        OrderDao orderDao = Main.daoSession.getOrderDao();
        BoothDao boothDao = Main.daoSession.getBoothDao();
        CookieTransactionsDao cookieDao = Main.daoSession.getCookieTransactionsDao();
        Date date = new Date(Calendar.getInstance().getTimeInMillis() + (1000 * 60 * 60 * 24 * 7));
        List<Booth> booths = boothDao.queryBuilder()

                .where(BoothDao.Properties.BoothDate.lt(date)).list();

        if (booths != null) {
            for (Booth booth : booths) {
                long orderCount = orderDao.queryBuilder()
                        .where(
                                OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(booth.getId())))
                        .count();
                long transCount = cookieDao.queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.TransBoothId.eq(booth.getId()))
                        .count();

                if (orderCount + transCount == 0) {
                    boothList.add(booth);
                }

            }
        }
        if (boothList.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public List<?> getActionList() {
        return boothList;
    }

    @Override
    public String getActionTitle() {
        return getContext().getString(R.string.booths_need_ordering);

    }

    @Override
    public String getHeaderText() {
        return getContext().getString(R.string.booths);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //net.kjmaster.cookiemom.action.ActionBoothOrder.setupInnerViewElements returns void
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.
        final ListView listView = (ListView) parent.findViewById(R.id.action_list);
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Booth booth = (Booth) listView.getAdapter().getItem(i);
                    if (booth != null) {
                        BoothOrderActivity_
                                .intent(getContext())
                                .requestCode(Constants.BOOTH_ORDER)
                                .boothId(booth.getId())
                                .startForResult(Constants.BOOTH_ORDER);

                    }
                }
            });
        }
    }
}
