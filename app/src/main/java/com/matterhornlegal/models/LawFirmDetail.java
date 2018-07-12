package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 5/11/2018.
 */

public class LawFirmDetail {

    private String firm_title;

    private String[] practice_area;

    private String location_lat;

    private String phone;

    private int firm_id;

    private String location_lng;

    private String email;

    private String location;

    private String image;

    private String websiteUrl;

    private String[] language;

    private String[] industry;

    private String firm_description;

    private String[] country;

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getFirm_title ()
    {
        return firm_title;
    }

    public void setFirm_title (String firm_title)
    {
        this.firm_title = firm_title;
    }

    public String[] getPractice_area ()
    {
        return practice_area;
    }

    public void setPractice_area (String[] practice_area)
    {
        this.practice_area = practice_area;
    }

    public String getLocation_lat ()
    {
        return location_lat;
    }

    public void setLocation_lat (String location_lat)
    {
        this.location_lat = location_lat;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public String getLocation_lng ()
    {
        return location_lng;
    }

    public void setLocation_lng (String location_lng)
    {
        this.location_lng = location_lng;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String[] getLanguage ()
    {
        return language;
    }

    public void setLanguage (String[] language)
    {
        this.language = language;
    }

    public String[] getIndustry ()
    {
        return industry;
    }

    public void setIndustry (String[] industry)
    {
        this.industry = industry;
    }

    public String getFirm_description ()
    {
        return firm_description;
    }

    public void setFirm_description (String firm_description)
    {
        this.firm_description = firm_description;
    }

    public String[] getCountry ()
    {
        return country;
    }

    public void setCountry (String[] country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [firm_title = "+firm_title+", practice_area = "+practice_area+", location_lat = "+location_lat+", phone = "+phone+", firm_id = "+firm_id+", location_lng = "+location_lng+", email = "+email+", location = "+location+", image = "+image+", language = "+language+", industry = "+industry+", firm_description = "+firm_description+", country = "+country+"]";
    }
}
