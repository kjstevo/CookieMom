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

package net.kjmaster.cookiemom.summary.stat.booth;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.R;

import it.gmariotti.cardslib.library.view.CardView;

/**
 * Card Examples
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
@EFragment(R.layout.summary_stat_booth_fragment)
public class SummaryStatBoothCardFragment extends android.support.v4.app.Fragment {


    @ViewById(R.id.summary_stat_booth_cardview)
    CardView mCardView;

    @AfterViews
    void afterViews() {
        initCard();

    }


    /**
     * This method builds a simple card
     */
    private void initCard() {

        //Create a Card
        SummaryStatBoothCard card = new SummaryStatBoothCard(getActivity().getApplicationContext());

        //Set card in the cardView
        mCardView.setCard(card);
    }


}
