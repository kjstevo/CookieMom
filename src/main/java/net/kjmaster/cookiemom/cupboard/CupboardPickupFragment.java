package net.kjmaster.cookiemom.cupboard;


import android.support.v4.app.Fragment;
import android.view.View;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.ui.CookieAmountContentCard;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.ui.CompleteCookieCard.CreateCompleteCookieCard;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 6:58 PM
 */
@EFragment(R.layout.scout_order_dialog)
public class CupboardPickupFragment extends Fragment implements ICookieActionFragment {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardListView;

    @FragmentArg
    boolean isBoxes = true;

    private final HashMap<String, String> valuesMap = new HashMap<String, String>();

    private boolean isRefresh = false;

    @AfterViews()
    void afterViews() {
        List<Card> mData = new ArrayList<Card>();

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.OrderedFromCupboard.eq(true),
                            OrderDao.Properties.OrderCookieType.eq(cookieType),
                            OrderDao.Properties.PickedUpFromCupboard.eq(false))

                    .list();
            if (!orderList.isEmpty()) {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                }
            }
            if (!isRefresh) {
                valuesMap.put(cookieType, String.valueOf(amountExpected));

            }


            Integer actualAmount = Integer.valueOf(valuesMap.get(cookieType));

            boolean autoFillEditFields = false;
            boolean showExpected = true;
            CookieAmountContentCard card = CreateCompleteCookieCard
                    (new CookieAmountContentCard(
                            getActivity(),
                            amountExpected,
                            false,
                            true,
                            actualAmount,
                            isBoxes
                    ),
                            cookieType,
                            getActivity());
            card.addCardHeader(getCardHeader(cookieType));

            card.addCardThumbnail(getCardThumbnail(cookieType));

            final int finalI = i;
            card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    ((CookieActionActivity) getActivity()).createNumberPicker(finalI).show();
                }
            });
            mData.add(card);


        }

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);

        cardListView.setAdapter(cardArrayAdapter);

    }

    private CardHeader getCardHeader(String cookieType) {
        CardHeader cardHeader = new CardHeader(getActivity());
        cardHeader.setTitle(cookieType);
        return cardHeader;
    }

    private CardThumbnail getCardThumbnail(String cookieType) {
        CardThumbnail cardThumbnail = new CardThumbnail(getActivity());
        cardThumbnail.setDrawableResource(Constants.getCookieNameImages().get(cookieType));
        return cardThumbnail;
    }

    @Override
    public HashMap<String, String> valuesMap() {
        return valuesMap;
    }

    public void refreshView() {
        this.isRefresh = true;
        afterViews();
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

}
