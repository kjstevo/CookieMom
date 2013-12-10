package net.kjmaster.cookiemom.personal;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.ui.CookieAmountContentCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.ui.CompleteCookieCard.*;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 6:58 PM
 */
@EFragment(R.layout.personal_order_dialog)
public class PersonalOrderFragment extends Fragment implements ICookieActionFragment {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardListView;

    public final HashMap<String,String> valuesMap=new HashMap<String,String>();
    private boolean isRefresh=false;

    @AfterViews()
    void afterViews(){
        List<Card> mData = new ArrayList<Card>();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            if (!isRefresh){
                valuesMap.put(cookieType, "0");
            }
            CookieAmountContentCard card;
            Integer actualAmount = Integer.valueOf(valuesMap.get(cookieType));
            int amountExpected = 0;
            boolean autoFillEditFields = false;
            boolean showExpected = false;
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
            PersonalOrderActivity personalOrderActivity = (PersonalOrderActivity) getActivity();

            if (personalOrderActivity != null) {
                card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW,new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        ((CookieActionActivity)getActivity()).createNumberPicker(finalI).show();
                    }
                });
            } else {
                Log.e("cookiemom", "Error in personalOrderActivity:");

            }

            mData.add(card);
        }
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);

        cardListView.setAdapter(cardArrayAdapter);

    }




    @ViewById(R.id.personal_contact)
    EditText mContact;

    @ViewById(R.id.personal_customer)
    EditText mCustomer;

    public String getCustomer(){
        if(mCustomer!=null){
           return mCustomer.getText().toString();
        } else {
            return "";
        }
    }




     public String getContact(){
        if(mContact!=null) {
            return mContact.getText().toString();
        } else {
            return "";
        }
     }

    @Override
    public HashMap<String, String> valuesMap() {
        return valuesMap;
    }

    @Override
    public void refreshView() {
        isRefresh=true;
        this.afterViews();
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }
}
