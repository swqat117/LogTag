package com.quascenta.logTag.main.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLoginListener;
import com.quascenta.logTag.main.EventListeners.LogTagCustomLogoutListener;
import com.quascenta.logTag.main.configuration.LogTagLoginBuilder;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.manager.UserSessionManager;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;
import com.quascenta.petersroad.activities.MainActivity;
import com.quascenta.petersroad.broadway.R;

import java.util.ArrayList;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class LogTagMainActivity extends AppCompatActivity implements LogTagCustomLogoutListener, LogTagCustomLoginListener {
    //SmartFacebookResult smartFacebookResult;
    TextView loginResult;
    CheckBox customLogin, facebookLogin, googleLogin, appLogoCheckBox;
    LogTagUser currentUser;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.button);
        loginResult = (TextView) findViewById(R.id.textView);
        LogTagLoginBuilder loginBuilder = new LogTagLoginBuilder();

        //Set facebook permissions
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_friends");


        //get the current user details
        currentUser = UserSessionManager.getCurrentUser(this);
        String display = "no user";
        if(currentUser != null) {
            if (currentUser instanceof Facebook_logTagUser) {
                Facebook_logTagUser facebookUser = (Facebook_logTagUser) currentUser;
                display = facebookUser.getProfileName() + " (FacebookUser)is logged in";
            } else if (currentUser instanceof Google_logTagUser) {
                display = ((Google_logTagUser) currentUser).getDisplayName() + " (GoogleUser) is logged in";
            } else {
                display = currentUser.getUsername() + " (Custom User) is logged in";
            }
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentUser != null) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(LogTagMainActivity.this);
                        builder.setMessage(R.string.user_exists);
                        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UserSessionManager.logout(LogTagMainActivity.this, currentUser, LogTagMainActivity.this, mGoogleApiClient);
                                currentUser = UserSessionManager.getCurrentUser(LogTagMainActivity.this);
                            }
                        });
                        builder.setCancelable(true);

                        builder.create().show();
                    } else {

                        LogTagLoginBuilder loginBuilder = new LogTagLoginBuilder();

                        //Set facebook permissions

                        ArrayList<String> permissions = new ArrayList<>();
                        permissions.add("public_profile");
                        permissions.add("email");
                        permissions.add("user_birthday");
                        permissions.add("user_friends");
                        Intent intent = loginBuilder.with(getApplicationContext())
                                .setAppLogo(R.drawable.cheese_4)
                                .isFacebookLoginEnabled(true)
                                .withFacebookAppId(getString(R.string.facebook_app_id)).withFacebookPermissions(permissions)
                                .isGoogleLoginEnabled(true)
                                .isCustomLoginEnabled(true, LogTagLoginConfig.LoginType.withEmail)
                                .setSmartCustomLoginHelper(LogTagMainActivity.this)
                                .build();

                        startActivityForResult(intent, LogTagLoginConfig.LOGIN_REQUEST);
                        //startActivity(intent);
                    }
                }
            });
        }
    }

    private int getlogo() {
        if(appLogoCheckBox.isChecked()){
            return R.drawable.cheese_4;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fail = "Login Failed";
        if(resultCode == LogTagLoginConfig.FACEBOOK_LOGIN_REQUEST){
            Facebook_logTagUser user;
            try {
                user = data.getParcelableExtra(LogTagLoginConfig.USER);
                String userDetails = user.getProfileName() + " " + user.getEmail() + " " + user.getBirthday();
                loginResult.setText(userDetails);
            }catch (Exception e){
                loginResult.setText(fail);
            }
        }
        else if(resultCode == LogTagLoginConfig.GOOGLE_LOGIN_REQUEST){
            Google_logTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
            String userDetails = user.getEmail() + " " + user.getDisplayName();
            loginResult.setText(userDetails);
        }
        else if(resultCode == LogTagLoginConfig.CUSTOM_LOGIN_REQUEST){
            LogTagUser user = data.getParcelableExtra(LogTagLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }
        /*else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }*/
        else if(resultCode == RESULT_CANCELED){
            loginResult.setText(fail);
        }

    }

    @Override
    public boolean customUserSignout(LogTagUser smartUser) {
        //Implement your logic
        UserSessionManager.logout(this, smartUser, this, mGoogleApiClient);
        return true;
    }


    @Override
    public boolean customSignin(LogTagUser user) {
        //This "user" will have only username and password set.
        Toast.makeText(this, user.getUsername() + " " + user.getEmail(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean customSignup(LogTagUser newUser) {
        //Implement your our custom sign up logic and return true if success
        return true;
    }
}
