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
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.HotHouseListActivity;
import com.haidehui.act.OverseaProjectListActivity;
import com.haidehui.adapter.BoutiqueHouseAdapter;
import com.haidehui.adapter.CycleAdapter;
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

import java.util.LinkedHashMap;

/**
 * 底部导航---首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private View mView;
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
    private Intent intent;
    private MouldList<HomeIndex3B> BoutiqueHouseList = new MouldList<>();

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
    }


    private void initView(View mView) {
        context = getActivity();
        homeCycleBean = new MouldList<ResultCycleIndex2B>();

        mViewPager = (LinearLayout) mView.findViewById(R.id.viewpager);
        ll_down_dots = (LinearLayout) mView.findViewById(R.id.ll_down_dots);
        tv_hot_house = (TextView) mView.findViewById(R.id.tv_hot_house);
        tv_oversea_project = (TextView) mView.findViewById(R.id.tv_oversea_project);
        tv_customer_service = (TextView) mView.findViewById(R.id.tv_customer_service);
        tv_home_notice = (TextView) mView.findViewById(R.id.tv_home_notice);
        ll_home_notice = (LinearLayout) mView.findViewById(R.id.ll_home_notice);
        myListView = (MyListView) mView.findViewById(R.id.lv);

        tv_hot_house.setOnClickListener(this);
        tv_oversea_project.setOnClickListener(this);
        tv_customer_service.setOnClickListener(this);
        tv_home_notice.setOnClickListener(this);
        ll_home_notice.setOnClickListener(this);
    }

    private void initData() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.bg_home_carousel_figure_normal)
                .showImageOnFail(R.mipmap.bg_home_carousel_figure_normal).resetViewBeforeLoading(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

        requestCycleIndex();

        requestHomeIndexData();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Intent intent = new Intent(LinerListActivity.this, LinerDetailActivity.class);
//                intent.putExtra("id", totalList.get(position - 1).getId());
//                startActivity(intent);
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
                String userId = null;
                try {
                    userId = DESUtil.decrypt(PreferenceUtil.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if (productIndexBean != null) {
                    Intent i_web = new Intent(context, WebActivity.class);
                    i_web.putExtra("type", WebActivity.WEBTYPE_NOTICE_DETAILS);
                    i_web.putExtra("id", productIndexBean.getBulletin().getId());
                    i_web.putExtra("title", "详情");
                    i_web.putExtra("uid", userId);
                    startActivity(i_web);
                }*/
                break;
            case R.id.tv_home_notice:
               /* if (productIndexBean != null) {
                    Intent i_web = new Intent(context, WebActivity.class);
                    i_web.putExtra("type", WebActivity.WEBTYPE_NOTICE_DETAILS);
                    i_web.putExtra("id", productIndexBean.getBulletin().getId());
                    i_web.putExtra("title", "详情");
                    startActivity(i_web);
                }*/

                break;
        }
    }

    /**
     *  获取首页数据
     */
    private void requestHomeIndexData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        HtmlRequest.getHomeData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                HomeIndex2B data = (HomeIndex2B) params.result;
                tv_home_notice.setText(data.getTitle());
                BoutiqueHouseList = data.getList();
                if (BoutiqueHouseList != null && BoutiqueHouseList.size() > 0) {
                    myAdapter = new BoutiqueHouseAdapter(context, BoutiqueHouseList);
                    myListView.setAdapter(myAdapter);
                }
            }

        });
    }

    /**
     *  请求轮播图数据
     */
    private void requestCycleIndex() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
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
