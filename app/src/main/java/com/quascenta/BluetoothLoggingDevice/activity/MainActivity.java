package com.quascenta.BluetoothLoggingDevice.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.quascenta.BluetoothLoggingDevice.BleConfig;
import com.quascenta.BluetoothLoggingDevice.BleLisenter;
import com.quascenta.BluetoothLoggingDevice.BleVO.BleDevice;
import com.quascenta.BluetoothLoggingDevice.BluetoothLeService;
import com.quascenta.BluetoothLoggingDevice.Command;
import com.quascenta.BluetoothLoggingDevice.LeDeviceListAdapter;
import com.quascenta.BluetoothLoggingDevice.bleApi.QppApi;
import com.quascenta.petersroad.broadway.R;

import java.util.Arrays;
import java.util.List;

import static com.quascenta.BluetoothLoggingDevice.activity.DeviceControlActivity.mBluetoothGatt;


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {

    private String TAG = MainActivity.class.getSimpleName();
    protected static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
    protected static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView mListView;
    private TextView mConnectedNum;
    private Button send;
    private BluetoothLeService mBluetoothLeService;
    private String currentAddress;

    private static final int REQUEST_ENABLE_BT = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mBluetoothLeService.setOnBleLisenter(new BleLisenter() {
                        @Override
                        public void onStart() {
                            super.onStart();

                        }

                        @Override
                        public void onStop() {
                            super.onStop();

                        }

                        @Override
                        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                            Log.e(TAG, "onLeScan");
//
//                            if(!BleConfig.matchProduct(scanRecord)){
//                                return;
//                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BleDevice bleDevice = new BleDevice(device);
                                    mLeDeviceListAdapter.addDevice(bleDevice);
                                    mLeDeviceListAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onConnectionChanged(final BluetoothGatt gatt, final BleDevice device) {
                            Log.e(TAG, "onConnectionChanged");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setConnectedNum();
                                    for (int i = 0; i < mLeDeviceListAdapter.getCount(); i++) {
                                        if (device.getBleAddress().equals(mLeDeviceListAdapter.getDevice(i).getBleAddress())) {
                                            if (device.isConnected()) {
                                                mLeDeviceListAdapter.getDevice(i).setConnected(true);
                                                Toast.makeText(MainActivity.this, R.string.line_success, Toast.LENGTH_SHORT).show();

                                            } else {
                                                mLeDeviceListAdapter.getDevice(i).setConnected(false);
                                                Toast.makeText(MainActivity.this, R.string.line_disconnect, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    mLeDeviceListAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onServicesDiscovered(BluetoothGatt gatt) {
                            super.onServicesDiscovered(gatt);

                    if (QppApi.qppEnable(mBluetoothGatt, uuidQppService, uuidQppCharWrite)) {}
                            //notify
                            displayGattServices(gatt.getDevice().getAddress(), mBluetoothLeService.getSupportedGattServices(gatt.getDevice().getAddress()));
                        }

                        @Override
                        public void onChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                            Log.e(TAG, "data===" + Arrays.toString(characteristic.getValue()));

                            QppApi.updateValueForNotification(gatt, characteristic);
                        }

                        @Override
                        public void onWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

                        }

                        @Override
                        public void onRead(BluetoothDevice device) {
                            super.onRead(device);

                            Log.e(TAG, "onRead");
                        }

                        @Override
                        public void onDescriptorWriter(BluetoothGatt gatt) {
                            super.onDescriptorWriter(gatt);

                         QppApi.setQppNextNotify(gatt, true);
                        }
                    });
                    //开始扫描  如果想更改扫描时间   则修改BleConfig中的SCAN_PERIOD = 10000;//默认扫描时间
                    mBluetoothLeService.scanLeDevice(true);
                    break;
            }
        }
    };

    public boolean changeLevelInner(String address, int color) {
//        byte[] data = new byte[Command.ComSyncColorLen];
//        System.arraycopy(Command.ComSyncColor, 0, data, 0, Command.ComSyncColor.length);
//        data[4] = (byte) (color & 0xff);
//        data[5] = (byte) ((color >> 8) & 0xff);
//        data[6] = (byte) ((color >> 16) & 0xff);
//        data[7] = (byte) ((color >> 24) & 0xff);
//        boolean result = mBluetoothLeService.wirteCharacteristic(address, mWriteCharacteristic, getWriteData(data));
        boolean result = mBluetoothLeService.wirteCharacteristic(address, mWriteCharacteristic, sendData(1));
        Log.e(TAG, "result==" + result);
        return result;
    }

    public byte[] getWriteData(byte[] data) {
//        byte[] bytes = new byte[data.length + 1];
        byte[] bytes = new byte[]{70, 127, -97, -80, -54, -117, 105, -76, -57};
        return bytes;
    }

    private BluetoothGattCharacteristic mWriteCharacteristic;//ble发送对象

    private void displayGattServices(String address, List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        currentAddress = address;
        String uuid = null;
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            Log.d(TAG, "displayGattServices: " + uuid);
            if(uuid.equals(BleConfig.UUID_SERVICE_TEXT)){
                Log.d(TAG, "service_uuid: " + uuid);
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    Log.e("all_characteristic", "Characteristic: " + uuid);
                    if (uuid.equals(BleConfig.UUID_NOTIFY_TEXT)) {
                        Log.e("console", "2gatt Characteristic: " + uuid);
                        mBluetoothLeService.setCharacteristicNotification(address,gattCharacteristic, true);//
//                        mBluetoothLeService.readCharacteristic(address,gattCharacteristic);//暂时注释
                    }else if(uuid.equals(BleConfig.UUID_CHARACTERISTIC_TEXT)){
                        mWriteCharacteristic = gattCharacteristic;
                        Log.e("write_characteristic", "Characteristic: " + uuid);
                    }

                }
            }
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            Log.e(TAG, "服务连接成功");
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            if (mBluetoothLeService != null) mHandler.sendEmptyMessage(1);
            // Automatically connects to the device upon successful start-up
            // initialization.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        initBleDevice();//初始化蓝牙
        initView();
    }

    private void bindService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        boolean bll = bindService(gattServiceIntent, mServiceConnection,
                BIND_AUTO_CREATE);
        if (bll) {
            System.out.println("---------------");
        } else {
            System.out.println("===============");
        }
    }

    //播放音乐
    public byte[] sendData(int play) {
        byte[] data = new byte[Command.qppDataSend.length];
        System.arraycopy(Command.qppDataSend, 0, data, 0, data.length);

        data[6] = 0x03;
        data[7] = (byte) play;
        Log.e(TAG,"data:"+Arrays.toString(data));
        return data;
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mConnectedNum = (TextView) findViewById(R.id.connected_num);
        send = (Button) findViewById(R.id.sendData);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentAddress != null){
                    changeLevelInner(currentAddress,-16717569);
                }
            }
        });
        setConnectedNum();
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        mListView.setAdapter(mLeDeviceListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BleDevice device = mLeDeviceListAdapter.getDevice(position);
                if (device != null) {
                    final Intent intent = new Intent(MainActivity.this, DeviceControlActivity.class);
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getmBleName());
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getBleAddress());
                    startActivity(intent);
                }
                if (mBluetoothLeService.mScanning) {
                    mBluetoothLeService.scanLeDevice(false);
                }
                if (device.isConnected()) {
                    mBluetoothLeService.disconnect(device.getBleAddress());
                } else {
                    mBluetoothLeService.connect(device.getBleAddress());
                }
            }
        });
    }

    public void initBleDevice() {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            requestPermission(new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION}, getString(R.string.ask_permission), new GrantedResult() {
                @Override
                public void onResult(boolean granted) {
                    if (!granted) {
                        finish();
                    } else {
                        bindService();// 绑定服务
                    }
                }
            });
        }
    }

    private void setConnectedNum() {
        if (mBluetoothLeService != null) {
            mConnectedNum.setText(getString(R.string.lined_num) + mBluetoothLeService.getConnectedDevices().size());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (mBluetoothLeService != null && !mBluetoothLeService.mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.scanLeDevice(true);
                }
                break;
            case R.id.menu_stop:
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.scanLeDevice(false);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        } else {
            //打开蓝牙  则开始绑定服务
            bindService();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.scanLeDevice(false);
        }
        mLeDeviceListAdapter.clear();
    }

}
