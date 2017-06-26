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
import android.widget.TextView;
import android.widget.Toast;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.IdCardCheckUtils;
import com.haidehui.uitls.NumUtils;
import com.haidehui.uitls.PreferenceUtil;
import com.haidehui.uitls.StringUtil;
import com.haidehui.uitls.ViewUtils;
import com.haidehui.widget.TitleBar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 添加银行卡
 * Created by hasee on 2017/6/14.
 */
public class AddBankActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_add_bank_get_verify_code;       //  获取验证码
    private EditText et_add_bank_verify_code;           //  验证码
    private TextView tv_add_bank_phone_mes;             //
    private TextView tv_add_bank_real_name;         //  真实姓名
    private TextView tv_add_bank_idcard;            //  身份证号码
    private EditText et_bank_name;          //  银行卡名称
    private EditText et_bank_address;       //  开户行所在地
    private EditText et_bank_num;           //  银行卡号
    private Button btn_add_bankcard;        //  确认添加

    private String verifyCode = "";
    private String realName = "";
    private String idCard = "";
    private String bankName = "";
    private String bankAddress = "";
    private String bankNum = "";

    private boolean smsflag = true;
    private boolean flag = true;
    private MyHandler mHandler;
    private String btnString;
    private int time = 60;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_add_bank);
        initTopTitle();
        initView();
        initData();

    }

    public void initData(){

        tv_add_bank_phone_mes.setText("请输入"+ StringUtil.replaceSubString(phone)+"收到的短信验证码");

//        verifyCode = et_add_bank_verify_code.getText().toString();
//        realName = et_add_bank_real_name.getText().toString();
//        idCard = et_add_bank_idcard.getText().toString();
//        bankName = et_bank_name.getText().toString();
//        bankAddress = et_bank_address.getText().toString();
//        bankNum = et_bank_num.getText().toString();
    }

    public void initView(){

        context = this;
        tv_add_bank_get_verify_code = (TextView) findViewById(R.id.tv_add_bank_get_verify_code);
        et_add_bank_verify_code = (EditText) findViewById(R.id.et_add_bank_verify_code);
        tv_add_bank_phone_mes = (TextView) findViewById(R.id.tv_add_bank_phone_mes);
        tv_add_bank_real_name = (TextView) findViewById(R.id.tv_add_bank_real_name);
        tv_add_bank_idcard = (TextView) findViewById(R.id.tv_add_bank_idcard);
        et_bank_name = (EditText) findViewById(R.id.et_bank_name);
        et_bank_address = (EditText) findViewById(R.id.et_bank_address);
        et_bank_num = (EditText) findViewById(R.id.et_bank_num);
        btn_add_bankcard = (Button) findViewById(R.id.btn_add_bankcard);

        tv_add_bank_get_verify_code.setOnClickListener(this);
        btn_add_bankcard.setOnClickListener(this);

        mHandler = new MyHandler();
        btnString = getResources().getString(R.string.sign_getsms_again);

        try {

            realName = DESUtil.decrypt(PreferenceUtil.getUserRealName());
            idCard = DESUtil.decrypt(PreferenceUtil.getIdNo());

        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_add_bank_real_name.setText(realName);
        tv_add_bank_idcard.setText(idCard);

        checkNull();

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.mybank_add_card))
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

    private void requestSMS() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("busiType", Urls.ADDBANKCARD);
        param.put("userId", userId);


        HtmlRequest.sentSMS(AddBankActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {
                        Toast.makeText(AddBankActivity.this, "短信发送成功",
                                Toast.LENGTH_LONG).show();
                        smsflag = true;
                        startThread();
                    } else {
                        smsflag = false;
                        Toast.makeText(AddBankActivity.this,
                                b.getMessage(), Toast.LENGTH_LONG)
                                .show();
                        tv_add_bank_get_verify_code.setClickable(true);
                    }
                } else {
                    tv_add_bank_get_verify_code.setClickable(true);
                    Toast.makeText(AddBankActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void addBankCard() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("bankAddress", bankAddress);
        param.put("bankCardNum", bankNum);
        param.put("bankName", bankName);
        param.put("idNo", idCard);
        param.put("realName", realName);
        param.put("userId", userId);
        param.put("validateCode", verifyCode);

        HtmlRequest.addBankCard(AddBankActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (b.getFlag().equals("true")) {
                        finish();
                    }
                    Toast.makeText(AddBankActivity.this,
                            b.getMessage(), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(AddBankActivity.this, "加载失败，请确认网络通畅",
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

            case R.id.tv_add_bank_get_verify_code:
                tv_add_bank_get_verify_code.setClickable(false);
                requestSMS();
//                smsflag = true;
//                startThread();


                break;

            case R.id.btn_add_bankcard:

//                if(IdCardCheckUtils.isIdCard(idCard)){
                    if(NumUtils.checkBankCard(bankNum)){
                        addBankCard();
                    }else{
                        Toast.makeText(context,"请输入正确银行卡号",Toast.LENGTH_SHORT).show();
                    }
//                }else{
//                    Toast.makeText(context,"请输入正确身份证号码",Toast.LENGTH_SHORT).show();
//                }


                break;

            default:

                break;


        }

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
            tv_add_bank_get_verify_code.setClickable(true);
            tv_add_bank_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_add_bank_get_verify_code.setBackgroundResource(R.drawable.shape_center_orange);
            tv_add_bank_get_verify_code.setText(getResources().getString(
                    R.string.sign_getsms_again));
        } else if (time < 60) {
            tv_add_bank_get_verify_code.setClickable(false);
            tv_add_bank_get_verify_code.setBackgroundResource(R.drawable.shape_center_gray);
            tv_add_bank_get_verify_code
                    .setTextColor(getResources().getColor(R.color.txt_white));
            tv_add_bank_get_verify_code.setText(btnString+"("+time+")");

        }
    }

    public void checkNull(){

        //  监听验证码的事件
        et_add_bank_verify_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
                bankName = et_bank_name.getText().toString().trim();
                bankAddress = et_bank_address.getText().toString().trim();
                bankNum = et_bank_num.getText().toString().trim();

                ViewUtils.setButton(editable.toString(),realName,idCard,bankName,bankAddress,bankNum,btn_add_bankcard);



            }
        });

        //  监听真实姓名的事件
