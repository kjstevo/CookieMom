package net.kjmaster.cookiemom.scout;

//~--- non-JDK imports --------------------------------------------------------

import android.os.Bundle;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kmaster.cookiemom.dao.Scout;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/28/13
 * Time: 1:26 AM
 */
@EActivity(R.layout.add_scout_layout)
public class AddScoutActivity extends CookieActionActivity {

    @FragmentByTag("add_scout")
    AddScoutDialogFragment addScoutDialogFragment;
    private String fragTag;

    @AfterViews
    void afterViews() {
        fragTag = getString(R.string.add_scout);
        replaceFrag(createFragmentTransaction(fragTag), AddScoutDialogFragment_.builder().build(), fragTag);
        createActionMode(getString(R.string.add_scout_title));
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //net.kjmaster.cookiemom.scout.AddScoutActivity.onRestoreInstanceState returns void
        AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {
            fragment.editText.setText(savedInstanceState.getString("scout_name"));
        }


        //12/8/13
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void saveData() {
        Main.daoSession.getScoutDao().insert(new Scout(null, getFragment().valuesMap().get(fragTag)));
        setResult(RESULT_OK);
        // ((ScoutFragment)getSupportFragmentManager().findFragmentByTag("Scouts")).refreshView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //net.kjmaster.cookiemom.scout.AddScoutActivity.onSaveInstanceState returns void
        AddScoutDialogFragment fragment = (AddScoutDialogFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {
            outState.putString("scout_name", fragment.editText.getText().toString());
        }
        //12/9/13
        super.onSaveInstanceState(outState);
    }
}


