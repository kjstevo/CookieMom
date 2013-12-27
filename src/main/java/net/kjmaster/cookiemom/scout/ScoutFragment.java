package net.kjmaster.cookiemom.scout;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import com.googlecode.androidannotations.annotations.*;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.MainActivity;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.scout.add.AddScoutActivity_;
import net.kjmaster.cookiemom.scout.expander.CustomExpander;
import net.kjmaster.cookiemom.scout.order.ScoutOrderActivity_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupActivity_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupDialog;
import net.kjmaster.cookiemom.scout.turnin.ScoutTurnInActivity_;
import net.kmaster.cookiemom.dao.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@OptionsMenu(R.menu.scout_frag)
@EFragment(R.layout.main_fragment)
public class ScoutFragment extends Fragment implements ISimpleDialogListener, ICookieActionFragment {

    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;
    private ScoutDao scoutDao;
    private OrderDao orderDao;
    private CookieTransactionsDao cookieTransactionsDao;
    private boolean isRefresh;

    @AfterViews
    void afterViews() {

        assignDaos();

        List<Scout> scouts = scoutDao.queryBuilder().list();

        if (!scouts.isEmpty()) {

            createScoutListCards(scouts);
        }
    }

    private void createScoutListCards(List<Scout> scouts) {
        List<Card> mData = new ArrayList<Card>();

        for (Scout scout1 : scouts) {

            ScoutCard mCard = new ScoutCard(getActivity());

            mCard.addCardHeader(getCardHeader(scout1));

            long orderCount;

            orderCount = orderDao.queryBuilder().where(
                    OrderDao.Properties.OrderScoutId.eq(scout1.getId()),
                    OrderDao.Properties.PickedUpFromCupboard.eq(true)).
                    orderAsc(OrderDao.Properties.OrderDate).
                    count();

            if (orderCount > 0) {
                mCard.setReadyForPickup(true);
            } else {
                mCard.setReadyForPickup(false);
            }

            List<Order> orders = orderDao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderScoutId.eq(scout1.getId()
                            )
                    )
                    .orderAsc(OrderDao.Properties.OrderDate)
                    .list();

            if (orders != null) {
                getOrders(mCard, orders);
            }

            List<CookieTransactions> transactionse = cookieTransactionsDao.queryBuilder().where(CookieTransactionsDao.Properties.TransScoutId.eq(scout1.getId())).orderAsc(CookieTransactionsDao.Properties.TransDate).list();

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

//        ScoutExpander scoutExpander = new ScoutExpander(getActivity(), scout.getId());
//        scoutExpander.setTitle("Transactions");
//        mCard.addCardExpand(scoutExpander);
//        mData.add(mCard);
    }

    private void getTransactions(ScoutCard mCard, List<CookieTransactions> transactionse) {
        Integer transTotal = 0;
        Double cashTotal = 0D;
        for (CookieTransactions cookieTransactions : transactionse) {
            transTotal = transTotal + cookieTransactions.getTransBoxes();
            cashTotal = cashTotal + cookieTransactions.getTransCash();
        }

        transTotal = transTotal * -1;
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        fmt.setMaximumFractionDigits(0);
        fmt.setMinimumFractionDigits(0);
        String owed = fmt.format((transTotal * 4) - cashTotal);

        mCard.setAltCount(transTotal.toString());
        String paid = fmt.format(cashTotal);
        mCard.setPaidText(paid);
        mCard.setOwedText(owed);
        double totalDue = (transTotal * 4);
        mCard.setTotalText(fmt.format(totalDue));

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
        ScoutPickupActivity_.intent(getActivity())
                .isEditable(true)
                .ScoutId(requestCode)
                .startForResult(Constants.SCOUT_REQUEST
                );
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        ScoutPickupActivity_.intent(getActivity()).isEditable(false).ScoutId(requestCode).startForResult(Constants.SCOUT_REQUEST);
    }

    private void scoutActionsMenu(MenuItem item, Scout scout) {
        if (item.getItemId() == R.id.menu_scout_order) {
            ScoutOrderActivity_.intent(getActivity()).scoutId(scout.getId()).startForResult(Constants.SCOUT_REQUEST);
        }

        if (item.getItemId() == R.id.menu_scout_pickup) {
            ScoutPickupDialog scoutPickupDialog = new ScoutPickupDialog();
            scoutPickupDialog.ScoutPickupDialog(scout, getActivity(), this);
        }

        if (item.getItemId() == R.id.menu_scout_turnin) {
            ScoutTurnInActivity_.intent(getActivity()).isEditable(true).ScoutId(scout.getId()).startForResult(Constants.SCOUT_REQUEST);
        }
    }

    @OptionsItem(R.id.menu_add_scout)
    void addScout() {
        AddScoutActivity_.intent(getActivity()).startForResult(Constants.SCOUT_REQUEST);
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


    @OnActivityResult(Constants.SCOUT_REQUEST)
    void scoutResullts(int resultCode) {
        refreshView();
        ((MainActivity) getActivity()).refreshAll();

    }

}



