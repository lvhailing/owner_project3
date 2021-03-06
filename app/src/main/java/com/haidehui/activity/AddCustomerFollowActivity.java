package com.haidehui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.CustomerFollowDetailsAdapter;
import com.haidehui.bean.ResultCustomerFollowDetailslistBean;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.model.TrackingDetails2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;


/**
 * 添加跟踪
 */
public class AddCustomerFollowActivity extends BaseActivity implements View.OnClickListener {
    private String customerId;
    private MyListView lv_follow_detail;
    private CustomerFollowDetailsAdapter adapter;
    private MouldList<ResultCustomerFollowDetailslistBean> detailsList;
    private ScrollView scrollview;
    private Button btn_save; // 保存
    private TextView tv_customer_name; // 客户姓名
    private TextView tv_customer_phone; // 客户电话
    private EditText et_project; // 项目
    private EditText et_room_number; // 房号
    private EditText et_remark; // 备注信息
    private ActivityStack stack;

    private String[] resultStr = new String[12];

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_add_customer_follow);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
             .setCenterText(getResources().getString(R.string.title_add_customer_follow)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_customer_phone = (TextView) findViewById(R.id.tv_customer_phone);
        et_project = (EditText) findViewById(R.id.et_project);
        et_room_number = (EditText) findViewById(R.id.et_room_number);
        et_remark = (EditText) findViewById(R.id.et_remark);
        lv_follow_detail = (MyListView) findViewById(R.id.lv_follow_detail);

        lv_follow_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
        });
    }

    private void initData() {
        customerId = getIntent().getStringExtra("customerId");
        requestData();
        btn_save.setOnClickListener(this);
        put();
        adapter = new CustomerFollowDetailsAdapter(AddCustomerFollowActivity.this, detailsList, new CustomerFollowDetailsAdapter.OnEditListener() {
            @Override
            public void onCheckBox(int position, boolean isChecked) {
                if (isChecked) {
                    resultStr[position] = "true";
                } else {
                    resultStr[position] = "false";
                }
            }
        });
        lv_follow_detail.setAdapter(adapter);
        setListViewHeightBasedOnChildren(AddCustomerFollowActivity.this, lv_follow_detail, 0);
        scrollview.smoothScrollTo(0, 0);

    }

    private void put() {
        detailsList = new MouldList<ResultCustomerFollowDetailslistBean>();
        String[] detailsStr = new String[]{"已电话联络客户，简单介绍项目", "已发项目资料，回答客户疑问", "已和客户面谈，详细介绍项目", "客户已参加公司推介会，深度了解项目", "再次回访客户，回答客户疑问", "客户确定赴实地看房团行程", "客户已全额支付购房定金，选好房号", "客户已正式签署合同", "客户已全额支付购买房屋首付款", "签约成功客户转介绍客户", "已和转介绍客户联络", "转介绍客户成功"};
        String[] checkStr = new String[]{"false", "false", "false", "false", "false", "false", "false", "false", "false", "false", "false", "false"};
        resultStr = checkStr;
        for (int i = 0; i < detailsStr.length; i++) {
            ResultCustomerFollowDetailslistBean bean = new ResultCustomerFollowDetailslistBean();
            bean.setDetails(detailsStr[i]);
            bean.setIsChecked(checkStr[i]);
            detailsList.add(bean);
        }
    }

    /**
     * 新增客户跟踪
     */
    private void submitData(String[] resultStr, String houseProject, String roomNumber, String trackingRemark) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        param.put("userId", userId);
        param.put("houseProject", houseProject);
        param.put("roomNumber", roomNumber);
        param.put("trackingRemark", trackingRemark);
        param.put("telephoneContactedAndIntroductionProject", resultStr[0]);
        param.put("projectMaterialsAndAnsweredQuestion", resultStr[1]);
        param.put("interviewedAndDetailedIntroductionProject", resultStr[2]);
        param.put("productPromotionAndDeepUnderstanding", resultStr[3]);
        param.put("revisitCustomerAndAnsweredQuestion", resultStr[4]);
        param.put("confirmCondoTourPlan", resultStr[5]);
        param.put("fullPaymentFrontmoneyAndSelectedRoomNum", resultStr[6]);
        param.put("officalSignature", resultStr[7]);
        param.put("fullPaymentDownpayment", resultStr[8]);
        param.put("recommendOtherCustomers", resultStr[9]);
        param.put("contactedOtherCustomers", resultStr[10]);
        param.put("recommendOtherCustomersSuccess", resultStr[11]);

        HtmlRequest.submitTracking(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                if ("true".equals(data.getFlag())) {
                    stack.removeAllActivityExceptOne("com.haidehui.act.MainActivity");
                    Intent intent = new Intent(mContext, CustomerTrackingActivity.class);
                    startActivity(intent);
                    Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String houseProject = et_project.getText().toString();
                String roomNumber = et_room_number.getText().toString();
                String remark = et_remark.getText().toString();
                int m = 0;
                if (!TextUtils.isEmpty(houseProject)) {
                    if (!TextUtils.isEmpty(roomNumber)) {
                        for (int i = 0; i < resultStr.length; i++) {
                            if (resultStr[i].equals("false")) {
                                m += 1;
                            }
                        }
                        if (m == 12) {
                            Toast.makeText(mContext, "请选择跟踪进度", Toast.LENGTH_LONG).show();
                        } else {
                            submitData(resultStr, houseProject, roomNumber, remark);
                        }
                    } else {
                        Toast.makeText(mContext, "请输入房产房号", Toast.LENGTH_LONG).show();
                        et_room_number.requestFocusFromTouch();
                    }
                } else {
                    Toast.makeText(mContext, "请输入项目名称", Toast.LENGTH_LONG).show();
                    et_project.requestFocusFromTouch();
                }
                break;
        }
    }

    private void requestData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        param.put("userId", userId);
        HtmlRequest.getTrackingDetails(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                TrackingDetails2B data = (TrackingDetails2B) params.result;
                tv_customer_name.setText(data.getCustomerName());
                tv_customer_phone.setText(data.getCustomerPhone());

            }
        });
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(Context context, ListView listView, int dividerHeight) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight() + dividerHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount()) + 5;

        listView.setLayoutParams(params);
    }

}
