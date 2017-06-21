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
 *  提交客户信息
 */
public class SubmitCustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_name;
    private EditText edt_phone;
    private EditText edt_email;
    private EditText edt_location;
    private EditText edt_project;
    private EditText edt_room_number;
    private EditText edt_area;
    private EditText edt_total_amount;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_submit_info);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_submit_info))
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
        edt_name= (EditText) findViewById(R.id.edt_name);
        edt_phone= (EditText) findViewById(R.id.edt_phone);
        edt_email= (EditText) findViewById(R.id.edt_email);
        edt_location= (EditText) findViewById(R.id.edt_location);
        edt_project= (EditText) findViewById(R.id.edt_project);
        edt_room_number= (EditText) findViewById(R.id.edt_room_number);
        edt_area= (EditText) findViewById(R.id.edt_area);
        edt_total_amount= (EditText) findViewById(R.id.edt_total_amount);
        btn_save= (Button) findViewById(R.id.btn_save);

    }
    private void initData() {
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String customerName=edt_name.getText().toString();
                String customerPhone=edt_phone.getText().toString();
                String customerEmail=edt_email.getText().toString();
                String houseLocation=edt_location.getText().toString();
                String houseProject=edt_project.getText().toString();
                String roomNumber=edt_room_number.getText().toString();
                String area=edt_area.getText().toString();
                String totalPrice=edt_total_amount.getText().toString();

                if (!TextUtils.isEmpty(customerName)){
                    if (!TextUtils.isEmpty(customerPhone)){
                        if (StringUtil.isMobileNO(customerPhone)){
                            if (!TextUtils.isEmpty(customerEmail)){
                                if (!TextUtils.isEmpty(houseLocation)){
                                    if (!TextUtils.isEmpty(houseProject)){
                                        if (!TextUtils.isEmpty(roomNumber)){
                                            if (!TextUtils.isEmpty(area)){
                                                if (!TextUtils.isEmpty(totalPrice)){

                                                    requestData(area, customerEmail, customerName, customerPhone, houseLocation, houseProject, roomNumber, totalPrice);
                                                }else{
                                                    Toast.makeText(mContext, "请输入房产总价", Toast.LENGTH_LONG).show();
                                                    edt_total_amount.requestFocusFromTouch();
                                                }

                                            }else{
                                                Toast.makeText(mContext, "请输入房产面积", Toast.LENGTH_LONG).show();
                                                edt_area.requestFocusFromTouch();
                                            }

                                        }else{
                                            Toast.makeText(mContext, "请输入房产房号", Toast.LENGTH_LONG).show();
                                            edt_room_number.requestFocusFromTouch();
                                        }

                                    }else{
                                        Toast.makeText(mContext, "请输入项目名称", Toast.LENGTH_LONG).show();
                                        edt_project.requestFocusFromTouch();
                                    }

                                }else{
                                    Toast.makeText(mContext, "请输入房产所在国家", Toast.LENGTH_LONG).show();
                                    edt_location.requestFocusFromTouch();
                                }

                            }else{
                                Toast.makeText(mContext, "请输入邮箱地址", Toast.LENGTH_LONG).show();
                                edt_email.requestFocusFromTouch();
                            }

                        }else{
                            Toast.makeText(mContext, "请输入正确的联系电话", Toast.LENGTH_LONG).show();
                            edt_phone.requestFocusFromTouch();
                        }

                    }else{
                        Toast.makeText(mContext, "请输入联系电话", Toast.LENGTH_LONG).show();
                        edt_phone.requestFocusFromTouch();
                    }

                }else{
                    Toast.makeText(mContext, "请输入客户姓名", Toast.LENGTH_LONG).show();
                    edt_name.requestFocusFromTouch();
                }


                break;
        }
    }

    /**
     * 提交客户信息
     */
    private void requestData(String area,String customerEmail,String customerName,String customerPhone,
                             String houseLocation,String houseProject,String roomNumber,String totalPrice) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("area", area);
        param.put("customerEmail", customerEmail);
        param.put("customerName", customerName);
        param.put("customerPhone", customerPhone);
        param.put("houseLocation", houseLocation);
        param.put("houseProject", houseProject);
        param.put("roomNumber", roomNumber);
        param.put("totalPrice", totalPrice);
        param.put("userId", "17021318005814472279");

        HtmlRequest.getAddCustomerInFo(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                        if ("true".equals(data.getFlag())){
                            Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(SubmitCustomerInfoActivity.this,CustomerInfoActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
        );
    }

}
