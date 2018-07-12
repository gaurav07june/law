package com.matterhornlegal.utils;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seema.gurtatta on 12/19/2017.
 */

public class ApiUtils {
    public interface ApiUrl {
        //String BASE_URL = "http://10.0.0.24/matterhorn/api/";
        String BASE_URL="http://newmediaguru.co/demo/matterhorn/api/";
        String GET_PRACTICE_AREAS_URL=BASE_URL+"users/getPracticeAreas";
        String GET_INDUSTRY_SECTORS_LIST=BASE_URL+"users/getIndustryList";
        String GET_LOCATIONS_LIST=BASE_URL+"users/getLocationlist";
        String LOGIN_URL=BASE_URL+"users/login/";
        String SOCIAL_LOGIN_URL=BASE_URL+"firms/socialLogin";
        String REGISTER_URL=BASE_URL+"users/register/";
        String FORGOT_PASSWORD_URL=BASE_URL+"users/forgotpassword";
        String LOGOUT=BASE_URL+"users/logout";
        String GET_VIDEO_GALLERY=BASE_URL+"firms/getVideoGallery";
        String GET_MY_VIDEO_GALLERY_URL = BASE_URL+"events/getMyVideos";
        String GET_LIVE_STREAM=BASE_URL+"events/getLiveStream";
        String GET_VIDEO_DETAIL=BASE_URL+"firms/getVideoDetail?video_id=";
        String GET_VIDEO_FILTER=BASE_URL+"firms/videoFilter";
        String UPLOAD_VIDEO=BASE_URL+"events/uploadVideo";

        String GET_FIRMS_LIST=BASE_URL+"firms/getListFirms";
        String GET_MY_FIRMS_LIST_URL = BASE_URL +"events/getMyLawfirm";
        String GET_FIRM_DETAIL=BASE_URL+"firms/getFirmDetail/?firm_id=";
        String GET_FIRM_FILTER=BASE_URL+"firms/getFirmFilter";
        String SEND_MESSAGE_URL=BASE_URL+"users/sendMessage";
        String RESET_PASSWORD=BASE_URL+"users/reset";
        String SUBMIT_LAW_FIRM=BASE_URL+"events/submitLawFirm";
        String UPDATE_PROFILE_PIC=BASE_URL+"events/changePicture";
        String GET_HOME_FEED_URL = BASE_URL+"users/homeFeed";
        String EDIT_PROFILE_URL = BASE_URL+"events/editProfile";
        String UPDATE_PROFILE_URL = BASE_URL+"events/updateProfile";
        String GET_BLOG_LIST = BASE_URL+"firms/getBlogSort";
        String GET_EVENT_LIST_URL = BASE_URL+"firms/getListEvents";
        String GET_MY_EVENT_LIST_URL = BASE_URL+"events/getUserEvent";
        String GET_EVENT_DETAIL_URL = BASE_URL +"firms/getEventDetail/?event_id=";
        String SUBMIT_EVENT_URL = BASE_URL + "events/createEvent";
        String GET_VIMEO_USER_QUOTA = "https://api.vimeo.com/me";

        String VIMEO_CREATE_VIDEO = "https://api.vimeo.com/me/videos";
        String DELETE_EVENT_URL = BASE_URL+"events/deleteEvent";
        String EDIT_EVENT_URL = BASE_URL+"events/editEvent";
        String UPDATE_EVENT_URL = BASE_URL+"events/updateEvent";
        String GET_LANGUAGE_URL = BASE_URL+"users/getLanguageList";

        String GET_VIDEO_INDUSTRY_LIST_URL = BASE_URL+"users/videoIndustryList";
        String GET_VIDEO_PRACTICE_LIST_URL= BASE_URL+"users/videoPracticeList";
        String GET_VIDEO_LOCATION_LIST_URL = BASE_URL+"users/videoLocationList";
        String GET_TNC_URL = BASE_URL +"users/terms_of_use";
        String GET_PRIVACY_POLICY_URL = BASE_URL+"users/privacy_policy";

