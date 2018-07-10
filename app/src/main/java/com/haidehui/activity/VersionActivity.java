package com.haidehui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.uitls.SystemInfo;
import com.haidehui.widget.TitleBar;

/**
 * 版本号
 * Created by hasee on 2017/6/12.
 */
public class VersionActivity extends BaseActivity{

    private TextView tv_version_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_version);
        initTopTitle();
        initView();
        initData();

    }

    public void initView(){

        tv_version_code = (TextView) findViewById(R.id.tv_version_code);

    }

    public void initData(){

        tv_version_code.setText("版本号：" + SystemInfo.sVersionName);

    }

    private void initTopTitle() {

        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.setting_version))
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

}
