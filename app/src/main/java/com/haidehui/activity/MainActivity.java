package com.haidehui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.MyApplication;
import com.haidehui.dialog.CheckVersionDialog;
import com.haidehui.fragment.DiscoveryFragment;
import com.haidehui.fragment.HomeFragment;
import com.haidehui.fragment.HouseResourcesFragment;
import com.haidehui.fragment.MineFragment;
import com.haidehui.model.ResultCheckVersionContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.service.AppUpgradeService;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private static final String TYPE = "android";

    private LinearLayout ll_tab_home;    //资产
    private LinearLayout ll_tab_house_resources;    //产品
    private LinearLayout ll_tab_discovery;    //服务
    private LinearLayout ll_tab_mine; //更多

    private ImageView iv_house_resources;
    private ImageView iv_discovery;
    private ImageView iv_home;
    private ImageView iv_mine;

    private TextView tv_house_resources;
    private TextView tv_discovery;
    private TextView tv_home;
    private TextView tv_mine;

    private List<Fragment> mFragments;
    private HomeFragment tab_home; //首页
    private HouseResourcesFragment tab_house_resources; //房源
    private DiscoveryFragment tab_discovery; //发现
    private MineFragment tab_mine; //我的
    private TitleBar title;

    private File destDir = null;
    private File destFile = null;
    private Intent updateIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_main);

