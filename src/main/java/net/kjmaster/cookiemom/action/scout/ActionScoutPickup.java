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

package net.kjmaster.cookiemom.action.scout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.action.ActionContentCard;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupDialog;

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
        return Constants.getSCOUT();
    }


}
