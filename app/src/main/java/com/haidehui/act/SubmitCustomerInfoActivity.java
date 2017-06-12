package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.haidehui.R;
import com.haidehui.adapter.CustomerInfoAdapter;
import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


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
/*
    private void requestList() {

        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HtmlRequest.getAddressList(AddressManageActivity.this, userId, token,
                new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result != null) {
                            ResultAddressManageContentBean data = (ResultAddressManageContentBean) params.result;
                            if (data.getAddressList().size() == 0) {
                                vs_inviterecord_switch.setDisplayedChild(1);
                            } else {
                                addressList.clear();
                                addressList.addAll(data.getAddressList());
                                adapter.notifyDataSetChanged();
                                lv_address_manage.getRefreshableView()
                                        .smoothScrollToPositionFromTop(0,
                                                80, 100);
                                lv_address_manage.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        lv_address_manage.onRefreshComplete();
                                    }
                                }, 1000);
                            }
                        } else {
                            vs_inviterecord_switch.setDisplayedChild(1);
                            Toast.makeText(AddressManageActivity.this, "加载失败，请确认网络通畅",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String name=edt_name.getText().toString();
                String phone=edt_phone.getText().toString();
                String email=edt_email.getText().toString();
                String location=edt_location.getText().toString();
                String project=edt_project.getText().toString();
                String room_number=edt_room_number.getText().toString();
                String area=edt_area.getText().toString();
                String total_amount=edt_total_amount.getText().toString();

                Intent intent=new Intent(this,CustomerInfoActivity.class);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

}
