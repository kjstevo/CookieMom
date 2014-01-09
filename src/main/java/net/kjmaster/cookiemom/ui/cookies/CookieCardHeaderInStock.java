package net.kjmaster.cookiemom.ui.cookies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kjmaster.cookiemom.global.Constants;

import it.gmariotti.cardslib.library.internal.CardHeader;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/22/13
 * Time: 9:00 PM
 */
public class CookieCardHeaderInStock extends CardHeader {
    private final int invTotal;
    private final String cookieType;
    private final int color;

    public CookieCardHeaderInStock(Context context, int invTotal, String cookieType, int color) {
        super(context, net.kjmaster.cookiemom.R.layout.ui_cookie_base_header_layout);
        this.invTotal = invTotal;
        this.cookieType = cookieType;
        this.color = color;

    }


    public int getInvTotal() {
        return invTotal;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView headerTitle = (TextView) parent.findViewById(net.kjmaster.cookiemom.R.id.card_header_inner_simple_title);
        TextView invHeader = (TextView) parent.findViewById(net.kjmaster.cookiemom.R.id.in_stock_header);
        if (invHeader != null) {
            invHeader.setText(String.valueOf(this.invTotal) + " in stock");
        }
        if (headerTitle != null) {
            headerTitle.setText(cookieType);
            for (int i = 0; i < Constants.CookieTypes.length; i++) {
                String cookie = Constants.CookieTypes[i];
                if (cookie.equals(cookieType)) {
                    headerTitle.setTextColor(getContext().getResources().getColor(color));
                }
            }


        }

        super.setupInnerViewElements(parent, view);
    }
}
