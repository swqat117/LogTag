package com.quascenta.QBlueLogger.api;

import android.bluetooth.BluetoothGatt;

/**
 * Created by AKSHAY on 1/6/2017.
 */

public interface iQppCallback {
    void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar, byte[] qppData);
}
