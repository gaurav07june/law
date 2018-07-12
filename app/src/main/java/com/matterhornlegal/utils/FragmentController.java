package com.matterhornlegal.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.matterhornlegal.fragments.BlogsFragment;
import com.matterhornlegal.fragments.EventsListFragment;
import com.matterhornlegal.fragments.FirmsFragment;
import com.matterhornlegal.fragments.HomeFragment;
import com.matterhornlegal.fragments.LiveStreamEventsFragment;
import com.matterhornlegal.fragments.MyEventsFragment;
import com.matterhornlegal.fragments.MyEventsListFragment;
import com.matterhornlegal.fragments.MyFirmsFragment;
import com.matterhornlegal.fragments.MyVideosFragment;
import com.matterhornlegal.fragments.VideosFragment;

import java.util.List;

/**
 * Created by karan.kalsi on 10/10/2017.
 */

public class FragmentController {

    private FragmentController()
    {

    }



    public static final void addHomeFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
        if (fragment==null)
        {


            fragmentTransaction.add(container, HomeFragment.newInstance("",""),HomeFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {
            hideAllFragments(activity,fragmentTransaction);
            fragmentTransaction.show(fragment).commit();
        }

    }

    public static final void addVideoFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(VideosFragment.class.getName());
        if (fragment==null)
        {


            fragmentTransaction.add(container, VideosFragment.newInstance("",""),VideosFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {


            hideAllFragments(activity, fragmentTransaction);
            fragmentTransaction.show(fragment).commit();
        }
    }
    public static final void addFirmFrag(AppCompatActivity activity,int container)
    {

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(FirmsFragment.class.getName());
        if (fragment==null)
        {


            fragmentTransaction.add(container, FirmsFragment.newInstance("",""),FirmsFragment.class.getName());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {
            hideAllFragments(activity, fragmentTransaction);
            fragmentTransaction.show(fragment).commit();
        }
    }

    public static final void replaceHomeFrag(AppCompatActivity activity,int container)
    {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(container, HomeFragment.newInstance("",""));
            fragmentTransaction.commit();

    }
    public static final void replaceVideoFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, VideosFragment.newInstance("",""));
        fragmentTransaction.commit();

    }
    public static final void replaceMyVideoFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, MyVideosFragment.newInstance("",""));
        fragmentTransaction.commit();

    }
    public static final void replaceMyFirmFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, MyFirmsFragment.newInstance("",""),AppConstants.FRAGMENTS_CONST.FIRM_FRAGMENT);

        fragmentTransaction.commit();

    }
    public static final void replaceFirmFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, FirmsFragment.newInstance("",""));
        fragmentTransaction.commit();

    }

    public static final void replaceBlogFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, BlogsFragment.newInstance("",""));
        fragmentTransaction.commit();

    }

    public static final void replaceMyEventListFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, MyEventsListFragment.newInstance("",""), AppConstants.FRAGMENTS_CONST.MY_EVENT_FRAGMENT);
        fragmentTransaction.commit();
        SharedPrefsUtils.setSharedPrefString(activity.getApplicationContext(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.FROM_ACTIVITY, AppConstants.ACTIVITY_CONST.MY_EVENT_FRAGMENT);

    }

    public static final void replaceMyEventsFrag(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, MyEventsFragment.newInstance("",""));
        fragmentTransaction.commit();

    }
    public static final void replaceLiveStreamFragment(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, LiveStreamEventsFragment.newInstance("",""));
        fragmentTransaction.commit();

    }
    public static final void replaceEventListFragment(AppCompatActivity activity,int container)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, EventsListFragment.newInstance("",""), AppConstants.FRAGMENTS_CONST.EVENT_FRAGMENT);
        fragmentTransaction.commit();
        SharedPrefsUtils.setSharedPrefString(activity.getApplicationContext(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.FROM_ACTIVITY, AppConstants.ACTIVITY_CONST.EVENT_FRAGMENT);

    }

    public static final void showFragmentIfHidden(AppCompatActivity activity)
    {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
       List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        if (fragments!=null && fragments.size()>0)
        {
            Fragment curFragment = fragments.get(fragments.size()-1);
            if (curFragment!=null )
            {

                fragmentTransaction.show(curFragment).commit();
            }
        }
    }

    private static final void hideAllFragments(AppCompatActivity activity, FragmentTransaction fragmentTransaction)
    {
         for (Fragment fragment : activity.getSupportFragmentManager().getFragments())
        {
            fragmentTransaction.hide(fragment);
        }
    }
}
