package com.haidehui.act;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.adapter.RenGouStatusAdapter;
import com.haidehui.bean.ResultRenGouStatuslistBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.StringUtil;
import com.haidehui.widget.MyListView;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 *  我的 --- 认购状态
 */
public class RenGouStatusActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_subscribed;
    private TextView tv_to_sign;
    private TextView tv_unsubscribe;
    private PullToRefreshListView lv_subscribed;
    private PullToRefreshListView lv_to_sign;
    private PullToRefreshListView lv_unsubscribe;
    private RenGouStatusAdapter adapter;
    private Resources mResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_rengou_status);
        initTopTitle();
        initView();
        initData();
        DefaultView();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.title_rengou_status))
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
        mResource = getResources();
        tv_subscribed= (TextView) findViewById(R.id.tv_subscribed);
        tv_to_sign= (TextView) findViewById(R.id.tv_to_sign);
        tv_unsubscribe= (TextView) findViewById(R.id.tv_unsubscribe);

        lv_subscribed= (PullToRefreshListView) findViewById(R.id.lv_subscribed);
        lv_to_sign= (PullToRefreshListView) findViewById(R.id.lv_to_sign);
        lv_unsubscribe= (PullToRefreshListView) findViewById(R.id.lv_unsubscribe);

        lv_subscribed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(RenGouStatusActivity.this, RengouDetailsActivity.class);
                startActivity(intent);
            }
        });
        lv_to_sign.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(RenGouStatusActivity.this, RengouDetailsActivity.class);
                startActivity(intent);
            }
        });
        lv_unsubscribe.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(RenGouStatusActivity.this, RengouDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        tv_subscribed.setOnClickListener(this);
        tv_to_sign.setOnClickListener(this);
        tv_unsubscribe.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_subscribed:
                initPageData("subscribed");
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_subscribed, mResource);
                break;
            case R.id.tv_to_sign:
                initPageData("to_sign");
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_to_sign, mResource);
                break;
            case R.id.tv_unsubscribe:
                initPageData("unsubscribe");
                StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                        R.id.tv_unsubscribe, mResource);
                break;
        }
    }
    private void initPageData(String status) {
        if (status.equals("subscribed")) {
            lv_subscribed.setVisibility(View.VISIBLE);
            lv_to_sign.setVisibility(View.GONE);
            lv_unsubscribe.setVisibility(View.GONE);
            test1();
            //          requestHotProductData();
        } else if (status.equals("to_sign")) {
            lv_to_sign.setVisibility(View.VISIBLE);
            lv_subscribed.setVisibility(View.GONE);
            lv_unsubscribe.setVisibility(View.GONE);
            test2();
  //          requestRecommendProductData();
        }else if(status.equals("unsubscribe")) {
            lv_unsubscribe.setVisibility(View.VISIBLE);
            lv_subscribed.setVisibility(View.GONE);
            lv_to_sign.setVisibility(View.GONE);
            test3();
  //          requestRecommendProductData();
        }
    }

    private void DefaultView() {
        initPageData("subscribed");
        StringUtil.changeButtonStyleRenGou(tv_subscribed, tv_to_sign, tv_unsubscribe,
                R.id.tv_subscribed, mResource);
    }

    private void test1(){
        MouldList<ResultRenGouStatuslistBean> list=new MouldList<ResultRenGouStatuslistBean>();
        for (int i=0;i<10;i++){
            ResultRenGouStatuslistBean bean=new ResultRenGouStatuslistBean();
            if(i%2==0){
                bean.setProject("帕诺拉普达海景公寓111111"+i);
                bean.setName("moumoumou");
                bean.setMoney("5000.00"+"元");
            }else{
                bean.setProject("泰国泰国帕诺海景公寓(ajdojagowjgao)111111");
                bean.setName("moumoumou");
                bean.setMoney("5000.00" + "元");
            }
            list.add(bean);
        }
        adapter = new RenGouStatusAdapter(RenGouStatusActivity.this, list);
        lv_subscribed.setAdapter(adapter);
    }
    private void test2(){
        MouldList<ResultRenGouStatuslistBean> list=new MouldList<>();
        for (int i=0;i<10;i++){
            ResultRenGouStatuslistBean bean=new ResultRenGouStatuslistBean();
            if(i%2==0){
                bean.setProject("帕诺拉普达海景公寓2222222"+i);
                bean.setName("moumoumou");
                bean.setMoney("5000.00" + "元");
            }else{
                bean.setProject("泰国泰国帕诺海景公寓(ajdojagowjgao)22222");
                bean.setName("moumoumou");
                bean.setMoney("5000.00" + "元");
            }
            list.add(bean);
        }
        adapter = new RenGouStatusAdapter(RenGouStatusActivity.this, list);
        lv_to_sign.setAdapter(adapter);
    }
    private void test3(){
        MouldList<ResultRenGouStatuslistBean> list=new MouldList<>();
        for (int i=0;i<10;i++){
            ResultRenGouStatuslistBean bean=new ResultRenGouStatuslistBean();
            if(i%2==0){
                bean.setProject("帕诺拉普达海景公寓333333"+i);
                bean.setName("moumoumou");
                bean.setMoney("5000.00" + "元");
            }else{
                bean.setProject("泰国泰国帕诺海景公寓(ajdojagowjgao)333333");
                bean.setName("moumoumou");
                bean.setMoney("5000.00" + "元");
            }
            list.add(bean);
        }
        adapter = new RenGouStatusAdapter(RenGouStatusActivity.this, list);
        lv_unsubscribe.setAdapter(adapter);
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
