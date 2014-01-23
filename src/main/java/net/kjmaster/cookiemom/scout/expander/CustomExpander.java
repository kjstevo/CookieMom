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

package net.kjmaster.cookiemom.scout.expander;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.BoothListFragmentCard;
import net.kjmaster.cookiemom.dao.Scout;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;


public class CustomExpander extends CardExpand {
    private final Scout scout;

    // Use your resource ID for your inner layout
    public CustomExpander(Context context, Scout scout) {
        super(context, R.layout.booth_expander_main_layout);
        this.scout = scout;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null) {
            return;
        }

        BoothListFragmentCard upcomingBoothsCard = new BoothListFragmentCard(getContext(), scout.getId());
        //Card upcomingBoothsCard       = new BoothContentCard(getContext(),) Card(getContext(), R.layout.carddemo_example_inner_content);
        CardHeader upcomingBoothsCardHeader = new CardHeader(getContext());


        upcomingBoothsCardHeader.setTitle(getContext().getString(R.string.upcoming_booths));
        upcomingBoothsCard.addCardHeader(upcomingBoothsCardHeader);

//      cardHeader2.setTitle("Booths");
//     cookieListCard.addCardHeader(cardHeader2);


        // Retrieve TextView elements
        CardView cookieListCardView = (CardView) view.findViewById(R.id.carddemo_example_card3_listeners1);
//


        Card cookieListCard = new ScoutExpanderCookieList(getContext(), this.scout);
        CardHeader cookieListCardHeader = new CardHeader(getContext());
        CardView upcomingBoothsCardView = (CardView) view.findViewById(R.id.carddemo_example_card3_listeners2);
        upcomingBoothsCardView.setCard(upcomingBoothsCard);
        cookieListCardHeader.setButtonExpandVisible(true);
        cookieListCardHeader.setTitle("Cookies");    // should use R.string.
        cookieListCard.addCardHeader(cookieListCardHeader);

        // Add expand
        ScoutExpander expand = new ScoutExpander(getContext(), R.layout.scout_expander_inner_expand, scout.getId());

        cookieListCard.addCardExpand(expand);


        cookieListCardView.setCard(cookieListCard);

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
