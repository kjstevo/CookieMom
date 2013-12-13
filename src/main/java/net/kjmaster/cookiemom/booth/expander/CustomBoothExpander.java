package net.kjmaster.cookiemom.booth.expander;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.Booth;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/12/13
 * Time: 4:17 PM
 */
public class CustomBoothExpander extends CardExpand {
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //net.kjmaster.cookiemom.booth.expander.CustomBoothExpander.setupInnerViewElements returns void

        //12/12/13
        if (view == null) {
            return;
        }


        Card card = new BoothExpanderCookieList(getContext(), this.mBooth);
        CardHeader header = new CardHeader(getContext());
        CardView cardView = (CardView) view.findViewById(R.id.carddemo_example_card3_listeners2);
        header.setTitle("Cookies");    // should use R.string.
        card.addCardHeader(header);
        cardView.setCard(card);
        super.setupInnerViewElements(parent, view);

    }

    private final Booth mBooth;

    public CustomBoothExpander(Context context, Booth booth) {
        this(context, R.layout.expand_scout_expanded, booth);
    }

    public CustomBoothExpander(Context context, int innerLayout, Booth booth) {
        super(context, innerLayout);
        mBooth = booth;
    }
}
