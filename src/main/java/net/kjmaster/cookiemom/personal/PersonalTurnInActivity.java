package net.kjmaster.cookiemom.personal;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.PersonalOrders;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerBuilder;
import net.kjmaster.cookiemom.ui.numberpicker.NumberPickerDialogFragment;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class PersonalTurnInActivity extends CookieActionActivity implements  NumberPickerDialogFragment.NumberPickerDialogHandler {

    @Extra
    String customerName;

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
    onPositiveButtonClicked();
    }
     private void saveTurnInData() {
     }

    @Override
    protected void saveData() {
           saveTurnInData();
    }


    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {

        if(reference==-1){
            Calendar c = Calendar.getInstance();
            PersonalOrders personalOrders=new PersonalOrders(null,c.getTime(),customerName,"","", 0,false,false,false,fullNumber,false);
            Main.daoSession.getPersonalOrdersDao().insert(personalOrders);
      //     CookieTransactions cookieTransactions=new CookieTransactions(null, customerName,-1L,"",0,Calendar.getInstance().getTime(),fullNumber);

        //    Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);

           finish();

        } else {
            super.onDialogNumberSet(reference,number,decimal,isNegative,fullNumber);
        }
     }


    public void onPositiveButtonClicked() {
        final NumberPickerBuilder numberPickerBuilder =
                new NumberPickerBuilder()
                        .setFragmentManager(
                                getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);

        numberPickerBuilder.setReference(-1);
        numberPickerBuilder.show();

    }


}
