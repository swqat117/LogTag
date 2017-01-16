package com.quascenta.QBlueLogger.api;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by AKSHAY on 1/6/2017.
 */

public class BluetoothClient {

        protected static final String TAG = "--> Bluetooth LE Client";

        // Timeout between following BLE Messages
        public static final int TIMER_BLE = 50;

        /// qpp start
        protected static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
        protected static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";

        private BluetoothAdapter mBluetoothAdapter = null;
        public BluetoothGatt mBluetoothGatt = null;

        public OnBluetoothWriteListener mDelegateWrite;
        public OnBluetoothReadListener mDelegateRead;
        public OnBluetoothConnectListener mDelegateConnected;
        public OnBluetoothConnectionPendingListener mDelegateConnectionPending;

        /** scan all Service ? */
        private boolean isInitialize = false;
        private boolean qppSendDataState = false;

        private boolean waitingForConnResp = false;

        private Context mContext;
        private Activity mAct;

        private SendThread sendDataThread = null;;

        // Countdown used to wait for the second connection attempt
        private static CountDownTimer counterConn;

        public BluetoothClient(BluetoothAdapter adapter, Context ctx, Activity act) {
            this.mBluetoothAdapter = adapter;
            this.mContext = ctx;
            this.mAct = act;

            // Set the callback to receive data
            receiveDataCallback();

        }

        public boolean isConnected() {
            return isInitialize;
        }

