package com.haidehui.act;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import java.util.LinkedHashMap;

/**
 * 房源详情
 */
public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private TextView tv_vp_page;
    private ImageView image;
    private ArrayList<ImageView> imageList;
    private ArrayList<String> list;
    private TextView tv_house_name;
    private HouseDetailAdapter mAdapter;
    private RelativeLayout rl_house_detail_addr; // 地址布局
    private Button btn_essential_info, btn_purchase_cost, btn_purchase_flow; // 基本信息、购房费用、购房流程
    //    private FragmentTransaction transaction;
    private EssentialInfoFragment essentialInfoFragment; // 购房基本信息
    private PurchaseCostFragment purchaseCostFragment; // 购房费用
    private PurchaseFlowFragment purchaseFlowFragment; // 购房流程
    private ImageView iv_phone;
    private String hid; // 房源编号
    private HouseDetail2B houseDetail;
    private ArrayList<String> houseImgList; // 房源图片列表
    private TextView tv_house_detail_price, tv_house_detail_area, tv_house_detail_house_type, tv_house_detail_commission_rate; // 价格，面积，居室类型，佣金比例
    private TextView tv_house_detail_address; // 地址
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_house_detail);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_house_detail)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        vp = (ViewPager) findViewById(R.id.vp);
        tv_house_name = (TextView) findViewById(R.id.tv_house_name);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);
        tv_house_detail_price = (TextView) findViewById(R.id.tv_house_detail_price);
        tv_house_detail_area = (TextView) findViewById(R.id.tv_house_detail_area);
        tv_house_detail_house_type = (TextView) findViewById(R.id.tv_house_detail_house_type);
        tv_house_detail_commission_rate = (TextView) findViewById(R.id.tv_house_detail_commission_rate);
        tv_house_detail_address = (TextView) findViewById(R.id.tv_house_detail_address);

        rl_house_detail_addr = (RelativeLayout) findViewById(R.id.rl_house_detail_addr);
        btn_essential_info = (Button) findViewById(R.id.btn_essential_info);
        btn_purchase_cost = (Button) findViewById(R.id.btn_purchase_cost);
        btn_purchase_flow = (Button) findViewById(R.id.btn_purchase_flow);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);

        btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
        btn_essential_info.setBackgroundResource(R.drawable.shape_center_orange_white);
        essentialInfoFragment = new EssentialInfoFragment();
        transaction.replace(R.id.fragment_container, essentialInfoFragment);
        transaction.commit();

        rl_house_detail_addr.setOnClickListener(this);
        btn_essential_info.setOnClickListener(this);
        btn_purchase_cost.setOnClickListener(this);
        btn_purchase_flow.setOnClickListener(this);
        iv_phone.setOnClickListener(this);

//        list = new ArrayList<>();
//        list.add("http://pic17.nipic.com/20111022/6322714_173008780359_2.jpg");
//        list.add("http://www.mincoder.com/assets/images/avatar.jpg");
//        list.add("http://pic.58pic.com/58pic/12/74/05/99C58PICYck.jpg");
//        list.add("http://f12.baidu.com/it/u=89957531,1663631515&fm=76");
//        list.add("http://f10.baidu.com/it/u=1304563494,724196614&fm=76");


    }

    private void initData() {
        requestDetailData();
    }

    private void setView() {
        houseImgList = houseDetail.getHouseImg();
        if (houseImgList != null && houseImgList.size() > 0) {
            mAdapter = new HouseDetailAdapter(mContext, houseImgList);
            vp.setAdapter(mAdapter);

            vp.setOnPageChangeListener(new MyOnPageChangeListener());

            updateNum(0);
        }

        tv_house_name.setText(houseDetail.getName());
        tv_house_detail_price.setText(houseDetail.getPrice() + "万元");
        tv_house_detail_area.setText(houseDetail.getArea() + "㎡");
        tv_house_detail_house_type.setText(houseDetail.getHouseType());
        tv_house_detail_commission_rate.setText(houseDetail.getCommissionRate());
        tv_house_detail_address.setText(houseDetail.getLocation());

        essentialInfoFragment.refreshLayoutInfo(houseDetail);

        //加载图片
//        ImageLoader.getInstance().displayImage(detail.getGolfPhoto(), iv_detail_photo);

    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            updateNum(position);

            Intent intent = new Intent(mContext, PhotoPreviewAc.class);
            intent.putStringArrayListExtra("urls", houseImgList);
            intent.putExtra("currentPos", position);
            startActivity(intent);
        }
    }

    private void updateNum(int currentPos) {
        tv_vp_page.setText(currentPos + 1 + "/" + houseImgList.size());
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
                btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
                btn_essential_info.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                essentialInfoFragment = new EssentialInfoFragment();
                transaction.replace(R.id.fragment_container, essentialInfoFragment);
                transaction.commit();

                break;
            case R.id.btn_purchase_cost:  // 购房费用
                btn_purchase_cost.setTextColor(Color.parseColor("#ddb57f"));
                btn_purchase_cost.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                purchaseCostFragment = new PurchaseCostFragment();
                transaction.replace(R.id.fragment_container, purchaseCostFragment);
                transaction.commit();
                break;
            case R.id.btn_purchase_flow:  // 购房流程
                btn_purchase_flow.setTextColor(Color.parseColor("#ddb57f"));
                btn_purchase_flow.setBackgroundResource(R.drawable.shape_center_orange_white);
                hideFragment(transaction);
                purchaseFlowFragment = new PurchaseFlowFragment();
                transaction.replace(R.id.fragment_container, purchaseFlowFragment);
                transaction.commit();
                break;
            case R.id.iv_phone:
                intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + getString(R.string.adviser_phone_num));
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    private void resetBtnTextAnaBg() {
        btn_essential_info.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_cost.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_flow.setTextColor(Color.parseColor("#b3b3b3"));

        btn_essential_info.setBackgroundResource(R.drawable.shape_center_gray_white);
        btn_purchase_cost.setBackgroundResource(R.drawable.shape_center_gray_white);
        btn_purchase_flow.setBackgroundResource(R.drawable.shape_center_gray_white);
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

    private void requestDetailData() {  // 获取最热房源详情页的数据
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("hid", hid);
        param.put("userId", "17021511395798036131");

        HtmlRequest.getHouseDetailData(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    houseDetail = (HouseDetail2B) params.result;
                    if (houseDetail != null) {
                        setView();
                    }
                }
            }
        });
    }

}
