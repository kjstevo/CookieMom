package net.kjmaster.cookiemom.ui.cookies;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    private final int actualAmount;
    private int actualAmountCases;
    private final int amountExpected;
    private int amountExpectedCases;
    private final boolean autoFillEditFields;
    private final boolean showExpected;
    private final boolean isBoxes;
    private TextView mCookieAmountCases;
    private TextView mAmountExpectedCases;
    private TextView mAmountCasesCaption;

    public CookieAmountContentCard(
            Context context,
            int amountExpected,
            boolean autoFillEditFields,
            boolean showExpected,
            boolean isBoxes
    ) {

        this(
                context,
                R.layout.ui_cookie_amount_layout,
                amountExpected,
                autoFillEditFields,
                showExpected,
                0,
                isBoxes
        );

    }

    public CookieAmountContentCard(
            Context context,
            int amountExpected,
            boolean autoFillEditFields,
            boolean showExpected,
            int actualAmount,
            boolean isBoxes
    ) {
        this(
                context,
                R.layout.ui_cookie_amount_layout,
                amountExpected,
                autoFillEditFields,
                showExpected,
                actualAmount,
                isBoxes
        );

    }

    public CookieAmountContentCard(
            Context context,
            int innerLayout,
            int amountExpected,
            boolean autoFillEditFields,
            boolean showExpected,
            int actualAmount,
            boolean isBoxes) {

        super(context, innerLayout);

        this.amountExpected = amountExpected;
        this.autoFillEditFields = autoFillEditFields;
        this.actualAmount = actualAmount;
        this.showExpected = showExpected;
        this.isBoxes = isBoxes;

        calcCasesExpected(amountExpected);
        calcCasesActual(actualAmount);

    }

    private void calcCasesActual(int actualAmount) {
        if (actualAmount % 12 > 0) {

            actualAmountCases = (actualAmount / 12) + 1;

        } else {

            actualAmountCases = actualAmount / 12;

        }
    }

    private void calcCasesExpected(int amountExpected) {
        if (amountExpected % 12 > 0) {

            amountExpectedCases = (amountExpected / 12) + 1;

        } else {

            amountExpectedCases = (amountExpected / 12);

        }
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements

        TextView mAmountExpected = (TextView) parent.findViewById(R.id.amount_expected);
        mAmountExpectedCases = (TextView) parent.findViewById(R.id.cases_expected);
        TextView mCookieAmount = (TextView) parent.findViewById(R.id.cookie_amount);
        mAmountCasesCaption = (TextView) parent.findViewById(R.id.amount_cases_caption);

        mCookieAmount.addTextChangedListener(new CookieTextWatcher());

        mCookieAmountCases = (TextView) parent.findViewById(R.id.edit_text_cases);

        mCookieAmount.setTag(this.getCardHeader().getTitle());

        LinearLayout mExpectedPanel = (LinearLayout) parent.findViewById(R.id.expected_panel);

        if (mExpectedPanel != null) {

            if (showExpected) {

                mExpectedPanel.setVisibility(View.VISIBLE);

            } else {

                mExpectedPanel.setVisibility(View.GONE);

            }
        }
        if (mAmountExpected != null) {
            try {
                if (autoFillEditFields) {

                    mCookieAmount.setText(String.valueOf(amountExpected));
                    updateCaeaActual();

                } else {

                    mCookieAmount.setText(String.valueOf(actualAmount));
                    mCookieAmountCases.setText(String.valueOf(actualAmountCases));
                }

                mAmountExpected.setText(String.valueOf(amountExpected));
                updateCasesExpected();

            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
        if (!isBoxes) {

            mCookieAmountCases.setVisibility(View.VISIBLE);
            mAmountCasesCaption.setVisibility(View.VISIBLE);


        }
    }

    private void updateCasesExpected() {
        mAmountExpectedCases.setText(String.valueOf(amountExpectedCases));
    }

    private void updateCaeaActual() {
        mCookieAmountCases.setText(String.valueOf(amountExpectedCases));
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

