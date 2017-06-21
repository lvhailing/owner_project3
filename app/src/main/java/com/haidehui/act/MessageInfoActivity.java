package com.haidehui.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.MessageInfoAdapter;
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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 收支消息列表（账本消息）
 * Created by hasee on 2017/6/8.
 */
public class MessageInfoActivity extends BaseActivity{

    private PullToRefreshListView listview_message_info;
    private MouldList<ResultMessageItemContentBean> list;
    private Context context;
//    private ViewSwitcher vs_messgae_info;
    private int page = 1;
    private int cachePage_pro = page;
    private MessageInfoAdapter infoAdapter;
    private ResultMessageContentBean infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message_info);
        initTopTitle();
        initView();

    }

    public void initData(){

        setView();
//        requestData();
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

    public void setView(){

//        test();

        infoAdapter = new MessageInfoAdapter(context,list);
        requestData();

        listview_message_info.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
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
        listview_message_info.setAdapter(infoAdapter);
        listview_message_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i-1).getBusiType().equals("withdrawCash")){       //  提现

                    Intent intent = new Intent(context, WithDrawDetailsActivity.class);
                    intent.putExtra("messageId", list.get(i-1).getId());
                    intent.putExtra("id", list.get(i-1).getBusiPriv());
                    startActivity(intent);

                }else if(list.get(i-1).getBusiType().equals("registerCash")){     //  下载奖励详情


                    Intent intent = new Intent(context,AwardDetailsActivity.class);
                    intent.putExtra("messageId", list.get(i-1).getId());
                    intent.putExtra("id", list.get(i-1).getBusiPriv());
                    startActivity(intent);


                }else if(list.get(i-1).getBusiType().equals("commissionOver")){     //  佣金收益

                    Intent intent = new Intent(context, CommissionDetailsActivity.class);
                    intent.putExtra("messageId", list.get(i-1).getId());
                    intent.putExtra("id", list.get(i-1).getBusiPriv());
                    startActivity(intent);

                }else if(list.get(i-1).getBusiType().equals("awardredemption")){        //  活动奖励

                    Intent intent = new Intent(context,AwardDetailsActivity.class);
                    intent.putExtra("messageId", list.get(i-1).getId());
                    intent.putExtra("id", list.get(i-1).getBusiPriv());
                    startActivity(intent);

                }else{

                }

            }
        });

    }

    public void initView(){

        list = new MouldList<ResultMessageItemContentBean>();
        infoBean = new ResultMessageContentBean();
        context = this;
        listview_message_info = (PullToRefreshListView) findViewById(R.id.listview_message_info);
//        vs_messgae_info = (ViewSwitcher) findViewById(R.id.vs_messgae_info);
//        vs_messgae_info.setDisplayedChild(0);
//        test();

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.message_info))
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
        HtmlRequest.sentMessageInfo(MessageInfoActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    if (params.result != null) {
                        infoBean = (ResultMessageContentBean)params.result;
//                        list = infoBean.getList();
//                        setView();

                        if (infoBean.getList().size() == 0 && page!=1 ) {
                            Toast.makeText(context, "已经到最后一页",
                                    Toast.LENGTH_SHORT).show();
                            page = cachePage_pro - 1;
                            infoAdapter.notifyDataSetChanged();
                            listview_message_info.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
                            listview_message_info.onRefreshComplete();
                        }else if (infoBean.getList().size() == 0&&page==1){
//                            vs_messgae_info.setDisplayedChild(1);
                        }else {
                            // layout.addView(btnLayout);
                            list.clear();
                            list.addAll(infoBean.getList());
                            infoAdapter.notifyDataSetChanged();
//									lv_info_repayplan.getRefreshableView().smoothScrollToPositionFromTop(5, 0);
                            listview_message_info.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    listview_message_info.onRefreshComplete();
                                }
                            }, 1000);
                            listview_message_info.getRefreshableView().smoothScrollToPositionFromTop(0, 100, 100);
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



}
