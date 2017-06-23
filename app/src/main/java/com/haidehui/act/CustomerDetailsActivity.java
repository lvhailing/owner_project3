package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.CustomerDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;


/**
 *  客户信息详情
 */
public class CustomerDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private ImageView img_add_follow;
    private String customerId;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_location;
    private TextView tv_project;
    private TextView tv_room_number;
    private TextView tv_area;
    private TextView tv_total_amount;
    private TextView tv_time;

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
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_email= (TextView) findViewById(R.id.tv_email);
        tv_location= (TextView) findViewById(R.id.tv_location);
        tv_project= (TextView) findViewById(R.id.tv_project);
        tv_room_number= (TextView) findViewById(R.id.tv_room_number);
        tv_area= (TextView) findViewById(R.id.tv_area);
        tv_total_amount= (TextView) findViewById(R.id.tv_total_amount);
        tv_time= (TextView) findViewById(R.id.tv_time);

    }

    private void initData() {
        customerId=getIntent().getStringExtra("customerId");
        img_back.setOnClickListener(this);
        img_add_follow.setOnClickListener(this);
        requestData();
    }
    private void requestData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        param.put("userId", userId);
        HtmlRequest.getCustomerInFoDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        CustomerDetails2B data = (CustomerDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }

    private void setData(CustomerDetails2B data) {
        tv_name.setText(data.getCustomerName());
        tv_phone.setText(data.getCustomerPhone());
        tv_email.setText(data.getCustomerEmail());
        tv_location.setText(data.getHouseLocation());
        tv_project.setText(data.getHouseProject());
        tv_room_number.setText(data.getRoomNumber());
        tv_area.setText(data.getArea());
        tv_total_amount.setText(data.getTotalPrice());
        tv_time.setText(data.getCreateTime());
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
