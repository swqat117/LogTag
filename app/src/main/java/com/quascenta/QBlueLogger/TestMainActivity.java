package com.quascenta.QBlueLogger;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.quascenta.QBlueLogger.QppBle.QppApi;
import com.quascenta.petersroad.broadway.R;

public class TestMainActivity extends AppCompatActivity implements BluetoothClient.OnBluetoothConnectListener, BluetoothClient.OnBluetoothConnectionPendingListener, BluetoothClient.OnBluetoothReadListener, BluetoothClient.OnBluetoothWriteListener {


    private byte[] mBufferDataCmd = null;

    private int mBufferOffset = 0;
    private int mExpectedSize = 0;

    private static CountDownTimer counterAck;
    private static OnOperationListener mDelegateOperation;
    // Countdown used to wait for an operation to be completed (operation specific)
    private static CountDownTimer counterOp;
    public static int TIMER_BLE_OP;
    private static final String TAG = "TEST MAIN ACTIVITY";
    private static AsyncTask<?, ?, ?> runningTask;
    private int mPermissionIdx = 0x10;
    private SparseArray<TestMainActivity.GrantedResult> mPermissions   = new SparseArray<>();
    private BluetoothAdapter mBluetoothAdapter;
    public static boolean showDiscoveryDialog;
    public static boolean showDisconnectionDialog;
    private static BluetoothClient mBluetooth;
    public static final int REQUEST_ENABLE_BT = 0x100;
    public static final int REQUEST_CONNECT_DEVICE = 0x101;
    private String mAddress;
    private String mName;
    private boolean warningInactivity = false;
    private static int mId;
    private MyDbHelper mMyDbHelper;
    public static final int TIMER_BLE_ACK_RX = 6000;
    public static abstract class GrantedResult implements Runnable{
        private boolean mGranted;
        public abstract void onResult(boolean granted);
        @Override
        public void run(){
            onResult(mGranted);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_test_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showBluetoothNotSupportedDialog();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if(mBluetooth == null)
                mBluetooth = new BluetoothClient(mBluetoothAdapter, getApplicationContext(), TestMainActivity.this);

            // Prepare the delegates for the callback interfaces
            mBluetooth.mDelegateConnectionPending = this;
            mBluetooth.mDelegateConnected = this;
            mBluetooth.mDelegateRead = this;
            mBluetooth.mDelegateWrite = this;

            // Launch the DeviceDiscoveryActivity to see devices and do scan
            if(!mBluetooth.isConnected()) {
                Intent serverIntent = new Intent(getApplicationContext(), BluetoothDiscovery.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                // Don't show the dialog anymore
                showDiscoveryDialog = false;
            }
        }
    }
    public void requestPermission(String[] permissions, String reason, TestMainActivity.GrantedResult runnable) {
        if(runnable == null){
            return;
        }
        runnable.mGranted = false;
        if (Build.VERSION.SDK_INT < 23 || permissions == null || permissions.length == 0) {
            runnable.mGranted = true;
            runOnUiThread(runnable);
            return;
        }
        final int requestCode = mPermissionIdx++;
        mPermissions.put(requestCode, runnable);


        boolean granted = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                granted = granted && checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }

        if (granted) {
            runnable.mGranted = true;
            runOnUiThread(runnable);
            return;
        }


