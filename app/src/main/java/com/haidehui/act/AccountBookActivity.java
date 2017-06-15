package com.haidehui.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.AccountBookAdapter;
import com.haidehui.adapter.CustomerInfoAdapter;
import com.haidehui.bean.ResultAccountBooklistBean;
import com.haidehui.dialog.IntroductionsDialog;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 *  我的 --- 我的账本
 */
public class AccountBookActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView scrollview;
    private ImageView img_back;
    private ImageView img_income_intro;
    private TextView tv_not_give;
    private TextView tv_total_give;
    private TextView tv_gived;
    private TextView tv_commission;
    private TextView tv_award;
    private TextView tv_withdraw;
    private MyListView lv_commission;
    private MyListView lv_award;
    private MyListView lv_withdraw;
    private AccountBookAdapter adapter;
    private Resources mResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_account_book);
        initTopTitle();
        initView();
        initData();
        DefaultView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setVisibility(View.GONE);
    }

    private void initView() {
        mResource = getResources();
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        img_back= (ImageView) findViewById(R.id.img_back);
        img_income_intro= (ImageView) findViewById(R.id.img_income_intro);
        tv_not_give= (TextView) findViewById(R.id.tv_not_give);
        tv_total_give= (TextView) findViewById(R.id.tv_total_give);
        tv_gived= (TextView) findViewById(R.id.tv_gived);
        tv_commission= (TextView) findViewById(R.id.tv_commission);
        tv_award= (TextView) findViewById(R.id.tv_award);
        tv_withdraw= (TextView) findViewById(R.id.tv_withdraw);

        lv_commission= (MyListView) findViewById(R.id.lv_commission);
        lv_award= (MyListView) findViewById(R.id.lv_award);
        lv_withdraw= (MyListView) findViewById(R.id.lv_withdraw);

        lv_commission.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, CommissionDetailsActivity.class);
                startActivity(intent);
            }
        });
        lv_award.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, AwardDetailsActivity.class);
                startActivity(intent);
            }
        });
        lv_withdraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(AccountBookActivity.this, WithDrawDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        img_back.setOnClickListener(this);
        img_income_intro.setOnClickListener(this);
        tv_commission.setOnClickListener(this);
        tv_award.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_income_intro:
                IntroductionsDialog dialog =new IntroductionsDialog(this);
                dialog.show();
                break;
            case R.id.tv_commission:
                initPageData("commission");
                StringUtil.changeButtonStyleThree(tv_commission,tv_award,tv_withdraw,
                        R.id.tv_commission,mResource);
                break;
            case R.id.tv_award:
                initPageData("award");
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_award, mResource);
                break;
            case R.id.tv_withdraw:
                initPageData("withdraw");
                StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                        R.id.tv_withdraw, mResource);
                break;
        }
    }
    private void initPageData(String status) {
        if (status.equals("commission")) {
            lv_commission.setVisibility(View.VISIBLE);
            lv_award.setVisibility(View.GONE);
            lv_withdraw.setVisibility(View.GONE);
            test1();
            scrollview.smoothScrollTo(0, 0);
            //          requestHotProductData();
        } else if (status.equals("award")) {
            lv_award.setVisibility(View.VISIBLE);
            lv_commission.setVisibility(View.GONE);
            lv_withdraw.setVisibility(View.GONE);
            test2();
            scrollview.smoothScrollTo(0, 0);
  //          requestRecommendProductData();
        }else if(status.equals("withdraw")) {
            lv_withdraw.setVisibility(View.VISIBLE);
            lv_award.setVisibility(View.GONE);
            lv_commission.setVisibility(View.GONE);
            test3();
            scrollview.smoothScrollTo(0, 0);
  //          requestRecommendProductData();
        }
    }

    private void DefaultView() {
        initPageData("commission");
        StringUtil.changeButtonStyleThree(tv_commission, tv_award, tv_withdraw,
                R.id.tv_commission, mResource);
    }

    private void test1(){
        MouldList<ResultAccountBooklistBean> list=new MouldList<ResultAccountBooklistBean>();
        for (int i=0;i<10;i++){
            ResultAccountBooklistBean bean=new ResultAccountBooklistBean();
            if(i%2==0){
                bean.setInfo("多伦多皇冠大道四季酒店"+i);
                bean.setMoney("+2222.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("finished");
            }else{
                bean.setInfo("香港维多利亚");
                bean.setMoney("+555555.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("ing");
            }
            list.add(bean);
        }
        adapter = new AccountBookAdapter(AccountBookActivity.this, list);
        lv_commission.setAdapter(adapter);
        setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_commission, 0);
    }
    private void test2(){
        MouldList<ResultAccountBooklistBean> list=new MouldList<>();
        for (int i=0;i<10;i++){
            ResultAccountBooklistBean bean=new ResultAccountBooklistBean();
            if(i%2==0){
                bean.setInfo("变变变222了"+i);
                bean.setMoney("+2222.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("finished");
            }else{
                bean.setInfo("香港维多利亚");
                bean.setMoney("+5555555.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("ing");
            }
            list.add(bean);
        }
        adapter = new AccountBookAdapter(AccountBookActivity.this, list);
        lv_award.setAdapter(adapter);
        setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_award, 0);
    }
    private void test3(){
        MouldList<ResultAccountBooklistBean> list=new MouldList<>();
        for (int i=0;i<10;i++){
            ResultAccountBooklistBean bean=new ResultAccountBooklistBean();
            if(i%2==0){
                bean.setInfo("变变变3了"+i);
                bean.setMoney("+2222.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("finished");
            }else{
                bean.setInfo("香港维多利亚");
                bean.setMoney("+55555.22");
                bean.setTime("08-20 04:09");
                bean.setStatus("ing");
            }
            list.add(bean);
        }
        adapter = new AccountBookAdapter(AccountBookActivity.this, list);
        lv_withdraw.setAdapter(adapter);
        setListViewHeightBasedOnChildren(AccountBookActivity.this, lv_withdraw, 0);
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
    /* private void requestListData() {  // 获取最热房源列表数据
        Map<String, Object> param = new HashMap<>();
        param.put("currentPage", currentPage + "");

        try {
            HtmlRequest.getHotHouseData(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
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