//        et_add_bank_real_name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
//                bankName = et_bank_name.getText().toString().trim();
//                bankAddress = et_bank_address.getText().toString().trim();
//                bankNum = et_bank_num.getText().toString().trim();
//
//                ViewUtils.setButton(verifyCode,editable.toString(),idCard,bankName,bankAddress,bankNum,btn_add_bankcard);
//
//            }
//        });
//
//        //  监听身份证的事件
//        et_add_bank_idcard.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
//                bankName = et_bank_name.getText().toString().trim();
//                bankAddress = et_bank_address.getText().toString().trim();
//                bankNum = et_bank_num.getText().toString().trim();
//
//                ViewUtils.setButton(verifyCode,realName,editable.toString(),bankName,bankAddress,bankNum,btn_add_bankcard);
//            }
//        });
        //  监听银行名称的事件
        et_bank_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
                bankName = et_bank_name.getText().toString().trim();
                bankAddress = et_bank_address.getText().toString().trim();
                bankNum = et_bank_num.getText().toString().trim();

                ViewUtils.setButton(verifyCode,realName,idCard,editable.toString(),bankAddress,bankNum,btn_add_bankcard);
            }
        });
        //  监听银行开户地事件
        et_bank_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
                bankName = et_bank_name.getText().toString().trim();
                bankAddress = et_bank_address.getText().toString().trim();
                bankNum = et_bank_num.getText().toString().trim();

                ViewUtils.setButton(verifyCode,realName,idCard,bankName,editable.toString(),bankNum,btn_add_bankcard);
            }
        });
        //  监听银行卡号事件
        et_bank_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                verifyCode = et_add_bank_verify_code.getText().toString().trim();
//                realName = et_add_bank_real_name.getText().toString().trim();
//                idCard = et_add_bank_idcard.getText().toString().trim();
                bankName = et_bank_name.getText().toString().trim();
                bankAddress = et_bank_address.getText().toString().trim();
                bankNum = et_bank_num.getText().toString().trim();

                ViewUtils.setButton(verifyCode,realName,idCard,bankName,bankAddress,editable.toString(),btn_add_bankcard);
            }
        });

    }

}
