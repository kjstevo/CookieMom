package net.kjmaster.cookiemom.global;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultLong;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/21/13
 * Time: 2:35 PM
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface ISettings {


    @DefaultString("My Scout")
    String ScoutName();

    @DefaultLong(-1)
    long ScoutId();


    @DefaultString("scouts-db")
    String dbName();


    @DefaultString("scouts-db")
    String dbCookieMomName();


    @DefaultString("personal-db")
    String dbPersonalName();


    @DefaultString("")
    String CookieColors();


    @DefaultString("Smiles,Trefoils,Do Si Dos,Samoas,Dulce,Berry,Tag-a-Longs,Thin Mint")
    String CookieList();

    @DefaultBoolean(false)
    boolean useAutoFillCases();

    @DefaultBoolean(false)
    boolean useCustomCookies();

    @DefaultString("0,0,0,0,0,0,0,0")
    String MediumBoothAmts();


    @DefaultString("0,0,0,0,0,0,0,0")
    String LargeBoothAmts();


    @DefaultString("0,0,0,0,0,0,0,0")
    String SmallBoothAmts();

    long CookieYear();

    @DefaultBoolean(false)
    boolean isDefaultScoutSet();


}
