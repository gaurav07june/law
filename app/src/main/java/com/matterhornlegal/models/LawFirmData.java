package com.matterhornlegal.models;

import java.io.Serializable;

/**
 * Created by gaurav.singh on 5/10/2018.
 */

public class LawFirmData implements Serializable{

    private String[] practice_area;


    private String title;

    private int firm_id;

    private String location;

    private String image;

    public String[] getPractice_area() {
        return practice_area;
    }

    public void setPractice_area(String[] practice_area) {
        this.practice_area = practice_area;
    }


    /*public String[] getPractice_area() {
        return practice_area;
    }

    public void setPractice_area(String[] practice_area) {
        this.practice_area = practice_area;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [practice_area = "+practice_area+", title = "+title+", firm_id = "+firm_id+", location = "+location+", image = "+image+"]";
    }
}
