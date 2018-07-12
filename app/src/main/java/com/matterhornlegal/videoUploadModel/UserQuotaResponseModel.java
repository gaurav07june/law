package com.matterhornlegal.videoUploadModel;

/**
 * Created by gaurav.singh on 6/1/2018.
 */

public class UserQuotaResponseModel {

    private String resource_key;

    private String bio;

    private String location;

    private UploadQuota upload_quota;

    public String getResource_key() {
        return resource_key;
    }

    public void setResource_key(String resource_key) {
        this.resource_key = resource_key;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UploadQuota getUpload_quota() {
        return upload_quota;
    }

    public void setUpload_quota(UploadQuota upload_quota) {
        this.upload_quota = upload_quota;
    }
}
