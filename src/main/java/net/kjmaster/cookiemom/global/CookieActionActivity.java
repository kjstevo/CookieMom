package net.kjmaster.cookiemom.global;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerDialogFragment;

import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/24/13
 * Time: 7:59 PM
 */

public abstract class CookieActionActivity extends FragmentActivity implements ActionMode.Callback, NumberPickerDialogFragment.NumberPickerDialogHandler {

    protected final CookieTransactionsDao cookieTransactionsDao = Main.daoSession.getCookieTransactionsDao();
    protected final OrderDao orderDao = Main.daoSession.getOrderDao();
    private ActionMode actionMode;

    protected void createActionMode(String title) {

        actionMode = startActionMode(this);
        if (actionMode == null) return;
        if (!setActionTitle(title)) return;

        setDoneAction();
    }

    private boolean setDoneAction() {

        final View doneButton = getDoneButton();

        if (doneButton == null) {
            return false;
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                setResult(RESULT_OK);
                actionMode.finish();
                finish();
            }
        });
        return true;
    }


    private View getDoneButton() {
        Resources resources = Resources.getSystem();
        if (resources == null) {
            return null;
        }
        int doneButtonId = resources.getIdentifier("action_mode_close_button", "id", "android");
        return this.findViewById(doneButtonId);
    }


    private boolean setActionTitle(String title) {
        if (actionMode == null) {
            return false;
        }
        actionMode.setTitle(title);
        return true;
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

        if (inflater != null) {
            inflater.inflate(R.menu.add_scout, menu);
        }

        return true;
    }


    public HashMap<String, String> getValMap() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String cookieType : Constants.CookieTypes) {
            hashMap.put(cookieType, "0");
        }
        return hashMap;
    }

    public HashMap<String, String> getExpectedValMap() {
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

    protected Integer getBoxesInInventory(String cookieFlavor) {
        Integer boxesInInventory = 0;
        List<CookieTransactions> transactionsList = cookieTransactionsDao.queryBuilder()
                .where(
                        CookieTransactionsDao.Properties.CookieType.eq(cookieFlavor)
                )
                .list();
        for (CookieTransactions transaction : transactionsList) {
            boxesInInventory += transaction.getTransBoxes();
        }
        return boxesInInventory;
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

        for (String cookieType : Constants.CookieTypes) {
            outState.putString(cookieType, mFragment.valuesMap().get(cookieType));
        }
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

