package com.quascenta.QBlueLogger.activities;

import com.quascenta.QBlueLogger.api.QppApi;
import com.quascenta.QBlueLogger.api.iQppCallback;
import com.quascenta.petersroad.broadway.R;


import android.app.Activity;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothGatt;
        import android.bluetooth.BluetoothGattCallback;
        import android.bluetooth.BluetoothGattCharacteristic;
        import android.bluetooth.BluetoothGattDescriptor;
        import android.bluetooth.BluetoothManager;
        import android.bluetooth.BluetoothProfile;
        import android.content.Context;
        import android.os.Bundle;
        import android.os.Handler;
        import android.text.InputFilter;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

/**
 * Created by QLS on 2016/7/28.
 */
public class QppActivity extends Activity{

    protected static final String TAG = QppActivity.class.getSimpleName();
    //Obtain the deviceName and deviceAddress from blueWelcomeActivity
    public  static final String EXTRAS_DEVICE_NAME = "deviceName";
    public  static final String EXTRAS_DEVICE_ADDRESS = "deviceAddress";

    protected static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
    protected static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
    private String deviceName;
    private String deviceAddress;

    private BluetoothManager mBluetoothManager = null;
    private static BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothGatt mBluetoothGatt = null;
    private boolean mConnected = false;
    private boolean isInitialize = false;

    private static final int MAX_DATA_SIZE = 40;

    private TextView textDeviceName;
    private TextView textDeviceAddres;
    private TextView textConnectionStatus;

    //Rx:
    private TextView textQppNotify;
    private TextView textQppDataRate;

    //Tx:
    private EditText editSend;
    private Button btnQppTextSend;
    //Repeat start
    private CheckBox checkboxRepeat;
    private CheckBox checkTransFormat;

    private TextView labelRepeatCounter;
    private TextView labelTransFormat;
    private TextView textRepeatCounter;

    private long qppRepeatCounter = 0;

    private boolean dataRecvFlag = false;
    private long qppSumDataReceived = 0;
    private long qppRecevDataTime = 0;

    private boolean qppSendDataState = false;


    private boolean initialize() {
        //For API level 18  and above ,get a reference to BluetoothAdapter through BluetoothManager
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if (mBluetoothManager == null) {
                Log.e(TAG, "--> Unable to initialize BluetoothManager.");
                return false;
            }
        }


        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "--> Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }
  /*
    private Handler mHandler = new Handler() {
          public void handleMessage(Message msg) {
              switch (msg.what) {
                   case 1:

                      break;
                 case 2:
                       break;
               }
           };
     }*/

    private Handler handlerSend = new Handler();
    final Runnable runnableSend = new Runnable() {
        private void QppSendNextData() {
            byte[] qppDataSend1 = null;
            try {
                qppDataSend1 = editSend.getText().toString().getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!QppApi.chenkInputString(qppDataSend1)) {
                Log.e(TAG, "--> qppDataSend1 = input string is illegal!");
                return;
            }

            if (qppDataSend1 == null) {
                Log.e(TAG, "--> qppDataSend1 = null!");
                return;
            }

            if (!QppApi.qppSendData(mBluetoothGatt, qppDataSend1)) {
                Log.e(TAG, "--> Send data failed");
            }

            qppRepeatCounter++;
            setRepeatCounter(" " + qppRepeatCounter);

        }
        public void run() {
            QppSendNextData();
        }
    };

    /**
     *
     *@author LucianQu
     *created at 2016/8/5 18:14
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w("Qn Dbg", "--> BluetoothAdapter not initialized or unspecified address" );
            return false;
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "--> Device not found , Unable to connect !");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "--> Trying to create a new connection. Gatt :" + mBluetoothGatt);
        return true;
    }
    /**
     *Created at : 2016/8/6 15:52
     *Description: disconnect ble service
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w("Qn Dbg", "--> BluetoothAdapter not initialized!");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     *discover service
     *@author LucianQu
     *created at 2016/8/5 17:52
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "--> onConnectionStateChange : " + status + " newState : " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnected = true;
                Log.i(TAG, "--> Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery :" + mBluetoothGatt.discoverServices());

            }else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "--> Disconnected from GATT server.");
                clearHandler(handlerQppDataRate, runnableQppDataRate);
                clearHandler(handlerQppDataRateClear, runnableQppDataRateClear);
                mConnected = false;
                dataRecvFlag = false;
                if (qppSendDataState) {
                    setBtnSendState("Send");
                    qppSendDataState = false;
                }

            }
            invalidateOptionsMenu();
        }
        /**
         *Desc:
         *  onConnectionStateChange OnServiceDiscovered,
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (QppApi.qppEnable(mBluetoothGatt, uuidQppService, uuidQppCharWrite)) {
                isInitialize = true;
                setConnectState(R.string.qpp_support);
            }else {
                isInitialize = false;
                setConnectState(R.string.qpp_not_support);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            QppApi.updateValueForNotifition(gatt, characteristic);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.w(TAG, "onDescriptorWrite");
            QppApi.setQppNextNotify(gatt, true);
        }

        //Callback indicating the result of a characteristic write operation
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS && qppSendDataState) {
                QppApi.qppSendData(gatt,btnQppTextSend.getText().toString().getBytes());
                if (handlerSend != null && runnableSend != null) {
                    QppApi.qppSendData(gatt,btnQppTextSend.getText().toString().getBytes());
                    Log.i(TAG, "--> A GATT Characteristic Continuous Write operation completed successfully");
                }
            }else if (status == BluetoothGatt.GATT_SUCCESS) {
                QppApi.qppSendData(gatt,btnQppTextSend.getText().toString().getBytes());
                Log.i(TAG, "--> A GATT Characteristic Write operation completed successfully");
            }else if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "--> A GATT Characteristic Write operation completed is failure");
            }
        }
    };





    private void setConnectState(final int stat) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textConnectionStatus.setText(stat);
            }
        });
    }

    private void setQppNotify(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textQppNotify.setText(str);//更新UI界面显示
            }
        });
    }

    private void setBtnSendState(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnQppTextSend.setText(str);
            }
        });
    }

    private void setRepeatCounter(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textRepeatCounter.setText(str);
            }
        });
    }

    /**
     * @createAuthor: LucianQu
     * @createTime: 2016/8/9 18:17
     *
     * @desc:   remove input handler's callback,set handler = null;
     * @param:  handler, runnable
     * @return: null
     */
    private void clearHandler(Handler handler, Runnable runnable) {
        if (handler != null) {

            handler.removeCallbacks(runnable);
            handler = null;
          /*
          * thread.interrupt();// 中断线程
          * thread = null;// 将线程状态置为空,如果不置为空的话就会不断发送中断命令
          *
          * */
        }
    }

