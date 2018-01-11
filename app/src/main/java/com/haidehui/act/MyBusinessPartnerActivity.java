package com.haidehui.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.fragment.InvestmentGuideFragment;
import com.haidehui.fragment.OneLevelRecommendationFragment;
import com.haidehui.fragment.ProductRoadshowFragment;
import com.haidehui.fragment.TwoLevelRecommendationFragment;
import com.haidehui.widget.TitleBar;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;

/**
 * Created by hong on 2018/1/11.
 */

public class MyBusinessPartnerActivity extends BaseActivity implements View.OnClickListener {

    private TitleBar titleBar;
    private ViewPager vp;
    private TextView tv_one_level_recommendation; // 一级推荐
    private TextView tv_two_level_recommendation; // 二级推荐
    private View v_line;
    private ArrayList<Fragment> fragments;
    private OneLevelRecommendationFragment oneLevelFragment; // 一级推荐 Fragment
    private TwoLevelRecommendationFragment twoLevelFragment; // 二级推荐 Fragment
    private int screenWidth; //屏幕宽度
    private int line_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_my_business_partner);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.showLeftImg(true);
        titleBar.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                 .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_business_partner))
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
        vp = (ViewPager) findViewById(R.id.vp);
        tv_one_level_recommendation = (TextView) findViewById(R.id.tv_one_level_recommendation);
        tv_two_level_recommendation = (TextView) findViewById(R.id.tv_two_level_recommendation);
        v_line = findViewById(R.id.line);

        tv_one_level_recommendation.setOnClickListener(this);
        tv_two_level_recommendation.setOnClickListener(this);

        // 默认设置第0个title状态
        setTitleStyle(0);

        fragments = new ArrayList<Fragment>();
        oneLevelFragment = new OneLevelRecommendationFragment();
        twoLevelFragment = new TwoLevelRecommendationFragment();
        fragments.add(oneLevelFragment);
        fragments.add(twoLevelFragment);

        // 屏幕宽度
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        // 设置下划线宽度
        line_width = screenWidth / fragments.size();
        v_line.getLayoutParams().width = line_width;
        v_line.requestLayout();

        vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setTitleStyle(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //此处在手滑动时 会被不停调用
                float tagerX = arg0 * line_width + arg2 / fragments.size();
                ViewPropertyAnimator.animate(v_line).translationX(tagerX).setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }
        // 修改文字的颜色
        private void setTitleStyle(int pos) {
            if (pos == 0) {
                tv_one_level_recommendation.setTextColor(getResources().getColor(R.color.txt_orange));
                tv_two_level_recommendation.setTextColor(getResources().getColor(R.color.txt_light_gray));
            } else if (pos == 1) {
                tv_one_level_recommendation.setTextColor(getResources().getColor(R.color.txt_light_gray));
                tv_two_level_recommendation.setTextColor(getResources().getColor(R.color.txt_orange));
            }

        }

        //控制线条滚动，每点击一次调用一次
        private void setLineStyle(int pos) {
            ViewPropertyAnimator.animate(v_line).translationX(screenWidth / 2 * pos).setDuration(300);
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tv_one_level_recommendation:  // 一级推荐
        setTitleStyle(0);
        setLineStyle(0);
        vp.setCurrentItem(0);
//        investmentGuideFr.upDateInvestmentGuideList();

        break;
        case R.id.tv_two_level_recommendation: // 二级推荐
        setTitleStyle(1);
        setLineStyle(1);
        vp.setCurrentItem(1);
//        roadShowFr.upDateRoadShowList();

        break;
        }

    }
}
