package com.matterhornlegal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.RegisteredUserDetail;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private NMGTextView mTvLogin, mTvSignup, mTvSkip;
    private RegisteredUserDetail registeredUserDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        mTvLogin = (NMGTextView) findViewById(R.id.splashLoginTv);
        mTvSignup = (NMGTextView) findViewById(R.id.splashSignupTv);
        mTvSkip = (NMGTextView) findViewById(R.id.splashSkipTv);

        mTvLogin.setOnClickListener(this);
        mTvSignup.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);

        /*String token=SharedPrefsUtils.getSharedPrefString(SplashActivity.this, AppConstants.Prefrences.PREF_NAME,AppConstants.Prefrences.TOKEN,"");
        String id=SharedPrefsUtils.getSharedPrefString(SplashActivity.this, AppConstants.Prefrences.PREF_NAME,AppConstants.Prefrences.USER_ID,"");
        */
        /*if(!Utils.isNullOrEmpty(token) && !Utils.isNullOrEmpty(id)){
            Intent intent = new Intent(SplashActivity.this, HomeLandingActivity.class);
            startActivity(intent);
            finish();
        }*/
        if (AppGlobalData.getInstance().getUserFromPref(getApplicationContext())){
            Intent intent = new Intent(SplashActivity.this, HomeLandingActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splashLoginTv: {
                Intent intent = new Intent(SplashActivity.this, LoginEmailActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.splashSignupTv: {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.splashSkipTv: {
                AppGlobalData.getInstance().setGuest(true);
                AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                Intent intent = new Intent(SplashActivity.this, HomeLandingActivity.class);
                startActivity(intent);
                finish();
            }
            break;
        }
    }
}
