package net.kjmaster.cookiemom.settings;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dell_Owner on 1/17/14.
 */
@EFragment(R.layout.settings_layout)
public class SettingsFragment extends Fragment {
    @ViewById()
    Spinner setting_cookie_list;

    @ViewById()
    CheckBox setting_custom_cookie_check;

    @ViewById()
    CheckBox settings_auot_fill_cases_check;

    @ViewById
    EditText settings_edit_cookie;

    @ViewById()
    Button setting_save_cookie;

    @Pref
    ISettings_ iSettings;
    public CharSequence itemSelected = "";
    public boolean isDirty=false;

    @AfterViews
    void afterViews() {
        if (TextUtils.split(iSettings.CookieList().get(), ",").length != getResources().getStringArray(R.array.cookie_names_array).length) {
            fillCookieList(TextUtils.join(",", getResources().getStringArray(R.array.cookie_names_array)));
            iSettings.CookieList().put(TextUtils.join(",", getResources().getStringArray(R.array.cookie_names_array)));
        } else {
            fillCookieList(iSettings.CookieList().get());
        }
        setChecks();
        createHooks();
        setting_cookie_list.setEnabled(iSettings.useCustomCookies().get());
        settings_edit_cookie.setEnabled(iSettings.useCustomCookies().get());
        setting_save_cookie.setEnabled(iSettings.useCustomCookies().get());
    }

    public String getEditedText() {
        if (settings_edit_cookie.getText() != null) {
            return settings_edit_cookie.getText().toString();
        } else {
            return itemSelected.toString();
        }
    }

    private void createHooks() {
        setting_custom_cookie_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iSettings.useCustomCookies().put(b);
                setting_cookie_list.setEnabled(b);
                settings_edit_cookie.setEnabled(b);

                setting_save_cookie.setEnabled(b);
                if (!b) {
                    String[] cookies =
                            getResources().getStringArray(R.array.cookie_names_array);

                    if (!TextUtils.join(",", cookies).equals(iSettings.CookieList().get())) {
                        for (int i = 0; i < Constants.CookieTypes.length; i++) {
                            String cookieType = Constants.CookieTypes[i];
                            SettingsActivity settingsActivity = (SettingsActivity) getActivity();
                            settingsActivity.SaveCookie(cookieType, cookies[i]);
                        }
                    }
                }
            }
        });
        settings_auot_fill_cases_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iSettings.useAutoFillCases().put(b);
            }
        });
        setting_cookie_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                settings_edit_cookie.setEnabled(true);
                if (adapterView.getSelectedItem() != null) {
                    itemSelected = adapterView.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                settings_edit_cookie.setEnabled(false);
            }
        });
    }


    private void setChecks() {
        settings_auot_fill_cases_check.setChecked(
                iSettings.useAutoFillCases().get());
        setting_custom_cookie_check.setChecked(iSettings.useCustomCookies().get());
    }

    @Click(R.id.setting_save_cookie)
    void onSave() {
        if (!setting_custom_cookie_check.isChecked()) {
            return;
        }
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        if (settingsActivity != null && settings_edit_cookie.getText() != null && !settings_edit_cookie.getText().toString().isEmpty()) {
            settingsActivity.SaveCookie(itemSelected.toString(), settings_edit_cookie.getText().toString());
        }


    }

    public void fillCookieList(String cookieList) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, TextUtils.split(cookieList, ","));
        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList
                );
        settings_edit_cookie.setText("");
        setting_cookie_list.setAdapter(spinnerAdapter);
        setting_cookie_list.requestLayout();

    }
}
