package com.quascenta.QBlueLogger.api;

/**
 * Created by AKSHAY on 1/6/2017.
 */
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class QppApi  {
    private static String TAG = QppApi.class.getSimpleName();
    private static iQppCallback iQppCallback;
    private static ArrayList<BluetoothGattCharacteristic>
            arrayNtfCharList = new ArrayList<BluetoothGattCharacteristic>();
    private static BluetoothGattCharacteristic writeCharacteristic;//write Characteristic
    private static BluetoothGattCharacteristic notifyCharacteristic;//notify Characteristic
    private static byte notifyCharIndex;

    private static String uuidQppService;
    private static String uuidQppCharWrite;

    private static final String UUIDDes = "00002902-0000-1000-8000-00805f9b34fb";
    private static final String HexStr = "0123456789abcdefABCDEF";

    private static boolean NotifyEnabled = false;
    public static boolean CheckHex = false;

    public static final int qppServerBufferSize = 20;

    /**
     * Created at : 2016/8/8 15:36
     * Description: Callback
     */
    public static void setCallback(iQppCallback mCallback) {
        iQppCallback = mCallback;
    }

    public static void setCheckHexState(boolean state) {
        if (state)
            CheckHex = true;
        else
            CheckHex = false;
    }

    public static void updateValueForNotifition(BluetoothGatt bluetoothGatt,
                                                BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || characteristic == null) {
            Log.e(TAG, "--> Invalid argument!");
            return;
        }
        if (!NotifyEnabled) {
            Log.e(TAG, "--> The notifyCharacteristic not enabled");
            return;
        }
        String strUUIDFornotifyChar = characteristic.getUuid().toString();
        final byte[] qppData = characteristic.getValue();
        if (qppData != null && qppData.length > 0) {
            iQppCallback.onQppReceiveData(bluetoothGatt, strUUIDFornotifyChar, qppData);
        }
    }

    private static void resetQppField() {
        arrayNtfCharList.clear();
        writeCharacteristic = null;
        notifyCharacteristic = null;
        notifyCharIndex = 0;
    }

    public static boolean qppEnable(BluetoothGatt bluetoothGatt, String qppServiceUUID, String wtriteCharUUID) {

        if (qppServiceUUID != null) {
            uuidQppService = qppServiceUUID;
        }

        if (wtriteCharUUID != null) {
            uuidQppCharWrite = wtriteCharUUID;
        }

        if (bluetoothGatt == null || qppServiceUUID.isEmpty() || wtriteCharUUID.isEmpty()) {
            Log.e(TAG, "--> invalid arguments");
            return false;
        }

        BluetoothGattService qppService = bluetoothGatt.getService(UUID.fromString(qppServiceUUID));
        if (qppService == null) {
            Log.e(TAG, "--> qppService not found!");
            return false;
        }

        List<BluetoothGattCharacteristic> gattCharacteristics = qppService.getCharacteristics();
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : gattCharacteristics) {
            BluetoothGattCharacteristic chara = bluetoothGattCharacteristic;
            if (chara.getUuid().toString().equals(wtriteCharUUID)) {
                writeCharacteristic = chara;
            } else if (chara.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                notifyCharacteristic = chara;
                arrayNtfCharList.add(chara);
            }
        }

        if (arrayNtfCharList != null && arrayNtfCharList.size() > 0) {
            if (!setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(0), true)) {
                return false;
            }
        }
        notifyCharIndex++;
        return true;
    }

    /**
     * @author LucianQu
     * created at 2016/8/5 17:19
     */
    private static boolean setCharacteristicNotification(BluetoothGatt bluetoothGatt
            , BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "--> BluetoothAdapter not initialized!");
            return false;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        try {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(UUIDDes));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                return (bluetoothGatt.writeDescriptor(descriptor));
            } else {
                Log.e(TAG, "--> descriptor is null!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Created at : 2016/8/6 18:46
     * Description: 删除空格
     */
    private static byte[] deleteSpace(byte[] bytes) {
        byte[] mbytes = null;
        if (bytes == null) {
            return null;
        }
        ArrayList<Byte> arr = new ArrayList<Byte>();
        for (Byte mbyte : bytes
                ) {
            if (mbyte != 32)
                arr.add(mbyte);
        }
        if (arr.isEmpty()) {
            return null;
        }

        try {
            Iterator<Byte> it = arr.iterator();
            int i = 0;
            if (null != it) {
                mbytes = new byte[arr.size()];
            }
            while (it.hasNext()) {
                mbytes[i] = (Byte) it.next();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mbytes;
    }

    /**
     * Created at : 2016/8/6 18:43
     * Description: 检查输入字节数组
     */
    public static boolean chenkInputString(byte[] bytes) {
        int i = 0;
        byte[] mbytes = null;
        mbytes = deleteSpace(bytes);
        // mbytes = bytes.toString().split(" ").toString().getBytes();
        if (mbytes == null)
            return false;
        Log.i("Qn Dbg", "--> mBytes[].length : " + mbytes.length);

        do {
            int checkChar = HexStr.indexOf(mbytes[i]);
            if (checkChar == -1)
                return false;
            i++;
        } while (i < mbytes.length);

        return true;
    }

    /**
     * Created at : 2016/8/6 19:04
     * Description:
     */
    public static boolean qppSendData(BluetoothGatt bluetoothGatt, byte[] qppData) {
        boolean ret = false;
        if (bluetoothGatt == null) {
            Log.e(TAG, "--> BluetoothAdapter not initialized ");
            return false;
        }
        int len = qppData.length;
        if (len <= qppServerBufferSize) {
            return writeValue(bluetoothGatt, writeCharacteristic, qppData);
        } else {
            int count = 0;
            int offset = 0;
            while (offset < len) {
                if ((len - offset) < qppServerBufferSize) {
                    count = len - offset;
                } else {
                    count = qppServerBufferSize;
                }
                byte temArray[] = new byte[count];
                System.arraycopy(qppData, offset, temArray, 0, count);
                ret = writeValue(bluetoothGatt, writeCharacteristic, temArray);
                if (!ret)
                    return ret;
                offset = offset + count;
            }
        }
        return true;
    }

    public static boolean setQppNextNotify(BluetoothGatt bluetoothGatt, boolean EnableNotifyChara) {
        if (notifyCharIndex == arrayNtfCharList.size()) {
            NotifyEnabled = true;
            return true;
        }
        return setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(notifyCharIndex++),
                EnableNotifyChara);
    }

    private static boolean writeValue(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic
            writeCharacteristic, byte[] qppData) {
        if (bluetoothGatt == null) {
            Log.e(TAG, "--> BluetoothAdapter not initialized");
            return false;
        }
        if (qppData == null) {
            Log.e(TAG, "--> qppData is null.");
            return false;
        }
        qppData = deleteSpace(qppData);
        if (CheckHex) {
            qppData = textAscII2Hex(qppData);
        }
        writeCharacteristic.setValue(qppData);
        return bluetoothGatt.writeCharacteristic(writeCharacteristic);
    }

    private static byte[] textAscII2Hex(byte[] qppData) {
        byte[] mBytes = deleteSpace(qppData);
        int len = mBytes.length >> 1;
        if (qppData.length == 0) {
            Log.e(TAG, "--> qppData.length = 0.");
            return null;
        }
        chenkInputString(qppData);
        byte[] dataArr = new byte[len];
        for (int i = 0; i < len; i++) {
            int strPos = i * 2;
            dataArr[i] = (byte) (charToByte(mBytes[strPos]) << 4 | charToByte(mBytes[strPos + 1]));

        }
        return dataArr;
    }

    private static int charToByte(byte b) {
        if (b >= 48 && b <= 57) {
            b = (byte) (b - 48);
        } else if (b >= 65 && b <= 90) {
            b = (byte) (b - 55);
        } else if (b >= 97 && b <= 122) {
            b = (byte) (b - 87);
        }
        return b;
    }

}


