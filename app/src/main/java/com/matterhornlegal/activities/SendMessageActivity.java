package com.matterhornlegal.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.databinding.ActivitySendMessageBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.SharedPrefsUtils;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.List;
import java.util.Map;

/**
 * Created by seema.gurtatta on 10/25/2017.
 */
public class SendMessageActivity extends BaseActivity implements View.OnClickListener{


    private Toolbar mToolbar;
    private NMGButton mBtnSendMessage;
    private NMGEditText mEtFirstName, mEtLastName, mEtEmail, mEtPhone, mEtCountry, mEtContent, mEtSubject;
    private ImageView mIvCross;
    private List<DataModel> locationList;
    private String countryLocationId = "";
    private String countryName = "";
    private ActivitySendMessageBinding binding;
    private SimpleSpinnerAdapter countrySpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_message);
        initView();
    }


    private void initView() {
        mEtFirstName = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtLastName = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtEmail = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtPhone = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtCountry = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtContent = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mEtSubject = (NMGEditText) findViewById(R.id.sendMessageFirstNameEt);
        mIvCross=(ImageView)findViewById(R.id.sendMessageCrossIv);
        mBtnSendMessage=(NMGButton)findViewById(R.id.sendMessageBtn);

        mBtnSendMessage.setOnClickListener(this);
        mIvCross.setOnClickListener(this);
        binding.sendMessageCountryEt.setOnClickListener(this);
        binding.sendMessageBtn.setOnClickListener(this);

        binding.sendMessageSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.sendMessageCountryEt.setText(locationList.get(position - 1).getName());
                    countryLocationId = Integer.toString(locationList.get(position - 1).getId());
                    countryName = locationList.get(position - 1).getName();
                } else {
                    binding.sendMessageCountryEt.setText(countryName);
                    //countryLocation = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getData(ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendMessageCrossIv:
                onBackPressed();
                break;
            case R.id.sendMessageBtn:
                if (isValidForm()){
                    getData(ApiUtils.ApiActionEvents.SEND_MESSAGE);
                }
                break;
            case R.id.sendMessageCountryEt:
                binding.sendMessageSpinnerCountry.performClick();
                break;

        }
    }

    private boolean isValidForm(){
        if (!isValidEditField(binding.sendMessageFirstNameEt)){
            return false;
        }
        if (!isValidEditField(binding.sendMessageLastNameEt)){
            return false;
        }
        if (!isValidEditField(binding.sendMessageEmailEt)){
            return false;
        }
        if (!Utils.isValidEmail(binding.sendMessageEmailEt.getText().toString().trim())){
            binding.sendMessageEmailEt.setError(getResources().getString(R.string.email_address_valid_error));
            binding.sendMessageEmailEt.requestFocus();
            return false;
        }
        if (!isValidEditField(binding.sendMessagePhoneEt)){
            return false;
        }
        if (!isValidEditField(binding.sendMessageCountryEt)){
            return false;
        }
        if (!isValidEditField(binding.sendMessageSubjectEt)){
            return false;
        }
        if (!isValidEditField(binding.sendMessageContentEt)){
            return false;
        }
        if (!binding.sendMessageCaptchaLayout.captchaCbox.isChecked()){
            ToastUtils.showToast(SendMessageActivity.this, getResources().getString(R.string.captcha_error));
            return false;
        }
        return true;
    }

    private boolean isValidEditField(View view){
        NMGEditText editText = (NMGEditText) view;
        if (editText.getText().toString().trim().isEmpty()){
            editText.setError(getResources().getString(R.string.empty_field_error));
            editText.requestFocus();
            return false;
        }
        return true;
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
                 if (progressDialog != null) {
                    if (!progressDialog.isShowing()) {
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
            case ApiUtils.ApiActionEvents.SEND_MESSAGE:
                if (progressDialog != null) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
                String firstName = binding.sendMessageFirstNameEt.getText().toString().trim();
                String lastName = binding.sendMessageLastNameEt.getText().toString().trim();
                String email = binding.sendMessageEmailEt.getText().toString().trim();
                String phoneNo = binding.sendMessagePhoneEt.getText().toString().trim();
                String country = binding.sendMessageCountryEt.getText().toString().trim();
                String subject = binding.sendMessageSubjectEt.getText().toString().trim();
                String message = binding.sendMessageContentEt.getText().toString().trim();
                Map<String, String> sendMessageheader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                String sendMessagepayload = ApiUtils.getSendMessagePayload(firstName, lastName, email,phoneNo, country,
                        subject, message).toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<BaseModel>(
                                ApiUtils.ApiUrl.SEND_MESSAGE_URL, sendMessageheader,sendMessagepayload, BaseModel.class,
                                new VolleyErrorListener(this, this, actionID), new Gson()) {

                            @Override
                            protected void deliverResponse(BaseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("location response: " + data);
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
            case ApiUtils.ApiActionEvents.SEND_MESSAGE:
                if (progressDialog != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof BaseModel){
                    BaseModel responseModel = (BaseModel) serviceResponse;
                     ToastUtils.showToast(SendMessageActivity.this, responseModel.getMessage());
                    finish();
                }
                break;
        }
    }
    private void setCountrySpinner(List<DataModel> locationList) {
        //ArrayList<String> countryNames = new ArrayList<>();
        String[] countryNames = new String[locationList.size() + 1];
        countryNames[0] = "Select country";
        for (int i = 0; i < locationList.size(); i++){
            countryNames[i + 1] = locationList.get(i).getName();
        }
        countrySpinnerAdapter = new SimpleSpinnerAdapter(SendMessageActivity.this, R.layout.simple_spinner_row, countryNames);
        binding.sendMessageSpinnerCountry.setAdapter(countrySpinnerAdapter);
    }
}
