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
import com.haidehui.adapter.MyAdapter;
import com.haidehui.fragment.EssentialInfoFragment;
import com.haidehui.fragment.PurchaseCostFragment;
import com.haidehui.fragment.PurchaseFlowFragment;
import com.haidehui.photo_preview.PhotoPreviewAc;
import com.haidehui.widget.TitleBar;

import java.util.ArrayList;

/**
 * 海外项目详情
 */
public class OverseaProjectDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private int currentPos;
    private TextView tv_vp_page;
    private ImageView image;
    private ArrayList<ImageView> imageList;
    private ArrayList<String> list;
    private TextView tv_house_name;
    private MyAdapter mAdapter;
    private RelativeLayout rl_house_detail_addr; // 地址布局
    private Button btn_essential_info, btn_purchase_cost, btn_purchase_flow; // 基本信息、购房费用、购房流程
    //    private FragmentTransaction transaction;
    private EssentialInfoFragment essentialInfoFragment; // 购房基本信息
    private PurchaseCostFragment purchaseCostFragment; // 购房费用
    private PurchaseFlowFragment purchaseFlowFragment; // 购房流程
    private ImageView iv_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_oversea_project_detail);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_house_detail))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        id = getIntent().getStringExtra("id");
        vp = (ViewPager) findViewById(R.id.vp);
        tv_house_name = (TextView) findViewById(R.id.tv_house_name);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);
        rl_house_detail_addr = (RelativeLayout) findViewById(R.id.rl_house_detail_addr);
        btn_essential_info = (Button) findViewById(R.id.btn_essential_info);
        btn_purchase_cost = (Button) findViewById(R.id.btn_purchase_cost);
        btn_purchase_flow = (Button) findViewById(R.id.btn_purchase_flow);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);

        btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
        essentialInfoFragment = new EssentialInfoFragment();
        transaction.replace(R.id.fragment_container, essentialInfoFragment);
        transaction.commit();

        rl_house_detail_addr.setOnClickListener(this);
        btn_essential_info.setOnClickListener(this);
        btn_purchase_cost.setOnClickListener(this);
        btn_purchase_flow.setOnClickListener(this);
        iv_phone.setOnClickListener(this);

        list = new ArrayList<>();
        list.add("http://pic17.nipic.com/20111022/6322714_173008780359_2.jpg");
        list.add("http://www.mincoder.com/assets/images/avatar.jpg");
        list.add("http://pic.58pic.com/58pic/12/74/05/99C58PICYck.jpg");
        list.add("http://f12.baidu.com/it/u=89957531,1663631515&fm=76");
        list.add("http://f10.baidu.com/it/u=1304563494,724196614&fm=76");

        mAdapter = new MyAdapter(mContext, list);
        vp.setAdapter(mAdapter);
        mAdapter.setOnImageListener(new MyAdapter.ImageViewListener() {
            @Override
            public void onImageClick(int postion) {
                Intent intent = new Intent(mContext, PhotoPreviewAc.class);
                intent.putStringArrayListExtra("urls", list);
                intent.putExtra("currentPos", postion);
                startActivity(intent);
            }
        });
//        vp.setOffscreenPageLimit(5);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(currentPos);

        updateNum();
    }

    private void initData() {
        requestDetailData();
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            currentPos = position;
            updateNum();
        }
    }

    private void updateNum() {
        tv_vp_page.setText(currentPos + 1 + "/" + list.size());
    }


    private void setView() {
        //加载图片
//        ImageLoader.getInstance().displayImage(detail.getGolfPhoto(), iv_detail_photo);

    }

    @Override
    public void onClick(View v) {
        resetBtnText();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.rl_house_detail_addr:   // 地址点击监听
//                Intent intent = new Intent(mContext, SubBookingGolfActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);
                break;
            case R.id.btn_essential_info:  // 基本信息
                btn_essential_info.setTextColor(Color.parseColor("#ddb57f"));
                hideFragment(transaction);
                essentialInfoFragment = new EssentialInfoFragment();
                transaction.replace(R.id.fragment_container, essentialInfoFragment);
                transaction.commit();

                break;
            case R.id.btn_purchase_cost:  // 购房费用
                btn_purchase_cost.setTextColor(Color.parseColor("#ddb57f"));
                hideFragment(transaction);
                purchaseCostFragment = new PurchaseCostFragment();
                transaction.replace(R.id.fragment_container, purchaseCostFragment);
                transaction.commit();
                break;
            case R.id.btn_purchase_flow:  // 购房流程
                btn_purchase_flow.setTextColor(Color.parseColor("#ddb57f"));
                hideFragment(transaction);
                purchaseFlowFragment = new PurchaseFlowFragment();
                transaction.replace(R.id.fragment_container, purchaseFlowFragment);
                transaction.commit();
                break;
            case R.id.iv_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + getString(R.string.adviser_phone_num));
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    private void resetBtnText() {
        btn_essential_info.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_cost.setTextColor(Color.parseColor("#b3b3b3"));
        btn_purchase_flow.setTextColor(Color.parseColor("#b3b3b3"));
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
//        HtmlRequest.getGolfDetail(this, id, new BaseRequester.OnRequestListener() {
//            @Override
//            public void onRequestFinished(BaseParams params) {
//                if (params != null) {
//                    golf2B = (GolfDetail2B) params.result;
//                    detail = golf2B.getGolf();
//                    if (detail != null) {
//                        setView();
//                    }
//                }
//            }
//        });
    }
}
