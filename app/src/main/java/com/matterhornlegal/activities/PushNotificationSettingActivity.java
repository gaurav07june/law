package com.matterhornlegal.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;

/**
 * Created by seema.gurtatta on 10/24/2017.
 */
public class PushNotificationSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvNewVideo, mIvNewEvent, mIvUpcomingEvent, mIvNewContent, mIvVideoApproval;
    private Toolbar mToolbar;
    private NMGTextView mTvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notifications);
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
        mIvNewVideo = (ImageView) findViewById(R.id.pushNewVideoIv);
        mIvNewEvent = (ImageView) findViewById(R.id.pushNewEventIv);
        mIvUpcomingEvent = (ImageView) findViewById(R.id.pushUpcomingEventIv);
        mIvNewContent = (ImageView) findViewById(R.id.pushNewContentIv);
        mIvVideoApproval = (ImageView) findViewById(R.id.pushVideoApprovalIv);

        mTvTitle = (NMGTextView) findViewById(R.id.titleTv);
        mTvTitle.setText(getString(R.string.pushNotifications));

        mIvNewVideo.setOnClickListener(this);
        mIvNewEvent.setOnClickListener(this);
        mIvUpcomingEvent.setOnClickListener(this);
        mIvNewContent.setOnClickListener(this);
        mIvVideoApproval.setOnClickListener(this);

        mIvNewVideo.setSelected(true);
        mIvNewEvent.setSelected(true);
        mIvUpcomingEvent.setSelected(true);
        mIvNewContent.setSelected(true);
        mIvVideoApproval.setSelected(true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pushNewVideoIv:
                mIvNewVideo.setSelected(!mIvNewVideo.isSelected());
                break;
            case R.id.pushNewEventIv:
                mIvNewEvent.setSelected(!mIvNewEvent.isSelected());
                break;
            case R.id.pushUpcomingEventIv:
                mIvUpcomingEvent.setSelected(!mIvUpcomingEvent.isSelected());
                break;
            case R.id.pushNewContentIv:
                mIvNewContent.setSelected(!mIvNewContent.isSelected());
                break;
            case R.id.pushVideoApprovalIv:
                mIvVideoApproval.setSelected(!mIvVideoApproval.isSelected());
                break;
        }
    }
}
