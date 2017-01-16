package com.quascenta.QBlueLogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by AKSHAY on 1/16/2017.
 */


    public class MyDbHelper extends SQLiteOpenHelper {
        // Database Version
        private static final int DATABASE_VERSION = 11;

        // Database Name
        private static final String DATABASE_NAME = "mwcwalletconndev";

        private static final String TABLE_CARDS = "cardsconndev";
        private static final String TABLE_DEVICES = "devicesconndev";

        private static final String KEY_ID = "id";
        private static final String KEY_ID_SCRIPT = "id_script";
        private static final String KEY_ID_VC = "id_vc";
        private static final String KEY_ID_DEV = "id_dev";
        private static final String KEY_ICON = "icon";
        private static final String KEY_NAME = "name";
        private static final String KEY_NUMBER = "number";
        private static final String KEY_CVC = "cvc";
        private static final String KEY_EXP_YEAR = "year";
        private static final String KEY_EXP_MONTH = "month";
        private static final String KEY_FAV = "fav";
        private static final String KEY_STATUS = "status";
        private static final String KEY_LOCK = "lock";
        private static final String KEY_MIFARE_TYPE = "mifare_type";
        private static final String KEY_TYPE = "type";
        private static final String KEY_MAC = "mac";
        private static final String KEY_ORDER = "listOrder";


        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE_CARDS = "CREATE TABLE " + TABLE_CARDS + " ( "
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "id_script INTEGER, " + "id_vc INTEGER, " + "id_dev INTEGER, " + "name TEXT, " + "number TEXT, " + "cvc TEXT, " + "month TEXT, " + "year TEXT, "
                    + "status INTEGER, "+ "fav INTEGER, " + "lock INTEGER, " + "icon INTEGER, " + "mifare_type INTEGER, " + "type INTEGER, " + "listOrder INTEGER)";

            String CREATE_TABLE_DEVICES = "CREATE TABLE " + TABLE_DEVICES + " ( "
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "mac TEXT)";

            db.execSQL(CREATE_TABLE_CARDS);
            db.execSQL(CREATE_TABLE_DEVICES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);

            this.onCreate(db);
        }

        public List<Device> getAllDevices() {
            SQLiteDatabase db = this.getReadableDatabase();
            List<Device> devicesList = new LinkedList<Device>();

            String query = "SELECT * FROM " + TABLE_DEVICES;
            Cursor cursor = db.rawQuery(query, null);

            Device device = null;
            if (cursor.moveToFirst()) {
                do {
                    // By default I create the device without services and then I add this value based on the request to Cards database
                    device = new Device(cursor.getInt(0), cursor.getString(1), cursor.getString(2), 0);

                    String queryServices = "SELECT * FROM " + TABLE_CARDS + " WHERE " + KEY_ID_DEV + " = " + device.getId();
                    Cursor cursorServices = db.rawQuery(queryServices, null);

                    // Update the count
                    device.setDeviceServices(cursorServices.getCount());

                    devicesList.add(device);
                } while (cursor.moveToNext());
            }

            return devicesList;
        }

        public int addDevice(String name, String mac){
            SQLiteDatabase db = this.getWritableDatabase();
            int id = 0;

            String query = "SELECT id FROM " + TABLE_DEVICES + " WHERE " + KEY_NAME + " = '" + name + "' AND " + KEY_MAC + " = '" + mac + "'";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                } while (cursor.moveToNext());
            }

            if(id <= 0) {
                ContentValues values = new ContentValues();
                values.put(KEY_NAME, name);
                values.put(KEY_MAC, mac);

                id = (int) db.insert(TABLE_DEVICES,
                        null,
                        values);
            }

            db.close();
            return id;
        }

    public void deleteDevice(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DEVICES, KEY_ID+" = ?",
                new String[] { String.valueOf(id) });

        db.close();
    }
}
