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
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;


/**
 * 我的 --- 我的信息（修改工作单位）
 */
public class MyInfoForWorkUnitActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_work_unit;
    private Button btn_save;
    private String workUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_info_work_unit);

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
        et_work_unit = (EditText) findViewById(R.id.et_work_unit);
        btn_save = (Button) findViewById(R.id.btn_save);


        btn_save.setOnClickListener(this);
    }

    private void initData() {
        workUnit = getIntent().getStringExtra("workUnit");
        et_work_unit.setText(workUnit);
        et_work_unit.requestFocusFromTouch();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save: // 保存
                String workUnit = et_work_unit.getText().toString();
                if (!TextUtils.isEmpty(workUnit)) {
                    saveData(workUnit);
                } else {
                    Toast.makeText(mContext, "请输入工作单位", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * 保存工作单位
     */
    private void saveData(final String workUnit) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("workUnit", workUnit);
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

//                    if (!TextUtils.isEmpty(workUnit)) {
//                        try {
//                            PreferenceUtil.setUserRealName(DESUtil.encrypt(workUnit));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    intent.putExtra("workUnit", workUnit);
                    setResult(2001, intent);
                    finish();
                }
            }
        });
    }


}
