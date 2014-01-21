package net.kjmaster.cookiemom.settings;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;

import java.util.List;

@EActivity(R.layout.scout_order_layout)
public class SettingsActivity extends FragmentActivity {

    private SettingsFragment settingsFragment;
    @Pref
    ISettings_ iSettings;

    @AfterViews
    void afterViews() {
        if (iSettings.CookieList().get() == null) {
            iSettings.CookieList().put(TextUtils.join(",", getResources().getStringArray(R.array.cookie_names_array)));
        } else {
            cookieList = iSettings.CookieList().get();
            String[] cookies = TextUtils.split(cookieList, ",");
            if (cookies.length != getResources().getStringArray(R.array.cookie_names_array).length) {
                cookieList = TextUtils.join(",", getResources().getStringArray(R.array.cookie_names_array));
            }
        }
        settingsFragment = SettingsFragment_.builder().build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, settingsFragment);
        ft.commit();

    }

    public void SaveCookie(String oldCookieName, String newCookieName) {
        cookieList = cookieList.replace(oldCookieName, newCookieName);
        if (TextUtils.split(cookieList, ",").length != getResources().getStringArray(R.array.cookie_names_array).length) {
            cookieList = iSettings.CookieList().get();
        } else {
            iSettings.CookieList().put(cookieList);

            updateOrdes(oldCookieName, newCookieName);
            updateTrans(oldCookieName, newCookieName);
            Constants.updateCookieTypes(TextUtils.split(cookieList, ","));
            Main.mCookieTypes = TextUtils.split(cookieList, ",");
        }

        settingsFragment.fillCookieList(cookieList);
    }

    private void updateTrans(String oldCookieName, String newCookieName) {
        List<CookieTransactions> transactions = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                .where(CookieTransactionsDao.Properties.CookieType.eq(oldCookieName))
                .list();
        for (CookieTransactions cookieTransactions : transactions) {
            cookieTransactions.setCookieType(newCookieName);
            cookieTransactions.update();
        }
    }

    private void updateOrdes(String oldCookieName, String newCookieName) {
        List<Order> orders = Main.daoSession.getOrderDao().queryBuilder()
                .where(OrderDao.Properties.OrderCookieType.eq(oldCookieName))
                .list();
        for (Order order : orders) {
            order.setOrderCookieType(newCookieName);
            order.update();
        }
    }

    String cookieList;


    @Override
    public void onBackPressed() {

        if (!iSettings.CookieList().get().equals(cookieList)) {
            iSettings.CookieList().put(cookieList);

            Intent data = getIntent();
            data.putExtra("cookie_list", cookieList);
            Constants.updateCookieTypes(TextUtils.split(cookieList, ","));
            setResult(Constants.SETTINGS_RESULT_DIRTY, data);

        }
        finish();
    }

    }



