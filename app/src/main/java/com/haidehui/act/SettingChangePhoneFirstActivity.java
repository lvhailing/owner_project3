package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
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
 * 修改手机——验证登录密码
 * Created by hasee on 2017/6/12.
 */
public class SettingChangePhoneFirstActivity extends BaseActivity{

    private EditText et_change_password;
    private TitleBar title;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting_change_phone_first);
        initTopTitle();
        initView();


    }

    public void initData(){
        changePasswordFirst();

    }

    private void changePasswordFirst() {


        password = et_change_password.getText().toString();
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("password", password);
        HtmlRequest.changePhone(SettingChangePhoneFirstActivity.this, param,new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {
                        Intent i_second = new Intent(SettingChangePhoneFirstActivity.this,SettingChangePhoneSecondActivity.class);
                        startActivity(i_second);
                        finish();
                    } else {

                        Toast.makeText(SettingChangePhoneFirstActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(SettingChangePhoneFirstActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initView(){
        et_change_password = (EditText) findViewById(R.id.et_change_password);


        et_change_password.addTextChangedListener(new TextWatcher() {
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
                .addAction(new TitleBar.Action(2, 0, R.color.blue_light),SettingChangePhoneFirstActivity.this.getResources().getString(R.string.setting_change_phone_next),R.drawable.shape_center_gray)
                .setCenterText(getResources().getString(R.string.setting_change_phone_first_title))
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

//                Toast.makeText(SettingChangePhoneFirstActivity.this,"////************",Toast.LENGTH_SHORT).show();
                initData();



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
}
