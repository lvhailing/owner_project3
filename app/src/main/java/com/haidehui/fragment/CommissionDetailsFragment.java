package com.haidehui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.act.CommissionDetailsActivity;
import com.haidehui.adapter.AccountBookCommissionAdapter;
import com.haidehui.model.AccountBookCommission2B;
import com.haidehui.model.AccountBookCommission3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedHashMap;

/**
 * 账本 --- 佣金收益
 */
public class CommissionDetailsFragment extends Fragment {
    private View mView;
    private Context context;
    private PullToRefreshListView lv;
    private MouldList<AccountBookCommission3B> commissionList= new MouldList<>();
    private AccountBookCommissionAdapter adapterCommission;
    private int currentPage = 1;    //当前页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_commission_details, container, false);
            try {
                initView(mView);
                initData();
                requestCommissionList();
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

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initView(View mView) {
        context = getActivity();
        lv= (PullToRefreshListView) mView.findViewById(R.id.lv_commission);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv);

    }

    private void initData() {

        adapterCommission = new AccountBookCommissionAdapter(getActivity(), commissionList);
        lv.setAdapter(adapterCommission);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), CommissionDetailsActivity.class);
                intent.putExtra("id", commissionList.get(position-1).getId());
                startActivity(intent);

            }
        });
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestCommissionList();

            }
        });

    }
    private void requestCommissionList() {  // 获取佣金收益列表数据
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("userId", "17021511395798036131");

        try {
            HtmlRequest.getCommissionList(getActivity(), param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        Toast.makeText(getActivity(), "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        lv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    AccountBookCommission2B data = (AccountBookCommission2B) params.result;
                    MouldList<AccountBookCommission3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(getActivity(), "已经到最后一页", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        commissionList.clear();
                    }
                    commissionList.addAll(everyList);

                    //刷新数据
                    adapterCommission.notifyDataSetChanged();

                    lv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lv.onRefreshComplete();
                        }
                    }, 1000);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
