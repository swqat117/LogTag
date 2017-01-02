package com.quascenta.petersroad.interfaces;

import com.quascenta.petersroad.model.device_log;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.Line;

/**
 * Created by AKSHAY on 11/7/2016.
 */

public interface IView {

    public void change(String s);

    public void onSuccess(ArrayList<device_log> device_logs);

    public void onFailiure();

    public void Plot( ArrayList<Line> arrayList);

}
