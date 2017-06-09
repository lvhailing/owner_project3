package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;

import com.haidehui.R;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 收支消息列表
 * Created by hasee on 2017/6/8.
 */
public class MessageInfoActivity extends BaseActivity{

    private PullToRefreshListView listview_message_info;
    private MouldList<ResultMessageContentBean> list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_message_info);
        initTopTitle();
        initView();
    }

    public void initView(){

        list = new MouldList<ResultMessageContentBean>();
        context = this;
        listview_message_info = (PullToRefreshListView) findViewById(R.id.listview_message_info);

        test();
        MessageInfoAdapter infoAdapter = new MessageInfoAdapter(context,list);
        listview_message_info.setAdapter(infoAdapter);



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
            b.setName("佣金收益"+i);
            b.setDate("2012-5-"+i);
            b.setNum("+20"+i+".00");
            list.add(b);

        }

    }

}
