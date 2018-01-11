package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.model.CustomerDetails2B;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;

/**
 *  修改客户信息
 */
public class EditCustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_name;
    private EditText edt_phone;
//    private EditText edt_email;
    private EditText edt_location;
    private EditText edt_project;
    private EditText edt_room_number;
    private EditText edt_area;
    private EditText edt_total_amount;
    private Button btn_submit;
    private CustomerDetails2B data;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_submit_info);

        initTopTitle();
        initView();
        initData();
        requestData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_edit_info))
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
        edt_name= (EditText) findViewById(R.id.et_name);
        edt_phone= (EditText) findViewById(R.id.et_phone);
//        edt_email= (EditText) findViewById(R.id.et_email);
        edt_location= (EditText) findViewById(R.id.et_location);
        edt_project= (EditText) findViewById(R.id.et_project);
        edt_room_number= (EditText) findViewById(R.id.et_room_number);
        edt_area= (EditText) findViewById(R.id.et_area);
        edt_total_amount= (EditText) findViewById(R.id.et_total_amount);
        btn_submit = (Button) findViewById(R.id.btn_submit);

    }
    private void initData() {
        customerId=getIntent().getStringExtra("customerId");
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                String houseLocation=edt_location.getText().toString();
                String houseProject=edt_project.getText().toString();
                String roomNumber=edt_room_number.getText().toString();
                String area=edt_area.getText().toString();
                String totalPrice=edt_total_amount.getText().toString();

                requestData(area,houseLocation, houseProject, roomNumber, totalPrice);

                break;
        }
    }
    private void requestData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        param.put("userId", userId);
        HtmlRequest.getCustomerInFoDetails(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        data = (CustomerDetails2B) params.result;
                        setData(data);
                    }
                }
        );
    }
    private void setData(CustomerDetails2B data) {
        edt_name.setText(data.getCustomerName());
        edt_phone.setText(data.getCustomerPhone());
//        edt_email.setText(data.getCustomerEmail());
        //以上不能编辑
        edt_name.setFocusable(false);
        edt_phone.setFocusable(false);
//        edt_email.setFocusable(false);

        edt_location.setText(data.getHouseLocation());
        edt_project.setText(data.getHouseProject());
        edt_room_number.setText(data.getRoomNumber());
        edt_area.setText(data.getArea());
        edt_total_amount.setText(data.getTotalPrice());
    }

    /**
     * 修改客户信息
     */
    private void requestData(String area,String houseLocation,String houseProject,String roomNumber,String totalPrice) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("area", area);
        param.put("houseLocation", houseLocation);
        param.put("houseProject", houseProject);
        param.put("roomNumber", roomNumber);
        param.put("totalPrice", totalPrice);
        param.put("userId", userId);
        param.put("customerId", customerId);

        HtmlRequest.getEditCustomerInFo(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                        if ("true".equals(data.getFlag())){
                            Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(EditCustomerInfoActivity.this,CustomerInfoActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

}
