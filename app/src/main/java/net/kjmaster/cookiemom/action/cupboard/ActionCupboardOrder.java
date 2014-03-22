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

package net.kjmaster.cookiemom.action.cupboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.action.ActionContentCard;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:59 PM
 */

public class ActionCupboardOrder extends ActionContentCard {

    private final Context mActivity;
    private final String pickupString;
    private final String orderString;


    public ActionCupboardOrder(Context mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
        this.pickupString = mActivity.getString(R.string.pickup);
        this.orderString = mActivity.getString(R.string.order);


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.

        final ListView listView = (ListView) parent.findViewById(R.id.action_list);

        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView textView = (TextView) view;
                    if (textView != null) {


                        if (textView.getText().equals(pickupString)) {
                            net.kjmaster.cookiemom.cupboard.CupboardPickupActivity_.intent(mActivity).startForResult(Constants.CUPBOARD_REQUEST);

                        } else {

                            if (textView.getText().equals(orderString)) {
                                net.kjmaster.cookiemom.cupboard.CupboardOrderActivity_.intent(mActivity).startForResult(Constants.PLACE_CUPBOARD_ORDER);
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public Boolean isCardVisible() {
        long cnt = Main.daoSession.getOrderDao().queryBuilder().where(OrderDao.Properties.PickedUpFromCupboard.eq(false)).count();
        return cnt > 0;
    }


    @Override
    public List<String> getActionList() {
        List<String> stringList = new ArrayList<String>();

        if (Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderDao.Properties.OrderedFromCupboard.eq(false),
                        OrderDao.Properties.OrderedBoxes.gt(0)
                )
                .count() > 0) {
            stringList.add(orderString);
        }

        if (Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderDao.Properties.OrderedFromCupboard.eq(true),
                        OrderDao.Properties.PickedUpFromCupboard.eq(false),
                        OrderDao.Properties.OrderedBoxes.gt(0))
                .count() > 0
                ) {
            stringList.add(pickupString);
        }

        return stringList;
    }


    @Override
    public String getActionTitle() {
        return "Cookie Cupboard activities needing action:";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String getHeaderText() {
        return "Cookie Cupboard";
    }


}
