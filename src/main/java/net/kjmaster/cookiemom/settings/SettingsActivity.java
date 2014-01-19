package net.kjmaster.cookiemom.settings;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

import net.kjmaster.cookiemom.R;

@EActivity(R.layout.scout_order_layout)
public class SettingsActivity extends FragmentActivity {

    @AfterViews
    void afterViews() {
        SettingsFragment settingsFragment = SettingsFragment_.builder().build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, settingsFragment);
        ft.commit();

    }


}
