package com.matterhornlegal.models;

/**
 * Created by seema.gurtatta on 12/19/2017.
 */

public class EditProfileResponseModel {
    private String message;

    private int status;

    private RegisteredUserDetail data;

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

    public RegisteredUserDetail getData ()
    {
        return data;
    }

    public void setData (RegisteredUserDetail data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+"]";
    }
}
