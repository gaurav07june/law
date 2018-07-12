package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivitySubmitVideoThanksBinding;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class SubmitVideoThanksActivity extends AppCompatActivity implements View.OnClickListener {
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private ActivitySubmitVideoThanksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_video_thanks);
        setToolbar();
        initView();
    }

    private void setToolbar() {
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

    private void initView() {
        mTvTitle = (NMGTextView) findViewById(R.id.titleTv);
        mTvTitle.setText(getString(R.string.submitVideo));
        binding.uploadVideoThanksAnotherBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadVideoThanksAnotherBtn:
                startActivity(new Intent(this, SubmitVideoActivity.class));
                finish();
                break;
        }
    }
}
