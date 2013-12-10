package net.kjmaster.cookiemom.action;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.scout.ScoutPickupDialog;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;
import net.kmaster.cookiemom.dao.Scout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:59 PM
 */

public class ActionScoutPickup extends ActionContentCard {
    private final FragmentActivity mActivity;

    private final Fragment mFragment;


    public ActionScoutPickup(FragmentActivity mActivity, Fragment fragment) {
        super(mActivity);
        this.mActivity = mActivity;
        this.mFragment = fragment;

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.
        final ListView listView = (ListView) parent.findViewById(R.id.action_list);
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Scout scout = (Scout) listView.getAdapter().getItem(i);
                    ScoutPickupDialog scoutPickupDialog = new ScoutPickupDialog();
                    scoutPickupDialog.ScoutPickupDialog(scout, mActivity, mFragment);


                }
            });
        }
    }


    @Override
    public Boolean isCardVisible() {
        long cnt = Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderDao.Properties.PickedUpFromCupboard.eq(true),
                        OrderDao.Properties.OrderedBoxes.notEq(0),
                        OrderDao.Properties.OrderScoutId.gt(-1)).count();
        return cnt > 0;
    }

    @Override
    public List<Scout> getActionList() {
        List<Scout> stringList = new ArrayList<Scout>();
        List<Order> list = Main.daoSession.getOrderDao().queryBuilder().where(OrderDao.Properties.PickedUpFromCupboard.eq(true), OrderDao.Properties.OrderScoutId.ge(0)).orderAsc(OrderDao.Properties.OrderScoutId).list();
        long lastId = -1;
        for (Order order : list) {
            if (order.getOrderScoutId() != lastId) {
                lastId = order.getOrderScoutId();
                stringList.add(Main.daoSession.getScoutDao().load(lastId));

            }
        }
        return stringList;
    }

    @Override
    public String getActionTitle() {
        return mContext.getString(R.string.cookies_ready_for_pickup_by);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeaderText() {
        return getContext().getString(R.string.Scouts);
    }


}
