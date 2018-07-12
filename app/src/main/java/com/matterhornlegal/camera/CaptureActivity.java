package com.matterhornlegal.camera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.matterhornlegal.camera.internal.BaseCaptureActivity;
import com.matterhornlegal.camera.internal.CameraFragment;


public class CaptureActivity extends BaseCaptureActivity {


    @Override
    @NonNull
    public Fragment getFragment() {
        return CameraFragment.newInstance();
    }
}