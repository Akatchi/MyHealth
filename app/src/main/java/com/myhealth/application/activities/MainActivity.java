package com.myhealth.application.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.myhealth.application.R;
import com.myhealth.application.config.SessionManager;


public class MainActivity extends Activity
{
    private SessionManager  session;
    private int             REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();
    }


    //Hier wordt de menu balk geiniteerd
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Hier wordt de in de xml gespecificeerde layout toegepast op de menu balk
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Hier worden de kliks op de items in de menu balk afgehandeld
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch( item.getItemId() )
        {
            case R.id.action_settings:
                //Als er op het settings icoon in de menubalk gedrukt is start dan de settings activiteit
                Intent i = new Intent(this, SettingsActivity.class);
                    startActivity(i);
                return true;
            case R.id.action_search_bluetooth:
                //Als er op de bluetooth zoek knop gedruk wordt moet er gezocht worden naar een bluetooth device
                //Todo: code om naar bluetooth apparaat te zoeken
                initBleutooth();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initBleutooth()
    {
        //Activeer de bluetooth en kijk of de user bleutooth op zijn device heeft
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null)
        {
            //User ondersteund geen Bleutooth
            Toast.makeText(getApplicationContext(), "No bleutooth support on your device", Toast.LENGTH_SHORT).show();
        }

        //Als bluetooth uitstaat vraag de user om het aan te zetten
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mBluetoothAdapter.startDiscovery();
        BroadcastReceiver mReceiver        = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
}
