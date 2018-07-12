package com.matterhornlegal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.LoginResponseModel;
import com.matterhornlegal.models.RegisteredUserDetail;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class LoginEmailActivity extends BaseActivity implements View.OnClickListener {
    private NMGTextView mTvSkip, mTvForgotPassword, txtGetStarted;
    private NMGButton mBtnLogin, mBtnSocial ;
    private NMGEditText mEtEmail, mEtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        initView();
    }

    private void initView() {

        mBtnSocial = (NMGButton) findViewById(R.id.loginSocialBtn);
        mBtnLogin = (NMGButton) findViewById(R.id.loginBtn);
        mTvSkip = (NMGTextView) findViewById(R.id.loginEmailSkipTv);
        mEtEmail = (NMGEditText) findViewById(R.id.loginEmailEt);
        mEtPassword = (NMGEditText) findViewById(R.id.loginPasswordEt);
        mTvForgotPassword=(NMGTextView)findViewById(R.id.loginForgotPasswordBtn);
        txtGetStarted = (NMGTextView) findViewById(R.id.txt_login_get_started);
        mBtnLogin.setOnClickListener(this);
        mBtnSocial.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
        txtGetStarted.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginSocialBtn: {
                Intent intent = new Intent(LoginEmailActivity.this, LoginTypeActivity.class);
                startActivity(intent);
            }

            break;
            case R.id.loginBtn: {
                if (isValidInput()){
                    getData(ApiUtils.ApiActionEvents.LOGIN);
                }

            }

            break;
            case R.id.loginEmailSkipTv: {
                AppGlobalData.getInstance().setGuest(true);
                AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                //SharedPrefsUtils.setSharedPrefBoolean(LoginEmailActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, true);
                Intent intent = new Intent(LoginEmailActivity.this, HomeLandingActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.loginForgotPasswordBtn:
                Intent intent = new Intent(LoginEmailActivity.this, ForgotPassActivity.class);
                startActivity(intent);
                break;

            case R.id.txt_login_get_started:
                startActivity(new Intent(LoginEmailActivity.this , SignUpActivity.class));
                finish();
                break;
        }

    }
    private boolean isValidInput(){
        if (mEtEmail.getText().toString().trim().isEmpty()){
            mEtEmail.setError(getResources().getString(R.string.username_email_emptty_error));
            mEtEmail.requestFocus();
            return false;
        }
        if (mEtPassword.getText().toString().trim().isEmpty()){
            mEtPassword.setError(getResources().getString(R.string.password_empty_error), null);
            mEtPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(LoginEmailActivity.this)) {
            ToastUtils.showToast(LoginEmailActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.LOGIN:
                //showProgressDialog(getString(R.string.pleaseWait));
                progressDialog.show();
                String payload = ApiUtils.getLoginPayload(
                        mEtEmail.getText().toString(), mEtPassword.getText().toString()).toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<LoginResponseModel>(
                                ApiUtils.ApiUrl.LOGIN_URL, payload, LoginResponseModel.class, new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(LoginResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("login response: " + data);

                                if (response.getStatus()==1 && response.getUser_details() != null) {
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
            case ApiUtils.ApiActionEvents.LOGIN:
                //removeProgressDialog();
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof LoginResponseModel) {
                        LoginResponseModel loginModel = (LoginResponseModel) serviceResponse;
                        Logger.error("  +api token : " + loginModel.getToken());
                        RegisteredUserDetail registeredUserDetail = loginModel.getUser_details();
                        registeredUserDetail.setPassword(mEtPassword.getText().toString().trim());
                        AppGlobalData.getInstance().setToken(loginModel.getToken());
                        AppGlobalData.getInstance().setGuest(false);
                        AppGlobalData.getInstance().setRegisteredUserDetail(registeredUserDetail);
                        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                        /*SharedPrefsUtils.setSharedPrefString(LoginEmailActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, loginModel.getToken());
                        SharedPrefsUtils.setSharedPrefString(LoginEmailActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.USER_ID, loginModel.getUser_details().getUser_id());
                        */
                        Intent intent = new Intent(LoginEmailActivity.this, HomeLandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(LoginEmailActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(LoginEmailActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}
