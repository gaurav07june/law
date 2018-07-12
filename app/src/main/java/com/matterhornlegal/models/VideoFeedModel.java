package com.matterhornlegal.models;

import java.io.Serializable;

/**
 * Created by gaurav.singh on 5/8/2018.
 */

public class VideoFeedModel implements Serializable{

    private String user_name;

    private String video_description;

    private String video_url;

    private String video_created;

    private String videos_count;

    private String video_title;

    private String video_id;

    private String video_thumbnail;

    private String user_thumbnail;

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getUser_thumbnail() {
        return user_thumbnail;
    }

    public void setUser_thumbnail(String user_thumbnail) {
        this.user_thumbnail = user_thumbnail;
    }

    public String getVideo_created() {
        return video_created;
    }

    public void setVideo_created(String video_created) {
        this.video_created = video_created;
    }

    public String getUser_name ()
    {
        return user_name;
    }

    public void setUser_name (String user_name)
    {
        this.user_name = user_name;
    }

    public String getVideo_description ()
    {
        return video_description;
    }

    public void setVideo_description (String video_description)
    {
        this.video_description = video_description;
    }

    public String getVideo_url ()
    {
        return video_url;
    }

    public void setVideo_url (String video_url)
    {
        this.video_url = video_url;
    }

    public String getVideos_count ()
    {
        return videos_count;
    }

    public void setVideos_count (String videos_count)
    {
        this.videos_count = videos_count;
    }

    public String getVideo_title ()
    {
        return video_title;
    }

    public void setVideo_title (String video_title)
    {
        this.video_title = video_title;
    }

    public String getVideo_id ()
    {
        return video_id;
    }

    public void setVideo_id (String video_id)
    {
        this.video_id = video_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_name = "+user_name+", video_description = "+video_description+", video_url = "+video_url+", videos_count = "+videos_count+", video_title = "+video_title+", video_id = "+video_id+"]";
    }
}
