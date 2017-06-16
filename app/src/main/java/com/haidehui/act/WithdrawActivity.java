package com.haidehui.act;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haidehui.R;
import com.haidehui.adapter.WithdrawAdapter;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.network.types.MouldList;
import com.haidehui.widget.TitleBar;

/**
 * 提现--选择提现账号
 * Created by hasee on 2017/6/14.
 */
public class WithdrawActivity extends BaseActivity{

    private ListView lv_withdraw_mybank;
    private Context context;
    private MouldList<ResultMessageContentBean> list;
    private int lastPress = 0;
    private WithdrawAdapter withdrawAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_withdraw_banks);
        initTopTitle();
        initView();


    }

    public void initView(){
        context = this;
        lv_withdraw_mybank = (ListView) findViewById(R.id.lv_withdraw_mybank);

        test();
        withdrawAdapter = new WithdrawAdapter(context,list);

        lv_withdraw_mybank.setAdapter(withdrawAdapter);

        lv_withdraw_mybank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                    withdrawAdapter.notifyDataSetChanged();

                                }
                            })
                            .show();
                }

                return true;
            }
        });

        lv_withdraw_mybank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent i_withdrawConfirm = new Intent(context,WithdrawConfirmActivity.class);

                startActivity(i_withdrawConfirm);

            }
        });



    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.withdraw_choose_bank_title))
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
            ResultMessageContentBean bean = new ResultMessageContentBean();

            bean.setName("中国银行"+i);
            bean.setNum("5454545**"+i);
            bean.setContent("zhang+"+i);
            list.add(bean);

        }


    }


}
