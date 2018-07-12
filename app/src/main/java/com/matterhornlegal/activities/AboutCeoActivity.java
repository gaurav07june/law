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
 * Created by seema.gurtatta on 10/26/2017.
 */
public class AboutCeoActivity extends AppCompatActivity implements View.OnClickListener{
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;

    private ImageView mIvExpandEducation, mIvExpandExperience;
    private NMGTextView mTvEducation, mTvExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ceo);
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
        mTvTitle.setText(getString(R.string.aboutCeo));

        mTvEducation=(NMGTextView)findViewById(R.id.aboutCeoEducationTv);
        mTvExperience=(NMGTextView)findViewById(R.id.aboutCeoExperienceTv);

        mIvExpandEducation=(ImageView)findViewById(R.id.aboutCeoEducationIv);
        mIvExpandExperience=(ImageView)findViewById(R.id.aboutCeoExperienceExpandIv);

        mIvExpandEducation.setOnClickListener(this);
        mIvExpandExperience.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutCeoEducationIv:
                mIvExpandEducation.setSelected(!mIvExpandEducation.isSelected());

                if(mIvExpandEducation.isSelected()){
                    mTvEducation.setVisibility(View.VISIBLE);
                }else{
                    mTvEducation.setVisibility(View.GONE);
                }
                break;
            case R.id.aboutCeoExperienceExpandIv:
                mIvExpandExperience.setSelected(!mIvExpandExperience.isSelected());

                if(mIvExpandExperience.isSelected()){
                    mTvExperience.setVisibility(View.VISIBLE);
                }else{
                    mTvExperience.setVisibility(View.GONE);
                }
                break;
        }
    }
}