        String CHANGE_PASSWORD_URL = BASE_URL + "events/changePassword";


        //String GET_VIDEO_FEED = BASE_URL+"firms/getVideoGallery";
//        http://10.0.0.24/matterhorn/api/users/getPracticeAreas
//        http://10.0.0.24/matterhorn/api/users/getIndustryList
//        http://10.0.0.24/matterhorn/api/users/getLocationlist
//        http://10.0.0.24/matterhorn/api/users/login/
//        http://10.0.0.24/matterhorn/api/users/register/
//        http://10.0.0.24/matterhorn/api/users/forgotpassword
//        http://10.0.0.24/matterhorn/api/users/reset?token=82swe2mAPyhhI1QyecIc7C1vymyigd
//        http://10.0.0.24/matterhorn/api/users/logout
//        http://10.0.0.24/matterhorn/api/events/getVideoGallery/?user_id=''&api_key=''&page=''
//        http://10.0.0.24/matterhorn/api/events/getLiveStream?user_id=''&api_key=''
//        http://10.0.0.24/matterhorn/api/events/getVideoDetail/?user_id=''&api_key=''&video_id=''
//        http://10.0.0.24/matterhorn/api/events/videoFilter
//        http://10.0.0.24/matterhorn/api/events/uploadVideo
//        http://10.0.0.24/matterhorn/api/firms/getListFirms
//        http://10.0.0.24/matterhorn/api/firms/getFirmDetail/?user_id=12&api_key=12328140294&firm_id=27259
//        http://10.0.0.24/matterhorn/api/firms/getFirmFilter
//        http://10.0.0.24/matterhorn/api/firms/searchListFirm?user_id=''&api_key=''&search=''
//        http://10.0.0.24/matterhorn/api/firms/submitLawForm
//        http://10.0.0.24/matterhorn/api/firms/profile
//        http://10.0.0.24/matterhorn/api/firms/updateProfile
//        http://10.0.0.24/matterhorn/api/firms/changePicture
//        http://10.0.0.24/matterhorn/api/firms/getUserListFirm
//        http://10.0.0.24/matterhorn/api/users/sendMessage
//        http://10.0.0.24/matterhorn/api/events/getEventDetail/?user_id=''&api_key=''&event_id=''
//        http://10.0.0.24/matterhorn/api/events/deleteEvent
//        http://10.0.0.24/matterhorn/api/events/eventSearchList/?user_id=''&api_key=''&search=''
//        http://10.0.0.24/matterhorn/api/events/createEvent

    }

    public interface ApiActionEvents {
        int GET_PRACTICE_AREAS=1;
        int GET_INDUSTRY_SECTORS_LIST=2;
        int GET_LOCATIONS_LIST=3;
        int LOGIN=4;
        int REGISTER=5;
        int FORGOT_PASSWORD=6;
        int LOGOUT=7;
        int GET_VIDEO_GALLERY=8;
        int GET_LIVE_STREAM=9;
        int GET_VIDEO_DETAIL=10;
        int GET_VIDEO_FILTER=11;
        int UPLOAD_VIDEO=12;
        int GET_FIRMS_LIST=13;
        int GET_FIRM_DETAIL=14;
        int GET_FIRM_FILTER=15;
        int SEND_MESSAGE=16;
        int RESET_PASSWORD=17;
        int SUBMIT_LAW_FIRM=18;
        int UPLOAD_PROFILE_PIC = 19;
        int GET_HOME_FEED =20;
        int GET_VIDEO_FEED = 21;
        int EDIT_PROFILE = 22;
        int UPDATE_PROFILE = 23;
        int DUMMY_HIT = 24;

        int VIMEO_USER_QUOTA = 25;
        int VIMEO_CREATE_VIDEO = 26;
        int VIMEO_VIDEO_UPLOAD = 27;
        int GET_EVENTS_LIST = 28;
        int GET_EVENT_DETAIL = 29;
        int SOCIAL_LOGIN=42;
        int UPLOAD_VIDEO_TO_SERVER = 30;
        int SUBMIT_EVENT = 31;
        int GET_MY_VIDEO_GALLERY = 32;
        int GET_MY_FIRM_LIST = 33;
        int DELETE_EVENT = 34;

