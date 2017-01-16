package com.quascenta.QBlueLogger.QppBle;

import android.bluetooth.BluetoothGatt;

/**
 * Created by AKSHAY on 1/16/2017.
 */

public interface iQppCallback {
    void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar, byte[] qppData);
}
