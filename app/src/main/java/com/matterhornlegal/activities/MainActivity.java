package com.matterhornlegal.activities;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.AppDemoPagerAdapter;
import com.matterhornlegal.databinding.ActivityMainBinding;
import com.matterhornlegal.models.DemoPageScrollEvent;
import com.matterhornlegal.models.DummyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private AppDemoPagerAdapter appDemoPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        binding=   DataBindingUtil.setContentView(this,R.layout.activity_main);
        setViews();
    }


    public void setViews()
    {
        appDemoPagerAdapter = new AppDemoPagerAdapter(getSupportFragmentManager());
        binding.appDemoPager.setAdapter(appDemoPagerAdapter);
        binding.demoScreenIndicator.setViewPager(binding.appDemoPager);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this).load("file:///android_asset/demo_bg.jpg").apply(options).into( binding.demoBg);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
