package com.haidehui.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.net.UserLoadout;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 修改密码
 * Created by hasee on 2017/6/12.
 */
public class SettingChangePasswordActivity extends BaseActivity{

    private EditText et_change_password_old;        //  旧密码
    private EditText et_change_password_new;        //  新密码
    private EditText et_change_password_new_again;        //  新密码验证
    private TitleBar title;
    private String oldPassword =  "";
    private String newPassword =  "";
    private String newPasswordAgain =  "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting_change_password);
        initTopTitle();
        initView();

    }

    public void initView(){

        context = this;
        et_change_password_old = (EditText) findViewById(R.id.et_change_password_old);
        et_change_password_new = (EditText) findViewById(R.id.et_change_password_new);
        et_change_password_new_again = (EditText) findViewById(R.id.et_change_password_new_again);

        et_change_password_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                oldPassword = et_change_password_old.getText().toString();
                newPassword = et_change_password_new.getText().toString();
                newPasswordAgain = et_change_password_new_again.getText().toString();
                checkButton(editable.toString(),newPassword,newPasswordAgain);

            }
        });

        et_change_password_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                oldPassword = et_change_password_old.getText().toString();
                newPassword = et_change_password_new.getText().toString();
                newPasswordAgain = et_change_password_new_again.getText().toString();
                checkButton(oldPassword,editable.toString(),newPasswordAgain);

            }
        });

        et_change_password_new_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                oldPassword = et_change_password_old.getText().toString();
                newPassword = et_change_password_new.getText().toString();
                newPasswordAgain = et_change_password_new_again.getText().toString();
                checkButton(oldPassword,newPassword,editable.toString());

            }
        });

    }

    private void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))

                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .addAction(new TitleBar.Action(2, 0, R.color.blue_light),SettingChangePasswordActivity.this.getResources().getString(R.string.setting_change_password_sure),R.drawable.shape_center_gray)
                .setCenterText(getResources().getString(R.string.setting_change_password_title))
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

//                Toast.makeText(SettingChangePasswordActivity.this,"////************",Toast.LENGTH_SHORT).show();
//                Intent i_second = new Intent(SettingChangePasswordActivity.this,SettingChangePhoneSecondActivity.class);
//                startActivity(i_second);


                if(StringUtil.checkPassword(newPassword)){
                    if(newPassword.equals(newPasswordAgain)){
                        changePassword();
                    }else{
                        Toast.makeText(context,"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(context,"请输入8至16位字母数字组合密码",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void changePassword() {

        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("newPassword", newPassword);
        param.put("password", oldPassword);
        param.put("userId", userId);
        HtmlRequest.changePassword(SettingChangePasswordActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if(b.getFlag().equals("true")){
                        Toast.makeText(SettingChangePasswordActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
//                        UserLoadout out = new UserLoadout(SettingChangePasswordActivity.this,userId);
//                        out.requestData();
                        Intent i_login = new Intent(SettingChangePasswordActivity.this, LoginActivity.class);
//                        i_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i_login.putExtra("GOTOMAIN",LoginActivity.GOTOMAIN);
                        startActivity(i_login);
                        finish();
                    }else{
                        Toast.makeText(SettingChangePasswordActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(SettingChangePasswordActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
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
    }

    public void checkButton(String str1,String str2,String str3){
        if(TextUtils.isEmpty(str1)){
            title.setChildBankground(R.drawable.shape_center_gray,false);
        }else{
            if(TextUtils.isEmpty(str2)){
                title.setChildBankground(R.drawable.shape_center_gray,false);
            }else{
                if(TextUtils.isEmpty(str3)){
                    title.setChildBankground(R.drawable.shape_center_gray,false);
                }else{
                    title.setChildBankground(R.drawable.shape_center_orange,true);
                }
            }
        }
    }

}
