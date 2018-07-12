package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.android.volley.Request;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.databinding.ActivitySignUpBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.SignUpResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{


    private String[] occupationList = {"Select occupation","Accountant", "Lawyer", "General Counsel", "Forensic Accountant"};
    private String[] interestedAreaList = {"Select interested area", "Law Videos", "Conferences", "Marketing", "Law Firms"};
    private SimpleSpinnerAdapter occupationSpinnerAdapter, interestedAreaSpinnerAdapter;

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
        setViews();
    }
    public void initView(){
        binding.signUpSkipLay.setOnClickListener(this);
        binding.signUpCreateBtn.setOnClickListener(this);
        binding.txtSignUpLogin.setOnClickListener(this);
        binding.edtInterestedAreas.setOnClickListener(this);
        binding.edtOccupation.setOnClickListener(this);

        binding.signupSpinnerOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.edtOccupation.setText(occupationList[position]);
                }else {
                    binding.edtOccupation.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.signupSpinnerInterestedArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.edtInterestedAreas.setText(interestedAreaList[position]);
                }else{
                    binding.edtInterestedAreas.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void setViews(){
        occupationSpinnerAdapter = new SimpleSpinnerAdapter(SignUpActivity.this, R.layout.simple_spinner_row, occupationList);
        binding.signupSpinnerOccupation.setAdapter(occupationSpinnerAdapter);
        interestedAreaSpinnerAdapter = new SimpleSpinnerAdapter(SignUpActivity.this, R.layout.simple_spinner_row, interestedAreaList);
        binding.signupSpinnerInterestedArea.setAdapter(interestedAreaSpinnerAdapter);

        SpannableString ss = new SpannableString(getResources().getString(R.string.matterhorn_term_condition));
        ClickableSpan clickableTermCond = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this,CmsActivity.class).putExtra("type","tnc"));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickablePrivacePolicy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this,CmsActivity.class).putExtra("type","pp"));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableTermCond, 73, 85, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickablePrivacePolicy, 90, 105, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.signUpAcceptTermsCbox.setText(ss);
        binding.signUpAcceptTermsCbox.setMovementMethod(LinkMovementMethod.getInstance());
        binding.signUpAcceptTermsCbox.setHighlightColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorOrange));


    }
    private AdapterView.OnItemClickListener onItemClickListener =
        new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        };

    public void onSkipClicked(View view) {
        AppGlobalData.getInstance().setGuest(true);
        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
        //SharedPrefsUtils.setSharedPrefBoolean(SignUpActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, true);
        startActivity(new Intent(this,HomeLandingActivity.class));
        finish();
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this,LoginEmailActivity.class));
        finish();
    }

    public void doCreateAccountTask(){
       // Toast.makeText(this, "going to sign up", Toast.LENGTH_SHORT).show();
        getData(ApiUtils.ApiActionEvents.REGISTER);
    }

    public boolean isAllFieldsValid(){

        if (binding.signUpFirstName.getText().toString().trim().isEmpty()){
            binding.signUpFirstName.setError(getResources().getString(R.string.first_name_empty_error));
            binding.signUpFirstName.requestFocus();
            return false;
        }
        /*if (binding.signUpLastName.getText().toString().trim().isEmpty()){
            binding.signUpLastName.setError(getResources().getString(R.string.last_name_empty_error));
            binding.signUpLastName.requestFocus();
            return false;
        }*/
        if (binding.signUpEmail.getText().toString().trim().isEmpty()){
            binding.signUpEmail.setError(getResources().getString(R.string.email_address_empty_error));
            binding.signUpEmail.requestFocus();
            return false;
        }
        if (!Utils.isValidEmail(binding.signUpEmail.getText().toString().trim())){
            binding.signUpEmail.setError(getResources().getString(R.string.email_address_valid_error));
            binding.signUpEmail.setText("");
            binding.signUpEmail.requestFocus();
            return false;
        }

        if (binding.signUpUserName.getText().toString().trim().isEmpty()){
            binding.signUpUserName.setError(getResources().getString(R.string.user_name_empty_error));
            binding.signUpUserName.requestFocus();
            return false;
        }
        if (binding.signUpPass.getText().toString().trim().isEmpty()){
            binding.signUpPass.setError(getResources().getString(R.string.password_empty_error), null);
            binding.signUpPass.requestFocus();
            return false;
        }

        if (!Utils.isValidPassword(binding.signUpPass.getText().toString().trim())){
            binding.signUpPass.setText("");
            binding.signUpPass.setError(getResources().getString(R.string.password_validation_msg), null);
            binding.signUpPass.requestFocus();
            return false;
        }
        if (binding.signUpCpass.getText().toString().trim().isEmpty()){
            binding.signUpCpass.requestFocus();
            binding.signUpCpass.setError(getResources().getString(R.string.password_empty_error), null);
            return false;
        }
        if (!binding.signUpPass.getText().toString().trim().equals(binding.signUpCpass.getText().toString().trim())){
            binding.signUpCpass.setText("");
            binding.signUpCpass.setError(getResources().getString(R.string.password_mismatch_error), null);
            binding.signUpCpass.requestFocus();
            return false;
        }
        if (!binding.signUpAcceptTermsCbox.isChecked()){
            binding.signUpAcceptTermsCbox.requestFocus();
            Toast.makeText(this, getResources().getString(R.string.term_condition_check_error), Toast.LENGTH_SHORT).show();
            //binding.signUpAcceptTermsCbox.setError(getResources().getString(R.string.term_condition_check_error));
            return false;
        }
        if (!binding.signupCaptchaLay.captchaCbox.isChecked()){
            binding.signupCaptchaLay.captchaCbox.requestFocus();
            Toast.makeText(this, (getResources().getString(R.string.captcha_error)), Toast.LENGTH_SHORT).show();
            //binding.signupCaptchaLay.captchaCbox.setError(getResources().getString(R.string.captcha_error));
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_create_btn:
                if (isAllFieldsValid()){
                    doCreateAccountTask();
                }
                break;
            case R.id.txt_sign_up_login:
                startActivity(new Intent(SignUpActivity.this, LoginEmailActivity.class));
                finish();
                break;
            case R.id.edt_interested_areas:
                binding.signupSpinnerInterestedArea.performClick();
                break;
            case R.id.edt_occupation:
                binding.signupSpinnerOccupation.performClick();
                break;
        }
    }


    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(SignUpActivity.this)) {
            ToastUtils.showToast(SignUpActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.REGISTER:
                showProgressDialog(getString(R.string.pleaseWait));
                String payload = ApiUtils.getRegisterPayload(
                        binding.signUpFirstName.getText().toString().trim(), binding.signUpLastName.getText().toString().toString(),
                        binding.signUpUserName.getText().toString().trim(),
                        binding.signUpEmail.getText().toString().trim(), binding.signUpPass.getText().toString().trim(),
                        binding.edtOccupation.getText().toString().trim(),
                        binding.edtInterestedAreas.getText().toString().trim()).toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<SignUpResponseModel>(
                                Request.Method.POST,ApiUtils.ApiUrl.REGISTER_URL, payload, SignUpResponseModel.class, new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(SignUpResponseModel response) {

                                String data = new String(mResponse.data);
                                Logger.error("login response: " + data);
                               // Toast.makeText(SignUpActivity.this, data, Toast.LENGTH_SHORT).show();

                                if (response.isSuccess() && response.getData() != null) {
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
            case ApiUtils.ApiActionEvents.REGISTER:
                removeProgressDialog();
                if (status) {
                    if (serviceResponse instanceof SignUpResponseModel) {
                        SignUpResponseModel responseModel = (SignUpResponseModel) serviceResponse;
                        Logger.error("  +api token : " + responseModel.getToken());

                        AppGlobalData.getInstance().setRegisteredUserDetail(responseModel.getData());
                        AppGlobalData.getInstance().setToken(responseModel.getToken());
                        AppGlobalData.getInstance().setGuest(false);
                        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                       // SharedPrefsUtils.setSharedPrefString(SignUpActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, responseModel.getToken());
                       // SharedPrefsUtils.setSharedPrefString(SignUpActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.USER_ID, responseModel.getData().getUser_id());
                        //SharedPrefsUtils.setRegisteredUserPref(SignUpActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL, AppGlobalData.getInstance().getRegisteredUserDetail());
                        //SharedPrefsUtils.setSharedPrefBoolean(SignUpActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, false);

                        Intent intent = new Intent(SignUpActivity.this, HomeLandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else if (statusCode == 401) {
                       //AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(SignUpActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(SignUpActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}
