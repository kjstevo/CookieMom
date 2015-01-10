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

package net.kjmaster.cookiemom.summary.stat.totals;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;

import java.text.NumberFormat;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * This class provides a simple card as Google Now SummaryStatScout
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatTotalsCard extends Card {


    public SummaryStatTotalsCard(Context context) {
        super(context, R.layout.summary_stat_totals_card_inner_layout);
        init();

    }

    private void init() {
        //Add Header
        CardHeader header = new CardHeader(getContext());
        header.setButtonExpandVisible(false);
        header.setTitle(getContext().getString(R.string.totals));
        addCardHeader(header);

        //Add expand
//        CardExpand expand = new GoogleNowExpandCard(getContext());
//        addCardExpand(expand);

//        //Add onClick Listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=" , Toast.LENGTH_LONG).show();
//            }
//        });

//        //Add swipe Listener
//        setOnSwipeListener(new OnSwipeListener() {
//            @Override
//            public void onSwipe(Card card) {
//                Toast.makeText(getContext(), "Card removed", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView boxesView = (TextView) parent.findViewById(R.id.summary_stat_totals_boxes_text);
        TextView cashView = (TextView) parent.findViewById(R.id.summary_stat_totals_paid_text);
        TextView gocView = (TextView) parent.findViewById(R.id.summary_stat_totals_goc_text);
        TextView gocCaption = (TextView) parent.findViewById(R.id.summary_stat_totals_goc_caption);
        int totalBoxes = 0;
        double totalBoxesValue = 0.00;
        double totalCash = 0.0;
        double totalGoc = 0.0;

        for (CookieTransactions transaction : Main.daoSession.getCookieTransactionsDao().loadAll()) {
            totalCash += transaction.getTransCash();
        }
        List<CookieTransactions> transactionsList = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(CookieTransactionsDao.Properties.TransScoutId.lt(0),
                        CookieTransactionsDao.Properties.TransBoothId.lt(0))
                .list();
        for (CookieTransactions transaction : transactionsList) {
            totalBoxes += transaction.getTransBoxes();
            try {
                totalBoxesValue += transaction.getTransBoxes() * Main.getCookieCosts(transaction.getCookieType()).doubleValue();
            } catch (Exception e) {
                Log.d("kjmaster", "No cookie type");
            }
        }
        totalGoc = totalCash - (totalBoxesValue);
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        fmt.setMaximumFractionDigits(0);
        fmt.setMinimumFractionDigits(0);

        boxesView.setText(String.valueOf(totalBoxes));
        cashView.setText(String.valueOf(fmt.format(totalCash)));
        if (totalGoc < 0) {
            totalGoc = totalGoc * -1;
            gocCaption.setText(mContext.getString(R.string.deficit));
        }
        gocView.setText(String.valueOf(fmt.format(totalGoc)));


    }


    //------------------------------------------------------------------------------------------


}
