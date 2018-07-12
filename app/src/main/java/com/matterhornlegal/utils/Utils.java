package com.matterhornlegal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.matterhornlegal.R;
import com.matterhornlegal.models.AppGlobalData;
import com.nmgtechnologies.socialloginlib.SocialLoginHelper;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by seema.gurtatta on 11/15/2016.
 */
public class Utils {
    public static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * @param pStr String object to be tested.
     * @returns true if the given string is null or empty or contains spaces
     * only.
     */
    public static boolean isNullOrEmpty(final String pStr) {
        return pStr == null || pStr.trim().length() == 0 || pStr.trim().equalsIgnoreCase("null");
    }

    /**
     * @param pEmail
     * @param pAllowBlank
     * @return true if pEmail matches with EMAIL_REGEX,
     * false otherwise
     */
    public static boolean isValidEmail(String pEmail, boolean pAllowBlank) {
        if (pAllowBlank && isNullOrEmpty(pEmail)) {
            return true;
        }
        Pattern validRegexPattern = Pattern.compile(EMAIL_REGEX);
        return validRegexPattern.matcher(pEmail).matches();
    }

    public static void signOutFromApp(Context context){
        // sign out form app
        AppGlobalData.getInstance().logOutRegisteredUser(context);
        // sign out from facebook
        SocialLoginHelper.getInstance().logoutFromFacebook();
        // sign out from twitter
        // sign out from google
        SocialLoginHelper.getInstance().logoutFromGoogle();
        // sign out from linkedIn
        SocialLoginHelper.getInstance().logoutFromLinkedIn();


    }


