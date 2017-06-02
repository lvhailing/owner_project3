package com.haidehui.act;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
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
import com.haidehui.fragment.HomeFragment;
import com.haidehui.fragment.MineFragment;
import com.haidehui.model.VersionMo;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ImageView mIvmine;

    private TextView tv_house_resources;
    private TextView tv_discovery;
    private TextView tv_home;
    private TextView tv_mine;

    private List<Fragment> mFragments;
    private HomeFragment tab_home; //首页
    private MineFragment tab_house_resources; //房源
    private MineFragment tab_discovery; //发现
    private MineFragment tab_mine; //我的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_main);
        initView();
        initVP();
        setSelect(0);
        initData();
    }

    private void initVP() {
        mFragments = new ArrayList<>();

        tab_home = new HomeFragment();
        tab_house_resources = new MineFragment();
        tab_discovery = new MineFragment();
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
                setTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int tab = intent.getIntExtra("tab", 0);
            setSelect(tab);
//            requestBulletinUnreadCount();
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        ll_tab_home = (LinearLayout) findViewById(R.id.ll_tab_home);
        ll_tab_house_resources = (LinearLayout) findViewById(R.id.ll_tab_house_resources);
        ll_tab_discovery = (LinearLayout) findViewById(R.id.ll_tab_discovery);
        ll_tab_mine = (LinearLayout) findViewById(R.id.ll_tab_mine);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_house_resources = (ImageView) findViewById(R.id.iv_house_resources);
        iv_discovery = (ImageView) findViewById(R.id.iv_discovery);
        mIvmine = (ImageView) findViewById(R.id.iv_mine);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_house_resources = (TextView) findViewById(R.id.tv_house_resources);
        tv_discovery = (TextView) findViewById(R.id.tv_discovery);
        tv_mine = (TextView) findViewById(R.id.tv_mine);

        ll_tab_home.setOnClickListener(this);
        ll_tab_house_resources.setOnClickListener(this);
        ll_tab_discovery.setOnClickListener(this);
        ll_tab_mine.setOnClickListener(this);
    }

    private void setSelect(int i) {
        setTab(i);
        mViewPager.setCurrentItem(i);
    }

    private void initData() {
        requestData();
    }

    private void setTab(int pos) {
        resetTvs();
        resetImages();

        switch (pos) {
            case 0:
                tv_home.setTextColor(Color.parseColor("#8a0002"));
//                iv_home.setImageResource(R.mipmap.icon_tab_asset_pressed);
                break;
            case 1:
                tv_house_resources.setTextColor(Color.parseColor("#8a0002"));
//                iv_house_resources.setImageResource(R.mipmap.icon_tab_product_pressed);
                break;
            case 2:
                tv_discovery.setTextColor(Color.parseColor("#8a0002"));
//                iv_discovery.setImageResource(R.mipmap.icon_tab_service_pressed);
                break;
            case 3:
                tv_mine.setTextColor(Color.parseColor("#8a0002"));
//                mIvmine.setImageResource(R.mipmap.icon_tab_mine_pressde);
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
//        iv_home.setImageResource(R.mipmap.img_asset_icon_normal);
//        iv_house_resources.setImageResource(R.mipmap.img_product_icon_normal);
//        iv_discovery.setImageResource(R.mipmap.img_news_icon_normal);
//        mIvmine.setImageResource(R.mipmap.img_mine_icon_normal);
    }

    //检查版本更新
    private void requestData() {
        Map<String, Object> param = new HashMap<>();
        param.put("type", "android");
        HtmlRequest.checkVersion(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result != null) {
                            final VersionMo b = (VersionMo) params.result;
                            //后台版本为已停运、未上线，不做处理
                            if (!TextUtils.isEmpty(b.getVersion())) {
                                Toast.makeText(mContext, "new version" + b.getVersion(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

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
                break;
            case R.id.ll_tab_house_resources:  // 房源
                setSelect(1);
                break;
            case R.id.ll_tab_discovery:  // 发现
                setSelect(2);
                break;
            case R.id.ll_tab_mine:  // 我的
                setSelect(3);
                break;

        }
    }

    private long lastTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

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
