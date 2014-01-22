package net.kjmaster.cookiemom.scout;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.dao.ScoutDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.scout.add.AddScoutActivity_;
import net.kjmaster.cookiemom.scout.edit.ScoutEditActivity_;
import net.kjmaster.cookiemom.scout.expander.CustomExpander;
import net.kjmaster.cookiemom.scout.order.ScoutOrderActivity_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupActivity_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupDialog;
import net.kjmaster.cookiemom.scout.turnin.ScoutTurnInActivity_;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

import static net.kjmaster.cookiemom.dao.CookieTransactionsDao.Properties.TransDate;
import static net.kjmaster.cookiemom.dao.CookieTransactionsDao.Properties.TransScoutId;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderDate;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderScoutId;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.PickedUpFromCupboard;

@OptionsMenu(R.menu.scout_frag)
@EFragment(R.layout.main_fragment)
public class ScoutFragment extends Fragment implements ISimpleDialogListener, ICookieActionFragment {

    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;
    private ScoutDao scoutDao;
    private OrderDao orderDao;
    private CookieTransactionsDao cookieTransactionsDao;
    private boolean isRefresh;
    private boolean isDelete = false;

    @AfterViews
    void afterViews() {

        assignDaos();

        List<Scout> scouts = scoutDao.queryBuilder().orderAsc(ScoutDao.Properties.ScoutName).list();

        if (!scouts.isEmpty()) {

            createScoutListCards(scouts);
        }
    }

    private void createScoutListCards(List<Scout> scouts) {
        List<Card> mData = new ArrayList<Card>();

        for (Scout scout1 : scouts) {

            ScoutCard mCard = new ScoutCard(getActivity());

            mCard.addCardHeader(getCardHeader(scout1));

            long orderCount = orderDao.queryBuilder()
                    .where(
                            OrderScoutId.eq(scout1.getId()),
                            PickedUpFromCupboard.eq(true))
                    .orderAsc(OrderDate)
                    .count();

            if (orderCount > 0) {
                mCard.setReadyForPickup(true);
            } else {
                mCard.setReadyForPickup(false);
            }

            List<Order> orders = orderDao.queryBuilder()
                    .where(
                            OrderScoutId.eq(scout1.getId()))
                    .orderAsc(OrderDate)
                    .list();

            if (orders != null) {
                getOrders(mCard, orders);
            }

            List<CookieTransactions> transactionse = cookieTransactionsDao.queryBuilder()
                    .where(
                            TransScoutId.eq(scout1.getId()))
                    .orderAsc(TransDate)
                    .list();

            if (transactionse != null) {
                getTransactions(mCard, transactionse);
            }

            createExpander(mData, scout1, mCard);
        }

        cardView.setAdapter(getCardArrayAdapter(mData));
    }


