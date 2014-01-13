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
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.scout.ScoutFragment_;
import net.kjmaster.cookiemom.scout.pickup.ScoutPickupActivity_;
import net.kjmaster.cookiemom.scout.select.SelectScoutListActivity_;
import net.kjmaster.cookiemom.summary.SummaryFragment_;

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
                menuInflater.inflate(R.menu.activity_main, menu);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }


    @AfterViews
    void afterViews() {

        createTabPager();

        changeColor(currentColor);


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

    @OptionsItem(R.id.menu_about)
    void onAbout(){
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



