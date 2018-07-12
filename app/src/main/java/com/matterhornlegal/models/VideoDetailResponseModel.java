package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 5/29/2018.
 */

public class VideoDetailResponseModel {

    private String message;

    private int status;

    private VideoFeedModel data;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public int getStatus ()
    {
        return status;
    }

    public void setStatus (int status)
    {
        this.status = status;
    }

    public VideoFeedModel getData ()
    {
        return data;
    }

    public void setData (VideoFeedModel data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+"]";
    }
}
