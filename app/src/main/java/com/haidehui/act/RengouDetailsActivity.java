package com.haidehui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.RenGouDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;
import android.widget.TextView;


/**
 *  认购详情
 */
public class RengouDetailsActivity extends BaseActivity implements View.OnClickListener {
    private String id;

    private TextView tv_customerName; // 客户姓名
    private TextView tv_customerPhone; // 联系电话
    private TextView tv_status; // 认购状态
    private TextView tv_projectName; // 项目名称
    private TextView tv_roomNumber; // 认购房号
    private TextView tv_downpaymentAmount; // 定金金额


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_rengou_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_rengou_details))
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
        id=getIntent().getStringExtra("id");

        tv_customerName= (TextView) findViewById(R.id.tv_customer_name);
        tv_customerPhone= (TextView) findViewById(R.id.tv_customer_phone);
        tv_status= (TextView) findViewById(R.id.tv_status);
        tv_projectName= (TextView) findViewById(R.id.tv_projectName);
        tv_roomNumber= (TextView) findViewById(R.id.tv_room_number);
        tv_downpaymentAmount= (TextView) findViewById(R.id.tv_downpaymentAmount);
    }

    private void initData() {
        requestData();
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("id", id);
        param.put("userId", userId);
        HtmlRequest.getRenGouDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        RenGouDetails2B data = (RenGouDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }

    private void setData(RenGouDetails2B data) {
        tv_customerName.setText(data.getCustomerName());
        tv_customerPhone.setText(data.getCustomerPhone());
        if ("subscribe".equals(data.getStatus())){
            tv_status.setText("已认购");
        }else if("turnSign".equals(data.getStatus())){
            tv_status.setText("转签约");
        }else if("unsubscribe".equals(data.getStatus())){
            tv_status.setText("已退订");
        }
        tv_projectName.setText(data.getProjectName());
        tv_roomNumber.setText(data.getRoomNumber());
        tv_downpaymentAmount.setText(data.getDownpaymentAmount()+"元");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
