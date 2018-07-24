package com.haidehui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;


/**
 * 我的 --- 我的信息（修改自我介绍）
 */
public class MyInfoForIntroduceMyselfActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_introduce_myself;
    private Button btn_save;
    private String introduceMyself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_info_introduce_myself);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back).setCenterText(getResources().getString(R.string.title_my_info))
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
        et_introduce_myself = (EditText) findViewById(R.id.et_introduce_myself);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
    }

    private void initData() {
        introduceMyself = getIntent().getStringExtra("selfInfo");
        et_introduce_myself.setText(introduceMyself);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: // 保存
                String introduceMyself = et_introduce_myself.getText().toString();
                // “自我介绍”字段，不录入信息也可以保存
                saveData(introduceMyself);
//                if (!TextUtils.isEmpty(introduceMyself)) {
//                } else {
//                    Toast.makeText(mContext, "请输入自我介绍的内容", Toast.LENGTH_LONG).show();
//                }
                break;
        }
    }

    /**
     * 保存自我介绍的内容
     */
    private void saveData(final String introduceMyself) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("selfInfo", introduceMyself);
        param.put("userId", userId);
        param.put("saveField", "selfInfo");

        HtmlRequest.saveUserInfos(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                if ("true".equals(data.getFlag())) {

//                    if (!TextUtils.isEmpty(nameStr)) {
//                        try {
//                            PreferenceUtil.setUserRealName(DESUtil.encrypt(nameStr));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    intent.putExtra("introduceMyself", introduceMyself);
                    setResult(3001, intent);
                    finish();
                }
            }
        });
    }


}
