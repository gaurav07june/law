package com.nmgtechnologies.socialloginlib;

/**
 * Created by karan.kalsi on 12/28/2017.
 */

public interface SocialLoginCallback {
    void onSocialLoginSuccess(LoginData loginData);
    void onSocialLoginFailed();
    void onSocialLoginCancelled();
}
