package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.HotHouseListActivity;
import com.haidehui.act.HouseDetailActivity;
import com.haidehui.act.OverseaProjectListActivity;
import com.haidehui.act.WebActivity;
import com.haidehui.adapter.BoutiqueHouseAdapter;
import com.haidehui.adapter.CycleAdapter;
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
import com.haidehui.widget.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 底部导航---首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ScrollView scrollView;
    private LinearLayout mViewPager; //顶部轮播图
    private LinearLayout ll_down_dots; // 轮播图下面的圆点
    private DisplayImageOptions options;
    private CycleAdapter cycleAdapter;//自定义viewPager
    private MyListView myListView; // 精品房源推荐列表
    private BoutiqueHouseAdapter myAdapter;
    private TextView tv_hot_house, tv_oversea_project, tv_customer_service; // 最热房源，海外项目，我的客服
    private Context context;
    private MouldList<ResultCycleIndex2B> homeCycleBean;
    private LinearLayout ll_home_notice; // 公告布局
    private TextView tv_home_notice; // 公告标题
    private TextView tv_no_house; // 精品房源无数据时显示的提示语
    private Intent intent;
    private MouldList<HomeIndex3B> BoutiqueHouseList = new MouldList<>();
    private HomeIndex2B homeIndexData;
    private String userId;

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
//        requestBoutiqueHouseData();
//        scrollView.smoothScrollTo(0, 0);
    }


    private void initView(View mView) {
        context = getActivity();
        homeCycleBean = new MouldList<ResultCycleIndex2B>();

        scrollView = (ScrollView) mView.findViewById(R.id.scrollView);
        mViewPager = (LinearLayout) mView.findViewById(R.id.viewpager);
        ll_down_dots = (LinearLayout) mView.findViewById(R.id.ll_down_dots);
        tv_hot_house = (TextView) mView.findViewById(R.id.tv_hot_house);
        tv_oversea_project = (TextView) mView.findViewById(R.id.tv_oversea_project);
        tv_customer_service = (TextView) mView.findViewById(R.id.tv_customer_service);
        tv_home_notice = (TextView) mView.findViewById(R.id.tv_home_notice);
        ll_home_notice = (LinearLayout) mView.findViewById(R.id.ll_home_notice);
        tv_no_house = (TextView) mView.findViewById(R.id.tv_no_house);
        myListView = (MyListView) mView.findViewById(R.id.lv);

        tv_hot_house.setOnClickListener(this);
        tv_oversea_project.setOnClickListener(this);
        tv_customer_service.setOnClickListener(this);
        tv_home_notice.setOnClickListener(this);
        ll_home_notice.setOnClickListener(this);
    }

    private void initData() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.bg_home_carousel_figure_normal).showImageOnFail(R.mipmap.bg_home_carousel_figure_normal).resetViewBeforeLoading(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestCycleIndex(); // 请求轮图数据

        requestHomeIndexData(); // 请求首页数据

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(context, HouseDetailActivity.class);
                intent.putExtra("hid", BoutiqueHouseList.get(position).getHid());
                startActivity(intent);
            }
        });
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
                intent = new Intent(context, WebActivity.class);
                intent.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                intent.putExtra("title", getResources().getString(R.string.message_notice_detail));
                intent.putExtra("url", Urls.URL_NOTICEDETAIL + homeIndexData.getBid() + "&userId=" + userId);
                startActivity(intent);

                break;
            case R.id.tv_home_notice: // 公告的点击监听
                intent = new Intent(context, WebActivity.class);
                intent.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                intent.putExtra("title", getResources().getString(R.string.message_notice_detail));
                intent.putExtra("url", Urls.URL_NOTICEDETAIL + homeIndexData.getBid() + "&userId=" + userId);
                startActivity(intent);

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
                tv_home_notice.setText(homeIndexData.getTitle());
                BoutiqueHouseList = homeIndexData.getList();
                if (BoutiqueHouseList != null && BoutiqueHouseList.size() > 0) {
                    myAdapter = new BoutiqueHouseAdapter(context, BoutiqueHouseList);
                    myListView.setAdapter(myAdapter);
                    scrollView.smoothScrollTo(0, 0);
                } else {
                    tv_no_house.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    /**
     * 请求轮播图数据
     */
    private void requestCycleIndex() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("params", "params");
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

}
