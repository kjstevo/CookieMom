package net.kjmaster.cookiemom.action;

//~--- non-JDK imports --------------------------------------------------------

import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.action.booth.ActionAssignBooth;
import net.kjmaster.cookiemom.action.booth.ActionBoothCheckIn;
import net.kjmaster.cookiemom.action.booth.ActionBoothCheckOut;
import net.kjmaster.cookiemom.action.booth.ActionBoothOrder;
import net.kjmaster.cookiemom.action.cupboard.ActionCupboardOrder;
import net.kjmaster.cookiemom.action.scout.ActionAddScout;
import net.kjmaster.cookiemom.action.scout.ActionScoutPickup;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupActivity_;

import java.util.ArrayList;
import java.util.List;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

//~--- JDK imports ------------------------------------------------------------

@OptionsMenu(R.menu.action_frag)
@EFragment(R.layout.main_fragment)
public class ActionFragment extends Fragment implements ISimpleDialogListener {
    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;

    @Pref
    ISettings_ iSettings;

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

    // Create a new action by create a new class extending ActionContentCard  adding a call to its construtor calling method here.
    private void createCards(List<ActionContentCard> actionContentCardLists) {
        addScoutPickupCard(actionContentCardLists);
        addBoothAssignCard(actionContentCardLists);
        addCupboardAssignCard(actionContentCardLists);
        addAddScoutCard(actionContentCardLists);
        addBoothCheckInCard(actionContentCardLists);
        addBoothCheckOutCard(actionContentCardLists);
        addBoothOrderCard(actionContentCardLists);

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

    private void addBoothOrderCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionBoothOrder(getActivity()));
    }

    private void addBoothCheckOutCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionBoothCheckOut(getActivity()));
    }

    private void addBoothCheckInCard(List<ActionContentCard> actionContentCardLists) {
        createActionItem(actionContentCardLists, new ActionBoothCheckIn(getActivity()));
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
            String title = actionItem.getHeaderText();


            cardHeader.setTitle(title);
            actionItem.addCardHeader(cardHeader);
            actionItem.setSwipeable(true);
            actionContentCardLists.add(actionItem);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
