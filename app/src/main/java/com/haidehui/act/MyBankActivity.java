package com.haidehui.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.MyBankAdapter;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultMyBankListContentBean;
import com.haidehui.model.ResultMyBankListContentItemBean;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 我的银行卡
 * Created by hasee on 2017/6/14.
 */
public class MyBankActivity extends BaseActivity implements View.OnClickListener{

    private ListView lv_mybank;         //
    private Context context;
    private MouldList<ResultMyBankListContentItemBean> list;
    private int lastPress = 0;
    private MyBankAdapter bankAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_mybank);
        initTopTitle();
        initView();

    }

    public void initData(){
//        test();
        requestData();
    }

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("page", "1");
        param.put("userId", userId);
        HtmlRequest.getMyBankList(MyBankActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultMyBankListContentBean b = (ResultMyBankListContentBean) params.result;
                if (b != null) {
                    list = b.getList();
                    bankAdapter = new MyBankAdapter(context,list);
                    lv_mybank.setAdapter(bankAdapter);

                } else {
                    Toast.makeText(MyBankActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void delete(final int position, String id) {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("id", id);
        param.put("userId", userId);
        HtmlRequest.deleteBankList(MyBankActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if(b.getFlag().equals("true")){
                        list.remove(position);
                        bankAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(MyBankActivity.this, b.getMessage(),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MyBankActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void initView(){

        context = this;
        list = new MouldList<ResultMyBankListContentItemBean>();
        lv_mybank = (ListView) findViewById(R.id.lv_mybank);

        View view = View.inflate(MyBankActivity.this, R.layout.ac_mybank_item_button, null);
        view.findViewById(R.id.rl_mybank_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "哈哈哈.点击了按钮.去处理自己的逻辑咯", Toast.LENGTH_SHORT).show();

                Intent i_addCard = new Intent(context,AddBankActivity.class);
                startActivity(i_addCard);

            }
        });
        lv_mybank.addFooterView(view);// 为listview添加footview

        lv_mybank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            private View delview;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lastPress < parent.getCount()) {
                    new AlertDialog.Builder(context)

                            .setMessage("您确定删除么？")
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                Intent i_recharge = new Intent();
//                                i_recharge.setClass(context, ReChargeActivity.class);
//                                context.startActivity(i_recharge);
//                                    requestCancelData();
                                    delete(position,list.get(position).getId());


                                }
                            })
                            .show();
                }

                return true;
            }
        });


    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.mybank_title))
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){


            default:

                break;

        }

    }


}
