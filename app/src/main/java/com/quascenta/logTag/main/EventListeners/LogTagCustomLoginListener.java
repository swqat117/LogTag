package com.quascenta.logTag.main.EventListeners;

import com.quascenta.logTag.main.models.LogTagUser;

/**
 * Created by AKSHAY on 12/27/2016.
 */

public interface LogTagCustomLoginListener {

    boolean customSignin(LogTagUser logTagUser);
    boolean customSignup(LogTagUser logTagNewUser);

}
