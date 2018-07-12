package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityChangePasswordBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.BaseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.Map;


public class ChangePasswordActivity extends BaseActivity {

    private Toolbar mToolbar;
    private NMGEditText mEtCurrentPassword, mEtNewPassword, metConfirmPassword;
    private NMGTextView mTvTitle;
    private NMGButton mBtnResetPassword;
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        setToolbar();
        initView();
    }

    private void setToolbar(){
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

    private void initView(){

        mTvTitle=(NMGTextView)findViewById(R.id.titleTv);
        mTvTitle.setText(getString(R.string.resetPassword));
    }

    public void onResetClicked(View v){
        if (isValidateForm()){
            getData(ApiUtils.ApiActionEvents.CHANGE_PASSWORD);
        }
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        switch (actionID){
            case ApiUtils.ApiActionEvents.CHANGE_PASSWORD:
                if (!Utils.isNetworkEnabled(ChangePasswordActivity.this)) {
                    ToastUtils.showToast(ChangePasswordActivity.this, getString(R.string.errorInternet));
                    return;
                }
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                    String currentPass = binding.changePasswordCurrentPasswodrEt.getText().toString().trim();
                    String newPass = binding.changePasswordNewPasswodrEt.getText().toString().trim();
                    String confirmPass = binding.changePasswordConfirmPasswodrEt.getText().toString().trim();
                    Map<String, String> chagepassHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                            AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                    String changePassPayload = ApiUtils.getChangePasswordPayload(currentPass, newPass, confirmPass).toString();
                    RequestManager.addRequest(
                            new GsonObjectRequest<BaseModel>(
                                    Request.Method.POST, ApiUtils.ApiUrl.CHANGE_PASSWORD_URL, chagepassHeader, changePassPayload, BaseModel.class,
                                    new VolleyErrorListener(this, this, actionID), new Gson()) {

                                @Override
                                protected void deliverResponse(BaseModel response) {
                                    String data = new String(mResponse.data);
                                    Logger.error("change password response: " + data);
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
                }
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID){
            case ApiUtils.ApiActionEvents.CHANGE_PASSWORD:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof BaseModel) {
                        BaseModel baseModel = (BaseModel) serviceResponse;
                        ToastUtils.showToast(ChangePasswordActivity.this, baseModel.getMessage());
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginEmailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.finish();
                        startActivity(intent);

                    }
                } else if (statusCode == 401) {
                    //AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(ChangePasswordActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(ChangePasswordActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }


    }

    private boolean isValidateForm(){
        if (binding.changePasswordCurrentPasswodrEt.getText().toString().trim().isEmpty()){
            binding.changePasswordCurrentPasswodrEt.setError(getResources().getString(R.string.current_password_empty_error));
            binding.changePasswordCurrentPasswodrEt.requestFocus();
            return false;
        }
        String userPass = AppGlobalData.getInstance().getRegisteredUserDetail().getPassword();
        if (userPass == null){
            userPass = "";
        }
        if (!(binding.changePasswordCurrentPasswodrEt.getText().toString().trim()).equals(userPass)){
            binding.changePasswordCurrentPasswodrEt.setError(getResources().getString(R.string.correct_password_error));
            binding.changePasswordCurrentPasswodrEt.requestFocus();
            return false;
        }
        if (binding.changePasswordNewPasswodrEt.getText().toString().trim().isEmpty()){
            binding.changePasswordNewPasswodrEt.setError(getResources().getString(R.string.new_password_empty_error));
            binding.changePasswordNewPasswodrEt.requestFocus();
            return false;
        }
        if (!Utils.isValidPassword(binding.changePasswordNewPasswodrEt.getText().toString().trim())){
            binding.changePasswordNewPasswodrEt.setError(getResources().getString(R.string.password_validation_msg));
            binding.changePasswordNewPasswodrEt.requestFocus();
            return false;
        }
        if (binding.changePasswordConfirmPasswodrEt.getText().toString().trim().isEmpty()){
            binding.changePasswordConfirmPasswodrEt.setError(getResources().getString(R.string.confirm_password_empty_error));
            binding.changePasswordConfirmPasswodrEt.requestFocus();
            return false;
        }

        if (!(binding.changePasswordNewPasswodrEt.getText().toString().trim()).equals(binding.changePasswordConfirmPasswodrEt.getText().toString().trim())){
            ToastUtils.showToast(this, getResources().getString(R.string.password_mismatch_error));
            return false;
        }
        return true;
    }
}
