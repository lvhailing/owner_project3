package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.dialog.VerifyPassWordDialog;
import com.haidehui.net.UserLoadout;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.widget.TitleBar;

import java.util.Set;

/**
 * 设置页面
 * Created by hasee on 2017/6/12.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_setting_change_phone; // 修改手机
    private ImageButton ib_setting_gesture; // 手势密码
    private RelativeLayout rl_setting_change_gesture_password; // 修改手势密码
    private RelativeLayout rl_setting_change_password; // 修改登录密码
    private RelativeLayout rl_setting_service_agreement; // 服务协议

    private RelativeLayout rl_setting_privacy_agreement; // 隐私协议
    private RelativeLayout rl_setting_advice;  // 意见反馈
    private RelativeLayout rl_setting_about;  //  关于我们
    private RelativeLayout rl_setting_version; //  版本号
    private TextView tv_setting_version_code;  //  版本号
    private TextView tv_setting_logout;  // 退出登录

    private ActivityStack stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting);
        initTopTitle();

        initView();
        initData();


    }

    public void initView() {
        stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        rl_setting_change_phone = (RelativeLayout) findViewById(R.id.rl_setting_change_phone);
        ib_setting_gesture = (ImageButton) findViewById(R.id.ib_setting_gesture);
        rl_setting_change_gesture_password = (RelativeLayout) findViewById(R.id.rl_setting_change_gesture_password);
        rl_setting_change_password = (RelativeLayout) findViewById(R.id.rl_setting_change_password);
        rl_setting_service_agreement = (RelativeLayout) findViewById(R.id.rl_setting_service_agreement);
        rl_setting_privacy_agreement = (RelativeLayout) findViewById(R.id.rl_setting_privacy_agreement);
        rl_setting_advice = (RelativeLayout) findViewById(R.id.rl_setting_advice);
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
        rl_setting_about.setOnClickListener(this);
        rl_setting_version.setOnClickListener(this);
        tv_setting_logout.setOnClickListener(this);

    }

    public void initData() {
        tv_setting_version_code.setText(SystemInfo.sVersionName);
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back).setCenterText(getResources().getString(R.string.setting_title)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

        if (PreferenceUtil.isGestureChose()) {
            ib_setting_gesture.setImageResource(R.mipmap.img_set_on);
            rl_setting_change_gesture_password.setVisibility(View.VISIBLE);

        } else {
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

        switch (view.getId()) {

            case R.id.rl_setting_change_phone:

                Intent i_change_phone = new Intent(this, SettingChangePhoneFirstActivity.class);
                startActivity(i_change_phone);

                break;

            case R.id.ib_setting_gesture:

                if (PreferenceUtil.isGestureChose()) {
                    ib_setting_gesture.setImageResource(R.mipmap.img_set_off);
                    rl_setting_change_gesture_password.setVisibility(View.GONE);
                    PreferenceUtil.setGestureChose(false);

                } else {

                    Intent i = new Intent(this, GestureEditActivity.class);
                    i.putExtra("from", Urls.ACTIVITY_GESEDIT);
                    i.putExtra("title", getResources().getString(R.string.setup_gesture_code));
                    i.putExtra("message", getResources().getString(R.string.setup_gesture_pattern));
                    startActivityForResult(i, 1000);

//                    ib_setting_gesture.setImageResource(R.mipmap.img_set_on);
//                    rl_setting_change_gesture_password.setVisibility(View.VISIBLE);
//                    PreferenceUtil.setGestureChose(true);

                }

                break;

            case R.id.rl_setting_change_gesture_password: // 修改手势密码

                Intent i = new Intent(SettingActivity.this, GestureVerifyActivity.class);
                i.putExtra("from", Urls.ACTIVITY_CHANGE_GESTURE);
                i.putExtra("title", getResources().getString(R.string.title_changegesture));
                i.putExtra("message", getResources().getString(R.string.set_gesture_pattern_old));
                startActivityForResult(i, 1001);

                break;

            case R.id.rl_setting_change_password: // 修改登录密码
                Intent i_change = new Intent(SettingActivity.this, SettingChangePasswordActivity.class);
                startActivity(i_change);

                break;

            case R.id.rl_setting_service_agreement:     // 服务协议
                Intent i_service = new Intent(SettingActivity.this, WebActivity.class);
                i_service.putExtra("type", WebActivity.WEBTYPE_SIGN_AGREEMENT);
                i_service.putExtra("title", getResources().getString(R.string.setting_service_agreement));
                i_service.putExtra("url", Urls.URL_SERVICE_AGREEMENT);
                startActivity(i_service);

                break;

            case R.id.rl_setting_privacy_agreement: //  隐私协议（作废）
                Intent i_privacy = new Intent(SettingActivity.this, WebActivity.class);
                startActivity(i_privacy);

                break;

            case R.id.rl_setting_advice:  // 意见反馈
                Intent i_advice = new Intent(SettingActivity.this, AdviceActivity.class);
                startActivity(i_advice);

                break;

            case R.id.rl_setting_about: // 关于我们
                Intent i_about = new Intent(SettingActivity.this, WebActivity.class);
                i_about.putExtra("type", WebActivity.WEBTYPE_ABOUT_US);
                i_about.putExtra("title", getResources().getString(R.string.setting_about));
                i_about.putExtra("url", Urls.URL_ABOUT_US);
                startActivity(i_about);
                break;

            case R.id.rl_setting_version:       //  版本号
//                Intent i_version = new Intent(SettingActivity.this,VersionActivity.class);
//                startActivity(i_version);
                Intent i_version = new Intent(SettingActivity.this, WebActivity.class);
                i_version.putExtra("type", WebActivity.WEBTYPE_ABOUT_US);
                i_version.putExtra("title", getResources().getString(R.string.setting_version));
                i_version.putExtra("url", Urls.URL_VERSION + SystemInfo.sVersionName);
                startActivity(i_version);
                break;
            case R.id.tv_setting_logout:

                UserLoadout out = new UserLoadout(SettingActivity.this, userId);
                out.requestData();
                finish();

                break;


            default:

                break;


        }


    }
}
