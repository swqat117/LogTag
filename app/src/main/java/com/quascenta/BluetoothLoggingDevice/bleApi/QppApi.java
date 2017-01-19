/**
 * 
 * todo: broadcast/receive broadcast. 
 */

/**
 * @author Quintic Zhang Fuquan
 *
 */
package com.quascenta.BluetoothLoggingDevice.bleApi;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class QppApi {
    	private static ArrayList<BluetoothGattCharacteristic> arrayNtfCharList = new ArrayList<BluetoothGattCharacteristic>();
	
    	/// send data
	private static BluetoothGattCharacteristic writeCharacteristic; ///  write Characteristic
	private static String uuidQppService;
	private static String uuidQppCharWrite ;
	private static final int qppServerBufferSize=20;

	private static final String HexStr = "0123456789abcdefABCDEF";
	/// receive data
	private static BluetoothGattCharacteristic notifyCharacteristic;	/** notify Characteristic*/
	private static byte notifyCharaIndex = 0;
	private static boolean NotifyEnabled=true;
	private static final String UUIDDes="00002902-0000-1000-8000-00805f9b34fb";
	private static String TAG =QppApi.class.getSimpleName();

	private static iQppCallback iQppCallback;

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len/2];

		for(int i = 0; i < len; i+=2){
			data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		}

		return data;
	}

	public static void setCallback(iQppCallback mCb){
	    iQppCallback= mCb;      
	}  
    public static void  updateValueForNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic){
	if(bluetoothGatt==null || characteristic==null){
	    Log.e(TAG,"invalid arguments as either characteristic is null or bluetooth gatt is null");
	    return;
	}
	if(!NotifyEnabled){
	    Log.e(TAG,"The notifyCharacteristic not enabled");
	    return;
	}
       	String strUUIDForNotifyChar = characteristic.getUuid().toString();
	     final byte [] qppData = characteristic.getValue();
			System.out.println("UpdateValueForNotificaion ------"+new String(qppData));
	     if(qppData!=null && qppData.length>0)
		 iQppCallback.onQppReceiveData(bluetoothGatt, strUUIDForNotifyChar, qppData );	  
    	}
	private static void resetQppField() {
	    writeCharacteristic = null;
	    notifyCharacteristic = null;
		  
	    arrayNtfCharList.clear();
	    NotifyEnabled=false;
	    notifyCharaIndex = 0;
	}    
    public static boolean qppEnable(BluetoothGatt bluetoothGatt, String qppServiceUUID, String writeCharUUID){
    resetQppField();
	if(qppServiceUUID!=null)
		uuidQppService = qppServiceUUID;
	if(writeCharUUID!=null)
		uuidQppCharWrite = writeCharUUID;
	if(bluetoothGatt==null || qppServiceUUID.isEmpty() || writeCharUUID.isEmpty()){
	    Log.e(TAG,"invalid arguments in qpp enable ");
	    return false;
	}
	BluetoothGattService qppService=bluetoothGatt.getService(UUID.fromString(qppServiceUUID));
	if(qppService==null){
	    Log.e(TAG,"Qpp service not found");
	    return false;
	}
	List<BluetoothGattCharacteristic> gattCharacteristics = qppService.getCharacteristics();
	for(int j=0; j < gattCharacteristics.size(); j++)
	{
	    BluetoothGattCharacteristic chara = gattCharacteristics.get(j);
	    if(chara.getUuid().toString().equals(writeCharUUID))
		{
			Log.i(TAG,"Wr char is "+chara.getUuid().toString());
			writeCharacteristic = chara;
	    }
		else if(chara.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY){
			Log.i(TAG,"NotiChar UUID is : "+chara.getUuid().toString());
			notifyCharacteristic = chara;
			arrayNtfCharList.add(chara);
	    }
	}			   
	    
	if(!setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(0), true))
	    return false;
	notifyCharaIndex++;
	
	return true;
    }
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

	/// data sent	
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
			return writeValue(bluetoothGatt, writeCharacteristic, new byte[]{53});

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
		boolean ret = characteristic.setValue(bytes);
		if(characteristic.setValue(bytes)){
			return gatt.writeCharacteristic(characteristic);
		}else{
			return false;
		}

	}

    public static boolean setQppNextNotify(BluetoothGatt bluetoothGatt, boolean EnableNotifyChara){
	if(notifyCharaIndex==arrayNtfCharList.size())
	{
	    NotifyEnabled=true;
	    return true;
	}
	return setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(notifyCharaIndex++), EnableNotifyChara);
    }
		
    private static boolean setCharacteristicNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic, boolean enabled) {
	if (bluetoothGatt == null) {
	    Log.w(TAG, "BluetoothAdapter not initialized");
	    return false;
	}

	bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        
	try {        	
	    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(UUIDDes ));
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

