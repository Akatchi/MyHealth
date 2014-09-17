package com.myhealth.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.myhealth.application.R;
import com.myhealth.application.config.SessionManager;


public class MainActivity extends Activity
{
    private SessionManager session;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        mPlanetTitles = getResources().getStringArray(R.array.drawer_values);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.item_sidebar, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {

            }

        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.hello_world, R.string.app_name)
        {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("hoi");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
                Intent i = new Intent(this, UrineActivity.class);
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
