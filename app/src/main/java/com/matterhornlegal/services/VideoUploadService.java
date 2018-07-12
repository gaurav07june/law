package com.matterhornlegal.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.matterhornlegal.BroadCastReceivers.UploadVideoBroadCast;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.SubmitVideoActivity;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.VideoUploadRequestModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.videoUploadModel.CreateVideoResponseModel;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.IScreen;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class VideoUploadService extends Service implements IScreen{

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private VideoUploadRequestModel videoUploadRequestModel = null;
    private final String CHANNEL_DEFAULT_IMPORTANCE = "uploadVideoChannel";
    private final int UPLOAD_VIDEO_NOTIFICATION_ID = 10;
    private String videoLink = "";
    private String uploadLinkUrl = "";
    private byte[] videoBuf = null;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    NotificationCompat.Builder builder;

    public VideoUploadService() {
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationIntent = new Intent(VideoUploadService.this, SubmitVideoActivity.class);
        pendingIntent = PendingIntent.getActivity(VideoUploadService.this, 0, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MatterhornLegal",
                    "com.matterhornlegal.LEGAL_CHANNEL",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Law related notification");
            mNotificationManager.createNotificationChannel(channel);

        } else {
            builder =  new NotificationCompat.Builder(VideoUploadService.this, CHANNEL_DEFAULT_IMPORTANCE);
        }
        builder = new NotificationCompat.Builder(VideoUploadService.this, CHANNEL_DEFAULT_IMPORTANCE);
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        videoUploadRequestModel =(VideoUploadRequestModel) intent.getSerializableExtra(AppConstants.EXTRA.EXTRA_VIDEO_UPLOAD_REQUEST_DATA);
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        if (videoUploadRequestModel != null){
            if (!videoUploadRequestModel.getFilePath().isEmpty()){
                videoBuf = getVideoByte(videoUploadRequestModel.getFilePath());
                Message msg = mServiceHandler.obtainMessage();
                msg.arg1 = startId;
                mServiceHandler.sendMessage(msg);
            }
        }else{
            stopSelf();
        }
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            showOngoingNotificatoin(getResources().getString(R.string.upload_video_notification_title),
                    getResources().getString(R.string.upload_video_notification_message), R.drawable.ic_stat_file_upload,
                    R.mipmap.ic_launcher, true);
           /* try{
                Thread.sleep(10000);
            }catch (InterruptedException e){

            }
            stopSelf();*/
           // start uploading video process
            getData(ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO);
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        switch (actionID){
            case ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO:
                if (serviceResponse instanceof CreateVideoResponseModel){
                    CreateVideoResponseModel responseModel = (CreateVideoResponseModel) serviceResponse;
                    videoLink = responseModel.getLink();
                    uploadLinkUrl = responseModel.getUpload().getUpload_link();
                    if (!uploadLinkUrl.isEmpty() || uploadLinkUrl !=null){
                        showOngoingNotificatoin(getResources().getString(R.string.upload_video_notification_title),
                                getResources().getString(R.string.uploadin_your_video), R.drawable.ic_stat_file_upload,
                                R.mipmap.ic_launcher, false);
                        Logger.error(uploadLinkUrl);
                        getData(ApiUtils.ApiActionEvents.VIMEO_VIDEO_UPLOAD);
                    }else{
                        showOngoingNotificatoin(getResources().getString(R.string.upload_video_notification_title),
                                getResources().getString(R.string.error_uploading_video), R.drawable.ic_stat_file_upload,
                                R.mipmap.ic_launcher, false);
                        stopSelf();
                    }
                    if (!status){
                        showOngoingNotificatoin(getResources().getString(R.string.upload_video_notification_title),
                                getResources().getString(R.string.error_uploading_video), R.drawable.ic_stat_file_upload,
                                R.mipmap.ic_launcher, false);
                    }
                }
                break;

            case ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER:
                if (serviceResponse instanceof BaseModel){
                    Intent intent = new Intent(VideoUploadService.this, UploadVideoBroadCast.class);
                    intent.putExtra(AppConstants.EXTRA.VIDEO_UPLOAD_BROAD_INTENT, AppConstants.commonConstants.UPLOAD_VIDEO_SUCCESS_ACTION);
                    sendBroadcast(intent);
                    showOngoingNotificatoin(getResources().getString(R.string.upload_video_notification_title),
                            getResources().getString(R.string.upload_success), R.drawable.ic_stat_file_upload,
                            R.mipmap.ic_launcher, false);
                   stopSelf();
                }
                break;
        }
    }
    @Override
    public void getData(final int actionID) {
        switch (actionID){
            case ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO:
                String createVideoPayload = ApiUtils.getCreateVideoPayload(videoUploadRequestModel.getFileSize(),
                        videoUploadRequestModel.getTitle()).toString();
                Map<String, String> createVideoHeader= ApiUtils.getVimeoCommonHeader();
                RequestManager.addRequest(new GsonObjectRequest<CreateVideoResponseModel>(
                        ApiUtils.ApiUrl.VIMEO_CREATE_VIDEO,createVideoHeader, createVideoPayload,
                        CreateVideoResponseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {
                    @Override
                    protected void deliverResponse(CreateVideoResponseModel response) {

                        String data = new String(mResponse.data);
                        Logger.error("create video response: " + data);
                        if (response != null) {
                            updateUi(true, actionID, response, 200);
                        } else {
                            updateUi(false, actionID, response, 200 );
                        }
                    }
                });
                break;
            case ApiUtils.ApiActionEvents.VIMEO_VIDEO_UPLOAD:
                Map<String, String> uploadVideoHeader = ApiUtils.getVimeoVedioUploadHeader();
                RequestManager.addRequest(new GsonObjectRequest<BaseModel>(
                        com.android.volley.Request.Method.PATCH, uploadLinkUrl, uploadVideoHeader,
                        videoBuf, BaseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {
                    @Override
                    protected void deliverResponse(BaseModel response) {
                        //show notification for upload done to vimeo
                        String data = new String(mResponse.data);
                        Logger.error("video upload response: " + data);
                        getData(ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER);
                    }
                });
                break;
            case ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER:
                String videoUploadPayload = ApiUtils.getVideoUploadPayload(
                        videoUploadRequestModel.getTitle(),
                        videoUploadRequestModel.getDescription(),
                        videoUploadRequestModel.getPracticeArea(),
                        videoUploadRequestModel.getIndustrySector(),
                        videoUploadRequestModel.getCountry(),
                        videoLink).toString();
                Map<String, String> videoUploadHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                RequestManager.addRequest(
                        new GsonObjectRequest<BaseModel>(
                                ApiUtils.ApiUrl.UPLOAD_VIDEO, videoUploadHeader, videoUploadPayload, BaseModel.class,
                                new VolleyErrorListener(this, this,  actionID)) {

                            @Override
                            protected void deliverResponse(BaseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("practice response: " + data);
                                if (response.isSuccess()) {
                                    updateUi(true, actionID, response, 200);
                                } else {
                                    if (Utils.isNullOrEmpty(response.getMessage())) {
                                        updateUi(false, actionID, getString(R.string.errorGeneric), 200);
                                    } else {
                                        updateUi(false, actionID, response.getMessage(), 200);
                                    }
                                    stopSelf();
                                }
                            }
                        });
                break;
        }

    }

    private byte[] getVideoByte(String filePath){
        byte[] buf = new byte[0];
        InputStream in = null;
        try {
            in = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException exception) {
        }
        try {
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;
        } catch (IOException exception) {
        }
        return buf;
    }
    private void showOngoingNotificatoin(String notificationTitle, String notificationMessage , int smallIcon,
                                         int largeIcon, boolean isFirstNotiCreate){
        int max = 100;
        int progress = 5;
        boolean isIndeterminant = false;
        Intent intentAction = new Intent(VideoUploadService.this, UploadVideoBroadCast.class);

        intentAction.putExtra(AppConstants.EXTRA.VIDEO_UPLOAD_BROAD_INTENT,AppConstants.commonConstants.STOP_UPLOAD_ACTION_NAME);

        PendingIntent pendingIntentAction = PendingIntent.getBroadcast(VideoUploadService.this,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), largeIcon))
                .setContentIntent(pendingIntent)
                .setProgress(max, progress, isIndeterminant)
                .setTicker(getResources().getString(R.string.upload_video_ticker_msg));

        if (isFirstNotiCreate){
            builder.addAction(R.drawable.close_icon, getResources().getString(R.string.stop_uploading), pendingIntentAction);
        }
        Notification notification = builder.build();
        startForeground(UPLOAD_VIDEO_NOTIFICATION_ID, notification);
    }
}
