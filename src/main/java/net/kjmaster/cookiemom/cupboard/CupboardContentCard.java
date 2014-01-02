package net.kjmaster.cookiemom.cupboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.R;
import org.jetbrains.annotations.NotNull;

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
    public void setupInnerViewElements(@NotNull ViewGroup parent, View view) {
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
