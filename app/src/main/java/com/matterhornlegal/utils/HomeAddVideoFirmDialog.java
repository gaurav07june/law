package com.matterhornlegal.utils;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.matterhornlegal.R;
import com.matterhornlegal.activities.SplashActivity;
import com.matterhornlegal.activities.SubmitEventActivity;
import com.matterhornlegal.activities.SubmitLawFirmActivity;
import com.matterhornlegal.activities.SubmitVideoActivity;
import com.matterhornlegal.models.AppGlobalData;

/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class HomeAddVideoFirmDialog {
    private Dialog dialog;
    private View dummy_container;
    private float videoY=0;
    private float eventY=0;
    private float firmY=0;
    private static final long ANIMATION_DURATION=500;
    private static final long ANIMATION_DELAY_FACTOR=100;
    boolean isDismissPending=false;
    boolean isShowPending=false;
    public HomeAddVideoFirmDialog(final Activity activity)
    {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.home_add_video_firm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
         dummy_container = dialog.findViewById(R.id.dummy_container);
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    if (!isDismissPending && !isShowPending)
                    animateDismiss();
                }
                return true;
            }
        });
        dialog.findViewById(R.id.home_add_event_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDismissPending && !isShowPending)
                    animateDismiss();
            }
        });

        dialog.findViewById(R.id.submit_video_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, SubmitVideoActivity.class));

            }
        });

        dialog.findViewById(R.id.submit_firm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = AppGlobalData.getInstance().getToken();
                String id = AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id();
                /*String token=SharedPrefsUtils.getSharedPrefString(activity, AppConstants.Prefrences.PREF_NAME,AppConstants.Prefrences.TOKEN,"");
                String id=SharedPrefsUtils.getSharedPrefString(activity, AppConstants.Prefrences.PREF_NAME,AppConstants.Prefrences.USER_ID,"");
                */
                if(!Utils.isNullOrEmpty(token) && !Utils.isNullOrEmpty(id)) {
                    activity.startActivity(new Intent(activity, SubmitLawFirmActivity.class));
                }else{
                    activity.startActivity(new Intent(activity, SplashActivity.class));
                }
            }
        });

        dialog.findViewById(R.id.submit_event_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, SubmitEventActivity.class));
            }
        });

    }
public void show()
{
    try{
        if(dialog==null )return;
        {
            dialog.show();
            animateShow();
        }
    }
    catch (Exception e)
    {

    }

}
    private void animateDismiss()
    {

        isDismissPending=true;
        final View video_btn =dialog.findViewById(R.id.submit_video_btn);
        final View event_btn =dialog.findViewById(R.id.submit_event_btn);
        final View firm_btn =dialog.findViewById(R.id.submit_firm_btn);
        firm_btn.post(new Runnable() {
            @Override
            public void run() {
                videoY = video_btn.getY();
                firmY = firm_btn.getY();
                eventY = event_btn.getY();
                firm_btn.animate().y(dummy_container.getHeight()+firm_btn.getHeight()).setDuration(ANIMATION_DURATION).setListener(null);
            }
        });

        event_btn.postDelayed(new Runnable() {
            @Override
            public void run() {
                event_btn.animate().y(dummy_container.getHeight()+event_btn.getHeight()).setDuration(ANIMATION_DURATION);
            }
        },ANIMATION_DELAY_FACTOR);

        video_btn.postDelayed(new Runnable() {
            @Override
            public void run() {
                video_btn.animate()
                        .y(dummy_container.getHeight()+video_btn.getHeight())
                        .setDuration(ANIMATION_DURATION)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

                                    video_btn.setY(videoY);
                                    firm_btn.setY(firmY);
                                    event_btn.setY(eventY);
                                    isDismissPending=false;
                                   dismiss();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
            }
        },ANIMATION_DELAY_FACTOR*2);


    }

private void animateShow()
{
    isShowPending=true;
    final View video_btn =dialog.findViewById(R.id.submit_video_btn);
    final View event_btn =dialog.findViewById(R.id.submit_event_btn);
    final View firm_btn =dialog.findViewById(R.id.submit_firm_btn);
    video_btn.post(new Runnable() {
        @Override
        public void run() {
            videoY = video_btn.getY();
            firmY = firm_btn.getY();
            eventY = event_btn.getY();

            video_btn.setY(dummy_container.getHeight()+video_btn.getHeight());
            event_btn.setY(dummy_container.getHeight()+event_btn.getHeight());
            firm_btn.setY(dummy_container.getHeight()+firm_btn.getHeight());
            video_btn.animate().y(videoY).setDuration(ANIMATION_DURATION).setListener(null);
        }
    });

    event_btn.postDelayed(new Runnable() {
        @Override
        public void run() {
            event_btn.animate().y(eventY).setDuration(ANIMATION_DURATION);
        }
    },ANIMATION_DELAY_FACTOR);

    firm_btn.postDelayed(new Runnable() {
        @Override
        public void run() {
            firm_btn.animate().y(firmY).setDuration(ANIMATION_DURATION)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isShowPending=false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    },ANIMATION_DELAY_FACTOR*2);
}

    public void dismiss()
    {
        try{
        if(dialog==null )return;
        dialog.dismiss();
    }
    catch (Exception e)
    {

    }
    }

    public boolean isShowing()
    {
        return dialog!=null && dialog.isShowing();
    }
}
