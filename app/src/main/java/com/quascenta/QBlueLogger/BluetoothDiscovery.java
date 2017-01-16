package com.quascenta.QBlueLogger;

/**
 * Created by AKSHAY on 1/16/2017.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quascenta.petersroad.broadway.R;

import java.util.ArrayList;
@SuppressWarnings("deprecation")
public class BluetoothDiscovery extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static final String EXTRA_DEVICE_ADDRESS = "deviceAddress";
    public static final String EXTRA_DEVICE_NAME = "deviceName";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    //ArrayList to store all macs
    private ArrayList<String> storageMACs;

    private boolean mScanning;
    private Handler mHandler;

    private ProgressBar scanningWheel;

    private TextView dialogTitle;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 6000;

    private View viewScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        // Setup the window
        setContentView(R.layout.ble_devicelist);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        //Set the progressBar
        scanningWheel = (ProgressBar) findViewById(R.id.progressBarScanning);
        scanningWheel.setVisibility(View.GONE);

        //Set the arraylist to store all MACs
        storageMACs = new ArrayList<String>();

        // Initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(v -> {
            doDiscovery();
            viewScan = v;
            viewScan.setVisibility(View.GONE);
            scanningWheel.setVisibility(View.VISIBLE);
        });

        // Initialize array adapters.
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.ble_devicename);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();

        // Title to be updated with the status
        dialogTitle = (TextView) findViewById(R.id.title_dialog);

        // Indicate scanning in the title
        dialogTitle.setText(R.string.start_scanning);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Clear the arraylist with all MACs
        storageMACs.clear();

        // Make sure we're not doing discovery anymore
        if(mBtAdapter != null)
            scanLeDevice(false);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        dialogTitle.setText(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // Clear all the discovered devices
        mNewDevicesArrayAdapter.clear();

        // Initializes list view adapter.
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBtAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                    viewScan.setVisibility(View.VISIBLE);

                    // When discovery is finished, change the Activity title
                    dialogTitle.setText(R.string.select_device);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBtAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallback);

            if(viewScan != null)
                viewScan.setVisibility(View.VISIBLE);

            // When discovery is finished, change the Activity title
            dialogTitle.setText(R.string.select_device);
        }
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            if (mScanning) {
                mBtAdapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();

            String name = info;
//            String address = info.substring(info.length() - 17);

            String address = storageMACs.get(position);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            intent.putExtra(EXTRA_DEVICE_NAME, name);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(device.getName() != null) {
                            mNewDevicesArrayAdapter.add(device.getName());
                            storageMACs.add(device.getAddress());
                        }
                    }

            });
        }
    };
}