package com.haidehui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
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
import com.haidehui.widget.TitleBar;


/**
 * 意见反馈
 * Created by hasee on 2017/6/12.
 */
public class AdviceActivity extends BaseActivity implements View.OnClickListener{

    private EditText advice_edt;        //
    private TextView tv_advice_call_phone;      //
    private TitleBar title;
    private String content = "";
    private Button btn_advice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_advice);
        initTopTitle();
        initView();


    }

    private void initTopTitle() {
        title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))

                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
//                .addAction(new TitleBar.Action(2, 0, R.color.blue_light),AdviceActivity.this.getResources().getString(R.string.setting_change_phone_third_submit),R.drawable.shape_center_gray)
                .setCenterText(getResources().getString(R.string.setting_advice))
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

//                Toast.makeText(AdviceActivity.this,"////************",Toast.LENGTH_SHORT).show();
//                Intent i_second = new Intent(AdviceActivity.this,SettingChangePhoneSecondActivity.class);
//                startActivity(i_second);


            }
        });
    }

    private void requestData() {

        content = advice_edt.getText().toString();


        ArrayMap<String, Object> param = new ArrayMap<>();


        param.put("userId", userId);
        param.put("content", content);

        HtmlRequest.advice(AdviceActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                ResultSentSMSContentBean b = (ResultSentSMSContentBean) params.result;
                if (b != null) {
                    if (Boolean.parseBoolean(b.getFlag())) {

                        Toast.makeText(AdviceActivity.this,
                                b.getMsg(), Toast.LENGTH_LONG)
                                .show();
                        finish();
                    } else {

                        Toast.makeText(AdviceActivity.this,
                                b.getMsg(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AdviceActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void initView(){

        advice_edt = (EditText) findViewById(R.id.advice_edt);
        tv_advice_call_phone = (TextView) findViewById(R.id.tv_advice_call_phone);
        btn_advice = (Button) findViewById(R.id.btn_advice);

        tv_advice_call_phone.setOnClickListener(this);
        btn_advice.setOnClickListener(this);
        btn_advice.setClickable(false);

        advice_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*if(TextUtils.isEmpty(editable)){
                    title.setChildBankground(R.drawable.shape_center_gray,false);

                }else{
                    title.setChildBankground(R.drawable.shape_center_orange,true);

                }*/

                if(TextUtils.isEmpty(editable)){
                    btn_advice.setClickable(false);
                    btn_advice.setBackgroundResource(R.drawable.shape_center_gray);
                }else{
                    btn_advice.setClickable(true);
                    btn_advice.setBackgroundResource(R.drawable.shape_center_orange);
                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

            case R.id.tv_advice_call_phone:

                Intent i_phone = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:"+getString(R.string.phone_number)));
                i_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_phone);

                break;

            case R.id.btn_advice:

                requestData();

                break;

            default:

                break;

        }


    }
}
