package com.matterhornlegal.models;

import java.util.List;

/**
 * Created by gaurav.singh on 6/8/2018.
 */

public class EventListResponseModel {

    private String message;

    private int status;

    private List<EventData> data;

    private int total_pages;

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

    public List<EventData> getData() {
        return data;
    }

    public void setData(List<EventData> data) {
        this.data = data;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", data = "+data+", total_pages = "+total_pages+"]";
    }
}
