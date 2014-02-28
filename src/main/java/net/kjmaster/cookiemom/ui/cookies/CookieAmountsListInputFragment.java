/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.kjmaster.cookiemom.ui.cookies;


import android.support.v4.app.Fragment;
import android.view.View;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

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
    boolean isEditable, hideCases, showExpected, autoFill, showInventory;

    private final HashMap<String, String> valuesBoxesMap = new HashMap<String, String>();
    public final HashMap<String, String> valueCasesMap = new HashMap<String, String>();
    public final HashMap<String, String> expectedValuesMap = new HashMap<String, String>();

    private boolean isRefresh = false;


    @AfterViews()
    void afterViews() {

        List<Card> mData = new ArrayList<Card>();
        if (!isRefresh) {
            valuesBoxesMap.putAll(((CookieActionActivity) getActivity()).getValMap());
            expectedValuesMap.putAll(valuesBoxesMap);


        }
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            int amountExpected = 0;
            Integer actualAmount = Integer.valueOf(valuesBoxesMap.get(cookieType));
            if (autoFill) {
                if (!isRefresh) {
                    if (actualAmount % 12 > 0) {
                        actualAmount = ((actualAmount / 12) + 1) * 12;
                    }
                }
            }
            if (showExpected) {
                amountExpected = Integer.valueOf(expectedValuesMap.get(cookieType));
            }

            CookieAmountContentCard card = CreateCompleteCookieCard
                    (new CookieAmountContentCard(
                            getActivity(),
                            amountExpected,
                            showExpected,
                            actualAmount,
                            hideCases
                    ),
                            cookieType,
                            getActivity(),
                            getResources().getColor(Constants.CookieColors[i])
                    );

            final int finalI = i;
            if (card == null) return;
            final CookieActionActivity cookieActionActivity =
                    (CookieActionActivity) getActivity();

            if (cookieActionActivity != null) {
                card.addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        cardListPos = cardListView.getFirstVisiblePosition();
                        cookieActionActivity.createNumberPicker(finalI).show();
                    }
                });

            }

            if (showInventory) {
                card.addCardHeader(getCardHeader(cookieType, getResources().getColor(Constants.CookieColors[i])));
            }
            mData.add(card);
        }
        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);

        cardListView.setAdapter(cardArrayAdapter);

        if (cardListView.getCount() > cardListPos) {
            cardListView.setSelectionFromTop(cardListPos, 0);
        }
    }


    private CardHeader getCardHeader(String cookieType, int color) {
        List<CookieTransactions> list = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(CookieTransactionsDao.Properties.CookieType.eq(cookieType))
                .list();
        int total = 0;
        if (list != null) {
            for (CookieTransactions cookieTransactions : list) {
                total += cookieTransactions.getTransBoxes();
            }
        }
        CardHeader cardHeader = new CookieCardHeaderInStock(getActivity(), total, cookieType, color);
        cardHeader.setTitle(cookieType);
        return cardHeader;
    }


    @InstanceState
    int cardListPos;


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
