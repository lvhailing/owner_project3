package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import android.text.TextUtils;


/**
 *  我的 --- 我的信息（修改姓名）
 */
public class MyInfoForNameActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_name;
    private Button btn_save;
    private String realName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_info_name);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_my_info))
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

    private void initView() {
        realName=getIntent().getStringExtra("realName");
        edt_name= (EditText) findViewById(R.id.edt_name);
        btn_save= (Button) findViewById(R.id.btn_save);

        edt_name.setText(realName);
        edt_name.requestFocusFromTouch();
    }

    private void initData() {
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String name=edt_name.getText().toString();
                if (!TextUtils.isEmpty(name)){
                    saveData(name);
                }else{
                    Toast.makeText(mContext, "请输入您的姓名", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    /**
     * 保存姓名
     */
    private void saveData(final String nameStr) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("realName", nameStr);
        param.put("userId", userId);

        HtmlRequest.saveName(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                        if ("true".equals(data.getFlag())) {

                            if(!TextUtils.isEmpty(nameStr)){
                                try {
                                    PreferenceUtil.setUserRealName(DESUtil.encrypt(nameStr));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent =new Intent(mContext,MyInfoActivity.class);
                            intent.putExtra("realName",nameStr);
                            setResult(2000,intent);
                            finish();
                        }
                    }
                }
        );
    }


}
