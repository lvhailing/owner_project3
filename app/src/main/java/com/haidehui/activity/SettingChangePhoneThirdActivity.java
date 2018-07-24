package com.haidehui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 修改手机——填写验证码
 * Created by hasee on 2017/6/12.
 */
public class SettingChangePhoneThirdActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_change_phone_get_verify_code;       //  获取验证码
    private EditText et_change_phone_verify_code;       //  输入验证码

    private boolean smsflag = true;
    private boolean flag = true;
    private MyHandler mHandler;
    private int time = 60;
    private String btnString;
    private TitleBar title;

    private String mobile = "";
    private String validateCode;

    private TextView tv_change_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting_change_phone_third);
        initTopTitle();
        initView();
        initData();


    }

    public void initData(){

        smsflag = true;
        startThread();


    }

    private void request() {

        validateCode = et_change_phone_verify_code.getText().toString();

        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("userId", userId);
        param.put("newMobile", mobile);
        param.put("validateCode", validateCode);

        HtmlRequest.changePhoneThird(SettingChangePhoneThirdActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {

                        Toast.makeText(SettingChangePhoneThirdActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
//                        UserLoadout out = new UserLoadout(SettingChangePhoneThirdActivity.this,userId);
//                        out.requestData();
                        Intent i_login = new Intent(SettingChangePhoneThirdActivity.this, LoginActivity.class);
                        i_login.putExtra("GOTOMAIN",LoginActivity.GOTOMAIN);
                        startActivity(i_login);
                        finish();

                    } else {
                        Toast.makeText(SettingChangePhoneThirdActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(SettingChangePhoneThirdActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void initView(){

        mHandler = new MyHandler();
        btnString = getResources().getString(R.string.sign_getsms_again);
        mobile = getIntent().getStringExtra("mobile");

        tv_change_phone_get_verify_code = (TextView) findViewById(R.id.tv_change_phone_get_verify_code);
        et_change_phone_verify_code = (EditText) findViewById(R.id.et_change_phone_verify_code);
        tv_change_phone = (TextView) findViewById(R.id.tv_change_phone);

        tv_change_phone.setText(mobile);

        tv_change_phone_get_verify_code.setOnClickListener(this);

        et_change_phone_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    title.setChildBankground(R.drawable.shape_center_gray,false);
                }else{
                    title.setChildBankground(R.drawable.shape_center_orange,true);
                }
            }
        });


    }

    private void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))

                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .addAction(new TitleBar.Action(2, 0, R.color.light_blue1),SettingChangePhoneThirdActivity.this.getResources().getString(R.string.setting_change_phone_third_submit),R.drawable.shape_center_gray)
                .setCenterText(getResources().getString(R.string.setting_change_phone_third_title))
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
//                Toast.makeText(SettingChangePhoneThirdActivity.this,"////************",Toast.LENGTH_SHORT).show();

                request();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        mHandler.removeCallbacks(myRunnable);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_change_phone_get_verify_code:
                tv_change_phone_get_verify_code.setClickable(false);
                requestSMS();
//                smsflag = true;
//                startThread();

                break;

            default:

                break;


        }


    }

    private void requestSMS() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("busiType", Urls.MOBILEEDIT);
        param.put("mobile", mobile);

        HtmlRequest.sentSMS(SettingChangePhoneThirdActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {
                        Toast.makeText(SettingChangePhoneThirdActivity.this, "短信发送成功",
                                Toast.LENGTH_LONG).show();
                        smsflag = true;
                        startThread();
                    } else {
                        tv_change_phone_get_verify_code.setClickable(true);
                        smsflag = false;
                        Toast.makeText(SettingChangePhoneThirdActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    tv_change_phone_get_verify_code.setClickable(true);
                    Toast.makeText(SettingChangePhoneThirdActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void startThread() {
        if (smsflag) {
            Thread t = new Thread(myRunnable);
            flag = true;
            t.start();
        }
    }


    Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            while (flag) {
                Message msg = new Message();
                time -= 1;
                msg.arg1 = time;
                if (time == 0) {
                    flag = false;
                    mHandler.sendMessage(msg);
                    time = 60;
                    mHandler.removeCallbacks(myRunnable);
                } else {
                    mHandler.sendMessage(msg);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setButtonStyle(msg.arg1);
        }

    }

    private void setButtonStyle(int time) {
        if (time == 0) {
            tv_change_phone_get_verify_code.setClickable(true);
            tv_change_phone_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_change_phone_get_verify_code.setBackgroundResource(R.drawable.shape_center_orange);
            tv_change_phone_get_verify_code.setText(getResources().getString(
                    R.string.sign_getsms_again));
        } else if (time < 60) {
            tv_change_phone_get_verify_code.setClickable(false);
            tv_change_phone_get_verify_code.setBackgroundResource(R.drawable.shape_center_gray);
            tv_change_phone_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_change_phone_get_verify_code.setText(btnString+"("+time+")");

        }
    }


}
