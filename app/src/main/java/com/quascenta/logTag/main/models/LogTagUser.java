package com.quascenta.logTag.main.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public class LogTagUser implements Parcelable {

    private String userId;
    private String displayName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String birthday;
    private int gender;
    private String photourl;



    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return displayName;
    }

    public void setUsername(String username) {
        this.displayName = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileLink() {
        return photourl;
    }

    public void setProfileLink(String profileLink) {
        this.photourl = profileLink;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LogTagUser(){}


    protected LogTagUser(Parcel in){
        userId = in.readString();
        displayName = in.readString();
        password =  in.readString();
        firstName =  in.readString();
        middleName = in.readString();
        lastName =  in.readString();
        email =  in.readString();
        birthday =  in.readString();
        gender = in.readInt();
        photourl =  in.readString();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(userId);
        parcel.writeString(displayName);
        parcel.writeString(password);
        parcel.writeString(firstName);
        parcel.writeString(middleName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(birthday);
        parcel.writeInt(gender);
        parcel.writeString(photourl);
            }


public static final Creator<LogTagUser> CREATOR = new Creator<LogTagUser>() {
    @Override
    public LogTagUser createFromParcel(Parcel parcel) {
        return new LogTagUser(parcel);
    }

    @Override
    public LogTagUser[] newArray(int i) {
        return new LogTagUser[i];
    }
};

}
