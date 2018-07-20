package com.haidehui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.activity.HouseDetailActivity;
import com.haidehui.activity.OverseaProjectDetailActivity;
import com.haidehui.activity.WebForShareActivity;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.MyRollViewPager;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 底部导航---发现模块
 */
public class DiscoveryFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private LinearLayout ll_vp; //顶部轮播图
    private LinearLayout ll_point_container; // 轮播图下面的圆点
    private Activity context;
    private MouldList<ResultCycleIndex2B> picList;
    private TextView tv_discovery_tab1, tv_discovery_tab2; // 投资指南，产品路演
    private ViewPager vp;
    private View v_line; // 投资指南，产品路演下的下划线
    private ArrayList<Fragment> fragments;
    private int screenWidth; // 屏幕宽度
    private int line_width; // 下划线宽度
    public InvestmentGuideFragment investmentGuideFr;
    public ProductRoadshowFragment roadShowFr;
    private MyRollViewPager rollViewPager;
    private ResultCycleIndex2B cycleIndex2B;
    private String linkType;
    private String target;
    private Intent intent;

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
    }

    private void initView(View mView) {
        context = getActivity();
        picList = new MouldList<ResultCycleIndex2B>();

        ll_vp = (LinearLayout) mView.findViewById(R.id.ll_vp);
        ll_point_container = (LinearLayout) mView.findViewById(R.id.ll_point_container);
        tv_discovery_tab1 = (TextView) mView.findViewById(R.id.tv_discovery_tab1);
        tv_discovery_tab2 = (TextView) mView.findViewById(R.id.tv_discovery_tab2);

        vp = (ViewPager) mView.findViewById(R.id.vp_living_room);
        v_line = mView.findViewById(R.id.line);

        tv_discovery_tab1.setOnClickListener(this);
        tv_discovery_tab2.setOnClickListener(this);

        // 默认设置第0个title状态
        setTitleStyle(0);

        fragments = new ArrayList<Fragment>();
        investmentGuideFr = new InvestmentGuideFragment();
        roadShowFr = new ProductRoadshowFragment();
        fragments.add(investmentGuideFr);
        fragments.add(roadShowFr);

        //屏幕宽度
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        // 设置下划线宽度
        line_width = screenWidth / fragments.size();
        v_line.getLayoutParams().width = line_width;
        v_line.requestLayout();

        vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            tv_discovery_tab1.setTextColor(getResources().getColor(R.color.txt_light_gray));
            tv_discovery_tab2.setTextColor(getResources().getColor(R.color.txt_orange));
        }

    }

    //控制线条滚动，每点击一次调用一次
    private void setLineStyle(int pos) {
        ViewPropertyAnimator.animate(v_line).translationX(screenWidth / 2 * pos).setDuration(300);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_discovery_tab1:  // 投资指南
                setTitleStyle(0);
                setLineStyle(0);
                vp.setCurrentItem(0);
                investmentGuideFr.upDateInvestmentGuideList();

                break;
            case R.id.tv_discovery_tab2: // 产品路演
                setTitleStyle(1);
                setLineStyle(1);
                vp.setCurrentItem(1);
                roadShowFr.upDateRoadShowList();

                break;

        }
    }

    // 请求轮播图数据
    private void requestCycleIndex() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("params", "params");
        HtmlRequest.getDiscoveryCycleIndex(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null && params.result != null) {
                    picList = (MouldList<ResultCycleIndex2B>) params.result;
                }
                freshVP();
            }
        });
    }

    private void freshVP() {
        if (rollViewPager == null) {
            //第一次从后台获取到数据
            rollViewPager = new MyRollViewPager(context, picList, ll_point_container);
            rollViewPager.setCycle(true);
            rollViewPager.setOnMyListener(new MyRollViewPager.MyClickListener() {
                @Override
                public void onMyClick(int position) {
                    if (picList != null && picList.size() > 0) {
                        cycleIndex2B = picList.get(position);
                        linkType = cycleIndex2B.getLinkType();
                        target = cycleIndex2B.getTargetUrl();

                        if (linkType.equals("url")) { // 跳转h5网页
                            intent = new Intent(context, WebForShareActivity.class);
                            intent.putExtra("type", WebForShareActivity.WEBTYPE_HTML);
                            intent.putExtra("url", target);
                            startActivity(intent);
                        } else if (linkType.equals("appProject")) { // 海外项目详情
                            intent = new Intent(context, OverseaProjectDetailActivity.class);
                            intent.putExtra("pid", target);
                            startActivity(intent);
                        } else if (linkType.equals("appHouse")) { // 房源详情
                            intent = new Intent(context, HouseDetailActivity.class);
                            intent.putExtra("hid", target);
                            startActivity(intent);
                        } else if (linkType.equals("appVideo")) { // 路演视频详情
                            if (PreferenceUtil.isLogin()) {
                                intent = new Intent(context, WebForShareActivity.class);
                                intent.putExtra("type", WebForShareActivity.WEBTYPE_ROADSHOW_DETAILS);
                                intent.putExtra("id", target);
                                intent.putExtra("uid", PreferenceUtil.getUserId());
                                intent.putExtra("title", "产品路演详情");
                                startActivity(intent);
                            } else {
                                intent = new Intent(context, WebForShareActivity.class);
                                intent.putExtra("type", WebForShareActivity.WEBTYPE_ROADSHOW_DETAILS);
                                intent.putExtra("id", target);
                                intent.putExtra("uid", "0");
                                intent.putExtra("title", "产品路演详情");
                                startActivity(intent);
                            }
                        } else if (linkType.equals("appInvestGuide")) { // 投资指南详情
                            if (PreferenceUtil.isLogin()) {  // 用户登录情况下加载投资指南详情页要传用户的userId：investmentGuide/detail/id/uid
                                intent = new Intent(context, WebForShareActivity.class);
                                intent.putExtra("type", WebForShareActivity.WEBTYPE_INVESTMENT_GUIDE_DETAILS);
                                intent.putExtra("id", target);
                                intent.putExtra("uid", PreferenceUtil.getUserId());
                                intent.putExtra("title", "投资指南详情");
                                startActivity(intent);
                            } else { // 用户未登录情况下加载投资指南详情页用户的userId传成 0：investmentGuide/detail/id/0
                                intent = new Intent(context, WebForShareActivity.class);
                                intent.putExtra("type", WebForShareActivity.WEBTYPE_INVESTMENT_GUIDE_DETAILS);
                                intent.putExtra("id", target);
                                intent.putExtra("uid", "0");
                                intent.putExtra("title", "投资指南详情");
                                startActivity(intent);
                            }
                        } else if (linkType.equals("none")) { // 无跳转
                        }
                    }
                }
            });
            rollViewPager.startRoll();
            ll_vp.addView(rollViewPager);
        } else {
            //第二或之后获取到数据，刷新vp
            rollViewPager.setPicList(picList);
            rollViewPager.reStartRoll();
        }
    }

    // 底部tab之间切换时，刷新轮播图数据
    public void upDateCycleIndex() {
        requestCycleIndex();
    }

    public void onMyPause() {
        if (rollViewPager != null) {
            rollViewPager.pause();
        }
    }
}