//        initRequestPermissions();
        initTopTitle();
        initView();
        initVP();
        setSelect(0);
        initData();
    }

    private void initRequestPermissions() {
//        String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        if (!EasyPermissions.hasPermissions(mContext, perms)) {
//            EasyPermissions.requestPermissions(mContext, "请允许权限进行下载安装", 110, perms);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int tab = intent.getIntExtra("tab", 0);
            setSelect(tab);
        }
    }

    public void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ll_tab_home = (LinearLayout) findViewById(R.id.ll_tab_home);
        ll_tab_house_resources = (LinearLayout) findViewById(R.id.ll_tab_house_resources);
        ll_tab_discovery = (LinearLayout) findViewById(R.id.ll_tab_discovery);
        ll_tab_mine = (LinearLayout) findViewById(R.id.ll_tab_mine);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_house_resources = (ImageView) findViewById(R.id.iv_house_resources);
        iv_discovery = (ImageView) findViewById(R.id.iv_discovery);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_house_resources = (TextView) findViewById(R.id.tv_house_resources);
        tv_discovery = (TextView) findViewById(R.id.tv_discovery);
        tv_mine = (TextView) findViewById(R.id.tv_mine);

        ll_tab_home.setOnClickListener(this);
        ll_tab_house_resources.setOnClickListener(this);
        ll_tab_discovery.setOnClickListener(this);
        ll_tab_mine.setOnClickListener(this);
    }

    private void initVP() {
        mFragments = new ArrayList<>();

        tab_home = new HomeFragment();
        tab_house_resources = new HouseResourcesFragment();
        tab_discovery = new DiscoveryFragment();
        tab_mine = new MineFragment();

        mFragments.add(tab_home);
        mFragments.add(tab_house_resources);
        mFragments.add(tab_discovery);
        mFragments.add(tab_mine);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                setSelect(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    public void setSelect(int i) {
        if (i == 0) {
            tab_home.resetScrollViewSmooth();
        } else {
            tab_home.onMyPause();
        }
        if (i == 1) {
            tab_house_resources.requestDefaultData();
        }
        if (i == 2) {
            tab_discovery.upDateCycleIndex();
            if (tab_discovery.investmentGuideFr != null && tab_discovery.roadShowFr != null) {
                tab_discovery.investmentGuideFr.upDateInvestmentGuideList();
                tab_discovery.roadShowFr.upDateRoadShowList();
            }
        } else {
            tab_discovery.onMyPause();
        }
        setTab(i);
    }

    private void initData() {
        requestData(); //检查版本更新
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setTab(int pos) {
        resetTvs();
        resetImages();

        switch (pos) {
            case 0:
                tv_home.setTextColor(Color.parseColor("#8a0002"));
                iv_home.setImageResource(R.mipmap.bg_home_pressed);
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                tv_house_resources.setTextColor(Color.parseColor("#8a0002"));
                iv_house_resources.setImageResource(R.mipmap.bg_house_resources_pressed);
                break;
            case 2:
                tv_discovery.setTextColor(Color.parseColor("#8a0002"));
                iv_discovery.setImageResource(R.mipmap.bg_discovery_pressed);
                break;
            case 3:
                tv_mine.setTextColor(Color.parseColor("#8a0002"));
                iv_mine.setImageResource(R.mipmap.bg_mine_pressed);
                break;

        }
    }

    private void resetTvs() {
        tv_home.setTextColor(Color.parseColor("#999999"));
        tv_house_resources.setTextColor(Color.parseColor("#999999"));
        tv_discovery.setTextColor(Color.parseColor("#999999"));
        tv_mine.setTextColor(Color.parseColor("#999999"));
    }

    private void resetImages() {
        iv_home.setImageResource(R.mipmap.bg_home_normal);
        iv_house_resources.setImageResource(R.mipmap.bg_house_resources_normal);
        iv_discovery.setImageResource(R.mipmap.bg_discovery_normal);
        iv_mine.setImageResource(R.mipmap.bg_mine_normal);
    }

    /**
     * 检查版本更新
     */
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("type", "android");

        HtmlRequest.checkVersion(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result != null) {
                    final ResultCheckVersionContentBean b = (ResultCheckVersionContentBean) params.result;
                    if (!TextUtils.isEmpty(b.getVersion())) {
                        if (!b.getVersion().equals(SystemInfo.sVersionName)) {
                            String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                            if (!EasyPermissions.hasPermissions(mContext, perms)) {
                                EasyPermissions.requestPermissions(mContext, "请允许权限进行下载安装", 110, perms);
                            }

                            showDialog(b);
                        } else {
                            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                                if (destDir == null) {
                                    destDir = new File(Environment.getExternalStorageDirectory().getPath() + MyApplication.mDownloadPath);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     *  提示更新的弹框
     * @param b
     */
    public void showDialog(final ResultCheckVersionContentBean b){
        CheckVersionDialog dialog = new CheckVersionDialog(MainActivity.this, new CheckVersionDialog.OnCheckVersion() {
            @Override
            public void onConfirm() {
                updateIntent = new Intent(MainActivity.this, AppUpgradeService.class);
                updateIntent.putExtra("titleId", R.string.app_chinese_name);
                updateIntent.putExtra("downloadUrl", b.getUrl());

//                String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//                if (!EasyPermissions.hasPermissions(mContext, perms)) {
//                    EasyPermissions.requestPermissions(mContext, "请允许权限进行下载安装", 110, perms);
//
//                } else {
//                }
                    MainActivity.this.startService(updateIntent);
            }

            @Override
            public void onCancel() {
            }
        }, "发现新版本,是否更新", b.getForcedUpgrade());

        dialog.setCancelable(false);
        dialog.show();
    }
//    private static int REQUESTPERMISSION = 110 ;
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == REQUESTPERMISSION){
//            if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    if(updateIntent!=null)
//                        startService(updateIntent);
//                }else{
//                    //提示没有权限，安装不了咯
//                    Toast.makeText(this, "抱歉，您没有权限安装此应用", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public boolean checkApkFile(String apkFilePath) {
        boolean result = false;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_home:  // 首页
                setSelect(0);
//                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_tab_house_resources:  // 房源
//                setSelect(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_tab_discovery:  // 发现
//                setSelect(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.ll_tab_mine:  // 我的
//                PreferenceUtil.setLogin(true);
                if (PreferenceUtil.isLogin()) {
//                    setSelect(3);
                    mViewPager.setCurrentItem(3);
                } else {
                    Intent i_login = new Intent(this, LoginActivity.class);
                    startActivity(i_login);

                }

                break;

        }
    }

    private long lastTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    // 双击退出函数
    private void exitBy2Click() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTime = currentTime;
        }
    }
}
