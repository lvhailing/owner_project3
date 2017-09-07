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
import com.haidehui.model.InvestmentGuideDetail2B;
import com.haidehui.model.RoadShowDetail2B;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.uitls.ActivityStack;
import com.haidehui.uitls.ShareUtil;

import java.util.HashMap;


public class WebForShareActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;
    private String type = null;
    private String url = null;
    public static final String WEBTYPE_INVESTMENT_GUIDE_DETAILS = "investment_guide_details"; // 投资指南详情
    public static final String WEBTYPE_ROADSHOW_DETAILS = "roadshow_details "; // 路演详情
    public static final String WEBTYPE_HTML = "html "; // h5网页
    public static final String WEBTYPE_PROJECT_MATERIAL_DETAIL = "project_material_detail "; // 项目材料预览


    public String title;
    private TextView tv_web_title; // 标题
    private ImageView iv_back; // 返回按钮
    private ImageView iv_btn_share; // 分享按钮
    private InvestmentGuideDetail2B investMentGuideDetail;
    private String investMentGuideTitle; // 投资指南分享时的标题
    public String investMentGuideBrief; // 投资指南分享时的简介
    private RoadShowDetail2B roadShowDetail;
    private String roadShowDetailTitle; // 标题
    private String speaker; // 演讲嘉宾
    private String roadShowTime; // 发布时间
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        initView();

        id = getIntent().getExtras().getString("id");
        requestInvestmentGuideDetailData();
        requestRoadShowDetailData();
    }

    /**
     * 获取投资指南详情（返回分享时所需的标题、简介）
     */
    private void requestInvestmentGuideDetailData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("id", getIntent().getExtras().getString("id"));

        HtmlRequest.getInvestmentGuideDetailData(this, param, new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    investMentGuideDetail = (InvestmentGuideDetail2B) params.result;
                    if (investMentGuideDetail != null) {
                        investMentGuideTitle = investMentGuideDetail.getTitle();
                        investMentGuideBrief = investMentGuideDetail.getBrief();
                    }
                }
            }
        });
    }

    /**
     * 获取路演详情（返回分享时所需的标题、简介）
     */
    private void requestRoadShowDetailData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("id", getIntent().getExtras().getString("id"));
        HtmlRequest.getRoadShowDetailData(this, param, new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                if (params != null) {
                    roadShowDetail = (RoadShowDetail2B) params.result;
                    if (roadShowDetail != null) {
                        roadShowDetailTitle = roadShowDetail.getTitle();
                        speaker = roadShowDetail.getSpeaker();
                        roadShowTime = roadShowDetail.getRoadShowTime();
                    }
                }
            }
        });
    }


    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initView() {

        ActivityStack stack = ActivityStack.getActivityManage();
        stack.addActivity(this);

        mWebView = (WebView) findViewById(R.id.webview_web);
        tv_web_title = (TextView) findViewById(R.id.tv_web_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_btn_share = (ImageView) findViewById(R.id.iv_btn_share);

        iv_back.setOnClickListener(this);
        iv_btn_share.setOnClickListener(this);

        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new MyJavaScriptinterface(), "click");


        if (type.equals(WEBTYPE_INVESTMENT_GUIDE_DETAILS)) { // 投资指南详情
            iv_btn_share.setVisibility(View.VISIBLE);
            url = Urls.URL_INVESTMENTGUIDE_DETAIL + "/" + getIntent().getExtras().getString("id");
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 路演详情
            iv_btn_share.setVisibility(View.VISIBLE);
            url = Urls.URL_ROADSHOWVIDEO_VIEW + getIntent().getExtras().getString("id");
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_HTML)) { // 打开H5网页

        } else if (type.equals(WEBTYPE_PROJECT_MATERIAL_DETAIL)) {
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        }


        HtmlRequest.synCookies(this, url);

        mWebView.loadUrl(url);

    }

//    private void loadPDF1(){
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setPluginsEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.requestFocus();
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        String pdfUrl = "http:xxx.pdf";
//        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdfUrl);
//
//    }

    public class MyJavaScriptinterface {
        @JavascriptInterface
        public void result() {
            /*if (type.equals(WEBTYPE_WITHDRAW)) {
                setResult(RESULT_OK);
			} */
            WebForShareActivity.this.finish();
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
            case R.id.iv_btn_share: // 分享按钮
                String url = null;
                if (type.equals(WEBTYPE_INVESTMENT_GUIDE_DETAILS)) { // 投资指南详情
                    url = Urls.URL_INVESTMENTGUIDE_DETAIL + "/" + id;
                    ShareUtil.sharedSDK(this, investMentGuideTitle, investMentGuideBrief, url);
                } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 路演详情
                    String text = speaker + roadShowTime;
                    url = Urls.URL_ROADSHOWVIDEO_VIEW +id;
                    ShareUtil.sharedSDK(this, roadShowDetailTitle, text, url);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack stack = ActivityStack.getActivityManage();
        stack.removeActivity(this);
    }
}
