package com.matterhornlegal.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityCmsBinding;
import com.matterhornlegal.models.TermPrivacyData;
import com.matterhornlegal.models.TermPrivacyResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

/**
 * Created by seema.gurtatta on 10/25/2017.
 */
public class CmsActivity extends BaseActivity {

    private Toolbar mToolbar;
    private NMGTextView mTvTitle;
    private String type;
    private TermPrivacyData termPrivacyData;
    private ActivityCmsBinding binding;
    private WebView webView;


    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms_webview);
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_cms);
        setToolbar();
        /*Demo for progressbar*/
        handler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /*Demo for progressbar*/

       // initView();
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
        webView = (WebView) findViewById(R.id.cmsWebview);
        webView.setWebViewClient(new MyWebViewClient());
        if (progressDialog != null){
            if (!progressDialog.isShowing()){
                progressDialog.show();
            }
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type");
            if (type.equalsIgnoreCase("pp")) {
                mTvTitle.setText(getString(R.string.privacyPolicy));
                //getData(ApiUtils.ApiActionEvents.GET_PRIVACY_POLICY);
                webView.loadUrl("https://matterhornlaw.staging.wpengine.com/privacy-policy/");
            } else if (type.equalsIgnoreCase("tnc")) {
                mTvTitle.setText(getString(R.string.termsConditions));
                /*CharSequence styledText = getText(R.string.term_of_use);
                binding.cmsContentTv.setText(Utils.fromHtml(getResources().getString(R.string.term_of_use)));*/
                //binding.tncWebview.loadData(getResources().getString(R.string.term_of_use), "text/html", "UTF-8");

                //getData(ApiUtils.ApiActionEvents.GET_TNC);
                webView.loadUrl("https://matterhornlaw.staging.wpengine.com/terms-of-use/");
            } else {
                mTvTitle.setText(getString(R.string.app_name));
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*if (url.startsWith("tel:")) {
                initiateCall(url);
                return true;
            }
            if (url.startsWith("mailto:")) {
                sendEmail(url.substring(7));
                return true;
            }*/
            return false;
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            /*String url = request.getUrl().toString();
            if (url.startsWith("tel:")) {
                initiateCall(url);
                return true;
            }
            if (url.startsWith("mailto:")) {
                sendEmail(url.substring(7));
                return true;
            }*/
            return false;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // Make a note that the page has finished loading.
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // Handle the error
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
        }
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        switch (actionID){

            case ApiUtils.ApiActionEvents.GET_PRIVACY_POLICY:
                if (!Utils.isNetworkEnabled(this)) {
                    ToastUtils.showToast(this, getString(R.string.errorInternet));
                    return;
                }
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                RequestManager.addRequest(new GsonObjectRequest<TermPrivacyResponseModel>(
                        ApiUtils.ApiUrl.GET_PRIVACY_POLICY_URL, null, TermPrivacyResponseModel.class,
                        new VolleyErrorListener(this, this, actionID)) {

                    @Override
                    protected void deliverResponse(TermPrivacyResponseModel response) {
                        String data = new String(mResponse.data);
                        Logger.error("privacy policy response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_TNC:
                if (!Utils.isNetworkEnabled(this)) {
                    ToastUtils.showToast(this, getString(R.string.errorInternet));
                    return;
                }
                if (progressDialog != null){
                    if (!progressDialog.isShowing()){
                        progressDialog.show();
                    }
                }
                RequestManager.addRequest(new GsonObjectRequest<TermPrivacyResponseModel>(
                        ApiUtils.ApiUrl.GET_TNC_URL, null, TermPrivacyResponseModel.class,
                        new VolleyErrorListener(this, this, actionID)) {

                    @Override
                    protected void deliverResponse(TermPrivacyResponseModel response) {
                        String data = new String(mResponse.data);
                        Logger.error("privacy policy response: " + data);
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
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_PRIVACY_POLICY:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof TermPrivacyResponseModel) {
                        TermPrivacyResponseModel responseModel = (TermPrivacyResponseModel) serviceResponse;
                        termPrivacyData = responseModel.getData();
                        binding.cmsContentTv.setText(termPrivacyData.getDescription());
                    }
                } else if (statusCode == 401) {
//                    AppUtils.logOut(LoginActivity.this);
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(CmsActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(CmsActivity.this, getString(R.string.errorGeneric));
                }
                break;
            case ApiUtils.ApiActionEvents.GET_TNC:
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof TermPrivacyResponseModel) {
                        TermPrivacyResponseModel responseModel = (TermPrivacyResponseModel) serviceResponse;
                        termPrivacyData = responseModel.getData();
                        binding.cmsContentTv.setText(termPrivacyData.getDescription());
                    }
                } else if (statusCode == 401) {
                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(CmsActivity.this, (String) serviceResponse);
                } else {
                    ToastUtils.showToast(CmsActivity.this, getString(R.string.errorGeneric));
                }
                break;
        }
    }
}