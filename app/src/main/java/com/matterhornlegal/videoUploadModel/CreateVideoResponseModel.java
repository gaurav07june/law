package com.matterhornlegal.videoUploadModel;

/**
 * Created by gaurav.singh on 6/1/2018.
 */

public class CreateVideoResponseModel {

    private String link;

    private Upload upload;

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
