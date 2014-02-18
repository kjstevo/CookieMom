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

package net.kjmaster.cookiemom.cupboard;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.editor.EditDataActivity_;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.ui.cookies.CookieCardHeader;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import static net.kjmaster.cookiemom.dao.CookieTransactionsDao.Properties.CookieType;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.OrderCookieType;
import static net.kjmaster.cookiemom.dao.OrderDao.Properties.PickedUpFromCupboard;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/3/13
 * Time: 4:06 PM
 */
@OptionsMenu(R.menu.cupboard_frag)
@EFragment(R.layout.main_fragment)
public class CupboardFragment extends Fragment {

    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;

    @App
    Main main;

    @AfterViews
    void afterViews() {
        List<Card> mData = new ArrayList<Card>();
        String[] cookieTypes = Constants.CookieTypes;

        for (int i = 0; i < cookieTypes.length; i++) {
            String cookieFlavor = cookieTypes[i];

            Integer sumTotal = getTransSum(cookieFlavor);
            Integer orderSumTotal = getOrderSum(cookieFlavor);
    
            CupboardContentCard mCard =
                    new CupboardContentCard(
                            getActivity(),
                            sumTotal,
                            orderSumTotal
                    );

            addCardThumbNail(cookieFlavor, mCard);
            addCardHeader(cookieFlavor, mCard, i);
            mData.add(mCard);
        }

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
        cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
        cardView.setAdapter(cardArrayAdapter);
    }

    private Integer getOrderSum(String cookieFlavor) {
        Integer orderSumTotal = 0;
        List<Order> cookieOrderList = getOrders(cookieFlavor);
        for (Order order : cookieOrderList) {
            orderSumTotal += order.getOrderedBoxes();
            Log.d("cookiemom", "Picked up flag is:" + order.getPickedUpFromCupboard().toString());
        }
        return orderSumTotal;
    }

    private Integer getTransSum(String cookieFlavor) {
        List<CookieTransactions> cookieTransactionsList = getCookieTransactions(cookieFlavor);
        Integer sumTotal = 0;
        for (CookieTransactions transaction : cookieTransactionsList) {
            sumTotal += transaction.getTransBoxes();
        }
        return sumTotal;
    }

    private void addCardHeader(String cookieFlavor, CupboardContentCard mCard, int index) {
        CookieCardHeader cardHeader = new CookieCardHeader(getActivity(), getResources().getColor(Constants.CookieColors[index]));

        //change to true to add menu.   also uncomment lines below and add code in method
        //See ScoutFragment for example

        cardHeader.setButtonOverflowVisible(false);

        //Uncomment below to add a menu to the card
        //See ScoutFragment for example.

        //            cardHeader.setPopupMenu(R.menu.cupboard_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
        //                @Override
        //                public void onMenuItemClick(BaseCard card, MenuItem item) {
        //                    selectCookieFlavorMenu(card, item);
        //                }
        //            });

        //                cardHeader.setPopupMenu(R.menu.cupboard_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
        //                    @Override
        //                    public void onMenuItemClick(BaseCard card, MenuItem item) {
        //                        selectCookieFlavorMenu(card, item);
        //                    }
        //                });
        mCard.addCardHeader(cardHeader);
        cardHeader.setTitle(cookieFlavor);
    }

    private void addCardThumbNail(String cookieFlavor, CupboardContentCard mCard) {
        CardThumbnail cardThumbnail =
                new CardThumbnail(
                        getActivity()
                );

        cardThumbnail.setDrawableResource(
                Constants.getCookieNameImages()
                        .get(cookieFlavor)
        );

        mCard.addCardThumbnail(cardThumbnail);
    }

    private List<Order> getOrders(String cookieFlavor) {
        return Main.daoSession.getOrderDao().queryBuilder()
                .where(
                        OrderCookieType.eq(cookieFlavor),
                        PickedUpFromCupboard.eq(false))
                .list();
    }

    private List<CookieTransactions> getCookieTransactions(String cookieFlavor) {
        return Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(
                        CookieType.eq(cookieFlavor))
                .list();
    }


    @OptionsItem(R.id.menu_place_cupboard_order)
    void placeOrder() {
            CupboardOrderActivity_.intent(getActivity()).startForResult(Constants.CUPBOARD_REQUEST);
    }

    @OptionsItem(R.id.menu_pickup_cupboard_order)
    void pickupOrder() {
            CupboardPickupActivity_.intent(getActivity()).startForResult(Constants.CUPBOARD_REQUEST);
    }

    @OptionsItem(R.id.menu_edit_data)
    void editData() {
        EditDataActivity_.intent(getActivity()).start();

    }

}