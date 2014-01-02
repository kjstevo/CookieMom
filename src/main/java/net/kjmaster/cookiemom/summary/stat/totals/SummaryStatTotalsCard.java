/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package net.kjmaster.cookiemom.summary.stat.totals;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

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
    public void setupInnerViewElements(@NotNull ViewGroup parent, View view) {

        TextView boxesView = (TextView) parent.findViewById(R.id.summary_stat_totals_boxes_text);
        TextView cashView = (TextView) parent.findViewById(R.id.summary_stat_totals_paid_text);
        TextView gocView = (TextView) parent.findViewById(R.id.summary_stat_totals_goc_text);
        TextView gocCaption = (TextView) parent.findViewById(R.id.summary_stat_totals_goc_caption);
        int totalBoxes = 0;
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
        }
        totalGoc = totalCash - (totalBoxes * 4);
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
