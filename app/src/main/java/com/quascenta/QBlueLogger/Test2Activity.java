package com.quascenta.QBlueLogger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quascenta.petersroad.broadway.R;

import java.util.List;

/**
 * Created by AKSHAY on 1/16/2017.
 */

public class Test2Activity extends TestMainActivity implements BluetoothClient.OnBluetoothConnectListener {


    public static final String TAG = "MyDevicesActivity";

    private TextView instruction;

    private List<Device> mDevices;
    private ListView mList;

    private MyDevicesAdapter mAdapter;
    private MyDbHelper mMyDbHelper;

    private boolean mNewConnectionRequired = false;
    private String mNewDeviceAddress;
    private String mNewDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);

        // Get database helper
        mMyDbHelper = new MyDbHelper(this);
        mList = (ListView) findViewById(R.id.list_devices);

        instruction = (TextView) findViewById(R.id.instruction);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showDevicesInfo();
    }

    private void showDevicesInfo() {
        // Refresh the list
        mDevices = mMyDbHelper.getAllDevices();

        if(mDevices.isEmpty())
            instruction.setText(getResources().getString(R.string.device_list_empty));
        else
            instruction.setText(getResources().getString(R.string.device_press));

        // Get the id of the device I am connected to
        int id = isDeviceConnected() ? getDeviceId() : 0;

        mAdapter = new MyDevicesAdapter(getApplicationContext(), mDevices, id);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    if(isDeviceConnected() == false) {
                        // initialize the BLE connection to this device
                        initConnection(mDevices.get(pos).getDeviceMac(), mDevices.get(pos).getDeviceName());
                    } else {
                        if(mDevices.get(pos).getId() == getDeviceId())
                            showDeviceDisconnectedDialog(mDevices.get(pos).getDeviceMac(), mDevices.get(pos).getDeviceName());
                        else
                            showDeviceConnectedDialog(mDevices.get(pos).getDeviceMac(), mDevices.get(pos).getDeviceName());
                    }
                }

        });

        // Register for actions
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                showRemovedeviceDialog(mDevices.get(position).getId(), mDevices.get(position).getDeviceName());
                return true;
            }
        });
    }

    private void showDeviceDisconnectedDialog(final String address, final String name) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Test2Activity.this);
        builder.setTitle("Bluetooth Connection");
        builder.setMessage("You are connected to this device. Are you sure you want to disconnect?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                closeConnection();

                // Show connected device info
                showDevicesInfo();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void showDeviceConnectedDialog(final String address, final String name) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Test2Activity.this);
        builder.setTitle("Bluetooth Connection");
        builder.setMessage("You are connected to another device. Are you sure you want to connect to " + name);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                closeConnection();

                // Show connected device info
                showDevicesInfo();

                // the user wants to initialize a new connection when the current one is closed
                mNewConnectionRequired = true;
                mNewDeviceAddress = address;
                mNewDeviceName = name;

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    private void showRemovedeviceDialog(final int id, final String name) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Test2Activity.this);
        builder.setTitle("Delete Device");
        builder.setMessage("Are you sure you want to delete " + name + " device? Registered services will not be removed from the eSE");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                // Delete device by ID
                mMyDbHelper.deleteDevice(id);

                // Get the id of the device I am connected to
                int id = isDeviceConnected() ? getDeviceId() : 0;

                // Remove the card and update the adapter
                mDevices = mMyDbHelper.getAllDevices();
                mAdapter.updateDevices(mDevices, id);

                // Update info on the screen
                mAdapter.notifyDataSetChanged();

                if(mDevices.isEmpty())
                    instruction.setText(getResources().getString(R.string.device_list_empty));

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    @Override
    public void onConnect(final boolean connected) {
        super.onConnect(connected);

        // Show connected device info
        Test2Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDevicesInfo();

                // Check whether we are supposed to connect to another device
                if(mNewConnectionRequired == true) {
                    // launch the new connection
                    initConnection(mNewDeviceAddress, mNewDeviceName);

                    mNewConnectionRequired = false;
                }
            }
        });
    }

}
