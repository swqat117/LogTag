package com.quascenta.QBlueLogger;

/**
 * Created by AKSHAY on 1/16/2017.
 */

public class BluetoothTLV {
    public static final byte WIRED_MODE_ENABLE = (byte) 0x20;
    public static final byte WIRED_MODE_DISABLE = (byte) 0x21;
    public static final byte SEND_RAW_APDU = (byte) 0x22;

    public static final byte FW_GET_VERSION = (byte) 0x15;

    public static final byte LS_EXECUTE_SCRIPT = (byte) 0xA0;

    public static final byte LTSM_CREATE_VC = (byte) 0xB0;
    public static final byte LTSM_DELETE_VC = (byte) 0xB1;
    public static final byte LTSM_ACTIVATE_VC = (byte) 0xB2;
    public static final byte LTSM_DEACTIVATE_VC = (byte) 0xB3;
    public static final byte LTSM_ADD_AND_UPDATE_MDAC = (byte) 0xB4;
    public static final byte LTSM_READ_MIFARE_DATA = (byte) 0xB5;

    public static byte[] getTlvCommand(byte type, byte[] value) {
        int lengthLength = 0;
        int valueLength = value.length;

        if(valueLength <= 127) {
            lengthLength = 1;
        } else {
            if(valueLength > 255) {
                lengthLength = 3;
            } else {
                lengthLength = 2;
            }
        }

        byte[] dataBT = new byte[1 + lengthLength + value.length];

        dataBT[0] = type;
        System.arraycopy(value, 0, dataBT, 1 + lengthLength, value.length);

        if(valueLength <= 127) {
            dataBT[1] = (byte) valueLength;
        } else {
            if(valueLength > 255) {
                dataBT[1] = (byte) 0x82;
                dataBT[2] = (byte) (value.length >> 8 & 0xFF);
                dataBT[3] = (byte) (value.length >> 0 & 0xFF);
            } else {
                dataBT[1] = (byte) 0x81;
                dataBT[2] = (byte) valueLength;
            }
        }

        return dataBT;
    }
}
