package com.quascenta.BluetoothLoggingDevice;

/**
 *
 */
public class Command {

    public static byte qppDataSend[] = {'T','R',0x16,0x06,'R',0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00}; //Array of data to send

    public static byte[] ComSyncColor    = {'C', 'O', 'L', 'R'};
    public static int    ComSyncColorLen = 8;
    public static byte[] ComSyncMode     = {'M', 'O', 'D', 'E'};
    public static int    ComSyncModeLen  = 6;
    public static byte[] ComSyncLevel    = {'L', 'E', 'V', 'L'};
    public static int    ComSyncLevelLen = 6;


    public static byte[] ComSyncLight    = {'A', 'L', 'P', 'A'};
    public static int    ComSyncLightLen = 6;


    public static byte[] ComSyncCalling    = {'C', 'A', 'L', 'L'};
    public static int    ComSyncCallingLen = 15;


    public static final int MODE_GRADUAL_START      = 0;
    public static final int MODE_RED_GRADUAL        = 0;
    public static final int MODE_GREEN_GRADUAL      = 1;
    public static final int MODE_BLUE_GRADUAL       = 2;
    public static final int MODE_YELLOW_GRADUAL     = 3;
    public static final int MODE_CYAN_GRADUAL       = 4;
    public static final int MODE_PURPLE_GRADUAL     = 5;
    public static final int MODE_WHITE_GRADUAL      = 6;
    public static final int MODE_RED_GREEN_GRADUAL  = 7;
    public static final int MODE_RED_BLIE_GRADUAL   = 8;
    public static final int MODE_GREEN_BLUEG_RADUAL = 9;
    public static final int MODE_COLORFUL_GRADUAL   = 10;
    public static final int MODE_COLORFUL_SWITCH    = 11;
    //频闪
    public static final int MODE_FLASH_START        = 12;
    public static final int MODE_COLORFUL_FLASH     = 12;
    public static final int MODE_RED_FLASH          = 13;
    public static final int MODE_GREEN_FLASH        = 14;
    public static final int MODE_BLUE_FLASH         = 15;
    public static final int MODE_YELLOW_FLASH       = 16;
    public static final int MODE_CYAN_FLASH         = 17;
    public static final int MODE_PURPLE_FLASH       = 18;
    public static final int MODE_WHITE_FLASH        = 19;
}
