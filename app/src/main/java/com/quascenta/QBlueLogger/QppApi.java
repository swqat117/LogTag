package com.quascenta.QBlueLogger;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AKSHAY on 1/3/2017.
 */

public class QppApi {

    private static ArrayList<BluetoothGattCharacteristic> arrayNtfCharList = new ArrayList<BluetoothGattCharacteristic>();


    //To send data


    private static BluetoothGattCharacteristic writeCharacteristic;
    private static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
    private static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
    private static final int qppServerBufferSize = 20;


    // Receive data

    private static BluetoothGattCharacteristic notifyCharacteristic;
    private static byte notifyCharaIndex = 0;
    private static boolean notifyEnabled = false;
    private static final String UUIDes = "00002902-0000-1000-8000-00805f9b34fb";
    private static String TAG = QppApi.class.getSimpleName();



    private static iQppCallback qppCallBack;

    public static void setCallBack(iQppCallback callBack){
    qppCallBack = callBack;
    }


    public static void updateValueForNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || characteristic == null) {
            Log.e(TAG, "-----------Invalid null arguments");
            return;
        }
        if (!notifyEnabled) {
            Log.e(TAG, "-----------The NotifyCharacteristic is not enabled");
        }

        String strUUIDForNotifyChar = characteristic.getUuid().toString();
        final byte[] qppData = characteristic.getValue();

        if (qppData != null && qppData.length > 0) {
            qppCallBack.onQppReceiveData(bluetoothGatt, strUUIDForNotifyChar, qppData);

        }
    }


    private static void resetQppField() {
        writeCharacteristic = null;
        notifyCharacteristic = null;

        arrayNtfCharList.clear();
        notifyCharaIndex = 0;
    }


    public static boolean qppEnable(BluetoothGatt bluetoothGatt, String qppServiceUUID, String charWriteUUID ){

         resetQppField();
        if(qppServiceUUID != null)
            uuidQppService = qppServiceUUID;
        if(charWriteUUID != null)
            uuidQppCharWrite = charWriteUUID;
        if(bluetoothGatt == null || qppServiceUUID.isEmpty() || charWriteUUID.isEmpty()){
            Log.e(TAG,"invalid arguements");
            return false;
        }

        BluetoothGattService qppService  = bluetoothGatt.getService(UUID.fromString(qppServiceUUID));

        if(qppService == null){
            Log.e(TAG,"Qpp service not found");
            return false;
        }

        List<BluetoothGattCharacteristic> gattCharacteristics = qppService.getCharacteristics();
        for(int j=0;j< gattCharacteristics.size();j++) {
            BluetoothGattCharacteristic chara = gattCharacteristics.get(j);
            if (chara.getUuid().toString().equals(charWriteUUID)) {
                writeCharacteristic = chara;
            } else if (chara.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                notifyCharacteristic = chara;
                arrayNtfCharList.add(chara);
            }
        } if(!setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(0), true))
                return false;
            notifyCharaIndex++;

            return true;
        }





    public static boolean qppSendData(BluetoothGatt bluetoothGatt, byte[] qppData){
        boolean ret=false;
        if(bluetoothGatt == null){
            Log.e(TAG,"BluetoothAdapter not initialized !");
            return false;
        }

        if(qppData == null){
            Log.e(TAG,"qppData = null !");
            return false;
        }
        int length=qppData.length;
        if(length<=qppServerBufferSize)
            return writeValue(bluetoothGatt, writeCharacteristic, qppData);
        else{
            int count=0;
            int offset=0;
            while(offset<length)
            {

                if((length-offset)<qppServerBufferSize)
                    count=length-offset;
                else
                    count=qppServerBufferSize;
                byte tempArray[]=new byte[count];
                System.arraycopy(qppData,offset,tempArray,0,count);
                ret=writeValue(bluetoothGatt, writeCharacteristic, tempArray);
                if(!ret)
                    return ret;

                offset=offset+count;
            }
        }
        return ret;
    }
    private static boolean writeValue(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] bytes){
        if(gatt == null){
            Log.e(TAG,"BluetoothAdapter not initialized");
            return false;
        }

        characteristic.setValue(bytes);
        return gatt.writeCharacteristic(characteristic);
    }

    public static boolean setQppNextNotify(BluetoothGatt bluetoothGatt, boolean EnableNotifyChara){
        if(notifyCharaIndex==arrayNtfCharList.size())
        {
            notifyEnabled=true;
            return true;
        }
        return setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(notifyCharaIndex++), EnableNotifyChara);
    }

    private static boolean setCharacteristicNotification(BluetoothGatt bluetoothGatt,BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        try {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(UUIDes ));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                return (bluetoothGatt.writeDescriptor(descriptor));
            }else{
                Log.e(TAG, "descriptor is null");
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }
}


