package com.quascenta.logTag.main.configuration;

import android.content.Context;
import android.content.Intent;

import com.quascenta.logTag.main.EventListeners.LogTagCustomLoginListener;
import com.quascenta.logTag.main.activities.LogTagLoginActivity;

import java.util.ArrayList;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class LogTagLoginBuilder {

    private Context context;
    private LogTagLoginConfig config;
    public static LogTagCustomLoginListener logTagCustomLoginListener;
    //private static final String CONFIGDATA = "config";

    public LogTagLoginBuilder() {
        config = new LogTagLoginConfig();
        config.setAppLogo(0);
        config.setIsFacebookEnabled(false);
        config.setIsGoogleEnabled(false);
    }

    public LogTagLoginBuilder with(Context context){
        this.context = context;
        return this;
    }

    public LogTagLoginBuilder setAppLogo(int logo){
        config.setAppLogo(logo);
        return this;
    }

    public LogTagLoginBuilder isFacebookLoginEnabled(boolean facebookLogin){
        config.setIsFacebookEnabled(facebookLogin);
        return this;
    }

    public LogTagLoginBuilder isGoogleLoginEnabled(boolean googleLogin){
        config.setIsGoogleEnabled(googleLogin);
        return this;
    }

    public LogTagLoginBuilder setSmartCustomLoginHelper(LogTagCustomLoginListener mSmartCustomLoginListener) {
        LogTagLoginBuilder.logTagCustomLoginListener = mSmartCustomLoginListener;
        return this;
    }

    public LogTagLoginBuilder withFacebookAppId(String appId){
        config.setFacebookAppId(appId);
        return this;
    }

    public LogTagLoginBuilder withFacebookPermissions(ArrayList<String> permissions){
        config.setFacebookPermissions(permissions);
        return this;
    }

    public LogTagLoginBuilder isCustomLoginEnabled(boolean customlogin, LogTagLoginConfig.LoginType loginType){
        config.setIsCustomLoginEnabled(customlogin);
        config.setLoginType(loginType);
        return this;
    }

    public Intent build(){
        Intent intent = new Intent(context, LogTagLoginActivity.class);
        intent.putExtras(config.pack());
        return intent;
    }




}
