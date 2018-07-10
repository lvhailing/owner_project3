package com.haidehui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.ResultUserLoginContentBean;
import com.haidehui.net.UserLogin;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.TitleBar;

import java.util.Observable;
import java.util.Observer;

/**
 * 用户登录
 * Created by hasee on 2017/6/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, Observer {

    private EditText et_login_phone; //  用户名
    private EditText et_login_password; //  密码
    private Button btn_login; //  登录
    private TextView tv_login_forget_password; //  忘记密码
    private TextView tv_login_sign; //  注册
    private LinearLayout ll_login_phone_service; //  客服电话
    private ResultUserLoginContentBean bean;
    public static String GOTOMAIN = "1000";
    private String resultCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_login);
        initTopTitle();
        initView();

    }

    public void initView() {
        resultCode = getIntent().getStringExtra("GOTOMAIN");

        PreferenceUtil.setAutoLoginPwd("");
        PreferenceUtil.setLogin(false);
        PreferenceUtil.setPhone("");
        PreferenceUtil.setUserId("");
        PreferenceUtil.setUserNickName("");
        PreferenceUtil.setCookie("");
        PreferenceUtil.setToken("");
        PreferenceUtil.setGestureChose(false);
        PreferenceUtil.setGesturePwd("");

//        bean = new ResultUserLoginContentBean();
        et_login_phone = (EditText) findViewById(R.id.et_login_phone);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_login_forget_password = (TextView) findViewById(R.id.tv_login_forget_password);
        tv_login_sign = (TextView) findViewById(R.id.tv_login_sign);
        ll_login_phone_service = (LinearLayout) findViewById(R.id.ll_login_phone_service);


        btn_login.setOnClickListener(this);
        tv_login_sign.setOnClickListener(this);
        ll_login_phone_service.setOnClickListener(this);
        tv_login_forget_password.setOnClickListener(this);
        btn_login.setClickable(false);
        et_login_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {

                    btn_login.setClickable(false);
                    btn_login.setBackgroundResource(R.drawable.shape_center_gray);

                } else {
                    if (TextUtils.isEmpty(et_login_password.getText().toString())) {
                        btn_login.setClickable(false);
                        btn_login.setBackgroundResource(R.drawable.shape_center_gray);
                    } else {
                        btn_login.setClickable(true);
                        btn_login.setBackgroundResource(R.drawable.shape_center_orange);
                    }

                }
            }
        });

        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    btn_login.setClickable(false);
                    btn_login.setBackgroundResource(R.drawable.shape_center_gray);
                } else {
                    if (TextUtils.isEmpty(et_login_phone.getText().toString())) {
                        btn_login.setClickable(false);
                        btn_login.setBackgroundResource(R.drawable.shape_center_gray);
                    } else {
                        btn_login.setClickable(true);
                        btn_login.setBackgroundResource(R.drawable.shape_center_orange);
                    }


                }
            }
        });

    }

    public void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserLogin.getInstance().addObserver(this);
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
        UserLogin.getInstance().deleteObserver(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login: // 登录
                String username = et_login_phone.getText().toString();
                String password = et_login_password.getText().toString();
                UserLogin.getInstance().userlogining(LoginActivity.this, username, password, "");

                break;

            case R.id.tv_login_forget_password: // 忘记密码
                Intent i_find_password = new Intent(this, FindPasswordActivity.class);
                startActivity(i_find_password);
                break;

            case R.id.tv_login_sign: // 注册
                Intent i_sign = new Intent(this, SignActivity.class);
                startActivity(i_sign);

                break;

            case R.id.ll_login_phone_service: // 客服电话
                Intent i_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.phone_number)));
                i_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_phone);

                break;

            default:

                break;


        }

    }

    @Override
    public void update(Observable observable, Object data) {
        bean = (ResultUserLoginContentBean) data;
        if (bean != null) {
            if (Boolean.parseBoolean(bean.getFlag())) {
                if (!TextUtils.isEmpty(resultCode)) {
                    if (resultCode.equals(GOTOMAIN)) {  //  登录完成跳至主页
                        Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                        iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iMain);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            } else {
                Toast.makeText(LoginActivity.this, bean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(resultCode)) {
                if (resultCode.equals(GOTOMAIN)) {  // 点忘记密码跳转到登录页面时，若用户不登录直接点手机上返回键时，返回到主页
                    Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                    iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(iMain);
                    finish();
                } else {
                    finish();
                }
            } else {
                finish();
            }
        }
        return false;
    }

}
