/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package net.kjmaster.cookiemom.summary.stat.scout;

import android.widget.ScrollView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R;

/**
 * Card Examples
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
@EFragment(R.layout.summary_stat_scout_fragment)
public class SummaryStatScoutCardFragment extends android.support.v4.app.Fragment {


    @ViewById(R.id.summary_stat_scout_card_scrollview)
    ScrollView mScrollView;

    @ViewById(R.id.summary_stat_scout_cardview)
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
        SummaryStatScoutCard card = new SummaryStatScoutCard(getActivity().getApplicationContext());

        //Set card in the cardView
        mCardView.setCard(card);
    }


}
