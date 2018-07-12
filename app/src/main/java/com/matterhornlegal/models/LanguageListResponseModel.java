package com.matterhornlegal.models;

import java.util.List;

/**
 * Created by gaurav.singh on 6/20/2018.
 */

public class LanguageListResponseModel {

    private String message;

    private int status;

    private List<DataModel> data;

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

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+"]";
    }
}
