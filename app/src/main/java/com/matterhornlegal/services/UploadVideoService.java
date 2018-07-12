package com.matterhornlegal.services;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.SubmitVideoActivity;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.VideoUploadRequestModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadVideoService extends IntentService implements IScreen{
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private VideoUploadRequestModel videoUploadRequestModel;
    private static Context context;
    private String videoLink = "";
    private String uploadLinkUrl = "";
    private byte[] videoBuf;
    private static final String ACTION_UPLOAD_VIDEO = "com.matterhornlegal.services.action.UPLOAD_VIDEO";
    //private static final String ACTION_BAZ = "com.matterhornlegal.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_FILE_PATH = "com.matterhornlegal.services.extra.FILE_PATH";
    private static final String EXTRA_PARAM_FILE_SIZE = "com.matterhornlegal.services.extra.FILE_SIZE";
    private static final String EXTRA_PARAM_VIDEO_TITLE = "com.matterhornlegal.services.extra.VIDEO_TITLE";
    private static final String EXTRA_VIDEO_UPLOAD_REQUEST_DATA = "com.matterhornlegal.services.extra.UPLOAD_DATA";

    public static final String NOTIFICATION = "com.matterhornlegal.android.service.receiver";

    public UploadVideoService() {
        super("UploadVideoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUploadVideo(Context context, VideoUploadRequestModel videoUploadRequestModel) {
        Intent intent = new Intent(context, UploadVideoService.class);
        intent.setAction(ACTION_UPLOAD_VIDEO);
        intent.putExtra(EXTRA_VIDEO_UPLOAD_REQUEST_DATA, videoUploadRequestModel);
        context.startService(intent);
    }

    private void startForgroundNotification(){
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "upload_video_notification")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Matterhorn Legal")
                .setContentText("Uploading...")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setTicker("Uploading...")
                .build();

        startForeground(99999, notification);
    }
    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
   /* public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UploadVideoService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_VIDEO.equals(action)) {
                videoUploadRequestModel = (VideoUploadRequestModel) intent.getSerializableExtra(EXTRA_VIDEO_UPLOAD_REQUEST_DATA);
                handleActionUploadVideo();
            } /*else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUploadVideo() {
        startForgroundNotification();
        if (videoUploadRequestModel != null){

            if (!videoUploadRequestModel.getFilePath().isEmpty()){
                videoBuf = getVideoByte(videoUploadRequestModel.getFilePath());
                getData(ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO);
            }
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
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

    private void publishResult(){
        Intent intent = new Intent(NOTIFICATION);
        /*intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);*/
        sendBroadcast(intent);
    }


    @Override
    public void getData(final int actionID){

        switch (actionID){
            case ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO:
                // notify user for uploading video

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
                            //show notification for error
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
                        // ToastUtils.showToast(SubmitVideoActivity.this, "Video uploaded successfully");
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
                                }
                            }
                        });
                break;
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
                        Logger.error(uploadLinkUrl);
                        getData(ApiUtils.ApiActionEvents.VIMEO_VIDEO_UPLOAD);
                    }else{
                        // show error notification

                    }
                }
                break;

            case ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER:
                if (serviceResponse instanceof BaseModel){
                    // show success notification , on click of which open the upload success page
                    publishResult();
                }
                break;
        }
    }
}