    private CardHeader getCardHeader(final Scout scout) {
        CardHeader scoutHeader = new CardHeader(getActivity());

        scoutHeader.setButtonExpandVisible(true);

        scoutHeader.setButtonOverflowVisible(true);

        scoutHeader.setPopupMenu(R.menu.scout_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                scoutActionsMenu(item, scout);
            }
        });
        scoutHeader.setTitle(scout.getScoutName());
        return scoutHeader;
    }


    private CardArrayAdapter getCardArrayAdapter(List<Card> mData) {
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
        cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
        return cardArrayAdapter;
    }

    private void createExpander(List<Card> mData, Scout scout, ScoutCard mCard) {
        CustomExpander expand = new CustomExpander(getActivity(), scout);

        // Set inner title in Expand Area
        expand.setTitle(getResources().getString(R.string.scout_turn_in_title));

        // Add expand to a card
        mCard.addCardExpand(expand);
        mData.add(mCard);

    }

    private void getTransactions(ScoutCard mCard, List<CookieTransactions> transactionse) {
        Integer transTotal = 0;
        Double cashTotal = 0D;
        for (CookieTransactions cookieTransactions : transactionse) {
            transTotal = transTotal + cookieTransactions.getTransBoxes();
            cashTotal = cashTotal + cookieTransactions.getTransCash();
        }
//because boxes taken from inv are represented as negative numbers, but that looks weird.
        transTotal = transTotal * -1;


        mCard.setAltCount(transTotal.toString());
        mCard.setPaidText(getNumberFormatAsCash(cashTotal));
        mCard.setOwedText(getNumberFormatAsCash((transTotal * 4) - cashTotal));
        mCard.setTotalText(getNumberFormatAsCash((double) (transTotal * 4)));

    }

    private String getNumberFormatAsCash(Double amt) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        fmt.setMaximumFractionDigits(0);
        fmt.setMinimumFractionDigits(0);
        return fmt.format(amt);
    }

    private void getOrders(ScoutCard mCard, List<Order> orders) {
        Integer totalOrders = 0;
        for (Order order : orders) {
            totalOrders = totalOrders + order.getOrderedBoxes();
        }
        mCard.setTitle(totalOrders.toString());
    }

    private void assignDaos() {
        scoutDao = Main.daoSession.getScoutDao();

        orderDao = Main.daoSession.getOrderDao();

        cookieTransactionsDao = Main.daoSession.getCookieTransactionsDao();
    }


    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (isDelete) {
            deleteScout(scoutDao.load((long) requestCode));
        } else {
            ScoutPickupActivity_.intent(getActivity())
                    .isEditable(true)
                    .ScoutId(requestCode)
                    .startForResult(Constants.SCOUT_REQUEST
                    );
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (isDelete) {
            isDelete = false;

        } else {
            ScoutPickupActivity_.intent(getActivity()).isEditable(false).ScoutId(requestCode).startForResult(Constants.SCOUT_REQUEST);
        }
    }
//overflow menu for each card

    private void scoutActionsMenu(MenuItem item, Scout scout) {
        switch (item.getItemId()) {
            case R.id.menu_scout_order:
                ScoutOrderActivity_.intent(getActivity()).scoutId(scout.getId()).startForResult(Constants.SCOUT_REQUEST);
                break;
            case R.id.menu_scout_pickup:
                ScoutPickupDialog scoutPickupDialog = new ScoutPickupDialog();
                scoutPickupDialog.ScoutPickupDialog(scout, getActivity(), this);
                break;

            case R.id.menu_scout_turnin:
                ScoutTurnInActivity_.intent(getActivity()).isEditable(true).ScoutId(scout.getId()).startForResult(Constants.SCOUT_REQUEST);
                break;
            case R.id.menu_scout_delete:
                isDelete = true;
                SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                        .setTitle("WARNING!")
                    .setMessage("Are you sure you want to delete this? This action can not be undone!")
                    .setPositiveButtonText("Delete")
                    .setNegativeButtonText("Cancel")
                    .setCancelable(true)
                    .setTargetFragment(this, scout.getId().intValue())
                    .setRequestCode(scout.getId().intValue())
                    .setTag(scout.getId().toString())
                    .show();
                break;
            case R.id.menu_scout_edit_order:
                ScoutEditActivity_.intent(getActivity()).ScoutId(scout.getId()).requestCode(Constants.SCOUT_REQUEST).startForResult(Constants.SCOUT_REQUEST);
                break;
        }
    }

    private void deleteScout(Scout scout) {
        Main.daoSession.getCookieTransactionsDao().deleteInTx(scout.getScoutsCookieTransactions());
        Main.daoSession.getOrderDao().deleteInTx(scout.getScoutOrders());
        Main.daoSession.getBoothAssignmentsDao().deleteInTx(scout.getBoothsAssigned());
        Main.daoSession.getScoutDao().deleteInTx(scout);
        refreshView();
    }


    @OptionsItem(R.id.menu_add_scout)
    void addScout() {
        AddScoutActivity_.intent(getActivity()).startForResult(Constants.ADD_SCOUT_REQUEST);
    }


    @Override
    public HashMap<String, String> valuesMap() {
        return null;
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



