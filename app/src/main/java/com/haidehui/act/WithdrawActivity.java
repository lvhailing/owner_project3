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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.haidehui.R;
import com.haidehui.adapter.MyBankAdapter;
import com.haidehui.adapter.WithdrawAdapter;
import com.haidehui.dialog.BasicDialog;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultMyBankListContentBean;
import com.haidehui.model.ResultMyBankListContentItemBean;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.widget.TitleBar;

import java.util.LinkedHashMap;

/**
 * 提现--选择提现账号
 * Created by hasee on 2017/6/14.
 */
public class WithdrawActivity extends BaseActivity{

    private ListView lv_withdraw_mybank;
    private Context context;
    private MouldList<ResultMyBankListContentItemBean> list;
    private int lastPress = 0;
    private MyBankAdapter withdrawAdapter;
    private RelativeLayout rl_mybank_add;
    private ViewSwitcher vs_withdraw_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_withdraw_banks);
        initTopTitle();
        initView();


    }

    public void initData(){
        requestData();
//        test();
        rl_mybank_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_addCard = new Intent(context,AddBankActivity.class);
                startActivity(i_addCard);
            }
        });


        lv_withdraw_mybank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            private View delview;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lastPress < parent.getCount()) {
                    /*new AlertDialog.Builder(context)

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
                            .show();*/

                    BasicDialog dialog=new BasicDialog(context, new BasicDialog.OnBasicChanged() {
                        @Override
                        public void onConfim() {
                            delete(position,list.get(position).getId());
                        }

                        @Override
                        public void onCancel() {
                        }
                    },"确认删除吗？","确认");
                    dialog.show();
                }

                return true;
            }
        });

        lv_withdraw_mybank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent i_withdrawConfirm = new Intent(context,WithdrawConfirmActivity.class);
                i_withdrawConfirm.putExtra("bankAddress",list.get(i).getBankCardNum());
                i_withdrawConfirm.putExtra("bankCardId",list.get(i).getId());
                i_withdrawConfirm.putExtra("bankCardNum",list.get(i).getBankCardNum());
                i_withdrawConfirm.putExtra("bankName",list.get(i).getBankName());
                i_withdrawConfirm.putExtra("realName",list.get(i).getRealName());


                startActivity(i_withdrawConfirm);

            }
        });

        /*View view = View.inflate(WithdrawActivity.this, R.layout.ac_mybank_item_button, null);
        view.findViewById(R.id.rl_mybank_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "哈哈哈.点击了按钮.去处理自己的逻辑咯", Toast.LENGTH_SHORT).show();

                Intent i_addCard = new Intent(context,AddBankActivity.class);
                startActivity(i_addCard);

            }
        });

        lv_withdraw_mybank.addFooterView(view);// 为listview添加footview*/

    }

    public void initView(){
        ActivityStack stack = ActivityStack.getActivityManage();
        stack.addActivity(this);
        context = this;
        lv_withdraw_mybank = (ListView) findViewById(R.id.lv_withdraw_mybank);
        rl_mybank_add = (RelativeLayout) findViewById(R.id.rl_mybank_add);
        vs_withdraw_bank = (ViewSwitcher) findViewById(R.id.vs_withdraw_bank);
        vs_withdraw_bank.setDisplayedChild(0);


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

    private void requestData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("page", "");
        param.put("userId", userId);

        HtmlRequest.getMyBankList(WithdrawActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultMyBankListContentBean b = (ResultMyBankListContentBean) params.result;
                if (b != null) {
                    list = b.getList();
                    if(list.size()==0){
                        vs_withdraw_bank.setDisplayedChild(1);
                    }else{
                        vs_withdraw_bank.setDisplayedChild(0);
                    }
                    withdrawAdapter = new MyBankAdapter(context,list);

                    lv_withdraw_mybank.setAdapter(withdrawAdapter);


                } else {
                    vs_withdraw_bank.setDisplayedChild(1);
                    Toast.makeText(WithdrawActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void delete(final int position, String id) {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("id", id);
        param.put("userId", userId);

        HtmlRequest.deleteBankList(WithdrawActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if(b.getFlag().equals("true")){
                        list.remove(position);
                        if(list.size()==0){
                            vs_withdraw_bank.setDisplayedChild(1);
                        }else{
                            vs_withdraw_bank.setDisplayedChild(0);
                        }
                        withdrawAdapter.notifyDataSetChanged();

                    }
                    Toast.makeText(WithdrawActivity.this, b.getMessage(),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(WithdrawActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
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
