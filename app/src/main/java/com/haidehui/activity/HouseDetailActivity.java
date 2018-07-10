package com.haidehui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.HouseDetailAdapter;
import com.haidehui.fragment.EssentialInfoFragment;
import com.haidehui.fragment.PurchaseCostFragment;
import com.haidehui.fragment.PurchaseFlowFragment;
import com.haidehui.model.HouseDetail2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.photo_preview.PhotoPreviewAc;
import com.haidehui.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 房源详情
 */
public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private TextView tv_vp_page;
    private ImageView image;
    private ArrayList<ImageView> imageList;
    private TextView tv_house_name;
    private HouseDetailAdapter mAdapter;
    private RelativeLayout rl_house_detail_addr; // 地址布局
    private Button btn_essential_info, btn_purchase_cost, btn_purchase_flow; // 基本信息、购房费用、购房流程
    private EssentialInfoFragment essentialInfoFragment; // 购房基本信息
    private PurchaseCostFragment purchaseCostFragment; // 购房费用
    private PurchaseFlowFragment purchaseFlowFragment; // 购房流程
    private RelativeLayout rl_phone;
    private String hid; // 房源编号
    private HouseDetail2B houseDetail;
    private ArrayList<String> houseImgList; // 房源图片列表
    private TextView tv_house_detail_price ; // 价格
    private TextView tv_house_detail_area ; //  面积
    private TextView tv_house_detail_house_type; //  居室类型
