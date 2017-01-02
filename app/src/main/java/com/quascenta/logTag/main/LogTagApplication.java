package com.quascenta.logTag.main;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.quascenta.petersroad.MyApp;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class LogTagApplication extends MyApp {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
    }
}
