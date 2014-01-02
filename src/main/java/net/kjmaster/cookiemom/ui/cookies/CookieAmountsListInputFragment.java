package net.kjmaster.cookiemom.ui.cookies;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import com.googlecode.androidannotations.annotations.*;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.ui.cookies.CompleteCookieCard.CreateCompleteCookieCard;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 6:58 PM
 */
@EFragment(R.layout.scout_order_dialog)
public class CookieAmountsListInputFragment extends Fragment implements ICookieActionFragment {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardListView;

    @FragmentArg
    boolean isEditable;

    @FragmentArg
    boolean isBoxes;

    private final HashMap<String, String> valuesBoxesMap = new HashMap<String, String>();
    public final HashMap<String, String> valueCasesMap = new HashMap<String, String>();
    public final HashMap<String, String> valueMap = new HashMap<String, String>();
    private boolean isRefresh = false;


    @AfterViews()
    void afterViews() {

        List<Card> mData = new ArrayList<Card>();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {

            final String cookieType = Constants.CookieTypes[i];
            if (!isRefresh) {
                valuesBoxesMap.putAll(((CookieActionActivity) getActivity()).getValMap());

            }
            CookieAmountContentCard card;
            Integer actualAmount = Integer.valueOf(valuesBoxesMap.get(cookieType));

            int amountExpected = 0;
            card = CreateCompleteCookieCard
                    (new CookieAmountContentCard(
                            getActivity(),
                            amountExpected,
                            false,
                            false,
                            actualAmount,
                            true
                    ),
                            cookieType, getActivity(), Constants.CookieColors[i]
                    );

            final int finalI = i;

            final CookieActionActivity cookieActionActivity = (CookieActionActivity) getActivity();
            if (cookieActionActivity != null) {

                card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        cardListPos = cardListView.getFirstVisiblePosition();
                        cookieActionActivity.createNumberPicker(finalI).show();
                    }
                });
            } else {
                Log.e("cookiemom", "Error in scoutPickupActivity:");

            }

            mData.add(card);
        }
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);

        cardListView.setAdapter(cardArrayAdapter);
        if (cardListView.getCount() > cardListPos) {
            cardListView.setSelectionFromTop(cardListPos, 0);
        }


    }

    @InstanceState
    int cardListPos;

    @NotNull
    @Override
    public HashMap<String, String> valuesMap() {
        return valuesBoxesMap;
    }

    @Override
    public void refreshView() {
        isRefresh = true;
        this.afterViews();
    }

    @Override
    public boolean isRefresh() {

        return isRefresh;
    }


}
