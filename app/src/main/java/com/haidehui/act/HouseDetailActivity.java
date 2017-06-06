package com.haidehui.act;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.myAdapter;
import com.haidehui.widget.TitleBar;

import java.util.ArrayList;

/**
 * 房源详情
 */
public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private TextView tv_house_name;
    private TextView tv_vp_page;
    private ArrayList<ImageView> imageList;

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
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_house_detail))
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
//        id = getIntent().getStringExtra("id");
//        vp = (ViewPager) findViewById(R.id.vp);
        tv_house_name = (TextView) findViewById(R.id.tv_house_name);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);
//
//        int[] imgResIDs = {R.drawable.a, R.drawable.b, R.drawable.c};
//        imageList = new ArrayList<ImageView>();
//        for (int i = 0; i < imgResIDs.length; i++) {
//            ImageView image = new ImageView(this);
//            image.setBackgroundResource(imgResIDs[i]);
//            imageList.add(image);
//        }

//        vp.setAdapter(new myAdapter(this, imageList));
//        vp.setOffscreenPageLimit(5);
//        vp.setOnPageChangeListener(new MyOnPageChangeListener());
//        vp.setPageTransformer(true, new ZoomOutPageTransformer());
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
            //改变当前所选图片的位置
            tv_vp_page.setText(position + 1 + "/" + imageList.size());
        }
    }

    private void setView() {
        //加载图片
//        ImageLoader.getInstance().displayImage(detail.getGolfPhoto(), iv_detail_photo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.btn_submit:   //立即预约
//                Intent intent = new Intent(GolfDetailActivity.this, SubBookingGolfActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("name", detail.getGolfName());
//                intent.putExtra("golfRights", detail.getGolfRights());//高尔夫权限  not：优惠价  A1：嘉宾价  A2和VIP（都显示）：会员价
//                startActivity(intent);
//                break;
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
