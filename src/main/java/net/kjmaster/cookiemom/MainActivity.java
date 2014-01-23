/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.kjmaster.cookiemom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OnActivityResult;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.action.ActionFragment_;
import net.kjmaster.cookiemom.booth.BoothFragment_;
import net.kjmaster.cookiemom.cupboard.CupboardFragment_;
import net.kjmaster.cookiemom.dao.Booth;
import net.kjmaster.cookiemom.dao.BoothAssignments;
import net.kjmaster.cookiemom.dao.BoothAssignmentsDao;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.scout.ScoutFragment_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupActivity_;
import net.kjmaster.cookiemom.scout.select.SelectScoutListActivity_;
import net.kjmaster.cookiemom.settings.SettingsActivity_;
import net.kjmaster.cookiemom.summary.SummaryFragment_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialogFragment;

@SuppressLint("Registered")
@EActivity(R.layout.main_activity)
public class MainActivity extends FragmentActivity {

    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;

    private Drawable oldBackground = null;
    private int currentColor = Color.GREEN;
    private String actionItemTitle;
    private Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        String dbName = iSettings.dbName().get();
        if (dbName != null) {
            if (dbName.toString().equals("personal-db")) {
                menuInflater.inflate(R.menu.activity_main_personal, menu);
            } else {
                if (iSettings.useCustomCookies().get()) {
                    menuInflater.inflate(R.menu.activity_main_custom_cookie, menu);
                } else {
                    menuInflater.inflate(R.menu.activity_main, menu);
                }

            }
        }

