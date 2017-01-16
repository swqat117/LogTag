package com.quascenta.logTag.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.quascenta.QBlueLogger.Test2Activity;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLoginListener;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLogoutListener;
import com.quascenta.logTag.main.configuration.LogTagLoginBuilder;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.manager.Userpreferences;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;
import com.quascenta.petersroad.activities.MainActivity1;
import com.quascenta.petersroad.broadway.R;

import java.util.ArrayList;

/**
 * Created by AKSHAY on 12/28/2016.
 */

public class LogTagDashboardActivity extends AppCompatActivity implements LogTagCustomLogoutListener, LogTagCustomLoginListener {

    public static final String TAG = "MainActivity";
    Intent intent;
    LogTagUser currentUser;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fail = "Login Failed";
        if (resultCode == LogTagLoginConfig.FACEBOOK_LOGIN_REQUEST) {
            Facebook_logTagUser user;
            try {
                user = data.getParcelableExtra(LogTagLoginConfig.USER);
                Userpreferences.with(getApplicationContext()).setUserSession(currentUser);

            } catch (Exception e) {

            }
        } else if (resultCode == LogTagLoginConfig.GOOGLE_LOGIN_REQUEST) {
            Google_logTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
            Userpreferences.with(getApplicationContext()).setUserSession(currentUser);
            String userDetails = user.getEmail() + " " + user.getDisplayName();
            setTitle(userDetails);

        } else if (resultCode == LogTagLoginConfig.CUSTOM_LOGIN_REQUEST) {
            LogTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
            Userpreferences.with(getApplicationContext()).setUserSession(currentUser);
            String userDetails = user.getUsername() + " (Custom User)";
            setTitle(userDetails);
        }
        /*else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }*/
        else if (resultCode == RESULT_CANCELED) {
            setTitle(fail);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = Userpreferences.with(getApplicationContext()).getCurrentUser();
        if (Userpreferences.with(getApplicationContext()).contains("user_session_key")) {
            initBluetoothLoggers();

        } else {
            loadLogin();
        }


    }

    void loadLogin() {
        LogTagLoginBuilder logTagLoginBuilder = new LogTagLoginBuilder();
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_friends");
        Intent intent = logTagLoginBuilder.with(getApplicationContext())
                .setAppLogo(R.drawable.elogtag_invert)
                .isFacebookLoginEnabled(true)
                .withFacebookAppId(getString(R.string.facebook_app_id)).withFacebookPermissions(permissions)
                .isGoogleLoginEnabled(true)
                .isCustomLoginEnabled(true, LogTagLoginConfig.LoginType.withEmail)
                .setSmartCustomLoginHelper(LogTagDashboardActivity.this)
                .build();
        startActivityForResult(intent, LogTagLoginConfig.LOGIN_REQUEST);


    }

    void init() {
        String x = Userpreferences.with(getApplicationContext()).read("user_session_key", "Unavailable");
        currentUser = Userpreferences.with(getApplicationContext()).readObject("user_session_key");
        Intent intent = new Intent(this, MainActivity1.class);
        startActivity(intent);
        finish();
    }

    void initBluetoothLoggers(){
        String x = Userpreferences.with(getApplicationContext()).read("user_session_key", "Unavailable");
        currentUser = Userpreferences.with(getApplicationContext()).readObject("user_session_key");
        Intent intent = new Intent(this, Test2Activity.class);
        startActivity(intent);
        finish();
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


}

