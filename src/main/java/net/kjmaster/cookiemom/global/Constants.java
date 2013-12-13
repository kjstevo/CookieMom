package net.kjmaster.cookiemom.global;

import net.kjmaster.cookiemom.R;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/3/13
 * Time: 7:29 PM
 */
public class Constants {


    public static final String[] CookieTypes = new String[]{
            "Thin Mint", "Samoas", "Trefoils", "Tag-a-Longs",
            "Do Si Dos", "Dulce", "Berry", "Smiles"};
    public static final int SCOUT_REQUEST = 4343;
    public static final int ADD_BOOTH_REQUEST_CODE = 888;
    public static final int ADD_BOOTH_RESULT_OK = 777;

    //    private SQLiteDatabase db;
    public static final int ASSIGN_SCOUT_REQUEST_CODE = 111;

    private static final HashMap<String, Integer> cookieImages = new HashMap<String, Integer>();
    public static final int PLACE_CUPBOARD_ORDER = 444;
    public static final int CUPBOARD_REQUEST = 4343;
    public static final int ACTION_REQUEST = 4343;
    public static final int REMOVE_SCOUT_REQUEST_CODE = 1111;

    public static HashMap<String, Integer> getCookieNameImages() {
        cookieImages.put(CookieTypes[0], R.drawable.mint);
        cookieImages.put(CookieTypes[1], R.drawable.samoa);
        cookieImages.put(CookieTypes[2], R.drawable.trefoil);
        cookieImages.put(CookieTypes[3], R.drawable.tags);
        cookieImages.put(CookieTypes[4], R.drawable.do_si_do);
        cookieImages.put(CookieTypes[5], R.drawable.dulce);
        cookieImages.put(CookieTypes[6], R.drawable.berry);
        cookieImages.put(CookieTypes[7], R.drawable.smile);
        return cookieImages;


    }
}
