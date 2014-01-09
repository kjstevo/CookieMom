package net.kjmaster.cookiemom;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.googlecode.androidannotations.annotations.EApplication;
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

    @Pref
    ISettings_ miSettings;


    public static String dbName;
    public static boolean mIsPersonal = false;


    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }


    public void switchDb(String newDbName) {
        dbName = newDbName;

        init();


    }

    private void init() {

        this.dbName = miSettings.dbName().get();
        if (this.dbName == null) {
            createDBSettings();
        }

        initStrings(this.dbName.equals(miSettings.dbPersonalName().get()));
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

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
        this.dbName = "scouts-db";

    }


    public static DaoSession daoSession = null;
}
