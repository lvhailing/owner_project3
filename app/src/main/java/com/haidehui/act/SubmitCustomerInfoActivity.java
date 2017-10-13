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
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;

import android.text.TextUtils;

/**
 * 提交客户信息
 */
public class SubmitCustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name; // 客户姓名
    private EditText et_phone; // 电话
    private EditText et_email; // 邮箱地址
    private EditText et_location; // 房产所在国家
    private EditText et_project; // 项目名称
    private EditText et_room_number; // 房产房号
    private EditText et_area; // 房产面积
    private EditText et_total_amount; // 房产总价
    private Button btn_submit; // 立即提交

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_submit_info);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
             .setCenterText(getResources().getString(R.string.title_submit_info)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {
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
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        et_location = (EditText) findViewById(R.id.et_location);
        et_project = (EditText) findViewById(R.id.et_project);
        et_room_number = (EditText) findViewById(R.id.et_room_number);
        et_area = (EditText) findViewById(R.id.et_area);
        et_total_amount = (EditText) findViewById(R.id.et_total_amount);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:  // 立即提交
                String customerName = et_name.getText().toString();
                String customerPhone = et_phone.getText().toString();
                String customerEmail = et_email.getText().toString();
                String houseLocation = et_location.getText().toString();
                String houseProject = et_project.getText().toString();
                String roomNumber = et_room_number.getText().toString();
                String area = et_area.getText().toString();
                String totalPrice = et_total_amount.getText().toString();

                if (!TextUtils.isEmpty(customerName)) {
                    if (!TextUtils.isEmpty(customerPhone)) {
                        if (StringUtil.isMobileNO(customerPhone)) {
                            if (!TextUtils.isEmpty(customerEmail)) {
                                if (StringUtil.isEmail(customerEmail)) {
                                    requestData(area, customerEmail, customerName, customerPhone, houseLocation, houseProject, roomNumber, totalPrice);
                                } else {
                                    Toast.makeText(mContext, "请输入正确的邮箱地址", Toast.LENGTH_LONG).show();
                                    et_email.requestFocusFromTouch();
                                }
                            } else {
                                Toast.makeText(mContext, "请输入邮箱地址", Toast.LENGTH_LONG).show();
                                et_email.requestFocusFromTouch();
                            }
                        } else {
                            Toast.makeText(mContext, "请输入正确的联系电话", Toast.LENGTH_LONG).show();
                            et_phone.requestFocusFromTouch();
                        }
                    } else {
                        Toast.makeText(mContext, "请输入联系电话", Toast.LENGTH_LONG).show();
                        et_phone.requestFocusFromTouch();
                    }
                } else {
                    Toast.makeText(mContext, "请输入客户姓名", Toast.LENGTH_LONG).show();
                    et_name.requestFocusFromTouch();
                }

                break;
        }
    }

    /**
     * 提交客户信息
     */
    private void requestData(String area, String customerEmail, String customerName, String customerPhone, String houseLocation, String houseProject, String roomNumber, String totalPrice) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("area", area);
        param.put("customerEmail", customerEmail);
        param.put("customerName", customerName);
        param.put("customerPhone", customerPhone);
        param.put("houseLocation", houseLocation);
        param.put("houseProject", houseProject);
        param.put("roomNumber", roomNumber);
        param.put("totalPrice", totalPrice);
        param.put("userId", userId);

        HtmlRequest.getAddCustomerInFo(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                if ("true".equals(data.getFlag())) {
                    Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SubmitCustomerInfoActivity.this, CustomerInfoActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
