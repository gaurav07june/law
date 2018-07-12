package com.matterhornlegal.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityEventDetailBinding;

import com.matterhornlegal.databinding.ActivityEventDetailParrallaxBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.EventData;
import com.matterhornlegal.models.EventDetail;
import com.matterhornlegal.models.EventDetailResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppCommons;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.SharedPrefsUtils;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class EventDetailActivity extends BaseActivity implements View.OnClickListener{
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private int eventId;
    private EventData eventData;
    private ActivityEventDetailBinding binding;
    private LinkedHashMap<String, List<String>> categoryMap;
    private EventDetail eventDetail;
    private static final int WRITE_CALENDAR_PERMISSION_REQ_CODE = 401;
    private String eventRegistrationUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
       
        supportPostponeEnterTransition();
        eventData = (EventData) getIntent().getSerializableExtra(AppConstants.EXTRA.EVENT_DATA);
        eventId = eventData!=null ? eventData.getEvent_id() : 0;
        setToolbar();
        setListener();
        setView();
        if (eventId != 0){
            getData(ApiUtils.ApiActionEvents.GET_EVENT_DETAIL);
        }
    }
    private void setListener() {
        binding.imgShareEvent.setOnClickListener(this);
        binding.imgEditEvent.setOnClickListener(this);
        binding.imgDeleteEvent.setOnClickListener(this);
        binding.imgAddEventToCal.setOnClickListener(this);
        binding.txtEventDetailWebUrl.setOnClickListener(this);
        binding.eventDetailRegisterBtn.setOnClickListener(this);
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

    private void setView() {
        // when the event fragment is loaded, this sharedpreference is set in FragmentController
        // this is to identify if user is in myEvent fragment or event frgament
        // if user in event fragment , do not show the edit and delete image buttons
        String fromActivity = SharedPrefsUtils.getSharedPrefString(getApplicationContext(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.FROM_ACTIVITY, "");
        if (fromActivity.equals(AppConstants.ACTIVITY_CONST.EVENT_FRAGMENT)){
            binding.lnrlayEditEvent.setVisibility(View.GONE);
            binding.lnrlayDeleteEvent.setVisibility(View.GONE);
        }else{
            binding.lnrlayEditEvent.setVisibility(View.VISIBLE);
            binding.lnrlayDeleteEvent.setVisibility(View.VISIBLE);
        }
        binding.txtEventDetailTitle.setText(eventData.getEvent_title());
        binding.toolbarLayout.titleTv.setText(eventData.getEvent_title());
        //getSupportActionBar().setTitle(eventData.getEvent_title());
        String startEventDate = Utils.getFormatedDateTime(eventData.getEvent_start_date(), true);
        String endEventDate = Utils.getFormatedDateTime(eventData.getEvent_end_date(), true);
        binding.txtEventDetailDateRange.setText(String.format(getResources().getString(R.string.event_date_range), startEventDate, endEventDate));
        binding.txtEventDetailLocation.setText(eventData.getEvent_venue());
        binding.txtEventDetailWebUrl.setText("");
        binding.txtEventDetailTitle.setText("");
        binding.txtEventDetailAddInfo.setText("");

        binding.addToCalendarLay.setOnClickListener(this);
        Glide.with(getApplicationContext())
                .load(eventData.getEvent_image())
                .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.submitEventBannerIv);

    }
    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }


    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(this)) {
            ToastUtils.showToast(this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_EVENT_DETAIL:
               /* if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }*/

                String eventDetailUrl = ApiUtils.ApiUrl.GET_EVENT_DETAIL_URL+eventId;
                RequestManager.addRequest(
                        new GsonObjectRequest<EventDetailResponseModel>(
                                eventDetailUrl, null, EventDetailResponseModel.class, new VolleyErrorListener(this, this, actionID)) {
                            @Override
                            protected void deliverResponse(EventDetailResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("event detail response: " + data);
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
            case ApiUtils.ApiActionEvents.DELETE_EVENT:
                if (!progressDialog.isShowing()){
                    progressDialog.show();
                }
                Map<String, String> deleteEventHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                String deleteEventpayload = ApiUtils.getEventPayload(eventId).toString();

                RequestManager.addRequest(
                    new GsonObjectRequest<BaseModel>(
                            ApiUtils.ApiUrl.DELETE_EVENT_URL, deleteEventHeader, deleteEventpayload, BaseModel.class, new VolleyErrorListener(this, this, actionID)) {
                        @Override
                        protected void deliverResponse(BaseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("event detail response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_EVENT_DETAIL:
                /*if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/
                if (serviceResponse instanceof EventDetailResponseModel){
                    EventDetailResponseModel responseModel= (EventDetailResponseModel) serviceResponse;
                    eventDetail = responseModel.getData();
                    setPageView(eventDetail);
                }
                break;
            case ApiUtils.ApiActionEvents.DELETE_EVENT:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (serviceResponse instanceof BaseModel){
                    BaseModel responseModel = (BaseModel) serviceResponse;
                    HomeLandingActivity.isEventListUpdated = true;
                    ToastUtils.showToast(EventDetailActivity.this, responseModel.getMessage());
                    finish();
                }
        }
    }
    private void setPageView(EventDetail eventDetail){

        Glide.with(getApplicationContext())
                .load(eventDetail.getUser_thumb())
                .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                .into(binding.submitEventLogoIv);
        binding.txtEventDetailTitle.setText(eventDetail.getEvent_title());
         if (!eventDetail.getEvent_description().isEmpty() && eventDetail.getEvent_description().trim().length() != 0){
            binding.txtEventDetailDescription.setText(eventDetail.getEvent_description());
            Utils.makeTextViewResizable(binding.txtEventDetailDescription, 3,
                    getResources().getString(R.string.continue_reading), true, this);
        }
        if (!eventDetail.getEvent_short_desc().isEmpty() && eventDetail.getEvent_short_desc().trim().length() != 0){
            binding.txtEventDetailAddInfo.setText(eventDetail.getEvent_short_desc());
            Utils.makeTextViewResizable(binding.txtEventDetailAddInfo, 3, getResources().getString(R.string.continue_reading), true, this);
        }
        binding.txtEventDetailAddInfo.setText(eventDetail.getEvent_short_desc());
        binding.txtEventDetailLocation.setText(eventDetail.getEvent_venue());
        binding.txtEventDetailWebUrl.setText(eventDetail.getEvent_website_url());
        String startEventDate = Utils.getFormatedDateTime(eventDetail.getEvent_start_date(), true);
        String endEventDate = Utils.getFormatedDateTime(eventDetail.getEvent_end_date(), true);
        binding.txtEventDetailDateRange.setText(String.format(getResources().getString(R.string.event_date_range), startEventDate, endEventDate));
        String[] category = eventDetail.getEvent_category();
        int elemInGroup = 3;
        if (category.length > 0 && category != null){
            categoryMap = createGroupedMap(category, elemInGroup);
            populatePracticeAreaView(categoryMap);
        }
        eventRegistrationUrl = eventDetail.getEvent_registration_url();
    }
    private LinkedHashMap<String , List<String>> createGroupedMap(String[] category, int elemInGroup) {
        LinkedHashMap<String, List<String>> resultHashmap = new LinkedHashMap<>();
        List<String> stringArrayList = null;
        int remainingElement = category.length;
        int first = 0;
        int last = -1;
        while(remainingElement > 0){
            if (remainingElement > 3){
                stringArrayList = new ArrayList<>();
                stringArrayList.clear();
                first = last + 1;
                last = first + 2;

            }else{
                stringArrayList = new ArrayList<>();
                stringArrayList.clear();
                first = last + 1;
                last = category.length - 1;
            }
            for (int i = first; i <= last; i++){
                stringArrayList.add(category[i]);
            }
            remainingElement = remainingElement - 3;
            resultHashmap.put("map"+first, stringArrayList);
        }
        return resultHashmap;
    }
    private void populatePracticeAreaView(LinkedHashMap<String, List<String>> categoryMap) {
        for (Map.Entry<String, List<String>> entry : categoryMap.entrySet()) {
            String key = entry.getKey();

            // create the LinearLayouy
            LinearLayout parent = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            parent.setLayoutParams(params);
            parent.setOrientation(LinearLayout.HORIZONTAL);
            parent.requestLayout();
            List<String> stringList = entry.getValue();
            for (String value : stringList){
                //create all the views
                LinearLayout lnrLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.tag_tv_template, null);
                NMGTextView textView = lnrLayout.findViewById(R.id.tag_text_view);
                textView.setText(value.toString());
                // add the views to linearlayout
                parent.addView(lnrLayout);
            }


            // add the linear layout to the parent layout
            binding.lnrlayCategoryArea.addView(parent, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void checkCalendarWritePermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    WRITE_CALENDAR_PERMISSION_REQ_CODE);
        } else {
            addEventToCalendar();
        }
    }


@Override
public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
        case WRITE_CALENDAR_PERMISSION_REQ_CODE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addEventToCalendar();
        } else {
            Toast.makeText(this, R.string.calendar_write_permission_required,Toast.LENGTH_SHORT).show();
        }
        return;
        }
        }
        }
        public void addEventToCalendar()
        {
            if(eventData!=null)
            {
                AppCommons.addEventToDefaultCalendar(this,eventData);
                Toast.makeText(this, R.string.event_added_successfully,Toast.LENGTH_SHORT).show();
            }
           }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_to_calendar_lay:
                checkCalendarWritePermission();
                break;

            case R.id.img_add_event_to_cal:
                break;
            case R.id.img_delete_event:
                if (eventId != 0){
                    getData(ApiUtils.ApiActionEvents.DELETE_EVENT);
                }
                break;
            case R.id.img_edit_event:
                if (eventId != 0){
                    Intent intent = new Intent(EventDetailActivity.this, SubmitEventActivity.class);
                    intent.putExtra(AppConstants.EXTRA.EVENT_ID, eventId);
                    intent.putExtra(AppConstants.EXTRA.FROM_ACTIVITY, AppConstants.ACTIVITY_CONST.EVENT_DETAIL_ACTIVITY);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.img_share_event:

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, "MatterHornLaw Event");
                share.putExtra(Intent.EXTRA_TEXT, eventDetail.getEvent_website_url());
                startActivity(Intent.createChooser(share, "Share Event"));
                break;

            case R.id.txt_event_detail_web_url:
                if (eventDetail != null){
                    String url = eventDetail.getEvent_website_url();
                    if (!url.startsWith("http")){
                        url = "https://"+url;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try{
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }catch (ActivityNotFoundException e){
                        ToastUtils.showToast(EventDetailActivity.this, "website is not correct.");
                    }

                }
                break;
            case R.id.eventDetailRegisterBtn:
                if (eventDetail != null){
                    String url = eventDetail.getEvent_registration_url();
                    if (url != null){
                        if (!url.startsWith("http")){
                            url = "https://"+url;
                        }
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        try{
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }catch (ActivityNotFoundException e){
                            ToastUtils.showToast(EventDetailActivity.this, "website is not correct.");
                        }
                    }


                }
                break;

        }
    }
}
