package com.haidehui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.ProductRoadShowAdapter;
import com.haidehui.model.ProductRoadshow2B;
import com.haidehui.model.ProductRoadshow3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedHashMap;


// 发现--产品路演 列表页
public class ProductRoadshowFragment extends Fragment {
    private View mView;
    private FragmentActivity context;
    private PullToRefreshListView listView;
    private ProductRoadShowAdapter myAdapter;
    private MouldList<ProductRoadshow3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_product_road_show, container, false);
            try {
                initView(mView);
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }

        return mView;
    }

    private void initView(View mView) {
        context = getActivity();
        listView = (PullToRefreshListView) mView.findViewById(R.id.listview);

        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
    }

    private void initData() {
        myAdapter = new ProductRoadShowAdapter(context, totalList);
        listView.setAdapter(myAdapter);
        requestRoadShowListData();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestRoadShowListData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Intent intent = new Intent(LinerListActivity.this, LinerDetailActivity.class);
//                intent.putExtra("id", totalList.get(position - 1).getId());
//                startActivity(intent);
            }
        });

    }

    // 获取路演列表数据
    private void requestRoadShowListData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        HtmlRequest.getRoadShowListData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    upDate(listView);
                    return;
                }

                ProductRoadshow2B data = (ProductRoadshow2B) params.result;
                MouldList<ProductRoadshow3B> everyList = data.getList();
                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(context, "已经到最后一页", Toast.LENGTH_SHORT).show();
                }

                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 ,这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);

                //刷新数据
                myAdapter.notifyDataSetChanged();

                upDate(listView);

            }
        });

    }

    private void upDate(final PullToRefreshListView listView) {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.onRefreshComplete();
            }
        }, 1000);
    }

}
