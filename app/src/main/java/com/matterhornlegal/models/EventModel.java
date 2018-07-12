package com.matterhornlegal.models;

/**
 * Created by karan.kalsi on 10/30/2017.
 */

public class EventModel   {

    private String title;
    private String img;
    private String date;
    private boolean isApproved;
    private boolean isImportant;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventModel that = (EventModel) o;

        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }
}
