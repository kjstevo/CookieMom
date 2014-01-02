package net.kjmaster.cookiemom.ui.cookies;

import android.content.Context;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import net.kjmaster.cookiemom.global.Constants;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/27/13
 * Time: 5:03 AM
 */
public class CompleteCookieCard {

    private static int color;

    @NotNull
    public static CookieAmountContentCard CreateCompleteCookieCard(@NotNull CookieAmountContentCard mCard, String cookieType, Context context, int colorolor) {


        CookieCardHeader cardHeader = new CookieCardHeader(context, color);
        cardHeader.setTitle(cookieType);
        mCard.addCardHeader(cardHeader);

        CardThumbnail cardThumbnail = new CardThumbnail(context);
        cardThumbnail.setDrawableResource(Constants.getCookieNameImages().get(cookieType));
        mCard.addCardThumbnail(cardThumbnail);
        return mCard;
    }
}

