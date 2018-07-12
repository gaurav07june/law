package com.matterhornlegal.models;

/**
 * Created by seema.gurtatta on 11/17/2016.
 */
public class BaseModel {
    private int  status;
    private String message;

    public BaseModel() {
    }

    public BaseModel(int error, String message) {
        this.status = error;
        this.message = message;

    }

    public boolean isSuccess() {
        return status==1;
    }

    public void setStatus(int error) {
        this.status = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
