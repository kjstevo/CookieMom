package net.kjmaster.cookiemom.scout.turnin;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerDialogFragment;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;

import java.util.Calendar;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class ScoutTurnInActivity extends CookieActionActivity implements ISimpleDialogListener, NumberPickerDialogFragment.NumberPickerDialogHandler {

    @Extra
    long ScoutId;

    @Extra
    boolean isEditable;

    @StringRes(R.string.scout_turn_in_title)
    String scout_turn_in__title;

    @StringRes(R.string.scout_turn_in)
    String scout_turn_in;

    @StringRes(R.string.turn_in)
    String turn_in;

    @StringRes(R.string.cancel)
    String resCancel;


    @AfterViews
    void afterViewFrag() {
        ScoutTurnInDialog scoutTurnInDialog = new ScoutTurnInDialog();
        scoutTurnInDialog.ScoutTurnInDialog(Main.daoSession.getScoutDao().load(ScoutId), this);
    }

    private void saveTurnInData() {
        Calendar c = Calendar.getInstance();
        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();
        for (String cookieFlavor : CookieTypes) {
            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));
            CookieTransactions cookieTransactions = new CookieTransactions(
                    null, ScoutId, -1L, cookieFlavor, totalBoxes,
                    c.getTime(),
                    (double) 0
            );
            dao.insert(cookieTransactions);
        }
    }

    @Override
    protected boolean isEditable() {
        return isEditable;
    }

    @Override
    protected void saveData() {
        saveTurnInData();
    }


    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {

        if (reference == -1) {

            CookieTransactions cookieTransactions = new CookieTransactions(null, ScoutId, -1L, "", 0, Calendar.getInstance().getTime(), fullNumber);

            Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);

            finish();

        } else {
            super.onDialogNumberSet(reference, number, decimal, isNegative, fullNumber);
        }
    }


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
        replaceFrag(createFragmentTransaction(turn_in), CookieAmountsListInputFragment_.builder().isEditable(this.isEditable).isBoxes(true).build(), turn_in);
        createActionMode(scout_turn_in__title);

    }
}
