package com.matterhornlegal.customui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.matterhornlegal.fragments.HomeHeaderFragment;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by karan.kalsi on 10/12/2017.
 */

public class HomeHeaderPager extends ViewPager {
    private HomeHeaderPagerAdapter mAdapter;
    private List<OnPageChangeListener>  mListeners= new ArrayList<>();
    private Handler mHandler = new Handler();
    public HomeHeaderPager(Context context) {
        super(context);
        init(context);
    }

    public HomeHeaderPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context)
    {
        mAdapter=new HomeHeaderPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager());
        setAdapter(mAdapter);
        setCurrentItem(1,false);
        super.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0)
                {
                    setCurrentItemWrapper(mAdapter.getCount()-2);
                    notifyOnPageSelected(mAdapter.getCount()-3);
                }
                else
                   if (position==mAdapter.getCount()-1)
                   {
                       setCurrentItemWrapper(1);
                       notifyOnPageSelected(0);
                   }
                   else
                   {
                       notifyOnPageSelected(position-1);
                   }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer autochangeTimer = new Timer();
        autochangeTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(getCurrentItem()+1,true);
                    }
                });

            }
        },6000,6000);

    }


    private void setCurrentItemWrapper(final int position)

    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
        setCurrentItem(position,false);
            }
        },100);
    }

    private void notifyOnPageScrollStateChanged(int state)
    {
    for (OnPageChangeListener listener : mListeners)
    {
        listener.onPageScrollStateChanged(state);
    }

    }
    private void notifyOnPageSelected(int position)
    {
        for (OnPageChangeListener listener : mListeners)
        {
            listener.onPageSelected(position);
        }



    }
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {

        if (mListeners != null) {
            mListeners.add(listener);
        }
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    private class HomeHeaderPagerAdapter extends FragmentStatePagerAdapter
    {
        int[] home_headers_txt=new int[AppConstants.HOME_HEADERS_TXT.length+2];
        int[] home_header_img=new int[AppConstants.HOME_HEADERS_IMG.length+2];

        public HomeHeaderPagerAdapter(FragmentManager fm) {
            super(fm);
            init();
        }

        private void init() {

           int size =  AppConstants.HOME_HEADERS_TXT.length;
            for(int i=0;i<home_headers_txt.length;i++)
            {

                if (i==0)
                    home_headers_txt[i] = AppConstants.HOME_HEADERS_TXT[size-1];
                else
                if (i==home_headers_txt.length-1)
                        home_headers_txt[i] = AppConstants.HOME_HEADERS_TXT[0];
                else
                    home_headers_txt[i] = AppConstants.HOME_HEADERS_TXT[i-1];


                if (i==0)
                    home_header_img[i] = AppConstants.HOME_HEADERS_IMG[size-1];
                else
                if (i==home_header_img.length-1)
                    home_header_img[i] = AppConstants.HOME_HEADERS_IMG[0];
                else
                    home_header_img[i] = AppConstants.HOME_HEADERS_IMG[i-1];

            }
        }


        @Override
        public Fragment getItem(int position) {
            return HomeHeaderFragment.newInstance(home_headers_txt[position],home_header_img[position]);
        }

        @Override
        public int getCount() {
            return home_headers_txt.length;
        }
    }
}
