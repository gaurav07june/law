package com.matterhornlegal.models;


public class HomeFeedResponseModel {
    private int status;

    private String message;

    private HomeFeedData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HomeFeedData getData() {
        return data;
    }

    public void setData(HomeFeedData data) {
        this.data = data;
    }
}
