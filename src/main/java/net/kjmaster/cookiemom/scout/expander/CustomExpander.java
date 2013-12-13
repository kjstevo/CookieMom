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

public class CustomExpander extends CardExpand {
    private final Scout scout;

    // Use your resource ID for your inner layout
    public CustomExpander(Context context, Scout scout) {
        super(context, R.layout.custom_expander_main_layout);
        this.scout = scout;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null) {
            return;
        }

        BoothListFragmentCard card1 = new BoothListFragmentCard(getContext(), scout.getId());
        //Card card1       = new BoothContentCard(getContext(),) Card(getContext(), R.layout.carddemo_example_inner_content);
        CardHeader cardHeader1 = new CardHeader(getContext());


        cardHeader1.setTitle("Upcoming Booths");
        card1.addCardHeader(cardHeader1);

//      cardHeader2.setTitle("Booths");
//     card2.addCardHeader(cardHeader2);


        // Retrieve TextView elements
        CardView cardView1 = (CardView) view.findViewById(R.id.carddemo_example_card3_listeners1);
//


        Card card2 = new ScoutExpanderCookieList(getContext(), this.scout);
        CardHeader header = new CardHeader(getContext());
        CardView cardView2 = (CardView) view.findViewById(R.id.carddemo_example_card3_listeners2);
        cardView2.setCard(card1);
        header.setButtonExpandVisible(true);
        header.setTitle("Cookies");    // should use R.string.
        card2.addCardHeader(header);

        // Add expand
        ScoutExpander expand = new ScoutExpander(getContext(), R.layout.carddemo_googlenow_inner_expand, scout.getId());

        card2.addCardExpand(expand);


        cardView1.setCard(card2);

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
