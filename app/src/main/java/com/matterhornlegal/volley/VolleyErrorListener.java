package com.matterhornlegal.volley;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.matterhornlegal.R;
import com.matterhornlegal.utils.CircularProgressDialog;
import com.matterhornlegal.utils.ToastUtils;


public class VolleyErrorListener implements Response.ErrorListener {
    private final int action;
    private final IScreen screen;
    private Context mContext;
    private CircularProgressDialog progressDialog;


    public VolleyErrorListener(final IScreen screen, Context ctx, final int action, CircularProgressDialog progressDialog) {
        this.screen = screen;
        this.action = action;
        this.mContext = ctx;
        this.progressDialog = progressDialog;
    }
    public VolleyErrorListener(final IScreen screen, Context ctx, final int action) {
        this.screen = screen;
        this.action = action;
        this.mContext = ctx;

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (progressDialog != null){
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        if(error==null){
            return;
        }
        NetworkResponse networkResponse = error.networkResponse;
        if(networkResponse==null){
            screen.updateUi(false, action, "",200);
            return;
        }
        int status = networkResponse.statusCode;
        if (status == 401) {
//            byte[] array=networkResponse.data;
//            String jsonString=new String(array);
//            try {
//                JSONObject json = new JSONObject(jsonString);
//
//
//            }catch (Exception e){
//
//            }

            if (mContext != null && screen != null) {
                screen.updateUi(false, action, "",status);
            }

        }
        if (error instanceof NoConnectionError) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.errorInternet));
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, "",status);
            }
//        } else if (error instanceof AuthFailureError) {
        } else if (error instanceof NetworkError) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.errorNetworkIssue));
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, "",status);
            }
        } else if (error instanceof ParseError) {
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, mContext.getString(R.string.errorParsing),status);
            }
        } else if (error instanceof ServerError) {
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, mContext.getString(R.string.errorNetworkIssue),status);
            }
        } else if (error instanceof TimeoutError) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.errorTimeOut));
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, "",status);
            }
        } else {
            if (mContext != null && screen != null) {
                screen.updateUi(false, action, mContext.getString(R.string.errorNetworkIssue),status);
            }
        }
    }

}
