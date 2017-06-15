package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.widget.TitleBar;

/**
 * 修改手机——修改手机号
 * Created by hasee on 2017/6/12.
 */
public class SettingChangePhoneSecondActivity extends BaseActivity{

    private TextView tv_change_phone;       //  当前手机号
    private EditText et_change_phone;       //  新手机号
    private TitleBar title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_setting_change_phone_second);
        initTopTitle();
        initView();


    }
    public void initView(){

        tv_change_phone = (TextView) findViewById(R.id.tv_change_phone);
        et_change_phone = (EditText) findViewById(R.id.et_change_phone);


        et_change_phone.addTextChangedListener(new TextWatcher() {
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
                .addAction(new TitleBar.Action(2, 0, R.color.blue_light),SettingChangePhoneSecondActivity.this.getResources().getString(R.string.setting_change_phone_next),R.drawable.shape_center_gray)
                .setCenterText(getResources().getString(R.string.setting_change_phone_second_title))
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

//                Toast.makeText(SettingChangePhoneSecondActivity.this,"////************",Toast.LENGTH_SHORT).show();
                Intent i_third = new Intent(SettingChangePhoneSecondActivity.this,SettingChangePhoneThirdActivity.class);
                startActivity(i_third);

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
