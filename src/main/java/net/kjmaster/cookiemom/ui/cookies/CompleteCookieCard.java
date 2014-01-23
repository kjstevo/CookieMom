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

import net.kjmaster.cookiemom.global.Constants;

import it.gmariotti.cardslib.library.internal.CardThumbnail;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/27/13
 * Time: 5:03 AM
 */
public class CompleteCookieCard {

    public static CookieAmountContentCard CreateCompleteCookieCard(CookieAmountContentCard mCard, String cookieType, Context context, int mColor) {


        CookieCardHeader cardHeader = new CookieCardHeader(context, mColor);
        cardHeader.setTitle(cookieType);
        mCard.addCardHeader(cardHeader);

        CardThumbnail cardThumbnail = new CardThumbnail(context);
        cardThumbnail.setDrawableResource(Constants.getCookieNameImages().get(cookieType));
        mCard.addCardThumbnail(cardThumbnail);
        return mCard;
    }
}

