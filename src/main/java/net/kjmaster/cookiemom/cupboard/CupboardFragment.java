package net.kjmaster.cookiemom.cupboard;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import com.googlecode.androidannotations.annotations.*;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.MainActivity;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;
import net.kmaster.cookiemom.dao.Order;

import java.util.ArrayList;
import java.util.List;

import static net.kmaster.cookiemom.dao.CookieTransactionsDao.Properties.CookieType;
import static net.kmaster.cookiemom.dao.OrderDao.Properties.OrderCookieType;
import static net.kmaster.cookiemom.dao.OrderDao.Properties.PickedUpFromCupboard;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/3/13
 * Time: 4:06 PM
 */
@OptionsMenu(R.menu.cupboard_frag)
@EFragment(R.layout.fragment_sample)
public class CupboardFragment extends Fragment {
    //    private SQLiteDatabase db;
    public static final int GET_ORDER_REQUEST_CODE = 6936;
    private boolean isOrderAvail = false;
    private boolean isPickupAvail = false;
//    private DaoMaster daoMaster;
//    private DaoSession daoSession;
//
//
//    private Cursor cursor;

    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;

    @App
    Main main;

    @AfterViews
    void afterViews() {
//        Bundle bundle = getArguments();
//        String label = bundle.getString("label");

        //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "scouts-db", null);
//        db = helper.getWritableDatabase();
//        daoMaster = new DaoMaster(db);
//        daoSession=daoMaster.newSession();
////        CookieFlavorDao cookieFlavorDao = daoSession.getCookieFlavorDao();
        List<String> cookieFlavors = new ArrayList<String>(Constants.getCookieNameImages().keySet());

        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();


        List<Card> mData = new ArrayList<Card>();
        for (final String cookieFlavor : cookieFlavors) {

            List trans = dao.queryBuilder().where(CookieType.eq(cookieFlavor)).list();
            Integer sumTotal = 0;
            for (Object tran1 : trans) {
                CookieTransactions tran = (CookieTransactions) tran1;
                sumTotal = sumTotal + tran.getTransBoxes();

            }
            if (sumTotal > 0) isPickupAvail = true;
            Integer orderSumTotal = 0;
            trans = Main.daoSession.getOrderDao().queryBuilder().where(OrderCookieType.eq(cookieFlavor), PickedUpFromCupboard.eq(false)).list();
            for (Object tran1 : trans) {
                Order tran = (Order) tran1;
                orderSumTotal = orderSumTotal + tran.getOrderedBoxes();
                Log.d("cookiemom", "Picked up flag is:" + tran.getPickedUpFromCupboard().toString());

            }
            if (orderSumTotal > 0) isOrderAvail = true;
            CupboardContentCard mCard = new CupboardContentCard(getActivity(), sumTotal, orderSumTotal);

            CardThumbnail cardThumbnail = new CardThumbnail(getActivity());
            cardThumbnail.setDrawableResource(Constants.getCookieNameImages().get(cookieFlavor));
            mCard.addCardThumbnail(cardThumbnail);
            CardHeader cardHeader = new CardHeader(getActivity());
            cardHeader.setButtonOverflowVisible(true);
            cardHeader.setPopupMenu(R.menu.cupboard_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    selectCookieFlavorMenu(card, item);
                }
            });

//                cardHeader.setPopupMenu(R.menu.cupboard_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//                    @Override
//                    public void onMenuItemClick(BaseCard card, MenuItem item) {
//                        selectCookieFlavorMenu(card, item);
//                    }
//                });


            mCard.addCardHeader(cardHeader);
            cardHeader.setTitle(cookieFlavor);

            mData.add(mCard);
        }

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
        cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
        cardView.setAdapter(cardArrayAdapter);

    }

    @SuppressWarnings("UnusedParameters")
    private void selectCookieFlavorMenu(BaseCard card, MenuItem item) {


    }


    @OptionsItem(R.id.menu_place_cupboard_order)
    void placeOrder() {
        if (isOrderAvail) {
            CupboardOrderActivity_.intent(getActivity()).startForResult(Constants.CUPBOARD_REQUEST);
        }
    }

    @OptionsItem(R.id.menu_pickup_cupboard_order)
    void pickupOrder() {
        if (isPickupAvail) {
            CupboardPickupActivity_.intent(getActivity()).startForResult(Constants.CUPBOARD_REQUEST);
        }
    }

    //TODO prevent negative inventory
    @OnActivityResult(Constants.CUPBOARD_REQUEST)
    void cupboardResults() {
        ((MainActivity) getActivity()).refreshAll();
    }


}