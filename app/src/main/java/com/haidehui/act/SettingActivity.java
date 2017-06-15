package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.dialog.VerifyPassWordDialog;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.widget.TitleBar;

import java.util.Set;

/**
 * 设置页面
 * Created by hasee on 2017/6/12.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rl_setting_change_phone;     //  修改手机
    private ImageButton ib_setting_gesture;             //  手势密码
    private RelativeLayout rl_setting_change_gesture_password;     //  修改手势密码
    private RelativeLayout rl_setting_change_password;     //  修改登录密码
    private RelativeLayout rl_setting_service_agreement;     //  服务条款
    private RelativeLayout rl_setting_privacy_agreement;     //  隐私协议
    private RelativeLayout rl_setting_advice;     //  意见反馈
    private RelativeLayout rl_setting_invite;     //  推荐朋友
    private RelativeLayout rl_setting_about;     //  关于我们
    private RelativeLayout rl_setting_version;     //  版本号
    private TextView tv_setting_version_code;       //  版本号
    private TextView tv_setting_logout;         //  退出登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting);
        initTopTitle();

        initView();
        initData();


    }

    public void initView(){
        rl_setting_change_phone = (RelativeLayout) findViewById(R.id.rl_setting_change_phone);
        ib_setting_gesture = (ImageButton) findViewById(R.id.ib_setting_gesture);
        rl_setting_change_gesture_password = (RelativeLayout) findViewById(R.id.rl_setting_change_gesture_password);
        rl_setting_change_password = (RelativeLayout) findViewById(R.id.rl_setting_change_password);
        rl_setting_service_agreement = (RelativeLayout) findViewById(R.id.rl_setting_service_agreement);
        rl_setting_privacy_agreement = (RelativeLayout) findViewById(R.id.rl_setting_privacy_agreement);
        rl_setting_advice = (RelativeLayout) findViewById(R.id.rl_setting_advice);
        rl_setting_invite = (RelativeLayout) findViewById(R.id.rl_setting_invite);
        rl_setting_about = (RelativeLayout) findViewById(R.id.rl_setting_about);
        rl_setting_version = (RelativeLayout) findViewById(R.id.rl_setting_version);

        tv_setting_version_code = (TextView) findViewById(R.id.tv_setting_version_code);
        tv_setting_logout = (TextView) findViewById(R.id.tv_setting_logout);

        rl_setting_change_phone.setOnClickListener(this);
        ib_setting_gesture.setOnClickListener(this);
        rl_setting_change_gesture_password.setOnClickListener(this);
        rl_setting_change_password.setOnClickListener(this);
        rl_setting_service_agreement.setOnClickListener(this);
        rl_setting_privacy_agreement.setOnClickListener(this);
        rl_setting_advice.setOnClickListener(this);
        rl_setting_invite.setOnClickListener(this);
        rl_setting_about.setOnClickListener(this);
        rl_setting_version.setOnClickListener(this);
        tv_setting_logout.setOnClickListener(this);

    }

    public void initData(){

        tv_setting_version_code.setText(SystemInfo.sVersionName);

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.setting_title))
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

        if(PreferenceUtil.isGestureChose()){
            ib_setting_gesture.setImageResource(R.mipmap.img_set_on);
            rl_setting_change_gesture_password.setVisibility(View.VISIBLE);

        }else{
            ib_setting_gesture.setImageResource(R.mipmap.img_set_off);
            rl_setting_change_gesture_password.setVisibility(View.GONE);
//            imgGestureSwitch.setVisibility(View.GONE);
        }


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

            case R.id.rl_setting_change_phone:

                Intent i_change_phone = new Intent(this,SettingChangePhoneFirstActivity.class);

                startActivity(i_change_phone);

                break;

            case R.id.ib_setting_gesture:

                if(PreferenceUtil.isGestureChose()){
                    ib_setting_gesture.setImageResource(R.mipmap.img_set_off);
                    rl_setting_change_gesture_password.setVisibility(View.GONE);
                    PreferenceUtil.setGestureChose(false);

                }else{
                    ib_setting_gesture.setImageResource(R.mipmap.img_set_on);
                    rl_setting_change_gesture_password.setVisibility(View.VISIBLE);
                    PreferenceUtil.setGestureChose(true);
//            imgGestureSwitch.setVisibility(View.GONE);
                }

                /*if(PreferenceUtil.isGestureChose()){

                    VerifyPassWordDialog dialog_gesture = new VerifyPassWordDialog(SettingActivity.this, new VerifyPassWordDialog.OnVerifyPW() {
                        @Override
                        public void onConfirm(String input) {

//                            requestData(input,"gesrure");

                        }

                        @Override
                        public void onCancel() {

                        }
                    },"请输入登录密码");
                    dialog_gesture.show();


					*//*Intent i = new Intent(this,
							GestureVerifyActivity.class);
					i.putExtra("from", ApplicationConsts.ACTIVITY_ACCOUNT);
					i.putExtra("title","手势密码登录");
					i.putExtra("message","请画出手势密码解锁");
					startActivityForResult(i, 1000);*//*
                }else{
//                    Intent i = new Intent(this,
//                            GestureEditActivity.class);
//                    i.putExtra("comeflag", 4);
//                    i.putExtra("title", R.string.setup_gesture_code);
//                    i.putExtra("skip","skip_from_account");
//                    startActivity(i);

                }*/
                PreferenceUtil.setFirstLogin(true);


                break;

            case R.id.rl_setting_change_gesture_password:       //  修改手势密码


                break;

            case R.id.rl_setting_change_password:       //  修改登录密码

                Intent i_change = new Intent(SettingActivity.this,SettingChangePasswordActivity.class);

                startActivity(i_change);

                break;

            case R.id.rl_setting_service_agreement:     // 服务协议

                Intent i_service = new Intent(SettingActivity.this,WebActivity.class);

                startActivity(i_service);

                break;

            case R.id.rl_setting_privacy_agreement:     //  隐私协议

                Intent i_privacy = new Intent(SettingActivity.this,WebActivity.class);

                startActivity(i_privacy);

                break;

            case R.id.rl_setting_advice:        //  意见反馈

                Intent i_advice = new Intent(SettingActivity.this,AdviceActivity.class);

                startActivity(i_advice);

                break;

            case R.id.rl_setting_invite:        //      邀请好友

                Intent i_recommend = new Intent(SettingActivity.this,RecommendActivity.class);
                startActivity(i_recommend);

                break;

            case R.id.rl_setting_about:     //  关于我们
                Intent i_about = new Intent(SettingActivity.this,AboutActivity.class);

                startActivity(i_about);
                break;

            case R.id.rl_setting_version:       //  版本号
                Intent i_version = new Intent(SettingActivity.this,VersionActivity.class);

                startActivity(i_version);
                break;
            case R.id.tv_setting_logout:


                break;


            default:

                break;



        }


    }
}
