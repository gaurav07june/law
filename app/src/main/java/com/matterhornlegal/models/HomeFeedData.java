package com.matterhornlegal.models;

import java.util.List;

/**
 * Created by gaurav.singh on 6/6/2018.
 */

public class HomeFeedData {

    private List<LawFirmData> firm_data;

    private List<VideoFeedModel> video_data;

    private List<BlogModel> blog_data;

    public List<LawFirmData> getFirm_data() {
        return firm_data;
    }

    public void setFirm_data(List<LawFirmData> firm_data) {
        this.firm_data = firm_data;
    }

    public List<VideoFeedModel> getVideo_data() {
        return video_data;
    }

    public void setVideo_data(List<VideoFeedModel> video_data) {
        this.video_data = video_data;
    }

    public List<BlogModel> getBlog_data() {
        return blog_data;
    }

    public void setBlog_data(List<BlogModel> blog_data) {
        this.blog_data = blog_data;
    }
}

