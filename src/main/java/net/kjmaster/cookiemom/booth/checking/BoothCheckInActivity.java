package net.kjmaster.cookiemom.booth.checking;

import android.annotation.SuppressLint;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieOrCashDialogBase;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment;
import net.kjmaster.cookiemom.ui.cookies.CookieAmountsListInputFragment_;
import net.kmaster.cookiemom.dao.CookieTransactions;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/10/13
 * Time: 8:40 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class BoothCheckInActivity extends CookieOrCashDialogBase {
    private String fragName;

    @Override
    protected boolean isEditable() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Extra
    long BoothId;

    @Override
    protected void saveData() {
        performCheckIn();
    }

    @Override
    protected boolean isNegative() {
        return false;
    }

    @AfterViews
    void afterViewFrag() {

        super.CreateDialog(BoothId, this, "Booth Check-in");
    }

    @Override
    protected void createActionFragment() {
        fragName = getString(R.string.booth_checkin_order);
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder().isBoxes(true).isEditable(this.isEditable()).build(),
                fragName);

        createActionMode(getString(R.string.checkin));
    }

    private void performCheckIn() {
        CookieAmountsListInputFragment fragment = (CookieAmountsListInputFragment) getSupportFragmentManager().findFragmentByTag(fragName);
        Calendar c = Calendar.getInstance();
        if (fragment != null) {
            for (String cookieType : Constants.CookieTypes) {
                int amt = Integer.valueOf(fragment.valuesMap().get(cookieType));
                CookieTransactions cookieTransactions = new CookieTransactions(null, -1L, BoothId, cookieType, amt, c.getTime(), 0.0);
                Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);
            }
        }


    }

    @Override
    protected boolean isEditableValue() {
        return true;
    }

    @Override
    protected void saveForemData() {
        performCheckIn();

    }


}
