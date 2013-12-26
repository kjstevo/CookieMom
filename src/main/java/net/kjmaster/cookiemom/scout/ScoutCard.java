package net.kjmaster.cookiemom.scout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.R;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/11/13
 * Time: 7:27 PM
 */
public class ScoutCard extends Card {
    private String mTitleText = "0";
    private String altCount = "0";
    private String paidText = "$0.00";
    private String owedText = "$0.00";
    private String totalText = "$0.00";
    private boolean isReadyForPickup = false;
    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected TextView mCheckbox;
    protected TextView mTotalAmount;
    protected TextView mScoutPaidText;
    protected TextView mScoutOwedText;

    /**
     * Constructor with a custom inner layout
     *
     * @param context
     */
    public ScoutCard(Context context) {
        this(context, R.layout.scout_inner_content);
    }

    /**
     * @param context
     * @param innerLayout
     */
    @SuppressWarnings("JavaDoc")
    public ScoutCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init() {

        // No Header
        addPartialOnClickListener(CLICK_LISTENER_CONTENT_VIEW, new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                View view1 = card.getCardView().findViewById(R.id.card_content_expand_layout);

                if (card.isExpanded()) {
                    card.getCardView().getOnExpandListAnimatorListener().onCollapseStart(card.getCardView(), view1);
                } else {
                    card.getCardView().getOnExpandListAnimatorListener().onExpandStart(card.getCardView(), view1);
                }
            }
        });
    }

    public void setAltCount(String altCount) {
        this.altCount = altCount;
    }

    @Override
    public void setTitle(String title) {
        this.mTitleText = title;

        super.setTitle(title);    // To change body of overridden methods use File | Settings | File Templates.
    }


    public void setOwedText(String cashText) {
        this.owedText = cashText;
    }

    public void setPaidText(String cashText) {
        this.paidText = cashText;
    }

    public void setTotalText(String totalText) {
        this.totalText = totalText;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        // Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_main_inner_simple_title);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.with_scouts_count);
        mCheckbox = (TextView) parent.findViewById(R.id.ready_for_pickup);
        mScoutOwedText = (TextView) parent.findViewById(R.id.owed_text);
        mScoutPaidText = (TextView) parent.findViewById(R.id.paid_text);
        mTotalAmount = (TextView) parent.findViewById(R.id.total_due);

        if (mTitle != null) {
            mTitle.setText(mTitleText);
        }

        if (mSecondaryTitle != null) {
            mSecondaryTitle.setText(altCount);
        }

        if (mCheckbox != null) {
            if (isReadyForPickup) {
                mCheckbox.setVisibility(View.VISIBLE);
            }
        }
        if (mScoutPaidText != null) {
            mScoutPaidText.setText(paidText);
        }

        if (mScoutOwedText != null) {
            mScoutOwedText.setText(owedText);
        }

        if (mTotalAmount != null) {
            mTotalAmount.setText(totalText);
        }
    }

    public boolean isReadyForPickup() {
        return isReadyForPickup;
    }

    public void setReadyForPickup(boolean readyForPickup) {
        isReadyForPickup = readyForPickup;
    }
}
