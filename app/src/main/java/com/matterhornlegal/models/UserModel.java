package com.matterhornlegal.models;

/**
 * Created by seema.gurtatta on 12/20/2017.
 */

public class UserModel {

    private String user_id, email, first_name, last_name;
    private String api_key, address, phone, profile_pic;

    public String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

}
