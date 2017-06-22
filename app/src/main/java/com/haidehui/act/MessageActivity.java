package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.ResultMessageInfoContentBean;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 消息页面
 * Created by hasee on 2017/6/8.
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout rl_messgae_info;     //  收支消息(账本消息)
    private RelativeLayout rl_messgae_notice;       //  公告通知
    private RelativeLayout rl_messgae_other;        //  其他
    private TextView tv_message_info_num;       //  收支消息数量
    private TextView tv_message_notice_num;       //  公告消息数量
    private TextView tv_message_other_num;       //  其他消息数量
    private ResultMessageInfoContentBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message);

        initTopTitle();
        initView();




    }

    public void initData(){

        requestData();

    }

    public void initView(){

        bean = new ResultMessageInfoContentBean();

        rl_messgae_info = (RelativeLayout) findViewById(R.id.rl_messgae_info);
        rl_messgae_notice = (RelativeLayout) findViewById(R.id.rl_messgae_notice);
        rl_messgae_other = (RelativeLayout) findViewById(R.id.rl_messgae_other);

        tv_message_info_num = (TextView) findViewById(R.id.tv_message_info_num);
        tv_message_notice_num = (TextView) findViewById(R.id.tv_message_notice_num);
        tv_message_other_num = (TextView) findViewById(R.id.tv_message_other_num);

        rl_messgae_info.setOnClickListener(this);
        rl_messgae_notice.setOnClickListener(this);
        rl_messgae_other.setOnClickListener(this);

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.message_title))
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

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("userId", userId);

        HtmlRequest.getMessageInfo(MessageActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                bean = (ResultMessageInfoContentBean) params.result;
                if (bean != null) {
                   setView();

                } else {
                    Toast.makeText(MessageActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setView(){

        tv_message_info_num.setText(bean.getCountNum());
        tv_message_notice_num.setText(bean.getBulletNum());
        tv_message_other_num.setText(bean.getOthersNum());

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_messgae_info:
                Intent i_info = new Intent(this,MessageInfoActivity.class);
                startActivity(i_info);

                break;
            case R.id.rl_messgae_notice:
                Intent i_notice = new Intent(this,MessageNoticeActivity.class);
                startActivity(i_notice);
                break;
            case R.id.rl_messgae_other:
                Intent i_other = new Intent(this,MessageOtherActivity.class);
                startActivity(i_other);
                break;

            default:

                break;


        }
    }



}
