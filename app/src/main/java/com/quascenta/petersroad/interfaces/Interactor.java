package com.quascenta.petersroad.interfaces;

import com.quascenta.petersroad.model.device_log;

import java.util.ArrayList;

/**
 * Created by AKSHAY on 11/10/2016.
 */

public interface Interactor {

    public void RetrieveCsv(String s,OnFinishedListener listener);


    interface OnFinishedListener {
        void onError();
        void onSuccess(ArrayList<device_log> device_logs);
    }


}
