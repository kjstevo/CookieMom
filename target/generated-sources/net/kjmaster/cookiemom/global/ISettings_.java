//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.global;

import android.content.Context;
import android.content.SharedPreferences;
import com.googlecode.androidannotations.api.sharedpreferences.BooleanPrefEditorField;
import com.googlecode.androidannotations.api.sharedpreferences.BooleanPrefField;
import com.googlecode.androidannotations.api.sharedpreferences.EditorHelper;
import com.googlecode.androidannotations.api.sharedpreferences.LongPrefEditorField;
import com.googlecode.androidannotations.api.sharedpreferences.LongPrefField;
import com.googlecode.androidannotations.api.sharedpreferences.SharedPreferencesHelper;
import com.googlecode.androidannotations.api.sharedpreferences.StringPrefEditorField;
import com.googlecode.androidannotations.api.sharedpreferences.StringPrefField;

public final class ISettings_
    extends SharedPreferencesHelper
{


    public ISettings_(Context context) {
        super(context.getSharedPreferences("ISettings", 0));
    }

    public ISettings_.ISettingsEditor_ edit() {
        return new ISettings_.ISettingsEditor_(getSharedPreferences());
    }

    public StringPrefField ScoutName() {
        return stringField("ScoutName", "My Scout");
    }

    public LongPrefField ScoutId() {
        return longField("ScoutId", -1L);
    }

    public StringPrefField dbName() {
        return stringField("dbName", "scouts-db");
    }

    public StringPrefField dbCookieMomName() {
        return stringField("dbCookieMomName", "scouts-db");
    }

    public StringPrefField dbPersonalName() {
        return stringField("dbPersonalName", "personal-db");
    }

    public StringPrefField CookieColors() {
        return stringField("CookieColors", "");
    }

    public StringPrefField CookieList() {
        return stringField("CookieList", "Thin Mint,Samoas,Trefoils,Tag-a-Longs,Do Si Dos,Dulce,Berry,Smiles");
    }

    public StringPrefField MediumBoothAmts() {
        return stringField("MediumBoothAmts", "0,0,0,0,0,0,0,0");
    }

    public StringPrefField LargeBoothAmts() {
        return stringField("LargeBoothAmts", "0,0,0,0,0,0,0,0");
    }

    public StringPrefField SmallBoothAmts() {
        return stringField("SmallBoothAmts", "0,0,0,0,0,0,0,0");
    }

    public LongPrefField CookieYear() {
        return longField("CookieYear", 0L);
    }

    public BooleanPrefField isDefaultScoutSet() {
        return booleanField("isDefaultScoutSet", false);
    }

    public final static class ISettingsEditor_
        extends EditorHelper<ISettings_.ISettingsEditor_>
    {


        ISettingsEditor_(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> ScoutName() {
            return stringField("ScoutName");
        }

        public LongPrefEditorField<ISettings_.ISettingsEditor_> ScoutId() {
            return longField("ScoutId");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> dbName() {
            return stringField("dbName");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> dbCookieMomName() {
            return stringField("dbCookieMomName");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> dbPersonalName() {
            return stringField("dbPersonalName");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> CookieColors() {
            return stringField("CookieColors");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> CookieList() {
            return stringField("CookieList");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> MediumBoothAmts() {
            return stringField("MediumBoothAmts");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> LargeBoothAmts() {
            return stringField("LargeBoothAmts");
        }

        public StringPrefEditorField<ISettings_.ISettingsEditor_> SmallBoothAmts() {
            return stringField("SmallBoothAmts");
        }

        public LongPrefEditorField<ISettings_.ISettingsEditor_> CookieYear() {
            return longField("CookieYear");
        }

        public BooleanPrefEditorField<ISettings_.ISettingsEditor_> isDefaultScoutSet() {
            return booleanField("isDefaultScoutSet");
        }

    }

}
