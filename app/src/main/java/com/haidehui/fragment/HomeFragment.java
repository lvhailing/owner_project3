package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.HotHouseListActivity;
import com.haidehui.act.HouseDetailActivity;
import com.haidehui.act.OverseaProjectListActivity;
import com.haidehui.act.WebActivity;
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
import com.haidehui.widget.MyListView;
import com.haidehui.widget.MyRollViewPager;

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
    private MyListView myListView; // 精品房源推荐列表
    private BoutiqueHouseAdapter myAdapter; // 精品房源 Adapter
    private TextView tv_hot_house; // 最热房源
    private TextView tv_oversea_project; // 海外项目
    private TextView tv_customer_service; // 我的客服
    private LinearLayout ll_home_notice; // 公告布局
    private TextView tv_home_notice; // 公告标题
    private TextView tv_no_house; // 精品房源无数据时显示的提示语
    private Intent intent;
    private MouldList<HomeIndex3B> boutiqueHouseList = new MouldList<>(); // 精品房源列表
    private HomeIndex2B homeIndexData;
    private String userId;
    private MyRollViewPager rollViewPager;
    private RelativeLayout rl_empty_house; // 无精品房源 显示的布局

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

        resetScrollViewSmooth();
    }

    public void resetScrollViewSmooth() {
        requestCycleIndex(); // 请求轮播图数据
        requestHomeIndexData(); // 请求首页数据

    }

    private void initView(View mView) {
        context = getActivity();
        picList = new MouldList<ResultCycleIndex2B>();

        rl_empty_house = (RelativeLayout) mView.findViewById(R.id.rl_empty_house);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) mView.findViewById(R.id.img_empty);
        tv_empty.setText("暂无精品房源");
        rl_empty_house.setVisibility(View.GONE);
        img_empty.setBackgroundResource(R.mipmap.ic_empty_house_resources);


        scrollView = (ScrollView) mView.findViewById(R.id.scrollView);
        ll_vp = (LinearLayout) mView.findViewById(R.id.ll_vp);
        ll_point_container = (LinearLayout) mView.findViewById(R.id.ll_point_container);
        tv_hot_house = (TextView) mView.findViewById(R.id.tv_hot_house);
        tv_oversea_project = (TextView) mView.findViewById(R.id.tv_oversea_project);
        tv_customer_service = (TextView) mView.findViewById(R.id.tv_customer_service);
        tv_home_notice = (TextView) mView.findViewById(R.id.tv_home_notice);
        ll_home_notice = (LinearLayout) mView.findViewById(R.id.ll_home_notice);
//        tv_no_house = (TextView) mView.findViewById(R.id.tv_no_house);
        myListView = (MyListView) mView.findViewById(R.id.lv);

        tv_hot_house.setOnClickListener(this);
        tv_oversea_project.setOnClickListener(this);
        tv_customer_service.setOnClickListener(this);
        tv_home_notice.setOnClickListener(this);
        ll_home_notice.setOnClickListener(this);
    }

    private void initData() {
        userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(context, HouseDetailActivity.class);
                intent.putExtra("hid", boutiqueHouseList.get(position).getHid());
                startActivity(intent);
            }
        });
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
                boutiqueHouseList = homeIndexData.getList();
                if (myAdapter == null) {
                    myAdapter = new BoutiqueHouseAdapter(context, boutiqueHouseList);
                    myListView.setAdapter(myAdapter);
                } else {
                    myAdapter.setList(boutiqueHouseList);
                    myAdapter.notifyDataSetChanged();
                }
                if (boutiqueHouseList != null && boutiqueHouseList.size() > 0) {
                    rl_empty_house.setVisibility(View.GONE);
                    myListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, 0);
                        }
                    }, 50);
                } else if (boutiqueHouseList != null && boutiqueHouseList.size() <= 0) {
                    rl_empty_house.setVisibility(View.VISIBLE);
                } else {
                    rl_empty_house.setVisibility(View.VISIBLE);
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
