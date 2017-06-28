package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.CustomerInfoAdapter;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.CustomerInfo2B;
import com.haidehui.model.CustomerInfo3B;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.EditCustomerInfoDialog;
import com.haidehui.widget.SelectPhotoDialog;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.haidehui.uitls.ActivityStack;


/**
 *  我的 --- 客户信息
 */
public class CustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_customer_info;
    private CustomerInfoAdapter adapter;
    private MouldList<CustomerInfo3B> totalList=new MouldList<>();
    private int currentPage = 1;    //当前页
    private Button btn_submit;
    private static int mDelId = 0;
    private String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_cutomer_info);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_customer_info))
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
        ActivityStack stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        btn_submit= (Button) findViewById(R.id.btn_submit);

        lv_customer_info = (PullToRefreshListView) findViewById(R.id.lv_customer_info);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv_customer_info);

        lv_customer_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(CustomerInfoActivity.this, CustomerDetailsActivity.class);
                intent.putExtra("customerId", totalList.get(position - 1).getCustomerId());
                startActivity(intent);
            }
        });
        lv_customer_info.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                EditCustomerInfoDialog mDialog = new EditCustomerInfoDialog(mContext,new EditCustomerInfoDialog.OnSelectPhotoChanged() {
                    @Override
                    public void onDelete() {
                        mDelId = position - 1;
                        customerId=totalList.get(position-1).getCustomerId();
                        showDialog();
                    }

                    @Override
                    public void onEdit() {
                        customerId=totalList.get(position-1).getCustomerId();
                        Intent intent=new Intent(mContext,EditCustomerInfoActivity.class);
                        intent.putExtra("customerId",customerId);
                        startActivity(intent);
                    }

                });
                mDialog.show();
                return true;

            }
        });
        lv_customer_info.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestListData();
            }
        });

    }
    private void initData() {
        btn_submit.setOnClickListener(this);
        adapter = new CustomerInfoAdapter(CustomerInfoActivity.this, totalList);
        lv_customer_info.setAdapter(adapter);
        requestListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void showDialog() {
        BasicDialog dialog=new BasicDialog(this, new BasicDialog.OnBasicChanged() {
            @Override
            public void onConfim() {
                deleteItem();
            }

            @Override
            public void onCancel() {
            }
        },"此操作将同时删除该客户相应的跟踪信息","确认");
        dialog.show();
    }
    /**
     * 删除Item
     */
    private void deleteItem() {
        int size = totalList.size();
        if (size > 0) {
            totalList.remove(mDelId);
            adapter.notifyDataSetChanged();

            deleteData(customerId);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode==1000) {
 //           requestList(); 重新请求数据
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("userId", userId);

        try {
            HtmlRequest.getCustomerInfoList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        lv_customer_info.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv_customer_info.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    CustomerInfo2B data = (CustomerInfo2B) params.result;
                    MouldList<CustomerInfo3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已经到最后一页", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        totalList.clear();
                    }
                    totalList.addAll(everyList);

                    //刷新数据
                    adapter.notifyDataSetChanged();

                    lv_customer_info.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lv_customer_info.onRefreshComplete();
                        }
                    }, 1000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteData(String customerId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        param.put("userId", userId);
        HtmlRequest.deleteCustomerInFo(this, param, new BaseRequester.OnRequestListener() {
                    @Override
                    public void onRequestFinished(BaseParams params) {
                        if (params.result == null) {
                            Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                        if ("true".equals(data.getFlag())) {
                            Toast.makeText(mContext, data.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                Intent intent = new Intent(this, SubmitCustomerInfoActivity.class);
                startActivityForResult(intent, 1000);
                break;
        }
    }



}
