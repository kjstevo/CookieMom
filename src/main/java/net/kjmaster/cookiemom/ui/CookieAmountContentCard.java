package net.kjmaster.cookiemom.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.R;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public class CookieAmountContentCard extends Card {
    private final int actualAmount;
    private final int actualAmountCases;
    private final int amountExpected;
    private final int amountExpectedCases;
    private final boolean autoFillEditFields;
    private final boolean showExpected;
    private final boolean isBoxes;

    public CookieAmountContentCard(Context context, int amountExpected, boolean autoFillEditFields, boolean showExpected, boolean isBoxes) {
        this(context, R.layout.cookie_amount_layout, amountExpected, autoFillEditFields, showExpected, 0, isBoxes);

    }

    public CookieAmountContentCard(Context context, int amountExpected, boolean autoFillEditFields, boolean showExpected, int actualAmount, boolean isBoxes) {
        this(context, R.layout.cookie_amount_layout, amountExpected, autoFillEditFields, showExpected, actualAmount, isBoxes);

    }

    public CookieAmountContentCard(Context context, int innerLayout, int amountExpected, boolean autoFillEditFields, boolean showExpected, int actualAmount, boolean isBoxes) {
        super(context, innerLayout);
        this.amountExpected = amountExpected;
        this.autoFillEditFields = autoFillEditFields;
        this.actualAmount = actualAmount;
        this.showExpected = showExpected;
        this.isBoxes = isBoxes;
        if (amountExpected % 12 > 0) {
            amountExpectedCases = amountExpected / 12 + 1;
        } else {
            amountExpectedCases = amountExpected / 12;
        }
        if (actualAmount % 12 > 0) {
            actualAmountCases = (actualAmount / 12) + 1;
        } else {
            actualAmountCases = actualAmount / 12;
        }

    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements

        TextView mAmountExpected = (TextView) parent.findViewById(R.id.amount_expected);
        TextView mAmountExpectedCases = (TextView) parent.findViewById(R.id.cases_expected);
        EditText mCookieAmount = (EditText) parent.findViewById(R.id.cookie_amount);
        EditText mCookieAmountCases = (EditText) parent.findViewById(R.id.edit_text_cases);
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
                    mCookieAmountCases.setText(String.valueOf(amountExpectedCases));

                } else {
                    mCookieAmount.setText(String.valueOf(actualAmount));
                    mCookieAmountCases.setText(String.valueOf(actualAmountCases));
                }
                mAmountExpected.setText(String.valueOf(amountExpected));
                mAmountExpectedCases.setText(String.valueOf(amountExpectedCases));


            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
        if (!isBoxes) {
            assert mAmountExpected != null;
            mAmountExpected.setVisibility(View.GONE);
            mAmountExpectedCases.setVisibility(View.VISIBLE);
            mCookieAmountCases.setVisibility(View.VISIBLE);
            mCookieAmount.setVisibility(View.GONE);
        }
    }
}

