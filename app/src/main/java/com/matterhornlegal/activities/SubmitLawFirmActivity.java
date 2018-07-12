package com.matterhornlegal.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.NothingSelectedSpinnerAdapter;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.DataResponseModel;
import com.matterhornlegal.models.IndustryListResponseModel;
import com.matterhornlegal.models.LanguageListResponseModel;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.models.PracticeAreaListResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.SharedPrefsUtils;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.utils.image_utils.BitmapUtils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;
import com.rey.material.widget.CheckBox;
import com.thomashaertel.widget.MultiSpinner;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class SubmitLawFirmActivity extends BaseActivity implements View.OnClickListener {
    private NMGTextView mTvTitle;
    private Toolbar mToolbar;
    private CircleImageView mIvLawFirm;
    private RelativeLayout mRlImageUpload;
    private NMGEditText mEtLawFirmName, mEtLawFirmEmail, mEtDescription, mEtAddress, mEtContactNum, mEtWebsite;
    private NMGEditText mEtPracticeArea, mEtIndustrySector, mEtCountry, mEtLanguage, mEtHearAboutUs, mEtLinkedIn, mEtTwitter;
    private Spinner mSpinnerReference;

    private CheckBox mCbTerms;
    private NMGButton mBtnSubmit;
    private ArrayList<DataModel> mListLocation = new ArrayList<>(), mListIndustrySector = new ArrayList<>(), mListPracticeArea = new ArrayList<>();
    private ArrayList<DataModel> mLanguageList = new ArrayList<>();
    private MultiSpinner mSpinnerIndustrySectors, mSpinnerPracticeArea, mSpinnerCouuntry, mSpinnerLanguage;
    private Uri mCapturedImageURI;
    private String mImagePath=null, mImageBase64="";
    private String mSelectedPracticeAreaIds="", mSelectedIndustrySectorIds="", mSelectedLocationid = "", mSelectedLanguageId = "";
    private String[] mReferenceArray = {"Select one","Vimeo", "Google", "Twitter", "Another law firm"};
    private SimpleSpinnerAdapter referenceAdapter;
    private boolean[] languageSelectedItems, practiceAreaSelectedItems, industrySectorSelectedItems, countrySelectedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_lawfirm);
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
        mTvTitle.setText(getString(R.string.submitLawFirm));

        mIvLawFirm = (CircleImageView) findViewById(R.id.submitLawFirmLogoIv);
        mRlImageUpload = (RelativeLayout) findViewById(R.id.submitLawFirmUploadLogoRl);
        mEtLawFirmName = (NMGEditText) findViewById(R.id.submitLawFirmTitleEt);
        mEtLawFirmEmail = (NMGEditText) findViewById(R.id.submitLawFirmWEmailEt);
        mEtDescription = (NMGEditText) findViewById(R.id.submitLawFirmDescriptionEt);
        mEtAddress = (NMGEditText) findViewById(R.id.submitLawFirmAddressEt);
        mEtContactNum = (NMGEditText) findViewById(R.id.submitLawFirmContactNumberEt);
        mEtWebsite = (NMGEditText) findViewById(R.id.submitLawFirmWebsiteEt);
        mEtLinkedIn = (NMGEditText) findViewById(R.id.submitLawFirmLinkedinEt);
        mEtTwitter = (NMGEditText) findViewById(R.id.submitLawFirmTwitterEt);
        mEtPracticeArea = (NMGEditText) findViewById(R.id.submitLawFirmPracticeAreasEt);
        mEtIndustrySector = (NMGEditText) findViewById(R.id.submitLawFirmIndustrySectorsEt);
        mEtCountry = (NMGEditText) findViewById(R.id.submitLawFirmCountryEt);
        mEtLanguage = (NMGEditText) findViewById(R.id.submitLawFirmLanguageEt);
        mEtHearAboutUs = (NMGEditText) findViewById(R.id.submitLawFirmHearAboutUsEt);

        mSpinnerIndustrySectors = (MultiSpinner) findViewById(R.id.measureSpinnerIndustrySector);
        mSpinnerPracticeArea = (MultiSpinner) findViewById(R.id.measureSpinnerPracticeArea);
        mSpinnerCouuntry = (MultiSpinner) findViewById(R.id.measureSpinnerCountry);
        mSpinnerLanguage = (MultiSpinner) findViewById(R.id.measureSpinnerLanguage);
        mSpinnerReference = (Spinner) findViewById(R.id.measureSpinnerHearAboutUs);

        mCbTerms = (CheckBox) findViewById(R.id.submitLawFirmTermsCheckBox);
        mBtnSubmit = (NMGButton) findViewById(R.id.submitLawFirmSubmitBtn);
        SpannableString ss = new SpannableString(getResources().getString(R.string.byPostingFirmAgreeTnC));
        ClickableSpan clickableTermCond = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SubmitLawFirmActivity.this,CmsActivity.class).putExtra("type","tnc"));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableTermCond, 39, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCbTerms.setText(ss);
        mCbTerms.setMovementMethod(LinkMovementMethod.getInstance());
        mCbTerms.setHighlightColor(ContextCompat.getColor(SubmitLawFirmActivity.this, R.color.colorOrange));
        setListeners();
        referenceAdapter= new SimpleSpinnerAdapter(SubmitLawFirmActivity.this, R.layout.simple_spinner_row, mReferenceArray);
        mSpinnerReference.setAdapter(referenceAdapter);
        // get the saved preference values
        LanguageListResponseModel languageListResponseModel =SharedPrefsUtils.getLanguagePref(this,
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.LANGUAGE_LIST_PREF);
        LocationListResponseModel locationListResponseModel = SharedPrefsUtils.getCountryListPref(this,
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.COUNTRY_LIST_PREF);
        PracticeAreaListResponseModel practiceAreaListResponseModel = SharedPrefsUtils.getPracticeAreaPref(this,
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.PRACTICE_AREA_PREF);
        IndustryListResponseModel industryListResponseModel = SharedPrefsUtils.getIndustrySectorPref(this,
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.INDUSTRY_SECTOR_PREF);
        if (languageListResponseModel != null){
            mLanguageList = (ArrayList<DataModel>) languageListResponseModel.getData();
            setLanguageSpinner(true);
        }
        if (locationListResponseModel != null){
            mListLocation = locationListResponseModel.getData();
            setCountrySpinner(true);
        }
        if (practiceAreaListResponseModel != null){
            mListPracticeArea = practiceAreaListResponseModel.getData();
            setPracticeAreaSpinner(true);
        }
        if (industryListResponseModel != null){
            mListIndustrySector = industryListResponseModel.getData();
            setIndustrySectorSpinner(true);
        }
        getData(ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST);
        getData(ApiUtils.ApiActionEvents.GET_LANGUAGE);
        getData(ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS);
        getData(ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST);
    }

    private void setListeners() {
        mEtPracticeArea.setOnClickListener(this);
        mEtIndustrySector.setOnClickListener(this);
        mEtCountry.setOnClickListener(this);
        mEtLanguage.setOnClickListener(this);
        mEtHearAboutUs.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mRlImageUpload.setOnClickListener(this);


        /*mSpinnerCouuntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mEtCountry.setText(mListLocation.get(position - 1).getName());
                } else {
                    mEtCountry.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        mSpinnerReference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mEtHearAboutUs.setText(mReferenceArray[position]);
                } else {
                    mEtHearAboutUs.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setCountrySpinner(boolean isFistFill) {
        ArrayList<String> countryNames = new ArrayList<>();
        for (DataModel data : mListLocation) {
            countryNames.add(data.getName());
        }
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>
                (SubmitLawFirmActivity.this, android.R.layout.simple_spinner_item, countryNames);

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (isFistFill){
            countrySelectedItems = new boolean[locationAdapter.getCount()];

            for (int i = 0; i < countrySelectedItems.length; i++) {
                countrySelectedItems[i] = false;
            }
        }

        mSpinnerCouuntry.setAdapter(locationAdapter, false, onLocationSelectedListener);
        if (countrySelectedItems != null){
            mSpinnerCouuntry.setSelected(countrySelectedItems);
        }
    }

    private void setLanguageSpinner(boolean isFirstFill) {
        ArrayList<String> languageName = new ArrayList<>();
        for (DataModel data : mLanguageList) {
            languageName.add(data.getName());
        }
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>
                (SubmitLawFirmActivity.this, android.R.layout.simple_spinner_item, languageName);

        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (isFirstFill) {
            languageSelectedItems = new boolean[languageAdapter.getCount()];
            for (int i = 0; i < languageSelectedItems.length; i++) {
                languageSelectedItems[i] = false;
            }
        }
        mSpinnerLanguage.setAdapter(languageAdapter, false, onLanguageSelectedListener);
        if (languageSelectedItems != null){
            mSpinnerLanguage.setSelected(languageSelectedItems);
        }

    }

    private void setPracticeAreaSpinner(boolean isFirstFill) {
        ArrayList<String> practiecAreas = new ArrayList<>();
        for (DataModel data : mListPracticeArea) {
            practiecAreas.add(data.getName());
        }
        ArrayAdapter<String> practiecAreaAdapter = new ArrayAdapter<>
                (SubmitLawFirmActivity.this, android.R.layout.simple_spinner_item, practiecAreas);

        practiecAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (isFirstFill){
            practiceAreaSelectedItems = new boolean[practiecAreaAdapter.getCount()];

            for (int i = 0; i < practiceAreaSelectedItems.length; i++) {
                practiceAreaSelectedItems[i] = false;
            }
        }

        mSpinnerPracticeArea.setAdapter(practiecAreaAdapter, false, onPracticeAreaSelectedListener);
        if (practiceAreaSelectedItems != null){
            mSpinnerPracticeArea.setSelected(practiceAreaSelectedItems);
        }

    }


    private void setIndustrySectorSpinner(boolean isFirstFill) {
        ArrayList<String> industrySector = new ArrayList<>();
        for (DataModel data : mListIndustrySector) {
            industrySector.add(data.getName());
        }
        ArrayAdapter<String> industrySectorAdapter = new ArrayAdapter<>
                (SubmitLawFirmActivity.this, android.R.layout.simple_spinner_item, industrySector);

        industrySectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (isFirstFill){
            industrySectorSelectedItems = new boolean[industrySectorAdapter.getCount()];

            for (int i = 0; i < industrySectorSelectedItems.length; i++) {
                industrySectorSelectedItems[i] = false;
            }
        }

        mSpinnerIndustrySectors.setAdapter(industrySectorAdapter, false, onIndustrySectorSelectedListener);
        if (industrySectorSelectedItems != null){
            mSpinnerIndustrySectors.setSelected(industrySectorSelectedItems);
        }
    }

    private MultiSpinner.MultiSpinnerListener onPracticeAreaSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String practiceArea = "";
            mSelectedPracticeAreaIds="";
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    practiceAreaSelectedItems[i] = true;
                    if (Utils.isNullOrEmpty(practiceArea)) {
                        practiceArea += mListPracticeArea.get(i).getName();
                        mSelectedPracticeAreaIds+=mListPracticeArea.get(i).getId();
                    } else {
                        practiceArea += "," + mListPracticeArea.get(i).getName();
                        mSelectedPracticeAreaIds+=","+mListPracticeArea.get(i).getId();
                    }
                }
            }
            mEtPracticeArea.setText(practiceArea);
        }
    };

    private MultiSpinner.MultiSpinnerListener onLocationSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String locationName = "";
            mSelectedLocationid="";
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    countrySelectedItems[i] = true;
                    if (Utils.isNullOrEmpty(locationName)) {
                        locationName += mListLocation.get(i).getName();
                        mSelectedLocationid+=mListLocation.get(i).getId();
                    } else {
                        locationName += "," + mListLocation.get(i).getName();
                        mSelectedLocationid+=","+mListLocation.get(i).getId();
                    }
                }
            }
            mEtCountry.setText(locationName);
        }
    };
    private MultiSpinner.MultiSpinnerListener onLanguageSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String languageName = "";
            mSelectedLanguageId="";
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    languageSelectedItems[i] = true;
                    if (Utils.isNullOrEmpty(languageName)) {
                        languageName += mLanguageList.get(i).getName();
                        mSelectedLanguageId+=mLanguageList.get(i).getId();
                    } else {
                        languageName += "," + mLanguageList.get(i).getName();
                        mSelectedLanguageId+=","+mLanguageList.get(i).getId();
                    }
                }
            }
            mEtLanguage.setText(languageName);
        }
    };

    private MultiSpinner.MultiSpinnerListener onIndustrySectorSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String industrySector = "";
            mSelectedIndustrySectorIds="";
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    industrySectorSelectedItems[i] = true;
                    if (Utils.isNullOrEmpty(industrySector)) {
                        industrySector += mListIndustrySector.get(i).getName();
                        mSelectedIndustrySectorIds+=mListIndustrySector.get(i).getId();
                    } else {
                        industrySector += "," + mListIndustrySector.get(i).getName();
                        mSelectedIndustrySectorIds+=","+mListIndustrySector.get(i).getId();
                    }
                }
            }
            mEtIndustrySector.setText(industrySector);
        }
    };


    private boolean validateData(){
        if(Utils.isNullOrEmpty(mEtLawFirmName.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm title",Toast.LENGTH_SHORT);
            return false;
        }
       /* if(Utils.isNullOrEmpty(mEtLawFirmEmail.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm Email Id",Toast.LENGTH_SHORT);
            return false;
        }*/
       if (!Utils.isNullOrEmpty(mEtLawFirmEmail.getText().toString())){
           if (!Utils.isValidEmail(mEtLawFirmEmail.getText().toString().trim())){
               ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter valid Law Firm Email Id",Toast.LENGTH_SHORT);
               return false;
           }
       }

        if(Utils.isNullOrEmpty(mEtDescription.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm Description",Toast.LENGTH_SHORT);
            return false;
        }
       /* if(Utils.isNullOrEmpty(mSelectedPracticeAreaIds)){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please select Law Firm Practice Areas",Toast.LENGTH_SHORT);
            return false;
        }*/
        /*if(Utils.isNullOrEmpty(mSelectedIndustrySectorIds)){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please select Law Firm Industry Sectors",Toast.LENGTH_SHORT);
            return false;
        }*/

        if(Utils.isNullOrEmpty(mEtAddress.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm Address",Toast.LENGTH_SHORT);
            return false;
        }
        /*if(Utils.isNullOrEmpty(mEtCountry.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please select Law Firm Country",Toast.LENGTH_SHORT);
            return false;
        }*/
        if(Utils.isNullOrEmpty(mEtContactNum.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm Contact Number",Toast.LENGTH_SHORT);
            return false;
        }
        if(Utils.isNullOrEmpty(mEtWebsite.getText().toString())){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please enter Law Firm Website",Toast.LENGTH_SHORT);
            return false;
        }

        if(!mCbTerms.isChecked()){
            ToastUtils.showToast(SubmitLawFirmActivity.this,"Please accept Terms and Conditions",Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitLawFirmSubmitBtn:
                if(validateData()){
                    getData(ApiUtils.ApiActionEvents.SUBMIT_LAW_FIRM);
                }
                break;
            case R.id.submitLawFirmCountryEt:
                mSpinnerCouuntry.performClick();
                break;
            case R.id.submitLawFirmHearAboutUsEt:
                mSpinnerReference.performClick();
                break;

            case R.id.submitLawFirmPracticeAreasEt:
                mSpinnerPracticeArea.performClick();
                break;
            case R.id.submitLawFirmIndustrySectorsEt:
                mSpinnerIndustrySectors.performClick();
                break;
            case R.id.submitLawFirmLanguageEt:
                mSpinnerLanguage.performClick();
                break;
            case R.id.submitLawFirmUploadLogoRl:
                showImagePicker(new PickImageListener() {
                    @Override
                    public void onImagePicked(Bitmap bitmap, String imageBase64) {
                        mIvLawFirm.setImageBitmap(bitmap);
                        if (!imageBase64.isEmpty()){
                            mImageBase64 = "data:image/png;base64,"+imageBase64;
                            Logger.error(mImageBase64);
                        }

                        //Toast.makeText(SubmitLawFirmActivity.this, imageBase64, Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(SubmitLawFirmActivity.this)) {
            ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                /*if (!progressDialog.isShowing() && progressDialog != null){
                    progressDialog.show();
                }*/

                RequestManager.addRequest(
                        new GsonObjectRequest<LocationListResponseModel>(
                                ApiUtils.ApiUrl.GET_LOCATIONS_LIST, null, LocationListResponseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(LocationListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("location response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_LANGUAGE:
                RequestManager.addRequest(
                        new GsonObjectRequest<LanguageListResponseModel>(
                                ApiUtils.ApiUrl.GET_LANGUAGE_URL, null, LanguageListResponseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(LanguageListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("language response: " + data);
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

            case ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST:
                RequestManager.addRequest(
                        new GsonObjectRequest<IndustryListResponseModel>(
                                ApiUtils.ApiUrl.GET_INDUSTRY_SECTORS_LIST, null, IndustryListResponseModel.class,
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
            case ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS:
                RequestManager.addRequest(
                        new GsonObjectRequest<PracticeAreaListResponseModel>(
                                ApiUtils.ApiUrl.GET_PRACTICE_AREAS_URL, null, PracticeAreaListResponseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

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
            case ApiUtils.ApiActionEvents.SUBMIT_LAW_FIRM:
                if (!progressDialog.isShowing() && progressDialog != null){
                    progressDialog.show();
                }
                Map<String, String> submitLawFirmHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());

                String title = mEtLawFirmName.getText().toString().trim();
                String email = mEtLawFirmEmail.getText().toString().trim();
                String phone = mEtContactNum.getText().toString().trim();
                String websiteUrl = mEtWebsite.getText().toString().toString().trim();
                String address = mEtAddress.getText().toString().trim();
                String country = mSelectedLocationid;
                String description = mEtDescription.getText().toString().trim();
                String practice = mSelectedPracticeAreaIds;
                String industry = mSelectedIndustrySectorIds;
                String languages = mSelectedLanguageId;
                String linkedIn = mEtLinkedIn.getText().toString().trim();
                String twitter = mEtTwitter.getText().toString().trim();
                String reference = mEtHearAboutUs.getText().toString().trim();


                String submitLawFirmPayload = ApiUtils.getSubmitLawFirmPayload(title, email, phone, websiteUrl, address, country,
                        description, practice, industry, linkedIn, twitter, reference, mImageBase64, languages).toString();

                RequestManager.addRequest(new GsonObjectRequest<BaseModel>(
                        Request.Method.POST, ApiUtils.ApiUrl.SUBMIT_LAW_FIRM, submitLawFirmHeader,
                        submitLawFirmPayload, BaseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {

                            @Override
                            protected void deliverResponse(BaseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("submit law firm response: " + data);
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
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                if (status) {
                    if (serviceResponse instanceof LocationListResponseModel) {
                        LocationListResponseModel baseModel = (LocationListResponseModel) serviceResponse;
                       // ToastUtils.showToast(SubmitLawFirmActivity.this, baseModel.getMessage());
                        mListLocation.clear();
                        mListLocation.addAll(baseModel.getData());
                        if (baseModel != null){
                            SharedPrefsUtils.setCountryListPref(SubmitLawFirmActivity.this,
                                    AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.COUNTRY_LIST_PREF, baseModel);
                            setCountrySpinner(false);
                        }


                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST:
                if (status) {
                    if (serviceResponse instanceof IndustryListResponseModel) {
                        IndustryListResponseModel baseModel = (IndustryListResponseModel) serviceResponse;
                        //ToastUtils.showToast(SubmitLawFirmActivity.this, baseModel.getMessage());
                        mListIndustrySector.clear();
                        mListIndustrySector.addAll(baseModel.getData());
                        if (baseModel != null){
                            SharedPrefsUtils.setIndustrySectorPref(SubmitLawFirmActivity.this, AppConstants.Prefrences.PREF_NAME,
                                    AppConstants.Prefrences.INDUSTRY_SECTOR_PREF, baseModel);
                            setIndustrySectorSpinner(false);
                        }


                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.GET_LANGUAGE:
                if (status) {
                    if (serviceResponse instanceof LanguageListResponseModel) {
                        LanguageListResponseModel baseModel = (LanguageListResponseModel) serviceResponse;
                        //ToastUtils.showToast(SubmitLawFirmActivity.this, baseModel.getMessage());
                        mLanguageList.clear();
                        mLanguageList.addAll(baseModel.getData());
                        if (baseModel != null){
                            SharedPrefsUtils.setLanguagePref(SubmitLawFirmActivity.this, AppConstants.Prefrences.PREF_NAME,
                                    AppConstants.Prefrences.LANGUAGE_LIST_PREF, baseModel);
                            setLanguageSpinner(false);
                        }


                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS:
               /* if (progressDialog.isShowing() && progressDialog != null){
                    progressDialog.dismiss();
                }*/

                if (status) {
                    if (serviceResponse instanceof PracticeAreaListResponseModel) {
                        PracticeAreaListResponseModel baseModel = (PracticeAreaListResponseModel) serviceResponse;
                        //ToastUtils.showToast(SubmitLawFirmActivity.this, baseModel.getMessage());
                        mListPracticeArea.clear();
                        mListPracticeArea.addAll(baseModel.getData());
                        if (baseModel != null){
                            SharedPrefsUtils.setPracticeAreaPref(SubmitLawFirmActivity.this, AppConstants.Prefrences.PREF_NAME,
                                    AppConstants.Prefrences.PRACTICE_AREA_PREF, baseModel);
                            setPracticeAreaSpinner(false);
                        }

                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.SUBMIT_LAW_FIRM:
                if (progressDialog.isShowing() && progressDialog != null){
                    progressDialog.dismiss();
                }

                if (status) {
                    if (serviceResponse instanceof BaseModel) {
                        BaseModel baseModel = (BaseModel) serviceResponse;
                        ToastUtils.showToast(SubmitLawFirmActivity.this, baseModel.getMessage());
                        finish();
                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SubmitLawFirmActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}
