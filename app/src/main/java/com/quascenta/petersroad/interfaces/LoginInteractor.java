package com.quascenta.petersroad.interfaces;

/**
 * Created by AKSHAY on 11/1/2016.
 */

public interface LoginInteractor {

    boolean validEmail(CharSequence username);

    boolean validPassword (CharSequence password);
}
