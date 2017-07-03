package com.haidehui.act;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.uitls.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends FragmentActivity {

    private Thread mThread;
    private MyHandler mHandler;
    private MyRunnable mRunnable;

    private static String TAG = "splashactivity";

    private ArrayList<Fragment> fgList;
    private ImageView[] indicator_imgs = new ImageView[3];// 存放引到图片数组


    private ViewPager mViewPager;
    private String pushBean = null;
    public static boolean isForeground = false;

    public static final String APP_ID = "2882303761517425837";
    public static final String APP_KEY = "5191742577837";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        initView(PreferenceUtil.isFirst());


    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    public void registerMessageReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }


    private void initView(boolean is) {
        ImageView iv = (ImageView) findViewById(R.id.splash_img);
        mViewPager = (ViewPager) findViewById(R.id.splashviewpager);


        iv.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        iv.setBackgroundResource(R.mipmap.splash);

        //	设置状态
//		PreferenceUtil.setLogin(true);
//		PreferenceUtil.setGestureChose(true);

        mHandler = new MyHandler();
        mRunnable = new MyRunnable();
        mThread = new Thread(mRunnable);
        mThread.start();


    }

    @Override
    protected void onDestroy() {

        if (fgList != null) {
            fgList.clear();
            fgList = null;
        }
        PreferenceUtil.setFirst(false);
        unbindDrawables(mViewPager);
        System.gc();


        super.onDestroy();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//		setIntent(intent);
        if (getIntent().getStringExtra("pushBean") == null) {
            setIntent(intent);
        } else {

        }
        pushBean = getIntent().getStringExtra("pushBean");
//		Toast.makeText(this,"Splash======pushBean"+pushBean,Toast.LENGTH_LONG).show();

    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN:
                    if (PreferenceUtil.isGestureChose()) {
                        Intent i = new Intent(SplashActivity.this, GestureVerifyActivity.class);
                        i.putExtra("from", Urls.ACTIVITY_SPLASH);
                        i.putExtra("title", getResources().getString(R.string.gesture_edit_title));
                        i.putExtra("message", "手势密码");
                        startActivity(i);
                        finish();
                    } else {
                        Intent iMain = new Intent(SplashActivity.this, MainActivity.class);
                        iMain.putExtra("pushBean", pushBean);
                        startActivity(iMain);
                        finish();
                    }
                    break;
                case NOLOGIN:
                    Intent iMain = new Intent(SplashActivity.this, MainActivity.class);
                    iMain.putExtra("pushBean", pushBean);
                    startActivity(iMain);
                    finish();
                    break;
                case NOPWD:
                    Intent iMain_nopw = new Intent(SplashActivity.this, MainActivity.class);
                    iMain_nopw.putExtra("pushBean", pushBean);
                    startActivity(iMain_nopw);
                    finish();

                    break;
                default:
                    break;
            }
        }

    }

    private static final int LOGIN = 0;
    private static final int NOLOGIN = 1;
    private static final int NOPWD = 2;

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(2400);
                Message msg = new Message();
                if (PreferenceUtil.isLogin()) {
                    if (TextUtils.isEmpty(PreferenceUtil.getGesturePwd())) {
                        msg.what = NOPWD; // 如果未设置
                    } else {
                        msg.what = LOGIN; // 已设置手势密码
                    }
                } else {
                    msg.what = NOLOGIN; // 未登录
                }
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    /**
     * 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
     *
     * @param context
     * @param className
     * @return
     */
    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className)) {
            return false;
        }
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE).getString(KEY_GUIDE_ACTIVITY, "");// 取得所有类名 如 com.my.MainActivity
        if (mResultStr.equalsIgnoreCase("false")) {
            return false;
        } else {
            return true;
        }
    }

}
