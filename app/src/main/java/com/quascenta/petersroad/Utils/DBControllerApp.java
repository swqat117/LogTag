package com.quascenta.petersroad.Utils;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.quascenta.petersroad.model.DaoMaster;
import com.quascenta.petersroad.model.DaoSession;

/**
 * Created by AKSHAY on 11/11/2016.
 */

public class DBControllerApp extends Application {


    private static DBControllerApp sInstance;
    public DaoSession daoSession;


    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"lease-db",null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        sInstance = this;
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }
    public static synchronized DBControllerApp getInstance(){return sInstance;}
}
