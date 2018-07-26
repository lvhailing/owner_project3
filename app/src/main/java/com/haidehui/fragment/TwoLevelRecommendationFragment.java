package com.haidehui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.activity.RecommendDetailActivity;
import com.haidehui.adapter.RecommendationAdapter;
import com.haidehui.model.OneLevelRecomendation3B;
import com.haidehui.model.OneLevelRecommendation2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;

/**
 * 我的事业合伙人-- 二级推荐 列表页
 */
public class TwoLevelRecommendationFragment extends Fragment {
    private View mView;
    private Context context;
    private PullToRefreshListView listView;
    private RecommendationAdapter myAdapter;
    private MouldList<OneLevelRecomendation3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private ViewSwitcher vs;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_recommendation, container, false);
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

        vs = (ViewSwitcher) mView.findViewById(R.id.vs);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) mView.findViewById(R.id.img_empty);
        tv_empty.setText("暂无推荐");
        img_empty.setBackgroundResource(R.mipmap.icon_empty_recommendation);

        listView = (PullToRefreshListView) mView.findViewById(R.id.listview);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
    }

    private void initData() {
        myAdapter = new RecommendationAdapter(context, totalList);
        listView.setAdapter(myAdapter);

        requestOneLevelRecData();
        listView.getRefreshableView().setSelection(0);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestOneLevelRecData();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //item  点击监听
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Intent intent = new Intent(getActivity(), RecommendDetailActivity.class);
//                intent.putExtra("userId", totalList.get(position - 1).getUserId());
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.i("hh", "投资指南---Fragment----onResume");

//        currentPage = 1;
//        requestInvestmentGuideListData();
//        listView.getRefreshableView().setSelection(0);
    }

    // 获取二级推荐列表数据
    private void requestOneLevelRecData() {
        userId = null;
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("typeLevel", 2);
        param.put("page", currentPage + "");

        HtmlRequest.getOneLevelRecData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    vs.setDisplayedChild(1);
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    upDate(listView);
                    return;
                }

                OneLevelRecommendation2B data = (OneLevelRecommendation2B) params.result;
                MouldList<OneLevelRecomendation3B> everyList = data.getMyRecommendList();

                if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                    Toast.makeText(context, "已显示全部", Toast.LENGTH_SHORT).show();
                }

                if (currentPage == 1) {
                    //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 ,这两种情况之前的数据都清掉
                    totalList.clear();
                }
                totalList.addAll(everyList);
                if (totalList.size() == 0) {
                    vs.setDisplayedChild(1);
                } else {
                    vs.setDisplayedChild(0);
                }
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

    public void upDateInvestmentGuideList() {
        if (listView != null) {
            currentPage = 1;
            requestOneLevelRecData();
            listView.getRefreshableView().setSelection(0);
        }
    }
}
