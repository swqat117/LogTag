package com.quascenta.petersroad.interfaces;

import rx.Observable;
import rx.Subscription;

/**
 * Created by AKSHAY on 11/1/2016.
 */

public interface LoginPresenter {

        Subscription add(Observable<CharSequence> username, Observable<CharSequence> password);
}