        return super.onCreateOptionsMenu(menu);
    }


    @AfterViews
    void afterViews() {

        createTabPager();

        changeColor(currentColor);

        verifyCookieList();


    }

    private void verifyCookieList() {
        OrderDao dao = Main.daoSession.getOrderDao();
        CookieTransactionsDao dao2 = Main.daoSession.getCookieTransactionsDao();
        for (int i = 0; i < getResources().getStringArray(R.array.cookie_names_array).length; i++) {
            String cookieType = getResources().getStringArray(R.array.cookie_names_array)[i];
            if (!cookieType.equals(Constants.CookieTypes[i])) {
                List<Order> list = dao.queryBuilder().where(OrderDao.Properties.OrderCookieType.eq(cookieType)).list();
                for (Order order : list) {
                    order.setOrderCookieType(Constants.CookieTypes[i]);
                    order.update();
                }


                List<CookieTransactions> list2 = dao2.queryBuilder().where(CookieTransactionsDao.Properties.CookieType.eq(cookieType)).list();
                for (CookieTransactions cookieTransactions : list2) {
                    cookieTransactions.setCookieType(Constants.CookieTypes[i]);
                    cookieTransactions.update();

                }
            }
        }


        List<Order> list = dao.loadAll();
        //String cookieList = TextUtils.join(",", Constants.CookieTypes);
        ArrayList<String> arrayList = new ArrayList<String>();
        Collections.addAll(arrayList, Constants.CookieTypes);
        for (Order order : list) {
            Log.d("COOKIE",
                    order.getOrderCookieType());
            if (!arrayList.contains(order.getOrderCookieType())) {
                dao.delete(order);

            }
        }

        List<CookieTransactions> list2 = dao2.loadAll();
        for (CookieTransactions cookieTransactions : list2) {
            if (!arrayList.contains(cookieTransactions.getCookieType())) {
                dao2.delete(cookieTransactions);
            }
        }
    }


    private void createTabPager() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyNewPagerAdapter adapter = new MyNewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                getResources().getDisplayMetrics()
        );

        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
    }

    private void changeColor(int newColor) {
        tabs.setIndicatorColor(newColor);
        // change ActionBar color just if an ActionBar is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Drawable colorDrawable = new ColorDrawable(newColor);
            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
            LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable, bottomDrawable});

            if (oldBackground == null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    ld.setCallback(drawableCallback);
                } else {
                    getActionBar().setBackgroundDrawable(ld);
                }

            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});

                // workaround for broken ActionBarContainer drawable handling on
                // pre-API 17 builds
                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    getActionBar().setBackgroundDrawable(td);
                }

                td.startTransition(200);
            }

            oldBackground = ld;

            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

        }

        currentColor = newColor;
    }

    @App
    Main main;

    @Pref
    ISettings_ iSettings;

    @OptionsItem(R.id.menu_settings)
    void onSettings() {
        SettingsActivity_.intent(this).startForResult(Constants.SETTINGS_REQUEST);
    }

    @OnActivityResult(Constants.SETTINGS_REQUEST)
    void afterSettings(int resultcode, Intent data) {
        if (resultcode != Constants.SETTINGS_RESULT_DIRTY) {
            return;
        }
        if (data.getExtras() == null) {
            return;
        }
        String cookieList;
        cookieList = data.getExtras().getString("cookie_list");
        if (cookieList == null) {
            return;
        }
        main.updateCookieTypes(data.getExtras().getString("cookie_list"));
        refreshAll();

    }

    @OptionsItem(R.id.menu_about)
    void onAbout() {
        final LicensesDialogFragment fragment = LicensesDialogFragment.newInstance(R.raw.notices, true);
        fragment.show(getSupportFragmentManager(), null);

    }

    @OptionsItem(R.id.menu_eat_cookies)
    void onEatCookies() {
        SelectScoutListActivity_.intent(this)
                .requestCode(Constants.EAT_COOKIES)
                .startForResult(Constants.EAT_COOKIES);
    }

    @OnActivityResult(Constants.EAT_COOKIES)
    void onEatSelectScoutResult(int resultCode, Intent data) {
        if (data != null) {
            long scoutId = data.getLongExtra("scout_id", 0);
            ScoutPickupActivity_.intent(this).ScoutId(scoutId).isEditable(true).startForResult(Constants.SCOUT_REQUEST);
        }
    }

    @OptionsItem({R.id.menu_personal, R.id.menu_cookie_mom})
    void onProfileSwitch(MenuItem item) {
        if (item.getTitle().toString().equals(getString(R.string.Personal))) {
            iSettings.dbName().put(iSettings.dbPersonalName().get());
            main.switchDb(iSettings.dbPersonalName().get());
            afterViews();
            item.setTitle(R.string.cookie_mom);
        } else {
            iSettings.dbName().put(iSettings.dbCookieMomName().get());
            main.switchDb(iSettings.dbCookieMomName().get());
            afterViews();
            item.setTitle(R.string.Personal);
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    private final Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {


            //   getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };
//    @OnActivityResult(Constants.ADD_SCOUT_REQUEST_CODE)
//    void onResult(int resultCode) {
//           if (resultCode== RESULT_OK){
//
//           }


    //}
    @OnActivityResult(Constants.REMOVE_SCOUT_REQUEST_CODE)
    void onRemoveScout(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (data != null) {
                long scout_id = data.getLongExtra("scout_id", 0);
                long target_id = data.getLongExtra("target_id", 0);

                List<BoothAssignments> list = Main.daoSession.getBoothAssignmentsDao().queryBuilder()
                        .where(
                                BoothAssignmentsDao.Properties.BoothAssignScoutId.eq(scout_id),
                                BoothAssignmentsDao.Properties.BoothAssignBoothId.eq(target_id))
                        .list();

                for (BoothAssignments assignments : list) {
                    Main.daoSession.getBoothAssignmentsDao().delete(assignments);
                }

            }
        }
    }


    @OnActivityResult(Constants.ASSIGN_SCOUT_REQUEST_CODE)
    void onScoutAssign(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Main.daoSession.getBoothAssignmentsDao().insert(
                    new BoothAssignments(
                            null,
                            data.getLongExtra("scout_id", 0),
                            data.getLongExtra("target_id", 0)
                    )
            );


        }
        refreshAll();
    }

    public void refreshAll() {

        createTabPager();
    }

    @OnActivityResult(Constants.BOOTH_ORDER)
    void onBoothOrder(int resultCode, Intent data) {
        refreshAll();
    }


    @OnActivityResult(Constants.PLACE_CUPBOARD_ORDER)
    void placeCupboardOrder(int resultCode, Intent data) {
        refreshAll();

    }

    @OnActivityResult(Constants.SCOUT_REQUEST)
    void scoutResult(int resultCode) {
        refreshAll();

    }

    @OnActivityResult(Constants.ADD_BOOTH_REQUEST_CODE)
    void onBoothResult(int resultCode, Intent data) {
        if (resultCode == Constants.ADD_BOOTH_RESULT_OK) {
            if (data != null) {
                Main.daoSession.getBoothDao().insert(
                        new Booth(
                                null,
                                data.getStringExtra("add_booth"),
                                data.getStringExtra("address"),
                                new Date(data.getLongExtra("booth_date", 0)
                                )));

            } else {
                Log.e("No data intent on booth result", "LOG00000:");
            }
        }
        refreshAll();
    }


    public class MyNewPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = getResources().getStringArray(R.array.tab_names);

        public MyNewPagerAdapter(FragmentManager fm) {
            super(fm);
            if (iSettings.dbName().get().equals(iSettings.dbPersonalName().get())) {
                TITLES = getResources().getStringArray(R.array.personal_tab_names);
                Constants.setADD_SCOUT(getString(R.string.add_customer_title));
                Constants.setSCOUT(getString(R.string.Customers));
            } else {
                Constants.setADD_SCOUT(getString(R.string.add_scout));
                Constants.setSCOUT(getString(R.string.Scouts));
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position) {
                case 0:
                    fragment = ActionFragment_.builder().build();

                    break;
                case 1:
                    fragment = ScoutFragment_.builder().build();
                    break;
                case 2:
                    fragment = BoothFragment_.builder().build();
                    break;
                case 3:
                    fragment = CupboardFragment_.builder().build();
                    break;

                case 4:
                    fragment = SummaryFragment_.builder().build();
                    break;
                default:
                    fragment = ScoutFragment_.builder().build();


            }
            return fragment;
        }

    }
}



