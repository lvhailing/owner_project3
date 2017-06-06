package com.haidehui.common;

import android.app.Application;

import com.haidehui.network.http.APNManager;
import com.haidehui.photo_preview.fresco.ImageLoader;
import com.haidehui.uitls.ImageLoaderManager;
import com.haidehui.uitls.NetworkUtils;
import com.haidehui.uitls.PreferenceUtil;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static String mAppId;
    public static String mDownloadPath;

    public static MyApplication getInstance() {
        return instance;
    }

    private static final String TAG = "Init";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NetworkUtils.setContext(this);
        PreferenceUtil.initialize(this);
        //imageLoader初始化
        ImageLoaderManager.initImageLoader(this);
        //fresco初始化
        ImageLoader.getInstance().initImageLoader(getResources(), 1);
        APNManager.getInstance().checkNetworkType(this);
    }


}
