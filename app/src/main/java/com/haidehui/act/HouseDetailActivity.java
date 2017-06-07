package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.MyAdapter;
import com.haidehui.photo_preview.PhotoPreviewAc;
import com.haidehui.widget.TitleBar;

import java.util.ArrayList;

/**
 * 房源详情
 */
public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private int currentPos;
    private TextView tv_vp_page;
    private ImageView image;
    private ArrayList<ImageView> imageList;
    private ArrayList<String> list;
    private TextView tv_house_name;
    private MyAdapter mAdapter;

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
             .setCenterText(getResources().getString(R.string.title_house_detail)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        vp = (ViewPager) findViewById(R.id.vp);
        tv_house_name = (TextView) findViewById(R.id.tv_house_name);
        tv_vp_page = (TextView) findViewById(R.id.tv_vp_page);

        list = new ArrayList<>();
        list.add("http://pic17.nipic.com/20111022/6322714_173008780359_2.jpg");
        list.add("http://www.mincoder.com/assets/images/avatar.jpg");
        list.add("http://pic.58pic.com/58pic/12/74/05/99C58PICYck.jpg");
        list.add("http://f12.baidu.com/it/u=89957531,1663631515&fm=76");
        list.add("http://f10.baidu.com/it/u=1304563494,724196614&fm=76");

        mAdapter = new MyAdapter(mContext,list);
        vp.setAdapter(mAdapter);
        mAdapter.setOnImageListener(new MyAdapter.ImageViewListener() {
            @Override
            public void onImageClick(int postion, View imageView) {
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
