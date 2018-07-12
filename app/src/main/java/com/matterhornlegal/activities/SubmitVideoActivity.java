package com.matterhornlegal.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivitySubmitVideoBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.IndustryListResponseModel;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.models.PracticeAreaListResponseModel;
import com.matterhornlegal.models.VideoUploadRequestModel;
import com.matterhornlegal.services.VideoUploadService;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.videoUploadModel.CreateVideoResponseModel;
import com.matterhornlegal.videoUploadModel.UserQuotaResponseModel;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SubmitVideoActivity extends BaseActivity implements View.OnClickListener {
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private NMGButton mBtnUpload;
    private ActivitySubmitVideoBinding binding;
    private String fileSize;
    private String filePath;
    private long uploadVideoSize;
    private String uploadLinkUrl = "";
    private byte[] buf = null;
    private boolean countryListData, practiceAreaData, industrySectorData;
    private List<DataModel> industrySectorList;
    private List<DataModel> practiceAreaList;
    private List<DataModel> locationList;
    private SimpleSpinnerAdapter countrySpinnerAdapter, industrySectorSpinnerAdapter, practiceAreaSpinnerAdapter;
    private String industySectorId = "";
    private String practiceAreaId = "";
    private String countryLocation = "";
    private String videoLink;
    public static Intent uploadVideoServiceIntent;
    private static final int GALLERY_PERMISSION_REQ_CODE = 1001;
    private boolean isVideoSelected = false;
    private BroadcastReceiver receiver;
    private VideoUploadRequestModel videoUploadRequestModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_video);
        countryListData = false;
        practiceAreaData = false;
        industrySectorData = false;
        isVideoSelected = false;
        videoUploadRequestModel = new VideoUploadRequestModel();
        setToolbar();
        initView();
        initListener();

        // hit the location, practice area and industry name apis
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST);
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST);
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST);
    }

    private void initBroadCastReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getStringExtra(AppConstants.EXTRA.VIDEO_UPLOAD_BROAD_INTENT);
                if(action.equals(AppConstants.commonConstants.STOP_UPLOAD_ACTION_NAME)){
                    ToastUtils.showToast(context, "Upload is cancelled");
                    stopService(uploadVideoServiceIntent);
                }
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);
            }
        };
    }
    private void initListener(){
        binding.btnUploadAnotherVideo.setOnClickListener(this);
        binding.submitVideoCountryEt.setOnClickListener(this);
        binding.submitVideoIndustrySectorsEt.setOnClickListener(this);
        binding.submitVideoPracticeAreasEt.setOnClickListener(this);
        binding.submitVideoSubmitBtn.setOnClickListener(this);

        binding.videoUploadSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.submitVideoCountryEt.setText(locationList.get(position - 1).getName());
                    countryLocation = Integer.toString(locationList.get(position - 1).getId());
                } else {
                    binding.submitVideoCountryEt.setText("");
                    countryLocation = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.uploadVideoSpinnerPracticeArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.submitVideoPracticeAreasEt.setText(practiceAreaList.get(position - 1).getName());
                    practiceAreaId = Integer.toString(practiceAreaList.get(position - 1).getId());
                }else {
                    binding.submitVideoPracticeAreasEt.setText("");
                    practiceAreaId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.videoUploadSpinnerIndustrySector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.submitVideoIndustrySectorsEt.setText(industrySectorList.get(position - 1).getName());
                    industySectorId = Integer.toString(industrySectorList.get(position - 1).getId());
                }else{
                    binding.submitVideoIndustrySectorsEt.setText("");
                    industySectorId = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        mTvTitle.setText(getString(R.string.submitVideo));
        binding.lnrlayVideoUploadSuccess.setVisibility(View.GONE);
        binding.lnrlayUploadVideoMainContent.setVisibility(View.VISIBLE);
        binding.submitVideoSubmitBtn.setOnClickListener(this);
        binding.submitVideoUploadVideoBtn.setOnClickListener(this);

        SpannableString ss = new SpannableString(getResources().getString(R.string.byVideoUploadAgreeTerms));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SubmitVideoActivity.this,CmsActivity.class).putExtra("type","tnc"));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 33, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.submitVideoTermsCheckBox.setText(ss);
        binding.submitVideoTermsCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
        binding.submitVideoTermsCheckBox.setHighlightColor(ContextCompat.getColor(SubmitVideoActivity.this, R.color.colorOrange));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitVideoUploadVideoBtn:
                checkpermissionForVideoAccess();
                break;
            case R.id.submitVideoPracticeAreasEt:
                binding.uploadVideoSpinnerPracticeArea.performClick();
                break;
            case R.id.submitVideoIndustrySectorsEt:
                binding.videoUploadSpinnerIndustrySector.performClick();
                break;
            case R.id.submitVideoCountryEt:
                binding.videoUploadSpinnerCountry.performClick();
                break;
            case R.id.submitVideoSubmitBtn:
                if (isValidateInputs()){
                    if (!Utils.isNetworkEnabled(this)) {
                        ToastUtils.showToast(this, getString(R.string.errorInternet));
                        return;
                    }
                    videoUploadRequestModel.setTitle(binding.submitVideoTitleEt.getText().toString().trim());
                    videoUploadRequestModel.setDescription(binding.submitVideoDescriptionEt.getText().toString().trim());
                    videoUploadRequestModel.setPracticeArea(practiceAreaId);
                    videoUploadRequestModel.setIndustrySector(industySectorId);
                    videoUploadRequestModel.setCountry(countryLocation);
                    videoUploadRequestModel.setFilePath(filePath);
                    videoUploadRequestModel.setFileSize(uploadVideoSize);
                    uploadVideoServiceIntent = new Intent(this, VideoUploadService.class);
                    uploadVideoServiceIntent.putExtra(AppConstants.EXTRA.EXTRA_VIDEO_UPLOAD_REQUEST_DATA, videoUploadRequestModel);
                    startService(uploadVideoServiceIntent);
                    binding.submitVideoTitleEt.setText("");
                    binding.submitVideoDescriptionEt.setText("");
                    binding.submitVideoPracticeAreasEt.setText("");
                    binding.submitVideoIndustrySectorsEt.setText("");
                    binding.submitVideoCountryEt.setText("");
                    isVideoSelected = false;
                }
                break;
            case R.id.btn_upload_another_video:
                binding.submitVideoTitleEt.setText("");
                binding.submitVideoDescriptionEt.setText("");
                binding.txtVideoUrl.setText("");
                binding.submitVideoPracticeAreasEt.setText("");
                binding.submitVideoIndustrySectorsEt.setText("");
                binding.submitVideoCountryEt.setText("");
                isVideoSelected = false;
                binding.lnrlayUploadVideoMainContent.setVisibility(View.VISIBLE);
                binding.lnrlayVideoUploadSuccess.setVisibility(View.GONE);
                break;
        }
    }
    private void checkpermissionForVideoAccess(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQ_CODE);
        } else{
            Intent intent = new Intent(this, SelectVideoActivity.class);
            startActivityForResult(intent, AppConstants.INTENT_REQUEST_CODES.VIDEO_SELECT_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(AppConstants.commonConstants.UPLOAD_VIDEO_BROAD_INTENT);
        initBroadCastReceiver();
        registerReceiver(receiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case GALLERY_PERMISSION_REQ_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, SelectVideoActivity.class);
                    startActivityForResult(intent, AppConstants.INTENT_REQUEST_CODES.VIDEO_SELECT_REQUEST);
                } else {
                    Toast.makeText(this, R.string.gallery_permission_required,Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }
    private boolean isValidateInputs(){
        if (binding.submitVideoTitleEt.getText().toString().trim().isEmpty()){
            binding.submitVideoTitleEt.setError(getResources().getString(R.string.video_title_error));
            binding.submitVideoTitleEt.requestFocus();
            return false;
        }
        if (binding.submitVideoDescriptionEt.getText().toString().trim().isEmpty()){
            binding.submitVideoDescriptionEt.setError(getResources().getString(R.string.video_description_error));
            binding.submitVideoDescriptionEt.requestFocus();
            return false;
        }
        if (binding.submitVideoPracticeAreasEt.getText().toString().trim().isEmpty()){
            binding.submitVideoPracticeAreasEt.setError(getResources().getString(R.string.practice_area_error));
            binding.submitVideoPracticeAreasEt.requestFocus();
            return false;
        }
        if (binding.submitVideoIndustrySectorsEt.getText().toString().trim().isEmpty()){
            binding.submitVideoIndustrySectorsEt.setError(getResources().getString(R.string.industry_sector_error));
            binding.submitVideoIndustrySectorsEt.requestFocus();
            return false;
        }
        if (binding.submitVideoCountryEt.getText().toString().trim().isEmpty()){
            binding.submitVideoCountryEt.setError(getResources().getString(R.string.country_error));
            binding.submitVideoCountryEt.requestFocus();
            return false;
        }
        if (!isVideoSelected){
            ToastUtils.showToast(SubmitVideoActivity.this, getResources().getString(R.string.video_url_error));
            return false;
        }
       /* if (binding.txtVideoUrl.getText().toString().trim().isEmpty()){
            ToastUtils.showToast(this, getResources().getString(R.string.video_url_error));
            return false;
        }*/
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.INTENT_REQUEST_CODES.VIDEO_SELECT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // get String data from Intent
                filePath = data.getStringExtra(AppConstants.IntentConstants.VIDEO_FILE_NAME);
                fileSize = data.getStringExtra(AppConstants.IntentConstants.VIDEO_SIZE);
                uploadVideoSize = Long.parseLong(fileSize);
                // check for user quota whether there is enough space to upload the video
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
                getData(ApiUtils.ApiActionEvents.VIMEO_USER_QUOTA);
                //ToastUtils.showToast(this, uploadVideoSize + "");
            }
        }
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(this)) {
            ToastUtils.showToast(this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID){
            case ApiUtils.ApiActionEvents.VIMEO_USER_QUOTA:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                String getQuotaPayload = null;
                Map<String, String> getUserQuotaHeader = ApiUtils.getVimeoCommonHeader();
                RequestManager.addRequest(new GsonObjectRequest<UserQuotaResponseModel>(
                        Request.Method.GET, ApiUtils.ApiUrl.GET_VIMEO_USER_QUOTA,getUserQuotaHeader, getQuotaPayload,
                        UserQuotaResponseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {
                    @Override
                    protected void deliverResponse(UserQuotaResponseModel response) {
                        String data = new String(mResponse.data);
                        Logger.error("user quota response: " + data);
                        if (response != null) {
                            updateUi(true, actionID, response, 200);
                        } else {
                            ToastUtils.showToast(SubmitVideoActivity.this, "There is some error uploading video");
                        }
                    }
                });
                break;
            case ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                String createVideoPayload = ApiUtils.getCreateVideoPayload(uploadVideoSize,
                        binding.submitVideoTitleEt.getText().toString().trim()).toString();
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
                            ToastUtils.showToast(SubmitVideoActivity.this, "There is some error uploading video");
                        }
                    }
                });

                break;
            case ApiUtils.ApiActionEvents.VIMEO_VIDEO_UPLOAD:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                Map<String, String> uploadVideoHeader = ApiUtils.getVimeoVedioUploadHeader();
                RequestManager.addRequest(new GsonObjectRequest<BaseModel>(
                        com.android.volley.Request.Method.PATCH, uploadLinkUrl, uploadVideoHeader,
                        buf, BaseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {
                    @Override
                    protected void deliverResponse(BaseModel response) {
                        if (progressDialog != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                        String data = new String(mResponse.data);
                        Logger.error("video upload response: " + data);
                        getData(ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER);
                       // ToastUtils.showToast(SubmitVideoActivity.this, "Video uploaded successfully");
                    }
                });
                break;

            case ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST:
                if (progressDialog != null) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
                countryListData =false;
                RequestManager.addRequest(
                        new GsonObjectRequest<LocationListResponseModel>(
                                ApiUtils.ApiUrl.GET_VIDEO_LOCATION_LIST_URL, null, LocationListResponseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(LocationListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("location response: " + data);
                                if (response.getStatus() == 1 && response != null) {
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
            case ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                industrySectorData = false;
                RequestManager.addRequest(
                        new GsonObjectRequest<IndustryListResponseModel>(
                                ApiUtils.ApiUrl.GET_VIDEO_INDUSTRY_LIST_URL, null, IndustryListResponseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(IndustryListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("industry response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                practiceAreaData = false;
                RequestManager.addRequest(
                        new GsonObjectRequest<PracticeAreaListResponseModel>(
                                ApiUtils.ApiUrl.GET_VIDEO_PRACTICE_LIST_URL, null, PracticeAreaListResponseModel.class,
                                new VolleyErrorListener(this, this,  actionID)) {

                            @Override
                            protected void deliverResponse(PracticeAreaListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("practice response: " + data);
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
            case ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                String videoUploadPayload = ApiUtils.getVideoUploadPayload(binding.submitVideoTitleEt.getText().toString().trim(),
                        binding.submitVideoDescriptionEt.getText().toString().trim(), practiceAreaId,
                        industySectorId, countryLocation, binding.txtVideoUrl.getText().toString().trim()).toString();
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
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID){
            case ApiUtils.ApiActionEvents.VIMEO_USER_QUOTA:
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof UserQuotaResponseModel){
                    UserQuotaResponseModel responseModel = (UserQuotaResponseModel)serviceResponse;
                    long freeSpace = responseModel.getUpload_quota().getSpace().getFree();
                    if (freeSpace >= uploadVideoSize){
                        ToastUtils.showToast(SubmitVideoActivity.this, "Video is selected successfully.");
                        isVideoSelected = true;

                    }else{
                        ToastUtils.showToast(SubmitVideoActivity.this, "Video can not be uploaded");
                        isVideoSelected = false;
                    }
                }

                break;
            case ApiUtils.ApiActionEvents.VIMEO_CREATE_VIDEO:
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof CreateVideoResponseModel){
                    CreateVideoResponseModel responseModel = (CreateVideoResponseModel) serviceResponse;
                    videoLink = responseModel.getLink();
                   // ToastUtils.showToast(SubmitVideoActivity.this, videoLink);
                    binding.txtVideoUrl.setText(videoLink);
                    uploadLinkUrl = responseModel.getUpload().getUpload_link();
                    if (!uploadLinkUrl.isEmpty() || uploadLinkUrl !=null){
                        ToastUtils.showToast(SubmitVideoActivity.this, "Uploading your video.");
                        Logger.error(uploadLinkUrl);
                        getData(ApiUtils.ApiActionEvents.VIMEO_VIDEO_UPLOAD);
                    }else{
                        ToastUtils.showToast(SubmitVideoActivity.this,"Some error has occured");
                    }
                }
                break;

            case ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST:
                countryListData = true;
                if (progressDialog != null){
                    if (progressDialog.isShowing() && countryListData && practiceAreaData && industrySectorData){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof LocationListResponseModel){
                    LocationListResponseModel responseModel = (LocationListResponseModel) serviceResponse;
                    // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null){
                        locationList = responseModel.getData();
                        setCountrySpinner(locationList);
                    }
                }
                break;
            case ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST:
                practiceAreaData = true;
                if (progressDialog != null){
                    if (progressDialog.isShowing() && countryListData && practiceAreaData && industrySectorData){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof PracticeAreaListResponseModel){
                    PracticeAreaListResponseModel responseModel = (PracticeAreaListResponseModel) serviceResponse;
                    //ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null){
                        practiceAreaList = responseModel.getData();
                        setPracticeAreaSpinner(practiceAreaList);
                    }

                }
                break;
            case ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST:
                industrySectorData = true;
                if (progressDialog != null){
                    if (progressDialog.isShowing() && countryListData && practiceAreaData && industrySectorData){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof IndustryListResponseModel){
                    IndustryListResponseModel responseModel = (IndustryListResponseModel) serviceResponse;
                    // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null ){
                        industrySectorList = responseModel.getData();
                        setIndustrySectorSpinner(industrySectorList);
                    }

                }
                break;
            case ApiUtils.ApiActionEvents.UPLOAD_VIDEO_TO_SERVER:
                if (progressDialog != null){
                    if (progressDialog.isShowing() && countryListData && practiceAreaData && industrySectorData){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof BaseModel){
                    binding.lnrlayVideoUploadSuccess.setVisibility(View.VISIBLE);
                    binding.lnrlayUploadVideoMainContent.setVisibility(View.GONE);
                    binding.lnrlayVideoUploadSuccess.setOnClickListener(this);
                    ToastUtils.showToast(SubmitVideoActivity.this,  ((BaseModel) serviceResponse).getMessage());
                }
                break;
        }
    }

    private void setCountrySpinner(List<DataModel> locationList) {

        String[] countryNames = new String[locationList.size() + 1];
        countryNames[0] = "Select country";
        for (int i = 0; i < locationList.size(); i++){
            countryNames[i + 1] = locationList.get(i).getName();
        }
        countrySpinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, countryNames);
        binding.videoUploadSpinnerCountry.setAdapter(countrySpinnerAdapter);
    }
    private void setPracticeAreaSpinner(List<DataModel> practiceAreaList) {
        String[] practiceNameList= new String[practiceAreaList.size() + 1];
        practiceNameList[0]= "Select practice area";
        for (int i = 0; i < practiceAreaList.size(); i++){
            practiceNameList[i + 1] = practiceAreaList.get(i).getName();
        }
        practiceAreaSpinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, practiceNameList);
        binding.uploadVideoSpinnerPracticeArea.setAdapter(practiceAreaSpinnerAdapter);
    }
    private void setIndustrySectorSpinner(List<DataModel> industrySectorsList) {
        String[] industryNameList = new String[industrySectorsList.size() + 1];
        industryNameList[0] = "Select industry sector";
        for (int i = 0; i < industrySectorsList.size(); i++){
            industryNameList[i + 1] = industrySectorsList.get(i).getName();
        }
        industrySectorSpinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, industryNameList);
        binding.videoUploadSpinnerIndustrySector.setAdapter(industrySectorSpinnerAdapter);
    }
}