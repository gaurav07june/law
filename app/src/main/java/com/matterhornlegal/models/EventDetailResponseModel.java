package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/11/2018.
 */

public class EventDetailResponseModel {


    private String message;

    private int status;

    private EventDetail data;

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

    public EventDetail getData() {
        return data;
    }

    public void setData(EventDetail data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
