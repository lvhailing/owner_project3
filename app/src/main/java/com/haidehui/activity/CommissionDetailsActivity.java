package com.haidehui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.CommissionDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;


/**
 *  收益详情
 */
public class CommissionDetailsActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private TextView tv_commission; // 佣金收益
    private TextView tv_commiStatus; // 结佣状态
    private TextView tv_serialNumber; // 流水号
    private TextView tv_tradeNum; // 成交编号
    private TextView tv_customerName; // 客户姓名
    private TextView tv_customerMobile; // 联系电话
    private TextView tv_emergencyName; // 紧急联系人
    private TextView tv_emergencyMobile; // 紧急电话
    private TextView tv_projectLocation; // 房产所在地
    private TextView tv_projectName; // 项目名称
    private TextView tv_houseNum; // 房号
    private TextView tv_houseType; // 户型
    private TextView tv_houseArea; // 面积
    private TextView tv_purchaseAmount; // 认购总价
    private TextView tv_isCommitAmount; // 认购金
    private TextView tv_isSigned; // 签约状态
    private TextView tv_downPaymentAmount; // 首付款金额
    private TextView tv_repayedRate; // 已付款占比
    private TextView tv_isCommitData; // 客户资料
    private TextView tv_commissionAmountTotal; // 佣金总额
    private TextView tv_commissionRate; // 佣金比例
    private TextView tv_serviceFee; // 服务费用
    private TextView tv_actualCommiRepayed; // 实结佣金
    private TextView tv_commissionTime; // 结佣时间

    private LinearLayout layout_emergencyName;
    private LinearLayout layout_emergencyMobile;
    private LinearLayout layout_serviceFee;

    private String messageId;

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
        tv_commission= (TextView) findViewById(R.id.tv_commission);
        tv_commiStatus= (TextView) findViewById(R.id.tv_commiStatus);
        tv_serialNumber= (TextView) findViewById(R.id.tv_serialNumber);
        tv_tradeNum= (TextView) findViewById(R.id.tv_tradeNum);
        tv_customerName= (TextView) findViewById(R.id.tv_customer_name);
        tv_customerMobile= (TextView) findViewById(R.id.tv_customerMobile);
        tv_emergencyName= (TextView) findViewById(R.id.tv_emergencyName);
        tv_emergencyMobile= (TextView) findViewById(R.id.tv_emergencyMobile);
        tv_projectLocation= (TextView) findViewById(R.id.tv_projectLocation);
        tv_projectName= (TextView) findViewById(R.id.tv_projectName);
        tv_houseNum= (TextView) findViewById(R.id.tv_houseNum);
        tv_houseType= (TextView) findViewById(R.id.tv_houseType);
        tv_houseArea= (TextView) findViewById(R.id.tv_houseArea);
        tv_purchaseAmount= (TextView) findViewById(R.id.tv_purchaseAmount);
        tv_isCommitAmount= (TextView) findViewById(R.id.tv_isCommitAmount);
        tv_isSigned= (TextView) findViewById(R.id.tv_isSigned);
        tv_downPaymentAmount= (TextView) findViewById(R.id.tv_downPaymentAmount);
        tv_repayedRate= (TextView) findViewById(R.id.tv_repayedRate);
        tv_isCommitData= (TextView) findViewById(R.id.tv_isCommitData);
        tv_commissionAmountTotal= (TextView) findViewById(R.id.tv_commissionAmountTotal);
        tv_commissionRate= (TextView) findViewById(R.id.tv_commissionRate);
        tv_serviceFee= (TextView) findViewById(R.id.tv_serviceFee);
        tv_actualCommiRepayed= (TextView) findViewById(R.id.tv_actualCommiRepayed);
        tv_commissionTime= (TextView) findViewById(R.id.tv_commissionTime);

        layout_emergencyName= (LinearLayout) findViewById(R.id.layout_emergencyName);
        layout_emergencyMobile= (LinearLayout) findViewById(R.id.layout_emergencyMobile);
        layout_serviceFee= (LinearLayout) findViewById(R.id.layout_serviceFee);
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
        messageId=getIntent().getStringExtra("messageId");
        requestData();
    }

    /**
     *  获取佣金收获详情页数据
     */
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("id", id);
        param.put("messageId", messageId);
        param.put("userId", userId);
        HtmlRequest.getCommissionDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        CommissionDetails2B data = (CommissionDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }
    private void setData(CommissionDetails2B data) {
        tv_commission.setText("+"+data.getCommissionAmountReal());
        tv_commiStatus.setText("结佣完成");
        tv_serialNumber.setText(data.getSerialNumber());
        tv_tradeNum.setText(data.getTradeNum());
        tv_customerName.setText(data.getCustomerName());
        tv_customerMobile.setText(data.getCustomerMobile());
        tv_projectLocation.setText(data.getProjectLocation());
        tv_projectName.setText(data.getProjectName());
        tv_houseNum.setText(data.getHouseNum());
        tv_houseType.setText(data.getHouseType());
        tv_houseArea.setText(data.getHouseArea()+"㎡");
        tv_purchaseAmount.setText(data.getPurchaseAmount()+"元");
        tv_downPaymentAmount.setText(data.getDownPaymentAmount()+"元");
        tv_repayedRate.setText(data.getRepayedRate()+"%");
        tv_commissionAmountTotal.setText(data.getCommissionAmountTotal()+"元");
        tv_commissionRate.setText(data.getCommissionRate()+"%");
        tv_actualCommiRepayed.setText(data.getCommissionAmountReal()+"元");
        tv_commissionTime.setText(data.getCommissionTime());

        if ("true".equals(data.getIsSigned())){
            tv_isSigned.setText("已签约");
        }else{
            tv_isSigned.setText("未签约");
        }
        if ("true".equals(data.getIsCommitAmount())){
            tv_isCommitAmount.setText("已提交");
        }else{
            tv_isCommitAmount.setText("未提交");
        }
        if ("true".equals(data.getIsCommitData())){
            tv_isCommitData.setText("已提交");
        }else{
            tv_isCommitData.setText("未提交");
        }
        if(!TextUtils.isEmpty(data.getEmergencyName())){
            tv_emergencyName.setText(data.getEmergencyName());
        }else{
            tv_emergencyName.setText("--");
        }

        if(!TextUtils.isEmpty(data.getEmergencyMobile())){
            tv_emergencyMobile.setText(data.getEmergencyMobile());
        }else{
            tv_emergencyMobile.setText("--");
        }
        if(!TextUtils.isEmpty(data.getServiceFee())){
            tv_serviceFee.setText(data.getServiceFee());
        }else{
            tv_serviceFee.setText("--");
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
