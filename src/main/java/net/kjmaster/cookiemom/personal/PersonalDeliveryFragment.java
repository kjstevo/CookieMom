package net.kjmaster.cookiemom.personal;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.ui.CookieAmountContentCard;
import net.kmaster.cookiemom.dao.PersonalOrders;
import net.kmaster.cookiemom.dao.PersonalOrdersDao;

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
public class PersonalDeliveryFragment extends Fragment implements ICookieActionFragment {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardListView;

    @FragmentArg
  String personalCustomer;


    @FragmentArg
    boolean isEditable;

    public final HashMap<String, String> valuesMap = new HashMap<String, String>();

    private boolean isRefresh = false;

    @AfterViews()
    void afterViews() {
        List<Card> mData = new ArrayList<Card>();
        PersonalOrdersDao dao = Main.daoSession.getPersonalOrdersDao();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer totalBoxes = 0;
            List<PersonalOrders> orders = dao.queryBuilder()
                    .where(
                            PersonalOrdersDao.Properties.PersonalCookieType.eq(cookieType),
                            PersonalOrdersDao.Properties.PersonalCustomer.eq(personalCustomer),
                            PersonalOrdersDao.Properties.PersonalPickedUp.eq(true),
                            PersonalOrdersDao.Properties.PersonalDelivered.eq(false)
                    )
                    .list();

            for (PersonalOrders order1 : orders) {
                totalBoxes = totalBoxes + order1.getPersonalBoxes();
              }
            if (!isRefresh) {
                valuesMap.put(cookieType, totalBoxes.toString());
            }

            CookieAmountContentCard card;
            Integer actualAmount = Integer.valueOf(valuesMap.get(cookieType));
            int amountExpected = Integer.valueOf(valuesMap().get(cookieType));
            boolean autoFillEditFields = true;
            boolean showExpected = true;
            card = CreateCompleteCookieCard
                    (new CookieAmountContentCard(
                            getActivity(),
                            amountExpected,
                            autoFillEditFields,
                            showExpected,
                            actualAmount
                    ),
                            cookieType,
                            getActivity());

            final int finalI = i;

            PersonalDeliveryActivity personalDeliveryActivity = (PersonalDeliveryActivity) getActivity();

            if (personalDeliveryActivity != null) {
                if (personalDeliveryActivity.isEditable) {
                    card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new Card.OnCardClickListener() {
                        @Override
                        public void onClick(final Card card, final View view) {
                            ((CookieActionActivity)getActivity()).createNumberPicker(finalI).show();
                        }
                    });
                }
            } else {
                Log.e("cookiemom", "Error in personalDeliveryActivity:");
            }
            mData.add(card);
        }
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
        cardListView.setAdapter(cardArrayAdapter);
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
