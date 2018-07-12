package com.matterhornlegal.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.matterhornlegal.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class GsonObjectRequest<T> extends JsonRequest<T> {
    private final Gson mGson;
    private final Class<T> mClazz;
    private static final String TAG = "GsonObjectRequest";
    private boolean isBinaryRequest = false;

    public GsonObjectRequest(String url, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(url, new HashMap<String, String>(), jsonPayload, clazz, errorListener);

    }

    public GsonObjectRequest(int method,String url, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(method,url, new HashMap<String, String>(), jsonPayload, clazz, errorListener, new Gson());

    }

    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(url, mRequestHeaders, jsonPayload, clazz, errorListener, new Gson());
    }

    public GsonObjectRequest(String url, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        this(url, null, jsonPayload, clazz, errorListener, gson);

    }

    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        super(url, mRequestHeaders, jsonPayload, errorListener);
        Logger.error("Url: " + url);
        this.mClazz = clazz;
        mGson = gson;
    }

    public GsonObjectRequest(int method, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        super(method, url, mRequestHeaders, jsonPayload, errorListener);
        Logger.error("Url: " + url);
        this.mClazz = clazz;
        mGson = gson;
    }

    public GsonObjectRequest(int method, String url, Map<String, String> mRequestHeaders, byte[] bytePayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        super(method, url, mRequestHeaders, bytePayload, errorListener);
        Logger.error("Url: " + url);
        this.mClazz = clazz;
        mGson = gson;

    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.mResponse = response;
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "Response: " + json.toString());
            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Exception: "+e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Response: " + new String(response.data));
            Log.d(TAG, "Exception: "+e.getMessage());
//            if(context!=null){
//                StringUtils.writeToFile(context,new String(response.data));
//            }
            return Response.error(new ParseError(e));
        }
    }
//    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Context context) {
//        this(url, mRequestHeaders, jsonPayload, clazz, errorListener, new Gson());
//        this.context=context;
//    }
//    private Context context=null;
}