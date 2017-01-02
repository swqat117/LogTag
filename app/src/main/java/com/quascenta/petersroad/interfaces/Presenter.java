package com.quascenta.petersroad.interfaces;

import android.app.ProgressDialog;

import com.quascenta.petersroad.model.device_log;

import java.util.ArrayList;

/**
 * Created by AKSHAY on 11/7/2016.
 */

public interface Presenter {

    public void onDaySelected(int day, String month, int year);

    public void updatechart();

    public void updateLog();

    void OnMonthSelected(String month);

    public void CallBack(ArrayList<device_log> data);

    public void loadProgressBar(ProgressDialog dialog);
}
