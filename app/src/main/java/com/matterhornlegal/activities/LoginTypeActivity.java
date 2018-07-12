package com.matterhornlegal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.matterhornlegal.R;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.LoginResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;
import com.nmgtechnologies.socialloginlib.LoginData;
import com.nmgtechnologies.socialloginlib.SocialLoginCallback;
import com.nmgtechnologies.socialloginlib.SocialLoginHelper;

import org.json.JSONObject;

public class LoginTypeActivity extends BaseActivity implements SocialLoginCallback,View.OnClickListener {

    private LoginData loginData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
        SocialLoginHelper.getInstance().initForActivity(this);
        setViews();
    }
    private void setViews() {
        findViewById(R.id.login_via_fb_btn).setOnClickListener(this);
        findViewById(R.id.login_via_twitter_btn).setOnClickListener(this);
        findViewById(R.id.login_via_google_plus_btn).setOnClickListener(this);
        findViewById(R.id.login_via_linkedin).setOnClickListener(this);
    }

    public void onSkipClicked(View view) {
        AppGlobalData.getInstance().setGuest(true);
        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
        startActivity(new Intent(this, HomeLandingActivity.class));
        finish();
    }

    public void onLoginWithEmailClicked(View view) {

        startActivity(new Intent(this, LoginEmailActivity.class));
        finish();
    }

    public void onSignUpClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    @Override
    public void onSocialLoginSuccess(LoginData loginData) {
        Toast.makeText(this, "email: " + loginData.getEmail(), Toast.LENGTH_SHORT).show();
        this.loginData = loginData;
        getData(ApiUtils.ApiActionEvents.SOCIAL_LOGIN);
    }

    @Override
    public void onSocialLoginFailed() {
        Toast.makeText(this, R.string.social_login_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialLoginCancelled() {
        Toast.makeText(this, R.string.social_login_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_via_fb_btn:
                SocialLoginHelper.getInstance().loginViaFacebook();
                break;
            case R.id.login_via_twitter_btn:
                SocialLoginHelper.getInstance().loginViaTwitter();
                break;
            case R.id.login_via_google_plus_btn:
                SocialLoginHelper.getInstance().loginViaGoogle();
                break;
            case R.id.login_via_linkedin:
                SocialLoginHelper.getInstance().loginViaLinkedIn();
                break;
        }
    }


    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(this)) {
            ToastUtils.showToast(this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.SOCIAL_LOGIN:
                if (progressDialog != null) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
                JSONObject payloadObj = new JSONObject();
                try
                {
                    payloadObj.put("first_name",loginData.getFirstName()!=null ? loginData.getFirstName() : "");
                    payloadObj.put("last_name",loginData.getFirstName()!=null ? loginData.getFirstName() : "");
                    payloadObj.put("email",loginData.getEmail()!=null ? loginData.getEmail() : "");
                    payloadObj.put("social_id",loginData.getSocialId()!=null ? loginData.getSocialId() : "");

                }
                catch (Exception e)
                {

                }

                String payload = payloadObj.toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<LoginResponseModel>(
                                ApiUtils.ApiUrl.SOCIAL_LOGIN_URL, payload, LoginResponseModel.class, new VolleyErrorListener(this, this, actionID,progressDialog)) {

                            @Override
                            protected void deliverResponse(LoginResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("login response: " + data);

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
            case ApiUtils.ApiActionEvents.SOCIAL_LOGIN:
                //removeProgressDialog();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof LoginResponseModel) {
                        LoginResponseModel loginModel = (LoginResponseModel) serviceResponse;
                        Logger.error("  +api token : " + loginModel.getToken());
                        AppGlobalData.getInstance().setToken(loginModel.getToken());
                        AppGlobalData.getInstance().setGuest(false);
                        AppGlobalData.getInstance().setRegisteredUserDetail(loginModel.getUser_details());
                        AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
                        /*SharedPrefsUtils.setSharedPrefString(LoginEmailActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, loginModel.getToken());
                        SharedPrefsUtils.setSharedPrefString(LoginEmailActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.USER_ID, loginModel.getUser_details().getUser_id());
                        */
                        Intent intent = new Intent(LoginTypeActivity.this, HomeLandingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(LoginTypeActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(LoginTypeActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialLoginHelper.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
