package com.matterhornlegal.models;

/**
 * Created by seema.gurtatta on 12/19/2017.
 */

public class LoginResponseModel {


    private int status;
    private String message;
    private String token;
    private RegisteredUserDetail data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RegisteredUserDetail getData() {
        return data;
    }

    public void setData(RegisteredUserDetail data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public RegisteredUserDetail getUser_details() {
        return data;
    }
}
