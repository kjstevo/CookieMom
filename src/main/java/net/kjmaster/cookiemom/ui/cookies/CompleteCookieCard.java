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

