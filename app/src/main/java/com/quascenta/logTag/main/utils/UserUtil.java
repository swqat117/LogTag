package com.quascenta.logTag.main.utils;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.quascenta.logTag.main.configuration.LogTagLoginConfig;
import com.quascenta.logTag.main.models.Facebook_logTagUser;
import com.quascenta.logTag.main.models.Google_logTagUser;
import com.quascenta.logTag.main.models.LogTagUser;

import org.json.JSONException;
import org.json.JSONObject;



public class UserUtil {

    public Google_logTagUser populateGoogleUser(GoogleSignInAccount account){
        //Create a new google user
        Google_logTagUser googleUser = new Google_logTagUser();
        //populate the user
        googleUser.setDisplayName(account.getDisplayName());
        //googleUser.setIdToken(account.getIdToken());
        googleUser.setPhotoUrl(account.getPhotoUrl());
        googleUser.setEmail(account.getEmail());
        //googleUser.setServerAuthCode(account.getServerAuthCode());

        //return the populated google user
        return googleUser;
    }

    public Facebook_logTagUser populateFacebookUser(JSONObject object){
        Facebook_logTagUser facebookUser = new Facebook_logTagUser();
        facebookUser.setGender(-1);
        try {
            if (object.has(LogTagLoginConfig.FacebookFields.EMAIL))
                facebookUser.setEmail(object.getString(LogTagLoginConfig.FacebookFields.EMAIL));
            if (object.has(LogTagLoginConfig.FacebookFields.BIRTHDAY))
                facebookUser.setBirthday(object.getString(LogTagLoginConfig.FacebookFields.BIRTHDAY));
            if (object.has(LogTagLoginConfig.FacebookFields.GENDER)) {
                try {
                    LogTagLoginConfig.Gender gender = LogTagLoginConfig.Gender.valueOf(object.getString(LogTagLoginConfig.FacebookFields.GENDER));
                    switch (gender) {
                        case male:
                            facebookUser.setGender(0);
                            break;
                        case female:
                            facebookUser.setGender(1);
                            break;
                    }
                } catch (Exception e) {
                    //if gender is not in the enum it is set to unspecified value (-1)
                    facebookUser.setGender(-1);
                    Log.e(getClass().getSimpleName(), e.getMessage());
                }
            }
            if (object.has(LogTagLoginConfig.FacebookFields.LINK))
                facebookUser.setProfileLink(object.getString(LogTagLoginConfig.FacebookFields.LINK));
            if (object.has(LogTagLoginConfig.FacebookFields.ID))
                facebookUser.setUserId(object.getString(LogTagLoginConfig.FacebookFields.ID));
            if (object.has("name"))
                facebookUser.setUsername(object.getString("name"));
            if (object.has(LogTagLoginConfig.FacebookFields.FIRST_NAME))
                facebookUser.setFirstName(object.getString(LogTagLoginConfig.FacebookFields.FIRST_NAME));
            if (object.has(LogTagLoginConfig.FacebookFields.MIDDLE_NAME))
                facebookUser.setMiddleName(object.getString(LogTagLoginConfig.FacebookFields.MIDDLE_NAME));
            if (object.has(LogTagLoginConfig.FacebookFields.LAST_NAME))
                facebookUser.setLastName(object.getString(LogTagLoginConfig.FacebookFields.LAST_NAME));
        } catch (JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
            facebookUser = null;
        }
        return facebookUser;
    }

    public LogTagUser populateCustomUserWithUserName(String username, String email, String password){
        LogTagUser user = new LogTagUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(-1);
        return user;
    }

    public LogTagUser populateCustomUserWithEmail(String username, String email, String password){
        LogTagUser user = new LogTagUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(-1);
        return user;
    }

}
