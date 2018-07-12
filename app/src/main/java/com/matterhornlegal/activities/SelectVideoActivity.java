package com.matterhornlegal.activities;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.VideoPickAdapter;

import com.matterhornlegal.databinding.ActivitySelectVideoBinding;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.Map;

public class SelectVideoActivity extends BaseActivity implements VideoPickAdapter.VideoPickAdapterListener{
    private ActivitySelectVideoBinding binding;
    private Toolbar mToolbar;
    private Cursor videoCursor;
    private int videoColumnIndex;
    private VideoPickAdapter videoPickAdapter;

    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_video);
        setToolbar();
        initialization();
        //getData(ApiUtils.ApiActionEvents.DUMMY_HIT);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        actionBar.setTitle("Select Video");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initialization()
    {

        System.gc();
        String[] videoProjection = { MediaStore.Video.Media._ID,MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.SIZE };
        videoCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,videoProjection, null, null, null);

        count = videoCursor.getCount();
        videoPickAdapter = new VideoPickAdapter(videoCursor, this.getApplicationContext(), this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerview.getContext(),
                LinearLayoutManager.VERTICAL);
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
        binding.recyclerview.setAdapter(videoPickAdapter);
    }



    @Override
    public void onVideoClicked(Cursor cursor, int position) {
        System.gc();
        videoColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToPosition(position);
        String filename = cursor.getString(videoColumnIndex);
        videoColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
        cursor.moveToPosition(position);
        String videoSize = cursor.getString(videoColumnIndex);

        Intent intent = new Intent();
        intent.putExtra(AppConstants.IntentConstants.VIDEO_FILE_NAME,  filename);
        intent.putExtra(AppConstants.IntentConstants.VIDEO_SIZE, videoSize);
        setResult(RESULT_OK, intent);
        finish();
    }

}
