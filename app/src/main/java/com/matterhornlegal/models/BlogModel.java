package com.matterhornlegal.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by karan.kalsi on 10/30/2017.
 */

public class BlogModel implements Serializable{


    @SerializedName("id")
    private int blogId;
    @SerializedName("title")
    private String blogTitle;
    @SerializedName("description")
    private String blogDescription;
    @SerializedName("image")
    private String blogImg;
    @SerializedName("created")
    private String blogDate;
    @SerializedName("url")
    private String blogUrl="";

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public String getBlogImg() {
        return blogImg;
    }

    public void setBlogImg(String blogImg) {
        this.blogImg = blogImg;
    }

    public String getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(String blogDate) {
        this.blogDate = blogDate;
    }
}
