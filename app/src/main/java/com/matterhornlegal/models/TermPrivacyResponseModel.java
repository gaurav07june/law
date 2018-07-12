package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/26/2018.
 */

public class TermPrivacyResponseModel {

    private String message;

    private int status;

    private TermPrivacyData data;

    private int errorCode;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TermPrivacyData getData() {
        return data;
    }

    public void setData(TermPrivacyData data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+", errorCode = "+errorCode+"]";
    }
}
