package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.CustomerFollowAdapter;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.model.Tracking2B;
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


/**
 *  我的 --- 客户跟踪
 */
public class CustomerFollowActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv;
    private CustomerFollowAdapter adapter;
    private MouldList<Tracking2B> totalList=new MouldList<>();
    private static int mDelId = 0;
    private int currentPage = 1;    //当前页
    private String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_cutomer_follow);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_customer_follow))
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

        lv = (PullToRefreshListView) findViewById(R.id.lv);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(CustomerFollowActivity.this, CustomerFollowDetailsActivity.class);
                intent.putExtra("customerId",totalList.get(position-1).getCustomerId());
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CustomerFollowActivity.this, "长按啦啦啦", Toast.LENGTH_LONG).show();
                mDelId = position - 1;
                customerId=totalList.get(position-1).getCustomerId();
                showDialog();
                return true;
            }
        });

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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

            deleteData(customerId);

        }
    }
    private void initData() {
        adapter = new CustomerFollowAdapter(CustomerFollowActivity.this, totalList);
        lv.setAdapter(adapter);

    }
    @Override
    protected void onResume() {
        super.onResume();
        requestListData();
    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("userId", "17021318005814472279");

        try {
            HtmlRequest.getTrackingList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        lv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    MouldList<Tracking2B> everyList = (MouldList<Tracking2B>) params.result;
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

                    lv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lv.onRefreshComplete();
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
        param.put("userId", "17021318005814472279");
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

        }
    }




}
