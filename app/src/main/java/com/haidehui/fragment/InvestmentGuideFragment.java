package com.haidehui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.haidehui.activity.WebForShareActivity;
import com.haidehui.adapter.InvestmentGuideAdapter;
import com.haidehui.model.InvestmentGuide2B;
import com.haidehui.model.InvestmentGuide3B;
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

// 发现--投资指南 列表页
public class InvestmentGuideFragment extends Fragment {
    private View mView;
    private Context context;
    private PullToRefreshListView listView;
    private InvestmentGuideAdapter myAdapter;
    private MouldList<InvestmentGuide3B> totalList = new MouldList<>();
    private int currentPage = 1;    //当前页
    private ViewSwitcher vs;
    private String userId = "";

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

//        try {
//            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        vs = (ViewSwitcher) mView.findViewById(R.id.vs);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView) mView.findViewById(R.id.img_empty);
        tv_empty.setText("暂无投资指南信息");
        img_empty.setBackgroundResource(R.mipmap.ic_empty_investment_guide);

        listView = (PullToRefreshListView) mView.findViewById(R.id.listview);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);
    }

    private void initData() {
        myAdapter = new InvestmentGuideAdapter(context, totalList);
        listView.setAdapter(myAdapter);

        requestInvestmentGuideListData();
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
                requestInvestmentGuideListData();
            }
        });

        /**
         *  投资指南列表 item  点击监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (PreferenceUtil.isLogin()) { // 用户登录时：跳转到投资指南详情页时显示打电话、微信小卡片
                    try {
                        userId = DESUtil.decrypt(PreferenceUtil.getUserId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent i_web = new Intent(getActivity(), WebForShareActivity.class);
                    i_web.putExtra("type", WebForShareActivity.WEBTYPE_INVESTMENT_GUIDE_DETAILS);
                    i_web.putExtra("id", totalList.get(position - 1).getId());
                    i_web.putExtra("uid", userId);
                    i_web.putExtra("title", "投资指南详情");
                    startActivity(i_web);
                } else { // 用户未登录时：跳转到投资指南详情页不显示小卡片
                    Intent i_web = new Intent(getActivity(), WebForShareActivity.class);
                    i_web.putExtra("type", WebForShareActivity.WEBTYPE_INVESTMENT_GUIDE_DETAILS);
                    i_web.putExtra("id", totalList.get(position - 1).getId());
                    i_web.putExtra("uid", "0");  // 用户未登录时，用户id传“0”
                    i_web.putExtra("title", "投资指南详情");
                    startActivity(i_web);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取投资指南列表数据
     */
    private void requestInvestmentGuideListData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("page", currentPage + "");

        HtmlRequest.getInvestmentGuideListData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    vs.setDisplayedChild(1);
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    upDate(listView);
                    return;
                }

                InvestmentGuide2B data = (InvestmentGuide2B) params.result;
                MouldList<InvestmentGuide3B> everyList = data.getList();

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
            requestInvestmentGuideListData();
            listView.getRefreshableView().setSelection(0);
        }
    }
}
