package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.ExplainOrderAdapter;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.ExplainOrder2B;
import com.haidehui.model.ExplainOrder3B;
import com.haidehui.model.SubmitCustomer2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.EditCustomerInfoDialog;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * （我的模块）预约说明会
 */
public class ExplainOrderListActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_explain_order;
    private ExplainOrderAdapter adapter;
    private MouldList<ExplainOrder3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private Button btn_add_customers; // 新增客户 按钮
    private static int mDelId = 0;
    private String itemId;
    private ViewSwitcher vs;
    private TextView tv_empty;
    private ImageView img_empty;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_explain_order);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
             .setIndicator(R.drawable.back).setCenterText(getResources().getString(R.string.title_explain_order))
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

        vs = (ViewSwitcher) findViewById(R.id.vs);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        img_empty = (ImageView) findViewById(R.id.img_empty);
        tv_empty.setText("暂无参会客户");
        img_empty.setBackgroundResource(R.mipmap.empty_customerinfo);

        btn_add_customers = (Button) findViewById(R.id.btn_add_customers);
        lv_explain_order = (PullToRefreshListView) findViewById(R.id.lv_explain_order);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv_explain_order);

        btn_add_customers.setOnClickListener(this);

//        lv_explain_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//                Intent intent = new Intent(ExplainOrderListActivity.this, CustomerDetailsActivity.class);
//                intent.putExtra("userId", totalList.get(position - 1).getCustomerId());
//                startActivity(intent);
//            }
//        });
        /**
         *  item 长按监听   修改或删除
         */
        lv_explain_order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                EditCustomerInfoDialog mDialog = new EditCustomerInfoDialog(mContext, new EditCustomerInfoDialog.OnSelectPhotoChanged() {
                    @Override
                    public void onDelete() {  // 删除
                        mDelId = position - 1;
                        itemId = totalList.get(position - 1).getId();
                        showDialog();
                    }
                    @Override
                    public void onEdit() {   // 修改
                        itemId = totalList.get(position - 1).getId();
                        Intent intent = new Intent(mContext, EditExplainOrderInfoActivity.class);
                        intent.putExtra("id", itemId);
                        intent.putExtra("customerName", totalList.get(position - 1).getCustomerName());
                        intent.putExtra("customerPhone", totalList.get(position - 1).getCustomerPhone());
                        intent.putExtra("meetingTime", totalList.get(position - 1).getMeetingTime());
                        startActivityForResult(intent,1000);
                    }
                });
                mDialog.show();
                return true;

            }
        });

        lv_explain_order.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
        adapter = new ExplainOrderAdapter(ExplainOrderListActivity.this, totalList);
        lv_explain_order.setAdapter(adapter);
        requestListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void showDialog() {
        BasicDialog dialog = new BasicDialog(this, new BasicDialog.OnBasicChanged() {
            @Override
            public void onConfim() {
                deleteItem();
            }

            @Override
            public void onCancel() {
            }
        }, "你确认要删除该客户的信息吗？", "确认");
        dialog.show();
    }

    /**
     * 长按删除Item
     */
    private void deleteItem() {
        int size = totalList.size();
        if (size > 0) {
            totalList.remove(mDelId);
            adapter.notifyDataSetChanged();

            deleteData(userId,itemId);
            if (totalList.size() == 0) {
                vs.setDisplayedChild(1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1000) {
            currentPage = 1;
            requestListData(); //重新请求数据
            lv_explain_order.getRefreshableView().setSelection(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求预约客户列表数据
     */
    private void requestListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("page", currentPage + "");

        try {
            HtmlRequest.getExplainOrderData(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        vs.setDisplayedChild(1);
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        lv_explain_order.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv_explain_order.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }
                    ExplainOrder2B data = (ExplainOrder2B) params.result;
                    MouldList<ExplainOrder3B> everyList = data.getList();

                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
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
                    adapter.notifyDataSetChanged();

                    lv_explain_order.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lv_explain_order.onRefreshComplete();
                        }
                    }, 1000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  长按删除 客户信息
     * @param userId
     */
    private void deleteData(String userId,String customerAppointmentId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("customerAppointmentId", itemId);
        HtmlRequest.deleteExplainOrderListItem(this, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                SubmitCustomer2B data = (SubmitCustomer2B) params.result;
                if ("!true".equals(data.getFlag())) {
                    Toast.makeText(mContext, data.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_customers: // 新增客户
                Intent intent = new Intent(this, AddCustomerActivity.class);
                    startActivityForResult(intent, 1000);
//                if (status != null && !TextUtils.isEmpty(status) && status.equals("success")) {
//                    Intent intent = new Intent(this, SubmitCustomerInfoActivity.class);
//                    startActivityForResult(intent, 1000);
//                } else {
//                    Toast.makeText(mContext, "请您通过事业合伙人认证后再进行相关操作!", Toast.LENGTH_SHORT).show();
//                }

                break;
        }
    }


}
