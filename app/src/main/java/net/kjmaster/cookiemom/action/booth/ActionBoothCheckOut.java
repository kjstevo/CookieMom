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
import net.kjmaster.cookiemom.booth.checking.BoothCheckOutActivity_;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderScoutId;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.PickedUpFromCupboard;
import static net.kjmaster.cookiemom.global.Constants.BOOTH_ORDER;
import static net.kjmaster.cookiemom.global.Constants.CalculateNegativeBoothId;

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
        addCheckOutListener(listView);
    }

    private void addCheckOutListener(final ListView listView) {
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Booth booth = (Booth) listView.getAdapter().getItem(i);
                    if (booth != null) {
                        BoothCheckOutActivity_
                                .intent(getContext())
                                .BoothId(booth.getId())
                                .startForResult(BOOTH_ORDER);
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
                long orderCount = Main.daoSession.getOrderDao().queryBuilder()
                        .where(
                                OrderScoutId.eq(CalculateNegativeBoothId(booth.getId())),
                                PickedUpFromCupboard.eq(true))
                        .count();
                if (orderCount > 0) {
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
        return getContext().getString(R.string.booths_req_check_out);
    }

    @Override
    public String getHeaderText() {
        return getContext().getString(R.string.booths);
    }
}
