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
 * 找回密码
 * Created by hasee on 2017/6/6.
 */

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_findpassword_phone;        //  注册的手机号
    private TextView tv_findpassword_get_verify_code;       //  获取验证码
    private EditText et_findpassword_verify_code;       //  验证码
    private EditText et_findpassword_password;      //  密码
    private EditText et_findpassword_password_again;     //
    private Button btn_findpassword;        //  密码重置

    private String mobile = "";
    private String verifyCode = "";
    private String password = "";
    private String password_again = "";


    private boolean smsflag = true;
    private boolean flag = true;
    private MyHandler mHandler;
    private String btnString;
    private int time = 60;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_findpassword);
        initTopTitle();
        initView();



    }

    public void initView(){


        context = this;
        et_findpassword_phone = (EditText) findViewById(R.id.et_findpassword_phone);
        tv_findpassword_get_verify_code = (TextView) findViewById(R.id.tv_findpassword_get_verify_code);
        et_findpassword_verify_code = (EditText) findViewById(R.id.et_findpassword_verify_code);
        et_findpassword_password = (EditText) findViewById(R.id.et_findpassword_password);
        et_findpassword_password_again = (EditText) findViewById(R.id.et_findpassword_password_again);
        btn_findpassword = (Button) findViewById(R.id.btn_findpassword);

        tv_findpassword_get_verify_code.setOnClickListener(this);
        btn_findpassword.setOnClickListener(this);
        btn_findpassword.setClickable(false);

        mHandler = new MyHandler();
        btnString = getResources().getString(R.string.sign_getsms_again);



        checkNull();



    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.findpassword_titile))
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

            case R.id.btn_findpassword:         //      密码重置

                if(password.equals(password_again)){

                    if(StringUtil.checkPassword(password)){
                        findpassword();
                    }else{
                        Toast.makeText(context,"请输入8至16位字母数字组合密码",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(context,"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.signup_web:       //  海德汇协议

                break;
            case R.id.tv_findpassword_get_verify_code:          //  获取验证码

//                requestSMS();

                if(!TextUtils.isEmpty(mobile.trim())){
                    tv_findpassword_get_verify_code.setClickable(false);
                    requestSMS();
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
        param.put("mobile", mobile);
        param.put("busiType", Urls.LOGINRET);

        HtmlRequest.sentSMS(FindPasswordActivity.this, param,new BaseRequester.OnRequestListener() {

                    @Override
                    public void onRequestFinished(BaseParams params) {
                        ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                        if (b != null) {
                            if (Boolean.parseBoolean(b.getFlag())) {
                                Toast.makeText(FindPasswordActivity.this, "短信发送成功",
                                        Toast.LENGTH_LONG).show();
                                smsflag = true;
                                startThread();
                            } else {
                                tv_findpassword_get_verify_code.setClickable(true);
                                smsflag = false;
                                Toast.makeText(FindPasswordActivity.this,
                                        b.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            tv_findpassword_get_verify_code.setClickable(true);
                            Toast.makeText(FindPasswordActivity.this, "加载失败，请确认网络通畅",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void findpassword() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("mobile", mobile);
        param.put("newPassword", password);
        param.put("validateCode", verifyCode);

        HtmlRequest.findPassword(FindPasswordActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {
                        Toast.makeText(FindPasswordActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                        Intent i_login = new Intent(FindPasswordActivity.this, LoginActivity.class);
                        i_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i_login);
                        finish();
                    } else {
                        Toast.makeText(FindPasswordActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(FindPasswordActivity.this, "加载失败，请确认网络通畅",
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
            tv_findpassword_get_verify_code.setClickable(true);
            tv_findpassword_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_findpassword_get_verify_code.setBackgroundResource(R.drawable.shape_center_orange);
            tv_findpassword_get_verify_code.setText(getResources().getString(
                    R.string.sign_getsms_again));
        } else if (time < 60) {
            tv_findpassword_get_verify_code.setClickable(false);
            tv_findpassword_get_verify_code.setBackgroundResource(R.drawable.shape_center_gray);
            tv_findpassword_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_findpassword_get_verify_code.setText(btnString+"("+time+")");

        }
    }

    public void checkNull(){

        //  监听输入手机号变化

        et_findpassword_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_findpassword.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_findpassword.setClickable(false);
                }else{
                    mobile = et_findpassword_phone.getText().toString();
                    verifyCode = et_findpassword_verify_code.getText().toString();
                    password = et_findpassword_password.getText().toString();
                    password_again = et_findpassword_password_again.getText().toString();

                    ViewUtils.setButton(verifyCode,password,password_again,btn_findpassword);

                }
            }
        });

        //  监听输入验证码

        et_findpassword_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_findpassword.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_findpassword.setClickable(false);
                }else{

                    mobile = et_findpassword_phone.getText().toString();
                    verifyCode = et_findpassword_verify_code.getText().toString();
                    password = et_findpassword_password.getText().toString();
                    password_again = et_findpassword_password_again.getText().toString();

                    ViewUtils.setButton(mobile,password,password_again,btn_findpassword);

                }
            }
        });

        //  监听输入密码变化

        et_findpassword_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_findpassword.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_findpassword.setClickable(false);
                }else{
                    mobile = et_findpassword_phone.getText().toString();
                    verifyCode = et_findpassword_verify_code.getText().toString();
                    password = et_findpassword_password.getText().toString();
                    password_again = et_findpassword_password_again.getText().toString();

                    ViewUtils.setButton(mobile,verifyCode,password_again,btn_findpassword);
                }
            }
        });

        //  监听真实姓名输入变化

        et_findpassword_password_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    btn_findpassword.setBackgroundResource(R.drawable.shape_center_gray);
                    btn_findpassword.setClickable(false);
                }else{
                    mobile = et_findpassword_phone.getText().toString();
                    verifyCode = et_findpassword_verify_code.getText().toString();
                    password = et_findpassword_password.getText().toString();
                    password_again = et_findpassword_password_again.getText().toString();
                    ViewUtils.setButton(mobile,verifyCode,password,btn_findpassword);

                }
            }
        });

    }

}
