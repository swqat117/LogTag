package com.quascenta.logTag.main.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quascenta.logTag.main.configuration.LogTagLoginBuilder;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.manager.UserSessionManager;
import com.quascenta.logTag.main.manager.Userpreferences;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;
import com.quascenta.logTag.main.utils.DialogUtil;
import com.quascenta.logTag.main.utils.UserUtil;
import com.quascenta.petersroad.broadway.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class LogTagLoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 105;
    CallbackManager callbackManager;
    LogTagLoginConfig config;

    EditText usernameEditText, passwordEditText, usernameSignUp, emailSignup, passwordSignup , repeatPasswordSignup;
    ProgressDialog progress;
    ViewGroup mContainer;
    UserSessionManager sessionManager;
    LinearLayout signinContainer , signUpContainer;
    ImageView appLogo;


    private GoogleApiClient mgoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_tag_login);
        Bundle bundle = getIntent().getExtras();
        config = LogTagLoginConfig.unpack(bundle);
        FacebookSdk.setApplicationId(config.getFacebookAppId());

        //Get the containers required to inject the views
        mContainer = (ViewGroup) findViewById(R.id.main_container);
        signinContainer = (LinearLayout) findViewById(R.id.signin_container);
        signUpContainer = (LinearLayout) findViewById(R.id.signup_container);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //include view based on the user settings

        if(config.isCustomLoginEnabled()){
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_log_tag_customlogin,mContainer, false));

            if(config.isFacebookEnabled() || config.isGoogleEnabled()){
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_log_tag_divider,mContainer,false));
            }
            signUpContainer.addView(layoutInflater.inflate(R.layout.fragment_log_tag_customsignup,mContainer, false));
            findViewById(R.id.custom_signin_button).setOnClickListener(this);
            findViewById(R.id.custom_signup_button).setOnClickListener(this);
            findViewById(R.id.user_signup_button).setOnClickListener(this);
            signUpContainer.setVisibility(View.GONE);

        }

        if (config.isFacebookEnabled()){
            signinContainer.addView(layoutInflater.inflate(R.layout.fragment_log_tag_facebooklogin,mContainer, false));
            AppCompatButton facebookButton  = (AppCompatButton) findViewById(R.id.login_fb_button);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_vector,0,0,0);
            } else {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white_36dp,0,0,0);
            }

            facebookButton.setOnClickListener(this);
                    }

        if(config.isGoogleEnabled()){
             signinContainer.addView(layoutInflater.inflate(R.layout.fragment_log_tag_googlelogin,mContainer,false));
            AppCompatButton googleButton = (AppCompatButton) findViewById(R.id.login_google_button);

            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP){
                googleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_vector,0,0,0);
            } else{
                googleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_plus_white_36dp,0,0,0);
            }
            googleButton.setOnClickListener(this);
        }

        //bind the views

        appLogo = (ImageView) findViewById(R.id.applogo_imageView);
        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameSignUp = (EditText) findViewById(R.id.userNameSignUp);
        passwordSignup = (EditText) findViewById(R.id.passwordSignUp);
        repeatPasswordSignup = (EditText) findViewById(R.id.repeatPasswordSignUp);
        emailSignup = (EditText) findViewById(R.id.emailSignUp);

        if(config.getAppLogo() != 0 ){
            appLogo.setImageResource(config.getAppLogo());
        }else {
            appLogo.setVisibility(View.GONE);
        }
        hideSystemUI();
        callbackManager = CallbackManager.Factory.create();
    }
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        getSupportActionBar().hide();
    }
    @Override
    public void onClick(View view) {
         int id = view.getId();

        if(id == R.id.login_fb_button){
            doFacebookLogin();

        } else if(id == R.id.login_google_button){
            doGoogleLogin();
        } else if( id == R.id.custom_signin_button){
            doCustomSignin();
        } else if (id == R.id.custom_signup_button){
            signinContainer.setVisibility(View.GONE);
            signUpContainer.setVisibility(View.VISIBLE);
            findViewById(R.id.userNameSignUp).requestFocus();
        } else if( id == R.id.user_signup_button){
            doCustomSignup();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String TAG = "GOOGLE LOGIN";
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        progress.dismiss();
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for Facebook Login
        callbackManager.onActivityResult(requestCode,resultCode,data);


        if(requestCode == RC_SIGN_IN){
            progress = ProgressDialog.show(this,"",getString(R.string.getting_data),true);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("GOOGLE SIGN IN", "handleSigninResult:"+result.isSuccess());

            if (result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();
                UserUtil util = new UserUtil();
                Google_logTagUser google_logTagUser = util.populateGoogleUser(account);
                progress.dismiss();
                finishLogin(google_logTagUser);
            } else{
                Log.d("GOOGLE SIGN IN", ""+requestCode);
                progress.dismiss();
                Toast.makeText(this,"Google Login Failed, Please Try Again", Toast.LENGTH_SHORT).show();
                finishLogin(null);
            }
        }
        if(progress != null){
            progress.dismiss();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }



    private void doGoogleLogin() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile().requestId()
                .build();
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* On~ConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progress.dismiss();
        //mGoogleApiClient.connect();
    }

    private void doCustomSignup() {
        String username = usernameSignUp.getText().toString();
        String password = passwordSignup.getText().toString();
        String repeatPassword = repeatPasswordSignup.getText().toString();
        String email = emailSignup.getText().toString();
        if(username.equals("")){
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            usernameSignUp.setError(getResources().getText(R.string.username_error));
            usernameSignUp.requestFocus();
        } else if(password.equals("")) {
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            passwordSignup.setError(getResources().getText(R.string.password_error));
            passwordSignup.requestFocus();
        } else if(email.equals("")){
            //DialogUtil.getErrorDialog(R.string.no_email_error, this).show();
            emailSignup.setError(getResources().getText(R.string.no_email_error));
            emailSignup.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //DialogUtil.getErrorDialog(R.string.invalid_email_error, this).show();
            emailSignup.setError(getResources().getText(R.string.invalid_email_error));
            emailSignup.requestFocus();
        } else if(!password.equals(repeatPassword)){
            //DialogUtil.getErrorDialog(R.string.password_mismatch, this).show();
            repeatPasswordSignup.setError(getResources().getText(R.string.password_mismatch));
            repeatPasswordSignup.requestFocus();
        }
        else {
            if (LogTagLoginBuilder.logTagCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.loading_holder), true);
                LogTagUser newUser = new UserUtil().populateCustomUserWithUserName(username, email, password);
                if (LogTagLoginBuilder.logTagCustomLoginListener.customSignup(newUser)) {
                    progress.dismiss();
                    setResult(LogTagLoginConfig.CUSTOM_SIGNUP_REQUEST);
                    finishLogin(newUser);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }

    }

    private void doCustomSignin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.equals("")){
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            if(config.getLoginType() == LogTagLoginConfig.LoginType.withUsername) {
                usernameEditText.setError(getResources().getText(R.string.username_error));
            } else {
                usernameEditText.setError(getResources().getText(R.string.email_error));
            }
            usernameEditText.requestFocus();
        } else if(password.equals("")){
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            passwordEditText.setError(getResources().getText(R.string.password_error));
            passwordEditText.requestFocus();
        } else {
            if (LogTagLoginBuilder.logTagCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
                LogTagUser user;
                if(config.getLoginType() == LogTagLoginConfig.LoginType.withUsername) {
                    user = new UserUtil().populateCustomUserWithUserName(username, null, password);
                } else {
                    user = new UserUtil().populateCustomUserWithEmail(null, username, password);
                }
                if (LogTagLoginBuilder.logTagCustomLoginListener.customSignin(user)) {
                    progress.dismiss();
                    setResult(LogTagLoginConfig.CUSTOM_LOGIN_REQUEST);
                    finishLogin(user);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    private void doFacebookLogin() {
        if(config.isFacebookEnabled()) {
            Toast.makeText(LogTagLoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
            ArrayList<String> permissions = config.getFacebookPermissions();
            if (permissions == null) {
                permissions = LogTagLoginConfig.getDefaultFacebookPermissions();
            }
            LoginManager.getInstance().logInWithReadPermissions(LogTagLoginActivity.this, permissions);
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progress.setMessage(getString(R.string.getting_data));
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            progress.dismiss();
                            UserUtil util = new UserUtil();
                            Facebook_logTagUser facebookUser = util.populateFacebookUser(object);
                            if (facebookUser != null) {
                                finishLogin(facebookUser);
                            } else {
                                finish();
                            }
                        }
                    });
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    progress.dismiss();
                    finish();
                    Log.d("Facebook Login", "User cancelled the login process");
                }

                @Override
                public void onError(FacebookException e) {
                    progress.dismiss();
                    finish();
                    Toast.makeText(LogTagLoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void finishLogin(LogTagUser smartUser){
        if(smartUser != null) {

            if (Userpreferences.with(getApplicationContext()).setUserSession(smartUser)) {
                 final Intent intent = new Intent();

                intent.putExtra(LogTagLoginConfig.USER, smartUser);
                if (smartUser instanceof Facebook_logTagUser) {
                    setResult(LogTagLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                } else if (smartUser instanceof Google_logTagUser) {
                    System.out.println(smartUser.toString());
                    Userpreferences.with(getApplicationContext()).writeObject(smartUser,"user_session_key");

                    setResult(LogTagLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
                    finish();
                } else {
                    setResult(LogTagLoginConfig.CUSTOM_LOGIN_REQUEST, intent);
                }
                finish();
            } else {
                DialogUtil.getErrorDialog(R.string.network_error, this);
                finish();
            }
        } else {
            DialogUtil.getErrorDialog(R.string.login_failed, this);
            finish();
        }
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(signUpContainer.getVisibility() == View.VISIBLE && config.isCustomLoginEnabled()){
            signUpContainer.setVisibility(View.GONE);
            signinContainer.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

}
