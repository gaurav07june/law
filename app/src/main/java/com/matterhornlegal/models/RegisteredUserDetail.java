package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 5/2/2018.
 */

public class RegisteredUserDetail {

    private String first_name;

    private String phone;

    private String email;

    private String address;

    private String api_key;

    private String last_name;

    private String user_id;

    private String profile_pic;

    private String occupation;
    private String areas_of_interest;

    private boolean profileUpdated;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isProfileUpdated() {
        return profileUpdated;
    }

    public void setProfileUpdated(boolean profileUpdated) {
        this.profileUpdated = profileUpdated;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAreas_of_interest() {
        return areas_of_interest;
    }

    public void setAreas_of_interest(String areas_of_interest) {
        this.areas_of_interest = areas_of_interest;
    }

    public String getFirst_name() {

        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [first_name = "+first_name+", phone = "+phone+", email = "+email+", address = "+address+", api_key = "+api_key+", last_name = "+last_name+", user_id = "+user_id+", profile_pic = "+profile_pic+"]";
    }
}
