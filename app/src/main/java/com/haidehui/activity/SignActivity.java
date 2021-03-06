package com.haidehui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.StringUtil;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 用户注册
 * Created by hasee on 2017/6/6.
 */

public class SignActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_sign_phone; //  用户名
    private TextView tv_sign_get_verify_code; //  获取验证码
    private EditText et_sign_verify_code; //  验证码
    private EditText et_sign_password; //  密码
    private EditText et_sign_real_name; //  真实姓名
    private EditText et_sign_recommendation; //  推荐码
    private CheckBox signup_checkbox; //  同意协议
    private TextView signup_web; //  服务协议
    private Button btn_sign;  //  立即注册

    private String mobile = "";
    private String verifyCode = "";
    private String password = "";
    private String realName = "";
    private String recommendation = "";

    private boolean smsflag = true;
    private boolean flag = true;
    private MyHandler mHandler;
    private String btnString;
    private int time = 60;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_sign);
        initTopTitle();
        initView();

    }

    public void initView(){
        context = this;

        et_sign_phone = (EditText) findViewById(R.id.et_sign_phone);
        tv_sign_get_verify_code = (TextView) findViewById(R.id.tv_sign_get_verify_code);
        et_sign_verify_code = (EditText) findViewById(R.id.et_sign_verify_code);
        et_sign_password = (EditText) findViewById(R.id.et_sign_password);
        et_sign_real_name = (EditText) findViewById(R.id.et_sign_real_name);
        et_sign_recommendation = (EditText) findViewById(R.id.et_sign_recommendation);
        signup_checkbox = (CheckBox) findViewById(R.id.signup_checkbox);
        signup_web = (TextView) findViewById(R.id.signup_web);
        btn_sign = (Button) findViewById(R.id.btn_sign);

        signup_web.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
        tv_sign_get_verify_code.setOnClickListener(this);
        btn_sign.setClickable(false);

        mHandler = new MyHandler();
        btnString = getResources().getString(R.string.sign_getsms_again);

        checkNull();

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
        mHandler.removeCallbacks(myRunnable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign: //  立即注册
                if(StringUtil.checkPassword(password)){
                    signup();
                }else{
                    Toast.makeText(context,"请输入8至16位字母数字组合密码",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.signup_web: // 海德汇协议
                Intent i_service = new Intent(SignActivity.this,WebActivity.class);
                i_service.putExtra("type", WebActivity.WEBTYPE_SIGN_AGREEMENT);
                i_service.putExtra("title", getResources().getString(R.string.setting_sign_agreement));
                i_service.putExtra("url", Urls.URL_SIGNUP_WEB_AGREEMENT);
                startActivity(i_service);

                break;
            case R.id.tv_sign_get_verify_code:  // 获取验证码
                if(!TextUtils.isEmpty(mobile.trim())){
                    if(StringUtil.isMobileNO(mobile)){
                        tv_sign_get_verify_code.setClickable(false);
                        requestSMS();
                    }else{
                        Toast.makeText(context,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"请输入手机号",Toast.LENGTH_SHORT).show();
                }

//                smsflag = true;
//                startThread();
                break;

            default:

                break;
        }

    }

    private void requestSMS() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("busiType", Urls.REGISTER);
        param.put("mobile", mobile);

        HtmlRequest.sentSMS(SignActivity.this, param,new BaseRequester.OnRequestListener() {

                    @Override
                    public void onRequestFinished(BaseParams params) {
                        ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                        if (b != null) {
                            if (Boolean.parseBoolean(b.getFlag())) {
                                Toast.makeText(SignActivity.this, "短信发送成功",
                                        Toast.LENGTH_LONG).show();
                                smsflag = true;
                                startThread();
                            } else {
                                tv_sign_get_verify_code.setClickable(true);
                                smsflag = false;
                                Toast.makeText(SignActivity.this,
                                        b.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            Toast.makeText(SignActivity.this, "加载失败，请确认网络通畅",
                                    Toast.LENGTH_LONG).show();
                            tv_sign_get_verify_code.setClickable(true);
                        }
                    }
                });
    }

    private void signup() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("mobile", mobile);
        param.put("validateCode", verifyCode);
        param.put("password", password);
        param.put("realName", realName);
        param.put("parentRecommendCode", recommendation);

        HtmlRequest.signup(SignActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if(b.getFlag().equals("true")){
                        Toast.makeText(SignActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }else{
                        Toast.makeText(SignActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(SignActivity.this, "加载失败，请确认网络通畅",
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
            tv_sign_get_verify_code.setClickable(true);
            tv_sign_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_sign_get_verify_code.setBackgroundResource(R.drawable.shape_center_orange);
            tv_sign_get_verify_code.setText(getResources().getString(
                    R.string.sign_getsms_again));
        } else if (time < 60) {
            tv_sign_get_verify_code.setClickable(false);
            tv_sign_get_verify_code.setBackgroundResource(R.drawable.shape_center_gray);
            tv_sign_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_sign_get_verify_code.setText(btnString+"("+time+")");

        }
    }

    public void checkNull(){

        //  监听输入手机号变化

        et_sign_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_sign.setClickable(false);
                }else{
                    mobile = et_sign_phone.getText().toString();
                    verifyCode = et_sign_verify_code.getText().toString();
                    password = et_sign_password.getText().toString();
                    realName = et_sign_real_name.getText().toString();
                    recommendation = et_sign_recommendation.getText().toString();
                    if(!signup_checkbox.isChecked()){
                        btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                        btn_sign.setClickable(false);
                    }else{
                        ViewUtils.setButton(verifyCode,password,realName,btn_sign);
                    }

                }
            }
        });

        //  监听输入验证码

        et_sign_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_sign.setClickable(false);
                }else{
                    mobile = et_sign_phone.getText().toString();
                    verifyCode = et_sign_verify_code.getText().toString();
                    password = et_sign_password.getText().toString();
                    realName = et_sign_real_name.getText().toString();
                    recommendation = et_sign_recommendation.getText().toString();
                    if(!signup_checkbox.isChecked()){
                        btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                        btn_sign.setClickable(false);
                    }else{
                        ViewUtils.setButton(mobile,password,realName,btn_sign);
                    }

                }
            }
        });

        //  监听输入密码变化

        et_sign_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_sign.setClickable(false);
                }else{
                    verifyCode = et_sign_verify_code.getText().toString();
                    mobile = et_sign_phone.getText().toString();
                    password = et_sign_password.getText().toString();
                    realName = et_sign_real_name.getText().toString();
                    recommendation = et_sign_recommendation.getText().toString();
                    if(!signup_checkbox.isChecked()){
                        btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                        btn_sign.setClickable(false);
                    }else{
                        ViewUtils.setButton(mobile,verifyCode,realName,btn_sign);
                    }
                }
            }
        });

        //  监听真实姓名输入变化

        et_sign_real_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_sign.setClickable(false);
                }else{
                    verifyCode = et_sign_verify_code.getText().toString();
                    password = et_sign_password.getText().toString();
                    mobile = et_sign_phone.getText().toString();
                    realName = et_sign_real_name.getText().toString();
                    recommendation = et_sign_recommendation.getText().toString();
                    if(!signup_checkbox.isChecked()){
                        btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                        btn_sign.setClickable(false);
                    }else{
                        ViewUtils.setButton(mobile,verifyCode,password,btn_sign);
                    }

                }
            }
        });

        //  监听同意协议事件

        signup_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    btn_sign.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_sign.setClickable(false);
                }else{
                    mobile = et_sign_phone.getText().toString();
                    verifyCode = et_sign_verify_code.getText().toString();
                    password = et_sign_password.getText().toString();
                    realName = et_sign_real_name.getText().toString();
                    recommendation = et_sign_recommendation.getText().toString();
                    ViewUtils.setButton(mobile,verifyCode,password,realName,btn_sign);
                }
            }
        });

        et_sign_recommendation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recommendation = et_sign_recommendation.getText().toString();
            }
        });


    }



}
