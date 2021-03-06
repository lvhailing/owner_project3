package com.haidehui.fragment;

import android.app.Activity;
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
import com.haidehui.activity.AccountBookActivity;
import com.haidehui.adapter.AccountBookAwardAdapter;
import com.haidehui.model.AccountBookAward2B;
import com.haidehui.model.AccountBookAward3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haidehui.activity.AwardDetailsActivity;
import java.util.LinkedHashMap;

/**
 * 账本 --- 活动奖励
 */
public class AwardDetailsFragment extends Fragment {
    private View mView;
    private Context context;
    private PullToRefreshListView lv;
    private MouldList<AccountBookAward3B> awardList= new MouldList<>();
    private AccountBookAwardAdapter adapterAward;
    private int currentPage = 1;    //当前页
    private String userId = "";
    private AccountBookActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_award_details, container, false);
            try {
                initView(mView);
                initData();
                requestAwardList();
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
        lv= (PullToRefreshListView) mView.findViewById(R.id.lv_award);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv);

    }

    private void initData() {

        adapterAward = new AccountBookAwardAdapter(getActivity(), awardList);
        lv.setAdapter(adapterAward);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(context,AwardDetailsActivity.class);
                intent.putExtra("id", awardList.get(position-1).getId());
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
                requestAwardList();
                mActivity.onRefresh();

            }
        });

    }
    private void requestAwardList() {  // 获取活动奖励列表数据
        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "");
        param.put("userId", userId);

        try {
            HtmlRequest.getAwardList(getActivity(), param, new BaseRequester.OnRequestListener() {
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

                    AccountBookAward2B data = (AccountBookAward2B) params.result;
                    MouldList<AccountBookAward3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(getActivity(), "已显示全部", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        awardList.clear();
                    }
                    awardList.addAll(everyList);

                    //刷新数据
                    adapterAward.notifyDataSetChanged();

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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity= (AccountBookActivity) getActivity();

    }
}
