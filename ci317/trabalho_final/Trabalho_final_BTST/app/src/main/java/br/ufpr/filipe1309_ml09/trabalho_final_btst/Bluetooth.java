package br.ufpr.filipe1309_ml09.trabalho_final_btst;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;


public class Bluetooth extends Activity {

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    // Views
    private Button bt_find_stop;
    private Button bt_disc;
    private TextView tv_text;
    private TextView title_new_devices;
    private TextView tv_select_a_device;
    ListView newDevicesListView;

    // Local variables
    int DISCOVERABLE_DURATION = 15;
    ArrayList<BluetoothDevice> devices;
    public static Activity bt;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_RESET = 6;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes, that it is a requestCode(any unique integer greater than zero),
    // that's used  to check the return of onActivityResult()
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Debugging
    private static final String TAG = "Bluetooth";
    private static final boolean D = true;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mBTService = null;


    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Intent intent = new Intent(getBaseContext(), SuperTrunfo.class);
                            Globals.myBTService = mBTService;
                            startActivity(intent);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            tv_text.setText("Status: "+getResources().getString(R.string.title_connecting));
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            tv_text.setText("Status: "+getResources().getString(R.string.not_connected));
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_DEVICE_NAME:

                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.title_connected_to)+" " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.unable_to_connect), Toast.LENGTH_SHORT)
                            .show();
                    Globals.server = false;
                    restartActivity();
                    break;
                case MESSAGE_RESET:
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.connection_lost), Toast.LENGTH_SHORT)
                            .show();

                    // If the connection to the adversary is closed,then close(if it's open)
                    // the local SuperTrunfo Activity and restart
                    if (SuperTrunfo.st != null)
                        SuperTrunfo.st.finish();
                    restartActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        setConfigs();
        bt = this;

        // Verify if the device has bluetooth
        if(mBluetoothAdapter == null) {
            bt_find_stop.setEnabled(false);
            bt_disc.setEnabled(false);
            tv_text.setText("Status: "+getResources().getString(R.string.bt_not_supported_leaving));
            Toast.makeText(this, getResources().getString(R.string.bt_not_supported_leaving), Toast.LENGTH_LONG).show();
            finish();
            Intent intent = new Intent(getBaseContext(), SuperTrunfo.class);
            startActivity(intent);
            return;
        }

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        registerBR();

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            tv_select_a_device.setVisibility(View.VISIBLE);
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired)
                    .toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void setConfigs() {
        tv_text = (TextView) findViewById(R.id.tv_text);
        bt_disc = (Button) findViewById(R.id.bt_disc);
        bt_find_stop = (Button) findViewById(R.id.bt_find_stop);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devices = new ArrayList<BluetoothDevice>();
        title_new_devices = (TextView) findViewById(R.id.title_new_devices);
        tv_select_a_device = (TextView) findViewById(R.id.tv_select_a_device);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (D)
            Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setup() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            turnOnBT();
        } else {

            // Otherwise, setup the session
            if (mBTService == null)
                setupBluetooth();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBTService != null) {

            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mBTService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mBTService.start();
            }
        }
    }

    private void setupBluetooth() {
        configButtons();

        // Initialize the BluetoothService to perform bluetooth connections
        mBTService = new BluetoothService(this, mHandler);
    }

    private void configButtons() {

        // Button to make discoverable
        bt_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeviceVisible();
            }
        });

        // Button to discovery devices
        bt_find_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for the list o new devices goback to the original heigth
                newDevicesListView.setVisibility(View.GONE);
                newDevicesListView.setVisibility(View.VISIBLE);
                find();
            }
        });
    }

    /*
    *   Register the BroadcastReceiver
    */
    public void registerBR() {
        IntentFilter filter = new IntentFilter();

        // to identify when a device is founded
        filter.addAction(BluetoothDevice.ACTION_FOUND);

        // to identify when the discovery is finished
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // to identify when the visibility state change
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        // to identify if the bluetooth state (on/off) change
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // to identify when pairing state change
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver,filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device;

            // When found a device
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDevice(remote) object from the Intent
                device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                } else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.already_paired)+" "+device.getName(),
                            Toast.LENGTH_SHORT).show();
                }

            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(mBluetoothAdapter.isEnabled()) {
                    bt_find_stop.setEnabled(true);
                }
                bt_find_stop.setText(R.string.find_stop);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.none_found).toString()+"\n";
                    mNewDevicesArrayAdapter.add(noDevices);
                } else {
                    tv_select_a_device.setVisibility(View.VISIBLE);
                }

            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {

                // when visible mode finished
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,0);
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    bt_disc.setEnabled(false);
                } else {
                    bt_disc.setEnabled(true);
                }

            } else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

               // if bluetooth turn off
               if(mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_OFF){
                    // code
               } else if(mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_ON) {
                    // code
               }
            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                // code
            }
        }
    };

    private void turnOnBT() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void restartActivity() {
        if (mBTService != null)
            mBTService.stop();
        Globals.myBTService = null;
        Globals.server = false;
        recreate();
    }

    public void setDeviceVisible() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent enablaBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            enablaBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
            startActivity(enablaBtIntent);
        }
    }

    public void find() {
        // if is in discovery, then cancel
        if (mBluetoothAdapter.isEnabled())
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            } else {
                mNewDevicesArrayAdapter.clear();
                title_new_devices.setVisibility(View.VISIBLE);
                bt_find_stop.setEnabled(false);
                bt_find_stop.setText(getResources().getString(R.string.scanning));
                mBluetoothAdapter.startDiscovery();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice("s");
                }
                break;
            case REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {

                    // Bluetooth is now enabled, so set up a session
                    tv_text.setText("Status: Enable");
                    restartActivity();
                    setupBluetooth();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this,getResources().getString(R.string.bt_not_enabled_leaving),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void connectDevice(String address) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // Attempt to connect to the device
        mBTService.connect(device);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
            this.unregisterReceiver(mReceiver);
        }

        // Stop the Bluetooth services
        if (mBTService != null)
            mBTService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the
            // View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Globals.server = true;
            connectDevice(address);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
