package net.kjmaster.cookiemom.ui.cookies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.R;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 1/1/14
 * Time: 8:37 PM
 */
public class CookieCardHeader extends CardHeader {
    private final int mColor;

    public CookieCardHeader(Context context, int color) {
        this(context, R.layout.inner_base_header, color);


    }

    public CookieCardHeader(Context context, int innerLayout, int color) {
        super(context, innerLayout);
        this.mColor = color;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view != null) {
            TextView mTitleView = (TextView) view.findViewById(R.id.card_header_inner_simple_title);

            if (mTitleView != null) {
                mTitleView.setTextColor(parent.getResources().getColor(mColor));
                mTitleView.setText(mTitle);
            }

        }

    }
}
