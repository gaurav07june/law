package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.ActivityResetPasswordBinding;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.models.LoginResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.Set;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    ActivityResetPasswordBinding binding;
    private  Uri uri;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        uri = getIntent().getData();
        token = uri.getQueryParameter(AppConstants.commonConstants.RESET_PASS_URL_PARAM_TOKEN);
      //  Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        initView();
        setView();
    }

    private void setView() {

    }

    private void initView() {
        binding.resetPassBtn.setOnClickListener(this);
        binding.txtResetPassBackToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset_pass_btn:
                if (checkValidation()){
                    getData(ApiUtils.ApiActionEvents.RESET_PASSWORD);
                }

                break;
            case R.id.txt_reset_pass_back_to_login:
                Intent intent = new Intent(ResetPasswordActivity.this, LoginEmailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private boolean checkValidation() {
        if (binding.resetPassNewPassEt.getText().toString().trim().isEmpty()){
            binding.resetPassNewPassEt.requestFocus();
            binding.resetPassNewPassEt.setError(getResources().getString(R.string.password_empty_error));
            return false;
        }
        if (!Utils.isValidPassword(binding.resetPassNewPassEt.getText().toString().trim())){
            binding.resetPassNewPassEt.requestFocus();
            binding.resetPassNewPassEt.setText("");
            binding.resetPassNewPassEt.setError(getResources().getString(R.string.password_validation_msg));
            return false;
        }
        if (binding.resetPassConfirmPassEt.getText().toString().trim().isEmpty()){
            binding.resetPassConfirmPassEt.requestFocus();
            binding.resetPassConfirmPassEt.setError(getResources().getString(R.string.password_empty_error));
            return false;
        }
        if (!binding.resetPassNewPassEt.getText().toString().trim().equals(binding.resetPassConfirmPassEt.getText().toString().trim())){
            binding.resetPassConfirmPassEt.requestFocus();
            binding.resetPassConfirmPassEt.setError(getResources().getString(R.string.password_mismatch_error));
            return false;
        }
        return true;
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(ResetPasswordActivity.this)) {
            ToastUtils.showToast(ResetPasswordActivity.this, getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.RESET_PASSWORD:
                showProgressDialog(getString(R.string.pleaseWait));
                String payload = ApiUtils.getResetPasswordPayload(
                        binding.resetPassNewPassEt.getText().toString().trim()).toString();
                RequestManager.addRequest(
                    new GsonObjectRequest<BaseModel>(
                            Request.Method.POST,uri.toString(), payload, BaseModel.class,
                            new VolleyErrorListener(this, this, actionID)) {

                        @Override
                        protected void deliverResponse(BaseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("reset password response: " + data);
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
            case ApiUtils.ApiActionEvents.RESET_PASSWORD:
                removeProgressDialog();
                if (status) {
                    if (serviceResponse instanceof BaseModel) {
                        BaseModel baseModel = (BaseModel) serviceResponse;
                        ToastUtils.showToast(ResetPasswordActivity.this, baseModel.getMessage());
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginEmailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.finish();
                        startActivity(intent);

                    }
                } else if (statusCode == 401) {
                    //AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(ResetPasswordActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(ResetPasswordActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}
