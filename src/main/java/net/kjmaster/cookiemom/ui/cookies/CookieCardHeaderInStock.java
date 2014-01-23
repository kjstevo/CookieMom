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
                    headerTitle.setTextColor(color);
                }
            }


        }

        super.setupInnerViewElements(parent, view);
    }
}
