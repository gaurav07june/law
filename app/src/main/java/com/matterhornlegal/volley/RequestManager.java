package com.matterhornlegal.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    private static RequestManager instance;
    private RequestQueue mDataRequestQueue;
//    private RequestQueue mImageQueue;
    private Context mContext;
//    private Config mConfig;
    private RequestManager(Context context) {
        this.mContext = context;
    }

    public static synchronized RequestManager initializeWith(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    private synchronized RequestQueue getDataRequestQueue() {
        if (mDataRequestQueue == null) {
            Log.e("", "new request QUEUE");
            mDataRequestQueue = Volley.newRequestQueue( mContext.getApplicationContext());
            mDataRequestQueue.start();
        }
        return mDataRequestQueue;
    }

    public static <T> void addRequest(Request<T> pRequest) {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }

        pRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = instance.getDataRequestQueue();

        queue.add(pRequest);
    }



    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param pRequestTag
     */
    public static void cancelPendingRequests(Object pRequestTag) {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        if (instance.getDataRequestQueue() != null) {
            instance.getDataRequestQueue().cancelAll(pRequestTag);
        }
    }
}
