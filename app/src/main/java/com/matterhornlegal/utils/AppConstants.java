package com.matterhornlegal.utils;

import com.matterhornlegal.R;

/**
 * Created by karan.kalsi on 9/28/2017.
 */

public class AppConstants {
    public enum VIEW_TYPE{LIST,GRID,CALENDAR}

    public interface DEMO_PAGES {
        int COUNT = 2;
        int[] DEMO_TITLE = {R.string.app_demo_1_title, R.string.app_demo_2_title};
        int[] DEMO_DESC = {R.string.app_demo_1_desc, R.string.app_demo_2_desc};
        int[] DEMO_LOGO = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    }

    public static final int[] HOME_HEADERS_TXT =
            {
                    R.string.home_header_1,
                    R.string.home_header_2,
                    R.string.home_header_3,
            };
    public static final int[] HOME_HEADERS_IMG =
            {
                    R.drawable.home_header1_bg,
                    R.drawable.home_header2_bg,
                    R.drawable.home_header3_bg,
            };

    public interface INTENT_REQUEST_CODES{
        int DOCUMENT_REQUEST=101;
        int CAMERA_REQUEST=102;
        int GALLERY_REQUEST=103;
        int VIDEO_REQUEST = 104;
        int VIDEO_SELECT_REQUEST = 105;
    }
    public interface TIME
    {
        int HOME_HEADER_CHANGE_TIME=2000;
        int HOME_DRAWER_DELAY=200;
        int CLICK_DELAY=500;

    }

    public interface commonConstants{
        String RESET_PASS_URL_PARAM_TOKEN = "token";
        int PROFILE_IMAGE_PICK = 21;
        int CAMERA_PIC_REQUEST = 101;
        int GALLERY_PIC_REQUEST = 102;
        String STOP_UPLOAD_ACTION_NAME = "action_stop_uploading";
        String UPLOAD_VIDEO_BROAD_INTENT = "com.matterhornlegal.UPLOAD_VIDEO_INTENT";
        String UPLOAD_VIDEO_SUCCESS_ACTION = "com.matterhornlegal.UPLOAD_SUCCESS";
    }
    public interface Prefrences {
        String PREF_NAME = "PIS";
        String USER_ID = "userId";
        String USER_NAME = "userName";
        String TOKEN = "token";
        String REGISTERED_USER_DETAIL = "reg_user_detail";
        String GUEST_USER = "guest_user";
        String FROM_ACTIVITY = "pref_from_activity";
        String DIRECTORY_INITIALIZED = "directory_initialized";

        String COUNTRY_LIST_PREF = "pref_country_list";
        String PRACTICE_AREA_PREF = "pref_practice_area";
        String INDUSTRY_SECTOR_PREF = "pref_industry_sector";
        String LANGUAGE_LIST_PREF = "pref_language_list";
     }
    public interface IntentConstants{
        String FIRM_ID = "firm_id";
        String VIDEO_ID = "video_id";
        String VIDEO_FILE_NAME = "video_file_name";
        String VIDEO_SIZE = "video_size";
        String EVENT_ID = "event_id";
        String FIRM_DATA = "firm_data";
        String VIDEO_DATA = "video_data";
    }

    public interface SORT_DIR
    {
        String ASC="asc";
        String DSC="dsc";
    }
    public interface EXTRA
    {
        String BLOG_DATA="extra_blog_data";
        String EVENT_DATA="extra_event_data";
        String EVENT_ID = "extra_event_id";
        String FROM_ACTIVITY = "extra_from_activity";
        String SERVICE_RESULT = "extra_service_result";
        String EXTRA_VIDEO_UPLOAD_REQUEST_DATA = "extra_video_upload_req_data";
        String VIDEO_UPLOAD_BROAD_INTENT = "extra_video_upload_broad_intent";
    }
    public interface FRAGMENTS_CONST{
        String FIRM_FRAGMENT = "firm_fragment";
        String EVENT_FRAGMENT = "event_fragment";
        String MY_EVENT_FRAGMENT = "my_event_fragment";
    }
    public interface ACTIVITY_CONST{
        String EVENT_DETAIL_ACTIVITY = "eventDetailActivity";
        String EVENT_FRAGMENT = "eventFragment";
        String MY_EVENT_FRAGMENT = "myEventFragment";
    }

    public interface videoUploadExtra{
        int EXTRA_UPLOAD_PROCESS_STARTED = 7;
        int EXTRA_CREATE_VIDEO = 0;
        int EXTRA_CREATE_VIDEO_SUCCESS = 8;
        int EXTRA_UPLODING_TO_VIMEO = 1;
        int EXTRA_UPLOADING_TO_SERVER = 2;
        int EXTRA_CREATE_VIDEO_ERROR = 3;
        int EXTRA_VIMEO_UPLOAD_ERROR = 4;
        int EXTRA_VIMEO_UPLOAD_SUCCESS = 5;
        int EXTRA_SERVER_UPLOAD_SUCCESS = 6;
        int EXTRA_SERVER_UPLOAD_ERROR = 9;
    }

}
