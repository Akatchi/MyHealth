package com.myhealth.application.config;

/**
 * Created by Akatchi on 23-9-2014.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{

    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int index)
    {

        switch (index)
        {
            case 0:
                // Blood measurement activity
                return new BloodFragment();
            case 1:
                // Heart measurement activity
                return new HeartFragment();
            case 2:
                // ECG measrement activity
                return new ECGFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        // get item count - equal to number of tabs
        return 3;
    }

}