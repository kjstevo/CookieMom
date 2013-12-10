package net.kjmaster.cookiemom.global;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerDialogFragment;

import java.util.HashMap;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/24/13
 * Time: 7:59 PM
 */

public abstract class CookieActionActivity extends FragmentActivity implements ActionMode.Callback, NumberPickerDialogFragment.NumberPickerDialogHandler {

    private ActionMode actionMode;


    protected void createActionMode(String title) {
        actionMode = startActionMode(this);

        actionMode.setTitle(title);

        int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");

        View doneButton = this.findViewById(doneButtonId);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                setResult(RESULT_OK);
                actionMode.finish();
                finish();
            }
        });
    }

    private ICookieActionFragment mFragment;

    protected FragmentTransaction createFragmentTransaction(String fragTag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragTag);

        if (prev != null) {
            ft.remove(prev);
        }
        return ft;
    }

    protected void replaceFrag(FragmentTransaction ft, Fragment frag, String fragName) {

        mFragment = (ICookieActionFragment) frag;
        ft.replace(R.id.content, frag, fragName);
        ft.commit();


    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();

        inflater.inflate(R.menu.add_scout, menu);

        return true;
    }


    public HashMap<String, String> getValMap() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String cookieType : Constants.CookieTypes) {
            hashMap.put(cookieType, "0");
        }
        return hashMap;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;    // To change body of implemented methods use File | Settings | File Templates.
    }

    protected abstract boolean isEditable();

    protected abstract void saveData();


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        setResult(RESULT_CANCELED);
        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        finish();
    }

    //    @TextChange(R.id.cookie_amount)
//    void changeText(TextView tv, CharSequence text){
//       mFragment.valuesMap().put(
//               tv.getTag().toString(),
//               text.toString());
//    }
    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        mFragment.valuesMap().put(CookieTypes[reference], String.valueOf(number));
        mFragment.refreshView();
    }

    protected ICookieActionFragment getFragment() {
        return mFragment;
    }

    public NumberPickerBuilder createNumberPicker(int finalI) {
        final NumberPickerBuilder numberPickerBuilder =
                new NumberPickerBuilder()
                        .setFragmentManager(
                                getSupportFragmentManager())
                        .setStyleResId(
                                R.style.BetterPickersDialogFragment_Light
                        );

        numberPickerBuilder.setReference(finalI);

        return numberPickerBuilder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //net.kjmaster.cookiemom.global.CookieActionActivity.onCreate returns void
        for (String cookieType : Constants.CookieTypes) {
            outState.putString(cookieType, mFragment.valuesMap().get(cookieType));
        }
        //11/30/13
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        for (String cookieType : Constants.CookieTypes) {
            mFragment.valuesMap().put(cookieType, savedInstanceState.getString(cookieType));

        }
        mFragment.refreshView();

        //11/30/13
        super.onRestoreInstanceState(savedInstanceState);
    }


}

