package com.matterhornlegal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.matterhornlegal.R;

/**
 * Created by seema.gurtatta on 10/25/2017.
 */
public class TemporaryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnEditProfile, mBtnSettings, mBtnContactUs, mBtnPrivacyPolicy, mBtnTermsCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        init();
    }

    private void init() {
        mBtnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        mBtnSettings = (Button) findViewById(R.id.btnSettings);
        mBtnContactUs = (Button) findViewById(R.id.btnContactUs);
        mBtnPrivacyPolicy = (Button) findViewById(R.id.btnPrivacyPolicy);
        mBtnTermsCondition = (Button) findViewById(R.id.btnTermsConditions);

        mBtnEditProfile.setOnClickListener(this);
        mBtnSettings.setOnClickListener(this);
        mBtnContactUs.setOnClickListener(this);
        mBtnPrivacyPolicy.setOnClickListener(this);
        mBtnTermsCondition.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContactUs: {
                Intent intent = new Intent(TemporaryActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnSettings: {
                Intent intent = new Intent(TemporaryActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnEditProfile: {
                Intent intent = new Intent(TemporaryActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnPrivacyPolicy: {
                Intent intent = new Intent(TemporaryActivity.this, CmsActivity.class);
                intent.putExtra("type", "pp");
                startActivity(intent);
            }
            break;
            case R.id.btnTermsConditions: {
                Intent intent = new Intent(TemporaryActivity.this, CmsActivity.class);
                intent.putExtra("type", "tnc");
                startActivity(intent);
            }
            break;
        }
    }
}
