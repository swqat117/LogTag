package com.quascenta.logTag.main.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by AKSHAY on 12/28/2016.
 */

public class Userpreferences {
    static final String DEFAULT_SESSION_VALUE = "No logged in user";
   private static SharedPreferences sharedPreferences;
   private static Userpreferences userpreferences;

   private static final String PREFERENCES_NAME = "preferences";
   private static final String LENGTH = "_length";
    static final String USER_SESSION = "user_session_key";
    static final String USER_PREFS = "codelight_studios_user_prefs";

    private static final String DEFAULT_STRING_VALUE = "";
    private static final int DEFAULT_INT_VALUE = -1;
    private static final double DEFAULT_DOUBLE_VALUE = -1d;
    private static final float DEFAULT_FLOAT_VALUE = -1f;
    private static final long DEFAULT_LONG_VALUE = -1l;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;


    private Userpreferences(@NonNull Context context){
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);


    }

    private Userpreferences(@NonNull Context context, @NonNull String preferencesName){
        sharedPreferences = context.getApplicationContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }




    public static Userpreferences with(@NonNull Context context){
        if(userpreferences == null){
            userpreferences = new Userpreferences(context);
        }
        return userpreferences;
    }



    public static Userpreferences with(@NonNull Context context, boolean forceInstantiation){
        if(forceInstantiation){
            userpreferences = new Userpreferences(context);
        }
        return userpreferences;
    }

    public static Userpreferences with(@NonNull Context context, @NonNull String preferencesName,
                             boolean forceInstantiation) {
        if (forceInstantiation) {
            userpreferences = new Userpreferences(context, preferencesName);
        }
        return userpreferences;
    }

    // String related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public String read(String what) {
        return sharedPreferences.getString(what, DEFAULT_STRING_VALUE);
    }

    /**
     * @param what
     * @param defaultString
     * @return Returns the stored value of 'what'
     */
    public String read(String what, String defaultString) {
        return sharedPreferences.getString(what, defaultString);
    }

    /**
     * @param where
     * @param what
     */
    public void write(String where, String what) {
        sharedPreferences.edit().putString(where, what).apply();
    }

    // int related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public int readInt(String what) {
        return sharedPreferences.getInt(what, DEFAULT_INT_VALUE);
    }

    /**
     * @param what
     * @param defaultInt
     * @return Returns the stored value of 'what'
     */
    public int readInt(String what, int defaultInt) {
        return sharedPreferences.getInt(what, defaultInt);
    }

    /**
     * @param where
     * @param what
     */
    public void writeInt(String where, int what) {
        sharedPreferences.edit().putInt(where, what).apply();
    }

    // double related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public double readDouble(String what) {
        if (!contains(what))
            return DEFAULT_DOUBLE_VALUE;
        return Double.longBitsToDouble(readLong(what));
    }

    /**
     * @param what
     * @param defaultDouble
     * @return Returns the stored value of 'what'
     */
    public double readDouble(String what, double defaultDouble) {
        if (!contains(what))
            return defaultDouble;
        return Double.longBitsToDouble(readLong(what));
    }

    /**
     * @param where
     * @param what
     */
    public void writeDouble(String where, double what) {
        writeLong(where, Double.doubleToRawLongBits(what));
    }

    // float related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public float readFloat(String what) {
        return sharedPreferences.getFloat(what, DEFAULT_FLOAT_VALUE);
    }

    /**
     * @param what
     * @param defaultFloat
     * @return Returns the stored value of 'what'
     */
    public float readFloat(String what, float defaultFloat) {
        return sharedPreferences.getFloat(what, defaultFloat);
    }

    /**
     * @param where
     * @param what
     */
    public void writeFloat(String where, float what) {
        sharedPreferences.edit().putFloat(where, what).apply();
    }

    // long related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public long readLong(String what) {
        return sharedPreferences.getLong(what, DEFAULT_LONG_VALUE);
    }

    /**
     * @param what
     * @param defaultLong
     * @return Returns the stored value of 'what'
     */
    public long readLong(String what, long defaultLong) {
        return sharedPreferences.getLong(what, defaultLong);
    }

    /**
     * @param where
     * @param what
     */
    public void writeLong(String where, long what) {
        sharedPreferences.edit().putLong(where, what).apply();
    }

    // boolean related methods

    /**
     * @param what
     * @return Returns the stored value of 'what'
     */
    public boolean readBoolean(String what) {
        return sharedPreferences.getBoolean(what, DEFAULT_BOOLEAN_VALUE);
    }

    /**
     * @param what
     * @param defaultBoolean
     * @return Returns the stored value of 'what'
     */
    public boolean readBoolean(String what, boolean defaultBoolean) {
        return sharedPreferences.getBoolean(what, defaultBoolean);
    }

    /**
     * @param where
     * @param what
     */
    public void writeBoolean(String where, boolean what) {
        sharedPreferences.edit().putBoolean(where, what).apply();
    }

    // String set methods

    public void writeObject(LogTagUser user, String x){

        sharedPreferences.edit().putString(x,new Gson().toJson(user)).apply();


    }

    public LogTagUser readObject( String x){
        LogTagUser user1 = new Google_logTagUser();
        Gson gson = new GsonBuilder().create();

        user1 = gson.fromJson(sharedPreferences.getString(x,"defaultstring"),LogTagUser.class);
        System.out.println("Sdf"+user1.getUsername());
        return user1;
    }


    /**
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putStringSet(final String key, final Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sharedPreferences.edit().putStringSet(key, value).apply();
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value);
        }
    }

    /**
     * @param key
     * @param value
     */
    public void putOrderedStringSet(String key, Set<String> value) {
        int stringSetLength = 0;
        if (sharedPreferences.contains(key + LENGTH)) {
            // First read what the value was
            stringSetLength = readInt(key + LENGTH);
        }
        writeInt(key + LENGTH, value.size());
        int i = 0;
        for (String aValue : value) {
            write(key + "[" + i + "]", aValue);
            i++;
        }
        for (; i < stringSetLength; i++) {
            // Remove any remaining values
            remove(key + "[" + i + "]");
        }
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the String Set with HoneyComb compatibility
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(final String key, final Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            // Workaround for pre-HC's missing getStringSet
            return getOrderedStringSet(key, defValue);
        }
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the ordered String Set
     */
    public Set<String> getOrderedStringSet(String key, final Set<String> defValue) {
        if (contains(key + LENGTH)) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                for (int i = 0; i < stringSetLength; i++) {
                    set.add(read(key + "[" + i + "]"));
                }
            }
            return set;
        }
        return defValue;
    }

    // end related methods

    /**
     * @param key
     */
    public void remove(final String key) {
        if (contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                sharedPreferences.edit().remove(key + LENGTH).apply();
                for (int i = 0; i < stringSetLength; i++) {
                    sharedPreferences.edit().remove(key + "[" + i + "]").apply();
                }
            }
        }
        sharedPreferences.edit().remove(key);
    }

    /**
     * @param key
     * @return Returns if that key exists
     */
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }


    public  LogTagUser getCurrentUser(){
        LogTagUser smartUser = null;

        Gson gson = new Gson();
        String sessionUser = sharedPreferences.getString(USER_SESSION, DEFAULT_SESSION_VALUE);
        String user_type = sharedPreferences.getString(LogTagLoginConfig.USER_TYPE, LogTagLoginConfig.CUSTOMUSERFLAG);
        if(sessionUser.equals(DEFAULT_SESSION_VALUE)){
            try {
                switch (user_type) {
                    case LogTagLoginConfig.FACEBOOKFLAG:
                        smartUser = gson.fromJson(sessionUser, Facebook_logTagUser.class);
                        break;
                    case LogTagLoginConfig.GOOGLEFLAG:
                        smartUser = gson.fromJson(sessionUser, Google_logTagUser.class);
                        break;
                    default:
                        smartUser = gson.fromJson(sessionUser, LogTagUser.class);
                        break;
                }
            }catch (Exception e){
                Log.e("GSON", e.getMessage());
            }
        }
        return smartUser;
    }

    /**
     This method sets the session object for the current logged in user.
     This is called from inside the SmartLoginActivity to save the
     current logged in user to the shared preferences.
     */
    public  boolean setUserSession( LogTagUser smartUser){

        SharedPreferences.Editor editor;
        if(smartUser != null) {
            try {

                editor = sharedPreferences.edit();

                if (smartUser instanceof Facebook_logTagUser) {
                    editor.putString(LogTagLoginConfig.USER_TYPE, LogTagLoginConfig.FACEBOOKFLAG);
                } else if (smartUser instanceof Google_logTagUser) {
                    editor.putString(LogTagLoginConfig.USER_TYPE, LogTagLoginConfig.GOOGLEFLAG);
                } else {
                    editor.putString(LogTagLoginConfig.USER_TYPE, LogTagLoginConfig.CUSTOMUSERFLAG);
                }

                Gson gson = new Gson();
                smartUser.setPassword(null);
                String sessionUser = gson.toJson(smartUser);
                Log.d("GSON User session", sessionUser);
                editor.putString(USER_SESSION, sessionUser);
                editor.apply();
                return true;
            } catch (Exception e) {
                Log.e("Session Error 1", e.getMessage());
                return false;
            }
        } else {
            Log.e("Session Error 2", "User is null");
            return false;
        }
    }

    /**
     * Clear all the preferences
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

}