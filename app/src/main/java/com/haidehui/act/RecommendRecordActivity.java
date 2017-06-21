package com.haidehui.act;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.RecommendRecordAdapter;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultRecommendInfoContentBean;
import com.haidehui.model.ResultRecommendRecordContentBean;
import com.haidehui.model.ResultRecommendRecordItemContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 邀请记录
 * Created by hasee on 2017/6/13.
 */
public class RecommendRecordActivity extends BaseActivity{


    private ViewSwitcher vs_recommend_record;
    private TextView tv_recommend_friends;          //  邀请好友数
    private ListView lv_recommend_record;       //
    private MouldList<ResultRecommendRecordItemContentBean> list;
    private Context context;
    private ResultRecommendRecordContentBean bean;

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
        list = new MouldList<ResultRecommendRecordItemContentBean>();
        bean = new ResultRecommendRecordContentBean();

        vs_recommend_record = (ViewSwitcher) findViewById(R.id.vs_recommend_record);
        tv_recommend_friends = (TextView) findViewById(R.id.tv_recommend_friends);
        lv_recommend_record = (ListView) findViewById(R.id.lv_recommend_record);

        vs_recommend_record.setDisplayedChild(0);


    }

    public void initData(){


        getRecommendData();

//        test();


    }

    public void setView(){

        tv_recommend_friends.setText("推荐好友"+bean.getTotal()+"位");

        RecommendRecordAdapter recordAdapter = new RecommendRecordAdapter(context,list);

        lv_recommend_record.setAdapter(recordAdapter);

    }

    private void getRecommendData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();


//        param.put("userId", userId);
        param.put("userId", "17031409341310256680");

        HtmlRequest.getRecommendRecord(RecommendRecordActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                bean = (ResultRecommendRecordContentBean) params.result;
                if (bean != null) {
                    list = bean.getRecommendList();
                    setView();

                } else {
                    Toast.makeText(RecommendRecordActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
