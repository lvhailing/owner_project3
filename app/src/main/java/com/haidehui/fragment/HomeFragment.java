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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.act.HotHouseActivity;
import com.haidehui.act.HouseDetailActivity;
import com.haidehui.act.OverseaProjectActivity;
import com.haidehui.act.OverseaProjectDetailActivity;
import com.haidehui.adapter.BoutiqueHouseAdapter;
import com.haidehui.adapter.CycleAdapter;
import com.haidehui.model.BoutiqueHouse2B;
import com.haidehui.model.ResultCycleIndex2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.photo_preview.PhotoPreviewAc;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.MyListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 底部导航---产品
 */
public class HomeFragment extends Fragment implements View.OnClickListener, CycleAdapter.ImageCycleViewListener {
    private View mView;
    private LinearLayout mViewPager; //顶部轮播图
    private LinearLayout ll_down_dots; // 轮播图下面的圆点
    private DisplayImageOptions options;
    private CycleAdapter cycleAdapter;//自定义viewPager
    private MyListView myListView; // 精品房源推荐
    private BoutiqueHouseAdapter myAdapter;
    private TextView tv_hot_house, tv_oversea_project, tv_customer_service; //固收、浮收、保险
    private ScrollView scrollView;
    private Context context;
    //    private ResultProductIndexBean productIndexBean;
    private MouldList<ResultCycleIndex2B> homeCycleBean;
    private MouldList<BoutiqueHouse2B> list; // 精品房源数据
    private TextView tv_home_notice;
    private LinearLayout ll_home_notice;
    private Intent intent;

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
        scrollView.smoothScrollTo(0, 0);
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
        list = new MouldList<BoutiqueHouse2B>();

        mViewPager = (LinearLayout) mView.findViewById(R.id.viewpager);
        ll_down_dots = (LinearLayout) mView.findViewById(R.id.ll_down_dots);
        myListView = (MyListView) mView.findViewById(R.id.lv_boutique_house);
        scrollView = (ScrollView) mView.findViewById(R.id.scrollview);
        tv_hot_house = (TextView) mView.findViewById(R.id.tv_hot_house);
        tv_oversea_project = (TextView) mView.findViewById(R.id.tv_oversea_project);
        tv_customer_service = (TextView) mView.findViewById(R.id.tv_customer_service);
        tv_home_notice = (TextView) mView.findViewById(R.id.tv_home_notice);
        ll_home_notice = (LinearLayout) mView.findViewById(R.id.ll_home_notice);

        tv_hot_house.setOnClickListener(this);
        tv_oversea_project.setOnClickListener(this);
        tv_customer_service.setOnClickListener(this);
        tv_home_notice.setOnClickListener(this);
        ll_home_notice.setOnClickListener(this);
    }

    private void initData() {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.banner_one).showImageOnFail(R.drawable.banner_one).resetViewBeforeLoading(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();

        requestCycleIndex();
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(Context context, ListView listView, int dividerHeight) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight() + dividerHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount()) + 5;

        listView.setLayoutParams(params);
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

    /**
     * 请求精品房源数据
     */
    private void initHotProductData() {
//        if (!TextUtils.isEmpty(productIndexBean.getBulletin().getTopic())) {
//            tv_fragment_product_notice.setText(productIndexBean.getBulletin().getTopic());
//        }

        myAdapter = new BoutiqueHouseAdapter(context, list);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });
        setListViewHeightBasedOnChildren(getActivity(), myListView, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hot_house:  // 最热房源
                intent = new Intent(context, HouseDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_oversea_project: // 海外项目
                intent = new Intent(context, OverseaProjectDetailActivity.class);
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

    // 请求精品房源数据
    private void requestBoutiqueHouseData() {
//        HtmlRequest.getBoutiqueHouseData(context, new BaseRequester.OnRequestListener() {
//            @Override
//            public void onRequestFinished(BaseParams params) {
//                if (params != null) {
//                    productIndexBean = (ResultProductIndexBean) params.result;
//                    if (productIndexBean != null) {
//                        if (list != null) {
//                            list.clear();
//                        }
//                        list.addAll(productIndexBean.getList());
//                        initHotProductData();
//                    }
//                }
//            }
//        });
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
