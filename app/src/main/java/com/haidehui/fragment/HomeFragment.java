package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.HotHouseListActivity;
import com.haidehui.act.HouseDetailActivity;
import com.haidehui.act.OverseaProjectDetailActivity;
import com.haidehui.act.OverseaProjectListActivity;
import com.haidehui.act.WebActivity;
import com.haidehui.act.WebForShareActivity;
import com.haidehui.adapter.BoutiqueHouseAdapter;
import com.haidehui.common.Urls;
import com.haidehui.model.HomeIndex2B;
import com.haidehui.model.HomeIndex3B;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.MyExpandViewPager;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.MyRollViewPager;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 首页模块
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private Context context;
    private ScrollView scrollView;
    private LinearLayout ll_vp; //顶部轮播图
    private LinearLayout ll_point_container; // 轮播图下面的圆点
    private MouldList<ResultCycleIndex2B> picList;
    /**
     * 精品房源相关数据暂且隐藏，此处现换成公司、团队、平台等介绍
     */
    private MyListView myListView; // 精品房源推荐列表
    private BoutiqueHouseAdapter myAdapter; // 精品房源 Adapter
    private MouldList<HomeIndex3B> boutiqueHouseList = new MouldList<>(); // 精品房源列表

    private TextView tv_hot_house; // 最热房源
    private TextView tv_oversea_project; // 海外项目
    private TextView tv_customer_service; // 我的客服
    private LinearLayout ll_home_notice; // 公告布局
    private TextView tv_home_notice; // 公告标题
    private TextView tv_no_house; // 精品房源无数据时显示的提示语
    private Intent intent;
    private HomeIndex2B homeIndexData;
    private String userId;
    private MyRollViewPager rollViewPager;
    private RelativeLayout rl_empty_house; // 无精品房源 显示的布局
    private ResultCycleIndex2B cycleIndex2B; //  ResultCycleIndex2B 类型的对象
    private String linkType; // 轮播图跳转类型（url:h5页面跳转、appProject:app项目详情、appHouse:app房源详情、appVideo:路演视频详情、appInvestGuide:投资指南、none:无跳转）
    private String target; // url时是链接，其他情况为相关id
    private TextView tv_company_tab1; // 公司
    private TextView tv_team_tab2; // 团队
    private TextView tv_platform_tab3; // 平台
    private MyExpandViewPager expandViewPager;
    private View v_line; // 公司、团队、平台下面的横线
    private ArrayList<Fragment> fragments;
    private int screenWidth; // 屏幕宽度
    private int line_width; // 下划线宽度
    private CompanyFragment companyFragment;
    private TeamFragment teamFragment;
    private PlatFormFragment platFormFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
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
//        Log.i("hh", "首页---Fragment----onResume");
//        scrollView.smoothScrollTo(0, 0);
        resetScrollViewSmooth();
    }

    public void resetScrollViewSmooth() {
        requestCycleIndex(); // 请求轮播图数据
        requestHomeIndexData(); // 请求首页数据

    }

    private void initView(View mView) {
        context = getActivity();
        picList = new MouldList<ResultCycleIndex2B>();

//        rl_empty_house = (RelativeLayout) mView.findViewById(R.id.rl_empty_house);
//        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
//        ImageView img_empty = (ImageView) mView.findViewById(R.id.img_empty);
//        tv_empty.setText("暂无精品房源");
//        rl_empty_house.setVisibility(View.GONE);
//        img_empty.setBackgroundResource(R.mipmap.ic_empty_house_resources);

//        scrollView = (ScrollView) mView.findViewById(R.id.scrollView);
        ll_vp = (LinearLayout) mView.findViewById(R.id.ll_vp);
        ll_point_container = (LinearLayout) mView.findViewById(R.id.ll_point_container);
        tv_hot_house = (TextView) mView.findViewById(R.id.tv_hot_house);
        tv_oversea_project = (TextView) mView.findViewById(R.id.tv_oversea_project);
        tv_customer_service = (TextView) mView.findViewById(R.id.tv_customer_service);
        tv_home_notice = (TextView) mView.findViewById(R.id.tv_home_notice);
        ll_home_notice = (LinearLayout) mView.findViewById(R.id.ll_home_notice);
//        myListView = (MyListView) mView.findViewById(R.id.lv);

        tv_company_tab1 = (TextView) mView.findViewById(R.id.tv_company_tab1);
        tv_team_tab2 = (TextView) mView.findViewById(R.id.tv_team_tab2);
        tv_platform_tab3 = (TextView) mView.findViewById(R.id.tv_platform_tab3);
        expandViewPager = (MyExpandViewPager) mView.findViewById(R.id.vp);
        v_line = mView.findViewById(R.id.line);

        // 默认设置第0个title状态
        setTabTitleStyle(0);

        fragments = new ArrayList<Fragment>();
        companyFragment = new CompanyFragment();
        teamFragment = new TeamFragment();
        platFormFragment = new PlatFormFragment();
        fragments.add(companyFragment);
        fragments.add(teamFragment);
        fragments.add(platFormFragment);

        //屏幕宽度
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        // 设置下划线宽度
        line_width = screenWidth / fragments.size();
        v_line.getLayoutParams().width = line_width;
        v_line.requestLayout();

        expandViewPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        expandViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setTabTitleStyle(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //此处在手滑动时 会被不停调用
                float tagerX = arg0 * line_width + arg2 / fragments.size();
                ViewPropertyAnimator.animate(v_line).translationX(tagerX).setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        tv_hot_house.setOnClickListener(this);
        tv_oversea_project.setOnClickListener(this);
        tv_customer_service.setOnClickListener(this);
        tv_home_notice.setOnClickListener(this);
        ll_home_notice.setOnClickListener(this);
        tv_company_tab1.setOnClickListener(this);
        tv_team_tab2.setOnClickListener(this);
        tv_platform_tab3.setOnClickListener(this);
    }

    // 修改Tab按钮文字颜色
    private void setTabTitleStyle(int pos) {
        if (pos == 0) {
            tv_company_tab1.setTextColor(getResources().getColor(R.color.txt_orange));
            tv_team_tab2.setTextColor(getResources().getColor(R.color.txt_light_gray));
            tv_platform_tab3.setTextColor(getResources().getColor(R.color.txt_light_gray));
        } else if (pos == 1) {
            tv_company_tab1.setTextColor(getResources().getColor(R.color.txt_light_gray));
            tv_team_tab2.setTextColor(getResources().getColor(R.color.txt_orange));
            tv_platform_tab3.setTextColor(getResources().getColor(R.color.txt_light_gray));
        } else if (pos == 2) {
            tv_company_tab1.setTextColor(getResources().getColor(R.color.txt_light_gray));
            tv_team_tab2.setTextColor(getResources().getColor(R.color.txt_light_gray));
            tv_platform_tab3.setTextColor(getResources().getColor(R.color.txt_orange));
        }
    }

    //控制线条滚动，每点击一次调用一次
    private void setLineStyle(int pos) {
        ViewPropertyAnimator.animate(v_line).translationX(screenWidth / 3 * pos).setDuration(300);
    }

    private void initData() {
        userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Intent intent = new Intent(context, HouseDetailActivity.class);
//                intent.putExtra("hid", boutiqueHouseList.get(position).getHid());
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hot_house:  // 最热房源
                intent = new Intent(context, HotHouseListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_oversea_project: // 海外项目
                intent = new Intent(context, OverseaProjectListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_customer_service: // 我的客服
                intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + getString(R.string.phone_number));
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.ll_home_notice: // 公告的点击监听
                if (!TextUtils.isEmpty(homeIndexData.getBid())) {
                    intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                    intent.putExtra("title", getResources().getString(R.string.message_notice_detail));
                    intent.putExtra("url", Urls.URL_NOTICEDETAIL + homeIndexData.getBid() + "&userId=" + userId);
                    startActivity(intent);
                }
                break;
            case R.id.tv_home_notice: // 公告的点击监听
                if (!TextUtils.isEmpty(homeIndexData.getBid())) {
                    intent = new Intent(context, WebActivity.class);
                    intent.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                    intent.putExtra("title", getResources().getString(R.string.message_notice_detail));
                    intent.putExtra("url", Urls.URL_NOTICEDETAIL + homeIndexData.getBid() + "&userId=" + userId);
                    startActivity(intent);
                }
                break;
            case R.id.tv_company_tab1:  // 公司
                setTabTitleStyle(0);
                setLineStyle(0);
                expandViewPager.setCurrentItem(0);
//                investmentGuideFr.upDateInvestmentGuideList();

                break;
            case R.id.tv_team_tab2: // 团队
                setTabTitleStyle(1);
                setLineStyle(1);
                expandViewPager.setCurrentItem(1);
//                roadShowFr.upDateRoadShowList();

                break;
            case R.id.tv_platform_tab3: // 平台
                setTabTitleStyle(2);
                setLineStyle(2);
                expandViewPager.setCurrentItem(2);
//                roadShowFr.upDateRoadShowList();

                break;
        }
    }

    /**
     * 获取首页数据
     */
    private void requestHomeIndexData() {
        HashMap<String, Object> param = new HashMap<>();

        HtmlRequest.getHomeData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                homeIndexData = (HomeIndex2B) params.result;
                if (!TextUtils.isEmpty(homeIndexData.getTitle())) {
                    tv_home_notice.setText(homeIndexData.getTitle());
                } else {
                    tv_home_notice.setText("暂无公告");
                }
//                boutiqueHouseList = homeIndexData.getList();
//                if (myAdapter == null) {
//                    myAdapter = new BoutiqueHouseAdapter(context, boutiqueHouseList);
//                    myListView.setAdapter(myAdapter);
//                } else {
//                    myAdapter.setList(boutiqueHouseList);
//                    myAdapter.notifyDataSetChanged();
//                }
//                if (boutiqueHouseList != null && boutiqueHouseList.size() > 0) {
//                    rl_empty_house.setVisibility(View.GONE);
//                    myListView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.smoothScrollTo(0, 0);
//                        }
//                    }, 50);
//                } else if (boutiqueHouseList != null && boutiqueHouseList.size() <= 0) {
//                    rl_empty_house.setVisibility(View.VISIBLE);
//                } else {
//                    rl_empty_house.setVisibility(View.VISIBLE);
//                }
            }

        });
    }

    /**
     * 请求轮播图数据
     */
    private void requestCycleIndex() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("params", "params");
        HtmlRequest.getHomeCycleIndex(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        picList = (MouldList<ResultCycleIndex2B>) params.result;
                    }
                }
                freshVP();
            }
        });
    }

    /**
     * 请求轮播图数据
     */
    private void freshVP() {
        if (context == null) {
            return;
        }
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
                            intent = new Intent(context, WebForShareActivity.class);
                            intent.putExtra("type", WebForShareActivity.WEBTYPE_ROADSHOW_DETAILS);
                            intent.putExtra("id", target);
                            intent.putExtra("title", "产品路演详情");
                            startActivity(intent);
                        } else if (linkType.equals("appInvestGuide")) { // 投资指南详情
                            intent = new Intent(context, WebForShareActivity.class);
                            intent.putExtra("type", WebForShareActivity.WEBTYPE_INVESTMENT_GUIDE_DETAILS);
                            intent.putExtra("id", target);
                            intent.putExtra("title", "投资指南详情");
                            startActivity(intent);
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

    public void onMyPause() {
        if (rollViewPager != null) {
            rollViewPager.pause();
        }
    }


}
