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

package net.kjmaster.cookiemom.summary.stat.totals;


import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.R;

import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/25/13
 * Time: 10:58 PM
 */

@EFragment(R.layout.summary_stat_totals_fragment)
public class SummaryStatTotalsFragment extends Fragment {

    @ViewById(R.id.summary_stat_totals_cardview)
    CardView cardView;


    @AfterViews
    void afterViews() {
        SummaryStatTotalsCard mCard = new SummaryStatTotalsCard(getActivity());
        cardView.setCard(mCard);
    }
}