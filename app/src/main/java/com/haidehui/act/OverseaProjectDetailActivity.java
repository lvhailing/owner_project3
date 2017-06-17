package com.haidehui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.HouseDetailAdapter;
import com.haidehui.model.OverseaProjectDetail2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 海外项目详情
 */
public class OverseaProjectDetailActivity extends BaseActivity implements View.OnClickListener {
    private boolean isShow = false; //刚进来此页面时，项目居室、配套设施、地理位置等下面的内容默认是不显示的
    private ImageView iv_oversea_detail; // 顶部图片
    private TextView tv_pro_house_name;
    private TextView tv_pro_house_price;
    private TextView tv_pro_house_area;
    private TextView tv_pro_position;
    private TextView tv_pro_type;
    private TextView tv_pro_count;
    private TextView tv_pro_decoration_standard;
    private TextView tv_pro_house_desc;


    private ViewPager vp;
    private int currentPos;
    private TextView tv_vp_page;
    private ImageView image;
    private ArrayList<ImageView> imageList;
    private ArrayList<String> list;
    private HouseDetailAdapter mAdapter;
    private ImageView iv_project_click, iv_support_facilities_click, iv_geographic_location_click;
    private LinearLayout ll_pro_house_photos, ll_support_facilities, ll_geographic_location; // 项目居室，配套设施，地理位置等布局
    private TextView tv_project_des, tv_support_facilities_desc, tv_geographic_location_desc; // 项目居室，配套设施，地理位置等的描述
    private String pid;
    private OverseaProjectDetail2B OverseaProjectDetail;
    private ArrayList<String> houseTypeImgList;


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
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_oversea_project_detail)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

        pid = getIntent().getStringExtra("pid");
        iv_oversea_detail = (ImageView) findViewById(R.id.iv_oversea_detail);
        iv_project_click = (ImageView) findViewById(R.id.iv_project_click);
        iv_support_facilities_click = (ImageView) findViewById(R.id.iv_support_facilities_click);
        iv_geographic_location_click = (ImageView) findViewById(R.id.iv_geographic_location_click);

        tv_pro_house_name = (TextView) findViewById(R.id.tv_pro_house_name);
        tv_pro_house_price = (TextView) findViewById(R.id.tv_pro_house_price);
        tv_pro_house_area = (TextView) findViewById(R.id.tv_pro_house_area);
        tv_pro_position = (TextView) findViewById(R.id.tv_pro_position);
        tv_pro_type = (TextView) findViewById(R.id.tv_pro_type);
        tv_pro_count = (TextView) findViewById(R.id.tv_pro_count);
        tv_pro_decoration_standard = (TextView) findViewById(R.id.tv_pro_decoration_standard);
        tv_pro_house_desc = (TextView) findViewById(R.id.tv_pro_house_desc);
        tv_project_des = (TextView) findViewById(R.id.tv_project_des);
        tv_support_facilities_desc = (TextView) findViewById(R.id.tv_support_facilities_desc);
        tv_geographic_location_desc = (TextView) findViewById(R.id.tv_geographic_location_desc);

        ll_pro_house_photos = (LinearLayout) findViewById(R.id.ll_pro_house_photos);
        ll_support_facilities = (LinearLayout) findViewById(R.id.ll_support_facilities);
        ll_geographic_location = (LinearLayout) findViewById(R.id.ll_geographic_location);


        vp = (ViewPager) findViewById(R.id.vp);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);

        iv_project_click.setOnClickListener(this);
        iv_support_facilities_click.setOnClickListener(this);
        iv_geographic_location_click.setOnClickListener(this);

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
        tv_vp_page.setText(currentPos + 1 + "/" + houseTypeImgList.size());
    }


    private void setView() {
        //加载图片
        ImageLoader.getInstance().displayImage(OverseaProjectDetail.getProjectImg(), iv_oversea_detail);

        tv_pro_house_name.setText(OverseaProjectDetail.getName());
        tv_pro_house_price.setText(OverseaProjectDetail.getPrice());
        tv_pro_house_area.setText(OverseaProjectDetail.getArea());
        tv_pro_position.setText(OverseaProjectDetail.getLocation());
        tv_pro_type.setText(OverseaProjectDetail.getCategory());
        tv_pro_count.setText(OverseaProjectDetail.getTotal());
        tv_pro_decoration_standard.setText(OverseaProjectDetail.getDecorateStandard());
        tv_pro_house_desc.setText(OverseaProjectDetail.getProjectDesc());
        tv_project_des.setText(OverseaProjectDetail.getHouseType());
//        tv_support_facilities_desc.setText(OverseaProjectDetail.getSupportFacility());
//        tv_geographic_location_desc.setText(OverseaProjectDetail.getGeographyLocation());

        houseTypeImgList = OverseaProjectDetail.getHouseTypeImg();
        mAdapter = new HouseDetailAdapter(mContext, houseTypeImgList);
        vp.setAdapter(mAdapter);
//        vp.setOffscreenPageLimit(5);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(currentPos);

        updateNum();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_project_click:   // 项目居室
                if (!isShow) {
                    ll_pro_house_photos.setVisibility(View.VISIBLE);
                    tv_project_des.setText("");
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_pro_house_photos.setVisibility(View.GONE);
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.iv_support_facilities_click:  // 配套设施
                if (!isShow) {
                    ll_support_facilities.setVisibility(View.VISIBLE);
                    tv_support_facilities_desc.setText(OverseaProjectDetail.getSupportFacility());
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_support_facilities.setVisibility(View.GONE);
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.iv_geographic_location_click:  // 地理位置
                if (!isShow) {
                    ll_geographic_location.setVisibility(View.VISIBLE);
                    tv_geographic_location_desc.setText(OverseaProjectDetail.getGeographyLocation());
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_geographic_location.setVisibility(View.GONE);
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.btn_purchase_flow:  // 购房流程
                break;
            case R.id.iv_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + getString(R.string.adviser_phone_num));
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }


    private void requestDetailData() {  // 获取海外项目详情页的数据
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("pid", pid);

        HtmlRequest.getOverseaDetailData(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    OverseaProjectDetail = (OverseaProjectDetail2B) params.result;
                    if (OverseaProjectDetail != null) {
                        setView();
                    }
                }
            }
        });
    }
}
