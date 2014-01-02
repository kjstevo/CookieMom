package net.kjmaster.cookiemom.global;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultLong;
import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/21/13
 * Time: 2:35 PM
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface ISettings {

    @NotNull
    @DefaultString("My Scout")
    String ScoutName();

    @DefaultLong(-1)
    long ScoutId();

    @NotNull
    @DefaultString("scouts-db")
    String dbName();

    @NotNull
    @DefaultString("scouts-db")
    String dbCookieMomName();

    @NotNull
    @DefaultString("personal-db")
    String dbPersonalName();


    @NotNull
    @DefaultString("")
    String CookieColors();


    @NotNull
    @DefaultString("Thin Mint,Samoas,Trefoils,Tag-a-Longs,Do Si Dos,Dulce,Berry,Smiles")
    String CookieList();

    @NotNull
    @DefaultString("0,0,0,0,0,0,0,0")
    String MediumBoothAmts();

    @NotNull
    @DefaultString("0,0,0,0,0,0,0,0")
    String LargeBoothAmts();

    @NotNull
    @DefaultString("0,0,0,0,0,0,0,0")
    String SmallBoothAmts();

    long CookieYear();

    @DefaultBoolean(false)
    boolean isDefaultScoutSet();


}
