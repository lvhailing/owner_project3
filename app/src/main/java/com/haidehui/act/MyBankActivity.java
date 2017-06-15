package com.haidehui.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.MyBankAdapter;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;

/**
 * 我的银行卡
 * Created by hasee on 2017/6/14.
 */
public class MyBankActivity extends BaseActivity implements View.OnClickListener{

    private ListView lv_mybank;         //
    private Context context;
    private MouldList<ResultMessageContentBean> list;
    private int lastPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_mybank);
        initTopTitle();
        initView();
        initData();

    }

    public void initData(){

        test();
        final MyBankAdapter bankAdapter = new MyBankAdapter(context,list);

        lv_mybank.setAdapter(bankAdapter);

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
                                    list.remove(position);
                                    bankAdapter.notifyDataSetChanged();

                                }
                            })
                            .show();
                }

                return true;
            }
        });



    }

    public void initView(){

        context = this;
        lv_mybank = (ListView) findViewById(R.id.lv_mybank);



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

    public void test(){

        list = new MouldList<ResultMessageContentBean>();

        for(int i=0;i<5;i++){
            ResultMessageContentBean bean = new ResultMessageContentBean();

            bean.setName("中国银行"+i);
            bean.setNum("5454545**"+i);
            bean.setContent("zhang+"+i);
            list.add(bean);

        }


    }

}
