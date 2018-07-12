package com.matterhornlegal.models;

import java.io.Serializable;

/**
 * Created by gaurav.singh on 6/8/2018.
 */

public class EventData implements Serializable {

    private String event_start_date;

    private String event_venue;

    private int event_id;

    private double event_longitute;

    private String event_end_date;

    private String event_title;

    private double event_latitute;

    private String event_image;

    public String getEvent_start_date ()
    {
        return event_start_date;
    }

    public void setEvent_start_date (String event_start_date)
    {
        this.event_start_date = event_start_date;
    }

    public String getEvent_venue ()
    {
        return event_venue;
    }

    public void setEvent_venue (String event_venue)
    {
        this.event_venue = event_venue;
    }


    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public double getEvent_longitute() {
        return event_longitute;
    }

    public void setEvent_longitute(double event_longitute) {
        this.event_longitute = event_longitute;
    }

    public double getEvent_latitute() {
        return event_latitute;
    }

    public void setEvent_latitute(double event_latitute) {
        this.event_latitute = event_latitute;
    }

    public String getEvent_end_date ()
    {
        return event_end_date;
    }

    public void setEvent_end_date (String event_end_date)
    {
        this.event_end_date = event_end_date;
    }

    public String getEvent_title ()
    {
        return event_title;
    }

    public void setEvent_title (String event_title)
    {
        this.event_title = event_title;
    }


    public String getEvent_image ()
    {
        return event_image;
    }

    public void setEvent_image (String event_image)
    {
        this.event_image = event_image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [event_start_date = "+event_start_date+", event_venue = "+event_venue+", event_id = "+event_id+", event_longitute = "+event_longitute+", event_end_date = "+event_end_date+", event_title = "+event_title+", event_latitute = "+event_latitute+", event_image = "+event_image+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventData eventData = (EventData) o;
        String current_start_date = event_start_date!=null &&  event_start_date.split(" ").length>1 ?
                event_start_date.split(" ")[0] : "";
        String other_start_date=    eventData.event_start_date!=null &&  eventData.event_start_date.split(" ").length>1 ?
                eventData.event_start_date.split(" ")[0] : "";
      return current_start_date.equals(other_start_date);
    }

    @Override
    public int hashCode() {
        return event_start_date != null ? event_start_date.hashCode() : 0;
    }
}
