package com.haidehui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.MessageNoticeAdapter;
import com.haidehui.common.Urls;
import com.haidehui.model.Message2B;
import com.haidehui.model.Message3B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedHashMap;

/**
 * 公告通知列表
 * Created by hasee on 2017/6/9.
 */
public class MessageNoticeActivity extends BaseActivity{

    private PullToRefreshListView listView;
    private MouldList<Message3B> totalList = new MouldList<>();
    private ViewSwitcher vs;
    private int currentPage = 1;    //当前页
    private MessageNoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message_notice);
        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.message_notice))
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

    public void initView(){
        listView = (PullToRefreshListView) findViewById(R.id.listview_message_notice);
        vs = (ViewSwitcher) findViewById(R.id.vs_messgae_notice);
    }

    public void initData(){
        noticeAdapter = new MessageNoticeAdapter(mContext,totalList);
        listView.setAdapter(noticeAdapter);
        requestData();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    // 下拉刷新
                    currentPage = 1;

                } else {
                    //上划加载下一页
                    currentPage++;
                }
                requestData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                intent.putExtra("title", getResources().getString(R.string.message_notice_detail));
                intent.putExtra("url", Urls.URL_NOTICEDETAIL+totalList.get(position - 1).getBulletinId()+"&userId="+userId);
                startActivity(intent);

            }
        });
    }

    /**
     * 获取公告消息列表数据
     */
    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", currentPage + "" );
        param.put("userId", userId);

        HtmlRequest.getMessageNotice(MessageNoticeActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params == null) {
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

                Message2B data = (Message2B)params.result;
                MouldList<Message3B> everyList = data.getList();

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
                noticeAdapter.notifyDataSetChanged();

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }
        });
    }


}
