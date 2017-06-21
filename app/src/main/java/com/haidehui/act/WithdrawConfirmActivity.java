package com.haidehui.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.adapter.WithdrawAdapter;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultMessageContentBean;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.model.ResultWithdrawInfoContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.network.types.MouldList;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提现--确认提现
 * Created by hasee on 2017/6/14.
 */
public class WithdrawConfirmActivity extends BaseActivity implements View.OnClickListener{

    private Context context;
    private TextView tv_bank_banknum;       //  银行卡号
    private TextView tv_bank_username;       //  银行卡用户姓名
    private TextView tv_bank_name;       //  银行名称
    private EditText et_withdraw_amount;        //  转出金额
    private TextView tv_withdraw_able;          //  可提现金额
    private TextView tv_withdraw_get_verify_code;       //  获取验证码
    private EditText et_withdraw_verify_code;       //  输入验证码
    private Button btn_withdraw_confirm;       //  申请提现
    private TextView tv_withdraw_reward;        //  奖励提示


    private boolean smsflag = true;
    private boolean flag = true;
    private MyHandler mHandler;
    private String btnString;
    private int time = 60;

    private String amount = "";
    private String verifyCode = "";
    private ResultWithdrawInfoContentBean b;

    private String bankAddress = "";        //      银行开户地
    private String bankCardId = "";        //      银行卡id
    private String bankCardNum = "";        //      银行卡号
    private String bankName = "";        //      银行名称
    private String realName = "";        //      姓名开户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_withdraw_confirm);
        initTopTitle();
        initView();
        initData();

    }

    public void initData(){

        getWithdrawInfo();

    }

    public void initView(){
        context = this;
        b =  new ResultWithdrawInfoContentBean();
        bankAddress = getIntent().getStringExtra("bankAddress");
        bankCardId = getIntent().getStringExtra("bankCardId");
        bankCardNum = getIntent().getStringExtra("bankCardNum");
        bankName = getIntent().getStringExtra("bankName");
        realName = getIntent().getStringExtra("realName");


        tv_bank_banknum = (TextView) findViewById(R.id.tv_bank_banknum);
        tv_bank_username = (TextView) findViewById(R.id.tv_bank_username);
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        et_withdraw_amount = (EditText) findViewById(R.id.et_withdraw_amount);
        tv_withdraw_able = (TextView) findViewById(R.id.tv_withdraw_able);
        tv_withdraw_get_verify_code = (TextView) findViewById(R.id.tv_withdraw_get_verify_code);
        et_withdraw_verify_code = (EditText) findViewById(R.id.et_withdraw_verify_code);
        btn_withdraw_confirm = (Button) findViewById(R.id.btn_withdraw_confirm);
        tv_withdraw_reward = (TextView) findViewById(R.id.tv_withdraw_reward);

        tv_withdraw_get_verify_code.setOnClickListener(this);
        btn_withdraw_confirm.setOnClickListener(this);

        mHandler = new MyHandler();
        btnString = getResources().getString(R.string.sign_getsms_again);

        checkNull();

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
        mHandler.removeCallbacks(myRunnable);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_withdraw_get_verify_code:

                requestSMS();
//                smsflag = true;
//                startThread();

                break;

            case R.id.btn_withdraw_confirm:

                WithdrawConfirm();

                break;

            default:

                break;


        }

    }


    private void getWithdrawInfo() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("userId", userId);

        HtmlRequest.getWithdrawInfo(WithdrawConfirmActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                b = (ResultWithdrawInfoContentBean) params.result;
                if (b != null) {

                    setView();

                } else {
                    Toast.makeText(WithdrawConfirmActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 确认提现
     *
     */
    private void WithdrawConfirm() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("bankAddress", bankAddress);
        param.put("bankCardId", bankCardId);
        param.put("bankCardNum", bankCardNum);
        param.put("bankName", bankName);
        param.put("cashNum", amount);
        param.put("realName", realName);
        param.put("userId", userId);
        param.put("validateCode", verifyCode);

        HtmlRequest.WithdrawConfirm(WithdrawConfirmActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                b = (ResultWithdrawInfoContentBean) params.result;
                if (b != null) {

                    setView();

                } else {
                    Toast.makeText(WithdrawConfirmActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void setView(){

        tv_withdraw_able.setText("可提现金额"+b.getCashNum()+"元");

        if(b.getRewardStatus().equals("init")){
            tv_withdraw_reward.setVisibility(View.VISIBLE);
        }else if(b.getRewardStatus().equals("sended")){
            tv_withdraw_reward.setVisibility(View.GONE);
        }else{
            tv_withdraw_reward.setVisibility(View.GONE);
        }


        tv_bank_banknum.setText(bankCardNum);
        tv_bank_username.setText(realName);
        tv_bank_name.setText(bankName);


    }





    private void requestSMS() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("busiType", Urls.WITHDRAW);
        param.put("userId", userId);

        HtmlRequest.sentSMS(WithdrawConfirmActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getResult())) {
                        Toast.makeText(WithdrawConfirmActivity.this, "短信发送成功",
                                Toast.LENGTH_LONG).show();
                        smsflag = true;
                        startThread();
                    } else {
                        smsflag = false;
                        Toast.makeText(WithdrawConfirmActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(WithdrawConfirmActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startThread() {
        if (smsflag) {
            Thread t = new Thread(myRunnable);
            flag = true;
            t.start();
        }
    }

    Runnable myRunnable = new Runnable() {

        @Override
        public void run() {
            while (flag) {
                Message msg = new Message();
                time -= 1;
                msg.arg1 = time;
                if (time == 0) {
                    flag = false;
                    mHandler.sendMessage(msg);
                    time = 60;
                    mHandler.removeCallbacks(myRunnable);
                } else {
                    mHandler.sendMessage(msg);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setButtonStyle(msg.arg1);
        }

    }
    private void setButtonStyle(int time) {
        if (time == 0) {
            tv_withdraw_get_verify_code.setClickable(true);
            tv_withdraw_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_withdraw_get_verify_code.setBackgroundResource(R.drawable.shape_center_orange);
            tv_withdraw_get_verify_code.setText(getResources().getString(
                    R.string.sign_getsms_again));
        } else if (time < 60) {
            tv_withdraw_get_verify_code.setClickable(false);
            tv_withdraw_get_verify_code.setBackgroundResource(R.drawable.shape_center_gray);
            tv_withdraw_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_withdraw_get_verify_code.setText(btnString+"("+time+")");

        }
    }

    public void checkNull(){

        et_withdraw_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                amount = et_withdraw_amount.getText().toString();
                verifyCode = et_withdraw_verify_code.getText().toString();
                ViewUtils.setButton(editable.toString(),verifyCode,btn_withdraw_confirm);
            }
        });

        et_withdraw_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                amount = et_withdraw_amount.getText().toString();
                verifyCode = et_withdraw_verify_code.getText().toString();
                ViewUtils.setButton(amount,editable.toString(),btn_withdraw_confirm);
            }
        });

    }


}
