package com.haidehui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.haidehui.R;
import com.haidehui.widget.TitleBar;


/**
 *  收益详情
 */
public class CommissionDetailsActivity extends BaseActivity implements View.OnClickListener {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_commission_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_commission_details))
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

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
