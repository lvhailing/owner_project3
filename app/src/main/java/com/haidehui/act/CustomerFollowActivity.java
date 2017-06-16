package com.haidehui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.CustomerFollowAdapter;
import com.haidehui.bean.ResultCustomerFollowlistBean;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 *  我的 --- 客户跟踪
 */
public class CustomerFollowActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lv_customer_follow;
    private CustomerFollowAdapter adapter;
    private MouldList<ResultCustomerFollowlistBean> infoList;
    private static int mDelId = 0;
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
        infoList = new MouldList<ResultCustomerFollowlistBean>();
        for (int i=0;i<10;i++){
            ResultCustomerFollowlistBean bean=new ResultCustomerFollowlistBean();
            if (i%2==0){
                bean.setName("moumoumoumou111111111"+i);
                bean.setTime("2017-06-01 12:34");
            }else{
                bean.setName("moumoumoumou222222222"+i);
                bean.setTime("2017-06-01 11:22");
            }
            infoList.add(bean);
        }


        lv_customer_follow = (PullToRefreshListView) findViewById(R.id.lv_customer_follow);
        lv_customer_follow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(CustomerFollowActivity.this, CustomerFollowDetailsActivity.class);
                startActivity(intent);
            }
        });
        lv_customer_follow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CustomerFollowActivity.this, "长按啦啦啦", Toast.LENGTH_LONG).show();
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
        adapter = new CustomerFollowAdapter(CustomerFollowActivity.this, infoList);
        lv_customer_follow.setAdapter(adapter);
//        requestList();

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
