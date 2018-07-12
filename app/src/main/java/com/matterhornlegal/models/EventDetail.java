package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/11/2018.
 */

public class EventDetail {

    private String[] event_category;

    private String event_venue;

    private int event_id;

    private String event_description;

    private String event_title;

    private String event_end_date;

    private double event_latitute;

    private String event_short_desc;

    private String event_start_date;

    private String event_website_url;

    private double event_longitute;

    private String user_thumb;

    private String event_image;

    private String event_registration_url;

    public String getEvent_registration_url() {
        return event_registration_url;
    }

    public void setEvent_registration_url(String event_registration_url) {
        this.event_registration_url = event_registration_url;
    }

    public String[] getEvent_category() {
        return event_category;
    }

    public void setEvent_category(String[] event_category) {
        this.event_category = event_category;
    }

    public String getEvent_venue() {
        return event_venue;
    }

    public void setEvent_venue(String event_venue) {
        this.event_venue = event_venue;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public double getEvent_latitute() {
        return event_latitute;
    }

    public void setEvent_latitute(double event_latitute) {
        this.event_latitute = event_latitute;
    }

    public String getEvent_short_desc() {
        return event_short_desc;
    }

    public void setEvent_short_desc(String event_short_desc) {
        this.event_short_desc = event_short_desc;
    }

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_website_url() {
        return event_website_url;
    }

    public void setEvent_website_url(String event_website_url) {
        this.event_website_url = event_website_url;
    }

    public double getEvent_longitute() {
        return event_longitute;
    }

    public void setEvent_longitute(double event_longitute) {
        this.event_longitute = event_longitute;
    }

    public String getUser_thumb() {
        return user_thumb;
    }

    public void setUser_thumb(String user_thumb) {
        this.user_thumb = user_thumb;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }
}
