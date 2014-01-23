/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.kjmaster.cookiemom.booth;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.add.AddBoothActivity_;
import net.kjmaster.cookiemom.booth.checking.BoothCheckInActivity_;
import net.kjmaster.cookiemom.booth.checking.BoothCheckOutActivity_;
import net.kjmaster.cookiemom.booth.expander.CustomBoothExpander;
import net.kjmaster.cookiemom.booth.order.BoothOrderActivity_;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothAssignments;
import net.kjmaster.cookiemom.dao.BoothAssignmentsDao;
import net.kjmaster.cookiemom.dao.BoothDao;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.select.SelectScoutListActivity_;

import java.util.ArrayList;
import java.util.List;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

@OptionsMenu(R.menu.booth_frag)
@EFragment(R.layout.main_fragment)
public class BoothFragment
        extends Fragment implements ISimpleDialogListener {


    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;


    @AfterViews
    void afterViews() {

        BoothDao boothDao = Main.daoSession.getBoothDao();

        List booths = boothDao.queryBuilder()
                .list();
        if (!booths.isEmpty()) {
            List<Card> mData = new ArrayList<Card>();
            for (Object booth1 : booths) {
                final Booth booth = (Booth) booth1;
                BoothContentCard mCard = new BoothContentCard(getActivity(), booth);


                CardHeader cardHeader = new CardHeader(getActivity());
                cardHeader.setButtonOverflowVisible(true);
                cardHeader.setButtonExpandVisible(true);
                cardHeader.setPopupMenu(R.menu.booth_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                    @Override
                    public void onMenuItemClick(BaseCard card, MenuItem item) {
                        selectBoothMenu(card, item);
                    }
                });


                mCard.addCardHeader(cardHeader);
                cardHeader.setTitle(booth.getBoothLocation());
                mCard.setTitle("More Info");
                CustomBoothExpander customExpander = new CustomBoothExpander(getActivity(), booth);

                mCard.addCardExpand(customExpander);
                mData.add(mCard);
            }

            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
            cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
            cardView.setAdapter(cardArrayAdapter);
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        //net.kjmaster.cookiemom.booth.BoothFragment.onHiddenChanged returns void

        //12/16/13
        super.onHiddenChanged(hidden);
    }

    private void selectBoothMenu(BaseCard card, MenuItem item) {
        Booth booth = ((BoothContentCard) card.getParentCard()).getBooth();
        switch (item.getItemId()) {

            case R.id.menu_assign_scout:
                SelectScoutListActivity_.intent(getActivity()).requestCode(Constants.ASSIGN_SCOUT_REQUEST_CODE).targetId(booth.getId()).startForResult(Constants.ASSIGN_SCOUT_REQUEST_CODE);
                break;

            case R.id.menu_remove_scout:
                SelectScoutListActivity_.intent(getActivity()).requestCode(Constants.REMOVE_SCOUT_REQUEST_CODE).targetId(booth.getId()).startForResult(Constants.REMOVE_SCOUT_REQUEST_CODE);
                break;

            case R.id.menu_booth_delete:
                SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                        .setTitle("WARNING!")
                        .setMessage("Are you sure you want to delete this booth? This action can not be undone!")
                        .setPositiveButtonText("Delete Booth")
                        .setNegativeButtonText("Cancel")
                        .setCancelable(true)
                        .setTargetFragment(this, booth.getId().intValue())
                        .setRequestCode(booth.getId().intValue())
                        .setTag(booth.getId().toString())
                        .show();
                break;

            case R.id.menu_booth_checkout:
                BoothCheckOutActivity_.intent(getActivity()).BoothId(booth.getId()).startForResult(Constants.BOOTH_ORDER);

                break;

            case R.id.menu_booth_checkin:
                BoothCheckInActivity_.intent(getActivity()).BoothId(booth.getId()).startForResult(Constants.BOOTH_ORDER);

                break;
            case R.id.menu_booth_order:
                BoothOrderActivity_.intent(getActivity()).boothId(booth.getId()).requestCode(Constants.BOOTH_ORDER).startForResult(Constants.BOOTH_ORDER);

            default:

        }
    }


    @OptionsItem(R.id.menu_add_booth)
    void addBooth() {
        AddBoothActivity_.intent(getActivity()).startForResult(Constants.ADD_BOOTH_REQUEST_CODE);
    }


    @Override
    public void onPositiveButtonClicked(int requestCode) {
        BoothDao boothDao = Main.daoSession.getBoothDao();

        deleteBoothAssignments(requestCode);

        deleteBoothTrans(requestCode);

        deleteBoothOrders(requestCode);

        deleteBooth(requestCode, boothDao);

        afterViews();

    }

    private void deleteBooth(long requestCode, BoothDao boothDao) {
        boothDao.delete(boothDao.loadByRowId(requestCode));
    }

    private void deleteBoothAssignments(long requestCode) {
        List<BoothAssignments> list = Main.daoSession.getBoothAssignmentsDao().queryBuilder()
                .where(
                        BoothAssignmentsDao.Properties.BoothAssignBoothId.eq(requestCode))
                .list();
        if (list != null) {
            for (BoothAssignments assignments : list) {
                Main.daoSession.getBoothAssignmentsDao().delete(assignments);
            }
        }
    }

    private void deleteBoothTrans(long requestCode) {
        List<CookieTransactions> cookieTransactionsList =
                Main.daoSession.getCookieTransactionsDao().queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.TransBoothId.eq(requestCode))
                        .list();
        for (CookieTransactions cookieTransaction : cookieTransactionsList) {
            Main.daoSession.getCookieTransactionsDao().delete(cookieTransaction);
        }
    }

    private void deleteBoothOrders(long requestCode) {
        List<Order> orders = Main.daoSession.getOrderDao().queryBuilder()
                .where(OrderDao.Properties.OrderScoutId.eq(Constants.CalculateNegativeBoothId(requestCode)))
                .list();
        for (Order order : orders) {
            Main.daoSession.getOrderDao().delete(order);
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }
}



