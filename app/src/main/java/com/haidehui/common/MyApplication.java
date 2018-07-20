package com.haidehui.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.haidehui.R;
import com.haidehui.network.http.APNManager;
import com.haidehui.photo_preview.fresco.ImageLoader;
import com.haidehui.uitls.ImageLoaderManager;
import com.haidehui.uitls.NetworkUtils;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.uitls.CityDataHelper;

import java.io.InputStream;
import java.util.HashSet;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static String mAppId;
    public static String mDownloadPath;
    private static final String TAG = "Init";
    private CityDataHelper dataHelper;
    private BroadcastReceiver mReceiver;
    public String netType;
    IntentFilter mFilter;
    HashSet<NetListener> mListeners = new HashSet<NetListener>();


    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NetworkUtils.setContext(this);
        PreferenceUtil.initialize(this);
        SystemInfo.initialize(this);
        initNetReceiver();

        //imageLoader初始化
        ImageLoaderManager.initImageLoader(this);
        mAppId = getString(R.string.app_id);
        mDownloadPath = "/" + mAppId + "/download";

        //fresco初始化
        ImageLoader.getInstance().initImageLoader(getResources(), 1);
        APNManager.getInstance().checkNetworkType(this);

        //拷贝数据库文件
        dataHelper = CityDataHelper.getInstance(this);
        InputStream in = this.getResources().openRawResource(R.raw.province);
        dataHelper.copyFile(in, CityDataHelper.DATABASE_NAME, CityDataHelper.DATABASES_DIR);

        //ShareSDK 初始化
//        ShareSDK.initSDK(instance);

        //3.X版本以上含3.X版本，ShareSDK 初始化
        // 通过代码注册你的AppKey和AppSecret
//        MobSDK.init(instance, "1ea86a798f5d6", "69d1ab82675b878c6061887a6ab49279");

        //3.1.4版本 ShareSDK 初始化
//        MobSDK.init(this);
    }

    public interface NetListener {
        void onNetWorkChange(String netType);
    }

    /**
     * 加入网络监听
     *
     * @param l
     * @return
     */
    public boolean addNetListener(NetListener l) {
        boolean rst = false;
        if (l != null && mListeners != null) {
            rst = mListeners.add(l);
        }
        return rst;
    }

    /**
     * 移除网络监听
     *
     * @param l
     * @return
     */
    public boolean removeNetListener(NetListener l) {
        return mListeners.remove(l);
    }

    /**
     * 初始化网络监听器
     */
    private void initNetReceiver() {
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = manager.getActiveNetworkInfo();
                    if (info != null && info.isConnected()) {
                        netType = info.getTypeName();
                    } else {
                        netType = "";
                    }
                    for (NetListener lis : mListeners) {
                        if (lis != null) {
                            lis.onNetWorkChange(netType);
                        }
                    }
                }
            }
        };
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    /**
     * 注册网络监听器
     */
    public void registReceiver() {
        try {
            registerReceiver(mReceiver, mFilter);
        } catch (Exception e) {
        }
    }

    /**
     * 注销网络监听器
     */
    public void unRegisterNetListener() {
        if (mListeners != null) {
            mListeners.clear();
        }
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
