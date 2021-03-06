package com.haidehui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.dialog.DatePickDialog;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 预约说明会客户信息修改
 */
public class EditExplainOrderInfoActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name_customer; // 客户姓名
    private EditText et_phone; // 联系电话
    private RelativeLayout rl_date_participation; // 参加日期 布局
    private TextView tv_date; // 参加日期 展示
    private Button btn_save; // 保存

//    private long currentTime = System.currentTimeMillis();
    private String date;
    private String customerName;
    private String customerPhone;
    private String itemId;
    private String meetingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_save_customer);

        initTopTitle();
        initView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back).setCenterText(getResources().getString(R.string.title_edit_customer_info)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        itemId = getIntent().getStringExtra("id");
        customerName = getIntent().getStringExtra("customerName");
        customerPhone = getIntent().getStringExtra("customerPhone");
        meetingTime = getIntent().getStringExtra("meetingTime");

        et_name_customer = (EditText) findViewById(R.id.et_name_customer);
        et_phone = (EditText) findViewById(R.id.et_phone);
        rl_date_participation = (RelativeLayout) findViewById(R.id.rl_date_participation);
        tv_date = (TextView) findViewById(R.id.tv_date);
        btn_save = (Button) findViewById(R.id.btn_save);

        et_name_customer.setText(customerName);
        et_phone.setText(customerPhone);
        tv_date.setText(meetingTime);

        rl_date_participation.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 保存客户修改数据
     */
    private void requestData(String userId, String customerAppointmentId, String customerName, String customerPhone, String meetingTime) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("customerAppointmentId", itemId);
        param.put("customerName", customerName);
        param.put("customerPhone", customerPhone);
        param.put("meetingTime", date);

        HtmlRequest.getEditExplainOrderCustomerInFo(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                if ("true".equals(data.getFlag())) {
                    Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(EditExplainOrderInfoActivity.this, ExplainOrderListActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_date_participation: // 选择日期
                showDatePickerDialog();
                break;
            case R.id.btn_save: // 保存
                customerName = et_name_customer.getText().toString();
                customerPhone = et_phone.getText().toString();

                if (TextUtils.isEmpty(customerName)) {
                    Toast.makeText(mContext, "请输入客户姓名", Toast.LENGTH_LONG).show();
                    et_name_customer.requestFocusFromTouch();
                    return;
                }
                if (TextUtils.isEmpty(customerPhone)) {
                    Toast.makeText(mContext, "请输入联系电话", Toast.LENGTH_LONG).show();
                    et_name_customer.requestFocusFromTouch();
                    return;
                }
                if (TextUtils.isEmpty(meetingTime)) {
                    Toast.makeText(mContext, "请选择参加日期", Toast.LENGTH_LONG).show();
                    return;
                }
                requestData(userId, itemId, customerName, customerPhone, meetingTime);
                break;
        }
    }

    /**
     * 日期选择对话框
     */
    private void showDatePickerDialog() {
        DatePickDialog dialog = new DatePickDialog(this);
        dialog.setDateDialog(new DatePickDialog.MyCallback() {

            public void processTime(Dialog ad, String selectedDate) {
                //如2016年11月30日
                if (tv_date != null && isTimeValue(selectedDate)) {
                    //选择的是正确的时间
                    date = selectedDate.replace("年", "-").replace("月", "-").replace("日", "");
                    tv_date.setText(date);
                }
                ad.dismiss();
                ad = null;
            }
        });
    }

    private boolean isTimeValue(String selectedDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        try {
            Date selectDate = simpleDateFormat.parse(selectedDate);
            long currentTime = System.currentTimeMillis();
            if (currentTime - 24 * 60 * 60 * 1000 > selectDate.getTime()) {
                //选择的时间必须是从今天开始包含今天
                Toast.makeText(EditExplainOrderInfoActivity.this, "时间只能是今天或今天以后", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }
}
