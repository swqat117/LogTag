package com.quascenta.logTag.main.models;

import android.hardware.camera2.params.Face;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class Facebook_logTagUser extends LogTagUser implements Parcelable {

    private String profileName;


    public Facebook_logTagUser(){}




    protected Facebook_logTagUser(Parcel in){
        super(in);
        profileName = in.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        profileName = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

public static final Creator<Facebook_logTagUser> CREATOR = new Creator<Facebook_logTagUser>() {
    @Override
    public Facebook_logTagUser createFromParcel(Parcel parcel) {
        return new Facebook_logTagUser(parcel);
    }

    @Override
    public Facebook_logTagUser[] newArray(int i) {
        return new Facebook_logTagUser[i];
    }
};
    public String getProfileName(){
        return profileName;
    }

    public void setProfileName(String pr){
        this.profileName = profileName;

    }
}
