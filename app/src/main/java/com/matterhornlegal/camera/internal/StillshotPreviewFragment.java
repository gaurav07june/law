package com.matterhornlegal.camera.internal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.matterhornlegal.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class StillshotPreviewFragment extends BaseGalleryFragment {

    private ImageView mImageView;

    /**
     * Reference to the bitmap, in case 'onConfigurationChange' event comes, so we do not recreate the bitmap
     */
    private static Bitmap mBitmap;

    public static StillshotPreviewFragment newInstance(String outputUri, boolean allowRetry, int primaryColor) {
        final StillshotPreviewFragment fragment = new StillshotPreviewFragment();
        fragment.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString("output_uri", outputUri);
        args.putBoolean(CameraIntentKey.ALLOW_RETRY, allowRetry);
        args.putInt(CameraIntentKey.PRIMARY_COLOR, primaryColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mcam_fragment_stillshot, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = (ImageView) view.findViewById(R.id.stillshot_imageview);



        mRetry.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(mOutputUri,mImageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            try {
                mBitmap.recycle();
                mBitmap = null;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    /**
     * Sets bitmap to ImageView widget
     */
    private void setImageBitmap() {


        ImageLoader.getInstance().displayImage(mOutputUri,mImageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.retry || v.getId() == R.id.close_btn )
            mInterface.onRetry(mOutputUri);
        else if (v.getId() == R.id.confirm)
            mInterface.useMedia(mOutputUri);

    }

}