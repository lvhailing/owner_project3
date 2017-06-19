package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.AccountBookCommissionAdapter;
import com.haidehui.adapter.CustomerInfoAdapter;
import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.AccountBookCommission2B;
import com.haidehui.model.CustomerInfo2B;
import com.haidehui.model.HotHouse2B;
import com.haidehui.model.HotHouse3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *  我的 --- 客户信息
 */
public class CustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_customer_info;
    private CustomerInfoAdapter adapter;
    private MouldList<ResultCustomerInfolistBean> totalList;
    private int currentPage = 1;    //当前页
    private Button btn_submit;
    private static int mDelId = 0;
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
        btn_submit= (Button) findViewById(R.id.btn_submit);
        totalList = new MouldList<ResultCustomerInfolistBean>();


        lv_customer_info = (PullToRefreshListView) findViewById(R.id.lv_customer_info);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv_customer_info);

        lv_customer_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(CustomerInfoActivity.this, CustomerDetailsActivity.class);
                //              intent.putExtra("id", productBeanlist.get(position - 1).getId());
                startActivity(intent);
            }
        });
        lv_customer_info.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CustomerInfoActivity.this, "长按啦啦啦", Toast.LENGTH_LONG).show();
                mDelId = position - 1;
                showDialog();
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
    protected void showDialog() {
        BasicDialog dialog=new BasicDialog(this, new BasicDialog.OnBasicChanged() {
            @Override
            public void onConfim() {
                deleteItem();
            }

            @Override
            public void onCancel() {
            }
        },"确认删除吗？","确认");
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
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode==1000) {
 //           requestList(); 重新请求数据
            Toast.makeText(CustomerInfoActivity.this, "呦呦呦",
                    Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", "17021511395798036131");
        param.put("page", currentPage + "");

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
                    MouldList<ResultCustomerInfolistBean> everyList = data.getList();
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
