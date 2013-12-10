package net.kjmaster.cookiemom.action;

//~--- non-JDK imports --------------------------------------------------------

import android.support.v4.app.Fragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.ScoutPickupActivity_;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

@OptionsMenu(R.menu.action_frag)
@EFragment(R.layout.fragment_sample)
public class ActionFragment extends Fragment implements ISimpleDialogListener {
    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;

    @AfterViews
    void afterViews() {
        List<ActionContentCard> actionContentCardLists = new ArrayList<ActionContentCard>();

        createCards(actionContentCardLists);

        List<Card> mData = new ArrayList<Card>(actionContentCardLists);

        createListAdapter(mData);
    }

    private void createListAdapter(List<Card> mData) {
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
        cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
        cardView.setAdapter(cardArrayAdapter);
    }

    private void createCards(List<ActionContentCard> actionContentCardLists) {
        addScoutPickupCard(actionContentCardLists);
        addBoothAssignCard(actionContentCardLists);
        addCupboardAssignCard(actionContentCardLists);
        addAddScoutCard(actionContentCardLists);
        // addPersonalDelivery(actionContentCardLists);
    }


    private void addAddScoutCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionAddScout(getActivity()));
    }

    private void addCupboardAssignCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionCupboardOrder(getActivity()));
    }

    private void addBoothAssignCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionAssignBooth(getActivity()));
    }

    private void addScoutPickupCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionScoutPickup(getActivity(), this));
    }


    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode >= 0) {
            ScoutPickupActivity_.intent(getActivity()).isEditable(true).ScoutId(requestCode).startForResult(
                    Constants.ACTION_REQUEST);
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (requestCode >= 0) {
            ScoutPickupActivity_.intent(getActivity()).isEditable(false).ScoutId(requestCode).startForResult(
                    Constants.ACTION_REQUEST);
        }
    }

    private void createActionItem(List<ActionContentCard> actionContentCardLists, ActionContentCard actionItem) {
        if (actionItem.isCardVisible()) {
            CardHeader cardHeader = new CardHeader(getActivity());
            cardHeader.setTitle(actionItem.getHeaderText());
            actionItem.addCardHeader(cardHeader);
            actionItem.setSwipeable(true);
            actionContentCardLists.add(actionItem);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
