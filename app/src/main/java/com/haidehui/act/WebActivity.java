package com.haidehui.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.uitls.ShareUtil;


public class WebActivity extends Activity implements View.OnClickListener {
    private WebView mWebview;
    private String type = null;
    private String url = null;
//    public static final String WEBTYPE_INVESTMENT_GUIDE_DETAILS = "investment_guide_details"; // 投资指南详情
//    public static final String WEBTYPE_ROADSHOW_DETAILS = "roadshow_details "; // 路演详情
    public static final String WEBTYPE_NOTICE = "noticedetail "; // 公告详情/其他消息详情
    public static final String WEBTYPE_SERVICE_AGREEMENT = "service_agreement "; // 服务协议
    public static final String WEBTYPE_SIGN_AGREEMENT = "sign_agreement "; // 海德汇协议
    public static final String WEBTYPE_ABOUT_US = "about_us "; // 关于我们
    public static final String WEBTYPE_VERSION = "version "; // 版本号
    public static final String WEBTYPE_PROJECT_MATERIAL_DETAIL = "project_material_detail "; //项目材料

    public String title;
    private TextView tv_web_title; // 标题
    private ImageView iv_back; // 返回按钮
    private ImageView iv_btn_share; // 分享按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        initView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initView() {

        ActivityStack stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        mWebview = (WebView) findViewById(R.id.webview_web);
        tv_web_title = (TextView) findViewById(R.id.tv_web_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_btn_share = (ImageView) findViewById(R.id.iv_btn_share);

        iv_back.setOnClickListener(this);
        iv_btn_share.setOnClickListener(this);

        mWebview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        mWebview.getSettings().setUseWideViewPort(true);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(new MyJavaScriptinterface(), "click");


        if (type.equals(WEBTYPE_NOTICE)) { // 公告详情
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_SERVICE_AGREEMENT)) {
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_ABOUT_US)) { // 关于我们
            tv_web_title.setText(getIntent().getExtras().getString("title"));

        } else if (type.equals(WEBTYPE_SIGN_AGREEMENT)) { // 海德汇协议
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_VERSION)) { // 版本号
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_PROJECT_MATERIAL_DETAIL)) {
            tv_web_title.setText(getIntent().getExtras().getString("title"));

        }


        HtmlRequest.synCookies(this, url);

        mWebview.loadUrl(url);

    }

    public class MyJavaScriptinterface {
        @JavascriptInterface
        public void result() {
            /*if (type.equals(WEBTYPE_WITHDRAW)) {
                setResult(RESULT_OK);
			} */
            WebActivity.this.finish();
        }

        @JavascriptInterface
        public void login() {
//            if (type.equals(WEBTYPE_ADVERTIS_2)) {
//                Intent i_login = new Intent();
//                i_login.setClass(WebActivity.this, LoginActivity.class);
//                startActivity(i_login);
//            }
//            WebActivity.this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebview.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack stack = ActivityStack.getActivityManage();
        stack.removeActivity(this);
    }
}
