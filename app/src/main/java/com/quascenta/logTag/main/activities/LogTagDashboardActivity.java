package com.quascenta.logTag.main.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLoginListener;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLogoutListener;
import com.quascenta.logTag.main.configuration.LogTagLoginBuilder;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.manager.Userpreferences;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;
import com.quascenta.petersroad.Utils.Objectgenerator;
import com.quascenta.petersroad.adapters.DeviceListAdapter;
import com.quascenta.petersroad.broadway.R;
import com.quascenta.petersroad.fragments.CheeseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKSHAY on 12/28/2016.
 */

public class LogTagDashboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,LogTagCustomLogoutListener, LogTagCustomLoginListener {

    public static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    Intent intent;
    LogTagUser currentUser;
    LogTagUser newUser;
    //Search View
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private ListView device_list;
    LogTagLoginBuilder  logTagLoginBuilder;
    private DeviceListAdapter deviceListAdapter;
    private ArrayList<Objectgenerator> device;
    GoogleApiClient mGoogleApiClient;

    private ArrayList<Objectgenerator> friendList;


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            String fail = "Login Failed";
            if(resultCode == LogTagLoginConfig.FACEBOOK_LOGIN_REQUEST){
                Facebook_logTagUser user;
                try {
                    user = data.getParcelableExtra(LogTagLoginConfig.USER);
                    Userpreferences.with(getApplicationContext()).setUserSession(currentUser);

                }catch (Exception e){

                }
            }
            else if(resultCode == LogTagLoginConfig.GOOGLE_LOGIN_REQUEST){
                Google_logTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
                Userpreferences.with(getApplicationContext()).setUserSession(currentUser);


            }
            else if(resultCode == LogTagLoginConfig.CUSTOM_LOGIN_REQUEST){
                LogTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
                Userpreferences.with(getApplicationContext()).setUserSession(currentUser);

            }
        /*else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }*/
            else if(resultCode == RESULT_CANCELED){
                setTitle(fail);
            }

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = Userpreferences.with(getApplicationContext()).getCurrentUser();
        if(Userpreferences.with(getApplicationContext()).contains("user_session_key")) {
            init();

        }
        else{
           loadLogin();
        }




    }

    void loadLogin(){
         logTagLoginBuilder = new LogTagLoginBuilder();
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_friends");
         intent = logTagLoginBuilder.with(getApplicationContext())
                .setAppLogo(R.drawable.elogtag_invert)
                .isFacebookLoginEnabled(true)
                .withFacebookAppId(getString(R.string.facebook_app_id)).withFacebookPermissions(permissions)
                .isGoogleLoginEnabled(true)
                .isCustomLoginEnabled(true, LogTagLoginConfig.LoginType.withEmail)
                .setSmartCustomLoginHelper(LogTagDashboardActivity.this)
                .build();
        startActivityForResult(intent, LogTagLoginConfig.LOGIN_REQUEST);



    }

    void init(){
        String x = Userpreferences.with(getApplicationContext()).read("user_session_key","Unavailable");
        currentUser = Userpreferences.with(getApplicationContext()).readObject("user_session_key");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_mail_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        LogTagDashboardActivity.Adapter adapter = new LogTagDashboardActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new CheeseListFragment(), "ALERT TRACKER");
        adapter.addFragment(new CheeseListFragment(), "REPORT");
        setTitle("Welcome "+currentUser.getUsername());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //     searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LogTagDashboardActivity.this.finish();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    private void setupViewPager(ViewPager viewPager) {

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean customSignin(LogTagUser logTagUser) {
        return false;
    }

    @Override
    public boolean customSignup(LogTagUser logTagNewUser) {
        return false;
    }

    @Override
    public boolean customUserSignout(LogTagUser logTagUser) {
        return false;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }





}

