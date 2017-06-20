package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.RecommendRecordAdapter;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;

/**
 * 邀请记录
 * Created by hasee on 2017/6/13.
 */
public class RecommendRecordActivity extends BaseActivity{


    private ViewSwitcher vs_recommend_record;
    private TextView tv_recommend_friends;          //  邀请好友数
    private ListView lv_recommend_record;       //
    private MouldList<ResultMessageContentBean> list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_recommend_record);
        initTopTitle();
        initView();
        initData();

    }

    public void initView(){
        context = this;
        list = new MouldList<ResultMessageContentBean>();

        vs_recommend_record = (ViewSwitcher) findViewById(R.id.vs_recommend_record);
        tv_recommend_friends = (TextView) findViewById(R.id.tv_recommend_friends);
        lv_recommend_record = (ListView) findViewById(R.id.lv_recommend_record);

        vs_recommend_record.setDisplayedChild(0);


    }

    public void initData(){

        tv_recommend_friends.setText("推荐好友8位");


//        test();
        RecommendRecordAdapter recordAdapter = new RecommendRecordAdapter(context,list);

        lv_recommend_record.setAdapter(recordAdapter);

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.setting_recommend_record))
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




}
