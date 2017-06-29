package com.haidehui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.CommissionDetails2B;
import com.haidehui.model.WithDrawDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;


/**
 *  提现详情
 */
public class WithDrawDetailsActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private TextView tv_cashNum;
    private TextView tv_cashStatus;
    private TextView tv_id;
    private TextView tv_account;
    private TextView tv_crateTime;
    private RelativeLayout layout_delete;
    private TextView tv_remark;
    private ImageView img_delete;
    private String messageId;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_withdraw_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_withdraw_details))
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
        tv_cashNum= (TextView) findViewById(R.id.tv_cashNum);
        tv_cashStatus= (TextView) findViewById(R.id.tv_cashStatus);
        tv_id= (TextView) findViewById(R.id.tv_id);
        tv_account= (TextView) findViewById(R.id.tv_account);
        tv_crateTime= (TextView) findViewById(R.id.tv_crateTime);

        layout_delete= (RelativeLayout) findViewById(R.id.layout_delete);
        tv_remark= (TextView) findViewById(R.id.tv_remark);
        img_delete= (ImageView) findViewById(R.id.img_delete);

    }

    private void initData() {
        img_delete.setOnClickListener(this);
        id=getIntent().getStringExtra("id");
        messageId=getIntent().getStringExtra("messageId");
        requestData();
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("id", id);
        param.put("messageId", messageId);
        param.put("userId", userId);
        HtmlRequest.getWithDrawDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        WithDrawDetails2B data = (WithDrawDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }
    private void setData(WithDrawDetails2B data) {
        tv_cashNum.setText("-"+data.getCashNum());
        if (data.getCashStatus().equals("checking")){
            tv_cashStatus.setText("审核中");
        }else if (data.getCashStatus().equals("paying")){
            tv_cashStatus.setText("审核通过");
        }else if (data.getCashStatus().equals("fail")){
            tv_cashStatus.setText("审核失败");
            layout_delete.setVisibility(View.VISIBLE);
            tv_remark.setText(data.getRemark());

        }else if (data.getCashStatus().equals("success")){
            tv_cashStatus.setText("佣金已发放");
        }
        tv_id.setText(data.getId());
        tv_account.setText(data.getRealName()+"\n"+data.getIdNo()+"\n"+data.getBankName()+"\n"+data.getBankCardNum());
        tv_crateTime.setText(data.getCrateTime());

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_delete:
                layout_delete.setVisibility(View.GONE);
                break;

        }
    }

}
