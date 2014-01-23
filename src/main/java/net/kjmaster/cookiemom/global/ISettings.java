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
