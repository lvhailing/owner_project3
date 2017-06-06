package com.haidehui.photo_preview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.act.BaseActivity;

import java.util.ArrayList;

/**
 * 图片预览页面
 * 注意，SHOW_SELECT_BTN属性为true时或底部操作栏显示时，KEY_SELECTED 值不能为null
 * Created by lmnrenbc on 2017/5/2.
 */

public class PhotoPreviewAc extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ImageView iv_back;
    private TextView tv_num;

    private ArrayList<String> urls = new ArrayList<>();
    private PreviewAdapter previewAdapter;
    private int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_photo_preview);
        initUI();
    }


    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        tv_num = (TextView) findViewById(R.id.tv_num);

        iv_back.setOnClickListener(this);

        urls = getIntent().getStringArrayListExtra("urls");
        currentPos = getIntent().getIntExtra("currentPos", 0);

        //设置适配器
        previewAdapter = new PreviewAdapter(this, urls);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(previewAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
//        mViewPager.setCurrentItem(currentPos);

        updateNum();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentPos = position;
            updateNum();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void updateNum() {
        tv_num.setText((currentPos + 1) + "/" + urls.size());
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(pageChangeListener);
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

}
