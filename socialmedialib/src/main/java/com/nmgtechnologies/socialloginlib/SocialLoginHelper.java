package com.nmgtechnologies.socialloginlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.twitter.sdk.android.core.Twitter.TAG;

/**
 * Created by karan.kalsi on 12/28/2017.
 */

public class SocialLoginHelper {
    private static final String topCardUrl = "https://api.linkedin.com/v1/people/~:(first-name,last-name,email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private static final String FB_PROFILE_PIC_URL = "https://graph.facebook.com/%s/picture?type=large";
  private static final int RC_SIGN_IN=1001;
    private Activity mActivity = null;
    private CallbackManager facebookCallbackManager = null;
    private LISessionManager linkedInSessionManager = null;
    private TwitterAuthClient mTwitterAuthClient = null;
    private SocialLoginCallback socialLoginCallback = null;
    private GoogleSignInClient googleSignInClient=null;
    private GoogleSignInOptions googleSignInOptions=null;
    private static final SocialLoginHelper ourInstance = new SocialLoginHelper();

    public static SocialLoginHelper getInstance() {
        return ourInstance;
    }

    private SocialLoginHelper() {
    }

    public void init(Context context) {
        facebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookLoginCallback);

        String consumer_key=context.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        String consumer_secret=context.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        TwitterConfig config = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumer_key,consumer_secret ))
                .debug(true)
                .build();
        Twitter.initialize(config);

        mTwitterAuthClient = new TwitterAuthClient();
         googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        linkedInSessionManager= LISessionManager.getInstance(context);
    }

    public void initForActivity(Activity activity) {
        mActivity = activity;
        if (activity instanceof SocialLoginCallback) {
            socialLoginCallback = (SocialLoginCallback) activity;
        }
        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);


    }

    private AuthListener linkedInCallback = new AuthListener() {
        @Override
        public void onAuthSuccess() {
            APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
            apiHelper.getRequest(mActivity, topCardUrl, new ApiListener() {
                @Override

                public void onApiSuccess(ApiResponse s) {
     try {


                       LoginData loginData = new LoginData();
                        loginData.setFirstName(s.getResponseDataAsJson().get("firstName").toString());
                        loginData.setLastName(s.getResponseDataAsJson().get("lastName").toString());
                        loginData.setSocialId(getLinkedInSocialId(s.getResponseDataAsJson().get("publicProfileUrl").toString()));
                        loginData.setEmail(s.getResponseDataAsJson().get("emailAddress").toString());
                        loginData.setProfilePicUrl(s.getResponseDataAsJson().get("pictureUrl").toString());

         if (socialLoginCallback!=null)socialLoginCallback.onSocialLoginSuccess(loginData);

     }catch (Exception e){
                    }
                }
                @Override

                public void onApiError(LIApiError error) {
                   if (socialLoginCallback!=null)socialLoginCallback.onSocialLoginFailed();
                }
            });

        }

        @Override
        public void onAuthError(LIAuthError error) {
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginFailed();
        }
    };

    private String getLinkedInSocialId(String publicProfileUrl) {
        String id="";
        String[] ids = publicProfileUrl.split("/");
        if (ids.length>0)
        {
            id=  ids[ids.length-1];
        }
        return id;
    }

    private FacebookCallback<LoginResult> facebookLoginCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {

                            } else {

                                LoginData loginData = new LoginData();
                                String name = me.optString("name");
                                String email = me.optString("email");
                                String[] names = name.split(" ");

                                String id = me.optString("id");
                                loginData.setProfilePicUrl(String.format(FB_PROFILE_PIC_URL,id));
                                if (names.length > 0)
                                    loginData.setFirstName(names[0]);
                                if (names.length > 1)
                                    loginData.setLastName(names[1]);
                                loginData.setSocialId(id);

                                if (email != null && !email.isEmpty()) {
                                    loginData.setEmail(email);

                                }

                                socialLoginCallback.onSocialLoginSuccess(loginData);
                            }
                        }
                    });
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginCancelled();
        }

        @Override
        public void onError(FacebookException error) {
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginFailed();
        }

    };
    private Callback<TwitterSession> twitterLoginCallback = new Callback<TwitterSession>() {
        @Override
        public void success(final Result<TwitterSession> result) {

            TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false,false)
            .enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> userResult) {
                    LoginData loginData = new LoginData();
                    loginData.setFirstName(userResult.data.name);
                    loginData.setEmail( userResult.data.email);
                    loginData.setSocialId( String.valueOf(userResult.data.id));
                    loginData.setProfilePicUrl(userResult.data.profileImageUrl);

                    if (socialLoginCallback != null)
                        socialLoginCallback.onSocialLoginSuccess(loginData);
                      }

                @Override
                public void failure(TwitterException exc) {
                    if (socialLoginCallback != null) socialLoginCallback.onSocialLoginFailed();
                }
            });
        }

        @Override
        public void failure(TwitterException exception) {
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginFailed();
        }
    };


    public void loginViaFacebook() {
        if (mActivity != null)
            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email"));


    }

    public void logoutFromFacebook(){

        LoginManager.getInstance().logOut();
    }

    public void logoutFromGoogle(){
        if (googleSignInClient != null){
            googleSignInClient.signOut();
        }

    }

    public void logoutFromLinkedIn(){
        LISessionManager.getInstance(getApplicationContext()).clearSession();
    }

    public void loginViaTwitter() {
        if (mActivity != null)
            mTwitterAuthClient.authorize(mActivity, twitterLoginCallback);

    }
    public void loginViaLinkedIn() {
        Scope scope = Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
        linkedInSessionManager.init(mActivity, scope, linkedInCallback, true);


    }
    public void loginViaGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (facebookCallbackManager != null)
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (mTwitterAuthClient != null)
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(mActivity, requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            LoginData loginData = new LoginData();
            loginData.setFirstName(account.getDisplayName().replace(account.getFamilyName(), ""));
            loginData.setLastName(account.getFamilyName());
            loginData.setEmail(account.getEmail());
            loginData.setSocialId(account.getId());
            loginData.setProfilePicUrl(account.getPhotoUrl()!=null ? account.getPhotoUrl().toString() : "");
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginSuccess(loginData);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            if (socialLoginCallback != null) socialLoginCallback.onSocialLoginFailed();
        }
    }
}