    /**
     * @param pContext
     * @return
     * @note android.permission.ACCESS_NETWORK_STATE is required
     */
    public static boolean isNetworkEnabled(Context pContext) {
        NetworkInfo activeNetwork = getActiveNetwork(pContext);
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * @param pContext
     * @return
     * @note android.permission.ACCESS_NETWORK_STATE is required
     */
    public static NetworkInfo getActiveNetwork(Context pContext) {
        ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMngr == null ? null : conMngr.getActiveNetworkInfo();
    }

    /**
     * hides the soft keypad from screen
     *
     * @param pActivity
     * @param view
     */
    public static void hideSoftKeypad(Activity pActivity, View view) {
        InputMethodManager imm = (InputMethodManager) pActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String getPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static boolean isValidPassword(String passString){
        boolean isValid = true;
        Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern upperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        // checking for alphabets
        if (!upperCasePatten.matcher(passString).find()){
            isValid = false;
        }
        // checking for numerics
        if (!digitCasePatten.matcher(passString).find()){
            isValid = false;
        }
        // checking for lowercase
        if (!lowerCasePatten.matcher(passString).find()){
            isValid = false;
        }
        if (!specialCharPatten.matcher(passString).find()){
            isValid = false;
        }
        // check for starting char as alphabet
        Character firstChar = passString.charAt(0);
        if (!((firstChar >='a' && firstChar <='z') || (firstChar >='A' && firstChar <='Z'))){
            isValid = false;
        }

        if (!(passString.length() >= 6 && passString.length() <= 12)){
            isValid = false;
        }
        return isValid;
    }

    public static final boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static String getAppHiddenDirectory(Context context) {
        try {
            File hidden_dir = new File(Environment.getExternalStorageDirectory() + File.separator + "." + context.getString(R.string.app_name));
            // Context pContext, String pFileName, String pKey, boolean pDefault
            boolean isInitialized = SharedPrefsUtils.getSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.DIRECTORY_INITIALIZED, false);
            if (hidden_dir.exists() && !isInitialized) {
                FileUtils.cleanDirectory(hidden_dir);

                SharedPrefsUtils.setSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.DIRECTORY_INITIALIZED, true);

            }
            if (!hidden_dir.exists()) {
                hidden_dir.mkdirs();
                SharedPrefsUtils.setSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.DIRECTORY_INITIALIZED, true);
            }
            return hidden_dir.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Bitmap getBitmapImage(Intent data, Context context){
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
        return imageBitmap;
    }


    public static String encodeToBase64(Bitmap imageBitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public static boolean isValidatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }


    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static String getFormatedDateTime(String dateTime){
        if (dateTime != null){
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            //String outputPattern = "dd-MMM-yyyy h:mm a";
            String outputPattern = "MMM dd, yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(dateTime);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }else{
            return null;
        }

    }

    public static Date getDate(String webDate){
        if (webDate != null){
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            try {
               return inputFormat.parse(webDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    public static String toYYYYmmDD(String webDate,int buffer){
        if (webDate != null){
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "yyyyMMdd";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            try {
                Date untilDate =  inputFormat.parse(webDate);
                Calendar dt = Calendar.getInstance();
                dt.setTime(untilDate);
                dt.add(Calendar.DATE, buffer);
                return outputFormat.format(dt.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static String getWebDate(Date date){
        if (date != null){
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            try {
                return inputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";
    }
    public static String getFormatedDateTime(String dateTime, boolean time){
        if (dateTime != null){
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            //String outputPattern = "dd-MMM-yyyy h:mm a";
            String outputPattern = "MMM dd, yyyy (h:mm a)";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(dateTime);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }else{
            return null;
        }

    }


    /*for continue reading concept*/
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore, final Context context) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine == -1){
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex)+ " "+ expandText;
                }else{
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex).toString();
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(addClickablePartTextViewResizable(tv.getText().toString(), tv, lineEndIndex, expandText,
                        viewMore, context), TextView.BufferType.SPANNABLE);
            }
        });

    }
    public static SpannableStringBuilder addClickablePartTextViewResizable(final String strSpanned, final TextView tv,
                                                                           final int maxLine, final String spanableText, final boolean viewMore, final Context context) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {

            ssb.setSpan(new MySpannable(false, context){
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, context.getResources().getString(R.string.view_less), false, context);
                    } else {
                        makeTextViewResizable(tv, 3, context.getResources().getString(R.string.continue_reading), true, context);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;
        Context context;
        public MySpannable(boolean isUnderline, Context context) {
            this.isUnderline = isUnderline;
            this.context = context;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(ContextCompat.getColor(context, R.color.colorOrange));
        }
        @Override
        public void onClick(View widget) {
        }
    }

    public static String getFormatedTime(int hour, int minute){
        String h ="00", m = "00", merid = "AM";
        if (hour > 12){
            merid = "PM";
            hour = hour - 12;
        }else if (hour < 12){
            merid = "AM";
        }else if (hour == 12){
            merid = "PM";
        }
        if (hour == 00){
            hour = 12;
        }
        if (hour <10){
            h = "0"+hour;
        }else{
            h = String.valueOf(hour);
        }
        if (minute < 10){
            m = "0"+minute;
        }else{
            m = String.valueOf(minute);
        }
        return h+":"+m+" "+merid;
    }




    public void hitForVolleyRequest(Context context){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = ApiUtils.ApiUrl.VIMEO_CREATE_VIDEO;

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("approach", "tus");
            jsonBody.put("size", 1055736);

            JSONObject finalJson = new JSONObject();
            finalJson.put("upload", jsonBody);
            final String requestBody = finalJson.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", "bearer c931f5f75d5b68c908e15f9428675c3e");

                    params.put("Accept", "application/vnd.vimeo.*+json;version=3.4");
                    params.put("Content-Type", "application/json; charset=%s, utf-8");
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    String responseString = "";
                    try{
                        String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Log.d("Matterhorn", "Response: " + json.toString());
                    }catch (UnsupportedEncodingException exception){

                    }


                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*checking for the crashalytics*/
    /*public static void forceCrash() {
        throw new RuntimeException("This is a crash");
    }*/


    public static String timeAgoFormate(String timeString){
        String agoString = "";
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date past = format.parse("2016.02.05 AD at 23:59:30");
            Date past = format.parse(timeString);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if(seconds<60)
            {
                agoString = seconds+" seconds ago";
                System.out.println(seconds+" seconds ago");
            }
            else if(minutes<60)
            {
                agoString = minutes+" minutes ago";
                System.out.println(minutes+" minutes ago");
            }
            else if(hours<24)
            {
                agoString = hours+" hours ago";
                System.out.println(hours+" hours ago");
            }
            else
            {
                agoString = days+" days ago";
                System.out.println(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }

        return agoString;

    }

    @NonNull
    public static Spanned fromHtml(@NonNull String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
