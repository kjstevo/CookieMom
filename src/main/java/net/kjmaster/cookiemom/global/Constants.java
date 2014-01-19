package net.kjmaster.cookiemom.global;

import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/3/13
 * Time: 7:29 PM
 */
@EBean
public class Constants {

    @App
    Main main;

    //    public static final String[] CookieTypes = new String[]{
//            "Smiles", "Trefoils", "Do Si Dos",
//            "Samoas", "Dulce", "Berry",
//            "Tag-a-Longs", "Thin Mint"
//    };
    public static String[] CookieTypes = Main.getCookieTypes();


    public static final int[] CookieColors = new int[]{
            R.color.color_smiles, R.color.color_trefoils,
            R.color.color_dosidos, R.color.color_samoas,
            R.color.color_dulce, R.color.color_berry,
            R.color.color_tags, R.color.color_thin_mints
    };

    public static HashMap<String, Integer> getCookieNameImages() {
        cookieImages.put(CookieTypes[7], R.drawable.smile);
        cookieImages.put(CookieTypes[2], R.drawable.trefoil);
        cookieImages.put(CookieTypes[4], R.drawable.do_si_do);
        cookieImages.put(CookieTypes[1], R.drawable.samoa);
        cookieImages.put(CookieTypes[5], R.drawable.dulce);
        cookieImages.put(CookieTypes[6], R.drawable.berry);
        cookieImages.put(CookieTypes[3], R.drawable.tags);
        cookieImages.put(CookieTypes[0], R.drawable.mint);


        return cookieImages;


    }

    public static final int SETTINGS_REQUEST = 9090;
    public static final int SETTINGS_RESULT_DIRTY = 9696;
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
    public static final int BOOTH_ORDER = 9876;
    public static final int EAT_COOKIES = 2232;
    public static final int ADD_SCOUT_REQUEST = 24601;

    public static String getSCOUT() {
        return SCOUT;
    }

    public static void setSCOUT(String SCOUT) {
        Constants.SCOUT = SCOUT;
    }

    private static String SCOUT;

    public static String getADD_SCOUT() {
        return ADD_SCOUT;
    }

    public static void setADD_SCOUT(String ADD_SCOUT) {
        Constants.ADD_SCOUT = ADD_SCOUT;
    }

    public static String ADD_SCOUT;


    public static long CalculateNegativeBoothId(long boothId) {
        return ((-1 * boothId) - 100);
    }

    public static long CalculatePositiveBoothId(long scoutId) {
        return ((scoutId + 100) * -1);
    }


}
