package com.matterhornlegal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;

/**
 * Created by seema.gurtatta on 10/23/2017.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private NMGTextView mTvPushNotifications, mTvResetPassword;
    private NMGTextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();
        initView();
    }

    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        actionBar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        mTvPushNotifications=(NMGTextView)findViewById(R.id.settingsPushNotificationTv);
        mTvResetPassword=(NMGTextView)findViewById(R.id.settingsResetPasswordTv);
        mTvTitle=(NMGTextView)findViewById(R.id.titleTv);
        mTvTitle.setText(getString(R.string.settings));

        mTvPushNotifications.setOnClickListener(this);
        mTvResetPassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settingsPushNotificationTv: {
                Intent intent = new Intent(SettingsActivity.this, PushNotificationSettingActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.settingsResetPasswordTv:{
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }

                break;
        }

    }
}
