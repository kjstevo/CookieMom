package net.kjmaster.cookiemom.cupboard;


import android.support.v4.app.Fragment;
import android.view.View;
import com.googlecode.androidannotations.annotations.*;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.ui.CookieAmountContentCard;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 6:58 PM
 */
@EFragment(R.layout.scout_order_dialog)
public class CupboardOrderFragment extends Fragment implements ICookieActionFragment {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardListView;

    @App
    Main main;

    @FragmentArg
    boolean isBoxes;

    private final HashMap<String, String> valuesMap = new HashMap<String, String>();

    private boolean isRefresh = false;

    @AfterViews()
    void afterViews() {
        List<Card> mData = new ArrayList<Card>();
        OrderDao dao = Main.daoSession.getOrderDao();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            if (!isRefresh) {
                valuesMap.put(cookieType, "0");
            }
            Integer totalBoxes = 0;
            List<Order> orders = dao.queryBuilder().where(OrderDao.Properties.OrderCookieType.eq(cookieType), OrderDao.Properties.OrderedFromCupboard.eq(false)).list();
            for (Order order1 : orders) {
                totalBoxes = totalBoxes + order1.getOrderedBoxes();

            }


            CookieAmountContentCard card = new CookieAmountContentCard(getActivity(), totalBoxes, false, true, Integer.valueOf(valuesMap.get(cookieType)), isBoxes);

            CardHeader cardHeader = new CardHeader(getActivity());
            cardHeader.setTitle(cookieType);
            card.addCardHeader(cardHeader);
            CardThumbnail cardThumbnail = new CardThumbnail(getActivity());
            cardThumbnail.setDrawableResource(Constants.getCookieNameImages().get(cookieType));
            card.addCardThumbnail(cardThumbnail);

            final int finalI = i;
            card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new Card.OnCardClickListener() {
                @Override
                public void onClick(final Card card, final View view) {

                    final NumberPickerBuilder numberPickerBuilder =
                            new NumberPickerBuilder().setFragmentManager(getActivity().getSupportFragmentManager()).setStyleResId(R.style.BetterPickersDialogFragment_Light);
                    numberPickerBuilder.setReference(finalI);
                    numberPickerBuilder.show();
                }
            });

            mData.add(card);


        }

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);

        cardListView.setAdapter(cardArrayAdapter);

    }

    @Override
    public HashMap<String, String> valuesMap() {
        return valuesMap;  //To change body of implemented methods use File | Settings | File Templates.
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
