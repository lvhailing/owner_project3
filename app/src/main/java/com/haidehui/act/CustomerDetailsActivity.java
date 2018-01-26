package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.CustomerDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import android.text.TextUtils;


/**
 *  客户信息详情
 */
public class CustomerDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
//    private ImageView img_add_follow;
    private String customerId;
    private TextView tv_name; // 客户姓名
    private TextView tv_phone; // 联系电话
//    private TextView tv_email;
    private TextView tv_location; // 房产所在地
    private TextView tv_project; // 项目
    private TextView tv_room_number; // 房号
    private TextView tv_area; // 面积
    private TextView tv_total_amount; // 总价
    private TextView tv_time; // 提交时间
    private CustomerDetails2B data;

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
        ActivityStack stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        iv_back= (ImageView) findViewById(R.id.iv_back);
//        img_add_follow= (ImageView) findViewById(R.id.img_add_follow);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
//        tv_email= (TextView) findViewById(R.id.tv_email);
        tv_location= (TextView) findViewById(R.id.tv_location);
        tv_project= (TextView) findViewById(R.id.tv_project);
        tv_room_number= (TextView) findViewById(R.id.tv_room_number);
        tv_area= (TextView) findViewById(R.id.tv_area);
        tv_total_amount= (TextView) findViewById(R.id.tv_total_amount);
        tv_time= (TextView) findViewById(R.id.tv_time);

        iv_back.setOnClickListener(this);
    }

    private void initData() {
        customerId=getIntent().getStringExtra("customerId");

        requestData();
    }

    /**
     *  获取客户信息详情数据
     */
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
                        data = (CustomerDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }

    private void setData(CustomerDetails2B data) {
        tv_name.setText(data.getCustomerName());
        tv_phone.setText(data.getCustomerPhone());
//        tv_email.setText(data.getCustomerEmail());

        if (TextUtils.isEmpty(data.getHouseLocation())){
            tv_location.setText("--");
        }else{
            tv_location.setText(data.getHouseLocation());
        }
        if (TextUtils.isEmpty(data.getHouseProject())){
            tv_project.setText("--");
        }else{
            tv_project.setText(data.getHouseProject());
        }
        if (TextUtils.isEmpty(data.getRoomNumber())){
            tv_room_number.setText("--");
        }else{
            tv_room_number.setText(data.getRoomNumber());
        }
        if (TextUtils.isEmpty(data.getArea())){
            tv_area.setText("--");
        }else{
            tv_area.setText(data.getArea());
        }
        if (TextUtils.isEmpty(data.getTotalPrice())){
            tv_total_amount.setText("--");
        }else{
            tv_total_amount.setText(data.getTotalPrice());
        }
        tv_time.setText(data.getCreateTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
//            case R.id.img_add_follow:
//                Intent intent = new Intent(CustomerDetailsActivity.this, AddCustomerFollowActivity.class);
//                intent.putExtra("customerId", data.getCustomerId());
//                startActivity(intent);
//                break;
        }
    }

}