        int EDIT_EVENT = 35;

        int UPDATE_EVENT = 36;
        int GET_MY_EVENT_LIST = 37;
        int GET_VIDEO_INDUSTRY_LIST = 38;
        int GET_VIDEO_PRACTICE_LIST = 39;
        int GET_VIDEO_LOCATION_LIST = 40;
        int GET_LANGUAGE = 41;
        int GET_TNC = 42;
        int GET_PRIVACY_POLICY = 43;

        int CHANGE_PASSWORD = 44;

    }

    public interface ApiConstants {
        String USER_ID="user_id";
        String API_KEY="api_key";
        String PAGE="page";
        String VIDEO_ID="video_id";
        String FIRM_ID="firm_id";
        String USERNAME="username";
        String USER_PASS="user_pass";
        String EMAIL="email";
        String DISPLAY_NAME="display_name";
        String NOTIFY="notify";
        String SEARCH="search";
        String FIST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String OCCUPATION = "occupation";
        String AREA_OF_INTEREST = "areas_of_interest";

        String TITLE="title";
        String DESCRIPTION="description";
        String ADDRESS="address";
        String PHONE="phone";
        String WEBSITE="website";
        String PRACTICE="practice";
        String INDUSTRY="industry";
        String COUNTRY="location";
        String LANGUAGE = "language";
        String IMAGE="image";

        String PROFILE_PIC = "profile_pic";
        String SORT_DIR = "sort";
        String EVENT_ID= "event_id";
        String LOCATION = "country";
        String VIDEO = "video";
        String OLD_PASS = "old_password";
        String NEW_PASS = "new_password";
        String CONFIRM_PASS = "confirm_password";

        String SUBJECT = "subject";
        String MESSAGE = "message";
    }
    public static Map<String, String> getCommonHeader(String userId, String apiKey){
        Map<String, String>  params = new HashMap<String, String>();
        params.put(ApiConstants.USER_ID, userId);

        params.put(ApiConstants.API_KEY, apiKey);
        Logger.error("common header "+params.toString());

        return params;
    }
    public static Map<String, String> getVimeoVedioUploadHeader(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Tus-Resumable", "1.0.0");

        params.put("Upload-Offset", "0");
        params.put("Content-Type", "application/offset+octet-stream");
        Logger.error("video upload header "+params.toString());
        return params;
    }

    public static Map<String, String> getVimeoCommonHeader(){
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Authorization", "bearer c931f5f75d5b68c908e15f9428675c3e");

        params.put("Accept", "application/vnd.vimeo.*+json;version=3.4");
        //params.put("Content-Type", "application/json");
        Logger.error("user quota header "+params.toString());
        return params;
    }

    public static JsonObject getCreateVideoPayload(long videoSize, String videoTitle){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("approach" ,"tus");
        jsonObject.addProperty("size", videoSize);
        JsonObject finalJson = new JsonObject();
        finalJson.add("upload", jsonObject);
        finalJson.addProperty("name", videoTitle);
        Logger.error("create video payload "+finalJson.toString());
        return finalJson;

    }

