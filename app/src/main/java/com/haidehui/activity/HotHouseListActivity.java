package com.haidehui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.HotHouseAdapter;
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


/**
 * 首页-- 最热房源列表
 */
public class HotHouseListActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private HotHouseAdapter mAdapter;
    private MouldList<HotHouse3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private ViewSwitcher vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_hot_house);

        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.showLeftImg(true);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
             .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.title_hot_house))
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
        vs = (ViewSwitcher) findViewById(R.id.vs);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) findViewById(R.id.img_empty);
        tv_empty.setText("暂无最热房源");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_hot_house);

        listView = (PullToRefreshListView) findViewById(R.id.listview);

        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
    }

    private void initData() {
        mAdapter = new HotHouseAdapter(mContext, totalList);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // item 点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(mContext, HouseDetailActivity.class);
                intent.putExtra("hid", totalList.get(position - 1).getHid());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        requestListData();
        listView.getRefreshableView().setSelection(0);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取最热房源列表数据
     */
    private void requestListData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("page", currentPage + "");

        try {
            HtmlRequest.getHotHouseData(mContext, param, new BaseRequester.OnRequestListener() {
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

                    HotHouse2B data = (HotHouse2B) params.result;
                    MouldList<HotHouse3B> everyList = data.getList();

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


}
