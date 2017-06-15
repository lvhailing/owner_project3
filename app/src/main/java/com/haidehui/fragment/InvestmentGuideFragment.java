package com.haidehui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.InvestmentGuideAdapter;
import com.haidehui.model.InvestmentGuide2B;
import com.haidehui.model.InvestmentGuide3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.MyListView;

import java.util.HashMap;
import java.util.Map;

// 发现--投资指南 列表页
public class InvestmentGuideFragment extends Fragment {
    private View mView;
    private Context context;
    private MyListView myListView;
    private InvestmentGuideAdapter myAdapter;
    private MouldList<InvestmentGuide3B> list; // 投资指南列表数据
    private MouldList<InvestmentGuide3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_investment_guide, container, false);
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
        myListView = (MyListView) mView.findViewById(R.id.myListView);
    }

    private void initData() {
        myAdapter = new InvestmentGuideAdapter(context, list);
        myListView.setAdapter(myAdapter);

        requestInvestmentGuideListData();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });
//        setListViewHeightBasedOnChildren(getActivity(), myListView, 0);

    }


    // 获取投资指南列表数据
    private void requestInvestmentGuideListData() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPage + "");
        HtmlRequest.getInvestmentGuideListData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }

                InvestmentGuide2B data = (InvestmentGuide2B) params.result;
                MouldList<InvestmentGuide3B> everyList = data.getList();
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

            }
        });

    }
}
