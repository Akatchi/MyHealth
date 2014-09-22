package com.myhealth.application.activities;


import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.myhealth.application.R;
import com.myhealth.application.config.Variables;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends Activity
{
    private int                             REQUEST_ENABLE_BT = 1;
    private ListView                        mListview;
    private ArrayAdapter<String>            mArrayAdapter;
    private ArrayList<BluetoothDevice>      activeDevices;
    private BluetoothAdapter                mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        activeDevices = new ArrayList<BluetoothDevice>();

        mListview = (ListView) findViewById(R.id.listView);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                BluetoothDevice clickedDevice = activeDevices.get(position);
                Toast.makeText(getApplicationContext(), clickedDevice.getAddress(), Toast.LENGTH_SHORT).show();

                ConnectThread connect = new ConnectThread(clickedDevice);
                    connect.run();

            }
        });

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        initBluetooth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch( item.getItemId() )
        {
            case android.R.id.home:
                //Als er op de bluetooth zoek knop gedruk wordt moet er gezocht worden naar een bluetooth device
                Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initBluetooth()
    {
        //Activeer de bluetooth en kijk of de user bleutooth op zijn device heeft
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        if (mBluetoothAdapter == null)
        {
            //User ondersteund geen Bleutooth
            Toast.makeText(getApplicationContext(), "No bluetooth support on your device", Toast.LENGTH_SHORT).show();
        }

        //Als bluetooth uitstaat vraag de user om het aan te zetten
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (mBluetoothAdapter.isDiscovering()) { mBluetoothAdapter.cancelDiscovery(); }

        mBluetoothAdapter.startDiscovery();

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // only display the bonded devices.
                if(device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    activeDevices.add(device);
                }
            }
            mListview.setAdapter(mArrayAdapter);
        }
    };


    private class ConnectThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device)
        {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try
            {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Variables.UUID));
            }
            catch (IOException e) { e.printStackTrace(); }
            mmSocket = tmp;
        }

        public void run()
        {

            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try
            {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            }
            catch (IOException connectException)
            {
                // Unable to connect; close the socket and get out
                try
                {
                    mmSocket.close();
                }
                catch (IOException closeException) { closeException.printStackTrace(); }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e) { }
        }

        /** Manage the connnection at hand and read the bytes that are sent **/
        public void manageConnectedSocket(BluetoothSocket socket){

            try {
                ConnectedThread thread = new ConnectedThread(socket, new Handler());
                Thread t = new Thread(thread);
                t.start();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final Handler mHandler;

        public ConnectedThread(BluetoothSocket socket, Handler handler) {
            mmSocket = socket;
            mHandler = handler;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            long length = 0;
            try
            {
                length = mmInStream.available();
            }
            catch (Exception e) { e.printStackTrace(); }
            byte[] buffer = new byte[(int) length];  // buffer store for the stream
            int bytes; // bytes returned from read()
            Log.e("BEGIN LOG", "INCOMMING");
            // Keep listening to the InputStream until an exception occurs
            while (true)
            {
                try
                {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                    String test = new String(buffer);
                    Log.d("incomming", buffer.toString());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes)
        {
            try
            {
                mmOutStream.write(bytes);
            }
            catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e) { }
        }
    }
}
