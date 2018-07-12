package com.matterhornlegal.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class AboutUsActivity extends AppCompatActivity {
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
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
        mTvTitle.setText(getString(R.string.aboutUs));

    }


}
