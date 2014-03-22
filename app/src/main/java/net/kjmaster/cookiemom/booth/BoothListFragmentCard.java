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

package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothAssignments;
import net.kjmaster.cookiemom.dao.BoothAssignmentsDao;
import net.kjmaster.cookiemom.dao.BoothDao;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/1/13
 * Time: 10:41 PM
 */
public class BoothListFragmentCard extends Card {
    private final long scoutId;

    public BoothListFragmentCard(Context context, long scoutId) {
        super(context, R.layout.booth_list_fragment);
        this.scoutId = scoutId;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //net.kjm
        final Context context = getContext();
        final BoothDao boothDao = Main.daoSession.getBoothDao();
        final BoothAssignmentsDao assignmentsDao = Main.daoSession.getBoothAssignmentsDao();
        final ListView cardListView = (ListView) parent.findViewById(R.id.booth_listview);

        if (
                cardListView != null
                        && assignmentsDao != null
                        && boothDao != null
                ) {

            List<BoothAssignments> boothAssignments =
                    assignmentsDao.queryBuilder()
                            .where(
                                    BoothAssignmentsDao.Properties.BoothAssignScoutId.eq(scoutId))
                            .list();

            if (!boothAssignments.isEmpty()) {

                BoothTextAdapterListItem mData = new BoothTextAdapterListItem(context);

                for (BoothAssignments assignment : boothAssignments) {

                    Booth booth = boothDao.load(assignment.getBoothAssignBoothId());

                    if (booth != null) {

                        mData.add(booth);

                    }

                }

                cardListView.setAdapter(mData);

            }

        }

    }

}