        public void receiveDataCallback() {
            QppApi.setCallback(new iQppCallback() {
                @Override
                public void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar, byte[] qppData) {
                    mDelegateRead.onRead(qppData);
                }
            });
        }

        public void sendData(byte[] data) {
            if(isInitialize) {
                sendDataThread = new SendThread(data, true);
                sendDataThread.start();
            } else {
                mDelegateRead.onRead(null);
            }
        }

        private class SendThread extends Thread {
            byte[] data;

            public SendThread(byte[] data, boolean send) {
                this.data = data;
            }

            public void run() {
                if(data == null){
                    return;
                }

                int length = data.length;
                int count = 0;
                int offset = 0;

                while (offset < length) {
                    if ((length - offset) < QppApi.qppServerBufferSize)
                        count = length - offset;
                    else
                        count = QppApi.qppServerBufferSize;

                    // Give some time to the MCU
                    try {
                        Thread.sleep(TIMER_BLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    byte tempArray[] = new byte[count];
                    System.arraycopy(data, offset, tempArray, 0, count);
                    QppApi.qppSendData(mBluetoothGatt, tempArray);
                    offset = offset + count;
                }

                // We have transmitted the last BLE Message
                mDelegateWrite.onWrite();
            }
        }

        private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status,	int newState) {
                Log.w(TAG, "onConnectionStateChange. Status: " + status + " State: " + newState);

                if(status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        if(isInitialize == true) {
                            Log.d(TAG, "Connection received while I was already connected");
                            gatt.close();
                            return;
                        }

                        // Set the isInitialize here to avoid refreshing info layout before the Service Discovery
                        isInitialize = true;

                        // Cancel the counter for the second connection attempt
                        if(counterConn != null)
                            counterConn.cancel();

                        // Look for QPP Service
                        mBluetoothGatt.discoverServices();

                        mDelegateConnected.onConnect(true);
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        isInitialize = false;

                        if (qppSendDataState) {
                            qppSendDataState = false;
                        }

                        mDelegateConnected.onConnect(false);

                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.close();
                            mBluetoothGatt = null;
                        }
                    }

                    // Let the user launch a new connection request
                    waitingForConnResp = false;
                } else {
				/*
				 * Error 133 (GATT_ERROR) does not appear in the API
				 *
				 * This code solves the issue that occurs when the Conn Device receives EACI_MSG_EVT_CONN after a while
				 * and QPPS_CFG_INDNTF_IND is not received and therefore the connection is not well established.
				 * We can force the connections sending back this request
				 *
				 * If the connection is not established in 4 secs we can consider the connection as not established
				 *
				 */
                    if(status == 133) {
                        mBluetoothGatt.connect();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                counterConn = new CountDownTimer(4000, 1000) {
                                    @Override
                                    public void onFinish() {
                                        // There was an error, so we are not connected
                                        mDelegateConnected.onConnect(false);

                                        if (mBluetoothGatt != null) {
                                            mBluetoothGatt.close();
                                            mBluetoothGatt = null;
                                        }

                                        // Let the user launch a new connection request
                                        waitingForConnResp = false;
                                    }

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }
                                };

                                counterConn.start();
                            }
                        });
                    } else {
                        // There was an error, so we are not connected
                        mDelegateConnected.onConnect(false);

                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.close();
                            mBluetoothGatt = null;
                        }

                        // Let the user launch a new connection request
                        waitingForConnResp = false;
                    }
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.w(TAG, "onServicesDiscovered status: " + status);
                if (QppApi.qppEnable(mBluetoothGatt, uuidQppService, uuidQppCharWrite)) {
                    isInitialize = true;
                } else {
                    isInitialize = false;

                    // If this is not a QPP Profile we better close the connection
                    close();
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt,	BluetoothGattCharacteristic characteristic) {
                Log.w(TAG, "onCharacteristicChanged");
                QppApi.updateValueForNotifition(gatt, characteristic);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                // super.onDescriptorWrite(gatt, descriptor, status);
                Log.w(TAG, "onDescriptorWrite");
                QppApi.setQppNextNotify(gatt, true);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                // super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                } else {
                    Log.e(TAG, "Send failed!!!!");
                }
            }
        };

        public boolean connect(final String address) {
            if (mBluetoothAdapter == null || address == null) {
                Log.w("Qn Dbg", "BluetoothAdapter not initialized or unspecified address.");
                return false;
            }

            final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                Log.w(TAG, "Device not found. Unable to connect.");
                return false;
            }

            // setting the autoConnect parameter to false.
            if(waitingForConnResp == false) {
                mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);

                Log.d(TAG, "Trying to create a new connection. Gatt: " + mBluetoothGatt);

                // Wait for the response before letting the user launch a new request
                waitingForConnResp = true;
            } else {
                Log.d(TAG, "Pending connection detected");

                mDelegateConnectionPending.onConnectionPending();
            }

            return true;
        }

        public void disconnect() {
            if (mBluetoothAdapter == null) {
                Log.w("Qn Dbg", "BluetoothAdapter not initialized");
                return;
            }

            if (mBluetoothGatt == null) {
                Log.w("Qn Dbg", "BluetoothGatt not initialized");
                return;
            }

            Log.d(TAG, "Disconnect");
            mBluetoothGatt.disconnect();

            // We are no longer connected
            isInitialize = false;
        }

        public void close() {
            Log.d(TAG, "Close Connection");

            // Disconnect and then close the connection
            disconnect();

//		if (mBluetoothGatt != null) {
//			mBluetoothGatt.close();
//			mBluetoothGatt = null;
//		}

            // We are no longer connected
            isInitialize = false;
        }

        public void closeBluetoothClient() {
            if(mBluetoothGatt != null)
                mBluetoothGatt.close();

            mBluetoothGatt = null;
        }

        public void setWaitingForConnResp (boolean wait) {
            waitingForConnResp = wait;
        }

        // Interface callback for Bluetooth data read
        public interface OnBluetoothReadListener {
            void onRead(byte[] status);
        }

        // Interface callback for Bluetooth data read
        public interface OnBluetoothWriteListener {
            void onWrite();
        }

        // Interface callback for Bluetooth data read
        public interface OnBluetoothConnectListener {
            void onConnect(boolean connected);
        }

        // Interface callback for Bluetooth data read
        public interface OnBluetoothConnectionPendingListener {
            void onConnectionPending();
        }

}
