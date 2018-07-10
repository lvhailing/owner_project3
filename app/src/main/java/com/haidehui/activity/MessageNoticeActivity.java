package com.haidehui.activity;

import android.content.Context;
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
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultMessageItemContentBean;
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

    private PullToRefreshListView listview_message_notice;
    private MouldList<ResultMessageItemContentBean> list;
    private Context context;
    private ViewSwitcher vs_messgae_notice;
    private int page = 1;
    private int cachePage_pro = page;
    private MessageNoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message_notice);
        initTopTitle();
        initView();
        initData();
    }

    public void initData(){

        requestData();

        listview_message_notice.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isHeaderShown()) {
                    if (page >= 2) {
                        --page;
                        requestData();

                    } else {
                        page = 1;
                        requestData();
                    }

                } else {
                    ++page;
                    requestData();

                }
            }
        });

        listview_message_notice.setAdapter(noticeAdapter);

        listview_message_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent i_help = new Intent(context, WebActivity.class);
                i_help.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                i_help.putExtra("title", getResources().getString(R.string.message_notice_detail));
                i_help.putExtra("url", Urls.URL_NOTICEDETAIL+list.get(i-1).getBulletinId()+"&userId="+userId);
                startActivity(i_help);

            }
        });
    }

    public void initView(){

        list = new MouldList<ResultMessageItemContentBean>();
        context = this;
        listview_message_notice = (PullToRefreshListView) findViewById(R.id.listview_message_notice);
        vs_messgae_notice = (ViewSwitcher) findViewById(R.id.vs_messgae_notice);
        vs_messgae_notice.setDisplayedChild(0);

//        test();



        noticeAdapter = new MessageNoticeAdapter(context,list);




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

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", page);
        param.put("userId", userId);
        cachePage_pro = page;
        HtmlRequest.getMessageNotice(MessageNoticeActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        ResultMessageContentBean infoBean = (ResultMessageContentBean)params.result;
                        if(infoBean.getList()!=null){
                            if (infoBean.getList().size() == 0 && page!=1 ) {
                                Toast.makeText(context, "已显示全部",
                                        Toast.LENGTH_SHORT).show();
                                page = cachePage_pro - 1;
                                noticeAdapter.notifyDataSetChanged();
                                listview_message_notice.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
                                listview_message_notice.onRefreshComplete();
                            }else if (infoBean.getList().size() == 0&&page==1){
                            vs_messgae_notice.setDisplayedChild(1);
                            }else {
                                // layout.addView(btnLayout);

                                list.clear();
                                list.addAll(infoBean.getList());
                                noticeAdapter.notifyDataSetChanged();
//									lv_info_repayplan.getRefreshableView().smoothScrollToPositionFromTop(5, 0);
                                listview_message_notice.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        listview_message_notice.onRefreshComplete();
                                    }
                                }, 1000);
                                listview_message_notice.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
                            }
                        }


                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void test(){
        list = new MouldList<ResultMessageItemContentBean>();
        for(int i=0;i<10;i++){
            ResultMessageItemContentBean bean = new ResultMessageItemContentBean();
            bean.setDescription("-----");
            bean.setDescription("-----");
            bean.setDescription("-----");
            bean.setTitle("111");
            bean.setBulletinId("2222");
            list.add(bean);
        }

    }

}
