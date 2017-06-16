package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.haidehui.R;
import com.haidehui.adapter.AddCustomerFollowAdapter;
import com.haidehui.adapter.CustomerFollowDetailsAdapter;
import com.haidehui.bean.ResultCustomerFollowDetailslistBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;


/**
 *  添加跟踪
 */
public class AddCustomerFollowActivity extends BaseActivity implements View.OnClickListener {
    private MyListView lv_follow_detail;
    private AddCustomerFollowAdapter adapter;
    private MouldList<ResultCustomerFollowDetailslistBean> detailsList;
    private ScrollView scrollview;
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
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_add_customer_follow))
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
        test();
        adapter = new AddCustomerFollowAdapter(AddCustomerFollowActivity.this, detailsList, new AddCustomerFollowAdapter.OnEditListener() {
            @Override
            public void onCheckBox(int position, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });
        lv_follow_detail.setAdapter(adapter);
        setListViewHeightBasedOnChildren(AddCustomerFollowActivity.this, lv_follow_detail, 0);
        scrollview.smoothScrollTo(0, 0);
//        requestAddressList();

    }
    private void test() {
        detailsList = new MouldList<ResultCustomerFollowDetailslistBean>();
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
        String[] checkStr = new String[]{
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no",
                "no"
        };
        for (int i=0 ;i<detailsStr.length;i++ ){
            ResultCustomerFollowDetailslistBean bean=new ResultCustomerFollowDetailslistBean();
            bean.setDetails(detailsStr[i]);
            bean.setIsChecked(checkStr[i]);
            detailsList.add(bean);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

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
