package net.kjmaster.cookiemom.settings;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
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

    @App
    Main main;

    @ViewById()
    CheckBox setting_custom_cookie_check;

    @ViewById()
    CheckBox settings_auot_fill_cases_check;

    @ViewById
    EditText settings_edit_cookie;

    @Pref
    ISettings_ iSettings;
    private CharSequence itemSelected;

    @AfterViews
    void afterViews() {
        fillCookieList();
        setChecks();
        createHooks();
        setting_cookie_list.setEnabled(iSettings.useCustomCookies().get());
        settings_edit_cookie.setEnabled(iSettings.useCustomCookies().get());

    }

    private void createHooks() {
        setting_custom_cookie_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iSettings.useCustomCookies().put(b);
                setting_cookie_list.setEnabled(b);
                settings_edit_cookie.setEnabled(b);
                if (!b) {
                    String[] cookies =
                            getResources().getStringArray(R.array.cookie_names_array);
                    iSettings.CookieList().put(TextUtils.join("'", cookies));
                    fillCookieList();
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
                itemSelected = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                settings_edit_cookie.setEnabled(false);
            }
        });
    }

    @Click(R.id.setting_save_cookie)
    void onSave() {
        iSettings.CookieList().put(iSettings.CookieList().get().replace(itemSelected, settings_edit_cookie.getText()));
        fillCookieList();
        main.RefreshCookieList();

    }

    private void setChecks() {
        settings_auot_fill_cases_check.setChecked(
                iSettings.useAutoFillCases().get());
        setting_custom_cookie_check.setChecked(iSettings.useCustomCookies().get());
    }

    private void fillCookieList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, Main.getCookieTypes());
        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList
                );

        setting_cookie_list.setAdapter(spinnerAdapter);
    }
}
