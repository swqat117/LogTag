package com.quascenta.BluetoothLoggingDevice.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;


import com.quascenta.BluetoothLoggingDevice.BluetoothLeService;
import com.quascenta.petersroad.broadway.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;


@SuppressLint("NewApi")
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class
            .getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private EditText mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    public static BluetoothGatt mBluetoothGatt;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private boolean mConnected = false;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
           mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read
    // or notification operations.


private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
      @Override
        public void onReceive(Context context, Intent intent) {
           final String action = intent.getAction();
          System.out.println("ACTION "+action);
           if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
              mConnected = true;updateConnectionState(R.string.connected);
              invalidateOptionsMenu();
          } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
               mConnected = false;
               updateConnectionState(R.string.disconnected);
               invalidateOptionsMenu();
               clearUI();
           } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices(mDeviceAddress));//
          } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
           }
      }
   };

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

//        java.lang.SecurityException: Need ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get scan results
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (EditText) findViewById(R.id.data_value);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        boolean bll = bindService(gattServiceIntent, mServiceConnection,
                BIND_AUTO_CREATE);
        if (bll) {
            System.out.println("---------------");
        } else {
            System.out.println("===============");
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             gattServiceIntent.putExtra("value",mDataField.getText().toString());
             gattServiceIntent.putExtra("QPPSENDDATA_STATE",true);
               startService(gattServiceIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
              mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
              mBluetoothLeService.disconnect(mDeviceAddress);//
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }
    StringBuffer sb = new StringBuffer();
    private void displayData(String data) {
        sb.append(data);
        Log.d(TAG, "displayData: "+sb.toString());
    }

    // Demonstrates how to iterate through the supported GATT
    // Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the
    // ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            Log.d(TAG, "displayGattServices: "+uuid);
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.contains("fff4")) {
                    Log.e("console", "2gatt Characteristic: " + uuid);
                    mBluetoothLeService.setCharacteristicNotification(uuid, gattCharacteristic,true);
                    mBluetoothLeService.readCharacteristic(uuid, gattCharacteristic);//

                }
            }
        }
    }

    /**
     *
     * @return
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.EXTRA_DATA);
       return intentFilter;
   }

    /**
     *
     *
     * @param str
     * @return
     */
    public static String print10(String str) {

        StringBuffer buff = new StringBuffer();
        String array[] = str.split(" ");
        for (int i = 0; i < array.length; i++) {
            int num = Integer.parseInt(array[i], 16);
            buff.append(String.valueOf((char) num));
        }
        return buff.toString();
    }

    /**
     *
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte[] b) {

        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }


    /**
     *
     * @param data
     * @return
     */
    public static String BodyCHOLRead(String data) {

        String[] datas = data.split(print10("0A"));
        for (int i = 0 ; i < datas.length; i ++) {
            Log.d(TAG, String.format("split[%s]:%s",i, datas[i]));
        }
        String unit = "";
        String data7 = datas[7].split("\"")[1].split(":")[1].trim();
        if(data7.contains("mmol/L")){
            unit = "mmol/L";
        }

        StringBuilder sbr = new StringBuilder();
        for (int i = 7,j = 0; i < 11 ; i ++,j ++) {
            String values = datas[i].split("\"")[1].split(":")[1].trim();//207 mg/dL
            String[] results = values.split(" +");
            System.out.println("~~~~~" + values + ":" + results.length);
            String value = "----";

            if(results.length == 3){
                sbr.append(results[0]);
                value = results[1];
            } else if(results.length == 2){
                value = results[0];
            }

            if("----".equals(value)){
                sbr.append(value).append(",");
            } else if(i != 11 && "mg/dl".equals(unit)){
                sbr.append(unitConversion(value,j)).append(",");
            } else if(i != 11 && "mmol/L".equals(unit)){
                sbr.append(value).append(",");
            } else if(i != 11 && "g/L".equals(unit)){
                sbr.append(unitConversion(String.valueOf(Double.parseDouble(value) * 100),j)).append(",");
            } else {
                sbr.append(value).append(",");
            }
        }
        Log.d(TAG, ":" +  sbr);
        return sbr.substring(0, sbr.length() - 1);
    }

    private static String unitConversion(String input, int type){
        double value = Double.parseDouble(input);
        NumberFormat df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(2);


        if(type == 0){
            return df.format(value / 38.7);
        }
        if(type == 1){
            return df.format(value / 88.6);
        }
        if(type == 2){
            return df.format(value / 38.7);
        }
        if(type == 3){
            return df.format(value / 38.7);
        }
        return null;
    }
}
