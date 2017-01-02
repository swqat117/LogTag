package com.quascenta.logTag.main.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class Google_logTagUser extends LogTagUser implements Parcelable{

    private String displayName;
    private Uri photoUrl;
    //private String idToken;
    //private String serverAuthCode;



    public Google_logTagUser(){}


    private Google_logTagUser(Parcel parcel){
        super(parcel);
        displayName = parcel.readString();
        photoUrl = parcel.readParcelable(Uri.class.getClassLoader());


    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(displayName);
        parcel.writeParcelable(photoUrl, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Google_logTagUser> CREATOR = new Creator<Google_logTagUser>() {
        @Override
        public Google_logTagUser createFromParcel(Parcel parcel) {
            return new Google_logTagUser(parcel);
        }

        @Override
        public Google_logTagUser[] newArray(int i) {
            return new Google_logTagUser[i];
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
