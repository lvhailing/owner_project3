package com.haidehui.act;

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
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;


/**
 *  跟踪详情
 */
public class CustomerFollowDetailsActivity extends BaseActivity implements View.OnClickListener {
    private MyListView lv_follow_detail;
    private CustomerFollowDetailsAdapter adapter;
    private MouldList<ResultCustomerFollowDetailslistBean> detailsList=new MouldList<>();
    private ScrollView scrollview;
    private String customerId;
    private String customerTrackingId;

    private TextView tv_customerName;
    private TextView tv_customerPhone;
    private EditText edt_project;
    private EditText edt_room_number;
    private EditText edit_remark;
    private Button btn_save;

    private String[] checkStr=new String[12];
    private String[] resultStr=new String[12];
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_customer_follow_details);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_customer_follow_details))
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
        tv_customerName= (TextView) findViewById(R.id.tv_customerName);
        tv_customerPhone= (TextView) findViewById(R.id.tv_customerPhone);
        edt_project= (EditText) findViewById(R.id.edt_project);
        edt_room_number= (EditText) findViewById(R.id.edt_room_number);
        edit_remark= (EditText) findViewById(R.id.edit_remark);
        btn_save= (Button) findViewById(R.id.btn_save);
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        lv_follow_detail = (MyListView) findViewById(R.id.lv_follow_detail);
        lv_follow_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }
        });
    }
    private void initData() {
        btn_save.setOnClickListener(this);
        customerId=getIntent().getStringExtra("customerId");
        customerTrackingId=getIntent().getStringExtra("customerTrackingId");
        requestData();
    }

    private void requestData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerTrackingId", customerTrackingId);
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
                        setData(data);
                    }
                }
        );
    }

    private void setData(TrackingDetails2B data) {
        tv_customerName.setText(data.getCustomerName());
        tv_customerPhone.setText(data.getCustomerPhone());
        edt_project.setText(data.getHouseProject());
        edt_room_number.setText(data.getRoomNumber());
        edit_remark.setText(data.getTrackingRemark());

        if ("true".equals(data.getTelephoneContactedAndIntroductionProject())) {
            checkStr[0] = "true";
        }else{
            checkStr[0] = "false";
        }
        if ("true".equals(data.getProjectMaterialsAndAnsweredQuestion())) {
            checkStr[1] = "true";
        }else{
            checkStr[1] = "false";
        }
        if ("true".equals(data.getInterviewedAndDetailedIntroductionProject())) {
            checkStr[2] = "true";
        }else{
            checkStr[2] = "false";
        }
        if ("true".equals(data.getProductPromotionAndDeepUnderstanding())) {
            checkStr[3] = "true";
        }else{
            checkStr[3] = "false";
        }
        if ("true".equals(data.getRevisitCustomerAndAnsweredQuestion())) {
            checkStr[4] = "true";
        }else{
            checkStr[4] = "false";
        }
        if ("true".equals(data.getConfirmCondoTourPlan())) {
            checkStr[5] = "true";
        }else{
            checkStr[5] = "false";
        }
        if ("true".equals(data.getFullPaymentFrontmoneyAndSelectedRoomNum())) {
            checkStr[6] = "true";
        }else{
            checkStr[6] = "false";
        }
        if ("true".equals(data.getOfficalSignature())) {
            checkStr[7] = "true";
        }else{
            checkStr[7] = "false";
        }
        if ("true".equals(data.getFullPaymentDownpayment())) {
            checkStr[8] = "true";
        }else{
            checkStr[8] = "false";
        }
        if ("true".equals(data.getRecommendOtherCustomers())) {
            checkStr[9] = "true";
        }else{
            checkStr[9] = "false";
        }
        if ("true".equals(data.getContactedOtherCustomers())) {
            checkStr[10] = "true";
        }else{
            checkStr[10] = "false";
        }
        if ("true".equals(data.getRecommendOtherCustomersSuccess())) {
            checkStr[11] = "true";
        }else{
            checkStr[11] = "false";
        }
        resultStr=checkStr;
        put(checkStr);
    }
    private void put(String[] checkStr){
        String[] detailsStr = new String[]{
                "已电话联络客户，简单介绍项目",
                "已发项目资料，回答客户疑问",
                "已和客户面谈，详细介绍项目",
                "客户已参加公司推介会，深度了解项目",
                "再次回访客户，回答客户疑问",
                "客户确定赴实地看房团行程",
                "客户已全额支付购房定金，选好房号",
                "客户已正式签署合同",
                "客户已全额支付购买房屋首付款",
                "签约成功客户转介绍客户",
                "已和转介绍客户联络",
                "转介绍客户成功"
        };
        for (int i=0 ;i<detailsStr.length;i++ ){
            ResultCustomerFollowDetailslistBean bean=new ResultCustomerFollowDetailslistBean();
            bean.setDetails(detailsStr[i]);
            bean.setIsChecked(checkStr[i]);
            detailsList.add(bean);
        }
        adapter = new CustomerFollowDetailsAdapter(CustomerFollowDetailsActivity.this, detailsList, new CustomerFollowDetailsAdapter.OnEditListener() {
            @Override
            public void onCheckBox(int position, boolean isChecked) {
                if (isChecked) {
                    resultStr[position]="true";
                } else {
                    resultStr[position]="false";
                }
            }
        });
        lv_follow_detail.setAdapter(adapter);
        setListViewHeightBasedOnChildren(CustomerFollowDetailsActivity.this, lv_follow_detail, 0);
//        scrollview.smoothScrollTo(0, 0);
        scrollview.post(new Runnable() {
            @Override
            public void run() {

                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    /**
     * 新增客户跟踪
     */
    private void submitData(String[] resultStr,String houseProject,String roomNumber,String trackingRemark) {
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
                        if ("true".equals(data.getFlag())){
                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(mContext,CustomerTrackingActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
        );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                String houseProject=edt_project.getText().toString();
                String roomNumber=edt_room_number.getText().toString();
                String remark=edit_remark.getText().toString();
                int m=0;
                if (!TextUtils.isEmpty(houseProject)){
                    if (!TextUtils.isEmpty(roomNumber)){
                        for (int i=0;i<resultStr.length;i++){
                            if (resultStr[i].equals("false")){
                                m+=1;
                            }
                        }
                        if (m==12){
                            Toast.makeText(mContext, "请选择跟踪进度", Toast.LENGTH_LONG).show();
                        }else{
                            submitData(resultStr,houseProject,roomNumber,remark);
                        }

                    }else{
                        Toast.makeText(mContext, "请输入房产房号", Toast.LENGTH_LONG).show();
                        edt_room_number.requestFocusFromTouch();
                    }
                }else{
                    Toast.makeText(mContext, "请输入项目名称", Toast.LENGTH_LONG).show();
                    edt_project.requestFocusFromTouch();
                }
                break;
        }
    }
    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(Context context,ListView listView,int dividerHeight) {
        if(listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight()+dividerHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * listAdapter.getCount())+5;

        listView.setLayoutParams(params);
    }

}
