package com.matterhornlegal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.matterhornlegal.fragments.AppDemoFragment;
import com.matterhornlegal.utils.AppConstants;

/**
 *
 * Created by karan.kalsi on 9/28/2017.
 */

public class AppDemoPagerAdapter extends FragmentStatePagerAdapter {


    public AppDemoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return AppDemoFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return AppConstants.DEMO_PAGES.COUNT;
    }
}
