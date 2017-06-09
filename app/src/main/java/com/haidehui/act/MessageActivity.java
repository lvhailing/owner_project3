package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.widget.TitleBar;

/**
 * 消息页面
 * Created by hasee on 2017/6/8.
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout rl_messgae_info;     //  收支消息
    private RelativeLayout rl_messgae_notice;       //  公告通知
    private RelativeLayout rl_messgae_other;        //  其他
    private TextView tv_message_info_num;       //  收支消息数量
    private TextView tv_message_notice_num;       //  公告消息数量
    private TextView tv_message_other_num;       //  其他消息数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message);

        initTopTitle();
        initView();



    }
    public void initView(){

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
                .setCenterText(getResources().getString(R.string.login_sign))
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                break;
            case R.id.rl_messgae_other:

                break;

            default:

                break;


        }
    }
}
