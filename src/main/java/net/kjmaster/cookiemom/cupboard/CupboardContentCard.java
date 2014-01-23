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

package net.kjmaster.cookiemom.cupboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;

import it.gmariotti.cardslib.library.internal.Card;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 2:10 PM
 */
public class CupboardContentCard extends Card {

    private final int boxesOnHand;

    private final int boxesOnOrder;


    public CupboardContentCard(Context context, int boxesOnHand, int boxesOnOrder) {
        super(context, R.layout.cupboard_cookie_amount_no_exopected_layout);

        this.boxesOnHand = boxesOnHand;
        this.boxesOnOrder = boxesOnOrder;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        LinearLayout mExpectedBox = (LinearLayout) parent.findViewById(R.id.expected_panel);
        TextView mBoxesOnHand = (TextView) parent.findViewById(R.id.boxes_on_hand);
        if (mBoxesOnHand != null) {
            mBoxesOnHand.setText(String.valueOf(boxesOnHand));
        }

        TextView mBoxesOnOrder = (TextView) parent.findViewById(R.id.boxes_on_order);


        if (mBoxesOnOrder != null) {
            mBoxesOnOrder.setText(String.valueOf(boxesOnOrder));

        }
        if (mExpectedBox != null) {
            assert mBoxesOnOrder != null;
            if (mBoxesOnOrder.getText().equals("0")) {
                mExpectedBox.setVisibility(View.INVISIBLE);
            } else {
                mExpectedBox.setVisibility(View.VISIBLE);
            }

        }


    }
}
