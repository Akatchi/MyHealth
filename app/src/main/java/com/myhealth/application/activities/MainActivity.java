package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.myhealth.application.R;
import com.myhealth.application.asynctasks.LogoutTask;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.TabsPagerAdapter;
import com.myhealth.application.config.Variables;
import com.myhealth.application.database.BloodMeasure;
import com.myhealth.application.database.DatabaseHelper;

import java.util.List;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
    private SessionManager  session;
    private int             REQUEST_ENABLE_BT = 1;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList, mMeasurements;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<BloodMeasure> bloodMeasureList;
    private DatabaseHelper dbHelper;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private String tab1, tab2, tab3;
    // Tab titles
    private String[] tabs;
//    private String[] tabs = { "blood", "heart", "ecg" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        tab1 = this.getString(R.string.fragment_blood_measures);
        tab2 = this.getString(R.string.fragment_heart_measures);
        tab3 = this.getString(R.string.fragment_ecg_measures);

        tabs = new String[]{ tab1, tab2, tab3 };

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
//        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs)
        {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        mPlanetTitles = getResources().getStringArray(R.array.drawer_values);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mMeasurements = (ListView) findViewById(R.id.measureList);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.item_sidebar, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id)
            {
                String selectedItem = mPlanetTitles[position];

                if( selectedItem.equals(getResources().getString(R.string.test_urine)) )
                {
                    Intent intentUrine = new Intent(getApplicationContext(), UrineActivity.class);
                    startActivity(intentUrine);
                }
                else if( selectedItem.equals(getResources().getString(R.string.test_bloed)) )
                {
                    Intent intentBloed = new Intent(getApplicationContext(), BloedActivity.class);
                    startActivity(intentBloed);
                }
                else if( selectedItem.equals(getResources().getString(R.string.test_ecg)) )
                {
                    Intent intentECG = new Intent(getApplicationContext(), ECGActivity.class);
                    startActivity(intentECG);
                }
                else if( selectedItem.equals(getResources().getString(R.string.test_heartrate)) )
                {
                    Intent intentHeart = new Intent(getApplicationContext(), HeartActivity.class);
                    startActivity(intentHeart);
                }
                else if( selectedItem.equals(getResources().getString(R.string.logout)) )
                {
                    try
                    {
                        int code = new LogoutTask(session.getToken()).execute(new String[]{Variables.LOGOUTURL}).get();
                        session.logoutUser();
                    }
                    catch( Exception e ){ e.printStackTrace(); }
                }
            }

        });
        // the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {


            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });

    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {
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
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
