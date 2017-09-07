package com.haidehui.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.model.InvestmentGuideDetail2B;
import com.haidehui.model.RoadShowDetail2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.uitls.BASE64Encoder;
import com.haidehui.uitls.ShareUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class WebForPdfActivity extends Activity implements View.OnClickListener {
    private WebView mWebview;
    private String type = null;
    private String url = null;
    public static final String WEBTYPE_PROJECT_MATERIAL_DETAIL = "project_material_detail "; // 项目材料预览


    public String title;
    private TextView tv_web_title; // 标题
    private ImageView iv_back; // 返回按钮
    private ImageView iv_btn_share; // 分享按钮
//    private InvestmentGuideDetail2B investMentGuideDetail;
//    private String investMentGuideTitle; // 投资指南分享时的标题
//    public String investMentGuideBrief; // 投资指南分享时的简介
//    private RoadShowDetail2B roadShowDetail;
//    private String roadShowDetailTitle; // 标题
//    private String speaker; // 演讲嘉宾
//    private String roadShowTime; // 发布时间
//    private String id;

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

        WebSettings settings = mWebview.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);

        mWebview.setWebViewClient(new WebViewClient());
        mWebview.setWebChromeClient(new WebChromeClient());

        if (!TextUtils.isEmpty(url)) {
            byte[] bytes = null;

            // 将url转换成utf-8格式的字符
            try {
                bytes = url.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // 转码并加载界面
            if (bytes != null) {
                String encodeUrl = new BASE64Encoder().encode(bytes);   // BASE64转码
                mWebview.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + encodeUrl);   //加载
            }
        }



         if (type.equals(WEBTYPE_PROJECT_MATERIAL_DETAIL)) {
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        }


        HtmlRequest.synCookies(this, url);

//        mWebview.loadUrl(url);

    }

    public class MyJavaScriptinterface {
        @JavascriptInterface
        public void result() {
            /*if (type.equals(WEBTYPE_WITHDRAW)) {
                setResult(RESULT_OK);
			} */
            WebForPdfActivity.this.finish();
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
            case R.id.iv_btn_share: // 分享按钮
//                String url = null;
//                if (type.equals(WEBTYPE_INVESTMENT_GUIDE_DETAILS)) { // 投资指南详情
//                    url = Urls.URL_INVESTMENTGUIDE_DETAIL + "/" + id;
//                    ShareUtil.sharedSDK(this, investMentGuideTitle, investMentGuideBrief, url);
//                } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 路演详情
//                    String text = speaker + roadShowTime;
//                    url = Urls.URL_ROADSHOWVIDEO_VIEW +id;
//                    ShareUtil.sharedSDK(this, roadShowDetailTitle, text, url);
//                }
//                break;
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
