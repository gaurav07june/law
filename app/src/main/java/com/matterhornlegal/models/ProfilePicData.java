package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/19/2018.
 */

public class ProfilePicData {
    private String profile_pic;

    public String getProfile_pic ()
    {
        return profile_pic;
    }

    public void setProfile_pic (String profile_pic)
    {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [profile_pic = "+profile_pic+"]";
    }
}
