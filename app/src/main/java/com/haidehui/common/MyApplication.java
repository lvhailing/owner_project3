package com.haidehui.common;

import android.app.Application;

import com.haidehui.R;
import com.haidehui.network.http.APNManager;
import com.haidehui.photo_preview.fresco.ImageLoader;
import com.haidehui.uitls.ImageLoaderManager;
import com.haidehui.uitls.NetworkUtils;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.uitls.CityDataHelper;

import java.io.InputStream;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static String mAppId;
    public static String mDownloadPath;

    public static MyApplication getInstance() {
        return instance;
    }

    private static final String TAG = "Init";

    private CityDataHelper dataHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NetworkUtils.setContext(this);
        PreferenceUtil.initialize(this);
        SystemInfo.initialize(this);
        //imageLoader初始化
        ImageLoaderManager.initImageLoader(this);
        mDownloadPath = "/" + mAppId + "/download";
        //fresco初始化
        ImageLoader.getInstance().initImageLoader(getResources(), 1);
        APNManager.getInstance().checkNetworkType(this);


        //拷贝数据库文件
        dataHelper=CityDataHelper.getInstance(this);
        InputStream in = this.getResources().openRawResource(R.raw.province);
        dataHelper.copyFile(in, CityDataHelper.DATABASE_NAME, CityDataHelper.DATABASES_DIR);
    }

}
