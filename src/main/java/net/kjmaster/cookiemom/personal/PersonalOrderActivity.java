package net.kjmaster.cookiemom.personal;

import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.scout.SelectScoutListActivity_;
import net.kmaster.cookiemom.dao.*;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class PersonalOrderActivity extends CookieActionActivity{



    @Extra
    int requestCode;

    @Extra
    boolean isEating=false;

    @StringRes(R.string.add_personal)
    String fragTag;

    @StringRes(R.string.add_personal_title)
    String fragTitle;

    @Pref
    ISettings_ iSettings;

    private String customerName="";
    private String customerContact="";
    private long scoutId;

    @ViewById(R.id.personal_customer)
    TextView mCustomer;

    @ViewById(R.id.personal_contact)
    TextView mContact;

    @StringRes(R.string.eat_cookies)
    String eat_cookies;
    @AfterViews
    void afterViewFrag(){
        if(isEating){
            setTitle(eat_cookies);
            if(mCustomer!=null){
            mCustomer.setVisibility(View.GONE);
            }
            if(mContact!=null){
            mContact.setVisibility(View.GONE);
            }
        }
        replaceFrag(createFragmentTransaction(fragTag),PersonalOrderFragment_.builder().build(),fragTag);
         createActionMode(fragTitle);
        if (iSettings.isDefaultScoutSet().get()){
            if(iSettings.ScoutId().get()>-1){
                    scoutId=iSettings.ScoutId().get();
            }  else {
                selectScout();
            }

        }else{
                selectScout();
        }

      }

    private void selectScout() {
       SelectScoutListActivity_.intent(this).requestCode(Constants.SELECT_SCOUT_REQUEST_CODE).startForResult(Constants.SELECT_SCOUT_REQUEST_CODE);
    }

//    @TextChange(R.id.personal_customer)
//    void changePersonal(CharSequence text){
//        customerName=text.toString();
//    }
//
//    @TextChange(R.id.personal_contact)
//    void changeContact(CharSequence text){
//        customerContact=text.toString();
//    }

    @OnActivityResult(Constants.SELECT_SCOUT_REQUEST_CODE)
    void selectScout(int resultCode,Intent data){
        if (resultCode==RESULT_OK){
           scoutId=data.getLongExtra(getString(R.string.scout_id),-1);
            try{
                if ( scoutId > -1){
                    iSettings.ScoutId().put(scoutId);
                    iSettings.isDefaultScoutSet().put(true);
                    Scout scout = Main.daoSession.getScoutDao().load(scoutId);
                    iSettings.ScoutName().put(scout.getScoutName());

                }}
            catch(Exception e){
                Log.e("cookiemom","Error setting scoutId for default");

            }
        }

    }


    @Override
    protected void saveData() {
        if (isEating){
            for(int i=0;i< Constants.CookieTypes.length;i++){
                Integer intVal=Integer.valueOf(getFragment().valuesMap().get(Constants.CookieTypes[i]));
                if(intVal>0){
                    CookieTransactions cookieTransactions=new CookieTransactions(null,this.scoutId,-1L,Constants.CookieTypes[i],-1*intVal,Calendar.getInstance().getTime(),0.00);
                    Main.daoSession.getCookieTransactionsDao().insert(cookieTransactions);
                }
            }

        }   else {
        if (iSettings.isDefaultScoutSet().get()){

            PersonalOrdersDao dao=Main.daoSession.getPersonalOrdersDao();
                for(int i=0;i< Constants.CookieTypes.length;i++){
                        Integer intVal=Integer.valueOf(getFragment().valuesMap().get(Constants.CookieTypes[i]));
                        if(intVal>0){
                           PersonalOrderFragment fragment = (PersonalOrderFragment) getFragment();
                            PersonalOrders personalOrder=new PersonalOrders(
                                    null,
                                    Calendar.getInstance().getTime(),
                                    fragment.getCustomer(),
                                    fragment.getContact(),
                                    Constants.CookieTypes[i],
                                    intVal,
                                    false,
                                    false,
                                    false,
                                    Double.valueOf(0),
                                    false
                            );
                            dao.insert(personalOrder);
                            Main.daoSession.getOrderDao().insert(new Order(
                                    null,
                                    Calendar.getInstance().getTime(),
                                    iSettings.ScoutId().get(),
                                    Constants.CookieTypes[i],
                                    false,
                                    intVal,dao.getKey(personalOrder),
                                    false
                            ));
                         }
                    }

            setResult(RESULT_OK);
        } else {
            net.kjmaster.cookiemom.scout.SelectScoutListActivity_.intent(this).requestCode(Constants.ADD_DEFAULT_SCOUT).startForResult(Constants.ADD_DEFAULT_SCOUT);
        }
    }
    }

    @OnActivityResult(Constants.ADD_DEFAULT_SCOUT)
    void addDefaultScout(int resultCode,Intent data){
        if (resultCode==RESULT_OK){

            long scoutId=data.getLongExtra(getString(R.string.scout_id),-1L);

            iSettings.ScoutId().put(scoutId);
            iSettings.ScoutName().put(Main.daoSession.getScoutDao().load(scoutId).getScoutName());
            iSettings.isDefaultScoutSet().put(true);
            saveData();

        }

    }



}