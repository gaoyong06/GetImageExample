package com.example.gaoyong.getimageexample;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.example.gaoyong.getimageexample.Constants.Config;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * @author gao yong (gaoyong06[at]qq[dot]com)
 */
public class GIEApplication extends Application {

    public UnlimitedDiscCache discCache;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {

        Log.d(Constants.tag, "GIEApplication onCreate is run");
        //启用严格模式
        if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        Log.d(Constants.tag, "GIEApplication initImageLoader cacheDir = " + cacheDir);
        this.discCache = new UnlimitedDiscCache(cacheDir);
        initImageLoader(getApplicationContext(),this.discCache);
    }

    public static void initImageLoader(Context context,UnlimitedDiscCache discCache) {

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true) // default is false
                .cacheOnDisk(true) // default is false
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(discCache) // default
//                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheExtraOptions(480, 800, null)
                .defaultDisplayImageOptions(displayImageOptions) // default
                .writeDebugLogs() // Remove for release app
                .build();

//        config = ImageLoaderConfiguration.createDefault(context);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}


