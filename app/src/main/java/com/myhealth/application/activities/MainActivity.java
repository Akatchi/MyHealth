package com.myhealth.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.myhealth.application.R;
import com.myhealth.application.config.SessionManager;


public class MainActivity extends Activity
{
    private SessionManager session;

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
                Intent j = new Intent(this, BluetoothActivity.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
