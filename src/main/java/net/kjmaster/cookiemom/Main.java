package net.kjmaster.cookiemom;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.googlecode.androidannotations.annotations.EApplication;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.dao.DaoMaster;
import net.kjmaster.cookiemom.dao.DaoSession;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 12:26 AM
 */
@EApplication
public class Main extends Application {

    private static String[] mCookieTypes;
    @Pref
    ISettings_ miSettings;

    @StringArrayRes(R.array.cookie_names_array)
    String[] CookieTypes;

    public static String dbName;
    public static boolean mIsPersonal = false;


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public static String[] getCookieTypes() {
        return mCookieTypes;
    }

    public void switchDb(String newDbName) {
        dbName = newDbName;

        mCookieTypes = CookieTypes;
        init();


    }

    private void init() {

        dbName = miSettings.dbName().get();

        if (dbName == null) {
            createDBSettings();
        }


        initCookieList(miSettings.CookieList().get());
        initStrings(dbName.equals(miSettings.dbPersonalName().get()));
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    private void initCookieList(String cookieListString) {
        if (cookieListString != null) {
            if (miSettings.useCustomCookies().get()) {
                mCookieTypes = TextUtils.split(cookieListString, ",");
            } else {
                mCookieTypes = getResources().getStringArray(R.array.cookie_names_array);
                miSettings.CookieList().put(TextUtils.join(",", mCookieTypes));
            }
        } else {

            miSettings.CookieList().put(TextUtils.join(",", CookieTypes));
            mCookieTypes = CookieTypes;
        }
    }

    private void initStrings(boolean isPersonal) {
        if (isPersonal) {
            mIsPersonal = true;
            Constants.setADD_SCOUT(getString(R.string.add_customer_title));

        } else {
            mIsPersonal = false;
            Constants.setADD_SCOUT(getString(R.string.add_scout));
        }

    }

    private void createDBSettings() {
        miSettings.dbCookieMomName().put("scouts-db");
        miSettings.dbPersonalName().put("personal-db");
        miSettings.dbName().put("scouts-db");
        dbName = "scouts-db";

    }

    public void RefreshCookieList() {
        android.os.Process.sendSignal(android.os.Process.myPid(), android.os.Process.SIGNAL_QUIT);

    }

    public static DaoSession daoSession = null;
}