//    private TextView tv_house_detail_commission_rate; // 佣金比例
    private TextView tv_house_detail_address; // 地址
    private Intent intent;
    private int currentPage;
    private int flag = 1;
    private TitleBar titleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_house_detail);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.showLeftImg(true);
        titleBar.setFromActivity("1001");
        titleBar.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back)
                .setCenterText(getResources().getString(R.string.title_house_detail)).showMore(false).setTitleRightButton(R.drawable.ic_share_title).setOnActionListener(new TitleBar.OnActionListener() {

            @Override
            public void onMenu(int id) {
            }


            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }

    private void initView() {
        hid = getIntent().getStringExtra("hid");

        vp = (ViewPager) findViewById(R.id.vp_living_room);
        tv_house_name = (TextView) findViewById(R.id.tv_house_name);
        tv_vp_page = (TextView) findViewById(R.id.tv_living_room_page);
        tv_house_detail_price = (TextView) findViewById(R.id.tv_house_detail_price);
        tv_house_detail_area = (TextView) findViewById(R.id.tv_house_detail_area);
        tv_house_detail_house_type = (TextView) findViewById(R.id.tv_house_detail_house_type);
//        tv_house_detail_commission_rate = (TextView) findViewById(R.id.tv_house_detail_commission_rate);
        tv_house_detail_address = (TextView) findViewById(R.id.tv_house_detail_address);

        rl_house_detail_addr = (RelativeLayout) findViewById(R.id.rl_house_detail_addr);
        btn_essential_info = (Button) findViewById(R.id.btn_essential_info);
        btn_purchase_cost = (Button) findViewById(R.id.btn_purchase_cost);
        btn_purchase_flow = (Button) findViewById(R.id.btn_purchase_flow);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);

        resetBtnTextAnaBg();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        essentialInfoFragment = new EssentialInfoFragment();
        transaction.replace(R.id.fragment_container, essentialInfoFragment);
        transaction.commit();

        rl_house_detail_addr.setOnClickListener(this);
        btn_essential_info.setOnClickListener(this);
        btn_purchase_cost.setOnClickListener(this);
        btn_purchase_flow.setOnClickListener(this);
        rl_phone.setOnClickListener(this);

    }

    private void initData() {
        requestDetailData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag == 1) {
            btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
//            btn_essential_info.setBackgroundResource(R.drawable.shape_center_orange_white);
        } else if (flag == 2) {
            btn_purchase_cost.setTextColor(Color.parseColor("#ddb57f"));
//            btn_purchase_cost.setBackgroundResource(R.drawable.shape_center_orange_white);
        } else {
            btn_purchase_flow.setTextColor(Color.parseColor("#ddb57f"));
//            btn_purchase_flow.setBackgroundResource(R.drawable.shape_center_orange_white);
        }

    }

    @Override
    public void onClick(View v) {
        resetBtnTextAnaBg();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.rl_house_detail_addr:   // 地址点击监听
                intent = new Intent(mContext, PhotoPreviewAc.class);
                ArrayList<String> locationImgs = new ArrayList<>();
                locationImgs.add(houseDetail.getLocationImg());
                intent.putStringArrayListExtra("urls", locationImgs);
                startActivity(intent);
                break;
            case R.id.btn_essential_info:  // 基本信息
                flag = 1;
                requestDetailData();
                btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
//                btn_essential_info.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                essentialInfoFragment = new EssentialInfoFragment();
                transaction.replace(R.id.fragment_container, essentialInfoFragment);
                transaction.commit();

                break;
            case R.id.btn_purchase_cost:  // 购房费用
                flag = 2;
                btn_purchase_cost.setTextColor(Color.parseColor("#ddb57f"));
//                btn_purchase_cost.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                purchaseCostFragment = new PurchaseCostFragment();
                transaction.replace(R.id.fragment_container, purchaseCostFragment);
                transaction.commit();
                break;
            case R.id.btn_purchase_flow:  // 购房流程
                flag = 3;
                btn_purchase_flow.setTextColor(Color.parseColor("#ddb57f"));
//                btn_purchase_flow.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                purchaseFlowFragment = new PurchaseFlowFragment();
                transaction.replace(R.id.fragment_container, purchaseFlowFragment);
                transaction.commit();
                break;
            case R.id.rl_phone:
                intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + getString(R.string.phone_number));
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    /**
     * 设置“基本信息”、“购房费用”、“购房流程”等按钮的文字及背景的默认颜色
     */
    private void resetBtnTextAnaBg() {
        btn_essential_info.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_cost.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_flow.setTextColor(Color.parseColor("#b3b3b3"));

        btn_essential_info.setBackgroundResource(R.drawable.shape_center_light_gray_white);
        btn_purchase_cost.setBackgroundResource(R.drawable.shape_center_light_gray_white);
        btn_purchase_flow.setBackgroundResource(R.drawable.shape_center_light_gray_white);
    }

    // 去除所有的Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (essentialInfoFragment != null) {
            transaction.remove(essentialInfoFragment);
        }
        if (purchaseCostFragment != null) {
            transaction.remove(purchaseCostFragment);
        }
        if (purchaseFlowFragment != null) {
            transaction.remove(purchaseFlowFragment);
        }
    }

    /**
     * 获取最热房源详情页的数据
     */
    private void requestDetailData() {
       /* String userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        HashMap<String, Object> param = new HashMap<>();
        param.put("hid", hid);
        param.put("userId", userId);

        HtmlRequest.getHouseDetailData(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    houseDetail = (HouseDetail2B) params.result;
                    if (houseDetail != null) {
                        setView();

                        //设置分享参数
                        titleBar.setActivityParameters(hid, houseDetail.getName(),houseDetail.getHouseDesc());
                    }
                }
            }
        });
    }

    private void setView() {
        houseImgList = houseDetail.getHouseImg();
        if (houseImgList != null && houseImgList.size() > 0) {
            mAdapter = new HouseDetailAdapter(mContext, houseImgList);
            mAdapter.setOnImageListener(new HouseDetailAdapter.ImageViewListener() {
                @Override
                public void onImageClick(int position) { // 顶部轮播图点击监听
                    Intent intent = new Intent(mContext, PhotoPreviewAc.class);
                    intent.putStringArrayListExtra("urls", houseImgList);
                    intent.putExtra("currentPos", currentPage);
                    startActivity(intent);
                }
            });

            vp.setAdapter(mAdapter);
            vp.setOnPageChangeListener(new MyOnPageChangeListener());

            updateNum(0);
        }

        if (!TextUtils.isEmpty(houseDetail.getName())) {
            tv_house_name.setText(houseDetail.getName());
        }
        if (!TextUtils.isEmpty(houseDetail.getPrice())) {
            tv_house_detail_price.setText(houseDetail.getPrice() + "万元");
        }
        if (!TextUtils.isEmpty(houseDetail.getArea())) {
            tv_house_detail_area.setText(houseDetail.getArea());
        }
        if (!TextUtils.isEmpty(houseDetail.getHouseType())) {
            tv_house_detail_house_type.setText(houseDetail.getHouseType());
        }
//        if (!TextUtils.isEmpty(houseDetail.getCommissionRate())) {
//            tv_house_detail_commission_rate.setText(houseDetail.getCommissionRate());
//        }
        if (!TextUtils.isEmpty(houseDetail.getLocation())) {
            tv_house_detail_address.setText(houseDetail.getLocation());
        }

        essentialInfoFragment.refreshLayoutInfo(houseDetail);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            currentPage = position;
            updateNum(currentPage);
        }
    }

    private void updateNum(int currentPos) {
        tv_vp_page.setText(currentPos + 1 + "/" + houseImgList.size());
    }


}
