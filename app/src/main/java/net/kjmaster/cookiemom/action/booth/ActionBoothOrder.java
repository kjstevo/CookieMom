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

package net.kjmaster.cookiemom.action.booth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.action.ActionContentCard;
import net.kjmaster.cookiemom.booth.order.BoothOrderActivity_;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothDao;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;

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
        return !boothList.isEmpty();

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
        //net.kjmaster.net.kjmaster.cookiemom.action.booth.ActionBoothOrder.setupInnerViewElements returns void
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