        boolean request = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request = request && !shouldShowRequestPermissionRationale(permission);
            }
        }

        if (!request) {
            final String[] permissionTemp = permissions;
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                    .setMessage(reason)
                    .setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionTemp, requestCode);
                            }
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TestMainActivity.GrantedResult runnable = mPermissions.get(requestCode);
                            if (runnable == null) {
                                return;
                            }
                            runnable.mGranted = false;
                            runOnUiThread(runnable);
                        }
                    }).create();
            dialog.show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }
        }
    }


    public void closeConnection() {
        if(mBluetooth != null && mBluetooth.isConnected())
            mBluetooth.close();
        // it is the user who wants to start the disconnection so don't show the dialog
        showDisconnectionDialog = false;
        // Cancel the timer if exists
        if(counterAck != null)
            counterAck.cancel();


    }

    public void closeBluetoothClient() {
        if(mBluetooth != null)
            mBluetooth.closeBluetoothClient();

        mBluetooth = null;

        // Close running task
        if(runningTask != null) {
            runningTask.cancel(true);
        }
    }
    @Override
    public void onConnect(boolean connected) {
        runOnUiThread(new Runnable() {
            public void run() {
                if(connected == true) {
                    if(warningInactivity == false)
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.bt_conn_established), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.bt_conn_established_inact), Toast.LENGTH_LONG).show();

                    // Store the new detected device
                    mId = mMyDbHelper.addDevice(mName, mAddress);

                    // Make the user know the connection status


                    // By default we want to
                    showDisconnectionDialog = true;
                } else {
//		    		Toast.makeText(getApplicationContext(), "Bluetooth connection closed", Toast.LENGTH_LONG).show();

                    Log.d(TAG, "OnConnect FALSE");

                    // We are no longer completing NFC Operations

                    // Close running task
                    if(runningTask != null) {
                        runningTask.cancel(true);
                    }

                    // We are not connected anymore
                    mId = 0;

                    if(showDisconnectionDialog == true)
                        showConnectionLostDialog();

                    // Make the user know the connection status

                    // Close the connection
                    if(mBluetooth != null)
                        mBluetooth.close();


                }


            }
        });
    }
    private void showConnectionLostDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(TestMainActivity.this);
        builder.setTitle("Bluetooth disconnection");
        builder.setMessage("The connection with your Connected Device has been lost");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });


        // Create the AlertDialog object and return it
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }



    @Override
    public void onConnectionPending() {
        Toast.makeText(TestMainActivity.this, "Pending connection to be solved. Please wait a few seconds... (30 secs max)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        TestMainActivity.GrantedResult runnable = mPermissions.get(requestCode);
        if (runnable == null) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runnable.mGranted = true;
        }
        runOnUiThread(runnable);
    }


    @Override
    public void onRead(byte[] status) {
        if(status == null) {
            Toast.makeText(getApplicationContext(), "Error reading data in the Bluetooth connection", Toast.LENGTH_LONG).show();
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    int bytesToCopy = 0;

                    // This is the first message that I receive for this command
                    if(mBufferOffset == 0) {
                        // This is the total length of the command that I will receive
                        mExpectedSize = (status[1] & 0xff) + ((status[2] & 0xff) * 0x100);

                        // I prepare the buffer size
                        mBufferDataCmd = new byte[mExpectedSize];

                        if(mExpectedSize > QppApi.qppServerBufferSize - 3)
                            bytesToCopy = QppApi.qppServerBufferSize - 3;
                        else
                            bytesToCopy = mExpectedSize;

                        // Copy the data in the first position
                        System.arraycopy(status, 3, mBufferDataCmd, 0, bytesToCopy);
                        mBufferOffset = bytesToCopy;
                    } else {
                        if(mExpectedSize - mBufferOffset > QppApi.qppServerBufferSize)
                            bytesToCopy = QppApi.qppServerBufferSize;
                        else
                            bytesToCopy = mExpectedSize - mBufferOffset;

                        System.arraycopy(status, 0, mBufferDataCmd, mBufferOffset, bytesToCopy);
                        mBufferOffset = mBufferOffset + bytesToCopy;
                    }

                    Log.d(TAG, "BLE Message received " + mBufferOffset + " / " + mExpectedSize);

                    // If we have received the whole command we can proceed
                    if(mBufferOffset == mExpectedSize) {
                        if(mBufferDataCmd[0] == 'a' && mBufferDataCmd[1] == 'c' && mBufferDataCmd[2] == 'k') {
                            Log.d(TAG, "ACK Received");

                            // ACK Received, we can cancel the timer
                            if(counterAck != null) {
                                counterAck.cancel();

                                // Start the operation specific counter
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(counterOp != null)
                                            counterOp.cancel();

                                        counterOp = new CountDownTimer(TIMER_BLE_OP, 1000) {
                                            @Override
                                            public void onFinish() {
                                                Log.d(TAG, "Operation Counter fired");

                                                // Operation completed on the Connected Device


                                                // By sending a null we inform the main app about the BLE Channel error
                                                mDelegateOperation.processOperationNotCompleted();
                                            }

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                            }
                                        };

                                        // Start the counter
                                        counterOp.start();
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Received Data: " + new String(mBufferDataCmd));

                            Log.d(TAG, "Received Data String: " + new String(mBufferDataCmd));

                            StringBuilder sb = new StringBuilder();
                            for (byte b : mBufferDataCmd) {
                                sb.append(String.format("%02X ", b));
                            }
                            Log.e(TAG, "RECEIVED DATA+++++++++++++++++++++++++++++++= "+sb.toString());
                            // Operation completed on the Connected Device
                           // MyPreferences.setCardOperationOngoing(getApplicationContext(), false);

                            // Response received, we can cancel the timer
                            if(counterOp != null) {
                                counterOp.cancel();

                                if(counterAck != null)
                                    counterAck.cancel();
                            }

                            if(mDelegateOperation != null) {
                                mDelegateOperation.processOperationResult(mBufferDataCmd);
                            } else
                                Toast.makeText(getApplicationContext(), "DELEGATE NULL", Toast.LENGTH_LONG).show();
                        }

                        mBufferDataCmd = new byte[0];
                        mBufferOffset = 0;
                        mExpectedSize = 0;
                    } else {
                        // Send ACK?

                    }
                }
            });
        }
    }

    @Override
    public void onWrite() {
        runOnUiThread(new Runnable() {
            public void run() {
                if(counterAck != null)
                    counterAck.cancel();

                counterAck = new CountDownTimer(TIMER_BLE_ACK_RX, 5000) {
                    @Override
                    public void onFinish() {
                        Log.d(TAG, "ACK Counter fired");

                        // Operation completed on the Connected Device
                        // MyPreferences.setCardOperationOngoing(getApplicationContext(), false);

                        // By sending a null we inform the main app about the BLE Channel error
                        mDelegateOperation.processOperationNotCompleted();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                };

                // Start the counter
                counterAck.start();
            }
        });
    }


    public static void setRunningAsyncTask(AsyncTask<?, ?, ?> task) {
        runningTask = task;
    }

    private void showBluetoothNotSupportedDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(TestMainActivity.this);
        builder.setTitle("Bluetooth not supported");
        builder.setMessage("This application requires a valid Bluetooth adapter to run");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                if(mBluetooth == null)
                    mBluetooth = new BluetoothClient(mBluetoothAdapter, getApplicationContext(), TestMainActivity.this);

                // Prepare the delegates for the callback interfaces
                mBluetooth.mDelegateConnectionPending = this;
                mBluetooth.mDelegateConnected = this;
                mBluetooth.mDelegateRead = this;
                mBluetooth.mDelegateWrite = this;

                // Launch the DeviceDiscoveryActivity to see devices and do scan
                if(!mBluetooth.isConnected() && showDiscoveryDialog) {
                    Intent serverIntent = new Intent(getApplicationContext(), BluetoothDiscovery.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                    // Don't show the dialog anymore
                    showDiscoveryDialog = false;
                }
            } else {
                Toast.makeText(TestMainActivity.this, "Error enabling Bluetooth", Toast.LENGTH_LONG).show();
//        		finish();
            }
        } else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == Activity.RESULT_OK) {
            String address = data.getExtras().getString(BluetoothDiscovery.EXTRA_DEVICE_ADDRESS);
            String name = data.getExtras().getString(BluetoothDiscovery.EXTRA_DEVICE_NAME);

            initConnection(address, name);
        }
    }
    public void initConnection(String address, String name) {
        mAddress = address;
        mName = name;

        Log.d(TAG, "Launch new connection " + mAddress);

        if(mBluetooth != null)
            mBluetooth.connect(mAddress);
    }

    public static boolean isDeviceConnected() {
        if(mBluetooth == null)
            return false;

        return mBluetooth.isConnected();
    }

    public String getDeviceAddress() {
        return mAddress;
    }

    public String getDeviceName() {
        return mName;
    }

    public static int getDeviceId() {
        return mId;
    }
}
