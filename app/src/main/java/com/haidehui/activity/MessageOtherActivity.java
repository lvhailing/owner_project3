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
import com.haidehui.adapter.MessageOtherAdapter;
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
 * 其他消息列表
 * Created by hasee on 2017/6/8.
 */
public class MessageOtherActivity extends BaseActivity{

    private PullToRefreshListView listview_message_other;
    private MouldList<Message3B> list;
    private Context context;
    private ViewSwitcher vs_messgae_other;
    private int page = 1;
    private int cachePage_pro = page;
    private MessageOtherAdapter otherAdapter;
    private Message2B infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message_other);
        initTopTitle();
        initView();

    }

    public void initData(){
        requestData();
        otherAdapter = new MessageOtherAdapter(context,list);
        listview_message_other.setAdapter(otherAdapter);


        listview_message_other.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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

        listview_message_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent i_help = new Intent(context, WebActivity.class);
                i_help.putExtra("type", WebActivity.WEBTYPE_NOTICE);
                i_help.putExtra("title", getResources().getString(R.string.message_other_detail));
                i_help.putExtra("url", Urls.URL_OTHERDETAIL+list.get(i-1).getId());
                startActivity(i_help);



            }
        });
    }

    public void initView(){

        list = new MouldList<Message3B>();
        infoBean = new Message2B();
        context = this;
        listview_message_other = (PullToRefreshListView) findViewById(R.id.listview_message_other);
        vs_messgae_other = (ViewSwitcher) findViewById(R.id.vs_messgae_other);
        vs_messgae_other.setDisplayedChild(0);
//        test();



    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.message_other))
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
        param.put("messageType", "others");
        param.put("page", page);
        param.put("userId", userId);
//        param.put("userId", "17021511395798036131");
        cachePage_pro = page;
        HtmlRequest.sentMessageInfo(MessageOtherActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        infoBean = (Message2B)params.result;
//                        list = infoBean.getList();
//                        setView();
                        if(infoBean.getList()!=null){
                            if (infoBean.getList().size() == 0 && page!=1 ) {
                                Toast.makeText(context, "已显示全部",
                                        Toast.LENGTH_SHORT).show();
                                page = cachePage_pro - 1;
                                otherAdapter.notifyDataSetChanged();
                                listview_message_other.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
                                listview_message_other.onRefreshComplete();
                            }else if (infoBean.getList().size() == 0&&page==1){
                                vs_messgae_other.setDisplayedChild(1);
                            }else {
                                // layout.addView(btnLayout);
                                list.clear();
                                list.addAll(infoBean.getList());
                                otherAdapter.notifyDataSetChanged();
//									lv_info_repayplan.getRefreshableView().smoothScrollToPositionFromTop(5, 0);
                                listview_message_other.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        listview_message_other.onRefreshComplete();
                                    }
                                }, 1000);
                                listview_message_other.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
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

//        list = new MouldList<Message2B>();
//        for(int i=0;i<10;i++){
//            Message2B b = new Message2B();
//            b.setName("置业顾问认证"+i);
//            b.setDate("2012-5-"+i);
//            b.setNum("+20"+i+".00");
//            list.add(b);
//
//        }

    }

}
