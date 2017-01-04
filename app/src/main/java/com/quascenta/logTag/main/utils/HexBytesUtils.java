package com.quascenta.logTag.main.utils;

import android.util.Log;

/**
 * Created by AKSHAY on 1/3/2017.
 */



public class HexBytesUtils {
    private static final String qppHexStr = "0123456789ABCDEF";

    /**
     * Hex char convert to byte array
     *

     *
     * @param hexString
     * @return
     */
    public static byte[] hexStr2Bytes(String hexString){
		 /*int len = paramString.length()/2;
		 byte[] mbytes = new byte[len];
		 for(int i=0;i<len;i++){
			 mbytes[i] = (byte)Integer.parseInt(paramString.substring(i*2, i*2+2), 16);
		 }
		 return mbytes;*/

        if(hexString == null || hexString.isEmpty())
        {
            return null;
        }

        hexString = hexString.toUpperCase();

        int length = hexString.length() >> 1;
        char[] hexChars = hexString.toCharArray();

        int i = 0;
        Log.i("QnDbg","hexString.length() : "+hexString.length());

        do{
            int checkChar = qppHexStr.indexOf(hexChars[i]);

            if(checkChar == -1)
                return null;
            i++;
        }while (i < hexString.length());

        byte[] dataArr = new byte[length];


        for(i = 0; i < length; i++)
        {
            int strPos = i*2;

            dataArr[i] = (byte)(charToByte(hexChars[strPos]) << 4 | charToByte(hexChars[strPos+1]));
        }

        return dataArr;
    }

    private static byte charToByte(char c)
    {
        return (byte)qppHexStr.indexOf(c);
    }
}