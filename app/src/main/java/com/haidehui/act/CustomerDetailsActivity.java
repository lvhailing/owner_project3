package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.haidehui.R;
import com.haidehui.widget.TitleBar;


/**
 *  客户信息详情
 */
public class CustomerDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private ImageView img_add_follow;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_customer_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
        img_back= (ImageView) findViewById(R.id.img_back);
        img_add_follow= (ImageView) findViewById(R.id.img_add_follow);
    }

    private void initData() {
        img_back.setOnClickListener(this);
        img_add_follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_add_follow:
                Intent intent = new Intent(CustomerDetailsActivity.this, AddCustomerFollowActivity.class);
                startActivity(intent);
                break;
        }
    }

}
