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
import com.haidehui.activity.RengouDetailsActivity;
import com.haidehui.adapter.RenGouStatusAdapter;
import com.haidehui.model.RenGou2B;
import com.haidehui.model.RenGou3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import com.haidehui.uitls.PreferenceUtil;

/**
 *  认购状态--- 转签约
 */
public class TurnSignFragment extends Fragment {
    private View mView;
    private Context context;
    private PullToRefreshListView lv;
    private MouldList<RenGou3B> totalList= new MouldList<>();
    private RenGouStatusAdapter adapter;
    private int currentPage = 1;    //当前页
    private String userId = "";
    private ViewSwitcher vs;
    private TextView tv_empty;
    private ImageView img_empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_turn_sign, container, false);
            try {
                initView(mView);
                initData();
                requestList();
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
        vs = (ViewSwitcher) mView.findViewById(R.id.vs);
        tv_empty= (TextView) mView.findViewById(R.id.tv_empty);
        img_empty= (ImageView) mView.findViewById(R.id.img_empty);
        tv_empty.setText("暂无认购信息");
        img_empty.setBackgroundResource(R.mipmap.empty_rengou);
        context = getActivity();
        lv= (PullToRefreshListView) mView.findViewById(R.id.lv);
        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(lv);

    }

    private void initData() {

        adapter = new RenGouStatusAdapter(getActivity(), totalList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                Intent intent = new Intent(context,RengouDetailsActivity.class);
                intent.putExtra("id", totalList.get(position-1).getId());
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
                requestList();

            }
        });

    }
    private void requestList() {

        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> param = new HashMap<>();

        param.put("page", currentPage + "");
        param.put("status", "turnSign");
        param.put("userId", userId);

        try {
            HtmlRequest.getRenGouState(getActivity(), param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        vs.setDisplayedChild(1);
                        Toast.makeText(getActivity(), "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        lv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }

                    RenGou2B data = (RenGou2B) params.result;
                    MouldList<RenGou3B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(getActivity(), "已显示全部", Toast.LENGTH_SHORT).show();
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
                    adapter.notifyDataSetChanged();

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
