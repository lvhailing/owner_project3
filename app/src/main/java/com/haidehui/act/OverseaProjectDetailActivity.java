package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private OverseaProjectDetail2B overseaProjectDetail;
    private ArrayList<String> houseTypeImgList;
    private MyListView myListView;
    private MouldList<OverseaProjectDetail3B> relatedhouseList; // 相关房源列表
    private RelatedHouseAdapter myAdapter;
    private TextView tv_no_house; // 相关房源没数据时显示的提示语

    private RelativeLayout rl_pro_house,rl_pro_facilities,rl_pro_geographic_location; // 项目居室，配套设施，地理位置布局


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

        rl_pro_house = (RelativeLayout) findViewById(R.id.rl_pro_house);
        rl_pro_facilities = (RelativeLayout) findViewById(R.id.rl_pro_facilities);
        rl_pro_geographic_location = (RelativeLayout) findViewById(R.id.rl_pro_geographic_location);


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
        tv_no_house = (TextView) findViewById(R.id.tv_no_house);

        ll_pro_house_photos = (LinearLayout) findViewById(R.id.ll_pro_house_photos);
        ll_support_facilities = (LinearLayout) findViewById(R.id.ll_support_facilities);
        ll_geographic_location = (LinearLayout) findViewById(R.id.ll_geographic_location);
        myListView = (MyListView) findViewById(R.id.lv);

        vp = (ViewPager) findViewById(R.id.vp);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);

//        iv_project_click.setOnClickListener(this);
//        iv_support_facilities_click.setOnClickListener(this);
//        iv_geographic_location_click.setOnClickListener(this);
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

    private void updateNum() {
        tv_vp_page.setText(currentPos + 1 + "/" + houseTypeImgList.size());
    }


    private void setView() {
        //加载顶部图片
        ImageLoader.getInstance().displayImage(overseaProjectDetail.getProjectImg(), iv_oversea_detail);

        tv_pro_house_name.setText(overseaProjectDetail.getName());
        tv_pro_house_price.setText(overseaProjectDetail.getPrice());
        tv_pro_house_area.setText(overseaProjectDetail.getArea());
        tv_pro_position.setText(overseaProjectDetail.getLocation());
        tv_pro_type.setText(overseaProjectDetail.getCategory());
        tv_pro_count.setText(overseaProjectDetail.getTotal());
        tv_pro_decoration_standard.setText(overseaProjectDetail.getDecorateStandard());
        tv_pro_house_desc.setText(overseaProjectDetail.getProjectDesc());


        houseTypeImgList = overseaProjectDetail.getHouseTypeImg();
        mAdapter = new HouseDetailAdapter(mContext, houseTypeImgList);
        vp.setAdapter(mAdapter);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(currentPos);

        updateNum();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pro_house:   // 项目居室
                if (!isShow) {
                    ll_pro_house_photos.setVisibility(View.VISIBLE);
                    tv_project_des.setText(overseaProjectDetail.getHouseType());
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_pro_house_photos.setVisibility(View.GONE);
                    iv_project_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.rl_pro_facilities:  // 配套设施
                if (!isShow) {
                    ll_support_facilities.setVisibility(View.VISIBLE);
                    tv_support_facilities_desc.setText(overseaProjectDetail.getSupportFacility());
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_support_facilities.setVisibility(View.GONE);
                    iv_support_facilities_click.setBackgroundResource(R.mipmap.icon_oversea_down);
                }
                break;
            case R.id.rl_pro_geographic_location:  // 地理位置
                if (!isShow) {
                    ll_geographic_location.setVisibility(View.VISIBLE);
                    tv_geographic_location_desc.setText(overseaProjectDetail.getGeographyLocation());
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShow = true;
                } else {
                    isShow = false;
                    ll_geographic_location.setVisibility(View.GONE);
                    iv_geographic_location_click.setBackgroundResource(R.mipmap.icon_oversea_down);
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
                        } else {
                            tv_no_house.setVisibility(View.VISIBLE);
                        }
                        setView();
                    }
                }
            }
        });
    }
}
