package com.haidehui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.AttachmentAdapter;
import com.haidehui.adapter.HouseDetailAdapter;
import com.haidehui.adapter.HouseDetailPlanImgAdapter;
import com.haidehui.adapter.RelatedHouseAdapter;
import com.haidehui.model.OverseaProjectDetail2B;
import com.haidehui.model.OverseaProjectDetailHouseList3B;
import com.haidehui.model.OverseaProjectDetailPdfList3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.photo_preview.PhotoPreviewAc;
import com.haidehui.widget.FileManager;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 海外项目详情
 */
public class OverseaProjectDetailActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView scrollView;

    /**
     *  刚进来此页面时，项目居室、项目规划、配套设施、
     *  地理位置、项目材料等内容默认都不显示
     */
    private boolean isShowHouse = false;
    private boolean isShowPlan = false;
    private boolean isShowFacilities = false;
    private boolean isShowLocation = false;
    private boolean isShowProjectMaterial = false;

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

    private ViewPager vp_living_room; // 项目居室 轮播图
    private ViewPager vp_project_plan; // 项目规划 轮播图
    private int currentPos; // 项目居室图 当前位置
    private int currentPlanImgPos; // 项目规划图 当前位置
    private TextView tv_living_room_page;
    private TextView tv_project_plan_page;
    private HouseDetailAdapter mAdapter; // 轮播图Adapter

    //  项目居室、配套设施、地理位置、项目材料  后面的箭头图标
    private ImageView iv_project_click;
    private ImageView iv_project_plan_arrow;
    private ImageView iv_support_facilities_click;
    private ImageView iv_geographic_location_click;
    private ImageView iv_project_material;

    private RelativeLayout rl_project_house; // 项目居室 布局
    private RelativeLayout rl_project_plan; // 项目规划 布局
    private RelativeLayout rl_pro_facilities; //  配套设施 布局
    private RelativeLayout rl_pro_geographic_location; //  地理位置布局
    private RelativeLayout rl_project_material; // 项目材料布局

    private LinearLayout ll_pro_house_photos; // 项目居室 布局
    private LinearLayout ll_project_plan_photos; // 项目规划 布局
    private LinearLayout ll_support_facilities; //  配套设施 布局
    private LinearLayout ll_geographic_location; //  地理位置 布局
    private LinearLayout ll_project_material; // 项目材料 布局
    private TextView tv_project_des; // 项目居室 的描述
    private TextView tv_support_facilities_desc; //  配套设施 的描述
    private TextView tv_geographic_location_desc; //  地理位置 的描述
    private MyListView project_material_list; // 用于展示项目材料的list
    private MouldList<OverseaProjectDetailPdfList3B> attachmentList;
    private AttachmentAdapter attachmentAdapter;

    private String pid;
    private OverseaProjectDetail2B overseaProjectDetail;
    private ArrayList<String> houseTypeImgList; // 项目居室图片
    private MyListView myListView; // 相关房源列表
    private MouldList<OverseaProjectDetailHouseList3B> relatedhouseList; // 相关房源列表
    private RelatedHouseAdapter myAdapter; // 相关房源 Adapter
    private RelativeLayout rl_empty_house;// 相关房源没数据时显示的提示语
    private ArrayList<String> projectPlanImgList; // 项目规划图片
    private HouseDetailAdapter houseDetailAdapter;
    private HouseDetailPlanImgAdapter houseDetailPlanImgAdapter;
    private TitleBar titleBar;
    private String projectPath;
    private String projectMaterialName;
    private String url = null;
    private TextView tv_no_project_material; // 暂无相关材料
    private int currentPage;
    private Context context;
    private Dialog imageDialog;
    private ArrayList<String> topImageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_oversea_project_detail);

        initView();
        initData();
        initTopTitle();
    }

    private void initTopTitle() {
        titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.showLeftImg(true);
        titleBar.setFromActivity("1000");
        titleBar.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.mipmap.icon_back)
                 .setCenterText(getResources().getString(R.string.title_oversea_project_detail)).showMore(false).setTitleRightButton(R.drawable.ic_share_title)
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
        context = OverseaProjectDetailActivity.this;

        rl_empty_house = (RelativeLayout) findViewById(R.id.rl_empty_house);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) findViewById(R.id.img_empty);
        tv_empty.setText("暂无相关房源");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_house_resources);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        iv_oversea_detail = (ImageView) findViewById(R.id.iv_oversea_detail);
        iv_project_click = (ImageView) findViewById(R.id.iv_project_click);
        iv_project_plan_arrow = (ImageView) findViewById(R.id.iv_project_plan_arrow);
        iv_support_facilities_click = (ImageView) findViewById(R.id.iv_support_facilities_click);
        iv_geographic_location_click = (ImageView) findViewById(R.id.iv_geographic_location_click);
        iv_project_material = (ImageView) findViewById(R.id.iv_project_material);

        rl_project_house = (RelativeLayout) findViewById(R.id.rl_project_house);
        rl_project_plan = (RelativeLayout) findViewById(R.id.rl_project_plan);
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
        tv_no_project_material = (TextView) findViewById(R.id.tv_no_project_material);

        ll_pro_house_photos = (LinearLayout) findViewById(R.id.ll_pro_house_photos);
        ll_project_plan_photos = (LinearLayout) findViewById(R.id.ll_project_plan_photos);
        ll_support_facilities = (LinearLayout) findViewById(R.id.ll_support_facilities);
        ll_geographic_location = (LinearLayout) findViewById(R.id.ll_geographic_location);
        ll_project_material = (LinearLayout) findViewById(R.id.ll_project_material);

        myListView = (MyListView) findViewById(R.id.lv);
        project_material_list = (MyListView) findViewById(R.id.project_material_list);

        vp_living_room = (ViewPager) findViewById(R.id.vp_living_room);
        vp_project_plan = (ViewPager) findViewById(R.id.vp_project_plan);
        tv_living_room_page = (TextView) findViewById(R.id.tv_living_room_page);
        tv_project_plan_page = (TextView) findViewById(R.id.tv_project_plan_page);

        iv_oversea_detail.setOnClickListener(this);
        rl_project_house.setOnClickListener(this);
        rl_project_plan.setOnClickListener(this);
        rl_pro_facilities.setOnClickListener(this);
        rl_pro_geographic_location.setOnClickListener(this);
        rl_project_material.setOnClickListener(this);
    }

    private void initData() {
        pid = getIntent().getStringExtra("pid");

        requestDetailData(); // 获取海外项目详情页数据

        // 项目材料列表 点击监听
        project_material_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Intent intent = new Intent(mContext, WebActivity.class);
//                intent.putExtra("type", WebActivity.WEBTYPE_PROJECT_MATERIAL_DETAIL);
//                intent.putExtra("url", attachmentList.get(position).getPath());
//                PDF在线打开的代码
//                String nameEncode=attachmentList.get(position).getName();
//                String pathUrlEncode=attachmentList.get(position).getPath();
//                try{
//                    if(!TextUtils.isEmpty(nameEncode)){
//                        nameEncode= URLEncoder.encode(nameEncode,"UTF-8");
//                        nameEncode= URLEncoder.encode(nameEncode,"UTF-8");
//                    }
//                    if(!TextUtils.isEmpty(pathUrlEncode)){
//                        pathUrlEncode= URLEncoder.encode(pathUrlEncode,"UTF-8");
//                        pathUrlEncode=URLEncoder.encode(pathUrlEncode,"UTF-8");
//                    }
//                }catch (Exception e){
//                    System.out.println(e);
//                }
//                intent.putExtra("url", Urls.URL_VIEW_PDF +nameEncode+"&&path="+pathUrlEncode);
//                intent.putExtra("url",attachmentList.get(position).getPath() );
//                intent.putExtra("title", attachmentList.get(position).getName());
//                startActivity(intent);

                // 调用第三方打开pdf
                url = attachmentList.get(position).getPath();
                DownloaderTask task = new DownloaderTask();
                task.execute(url);

            }
        });

        // 相关房源列表 点击监听
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(mContext, HouseDetailActivity.class);
                intent.putExtra("hid", relatedhouseList.get(position).getHid());
                startActivity(intent);

            }
        });
    }

    /**
     *  项目居室轮播图回调监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            currentPos = position;
            updateLivingRoomNum();
        }
    }

    /**
     *  项目规划轮播图回调监听
     */
    private class MyPlanImgOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int position) {
            currentPlanImgPos = position;
            updateProjectPlanImgNum();
        }
    }

    /**
     * 项目居室图片切换时更新数字
     */
    private void updateLivingRoomNum() {
        if (houseTypeImgList.size() > 0) {
            tv_living_room_page.setText(currentPos + 1 + "/" + houseTypeImgList.size());
        } else {
            tv_living_room_page.setText(0 + "/" + 0);
        }
    }

    /**
     * 项目规划图片切换时更新数字
     */
    private void updateProjectPlanImgNum() {
        if (projectPlanImgList.size() > 0) {
            tv_project_plan_page.setText(currentPlanImgPos + 1 + "/" + projectPlanImgList.size());
        } else {
            tv_project_plan_page.setText(0 + "/" + 0);
        }
    }


    private void setView() {
        String topProductImg = overseaProjectDetail.getProjectImg();
        if (!TextUtils.isEmpty(topProductImg)) {
            // ImageLoader 加载顶部图片
            ImageLoader.getInstance().displayImage(topProductImg, iv_oversea_detail);

            topImageList = new ArrayList<>();
            topImageList.add(topProductImg);
        }

        if (!TextUtils.isEmpty(overseaProjectDetail.getName())) {
            tv_pro_house_name.setText(overseaProjectDetail.getName());
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getPrice())) {
            tv_pro_house_price.setText(overseaProjectDetail.getPrice() + "万元起");
        }
        if (!TextUtils.isEmpty(overseaProjectDetail.getArea())) {
            tv_pro_house_area.setText("面积" + overseaProjectDetail.getArea()+"㎡");
        }else {
            tv_pro_house_area.setText("--");
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

        // 项目居室轮播图
        houseTypeImgList = overseaProjectDetail.getHouseTypeImg();
        houseDetailAdapter = new HouseDetailAdapter(mContext, houseTypeImgList);
        houseDetailAdapter.setOnImageListener(new HouseDetailAdapter.ImageViewListener() {
            @Override
            public void onImageClick(int position) { // 轮播图点击监听
                Intent intent = new Intent(mContext, PhotoPreviewAc.class);
                intent.putStringArrayListExtra("urls", houseTypeImgList);
                intent.putExtra("currentPos", currentPage);
                startActivity(intent);
            }
        });
        vp_living_room.setAdapter(houseDetailAdapter);
        vp_living_room.setOnPageChangeListener(new MyOnPageChangeListener());
        vp_living_room.setCurrentItem(currentPos);

        updateLivingRoomNum();

        // 项目规划轮播图
        projectPlanImgList = overseaProjectDetail.getProjectPlanImg();
        houseDetailPlanImgAdapter = new HouseDetailPlanImgAdapter(mContext, projectPlanImgList);
        houseDetailPlanImgAdapter.setOnImageListener(new HouseDetailPlanImgAdapter.ImageViewListener() {
            @Override
            public void onImageClick(int position) { // 轮播图点击监听
                Intent intent = new Intent(mContext, PhotoPreviewAc.class);
                intent.putStringArrayListExtra("urls", projectPlanImgList);
                intent.putExtra("currentPos", currentPage);
                startActivity(intent);
            }
        });
        vp_project_plan.setAdapter(houseDetailPlanImgAdapter);
        vp_project_plan.setOnPageChangeListener(new MyPlanImgOnPageChangeListener());
        vp_project_plan.setCurrentItem(currentPlanImgPos);

        updateProjectPlanImgNum();


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
            case R.id.iv_oversea_detail: // 顶部图片
                if(topImageList != null && !topImageList.isEmpty()){
                    Intent intent = new Intent(mContext, PhotoPreviewAc.class);
                    intent.putStringArrayListExtra("urls", topImageList);
                    intent.putExtra("currentPos", currentPage);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"暂无项目图片",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rl_project_house:   // 项目居室
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
            case R.id.rl_project_plan:   // 项目规划
                if (!isShowPlan) {
                    ll_project_plan_photos.setVisibility(View.VISIBLE);
                    iv_project_plan_arrow.setBackgroundResource(R.mipmap.icon_oversea_up);
                    isShowPlan = true;
                } else {
                    isShowPlan = false;
                    ll_project_plan_photos.setVisibility(View.GONE);
                    iv_project_plan_arrow.setBackgroundResource(R.mipmap.icon_oversea_down);
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
                if (!isShowLocation) { // 布局处于打开状态
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
                if (!isShowProjectMaterial) { // 布局处于打开状态
                    ll_project_material.setVisibility(View.VISIBLE);
                    if (attachmentList != null && attachmentList.size() <= 0) {
                        tv_no_project_material.setVisibility(View.VISIBLE);
                    }
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
                        // 项目材料
                        attachmentList = overseaProjectDetail.getAttachment();
                        if (attachmentList != null && attachmentList.size() > 0) {
                            attachmentAdapter = new AttachmentAdapter(mContext, attachmentList);
                            project_material_list.setAdapter(attachmentAdapter);
                        }

                        // 相关房源
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

                        // 设置分享参数
                        titleBar.setActivityParameters(pid, overseaProjectDetail.getChineseName(), overseaProjectDetail.getProjectDesc());
                    }
                }
            }
        });
    }


    private class DownloaderTask extends AsyncTask<String, Void, File> {
        public DownloaderTask() {
        }

        @Override
        protected File doInBackground(String... params) {
            String url1 = params[0];
            if (url1 == null || url1.equals("")) {
                return null;
            }
            String fileName = System.currentTimeMillis() + url1.substring(url1.lastIndexOf("."));
            File file = null;
            String pathName = FileManager.getSecondDir(OverseaProjectDetailActivity.this, "contract") + File.separator + fileName;
            try {
                URL url = new URL(url1);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10 * 1000);

                connection.connect();
                if (connection.getResponseCode() == 200) {
                    int fileLength = connection.getContentLength();
                    if (fileLength <= 0) {
                        return null;
                    }
                    file = new File(pathName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    while (true) {
                        int len = inputStream.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        arrayOutputStream.write(buffer, 0, len);
                    }
                    arrayOutputStream.close();
                    inputStream.close();
                    byte[] data = arrayOutputStream.toByteArray();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return file;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            closeProgressDialog();
            if (result == null) {
                Toast t = Toast.makeText(getApplicationContext(), "连接错误！请稍后再试！", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }

            try {
                //呼唤第三方打开
                Intent intent = getFileIntent(result);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "您尚未安装" + end + "阅读器 ， 建议您到应用市场下载", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        long a = file.length();
        System.out.println(a);
        String type = getMIMEType(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    private String end = "";

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

	      /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
//	      else if(end.equals("pptx")||end.equals("ppt")){
//	    	  type = "application/vnd.ms-powerpoint";
//	      }else if(end.equals("docx")||end.equals("doc")){
//	    	  type = "application/vnd.ms-word";
//	      }else if(end.equals("xlsx")||end.equals("xls")){
//	    	  type = "application/vnd.ms-excel";
//	      }
        else {
//	    	  /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }


    private ProgressDialog mDialog;

    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mDialog = null;
                }
            });
            mDialog.show();
        }
    }

    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


}
