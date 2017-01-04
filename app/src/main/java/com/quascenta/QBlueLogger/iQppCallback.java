package com.quascenta.QBlueLogger;

import android.bluetooth.BluetoothGatt;

/**
 * Created by AKSHAY on 1/3/2017.
 */

public interface iQppCallback {
    void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar, byte[] qppData);
}