    /**
     *Desc:
     *
     */
    public void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }


    final Handler handlerQppDataRate = new Handler();
    final Runnable runnableQppDataRate = new Runnable() {
        @Override
        public void run() {
            qppRecevDataTime++;
            textQppDataRate.setText(" " + qppSumDataReceived / qppRecevDataTime + " Bps");
            dataRecvFlag = false;
        }
    };

    final Handler handlerQppDataRateClear = new Handler();
    final Runnable runnableQppDataRateClear = new Runnable() {
        @Override
        public void run() {
            textQppDataRate.setText("");
        }
    };

    ///////////////////////////
    /*
    * create main view
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpp);
        //Add back button at the upper left corner

        textDeviceName = (TextView)findViewById(R.id.text_device_name);
        textDeviceAddres = (TextView)findViewById(R.id.text_device_address);
        textConnectionStatus = (TextView)findViewById(R.id.text_connection_state);

        textQppNotify = (TextView) findViewById(R.id.text_qpp_notify);
        textQppDataRate = (TextView) findViewById(R.id.text_qpp_data_rate);

        editSend = (EditText)findViewById(R.id.edit_send);
        editSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DATA_SIZE)});

        checkboxRepeat = (CheckBox) findViewById(R.id.cb_repeat);
        labelRepeatCounter = (TextView) findViewById(R.id.label_repeat_counter);
        textRepeatCounter = (TextView) findViewById(R.id.text_repeat_counter);
        btnQppTextSend = (Button) findViewById(R.id.btn_qpp_text_send);

        checkTransFormat = (CheckBox) findViewById(R.id.cb_trans_format);

        deviceName = getIntent().getExtras().getString(EXTRAS_DEVICE_NAME);
        deviceAddress = getIntent().getExtras().getString(EXTRAS_DEVICE_ADDRESS);

        textDeviceName.setText(deviceName);
        textDeviceAddres.setText(deviceAddress);

        if (!initialize()) {
            Log.e(TAG,"--> unable to initialize Bluetooth!");
            finish();
        }

        QppApi.setCallback(new iQppCallback() {



            @Override
            public void onQppReceiveData(BluetoothGatt mBluetoothGatt, String qppUUIDForNotifyChar, byte[] qppData) {
                if (!dataRecvFlag) {

                    handlerQppDataRate.postDelayed(runnableQppDataRate, 1000);
                    handlerQppDataRateClear.postDelayed(runnableQppDataRateClear, 5000);
                    dataRecvFlag = true;
                        }

                setQppNotify("");
                qppSumDataReceived = qppSumDataReceived + qppData.length;


                    setQppNotify(new String(qppData));    }


        });


        btnQppTextSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mConnected || !isInitialize) {
                    Toast.makeText(QppActivity.this, "Please connect device first and ensure your" +
                            "device support BLE service", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkboxRepeat.isChecked()) {
                    if (!qppSendDataState) {
                        qppRepeatCounter = 0;
                        qppSendDataState = true;
                        btnQppTextSend.setText("Stop");
                        handlerSend.post(runnableSend);
                    }else {
                        qppSendDataState = false;
                        btnQppTextSend.setText("Send");
                    }
                }else {
                    if (qppSendDataState) {
                        qppSendDataState = false;
                        btnQppTextSend.setText("Send");
                    }else {
                        handlerSend.post(runnableSend);
                    }
                }
            }
        });

        checkboxRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    labelRepeatCounter.setVisibility(View.VISIBLE);
                    textRepeatCounter.setVisibility(View.VISIBLE);
                }else {
                    labelRepeatCounter.setVisibility(View.INVISIBLE);
                    textRepeatCounter.setVisibility(View.INVISIBLE);
                }
            }
        });


        checkTransFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    QppApi.setCheckHexState(true);
                }else {
                    QppApi.setCheckHexState(false);
                }
            }
        });




    }
    ///////////////////////////////////////////
    /*
    * create actionBar
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qppbluetooth_qpp_menu,menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        }else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }
    /*
    * actionBar optionsItemSelected
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                connect(deviceAddress);
                return true;
            case R.id.menu_disconnect:
                return true;
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     *onResume discover service
     *@author LucianQu
     *created at 2016/8/5 18:09
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(!mConnected) {
            textConnectionStatus.setText(R.string.qpp_not_support);
            invalidateOptionsMenu();
            connect(deviceAddress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearHandler(handlerQppDataRate, runnableQppDataRate);
        clearHandler(handlerQppDataRateClear, runnableQppDataRateClear);
        close();
    }
}