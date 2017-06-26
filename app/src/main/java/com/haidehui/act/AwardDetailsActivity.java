package com.haidehui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.AwardDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;
import android.widget.TextView;
import android.widget.LinearLayout;


/**
 *  奖励详情
 */
public class AwardDetailsActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private String messageId;
    private TextView tv_rewardAmount;
    private TextView tv_rewardStatus;
    private TextView tv_rewardType;
    private TextView tv_serialNumber;
    private TextView tv_tradeCode;
    private TextView tv_projectName;
    private TextView tv_houseNum;
    private TextView tv_remark;
    private TextView tv_createTime;

    private LinearLayout layout_tradeCode;
    private LinearLayout layout_projectName;
    private LinearLayout layout_houseNum;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_award_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_award_details))
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
        tv_rewardAmount= (TextView) findViewById(R.id.tv_rewardAmount);
        tv_rewardStatus= (TextView) findViewById(R.id.tv_rewardStatus);
        tv_serialNumber= (TextView) findViewById(R.id.tv_serialNumber);
        tv_tradeCode= (TextView) findViewById(R.id.tv_tradeCode);
        tv_projectName= (TextView) findViewById(R.id.tv_projectName);
        tv_houseNum= (TextView) findViewById(R.id.tv_houseNum);
        tv_remark= (TextView) findViewById(R.id.tv_remark);
        tv_createTime= (TextView) findViewById(R.id.tv_createTime);
        tv_rewardType= (TextView) findViewById(R.id.tv_rewardType);

        layout_tradeCode= (LinearLayout) findViewById(R.id.layout_tradeCode);
        layout_projectName= (LinearLayout) findViewById(R.id.layout_projectName);
        layout_houseNum= (LinearLayout) findViewById(R.id.layout_houseNum);

    }


    private void initData() {
        id=getIntent().getStringExtra("id");
        messageId=getIntent().getStringExtra("messageId");
        requestData();
    }
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("id", id);
        param.put("messageId", messageId);
        param.put("userId", userId);
        HtmlRequest.getAwardDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        AwardDetails2B data = (AwardDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }
    private void setData(AwardDetails2B data) {
        tv_rewardAmount.setText("+"+data.getRewardAmount());
        if(data.getRewardType().equals("download")){
            tv_rewardType.setText("下载奖励");
        }else if(data.getRewardType().equals("recommend")){
            tv_rewardType.setText("推荐奖励");
        }
        if(data.getRewardStatus().equals("sended")){
            tv_rewardStatus.setText("已完成");
        }else{
            tv_rewardStatus.setText("已发放");
            layout_tradeCode.setVisibility(View.GONE);
            layout_projectName.setVisibility(View.GONE);
            layout_houseNum.setVisibility(View.GONE);

        }

        tv_serialNumber.setText(data.getSerialNumber());
        tv_tradeCode.setText(data.getTradeCode());
        tv_projectName.setText(data.getProjectName());

        tv_houseNum.setText(data.getHouseNum());
        tv_remark.setText(data.getRemark());
        tv_createTime.setText(data.getCreateTime());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
