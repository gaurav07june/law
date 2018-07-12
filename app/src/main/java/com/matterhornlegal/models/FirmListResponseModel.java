package com.matterhornlegal.models;

import java.util.List;

/**
 * Created by gaurav.singh on 5/10/2018.
 */

public class FirmListResponseModel {

    private String message;

    private int status;

    private List<LawFirmData> data;

    private int total_pages;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<LawFirmData> getData() {
        return data;
    }

    public void setData(List<LawFirmData> data) {
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
