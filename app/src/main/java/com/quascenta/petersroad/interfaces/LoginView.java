package com.quascenta.petersroad.interfaces;

/**
 * Created by AKSHAY on 11/1/2016.
 */

public interface LoginView {

    public void updateUI();

    public void setUsernameError(String message,int code);

    public void setPasswordError(String error, int code);

    public void onSuccess(String message, int code);

}
