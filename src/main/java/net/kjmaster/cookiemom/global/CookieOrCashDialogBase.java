package net.kjmaster.cookiemom.global;

import android.support.v4.app.FragmentActivity;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;

import java.util.Calendar;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/13/13
 * Time: 8:10 PM
 */
public abstract class CookieOrCashDialogBase extends CookieActionActivity implements ISimpleDialogListener {

    private Long mId;

    protected void CreateDialog(Long id, FragmentActivity activity, String title) {
        mId = id;
        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle(title)
                .setMessage("Is this for money or cookies?")
                .setPositiveButtonText("Money")
                .setNegativeButtonText("Cookies")
                .setCancelable(true)
                .setRequestCode(id.intValue())
                .setTag(id.toString())
                .show();
    }

    protected abstract void createActionFragment();

    protected abstract boolean isEditableValue();

    protected abstract void saveForemData();

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        final NumberPickerBuilder numberPickerBuilder =
                new NumberPickerBuilder()
                        .setFragmentManager(
                                getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);

        numberPickerBuilder.setReference(-1);
        numberPickerBuilder.show();
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        createActionFragment();
    }

    @Override
    protected boolean isEditable() {
        return isEditableValue();
    }

    @Override
    protected void saveData() {
        saveForemData();

    }

    protected abstract boolean isNegative();

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {

        if (reference == -1) {
            CookieTransactions cookieTransactions;
            if (isNegative()) {

                cookieTransactions = new CookieTransactions(null, -1L, mId, "", 0, Calendar.getInstance().getTime(), fullNumber * -1);
            } else {
                cookieTransactions = new CookieTransactions(null, -1L, mId, "", 0, Calendar.getInstance().getTime(), fullNumber);
            }

            Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);

            finish();

        } else {
            super.onDialogNumberSet(reference, number, decimal, isNegative, fullNumber);
        }
    }
}


