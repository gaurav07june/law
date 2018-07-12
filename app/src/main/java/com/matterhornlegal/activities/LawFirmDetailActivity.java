package com.matterhornlegal.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityHomeLandingBinding;
import com.matterhornlegal.databinding.ActivityLawFirmDetailBinding;
import com.matterhornlegal.databinding.HomeDrawerHeaderLayoutBinding;
import com.matterhornlegal.fragments.MyEventsListFragment;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.LawFirmData;
import com.matterhornlegal.models.LawFirmDetail;
import com.matterhornlegal.models.LawFirmDetailResponseModel;
import com.matterhornlegal.models.RegisteredUserDetail;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.FragmentController;
import com.matterhornlegal.utils.HomeAddVideoFirmDialog;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LawFirmDetailActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener{
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private ImageView mIvMap;
    private ActivityLawFirmDetailBinding binding;
    private int firmId;
    private LinkedHashMap<String, List<String>> practiceAreaMap;
    private LinkedHashMap<String, List<String>> industySectorMap;
    private LinkedHashMap<String, List<String>> languageMap;
    private LinkedHashMap<String, List<String>> countryMap;
    private LawFirmData lawFirmData;
    private GoogleMap myMap;
    private final int CALL_PHONE_PERMISSION_REQ_CODE = 901;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_law_firm_detail);
        supportPostponeEnterTransition();
        firmId = getIntent().getIntExtra(AppConstants.IntentConstants.FIRM_ID, 0);
        lawFirmData = (LawFirmData) getIntent().getSerializableExtra(AppConstants.IntentConstants.FIRM_DATA);
        Glide.with(getApplicationContext())
                .load(lawFirmData.getImage())
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
                .into(binding.firmDetailImage);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.firm_detail_map);
        // for the smooth scrolling of map in scrollview
        binding.firmDetailTranImageMap.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        binding.firmDetailScrollview.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        binding.firmDetailScrollview.requestDisallowInterceptTouchEvent(true);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        binding.firmDetailScrollview.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        mapFragment.getMapAsync(this);
        setToolbar();
        initView();
        setListener();
    }

    private void setListener() {
        binding.firmDetailTxtPhoneNo.setOnClickListener(this);
        binding.firmDetailTxtEmailAddress.setOnClickListener(this);
        binding.firmDetailWebAddress.setOnClickListener(this);
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

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }

    private void initView() {
        getData(ApiUtils.ApiActionEvents.GET_FIRM_DETAIL);


    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(this)) {
            ToastUtils.showToast(this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_FIRM_DETAIL:
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }


                String payload = null;
                String url = ApiUtils.ApiUrl.GET_FIRM_DETAIL+firmId;
                RequestManager.addRequest(
                        new GsonObjectRequest<LawFirmDetailResponseModel>(
                                url, payload, LawFirmDetailResponseModel.class, new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(LawFirmDetailResponseModel response) {
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
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_FIRM_DETAIL:
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof LawFirmDetailResponseModel){
                    LawFirmDetailResponseModel responseModel = (LawFirmDetailResponseModel) serviceResponse;
                    LawFirmDetail lawFirmDetail = responseModel.getData();

                    binding.toolbarLayout.titleTv.setText(lawFirmDetail.getFirm_title());
                    binding.firmDetailTxtTitle.setText(lawFirmDetail.getFirm_title());
                    binding.firmDetailTxtDescription.setText(lawFirmDetail.getFirm_description());
                    Utils.makeTextViewResizable(binding.firmDetailTxtDescription, 3,
                            getResources().getString(R.string.continue_reading), true, this);
                    double lat = 0.0, lan = 0.0;
                    if (lawFirmDetail.getLocation_lat() != null){
                        if (!lawFirmDetail.getLocation_lat().isEmpty() || lawFirmDetail.getLocation_lat().length() > 0){
                            lat = Double.parseDouble(lawFirmDetail.getLocation_lat());
                        }
                    }else{
                        lat = 0.0;
                    }
                    if (lawFirmDetail.getLocation_lng() != null){
                        if (!lawFirmDetail.getLocation_lng().isEmpty() || lawFirmDetail.getLocation_lng().length() > 0){
                            lan = Double.parseDouble(lawFirmDetail.getLocation_lng());
                        }

                    }else{
                        lan = 0.0;
                    }
                    String[] practiceAreas = lawFirmDetail.getPractice_area();
                    String[] industrySectors = lawFirmDetail.getIndustry();
                    String[] languagesArray = lawFirmDetail.getLanguage();
                    String[] countryArray = lawFirmDetail.getCountry();
                    int elemInGroup = 3;
                    if (practiceAreas.length > 0 && practiceAreas != null){
                        practiceAreaMap = createGroupedMap(practiceAreas, elemInGroup);
                        populatePracticeAreaView(practiceAreaMap);
                    }
                    if (industrySectors.length > 0 && industrySectors != null){
                        industySectorMap= createGroupedMap(industrySectors, elemInGroup);
                        populateIndustySectorView(industySectorMap);
                    }
                    if (languagesArray.length > 0 && languagesArray != null){
                        languageMap = createGroupedMap(languagesArray, elemInGroup);
                        populateLanguageMap(languageMap);
                    }

                    if (countryArray.length > 0 && countryArray != null){
                        countryMap = createGroupedMap(countryArray, elemInGroup);
                        populateCountryMap(countryMap);
                    }

                    //String mapUrl = "http://maps.google.com/maps?q="+lat+","+lan+"("+ lawFirmDetail.getLocation() + ")&iwloc=A&hl=es";
                    //String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lan+"&size=1000x400&scale=2&zoom=10&markers=color:red|scale:2|label:M|"+lat+","+lan;
                    if (myMap != null){
                        // place marker if there is some valid lat lan
                        if (lat != 0.0 && lan != 0.0){
                            LatLng eventLatLng = new LatLng(lat, lan);
                            myMap.addMarker(new MarkerOptions().position(eventLatLng)
                                    .title(lawFirmDetail.getLocation()));
                            //myMap.moveCamera(CameraUpdateFactory.newLatLng(eventLatLng));
                            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLatLng, 8.0f));
                        }

                    }
                    /*Glide.with(this)
                            .load(mapUrl)
                            .into(binding.lawFirmMapIv);*/
                    Glide.with(getApplicationContext())
                            .load(lawFirmDetail.getImage())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                            /*.listener(new RequestListener<Drawable>() {

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
                            })*/
                            .into(binding.firmDetailImage);
                    binding.firmDetailTxtLocation.setText(lawFirmDetail.getLocation());
                    binding.firmDetailTxtPhoneNo.setText(lawFirmDetail.getPhone());
                    binding.firmDetailTxtEmailAddress.setText(lawFirmDetail.getEmail());
                    binding.firmDetailWebAddress.setText(lawFirmDetail.getWebsiteUrl());
                }
                break;
        }
    }

    private void populateIndustySectorView(LinkedHashMap<String, List<String>> industySectorMap) {
        for (Map.Entry<String, List<String>> entry : industySectorMap.entrySet()) {
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
            binding.lnrlayIndustrySector.addView(parent, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void populatePracticeAreaView(LinkedHashMap<String, List<String>> practiceAreaMap) {
        for (Map.Entry<String, List<String>> entry : practiceAreaMap.entrySet()) {
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
            binding.lnrlayPracticeArea.addView(parent, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void populateLanguageMap(LinkedHashMap<String, List<String>> languageMap) {
        for (Map.Entry<String, List<String>> entry : languageMap.entrySet()) {
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
            binding.lnrlayLanguages.addView(parent, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void populateCountryMap(LinkedHashMap<String, List<String>> countryMap) {
        for (Map.Entry<String, List<String>> entry : countryMap.entrySet()) {
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
            //add the linear layout to the parent layout
            binding.lnrlayCountry.addView(parent, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private LinkedHashMap<String , List<String>> createGroupedMap(String[] practiceAreas, int elemInGroup) {
        LinkedHashMap<String, List<String>> resultHashmap = new LinkedHashMap<>();
        List<String> stringArrayList = null;
        int remainingElement = practiceAreas.length;
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
                last = practiceAreas.length - 1;
            }
            for (int i = first; i <= last; i++){
                stringArrayList.add(practiceAreas[i]);
            }
            remainingElement = remainingElement - 3;
            resultHashmap.put("map"+first, stringArrayList);
        }
        return resultHashmap;
    }

    /*for continue reading concept*/
    public void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
//                tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
//                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }
    private SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, LawFirmDetailActivity.this.getResources().getString(R.string.view_less), false);
                    } else {
                        makeTextViewResizable(tv, 3, LawFirmDetailActivity.this.getResources().getString(R.string.continue_reading), true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.firm_detail_txt_phone_no:
                checkPermissionForPhoneCall();
                break;
            case R.id.firm_detail_txt_email_address:
                String[] emailAddress = {binding.firmDetailTxtEmailAddress.getText().toString().trim()};
                if (emailAddress[0] == null || emailAddress[0].isEmpty() || emailAddress[0].equals("")){
                    return;
                }
                composeEmail(emailAddress, "");
                break;
            case R.id.firm_detail_web_address:
                String url = binding.firmDetailWebAddress.getText().toString().trim();
                if (url == null || url.isEmpty() || url.equals("")){
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (!url.startsWith("http")){
                    url = "https://"+url;
                }
                try{
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }catch (ActivityNotFoundException e){
                    ToastUtils.showToast(LawFirmDetailActivity.this, "Website is not correct.");

                }
                break;
        }
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            ToastUtils.showToast(LawFirmDetailActivity.this, "No app available to send email.");
        }
    }
    private void checkPermissionForPhoneCall() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSION_REQ_CODE);
        } else{
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.firmDetailTxtPhoneNo.getText().toString().trim())));

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case CALL_PHONE_PERMISSION_REQ_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try{
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.firmDetailTxtPhoneNo.getText().toString().trim())));
                    }catch (SecurityException e){

                    }

                } else {
                    Toast.makeText(this, R.string.Phone_call_permission_required,Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }

    public class MySpannable extends ClickableSpan {
        private boolean isUnderline = false;
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(ContextCompat.getColor(LawFirmDetailActivity.this, R.color.colorOrange));
        }
        @Override
        public void onClick(View widget) {
        }
    }
}
