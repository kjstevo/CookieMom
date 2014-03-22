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
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothAssignmentsDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.select.SelectScoutListActivity_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:59 PM
 */

public class ActionAssignBooth extends ActionContentCard {
    private final Context mActivity;

    //TODO booth add refresh
    public ActionAssignBooth(Context mActivity) {
        super(mActivity);
        this.mActivity = mActivity;


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
                    SelectScoutListActivity_.intent(mActivity).requestCode(Constants.ASSIGN_SCOUT_REQUEST_CODE).targetId(booth.getId()).startForResult(Constants.ASSIGN_SCOUT_REQUEST_CODE);
                }
            });
        }
    }


    @Override
    public Boolean isCardVisible() {
        String scoutTitle = Constants.getSCOUT();
        if (scoutTitle.equals(mContext.getString(R.string.Customers))) {
            return false;
        }
        List<Booth> booths = Main.daoSession.getBoothDao().loadAll();
        if (booths.isEmpty()) {
            return false;
        }
        long cnt = 0;
        for (Booth booth : booths) {
            cnt = Main.daoSession.getBoothAssignmentsDao().queryBuilder()
                    .where(
                            BoothAssignmentsDao.Properties.BoothAssignBoothId.eq(booth.getId()))
                    .count();
        }

        return cnt < 2;
    }


    @Override
    public List<Booth> getActionList() {
        List<Booth> stringList = new ArrayList<Booth>();
        List<Booth> booths = Main.daoSession.getBoothDao().loadAll();

        for (Booth booth : booths) {
            long cnt = Main.daoSession.getBoothAssignmentsDao().queryBuilder().where(BoothAssignmentsDao.Properties.BoothAssignBoothId.eq(booth.getId())).count();
            if (cnt < 2) {

                stringList.add(booth);

            }
        }
        return stringList;
    }


    @Override
    public String getActionTitle() {
        return "The following booths need to be assigned scouts:";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String getHeaderText() {
        return "Booths";
    }


}
