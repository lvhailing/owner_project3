package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.CustomerTrackingAdapter;
import com.haidehui.adapter.HotHouseAdapter;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.HotHouse2B;
import com.haidehui.model.HotHouse3B;
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
import java.util.Map;


/**
 * 我的-- 客户跟踪列表
 */
public class CustomerTrackingActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private CustomerTrackingAdapter mAdapter;
    private MouldList<Tracking2B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private static int mDelId = 0;
    private String customerId;
    private String customerTrackingId;
    private ViewSwitcher vs;
    private TextView tv_empty;
    private ImageView img_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_customer_tracking);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).
                setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_customer_follow)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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
        vs = (ViewSwitcher) findViewById(R.id.vs);
        tv_empty= (TextView) findViewById(R.id.tv_empty);
        img_empty= (ImageView) findViewById(R.id.img_empty);
        tv_empty.setText("暂无客户跟踪信息");
        img_empty.setBackgroundResource(R.mipmap.empty_tracking);

        listView = (PullToRefreshListView) findViewById(R.id.listview);

        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
    }

    private void initData() {
        mAdapter = new CustomerTrackingAdapter(mContext, totalList);
        listView.setAdapter(mAdapter);

        requestListData();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(mContext, CustomerFollowDetailsActivity.class);
                intent.putExtra("customerId", totalList.get(position - 1).getCustomerId());
                intent.putExtra("customerTrackingId", totalList.get(position - 1).getCustomerTrackingId());
                startActivityForResult(intent,1000);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mDelId = position - 1;
                customerId = totalList.get(position - 1).getCustomerId();
                customerTrackingId = totalList.get(position - 1).getCustomerTrackingId();
                showDialog();
                return true;
            }
        });
    }
    protected void showDialog() {
        BasicDialog dialog=new BasicDialog(mContext, new BasicDialog.OnBasicChanged() {
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
            mAdapter.notifyDataSetChanged();

            deleteData(customerTrackingId);
            if (totalList.size()==0){
                vs.setDisplayedChild(1);
            }
        }
    }

    private void deleteData(String customerTrackingId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("customerTrackingId", customerTrackingId);
        param.put("userId", userId);
        HtmlRequest.deleteCustomerTracking(this, param, new BaseRequester.OnRequestListener() {
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

    }

    /**
     * 请求列表数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("userId", userId);

        try {
            HtmlRequest.getTrackingList(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        vs.setDisplayedChild(1);
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
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
                    if (totalList.size() == 0) {
                        vs.setDisplayedChild(1);
                    } else {
                        vs.setDisplayedChild(0);
                    }
                    //刷新数据
                    mAdapter.notifyDataSetChanged();

                    listView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.onRefreshComplete();
                        }
                    }, 1000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode==1000) {
            currentPage=1;
            requestListData(); //重新请求数据
            listView.getRefreshableView().setSelection(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
