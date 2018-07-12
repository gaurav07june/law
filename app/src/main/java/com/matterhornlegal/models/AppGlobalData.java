package com.matterhornlegal.models;

import android.content.Context;

import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.SharedPrefsUtils;

/**
 * Created by gaurav.singh on 5/4/2018.
 */

public class AppGlobalData {


    private static AppGlobalData instance = null;

    private AppGlobalData(){

    }
    public static AppGlobalData getInstance(){
        if (instance == null){
            instance = new AppGlobalData();
        }
        return instance;
    }
    private RegisteredUserDetail registeredUserDetail;
    private String token;
    private boolean isGuest;

    public RegisteredUserDetail getRegisteredUserDetail() {
        // from the sharedpreference saved data

        return registeredUserDetail;
    }

    public void setRegisteredUserDetail(RegisteredUserDetail registeredUserDetail) {
        this.registeredUserDetail = registeredUserDetail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }
    public boolean getUserFromPref(Context context)
    {
        registeredUserDetail=null;
        token = "";
        isGuest = true;
        registeredUserDetail = SharedPrefsUtils.getRegisteredUserPref(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL);
        token = SharedPrefsUtils.getSharedPrefString(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, "");
        isGuest = SharedPrefsUtils.getSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, true);
        return  registeredUserDetail != null && token != null && !isGuest;
    }
    /**
     * Save login data to prefs.
     * @param context
     */
    public void saveUserDetailToPref(Context context) {
        if (registeredUserDetail != null) {
            SharedPrefsUtils.setRegisteredUserPref(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL, registeredUserDetail);
        }
        SharedPrefsUtils.setSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, isGuest());
        if (token!=null)
            SharedPrefsUtils.setSharedPrefString(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, getToken());
    }

    public void logOutRegisteredUser(Context context){
        if (registeredUserDetail != null) {
            SharedPrefsUtils.setRegisteredUserPref(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL, null);
        }
        SharedPrefsUtils.setSharedPrefBoolean(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.GUEST_USER, true);
        if (token!=null)
            SharedPrefsUtils.setSharedPrefString(context, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.TOKEN, null);

    }

}