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

import net.kjmaster.cookiemom.R;

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
                mTitleView.setTextColor(mColor);
                mTitleView.setText(mTitle);
            }

        }

    }
}
