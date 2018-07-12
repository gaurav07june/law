package com.matterhornlegal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.LoginResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.SharedPrefsUtils;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

public class ForgotPassActivity extends BaseActivity implements View.OnClickListener{

    private NMGEditText mEtEmail;
    private NMGButton mBtnSubmit;
    private NMGTextView mTvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        mEtEmail=(NMGEditText)findViewById(R.id.forgot_pass_email_et);
        mBtnSubmit=(NMGButton)findViewById(R.id.forgot_pass_request_btn);
        mTvLogin=(NMGTextView)findViewById(R.id.forgot_pass_login);

        mBtnSubmit.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_pass_request_btn:
                getData(ApiUtils.ApiActionEvents.FORGOT_PASSWORD);
                break;
            case R.id.forgot_pass_login:
                startActivity(new Intent(this,LoginEmailActivity.class));
                finish();
                break;
        }
    }


    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(ForgotPassActivity.this)) {
            ToastUtils.showToast(ForgotPassActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.FORGOT_PASSWORD:
                showProgressDialog(getString(R.string.pleaseWait));
                String payload = ApiUtils.getForgotPasswordPayload(
                        mEtEmail.getText().toString()).toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<BaseModel>(
                                Request.Method.POST,ApiUtils.ApiUrl.FORGOT_PASSWORD_URL, payload, BaseModel.class,
                                new VolleyErrorListener(this, this, actionID)) {

                            @Override
                            protected void deliverResponse(BaseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("forgot password response: " + data);
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
            case ApiUtils.ApiActionEvents.FORGOT_PASSWORD:
                removeProgressDialog();
                if (status) {
                    if (serviceResponse instanceof BaseModel) {
                        BaseModel baseModel = (BaseModel) serviceResponse;
                        ToastUtils.showToast(ForgotPassActivity.this, baseModel.getMessage());

                    }
                } else if (statusCode == 401) {
                        //AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(ForgotPassActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(ForgotPassActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}
