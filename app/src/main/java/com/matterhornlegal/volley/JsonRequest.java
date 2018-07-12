
package com.matterhornlegal.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyLog;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public abstract class JsonRequest<T> extends Request<T> {

    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private  String mRequestBody;

    private  byte[] byteRequestBody;

    private boolean isBinaryRequest;

    /**
     * Request headers.
     */
    protected Map<String, String> mRequestHeaders;

    private Priority mPriority;

    protected NetworkResponse mResponse;

    public JsonRequest(String url, String jsonPayload, ErrorListener errorListener) {
        this(url, Collections.<String, String>emptyMap(), jsonPayload, errorListener);
        this.isBinaryRequest = false;
    }

    public JsonRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, ErrorListener errorListener) {
        this(jsonPayload == null ? Method.GET : Method.POST, url, mRequestHeaders, jsonPayload, errorListener);
        this.isBinaryRequest = false;

    }

    public JsonRequest(int method, String url, Map<String, String> mRequestHeaders, String jsonPayload, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mRequestBody = jsonPayload;
        this.mRequestHeaders = mRequestHeaders;
        this.isBinaryRequest = false;
    }

    public JsonRequest(int method, String url, Map<String, String> mRequestHeaders, byte[] bytePayload, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.byteRequestBody = bytePayload;
        this.mRequestHeaders = mRequestHeaders;
        this.isBinaryRequest = true;
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    @Override
    public Priority getPriority() {
        return this.mPriority;
    }


    @Override
    public String getBodyContentType() {
        if (isBinaryRequest){
            return "application/offset+octet-stream";
        }else{
            return PROTOCOL_CONTENT_TYPE;
        }

    }

    @Override
    public byte[] getBody() {
        try {
            if (isBinaryRequest){
                return byteRequestBody;
            }else{
                return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
            }

        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mRequestHeaders;
    }


}
