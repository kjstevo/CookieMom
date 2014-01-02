package net.kjmaster.cookiemom.scout.add;

//~--- non-JDK imports --------------------------------------------------------

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kmaster.cookiemom.dao.Scout;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/28/13
 * Time: 1:26 AM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_add_scout_layout)
public class AddScoutActivity extends CookieActionActivity {

    @FragmentByTag("add_scout")
    AddScoutDialogFragment addScoutDialogFragment;
    private String fragTag;
    private boolean isScout = true;


    @AfterViews
    void afterViews() {
        fragTag = getString(R.string.add_scout);
        if (Constants.getADD_SCOUT().equals(getString(R.string.add_scout))) {
            isScout = true;
            replaceFrag(createFragmentTransaction(fragTag), AddScoutDialogFragment_.builder().build(), fragTag);
        } else {
            isScout = false;
            replaceFrag(createFragmentTransaction(fragTag), AddCustomerDialogFragment_.builder().build(), fragTag);
        }

        createActionMode(net.kjmaster.cookiemom.global.Constants.ADD_SCOUT);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        //net.kjmaster.cookiemom.scout.add.AddScoutActivity.onRestoreInstanceState returns void
        AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {
            fragment.editText.setText(savedInstanceState.getString("scout_name"));
        }


        //12/8/13
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void saveData() {
        if (isScout) {
            Main.daoSession.getScoutDao().insert(new Scout(null, getFragment().valuesMap().get(fragTag)));
        } else {
            Main.daoSession.getScoutDao().insert(new Scout(null, getFragment().valuesMap().get(fragTag)
                    + "  "
                    + getFragment().valuesMap().get(getString(R.string.contact_info))));
        }
        setResult(RESULT_OK);
        // ((ScoutFragment)getSupportFragmentManager().findFragmentByTag("Scouts")).refreshView();

    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        //net.kjmaster.cookiemom.scout.add.AddScoutActivity.onSaveInstanceState returns void
        AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {
            outState.putString("scout_name", fragment.editText.getText().toString());
        }
        //12/9/13
        super.onSaveInstanceState(outState);
    }
}


