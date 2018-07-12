package com.matterhornlegal;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.RequestManager;
import com.nmgtechnologies.socialloginlib.SocialLoginHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by karan.kalsi on 9/28/2017.
 */

public class MatterHornApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;
        initImageLoader(this);
        RequestManager.initializeWith(this.getApplicationContext());
    }

    public static final String TAG = MatterHornApp.class
            .getSimpleName();
    private static MatterHornApp mInstance;

    private static final int MAX_SIZE = 100 * 1024 * 1024;
    private DisplayImageOptions imageOptions;

    private void initImageLoader(Context context) {
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.placeholder_rect)
                .showImageOnFail(R.drawable.placeholder_rect)
                .showImageOnLoading(R.drawable.placeholder_rect)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAX_SIZE))
                .defaultDisplayImageOptions(imageOptions)
                .diskCache(new UnlimitedDiskCache(new File(Utils.getAppHiddenDirectory(context))))
//                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
        SocialLoginHelper.getInstance().init(getApplicationContext());
    }
    public static synchronized MatterHornApp getInstance() {
        return mInstance;
    }
}
