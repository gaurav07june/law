package com.matterhornlegal.models;

import java.util.ArrayList;

/**
 * Created by gaurav.singh on 5/15/2018.
 */

public class PracticeAreaListResponseModel {
    private int status;

    private String message;
    private ArrayList<DataModel> data;

    public ArrayList<DataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DataModel> data) {
        this.data = data;
    }

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
}
