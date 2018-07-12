package com.matterhornlegal.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivitySubmitEventBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.EditEventData;
import com.matterhornlegal.models.EditEventResponseModel;
import com.matterhornlegal.models.EventData;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.FragmentController;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.UploadUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;


import com.rey.material.widget.CheckBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class SubmitEventActivity extends BaseActivity implements View.OnClickListener {

    private NMGEditText mEtTitle, mEtDescription, mEtAdditionalDetails, mEtCategory, mEtAddress, mEtVenue, mEtWebsite, mEtPhoneNumber, mEtEmail, mEtRegistrationUrl;
    private NMGEditText mEtStartDate, mEtStartTime, mEtStartAmPm, mEtEndDate, mEtEndTime, mEtEndAmPm;
    private NMGButton mBtnUploadDocument, mBtnSubmitNow;
    private CheckBox mCbTerms;
    private LinearLayout mLlDate, mLlFiles;
    private ImageView mIvVenueTba, mIvDateTba;
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private TextInputLayout mILVenue;
    private RelativeLayout mRlUploadLogo;
    private ImageView mIvBanner;
    private NMGButton mBtnUploadBanner;
    private CircleImageView mIvLogo;
    private String mFileName;

    private ActivitySubmitEventBinding binding;
    private List<DataModel> locationList;
    private List<DataModel> categoryList;
    private SimpleSpinnerAdapter countrySpinnerAdapter, categorySpinnerAdapter;
    private String countryLocation = "";
    private int categoryid;
    private String evetnLogoBase64 = "";
    private Calendar dateSelectedCal;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener time;
    private int eventId;
    private String fromActivity = null;

    boolean startDateClicked, endDateClicked, starTimeClicked, endTimeClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_submit_event);
        Intent intent = getIntent();
        eventId = intent.getIntExtra(AppConstants.EXTRA.EVENT_ID, 0);
        fromActivity = intent.getStringExtra(AppConstants.EXTRA.FROM_ACTIVITY);
        setToolbar();
        initView();
        initClickListener();
        getData(ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST);

    }

    private void setToolbar() {

        setSupportActionBar(binding.layoutToolbarGray.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        binding.layoutToolbarGray.toolbar.setNavigationIcon(R.drawable.ic_back);
        actionBar.setTitle("");
        binding.layoutToolbarGray.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initClickListener(){
        binding.submitEventvenueEt.setOnClickListener(this);
        binding.submitEventCategoryEt.setOnClickListener(this);
        binding.submitEventStartDateEt.setOnClickListener(this);
        binding.submitEventUploadEventLogo.setOnClickListener(this);
        binding.submitEventStartDateEt.setOnClickListener(this);
        binding.submitEventEndDateEt.setOnClickListener(this);
        binding.submitEventStartTimeEt.setOnClickListener(this);
        binding.submitEventEndTimeEt.setOnClickListener(this);
        binding.submitEventSubmitNowBtn.setOnClickListener(this);
        binding.submitEventUpdateNowBtn.setOnClickListener(this);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateSelectedCal.set(Calendar.YEAR, year);
                dateSelectedCal.set(Calendar.MONTH, monthOfYear);
                dateSelectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Logger.error(dateSelectedCal.getTime().toString());
                updateDateUI();
            }

        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateSelectedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateSelectedCal.set(Calendar.MINUTE, minute);
                Logger.error(dateSelectedCal.getTime().toString());
                updateTimeUI(hourOfDay,  minute);
            }
        };
        binding.submitEventSpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.submitEventvenueEt.setText(locationList.get(position - 1).getName());
                   // countryLocation = Integer.toString(locationList.get(position - 1).getId());
                    countryLocation = locationList.get(position - 1).getName();
                } else {
                    binding.submitEventvenueEt.setText(countryLocation);
                    //countryLocation = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.submitEventSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.submitEventCategoryEt.setText(categoryList.get(position - 1).getName());
                    categoryid = categoryList.get(position - 1).getId();
                } else {
                    binding.submitEventCategoryEt.setText("");
                    //categoryid = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void updateDateUI(){
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(outputPattern);
        if (startDateClicked){

            binding.submitEventStartDateEt.setText(sdf.format(dateSelectedCal.getTime()));
        }else if (endDateClicked){
            binding.submitEventEndDateEt.setText(sdf.format(dateSelectedCal.getTime()));
        }

    }

    private void updateTimeUI(int hourOfDay, int minute){
        String timeString = Utils.getFormatedTime(hourOfDay, minute);
        if (starTimeClicked){
            binding.submitEventStartTimeEt.setText(timeString.split(" ")[0]);
            binding.submitEventStartAmPmEt.setText(timeString.split(" ")[1]);
        }else if (endTimeClicked){
            binding.submitEventEndTimeEt.setText(timeString.split(" ")[0]);
            binding.submitEventEndAmPmEt.setText(timeString.split(" ")[1]);
        }
    }
    private void initView() {
        startDateClicked = false;
        endDateClicked = false;
        starTimeClicked = false;
        endTimeClicked = false;
        binding.submitEventSubmitNowBtn.setVisibility(View.VISIBLE);
        binding.submitEventUpdateNowBtn.setVisibility(View.GONE);
        dateSelectedCal = Calendar.getInstance();
        binding.layoutToolbarGray.titleTv.setText(getString(R.string.submitEvent));
        setCategorySpinner(getCategoryData());

        mEtTitle = (NMGEditText) findViewById(R.id.submitEventTitleEt);
        mEtDescription = (NMGEditText) findViewById(R.id.submitEventDescriptionEt);
        mEtAdditionalDetails = (NMGEditText) findViewById(R.id.submitEventAdditionalInformationEt);
        mEtCategory = (NMGEditText) findViewById(R.id.submitEventCategoryEt);
        mEtAddress = (NMGEditText) findViewById(R.id.submitEventAddressEt);
        mEtVenue = (NMGEditText) findViewById(R.id.submitEventvenueEt);
        mEtWebsite = (NMGEditText) findViewById(R.id.submitEventWebsiteEt);
        mEtPhoneNumber = (NMGEditText) findViewById(R.id.submitEventPhoneEt);
        mEtEmail = (NMGEditText) findViewById(R.id.submitEventWEmailEt);
        mEtRegistrationUrl = (NMGEditText) findViewById(R.id.submitEventRegistrationUrlEt);

        mEtStartDate = (NMGEditText) findViewById(R.id.submitEventStartDateEt);
        mEtStartTime = (NMGEditText) findViewById(R.id.submitEventStartTimeEt);
        mEtStartAmPm = (NMGEditText) findViewById(R.id.submitEventStartAmPmEt);
        mEtEndDate = (NMGEditText) findViewById(R.id.submitEventEndDateEt);
        mEtEndTime = (NMGEditText) findViewById(R.id.submitEventEndTimeEt);
        mEtEndAmPm = (NMGEditText) findViewById(R.id.submitEventEndAmPmEt);
        mILVenue = (TextInputLayout) findViewById(R.id.submitEventVenueInputLayout);

        mBtnUploadDocument = (NMGButton) findViewById(R.id.submitEventUploadDocumentBtn);
        mBtnSubmitNow = (NMGButton) findViewById(R.id.submitEventSubmitNowBtn);

        mCbTerms = (CheckBox) findViewById(R.id.submitEventTermsCheckBox);
        mLlDate = (LinearLayout) findViewById(R.id.submitEventDateLl);
        mLlFiles = (LinearLayout) findViewById(R.id.submitEventFilesLl);
        mIvVenueTba = (ImageView) findViewById(R.id.submitEventVenueTbaIv);
        mIvDateTba = (ImageView) findViewById(R.id.submitEventDateTbaIv);
        mIvLogo = (CircleImageView) findViewById(R.id.submitEventLogoIv);
        mIvBanner = (ImageView) findViewById(R.id.submitEventBannerIv);

        mLlFiles.setVisibility(View.GONE);

        mIvVenueTba.setOnClickListener(this);
        mIvDateTba.setOnClickListener(this);

        mEtEndDate.setOnClickListener(this);
        mEtEndTime.setOnClickListener(this);
        mEtStartDate.setOnClickListener(this);
        mEtStartTime.setOnClickListener(this);
        mBtnSubmitNow.setOnClickListener(this);
        mBtnUploadDocument.setOnClickListener(this);


        mEtStartDate.setFocusableInTouchMode(false);
        mEtStartDate.setFocusable(false);
        mEtStartDate.setClickable(true);
        mEtStartTime.setFocusableInTouchMode(false);
        mEtStartTime.setFocusable(false);
        mEtStartTime.setClickable(true);
        mEtEndDate.setFocusableInTouchMode(false);
        mEtEndDate.setFocusable(false);
        mEtEndDate.setClickable(true);
        mEtEndTime.setFocusableInTouchMode(false);
        mEtEndTime.setFocusable(false);
        mEtEndTime.setClickable(true);
        if (fromActivity != null){
            if (fromActivity.equals(AppConstants.ACTIVITY_CONST.EVENT_DETAIL_ACTIVITY)){
                binding.submitEventSubmitNowBtn.setVisibility(View.GONE);
                binding.submitEventUpdateNowBtn.setVisibility(View.VISIBLE);
                binding.layoutToolbarGray.titleTv.setText(getResources().getString(R.string.update_event));
                if (eventId != 0){
                    getData(ApiUtils.ApiActionEvents.EDIT_EVENT);
                }

            }
        }
        SpannableString ss = new SpannableString(getResources().getString(R.string.byPostingEventAgreeTnC));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SubmitEventActivity.this,CmsActivity.class).putExtra("type","tnc"));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 40, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.submitEventTermsCheckBox.setText(ss);
        binding.submitEventTermsCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
        binding.submitEventTermsCheckBox.setHighlightColor(ContextCompat.getColor(SubmitEventActivity.this, R.color.colorOrange));


    }

    private List<DataModel> getCategoryData(){
        categoryList = new ArrayList<>();
        DataModel dataModel = new DataModel();
        dataModel.setName("Legal Events");
        dataModel.setId(54);
        categoryList.add(dataModel);
        return categoryList;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitEventDateTbaIv:
                mIvDateTba.setSelected(!mIvDateTba.isSelected());
                if (mIvDateTba.isSelected()) {
                    mLlDate.setVisibility(View.GONE);
                } else {
                    mLlDate.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.submitEventVenueTbaIv:
                mIvVenueTba.setSelected(!mIvVenueTba.isSelected());
                if (mIvVenueTba.isSelected()) {
                    mILVenue.setVisibility(View.GONE);
                } else {
                    mILVenue.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.submitEventUploadDocumentBtn: {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (UploadUtils.verifyStoragePermissions(SubmitEventActivity.this)) {
                        openFilePicker();
                    } else {
                        UploadUtils.requestStoragePermissions(SubmitEventActivity.this);
                    }
                } else {
                    openFilePicker();
                }

            }
            break;

            case R.id.submitEventvenueEt:
                binding.submitEventSpinnerLocation.performClick();
                break;
            case R.id.submitEventCategoryEt:
                binding.submitEventSpinnerCategory.performClick();
                break;
            case R.id.submit_event_upload_event_logo:
                showImagePicker(new PickImageListener() {
                    @Override
                    public void onImagePicked(Bitmap bitmap, String imageBase64) {
                        Logger.error(imageBase64);
                        if (!imageBase64.isEmpty() && imageBase64 != null){
                            evetnLogoBase64 ="data:image/png;base64,"+imageBase64;
                        }
                    }
                });
                break;
            case R.id.submitEventStartDateEt:
                startDateClicked = true;
                endDateClicked = false;
                DatePickerDialog datePickerDialog = new DatePickerDialog(SubmitEventActivity.this, date, dateSelectedCal
                        .get(Calendar.YEAR), dateSelectedCal.get(Calendar.MONTH),
                        dateSelectedCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.submitEventEndDateEt:
                startDateClicked = false;
                endDateClicked = true;
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(SubmitEventActivity.this, date, dateSelectedCal
                        .get(Calendar.YEAR), dateSelectedCal.get(Calendar.MONTH),
                        dateSelectedCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog1.show();
                break;
            case R.id.submitEventStartTimeEt:
                starTimeClicked = true;
                endTimeClicked = false;
                new TimePickerDialog(SubmitEventActivity.this, time,
                        dateSelectedCal.get(Calendar.HOUR_OF_DAY), dateSelectedCal.get(Calendar.MINUTE), false).show();
                break;
            case R.id.submitEventEndTimeEt:
                starTimeClicked = false;
                endTimeClicked = true;
                new TimePickerDialog(SubmitEventActivity.this, time,
                        dateSelectedCal.get(Calendar.HOUR_OF_DAY), dateSelectedCal.get(Calendar.MINUTE), false).show();
                break;
            case R.id.submitEventSubmitNowBtn:
                if (isValidInput()){
                    getData(ApiUtils.ApiActionEvents.SUBMIT_EVENT);
                }
                break;
            case R.id.submitEventUpdateNowBtn:
                if (isValidInput()){
                    getData(ApiUtils.ApiActionEvents.UPDATE_EVENT);
                }
        }
    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf|application/msword|application/docx");
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "text/plain", "application/pdf"};

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), AppConstants.INTENT_REQUEST_CODES.DOCUMENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstants.INTENT_REQUEST_CODES.DOCUMENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();

//                        if (filesize >= FILE_SIZE_LIMIT) {
//                            Toast.makeText(this,"The selected file is too large. Selet a new file with size less than 2mb",Toast.LENGTH_LONG).show();
//                        } else {
                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType == null) {
                            String path = UploadUtils.getPath(this, uri);
                            if (path == null) {
                                mFileName = uri.getLastPathSegment();
                            } else {
                                File file = new File(path);
                                mFileName = file.getName();
                            }
                        } else {
                            Uri returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            mFileName = returnCursor.getString(nameIndex);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }
                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();
                        try {
                            copyFileStream(new File(sourcePath + "/" + mFileName), uri, this);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("matterhorn", "File name is: " + mFileName);
                        Toast.makeText(SubmitEventActivity.this, "Document selected: " + mFileName, Toast.LENGTH_SHORT).show();

//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case UploadUtils.REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
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
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                RequestManager.addRequest(
                        new GsonObjectRequest<LocationListResponseModel>(
                                ApiUtils.ApiUrl.GET_LOCATIONS_LIST, null, LocationListResponseModel.class,
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
            case ApiUtils.ApiActionEvents.SUBMIT_EVENT:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }

                String title = binding.submitEventTitleEt.getText().toString().trim();
                String description = binding.submitEventDescriptionEt.getText().toString().trim();
                String shortDesc = binding.submitEventAdditionalInformationEt.getText().toString().trim();
                int categoryId = categoryid;
                String address = binding.submitEventAddressEt.getText().toString().trim();
                String country = countryLocation;
                String website = binding.submitEventWebsiteEt.getText().toString().trim();
                String phone = binding.submitEventPhoneEt.getText().toString().trim();
                String email = binding.submitEventWEmailEt.getText().toString().trim();
                String registrationUrl = binding.submitEventRegistrationUrlEt.getText().toString().trim();
                String startDate = binding.submitEventStartDateEt.getText().toString().trim();
                String enddate = binding.submitEventEndDateEt.getText().toString().trim();
                String startTime = binding.submitEventStartTimeEt.getText().toString().trim();
                String endTime = binding.submitEventEndTimeEt.getText().toString().trim();
                String startmer = binding.submitEventStartAmPmEt.getText().toString().trim();
                String endMer = binding.submitEventEndAmPmEt.getText().toString().trim();

                String submitEventPayload = ApiUtils.getSubmitEventPayload(title,description, shortDesc,categoryId, address,country,
                        website, phone, email,registrationUrl, startDate, enddate, startTime, endTime, startmer, endMer, evetnLogoBase64).toString();
                Map<String, String> submitEventheader= ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                RequestManager.addRequest(
                    new GsonObjectRequest<BaseModel>(
                            ApiUtils.ApiUrl.SUBMIT_EVENT_URL, submitEventheader, submitEventPayload, BaseModel.class,
                            new VolleyErrorListener(this, this, actionID)) {

                        @Override
                        protected void deliverResponse(BaseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("submit event response: " + data);
                            if (response.isSuccess() && response != null) {
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
            case ApiUtils.ApiActionEvents.EDIT_EVENT:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                String eventEditPayload = ApiUtils.getEventPayload(eventId).toString();
                Map<String,  String> editEventheader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                RequestManager.addRequest(
                    new GsonObjectRequest<EditEventResponseModel>(
                            ApiUtils.ApiUrl.EDIT_EVENT_URL, editEventheader, eventEditPayload, EditEventResponseModel.class,
                            new VolleyErrorListener(this, this, actionID)) {

                        @Override
                        protected void deliverResponse(EditEventResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("edit event response: " + data);
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
            case ApiUtils.ApiActionEvents.UPDATE_EVENT:
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
                String update_title = binding.submitEventTitleEt.getText().toString().trim();
                String update_description = binding.submitEventDescriptionEt.getText().toString().trim();
                String update_shortDesc = binding.submitEventAdditionalInformationEt.getText().toString().trim();
                int update_categoryId = categoryid;
                String update_address = binding.submitEventAddressEt.getText().toString().trim();
                String update_country = countryLocation;
                String update_website = binding.submitEventWebsiteEt.getText().toString().trim();
                String update_phone = binding.submitEventPhoneEt.getText().toString().trim();
                String update_email = binding.submitEventWEmailEt.getText().toString().trim();
                String registration_url = binding.submitEventRegistrationUrlEt.getText().toString().trim();
                String update_startDate = binding.submitEventStartDateEt.getText().toString().trim();
                String update_enddate = binding.submitEventEndDateEt.getText().toString().trim();
                String update_startTime = binding.submitEventStartTimeEt.getText().toString().trim();
                String update_endTime = binding.submitEventEndTimeEt.getText().toString().trim();
                String update_startmer = binding.submitEventStartAmPmEt.getText().toString().trim();
                String update_endMer = binding.submitEventEndAmPmEt.getText().toString().trim();
                String updateEventPayload = ApiUtils.getUpdateEventPayload(eventId, update_title, update_description, update_shortDesc,
                        update_categoryId, update_address, update_country, update_website, update_phone, update_email, registration_url, update_startDate,
                        update_enddate, update_startTime, update_endTime, update_startmer, update_endMer, evetnLogoBase64).toString();
                Map<String, String> updateEventHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                RequestManager.addRequest(
                    new GsonObjectRequest<BaseModel>(
                            ApiUtils.ApiUrl.UPDATE_EVENT_URL, updateEventHeader, updateEventPayload, BaseModel.class,
                            new VolleyErrorListener(this, this, actionID)) {

                        @Override
                        protected void deliverResponse(BaseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("submit event response: " + data);
                            if (response.isSuccess() && response != null) {
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
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
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
            case ApiUtils.ApiActionEvents.SUBMIT_EVENT:
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof BaseModel){
                    BaseModel responseModel = (BaseModel) serviceResponse;
                    ToastUtils.showToast(SubmitEventActivity.this, responseModel.getMessage());
                    HomeLandingActivity.isEventListUpdated = true;
                    finish();
                }
                break;
            case ApiUtils.ApiActionEvents.EDIT_EVENT:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (serviceResponse instanceof EditEventResponseModel){
                    EditEventResponseModel responseModel = (EditEventResponseModel) serviceResponse;
                    EditEventData editEventData = responseModel.getData();
                    populatePageView(editEventData);
                }
                break;
            case ApiUtils.ApiActionEvents.UPDATE_EVENT:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (serviceResponse instanceof BaseModel){
                    BaseModel responseModel = (BaseModel) serviceResponse;
                    ToastUtils.showToast(SubmitEventActivity.this, responseModel.getMessage());
                    HomeLandingActivity.isEventListUpdated = true;
                    finish();
                }
                break;
        }
    }
    private void populatePageView(EditEventData editEventData) {

        binding.submitEventTitleEt.setText(editEventData.getTitle());
        binding.submitEventDescriptionEt.setText(editEventData.getDescription());
        binding.submitEventAdditionalInformationEt.setText(editEventData.getShort_desc());
        binding.submitEventCategoryEt.setText("Legal Events");
        binding.submitEventAddressEt.setText(editEventData.getAddress());
        countryLocation = editEventData.getCountry();
        categoryid = editEventData.getCategory_id();
        binding.submitEventvenueEt.setText(countryLocation);
        binding.submitEventWebsiteEt.setText(editEventData.getWebsite());
        binding.submitEventPhoneEt.setText(editEventData.getPhone());
        binding.submitEventWEmailEt.setText(editEventData.getEmail());
        binding.submitEventRegistrationUrlEt.setText(editEventData.getRegistration_url());
        evetnLogoBase64 = "";
        binding.submitEventStartDateEt.setText(editEventData.getStart_date());
        binding.submitEventEndDateEt.setText(editEventData.getEnd_date());
        String[] startTime = editEventData.getStart_time().split(":");
        String[] endTime = editEventData.getEnd_time().split(":");
        String stime = startTime[0]+":"+startTime[1];
        String eTime = endTime[0]+":"+endTime[1];
        binding.submitEventStartTimeEt.setText(stime);
        binding.submitEventEndTimeEt.setText(eTime);
        if (editEventData.getStart_meridian().equalsIgnoreCase("am")){
            binding.submitEventStartAmPmEt.setText("AM");
        }else if (editEventData.getStart_meridian().equalsIgnoreCase("pm")){
            binding.submitEventStartAmPmEt.setText("PM");
        }
        if (editEventData.getEnd_meridian().equalsIgnoreCase("am")){
            binding.submitEventEndAmPmEt.setText("AM");
        }else if (editEventData.getEnd_meridian().equalsIgnoreCase("pm")){
            binding.submitEventEndAmPmEt.setText("PM");
        }
        binding.submitEventTermsCheckBox.setChecked(true);
    }

    private void setCountrySpinner(List<DataModel> locationList) {

        String[] countryNames = new String[locationList.size() + 1];
        countryNames[0] = "Select country";
        for (int i = 0; i < locationList.size(); i++){
            countryNames[i + 1] = locationList.get(i).getName();
        }

        countrySpinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, countryNames);
        binding.submitEventSpinnerLocation.setAdapter(countrySpinnerAdapter);

    }

    private void setCategorySpinner(List<DataModel> categoryList){
        String[] categoryName = new String[categoryList.size() + 1];
        categoryName[0] = "Select category";
        for (int i = 0; i < categoryList.size(); i++){
            categoryName[i + 1] = categoryList.get(i).getName();
        }
        categorySpinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, categoryName);
        binding.submitEventSpinnerCategory.setAdapter(categorySpinnerAdapter);
    }

    private boolean isValidInput(){
        if (binding.submitEventTitleEt.getText().toString().trim().isEmpty()){
            binding.submitEventTitleEt.setError("Please enter title");
            binding.submitEventTitleEt.requestFocus();
            return false;
        }
        if (binding.submitEventDescriptionEt.getText().toString().trim().isEmpty()){
            binding.submitEventDescriptionEt.setError("Please enter description");
            binding.submitEventDescriptionEt.requestFocus();
            return false;
        }
        if (!Utils.isValidEmail(binding.submitEventWEmailEt.getText().toString().trim())){
            binding.submitEventWEmailEt.setError("Please enter valid email address");
            binding.submitEventWEmailEt.requestFocus();
            return false;
        }
        if (binding.submitEventAddressEt.getText().toString().trim().isEmpty()){
            binding.submitEventAddressEt.setError("Please enter address");
            binding.submitEventAddressEt.requestFocus();
            return false;
        }
        if (mILVenue.getVisibility() == View.VISIBLE){
            // check for venue field
            if (binding.submitEventvenueEt.getText().toString().isEmpty()){
                binding.submitEventvenueEt.setError("Please enter venue\\/location");
                binding.submitEventvenueEt.requestFocus();
                return false;
            }
        }
        if (fromActivity != null){
            if (fromActivity.equals(AppConstants.ACTIVITY_CONST.EVENT_DETAIL_ACTIVITY)){
            }
        }else{
            if (evetnLogoBase64.isEmpty() || evetnLogoBase64.length() == 0){
                ToastUtils.showToast(SubmitEventActivity.this, "Please select an event logo");
                return false;
            }
        }
        if (mLlDate.getVisibility() == View.VISIBLE){
            // check for date fields
            if (binding.submitEventStartDateEt.getText().toString().trim().isEmpty()){
                binding.submitEventStartDateEt.setError("Please select start date");
                binding.submitEventStartDateEt.requestFocus();
                return false;
            }
            if (binding.submitEventStartTimeEt.getText().toString().trim().isEmpty()){
                binding.submitEventStartTimeEt.setError("Please select start time");
                binding.submitEventStartTimeEt.requestFocus();
                return  false;
            }
            if (binding.submitEventEndDateEt.getText().toString().trim().isEmpty()){
                binding.submitEventEndDateEt.setError("Please select end date");
                binding.submitEventEndDateEt.requestFocus();
                return false;
            }
            if (binding.submitEventEndTimeEt.getText().toString().trim().isEmpty()){
                binding.submitEventEndTimeEt.setError("Please select end time");
                binding.submitEventEndTimeEt.requestFocus();
                return false;
            }
        }
        if (binding.submitEventWebsiteEt.getText().toString().trim().isEmpty()){
            binding.submitEventWebsiteEt.setError("Please enter website");
            binding.submitEventWebsiteEt.requestFocus();
            return false;
        }
        if (!binding.submitEventTermsCheckBox.isChecked()){
            ToastUtils.showToast(SubmitEventActivity.this,"Please check the term and condition");
            return false;
        }
        return true;
    }
}