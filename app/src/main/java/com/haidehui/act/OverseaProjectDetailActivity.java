package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.BoutiqueHouseAdapter;
import com.haidehui.adapter.HouseDetailAdapter;
import com.haidehui.adapter.RelatedHouseAdapter;
import com.haidehui.model.OverseaProjectDetail2B;
import com.haidehui.model.OverseaProjectDetail3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 海外项目详情
 */
public class OverseaProjectDetailActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView scrollView;

    private boolean isShowHouse = false; //刚进来此页面时，项目居室内容默认是不显示的
    private boolean isShowFacilities = false; //刚进来此页面时，配套设施内容默认是不显示的
    private boolean isShowLocation = false; //刚进来此页面时，地理位置内容默认是不显示的
    private boolean isShowProjectMaterial = false; //刚进来此页面时，项目材料内容默认是不显示的

    private ImageView iv_oversea_detail; // 顶部图片
    private TextView tv_pro_house_name; //  房子名称
    private TextView tv_pro_house_price; // 房子价格
    private TextView tv_pro_house_area; //  房子面积
    private TextView tv_pro_name; // 项目中文名称
    private TextView tv_pro_city; // 项目所在地区
    private TextView tv_pro_position; // 项目位置
    private TextView tv_pro_type; // 项目类型
    private TextView tv_pro_count; // 项目体量
    private TextView tv_pro_decoration_standard; // 装修标准
    private TextView tv_pro_house_desc; // 项目描述


    private ViewPager vp;
    private int currentPos;
    private TextView tv_vp_page;
    private HouseDetailAdapter mAdapter; // 轮播图Adapter

    //  项目居室、配套设施、地理位置、项目材料  后面的箭头图标
    private ImageView iv_project_click;
    private ImageView iv_support_facilities_click;
    private ImageView iv_geographic_location_click;
    private ImageView iv_project_material;

    private RelativeLayout rl_pro_house; // 项目居室 布局
    private RelativeLayout rl_pro_facilities; //  配套设施 布局
    private RelativeLayout rl_pro_geographic_location; //  地理位置布局
    private RelativeLayout rl_project_material; // 项目材料布局

    private LinearLayout ll_pro_house_photos; // 项目居室 布局
    private LinearLayout ll_support_facilities; //  配套设施 布局
    private LinearLayout ll_geographic_location; //  地理位置 布局
    private LinearLayout ll_project_material; // 项目材料 布局
    private TextView tv_project_des; // 项目居室 的描述
    private TextView tv_support_facilities_desc; //  配套设施 的描述
    private TextView tv_geographic_location_desc; //  地理位置 的描述
    private MyListView project_material_list; // 用于展示项目材料的list

    private String pid;
    private OverseaProjectDetail2B overseaProjectDetail;
    private ArrayList<String> houseTypeImgList;
    private MyListView myListView; // 相关房源列表
    private MouldList<OverseaProjectDetail3B> relatedhouseList; // 相关房源列表
    private RelatedHouseAdapter myAdapter; // 相关房源 Adapter
    private RelativeLayout rl_empty_house;// 相关房源没数据时显示的提示语


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
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back)
             .setCenterText(getResources().getString(R.string.title_oversea_project_detail)).showMore(true).setTitleRigterIcon(R.drawable.ic_share_title)
             .setOnActionListener(new TitleBar.OnActionListener() {

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

        rl_empty_house = (RelativeLayout) findViewById(R.id.rl_empty_house);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) findViewById(R.id.img_empty);
        tv_empty.setText("暂无相关房源");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_house_resources);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        iv_oversea_detail = (ImageView) findViewById(R.id.iv_oversea_detail);
        iv_project_click = (ImageView) findViewById(R.id.iv_project_click);
        iv_support_facilities_click = (ImageView) findViewById(R.id.iv_support_facilities_click);
        iv_geographic_location_click = (ImageView) findViewById(R.id.iv_geographic_location_click);
        iv_project_material = (ImageView) findViewById(R.id.iv_project_material);

        rl_pro_house = (RelativeLayout) findViewById(R.id.rl_pro_house);
        rl_pro_facilities = (RelativeLayout) findViewById(R.id.rl_pro_facilities);
        rl_pro_geographic_location = (RelativeLayout) findViewById(R.id.rl_pro_geographic_location);
        rl_project_material = (RelativeLayout) findViewById(R.id.rl_project_material);


        tv_pro_house_name = (TextView) findViewById(R.id.tv_pro_house_name);
        tv_pro_house_price = (TextView) findViewById(R.id.tv_pro_house_price);
        tv_pro_house_area = (TextView) findViewById(R.id.tv_pro_house_area);
        tv_pro_name = (TextView) findViewById(R.id.tv_pro_name);
        tv_pro_city = (TextView) findViewById(R.id.tv_pro_city);
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
        ll_project_material = (LinearLayout) findViewById(R.id.ll_project_material);

        myListView = (MyListView) findViewById(R.id.lv);
        project_material_list = (MyListView) findViewById(R.id.project_material_list);

        vp = (ViewPager) findViewById(R.id.vp);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);

        rl_pro_house.setOnClickListener(this);
        rl_pro_facilities.setOnClickListener(this);
        rl_pro_geographic_location.setOnClickListener(this);
    }

    private void initData() {
        requestDetailData();
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(mContext, HouseDetailActivity.class);
                intent.putExtra("hid", relatedhouseList.get(position).getHid());
                startActivity(intent);

            }
        });
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

    // 图片切换时更新数字
    private void updateNum() {
        if (houseTypeImgList.size() > 0) {
            tv_vp_page.setText(currentPos + 1 + "/" + houseTypeImgList.size());
        } else {
            tv_vp_page.setText(0 + "/" + 0);
        }
    }


    private void setView() {
        //加载顶部图片
        ImageLoader.getInstance().displayImage(overseaProjectDetail.getProjectImg(), iv_oversea_detail);

        if (!TextUtils.isEmpty(overseaProjectDetail.getName())) {
            tv_pro_house_name.setText(overseaProjectDetail.getName());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getPrice())) {
            tv_pro_house_price.setText(overseaProjectDetail.getPrice() + "万元起");
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getArea())) {
            tv_pro_house_area.setText("面积" + overseaProjectDetail.getArea());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getChineseName())) {
            tv_pro_name.setText(overseaProjectDetail.getChineseName());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getCity())) {
            tv_pro_city.setText(overseaProjectDetail.getCity());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getLocation())) {
            tv_pro_position.setText(overseaProjectDetail.getLocation());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getCategory())) {
            tv_pro_type.setText(overseaProjectDetail.getCategory());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getTotal())) {
            tv_pro_count.setText(overseaProjectDetail.getTotal() + "套");
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getDecorateStandard())) {
            tv_pro_decoration_standard.setText(overseaProjectDetail.getDecorateStandard());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getProjectDesc())) {
            tv_pro_house_desc.setText(overseaProjectDetail.getProjectDesc());
        }

        houseTypeImgList = overseaProjectDetail.getHouseTypeImg();
        mAdapter = new HouseDetailAdapter(mContext, houseTypeImgList);
        vp.setAdapter(mAdapter);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(currentPos);

        updateNum();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        scrollView.smoothScrollTo(0, 0);
        requestDetailData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pro_house:   // 项目居室
                if (!isShowHouse) {
                    ll_pro_house_photos.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(overseaProjectDetail.getHouseType())) {
                        tv_project_des.setText(overseaProjectDetail.getHouseType());
                    }
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShowHouse = true;
                } else {
                    isShowHouse = false;
                    ll_pro_house_photos.setVisibility(View.GONE);
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.rl_pro_facilities:  // 配套设施
                if (!isShowFacilities) { // 布局处于打开状态
                    ll_support_facilities.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(overseaProjectDetail.getSupportFacility())) {
                        tv_support_facilities_desc.setText(overseaProjectDetail.getSupportFacility());
                    }
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShowFacilities = true;
                } else { // 关闭状态
                    isShowFacilities = false;
                    ll_support_facilities.setVisibility(View.GONE);
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.rl_pro_geographic_location:  // 地理位置
                if (!isShowLocation) {
                    ll_geographic_location.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(overseaProjectDetail.getGeographyLocation())) {
                        tv_geographic_location_desc.setText(overseaProjectDetail.getGeographyLocation());
                    }
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShowLocation = true;
                } else {
                    isShowLocation = false;
                    ll_geographic_location.setVisibility(View.GONE);
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.rl_project_material: // 项目材料
                if (!isShowProjectMaterial) {
                    ll_project_material.setVisibility(View.VISIBLE);
                    // Todo 等后台接口好了修改此处代码
                    /*if (!TextUtils.isEmpty(overseaProjectDetail.getGeographyLocation())) {
                        tv_geographic_location_desc.setText(overseaProjectDetail.getGeographyLocation());
                    }*/
                    iv_project_material.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShowProjectMaterial = true;
                } else {
                    isShowProjectMaterial = false;
                    ll_project_material.setVisibility(View.GONE);
                    iv_project_material.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
        }
    }

    /**
     * 获取海外项目详情页的数据
     */
    private void requestDetailData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("pid", pid);

        HtmlRequest.getOverseaDetailData(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    overseaProjectDetail = (OverseaProjectDetail2B) params.result;
                    if (overseaProjectDetail != null) {
                        relatedhouseList = overseaProjectDetail.getList();
                        if (relatedhouseList != null && relatedhouseList.size() > 0) {
                            myAdapter = new RelatedHouseAdapter(mContext, relatedhouseList);
                            myListView.setAdapter(myAdapter);
                            myListView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.smoothScrollTo(0, 0);
                                }
                            }, 500);
                        } else {
                            rl_empty_house.setVisibility(View.VISIBLE);
                        }
                        setView();
                    }
                }
            }
        });
    }
}
