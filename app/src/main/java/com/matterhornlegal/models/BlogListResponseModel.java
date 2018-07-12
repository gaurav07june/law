package com.matterhornlegal.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by karan.kalsi on 5/28/2018.
 */

public class BlogListResponseModel {
    private int status;
    private String message;
    private int total_pages;
    @SerializedName("data")
    private List<BlogModel> blogList;

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

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<BlogModel> getBlogList() {
        return blogList;
    }

    public void setBlogList(List<BlogModel> blogList) {
        this.blogList = blogList;
    }
}
