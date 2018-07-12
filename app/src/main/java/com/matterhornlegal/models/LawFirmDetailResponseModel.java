package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 5/11/2018.
 */

public class LawFirmDetailResponseModel {
    private String message;

    private int status;

    private LawFirmDetail data;

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

    public LawFirmDetail getData() {
        return data;
    }

    public void setData(LawFirmDetail data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+"]";
    }
}
