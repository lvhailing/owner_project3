package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.CustomerInfoAdapter;
import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 *  我的 --- 客户信息
 */
public class CustomerInfoActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_customer_info;
    private CustomerInfoAdapter adapter;
    private MouldList<ResultCustomerInfolistBean> infoList;
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
        infoList = new MouldList<ResultCustomerInfolistBean>();
        ResultCustomerInfolistBean bean1=new ResultCustomerInfolistBean();
        bean1.setName("某某某1");
        bean1.setPhone("1234567890");

        ResultCustomerInfolistBean bean2=new ResultCustomerInfolistBean();
        bean2.setName("某某某2");
        bean2.setPhone("1234567890");

        ResultCustomerInfolistBean bean3=new ResultCustomerInfolistBean();
        bean3.setName("某某某3");
        bean3.setPhone("1234567890");

        ResultCustomerInfolistBean bean4=new ResultCustomerInfolistBean();
        bean4.setName("某某某4");
        bean4.setPhone("1234567890");

        ResultCustomerInfolistBean bean5=new ResultCustomerInfolistBean();
        bean5.setName("某某某5");
        bean5.setPhone("1234567890");

        infoList.add(bean1);
        infoList.add(bean2);
        infoList.add(bean3);
        infoList.add(bean4);
        infoList.add(bean5);

        lv_customer_info = (PullToRefreshListView) findViewById(R.id.lv_customer_info);
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
                Toast.makeText(CustomerInfoActivity.this,"长按啦啦啦", Toast.LENGTH_LONG).show();
                mDelId = position - 1;
                showDialog();
                return true;
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
        int size = infoList.size();
        if (size > 0) {
            infoList.remove(mDelId);
            adapter.notifyDataSetChanged();
        }
    }
    private void initData() {
        btn_submit.setOnClickListener(this);
        adapter = new CustomerInfoAdapter(CustomerInfoActivity.this, infoList);
        lv_customer_info.setAdapter(adapter);
//        requestList();

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
     * 请求信息列表
     *//*
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
            case R.id.btn_submit:
                Intent intent = new Intent(this, SubmitCustomerInfoActivity.class);
                startActivityForResult(intent, 1000);
                break;
        }
    }


    /* private void requestListData() {  // 获取最热房源列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("currentPage", currentPage + "");

        try {
            HtmlRequest.getHotHouseData(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    HotHouse2B data = (HotHouse2B) params.result;
                    MouldList<HotHouse3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已经到最后一页", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        totalList.clear();
                    }
                    totalList.addAll(everyList);

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
    }*/



}
