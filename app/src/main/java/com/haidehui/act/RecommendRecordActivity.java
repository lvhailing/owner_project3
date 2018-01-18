package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.RecommendRecordAdapter;
import com.haidehui.model.ResultRecommendRecordContentBean;
import com.haidehui.model.ResultRecommendRecordItemContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;

import static com.haidehui.R.id.tv_empty;

/**
 * 邀请记录
 * Created by hasee on 2017/6/13.
 */
public class RecommendRecordActivity extends BaseActivity {
    private ViewSwitcher vs_recommend_record;
    private TextView tv_recommend_friends;  //  邀请好友数
    private PullToRefreshListView listView;  // 推荐记录列表
    private MouldList<ResultRecommendRecordItemContentBean> totalList = new MouldList<>();
    private String recommendCode = "";
    private int currentPage = 1;    //当前页
    private RecommendRecordAdapter recordAdapter;
    private ResultRecommendRecordContentBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_recommend_record);

        initTopTitle();
        initView();
        initData();

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false).setIndicator(R.drawable.back).setCenterText(getResources().getString(R.string.setting_recommend_record)).showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

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

    public void initView() {
        recommendCode = getIntent().getStringExtra("recommendCode");

        tv_recommend_friends = (TextView) findViewById(R.id.tv_recommend_friends);
        listView = (PullToRefreshListView) findViewById(R.id.lv_recommend_record);
        vs_recommend_record = (ViewSwitcher) findViewById(R.id.vs_recommend_record);
        TextView tv_empty = (TextView)findViewById(R.id.tv_empty);
        ImageView img_empty = (ImageView)findViewById(R.id.img_empty);
        tv_empty.setText("暂无推荐");
        img_empty.setBackgroundResource(R.mipmap.icon_empty_recommendation);

        //PullToRefreshListView  上滑加载更多及下拉刷新
        ViewUtils.slideAndDropDown(listView);

        vs_recommend_record.setDisplayedChild(0);


    }

    public void initData() {
        recordAdapter = new RecommendRecordAdapter(mContext, totalList);
        listView.setAdapter(recordAdapter);

        getRecommendData();
//        test();
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    //下拉刷新
                    currentPage = 1;
                } else {
                    //上划加载下一页
                    currentPage++;
                }
                getRecommendData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        currentPage = 1;
//        getRecommendData();
//        listView.getRefreshableView().setSelection(0);
    }

    public void setView() {
        tv_recommend_friends.setText("推荐好友" + bean.getTotal() + "位");
    }

    /**
     * 获取邀请记录信息
     */
    private void getRecommendData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("recommendCode", recommendCode);
        param.put("page", currentPage + "");
        try {
            HtmlRequest.getRecommendRecord(RecommendRecordActivity.this, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params.result == null) {
                        vs_recommend_record.setDisplayedChild(1);
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        listView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listView.onRefreshComplete();
                            }
                        }, 1000);
                        return;
                    }
                    bean = (ResultRecommendRecordContentBean) params.result;
                    MouldList<ResultRecommendRecordItemContentBean> everyList = bean.getRecommendList();
                    setView();
                    if ((everyList == null || everyList.size() == 0) && currentPage != 1) {
                        Toast.makeText(mContext, "已显示全部", Toast.LENGTH_SHORT).show();
                    }

                    if (currentPage == 1) {
                        //刚进来时 加载第一页数据，或下拉刷新 重新加载数据 。这两种情况之前的数据都清掉
                        totalList.clear();
                    }

                    totalList.addAll(everyList);
                    if (totalList.size() == 0) {
                        vs_recommend_record.setDisplayedChild(1);
                    } else {
                        vs_recommend_record.setDisplayedChild(0);
                    }
                    //刷新数据
                    recordAdapter.notifyDataSetChanged();

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

//    public void test(){
//        list = new MouldList<ResultMessageContentBean>();
//        for(int i=0;i<10;i++){
//            ResultMessageContentBean b = new ResultMessageContentBean();
//            b.setName("haha"+i);
//            b.setDate("一级"+i);
//            b.setNum("100"+i);
//            list.add(b);
//
//        }
//
//    }

}
