package com.matterhornlegal.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.matterhornlegal.activities.SubmitVideoActivity;
import com.matterhornlegal.activities.SubmitVideoThanksActivity;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.ToastUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UploadVideoBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(AppConstants.EXTRA.VIDEO_UPLOAD_BROAD_INTENT);
        if(action.equals(AppConstants.commonConstants.STOP_UPLOAD_ACTION_NAME)){
            ToastUtils.showToast(context, "Upload cancelled.");
            context.stopService(SubmitVideoActivity.uploadVideoServiceIntent);
        }else if (action.equals(AppConstants.commonConstants.UPLOAD_VIDEO_SUCCESS_ACTION)){
            context.startActivity(new Intent(context, SubmitVideoThanksActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
