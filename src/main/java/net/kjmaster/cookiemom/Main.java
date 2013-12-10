package net.kjmaster.cookiemom;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.googlecode.androidannotations.annotations.EApplication;
import net.kmaster.cookiemom.dao.DaoMaster;
import net.kmaster.cookiemom.dao.DaoSession;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/5/13
 * Time: 12:26 AM
 */
@EApplication
public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "scouts-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public static DaoSession daoSession = null;
}
