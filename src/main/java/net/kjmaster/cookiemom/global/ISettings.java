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
@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface ISettings {

    @DefaultString("My Scout")
    String ScoutName();

    @DefaultLong(-1)
    long ScoutId();

    @DefaultString("Thin Mint,Samoas,Trefoils,Tag-a-Longs,Do Si Dos,Dulce,Berry,Smiles")
    String CookieList();

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
