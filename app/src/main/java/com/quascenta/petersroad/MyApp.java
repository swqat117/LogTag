package com.quascenta.petersroad;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.quascenta.petersroad.activities.SocialLoginManager;
import com.quascenta.petersroad.model.DaoMaster;
import com.quascenta.petersroad.model.DaoSession;
import com.quascenta.petersroad.Utils.DummyDevices;
import com.quascenta.petersroad.Utils.Objectgenerator;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Random;

import timber.log.Timber;

/**
 * Created by AKSHAY on 10/27/2016.
 */
public class MyApp extends MultiDexApplication {

    private static MyApp _instance;
    private RefWatcher _refWatcher;
    private static MyApp sInstance;
    public DaoSession daoSession;

    public static MyApp get(){
        return _instance;
    }

    public static RefWatcher getRefWatcher(){
        return MyApp.get()._refWatcher;
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
    public static synchronized MyApp getInstance(){return sInstance;}
    public static ArrayList<Objectgenerator> devices ;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        _instance = (MyApp) getApplicationContext();
        _refWatcher = LeakCanary.install(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"lease-db",null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        sInstance = this;
        SocialLoginManager.init(this);
        devices = getRandomSublist(DummyDevices.sLocationStrings,8,DummyDevices.sLocationStrings,DummyDevices.sLocationCompanies,DummyDevices.sLocationCompanies,DummyDevices.scust_track_id,DummyDevices.sid);
        Timber.plant(new Timber.DebugTree());
    }

    private ArrayList<Objectgenerator> getRandomSublist(String[] source_location, int amount,String[] destinaton_location ,String[] source_company,String[] destination_company,String[] trid,String[] serial) {
        ArrayList<Objectgenerator> list = new ArrayList<>(amount);

        Random random = new Random();
       for (int i =0; i<amount;i++){
           list.add(new Objectgenerator(trid[random.nextInt(amount)],serial[random.nextInt(amount)],source_location[random.nextInt(amount)],source_company[random.nextInt(amount)],destination_company[random.nextInt(amount)],destinaton_location[random.nextInt(amount)],10));
    }
        return list;
    }

    public void Randomgenerator(){


    }
}
