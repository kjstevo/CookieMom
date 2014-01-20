package net.kjmaster.cookiemom.settings;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;

@EActivity(R.layout.scout_order_layout)
public class SettingsActivity extends FragmentActivity {

    private SettingsFragment settingsFragment;

    @AfterViews
    void afterViews() {
        settingsFragment = SettingsFragment_.builder().build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, settingsFragment);
        ft.commit();

    }


    @Override
    public void onBackPressed() {
        if(settingsFragment!=null){
            if(settingsFragment.isDirty){
                setResult(Constants.SETTINGS_RESULT_DIRTY);
            }
        super.onBackPressed();
    }

    }
}