    public static JsonObject getLoginPayload(String username, String password) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.USERNAME, username);
        json.addProperty(ApiConstants.USER_PASS, password);
        Logger.error("Login payload: " + json.toString());
        return json;
    }

    public static JsonObject getEventPayload(int eventId) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.EVENT_ID, eventId);
        Logger.error("event payload: " + json.toString());
        return json;
    }
    public static JsonObject getBlogListPayLoad(String sort, int page) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.SORT_DIR,sort );
        json.addProperty(ApiConstants.PAGE, page);
        Logger.error("Login payload: " + json.toString());
        return json;
    }
    public static JsonObject getVideoFeedPayload(int pageNo) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.PAGE, pageNo);
        Logger.error("video payload: " + json.toString());
        return json;
    }
    public static JsonObject getFirmFeedPayload(int pageNo) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.PAGE, pageNo);
        Logger.error("video payload: " + json.toString());
        return json;
    }
    public static JsonObject getEventFeedPayload(int pageNo) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.PAGE, pageNo);
        Logger.error("Event feed payload: " + json.toString());
        return json;
    }
    public static JsonObject getSubmitEventPayload(String title, String description, String shortDesc, int categoryid, String address,
                                                   String country, String website, String phone, String email, String registrationUrl,  String startDate,
                                                   String endDate, String startTime, String endTime, String startmer, String endMer, String imageBase64){
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("description", description);
        json.addProperty("short_desc", shortDesc);
        json.addProperty("category_id", categoryid);
        json.addProperty("address", address);
        json.addProperty("country", country);
        json.addProperty("website", website);
        json.addProperty("phone", phone);
        json.addProperty("email", email);
        json.addProperty("registration_url", registrationUrl);
        json.addProperty("start_date", startDate);
        json.addProperty("end_date", endDate);
        json.addProperty("start_time", startTime);
        json.addProperty("end_time", endTime);
        json.addProperty("start_meridian", startmer);
        json.addProperty("end_meridian", endMer);
        json.addProperty("event_image", imageBase64);
        Logger.error("Submit Event payload: " + json.toString());
        return json;
    }
    public static JsonObject getUpdateEventPayload(int id, String title, String description, String shortDesc, int categoryid, String address,
                                                   String country, String website, String phone, String email, String registrationUrl, String startDate,
                                                   String endDate, String startTime, String endTime, String startmer, String endMer, String imageBase64){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("title", title);
        json.addProperty("description", description);
        json.addProperty("short_desc", shortDesc);
        json.addProperty("category_id", categoryid);
        json.addProperty("address", address);
        json.addProperty("country", country);
        json.addProperty("website", website);
        json.addProperty("phone", phone);
        json.addProperty("email", email);
        json.addProperty("registration_url", registrationUrl);
        json.addProperty("start_date", startDate);
        json.addProperty("end_date", endDate);
        json.addProperty("start_time", startTime);
        json.addProperty("end_time", endTime);
        json.addProperty("start_meridian", startmer);
        json.addProperty("end_meridian", endMer);
        json.addProperty("event_image", imageBase64);
        Logger.error("Submit Event payload: " + json.toString());
        return json;
    }
    public static JsonObject getFilteredFirmListPayload(String industry, String practice, String location, String language, String pageNo) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.INDUSTRY, industry);
        json.addProperty(ApiConstants.PRACTICE, practice);
        json.addProperty(ApiConstants.COUNTRY, location);
        json.addProperty(ApiConstants.LANGUAGE, language);
        json.addProperty(ApiConstants.PAGE, pageNo);
        Logger.error("filtered firm payload: " + json.toString());
        return json;
    }

    public static JsonObject getFilteredVideoListPayload(String industry, String practice, String location, String pageNo) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.INDUSTRY, industry);
        json.addProperty(ApiConstants.PRACTICE, practice);
        json.addProperty(ApiConstants.COUNTRY, location);
        json.addProperty(ApiConstants.PAGE, pageNo);
        Logger.error("filtered video payload: " + json.toString());
        return json;
    }

    public static JsonObject getRegisterPayload(String firstName, String lastName, String username,String email, String userPass,
                                                String occupation, String areasOfInterest){
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.FIST_NAME, firstName);
        json.addProperty(ApiConstants.LAST_NAME, lastName);
        json.addProperty(ApiConstants.USERNAME, username);
        json.addProperty(ApiConstants.EMAIL, email);
        json.addProperty(ApiConstants.USER_PASS, userPass);
        json.addProperty(ApiConstants.OCCUPATION, occupation);
        json.addProperty(ApiConstants.AREA_OF_INTEREST, areasOfInterest);
        Logger.error("Login payload: " + json.toString());
        return json;
    }
    public static JsonObject updateProfilePayload(String firstName, String lastName,String email, String address,
                                                  String phone){
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.FIST_NAME, firstName);
        json.addProperty(ApiConstants.LAST_NAME, lastName);
        json.addProperty(ApiConstants.EMAIL, email);
        json.addProperty(ApiConstants.ADDRESS, address);
        json.addProperty(ApiConstants.PHONE, phone);
        Logger.error("update profile payload: " + json.toString());
        return json;
    }
    public static JsonObject updateProfilePicPayload(String imgBase64String){
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.PROFILE_PIC, imgBase64String);
        Logger.error("update profile pic payload: " + json.toString());
        return json;
    }

    public static JsonObject getForgotPasswordPayload(String email) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.EMAIL, email);
        Logger.error("Forgot password payload: " + json.toString());
        return json;
    }

    public static JsonObject getResetPasswordPayload(String password) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.USER_PASS, password);
        Logger.error("Password payload: " + json.toString());
        return json;
    }

    public static JsonObject getChangePasswordPayload(String currentPass, String newPass, String confirmPass) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.OLD_PASS, currentPass);
        json.addProperty(ApiConstants.NEW_PASS, newPass);
        json.addProperty(ApiConstants.CONFIRM_PASS, confirmPass);
        Logger.error(" change Password payload: " + json.toString());
        return json;
    }
    public static JsonObject getSendMessagePayload(String firstName, String lastName, String email, String phone, String country,
                                                   String subject, String message) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.FIST_NAME, firstName);
        json.addProperty(ApiConstants.LAST_NAME, lastName);
        json.addProperty(ApiConstants.EMAIL, email);
        json.addProperty(ApiConstants.PHONE, phone);
        json.addProperty(ApiConstants.LOCATION, country);
        json.addProperty(ApiConstants.SUBJECT, subject);
        json.addProperty(ApiConstants.MESSAGE, message);
        Logger.error("send message payload: " + json.toString());
        return json;
    }

    public static JsonObject getVideoUploadPayload(String title, String description, String practice , String industry, String country, String videoUrl){
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.TITLE, title);
        json.addProperty(ApiConstants.DESCRIPTION, description);
        json.addProperty(ApiConstants.PRACTICE, practice);
        json.addProperty(ApiConstants.INDUSTRY, industry);
        json.addProperty(ApiConstants.LOCATION, country);
        json.addProperty(ApiConstants.VIDEO, videoUrl);
        Logger.error("video upload payload: " + json.toString());
        return json;
    }

    public static JsonObject getUploadProfilePicturePayload(String stringBase64){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.PROFILE_PIC, stringBase64);
        Logger.error("profilePicPayload"+ jsonObject.toString());
        return jsonObject;
    }
    public static JsonObject getLawFirmsListingPayload(String userId, String apiKey, int page) {
        JsonObject json = new JsonObject();
        json.addProperty(ApiConstants.USER_ID, userId);
//        json.addProperty(ApiConstants.API_KEY, apiKey);
        json.addProperty(ApiConstants.API_KEY, "12328140294");

        json.addProperty(ApiConstants.PAGE, page);
        Logger.error("Law Firms Listing payload: " + json.toString());
        return json;
    }

    public static JsonObject getSubmitLawFirmPayload( String title,String email, String phone, String website, String address,
                                                      String country, String description, String practice,String industry,
                                                      String linkedIn, String twitter, String reference, String imageBase64, String languages){
        JsonObject json = new JsonObject();
        json.addProperty("title", title);
        json.addProperty("email", email);
        json.addProperty("phone", phone);
        json.addProperty("website", website);
        json.addProperty("address", address);
        json.addProperty("country", country);
        json.addProperty("description", description);
        json.addProperty("practice", practice);
        json.addProperty("industry", industry);
        json.addProperty("linkedin", linkedIn);
        json.addProperty("twitter", twitter);
        json.addProperty("reference", reference);
        json.addProperty("image", imageBase64);
        json.addProperty("language", languages);
        Logger.error("submit law firm payload "+json.toString());
        return json;
    }

}
