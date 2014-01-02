package net.kjmaster.cookiemom.scout.expander;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.BoothListFragmentCard;
import net.kmaster.cookiemom.dao.Scout;
import org.jetbrains.annotations.Nullable;

public class CustomExpander extends CardExpand {
    private final Scout scout;

    // Use your resource ID for your inner layout
    public CustomExpander(Context context, Scout scout) {
        super(context, R.layout.booth_expander_main_layout);
        this.scout = scout;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, @Nullable View view) {
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
