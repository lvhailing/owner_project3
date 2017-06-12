package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.MessageInfoAdapter;
import com.haidehui.adapter.MessageNoticeAdapter;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.Map;

/**
 * 公告通知列表
 * Created by hasee on 2017/6/9.
 */
public class MessageNoticeActivity extends BaseActivity{

    private PullToRefreshListView listview_message_notice;
    private MouldList<ResultMessageContentBean> list;
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
    }

    public void initView(){

        list = new MouldList<ResultMessageContentBean>();
        context = this;
        listview_message_notice = (PullToRefreshListView) findViewById(R.id.listview_message_notice);
        vs_messgae_notice = (ViewSwitcher) findViewById(R.id.vs_messgae_notice);
        vs_messgae_notice.setDisplayedChild(0);
        test();
        noticeAdapter = new MessageNoticeAdapter(context,list);
        listview_message_notice.setAdapter(noticeAdapter);


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

        listview_message_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {





            }
        });


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
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", "");
        param.put("busiType", Urls.REGISTER);
        param.put("token", token);

        HtmlRequest.sentMessageInfo(MessageNoticeActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        ResultMessageContentBean infoBean = (ResultMessageContentBean)params.result;
//                        funds = infoBean.getFunds();
//                        setView();

//                        if (infoBean.getFunds().size() == 0 && page!=1 ) {
//                            Toast.makeText(context, "已经到最后一页",
//                                    Toast.LENGTH_SHORT).show();
//                            page = cachePage_pro - 1;
//                            infoAdapter.notifyDataSetChanged();
//                            listview_message_info.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
//                            listview_message_info.onRefreshComplete();
//                        }else if (infoBean.getFunds().size() == 0&&page==1){
//                            vs_messgae_info.setDisplayedChild(1);
//                        }else {
//                            // layout.addView(btnLayout);
//                            list.clear();
//                            list.addAll(infoBean.getFunds());
//                            infoAdapter.notifyDataSetChanged();
////									lv_info_repayplan.getRefreshableView().smoothScrollToPositionFromTop(5, 0);
//                            listview_message_info.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    listview_message_info.onRefreshComplete();
//                                }
//                            }, 1000);
//                            listview_message_info.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
//                        }

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

        list = new MouldList<ResultMessageContentBean>();
        for(int i=0;i<10;i++){
            ResultMessageContentBean b = new ResultMessageContentBean();
            b.setName("公告通知"+i);
            b.setDate("2012-5-"+i);
            b.setNum("+哈哈哈哈"+i+"");
            b.setContent("皮皮虾，我们走"+i);
            list.add(b);

        }

    }

}
