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

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;

import it.gmariotti.cardslib.library.internal.Card;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public class CookieAmountContentCard extends Card {
    private int actualAmount;
    private final int amountExpected;

    private final boolean showExpected;
    private final boolean isBoxes;
    private TextView mCookieAmountCases;

    private TextView mCookieAmount;
    private TextView mAmountExpected;
    private TextView mAmountCasesCaption;
    private LinearLayout mExpectedPanel;
    private int actualCasesAmount;

    public CookieAmountContentCard(
            Context context,
            int amountExpected,
            boolean showExpected,
            boolean isBoxes
    ) {

        this(
                context,
                R.layout.ui_cookie_amount_layout,
                amountExpected,
                showExpected,
                0,
                isBoxes
        );

    }

    public CookieAmountContentCard(
            Context context,
            int amountExpected,
            boolean showExpected,
            int actualAmount,
            boolean isBoxes
    ) {
        this(
                context,
                R.layout.ui_cookie_amount_layout,
                amountExpected,
                showExpected,
                actualAmount,
                isBoxes
        );

    }

    public CookieAmountContentCard(
            Context context,
            int innerLayout,
            int amountExpected,
            boolean showExpected,
            int actualAmount,
            boolean isBoxes) {

        super(context, innerLayout);

        this.amountExpected = amountExpected;
        this.actualAmount = actualAmount;
        this.showExpected = showExpected;
        this.isBoxes = isBoxes;


    }


    private void calcCasesActual(int amt) {
        if ((amt % 12) > 0) {
            actualCasesAmount = (amt / 12) + 1;

        } else {

            actualCasesAmount = (amt / 12);

        }
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements

        mAmountExpected = (TextView) parent.findViewById(R.id.amount_expected);

        mCookieAmount = (TextView) parent.findViewById(R.id.cookie_amount);
        mAmountCasesCaption = (TextView) parent.findViewById(R.id.amount_cases_caption);
        mCookieAmountCases = (TextView) parent.findViewById(R.id.edit_text_cases);
        mExpectedPanel = (LinearLayout) parent.findViewById(R.id.expected_panel);

        mCookieAmount.addTextChangedListener(new CookieTextWatcher());

        mCookieAmount.setTag(this.getCardHeader().getTitle());

        updateActualAmount();

        if (showExpected) {
            updateAmountExpected();

        } else {
            if (mExpectedPanel != null) {
                mExpectedPanel.setVisibility(View.GONE);
            }
        }

        if (!isBoxes) {
            calcCasesActual(actualAmount);
            updateCaeaActual();
        }
    }


    private void updateAmountExpected() {
        if (mExpectedPanel != null) {
            mExpectedPanel.setVisibility(View.VISIBLE);
        }
        if (mAmountExpected != null) {
            mAmountExpected.setText(String.valueOf(amountExpected));
        }
    }

    private void updateActualAmount() {
        if (mCookieAmount != null) {
            mCookieAmount.setText(String.valueOf(actualAmount));
        }
    }


    private void updateCaeaActual() {
        if (mCookieAmountCases != null) {
            mCookieAmountCases.setVisibility(View.VISIBLE);
            mCookieAmountCases.setText(String.valueOf(actualCasesAmount) + "(" + String.valueOf(actualCasesAmount * 12) + "bxs)");
        }
        if (mAmountCasesCaption != null) {
            mAmountCasesCaption.setVisibility(View.VISIBLE);
        }
    }


    private class CookieTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            calcCasesActual(Integer.valueOf(s.toString()));
            updateCaeaActual();
        }
    }
}

