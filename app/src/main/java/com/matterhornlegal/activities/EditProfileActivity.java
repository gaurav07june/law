package com.matterhornlegal.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityEditProfileBinding;
import com.matterhornlegal.models.AppGlobalData;

import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.ChangeProfileResponseModel;
import com.matterhornlegal.models.EditProfileResponseModel;

import com.matterhornlegal.models.ProfilePicData;
import com.matterhornlegal.models.RegisteredUserDetail;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.Map;


public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private ActivityEditProfileBinding binding;
    private Toolbar mToolbar;
    private NMGTextView mTvTitle;
    private RegisteredUserDetail registeredUserDetail;
    private String imageBase64;
    private SimpleSpinnerAdapter occupationAdapter;
    private  SimpleSpinnerAdapter interestedAreaAdapter;
    private Bitmap userImageBitmap;
    private String userImageBase64;


    private String[] occupationList = {"Select occupation","Accountant", "Lawyer", "General Counsel", "Forensic Accountant"};
    private String[] interestedAreaList = {"Select interested area", "Law Videos", "Conferences", "Marketing", "Law Firms"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        //registeredUserDetail = AppGlobalData.getInstance().getRegisteredUserDetail();
        //binding.setUserDetail(registeredUserDetail);
        setToolbar();
        initView();
        setListeners();

        getData(ApiUtils.ApiActionEvents.EDIT_PROFILE);
    }

    private void setListeners() {
        binding.signUpUserImage.setOnClickListener(this);
        binding.editProfileInterestedAreasEt.setOnClickListener(this);
        binding.editProfileOccupationEt.setOnClickListener(this);
        binding.editProfileBtn.setOnClickListener(this);

        binding.editProfileSpinnerInterestedArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.editProfileInterestedAreasEt.setText(binding.editProfileSpinnerInterestedArea.getSelectedItem().toString());

                } else {
                    binding.editProfileInterestedAreasEt.setText("");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.editProfileSpinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.editProfileOccupationEt.setText(binding.editProfileSpinnerOccupation.getSelectedItem().toString());
                }else{
                    binding.editProfileOccupationEt.setText("");
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
        mTvTitle.setText(getString(R.string.editProfile));
        occupationAdapter = new SimpleSpinnerAdapter(this,R.layout.simple_spinner_row,  occupationList);
        binding.editProfileSpinnerOccupation.setAdapter(occupationAdapter);
        interestedAreaAdapter = new SimpleSpinnerAdapter(this, R.layout.simple_spinner_row, interestedAreaList);
        binding.editProfileSpinnerInterestedArea.setAdapter(interestedAreaAdapter);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_user_image:
                showImagePicker(new PickImageListener() {
                    @Override
                    public void onImagePicked(Bitmap bitmap, String imageBase64) {
                        userImageBitmap = bitmap;
                        userImageBase64 = imageBase64;
                        Glide.with(getApplicationContext()).clear(binding.imgUserProfile);
                        binding.imgUserProfile.setImageBitmap(bitmap);
                        Logger.error(imageBase64);
                        getData(ApiUtils.ApiActionEvents.UPLOAD_PROFILE_PIC);
                    }
                });
                break;
            case R.id.editProfileInterestedAreasEt:
                binding.editProfileSpinnerInterestedArea.performClick();
                break;
            case R.id.editProfileOccupationEt:
                binding.editProfileSpinnerOccupation.performClick();
                break;
            case R.id.editProfileBtn:
                if (isAllFieldsValid()){
                    getData(ApiUtils.ApiActionEvents.UPDATE_PROFILE);
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(EditProfileActivity.this, AppGlobalData.getInstance().getRegisteredUserDetail().getProfile_pic(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(EditProfileActivity.this)) {
            ToastUtils.showToast(EditProfileActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.EDIT_PROFILE:
                //showProgressDialog(getString(R.string.pleaseWait));
                if (!progressDialog.isShowing() && progressDialog != null){
                    progressDialog.show();
                }
                String payload = null;
                Map<String, String> editProfileHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(), AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                Logger.error(editProfileHeader.toString());
                RequestManager.addRequest(new GsonObjectRequest<EditProfileResponseModel>(
                    Request.Method.POST, ApiUtils.ApiUrl.EDIT_PROFILE_URL, editProfileHeader,
                        payload, EditProfileResponseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {

                        @Override
                        protected void deliverResponse(EditProfileResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("edit profile response: " + data);
                           // Toast.makeText(EditProfileActivity.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                            if (response.getStatus() == 1 && response.getData() != null) {
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
            case ApiUtils.ApiActionEvents.UPDATE_PROFILE:
                if (!progressDialog.isShowing() && progressDialog != null){
                    progressDialog.show();
                }
                String updateProfilePayload = ApiUtils.updateProfilePayload(binding.editProfileFirstNameEt.getText().toString().trim(),
                        binding.editProfileLastNameEt.getText().toString().trim(), binding.editProfileEmailEt.getText().toString().trim(),
                        binding.editProfileAddressEt.getText().toString().trim(), binding.editProfilePhoneEt.getText().toString().trim()).toString();

                Map<String, String> UpdateProfileHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(), AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());

                RequestManager.addRequest(new GsonObjectRequest<BaseModel>(
                        Request.Method.POST, ApiUtils.ApiUrl.UPDATE_PROFILE_URL, UpdateProfileHeader,
                        updateProfilePayload, BaseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {

                    @Override
                    protected void deliverResponse(BaseModel response) {
                        String data = new String(mResponse.data);
                        Logger.error("edit profile response: " + data);
                       // Toast.makeText(EditProfileActivity.this, response.getMessage(),Toast.LENGTH_SHORT).show();
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

            case ApiUtils.ApiActionEvents.UPLOAD_PROFILE_PIC:
                if (!progressDialog.isShowing() && progressDialog != null){
                    progressDialog.show();
                }

                String updateProfilePicPayload = ApiUtils.getUploadProfilePicturePayload("data:image/png;base64,"+userImageBase64).toString();
                Map<String, String> updateProfilePicHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(), AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());

                RequestManager.addRequest(new GsonObjectRequest<ChangeProfileResponseModel>(
                        Request.Method.POST, ApiUtils.ApiUrl.UPDATE_PROFILE_PIC, updateProfilePicHeader,
                        updateProfilePicPayload, ChangeProfileResponseModel.class, new VolleyErrorListener(this, this, actionID), new Gson()) {

                    @Override
                    protected void deliverResponse(ChangeProfileResponseModel response) {
                        String data = new String(mResponse.data);
                        Logger.error("update profile pic response: " + data);
                       // Toast.makeText(EditProfileActivity.this, response.getMessage(),Toast.LENGTH_SHORT).show();
                        if (response.getStatus() == 1 && response.getData() != null) {
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
            case ApiUtils.ApiActionEvents.EDIT_PROFILE:
                //removeProgressDialog();
                if (progressDialog.isShowing() && progressDialog != null){
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof EditProfileResponseModel) {
                        EditProfileResponseModel responseModel = (EditProfileResponseModel) serviceResponse;
                        registeredUserDetail = responseModel.getData();
                        populateProfileView();
                        /*AppGlobalData.getInstance().setRegisteredUserDetail(responseModel.getData());
                        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());*/
                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(EditProfileActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(EditProfileActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.UPDATE_PROFILE:
                if (progressDialog.isShowing() && progressDialog != null){
                    progressDialog.dismiss();
                }
                // save the updated data in the user's preference
                RegisteredUserDetail registeredUserDetail =AppGlobalData.getInstance().getRegisteredUserDetail();
                registeredUserDetail.setFirst_name(binding.editProfileFirstNameEt.getText().toString().trim());
                registeredUserDetail.setLast_name(binding.editProfileLastNameEt.getText().toString().trim());
                registeredUserDetail.setEmail(binding.editProfileEmailEt.getText().toString().trim());
                registeredUserDetail.setPhone(binding.editProfilePhoneEt.getText().toString().trim());
                registeredUserDetail.setAddress(binding.editProfileAddressEt.getText().toString().trim());
                registeredUserDetail.setOccupation(binding.editProfileOccupationEt.getText().toString().trim());
                registeredUserDetail.setAreas_of_interest(binding.editProfileInterestedAreasEt.getText().toString().trim());
                registeredUserDetail.setProfileUpdated(true);
                AppGlobalData.getInstance().setGuest(false);
                AppGlobalData.getInstance().setRegisteredUserDetail(registeredUserDetail);
                AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                finish();
                break;
            case ApiUtils.ApiActionEvents.UPLOAD_PROFILE_PIC:
                if (progressDialog.isShowing() && progressDialog != null){
                    progressDialog.dismiss();
                }
                if (serviceResponse instanceof ChangeProfileResponseModel){
                    ChangeProfileResponseModel responseModel = (ChangeProfileResponseModel) serviceResponse;
                    ProfilePicData profilePicData = responseModel.getData();

                    RegisteredUserDetail registeredUserDetail1 = AppGlobalData.getInstance().getRegisteredUserDetail();
                    registeredUserDetail1.setProfileUpdated(true);
                    registeredUserDetail1.setProfile_pic(profilePicData.getProfile_pic());
                    AppGlobalData.getInstance().setRegisteredUserDetail(registeredUserDetail1);
                    AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                }
                break;
        }

    }
    private void populateProfileView() {
      Glide.with(getApplicationContext())
                .load(registeredUserDetail.getProfile_pic())
                .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                .into(binding.imgUserProfile);

        binding.editProfileFirstNameEt.setText(registeredUserDetail.getFirst_name());
        binding.editProfileLastNameEt.setText(registeredUserDetail.getLast_name());
        binding.editProfileEmailEt.setText(registeredUserDetail.getEmail());
        binding.editProfileAddressEt.setText(registeredUserDetail.getAddress());
        binding.editProfilePhoneEt.setText(registeredUserDetail.getPhone());
        binding.editProfileOccupationEt.setText(registeredUserDetail.getOccupation());
        binding.editProfileInterestedAreasEt.setText(registeredUserDetail.getAreas_of_interest());
    }

    private boolean isAllFieldsValid(){

        if (binding.editProfileFirstNameEt.getText().toString().trim().isEmpty()){
            binding.editProfileFirstNameEt.setError(getResources().getString(R.string.first_name_empty_error));
            binding.editProfileFirstNameEt.requestFocus();
            return false;
        }
        if (binding.editProfileLastNameEt.getText().toString().trim().isEmpty()){
            binding.editProfileLastNameEt.setError(getResources().getString(R.string.last_name_empty_error));
            binding.editProfileLastNameEt.requestFocus();
            return false;
        }
        if (binding.editProfileEmailEt.getText().toString().trim().isEmpty()){
            binding.editProfileEmailEt.requestFocus();
            binding.editProfileEmailEt.setError(getResources().getString(R.string.email_address_empty_error));
            return false;
        }
        if (!Utils.isValidEmail(binding.editProfileEmailEt.getText().toString().trim())){
            binding.editProfileEmailEt.setError(getResources().getString(R.string.email_address_valid_error));
            binding.editProfileEmailEt.setText("");
            binding.editProfileEmailEt.requestFocus();
            return false;
        }
        if (binding.editProfileAddressEt.getText().toString().trim().isEmpty()){
            binding.editProfileAddressEt.setError(getResources().getString(R.string.address_empty_error));
            binding.editProfileAddressEt.requestFocus();
            return false;
        }
        if (binding.editProfilePhoneEt.getText().toString().trim().isEmpty()){
            binding.editProfilePhoneEt.setError(getResources().getString(R.string.phone_empty_error));
            binding.editProfilePhoneEt.requestFocus();
            return false;
        }
        /*if (Utils.isValidatePhoneNumber(binding.editProfilePhoneEt.getText().toString().trim())){
            binding.editProfilePhoneEt.setError(getResources().getString(R.string.valid_phone_error));
            binding.editProfilePhoneEt.setText("");
            binding.editProfilePhoneEt.requestFocus();
            return false;
        }*/
        /*if (binding.editProfileOccupationEt.getText().toString().trim().isEmpty()){
            binding.editProfileOccupationEt.setError(getResources().getString(R.string.occupation_empty_error));
            binding.editProfileOccupationEt.requestFocus();
            return false;
        }
        if (binding.editProfileInterestedAreasEt.getText().toString().trim().isEmpty()){
            binding.editProfileInterestedAreasEt.setError(getResources().getString(R.string.interested_area_empty_error));
            binding.editProfileInterestedAreasEt.requestFocus();
            return false;
        }*/
        return true;
    }


}
