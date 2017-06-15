package com.haidehui.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.CycleAdapter;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 底部导航---发现模块
 */
public class DiscoveryFragment extends Fragment implements View.OnClickListener, CycleAdapter.ImageCycleViewListener {
    private View mView;
    private LinearLayout mViewPager; //顶部轮播图
    private LinearLayout ll_down_dots; // 轮播图下面的圆点
    private DisplayImageOptions options;
    private CycleAdapter cycleAdapter;//自定义viewPager
    private ScrollView scrollView;
    private Context context;
    //    private ResultProductIndexBean productIndexBean;
    private MouldList<ResultCycleIndex2B> homeCycleBean;
    private Intent intent;
    private TextView tv_discovery_tab1, tv_discovery_tab2; // 投资指南，产品路演
    private ViewPager viewPager;
    private View v_line;
    private ArrayList<Fragment> fragments;
    private int screenWidth; // 屏幕宽度
    private int line_width; // 下划线宽度
//    private int tagerX;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_discovery, container, false);
            try {
                initView(mView);
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        requestBoutiqueHouseData();
//        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            requestBoutiqueHouseData();
//            scrollView.smoothScrollTo(0, 0);
        } else {

        }
    }

    private void initView(View mView) {
        context = getActivity();
//        productIndexBean = new ResultProductIndexBean();
        homeCycleBean = new MouldList<ResultCycleIndex2B>();
//        scrollView = (ScrollView) mView.findViewById(R.id.scrollview);

        mViewPager = (LinearLayout) mView.findViewById(R.id.viewpager);
        ll_down_dots = (LinearLayout) mView.findViewById(R.id.ll_down_dots);
        tv_discovery_tab1 = (TextView) mView.findViewById(R.id.tv_discovery_tab1);
        tv_discovery_tab2 = (TextView) mView.findViewById(R.id.tv_discovery_tab2);

        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        v_line = mView.findViewById(R.id.line); // tab下的下划线

        tv_discovery_tab1.setOnClickListener(this);
        tv_discovery_tab2.setOnClickListener(this);


        // 默认设置第0个title状态
        setTitleStyle(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new InvestmentGuideFragment());
        fragments.add(new Tab2Fragment());

        //屏幕宽度
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        // 设置下划线宽度
        line_width = screenWidth / fragments.size();
        v_line.getLayoutParams().width = line_width;
        v_line.requestLayout();

        viewPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setTitleStyle(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //此处在手滑动时 会被不停调用
                float tagerX = arg0 * line_width + arg2 / fragments.size();
                ViewPropertyAnimator.animate(v_line).translationX(tagerX).setDuration(0);

//                ObjectAnimator oa = ObjectAnimator.ofFloat(v_line, "translationX", tagerX);
//                oa.setDuration(0);
//                oa.start();

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }

    // 修改文字的颜色
    private void setTitleStyle(int pos) {
        if (pos == 0) {
            tv_discovery_tab1.setTextColor(getResources().getColor(R.color.txt_orange));
            tv_discovery_tab2.setTextColor(getResources().getColor(R.color.txt_light_gray));
        } else if (pos == 1) {
            tv_discovery_tab1.setTextColor(getResources().getColor(R.color.txt_orange));
            tv_discovery_tab2.setTextColor(getResources().getColor(R.color.txt_light_gray));
        }

    }

    //控制线条滚动，每点击一次调用一次
    private void setLineStyle(int pos) {
        ViewPropertyAnimator.animate(v_line).translationX(screenWidth / 2 * pos).setDuration(300);
    }

    private void initData() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.banner_one).showImageOnFail(R.drawable.banner_one).resetViewBeforeLoading(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

        requestCycleIndex();
    }


    /**
     * 请求轮播图数据
     */
    private void requestData() {
        cycleAdapter = new CycleAdapter(context, homeCycleBean, options);
        cycleAdapter.setNetAndLinearLayoutMethod(ll_down_dots);
        cycleAdapter.setOnImageListener(new CycleAdapter.ImageCycleViewListener() {
            @Override
            public void onImageClick(int postion, View imageView) {
                if (homeCycleBean != null && homeCycleBean.size() != 0) {
                   /* if (!TextUtils.isEmpty(homeCycleBean.get(postion % homeCycleBean.size()).getUrl())) {
                        Intent i_web = new Intent(context, WebActivity.class);
                        i_web.putExtra("type", WebActivity.WEBTYPE_BANNER);
                        i_web.putExtra("url", homeCycleBean.get(postion % homeCycleBean.size()).getUrl());
                        i_web.putExtra("title", homeCycleBean.get(postion % homeCycleBean.size()).getDescription());
                        getActivity().startActivity(i_web);
                    }*/
                }
            }
        });
        cycleAdapter.setCycle(true);
        cycleAdapter.startRoll();
        mViewPager.addView(cycleAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_discovery_tab1:  // 投资指南
                setTitleStyle(0);
                setLineStyle(0);
                viewPager.setCurrentItem(0);

                break;
            case R.id.tv_discovery_tab2: // 产品路演
                setTitleStyle(1);
                setLineStyle(1);
                viewPager.setCurrentItem(1);

                break;

        }
    }


    // 请求轮播图数据
    private void requestCycleIndex() {
        Map<String, Object> param = new HashMap<>();
        param.put("appType", "android");
        HtmlRequest.getCycleIndex(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        homeCycleBean = (MouldList<ResultCycleIndex2B>) params.result;
                    }
                }
                requestData();
            }
        });
    }

    @Override
    public void onImageClick(int postion, View imageView) {
    }
}