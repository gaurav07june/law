package com.matterhornlegal.models;

/**
 * Created by gaurav.singh on 5/2/2018.
 */

public class SignUpResponseModel extends BaseModel {

    private String token;
    private RegisteredUserDetail data;

    public String getToken() {
        return token;
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
}
