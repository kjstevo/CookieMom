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

    public static String[] mCookieTypes;
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

    public void updateCookieTypes(String cookieTypes) {
        mCookieTypes = TextUtils.split(cookieTypes, ",");
        if (mCookieTypes.length != getResources().getStringArray(R.array.cookie_names_array).length) {
            mCookieTypes = getResources().getStringArray(R.array.cookie_names_array);

        }
        miSettings.CookieList().put(cookieTypes);
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

    public static DaoSession daoSession = null;
}
