package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/19/2018.
 */

public class ChangeProfileResponseModel {
    private String message;

    private int status;

    private ProfilePicData data;

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

    public ProfilePicData getData() {
        return data;
    }

    public void setData(ProfilePicData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+"]";
    }
}
