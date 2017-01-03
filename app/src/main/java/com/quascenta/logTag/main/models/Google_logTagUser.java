package com.quascenta.logTag.main.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class Google_logTagUser extends LogTagUser implements Parcelable{

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
    private String displayName1;
    //private String idToken;
    //private String serverAuthCode;
    private Uri photoUrl;


    public Google_logTagUser(){}

    private Google_logTagUser(Parcel parcel){
        super(parcel);
        displayName1 = parcel.readString();
        photoUrl = parcel.readParcelable(Uri.class.getClassLoader());


    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(displayName1);
        parcel.writeParcelable(photoUrl, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDisplayName() {
        return displayName1;
    }

    public void setDisplayName(String displayName) {
        this.displayName1 = displayName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
