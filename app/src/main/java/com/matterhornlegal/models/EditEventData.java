package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 6/14/2018.
 */

public class EditEventData {

    private String phone;

    private String website;

    private String end_meridian;

    private int category_id;

    private String start_meridian;

    private String short_desc;

    private String country;

    private int id;

    private String title;

    private String end_date;

    private String end_time;

    private String address;

    private String email;

    private String description;

    private String start_time;

    private String start_date;

    private String event_image;

    private String registration_url;

    public String getRegistration_url() {
        return registration_url;
    }

    public void setRegistration_url(String registration_url) {
        this.registration_url = registration_url;
    }

    public String getWebsite ()
    {
        return website;
    }

    public void setWebsite (String website)
    {
        this.website = website;
    }

    public String getEnd_meridian ()
    {
        return end_meridian;
    }

    public void setEnd_meridian (String end_meridian)
    {
        this.end_meridian = end_meridian;
    }



    public String getStart_meridian ()
    {
        return start_meridian;
    }

    public void setStart_meridian (String start_meridian)
    {
        this.start_meridian = start_meridian;
    }

    public String getShort_desc ()
    {
        return short_desc;
    }

    public void setShort_desc (String short_desc)
    {
        this.short_desc = short_desc;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }



    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getEnd_date ()
    {
        return end_date;
    }

    public void setEnd_date (String end_date)
    {
        this.end_date = end_date;
    }

    public String getEnd_time ()
    {
        return end_time;
    }

    public void setEnd_time (String end_time)
    {
        this.end_time = end_time;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }



    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getStart_time ()
    {
        return start_time;
    }

    public void setStart_time (String start_time)
    {
        this.start_time = start_time;
    }

    public String getStart_date ()
    {
        return start_date;
    }

    public void setStart_date (String start_date)
    {
        this.start_date = start_date;
    }

    public String getEvent_image ()
    {
        return event_image;
    }

    public void setEvent_image (String event_image)
    {
        this.event_image = event_image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone = "+phone+", website = "+website+", end_meridian = "+end_meridian+", category_id = "+category_id+", start_meridian = "+start_meridian+", short_desc = "+short_desc+", country = "+country+", id = "+id+", title = "+title+", end_date = "+end_date+", end_time = "+end_time+", address = "+address+", email = "+email+", description = "+description+", start_time = "+start_time+", start_date = "+start_date+", event_image = "+event_image+"]";
    }
}
