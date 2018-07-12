package com.matterhornlegal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.IndustryListResponseModel;
import com.matterhornlegal.models.LanguageListResponseModel;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.models.PracticeAreaListResponseModel;
import com.matterhornlegal.models.RegisteredUserDetail;

/**
 * Utility class to store/retrieve values in SharedPreferences.
 *
 *
 */
public class SharedPrefsUtils {

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static boolean getSharedPrefBoolean(Context pContext, String pFileName, String pKey, boolean pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getBoolean(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefBoolean(Context pContext, String pFileName, String pKey, boolean pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putBoolean(pKey, pValue);
        _editor.apply();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static int getSharedPrefInt(Context pContext, String pFileName, String pKey, int pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getInt(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefInt(Context pContext, String pFileName, String pKey, int pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putInt(pKey, pValue);
        _editor.apply();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static String getSharedPrefString(Context pContext, String pFileName, String pKey, String pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getString(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefString(Context pContext, String pFileName, String pKey, String pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putString(pKey, pValue);
        _editor.apply();
    }

    public static void setRegisteredUserPref(Context pContext, String pFileName, String pKey,
                                             RegisteredUserDetail pObject){
        if (pObject != null){
            SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
            Editor _editor = _sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(pObject);
            _editor.putString(pKey, json);
            _editor.apply();
        }

    }
    public static RegisteredUserDetail getRegisteredUserPref(Context pContext, String pFileName, String pKey){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = _sharedPref.getString(pKey, "");
        RegisteredUserDetail registeredUserDetail = gson.fromJson(json, RegisteredUserDetail.class);
        return registeredUserDetail;
    }
    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static long getSharedPrefLong(Context pContext, String pFileName, String pKey, long pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getLong(pKey, pDefault);
    }
    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefLong(Context pContext, String pFileName, String pKey, long pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putLong(pKey, pValue);
        _editor.apply();
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pDefault
     * @return
     */
    public static float getSharedPrefFloat(Context pContext, String pFileName, String pKey, float pDefault) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        return _sharedPref.getFloat(pKey, pDefault);
    }

    /**
     * @param pContext
     * @param pFileName
     * @param pKey
     * @param pValue
     */
    public static void setSharedPrefFloat(Context pContext, String pFileName, String pKey, float pValue) {
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.putFloat(pKey, pValue);
        _editor.apply();
    }
    public static void clearSharedPref(Context pContext, String pFileName){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Editor _editor = _sharedPref.edit();
        _editor.clear();
        _editor.apply();
    }

    public static void setCountryListPref(Context pContext, String pFileName, String pKey,
                                          LocationListResponseModel pObject){
        if (pContext != null){
            SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
            Editor _editor = _sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(pObject);
            _editor.putString(pKey, json);
            _editor.apply();
        }



    }
    public static LocationListResponseModel getCountryListPref(Context pContext, String pFileName, String pKey){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = _sharedPref.getString(pKey, "");
        LocationListResponseModel locationListResponseModel = gson.fromJson(json, LocationListResponseModel.class);
        return locationListResponseModel;
    }
    public static void setPracticeAreaPref(Context pContext, String pFileName, String pKey,
                                           PracticeAreaListResponseModel pObject){
        if (pContext != null){
            SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
            Editor _editor = _sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(pObject);
            _editor.putString(pKey, json);
            _editor.apply();
        }
    }
    public static PracticeAreaListResponseModel getPracticeAreaPref(Context pContext, String pFileName, String pKey){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = _sharedPref.getString(pKey, "");
        PracticeAreaListResponseModel practiceAreaListResponseModel = gson.fromJson(json, PracticeAreaListResponseModel.class);
        return practiceAreaListResponseModel;
    }

    public static void setIndustrySectorPref(Context pContext, String pFileName, String pKey, IndustryListResponseModel pObject){
        if (pContext != null){
            SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
            Editor _editor = _sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(pObject);
            _editor.putString(pKey, json);
            _editor.apply();
        }
    }
    public static IndustryListResponseModel getIndustrySectorPref(Context pContext, String pFileName, String pKey){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = _sharedPref.getString(pKey, "");
        IndustryListResponseModel industryListResponseModel = gson.fromJson(json, IndustryListResponseModel.class);
        return industryListResponseModel;
    }

    public static void setLanguagePref(Context pContext, String pFileName, String pKey, LanguageListResponseModel pObject){
        if (pContext != null){
            SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
            Editor _editor = _sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(pObject);
            _editor.putString(pKey, json);
            _editor.apply();
        }
    }
    public static LanguageListResponseModel getLanguagePref(Context pContext, String pFileName, String pKey){
        SharedPreferences _sharedPref = pContext.getSharedPreferences(pFileName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = _sharedPref.getString(pKey, "");
        LanguageListResponseModel languageListResponseModel = gson.fromJson(json, LanguageListResponseModel.class);
        return languageListResponseModel;
    }
}
