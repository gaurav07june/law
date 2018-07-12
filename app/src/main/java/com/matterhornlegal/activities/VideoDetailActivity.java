package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityVideoDetailBinding;
import com.matterhornlegal.models.VideoDetailResponseModel;
import com.matterhornlegal.models.VideoFeedModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.nmgvideoviewmodule.UniversalMediaController;
import com.example.nmgvideoviewmodule.UniversalVideoView;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;


public class VideoDetailActivity extends BaseActivity implements View.OnClickListener,UniversalVideoView.VideoViewCallback, UniversalMediaController.MediaControllerCallback{
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private ActivityVideoDetailBinding binding;
    private int videoId;

    private static final String TAG = "VideoDetailActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static  String videoUrl = null;
    private VideoFeedModel videoFeedModel;
    private String videoTitle = "";

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        Intent intent = getIntent();
        videoId = Integer.parseInt(intent.getStringExtra(AppConstants.IntentConstants.VIDEO_ID));
        videoFeedModel =(VideoFeedModel) intent.getSerializableExtra(AppConstants.IntentConstants.VIDEO_DATA);

        //Toast.makeText(this, videoId+"", Toast.LENGTH_SHORT).show();
        setToolbar();
        initView();
        setListener();
        setPageView();
        //setVideoAreaSize();
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_DETAIL);
    }



    private void setListener() {
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoViewCallback(this);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSeekPosition > 0) {
                    mVideoView.seekTo(mSeekPosition);
                }
                mVideoView.start();
                mMediaController.setTitle(videoTitle);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });
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
        mTvTitle.setText(getString(R.string.videoTitle));
        mVideoLayout = findViewById(R.id.frmlay_video_layout);
        mBottomLayout = findViewById(R.id.scrview_bottom_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mMediaController.setMediaControllerCallback(this);
        mStart = (TextView) findViewById(R.id.bnt_start);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    /**
     * Set the size of the video area
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(videoUrl);
                mVideoView.requestFocus();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Logger.error("onPause UniversalVideoView callback");

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Logger.error("onStart UniversalVideoView callback");

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Logger.error("onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Logger.error("onBufferingEnd UniversalVideoView callback");

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(VideoDetailActivity.this)) {
            ToastUtils.showToast(VideoDetailActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_VIDEO_DETAIL:
               /* if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }*/
                String payload = null;
                String url = ApiUtils.ApiUrl.GET_VIDEO_DETAIL+videoId;
                RequestManager.addRequest(
                    new GsonObjectRequest<VideoDetailResponseModel>(
                            Request.Method.GET, url, payload, VideoDetailResponseModel.class, new VolleyErrorListener(this, this, actionID)) {

                        @Override
                        protected void deliverResponse(VideoDetailResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("video gallery response: " + data);
                            //Toast.makeText(LawFirmDetailActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();

                            if (response.getStatus() == 1) {
                                updateUi(true, actionID, response, 200);
                            } else {
                                if (Utils.isNullOrEmpty(response.getMessage())) {
                                    updateUi(false, actionID, getString(R.string.errorGeneric), 200);
                                } else {
                                    updateUi(false, actionID, response.getMessage(), 200);
                                }
                            }
                        }
                    });
                break;
        }
    }
    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_VIDEO_DETAIL:
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (serviceResponse instanceof VideoDetailResponseModel){
                        VideoDetailResponseModel responseModel = (VideoDetailResponseModel) serviceResponse;
                        videoFeedModel = responseModel.getData();
                        if (videoFeedModel != null){
                            if (videoFeedModel.getVideo_url() != null || videoFeedModel.getVideo_url() != ""){
                                videoUrl = videoFeedModel.getVideo_url();
                                setPageView();
                                setVideoAreaSize();
                            }
                        }
                    }
                }
                break;
        }
    }
    private void setPageView() {
        if (videoFeedModel != null){
            videoTitle = videoFeedModel.getVideo_title();
            binding.txtVideoDescription.setText(videoFeedModel.getVideo_description());
            binding.txtVideoTitle.setText(videoFeedModel.getVideo_title());
            binding.videoDetailToolbar.titleTv.setText(videoFeedModel.getVideo_title());

            Glide.with(getApplicationContext())
                    .load(videoFeedModel.getUser_thumbnail())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(this.getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.videoDetailImgUserImage);
            binding.videoDetailTxtUserName.setText(videoFeedModel.getUser_name());
            binding.videoDetailTxtCount.setText(videoFeedModel.getVideos_count());
            binding.txtUploadTime.setText(Utils.timeAgoFormate(videoFeedModel.getVideo_created()));
        }
    }

    @Override
    public void onShareClicked() {
        if (videoFeedModel.getVideo_url() != null){
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, videoFeedModel.getVideo_title());
            share.putExtra(Intent.EXTRA_TEXT, videoFeedModel.getVideo_url());
            startActivity(Intent.createChooser(share, "Share Video"));
        }

    }
}
