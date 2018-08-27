package com.haidehui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
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
import com.haidehui.uitls.ShareUtil;

import java.util.HashMap;


public class WebForShareActivity extends Activity implements View.OnClickListener {
    private WebView mWebview;
    private String type = null;
    private String url = null;
    public static final String WEBTYPE_INVESTMENT_GUIDE_DETAILS = "investment_guide_details"; // 投资指南详情
    public static final String WEBTYPE_ROADSHOW_DETAILS = "roadshow_details "; // 路演详情
    public static final String WEBTYPE_HTML = "html "; // h5网页
    public static final String WEBTYPE_PROJECT_MATERIAL_DETAIL = "project_material_detail "; // 项目材料预览

    public String title;
    private TextView tv_web_title; // H5页显示的标题
    private ImageView iv_back; // 返回按钮
    private ImageView iv_btn_share; // 分享按钮

    private InvestmentGuideDetail2B investMentGuideDetail;
    private String investMentGuideTitle; // 投资指南分享时的标题
    public String investMentGuideBrief; // 投资指南分享时的简介

    private RoadShowDetail2B roadShowDetail;
    private String roadShowDetailTitle; // 路演的标题
    private String speaker; // 演讲嘉宾
    private String roadShowTime; // 发布时间
    private String id; //编号
    private String userId; // 用户Id

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);

        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        id = getIntent().getExtras().getString("id");
        userId = getIntent().getExtras().getString("uid");

        initView();

        // 发现-- 轮播图点击时根据不同的类型调取相应的数据
        if (type.equals(WEBTYPE_INVESTMENT_GUIDE_DETAILS)) { // 如果是投资指南则调投资指南的接口
            requestInvestmentGuideDetailData();
        } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 如果是产品路演则调路演的接口
            requestRoadShowDetailData();
        }
    }

    /**
     * 发现-- 轮播图点击跳转投资指南详情时调的接口
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
     * 发现-- 轮播图点击跳转路演详情页时调的接口
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

        //解决 WebView不支持加载Https
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });

        mWebview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        mWebview.getSettings().setUseWideViewPort(true);

        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.addJavascriptInterface(new MyJavaScriptinterface(), "click");

        /**
         * 解决Android 5.0以后，WebView默认不支持同时加载Https和Http混合模式，
         * 当一个安全的站点（https）去加载一个非安全的站点（http）时，需要配置Webview加载内容的混合模式
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        if (type.equals(WEBTYPE_INVESTMENT_GUIDE_DETAILS)) { // 投资指南详情
            iv_btn_share.setVisibility(View.VISIBLE);
            url = Urls.URL_INVESTMENTGUIDE_DETAIL + "/" + id+ "/" +"0"; //
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 路演详情
            iv_btn_share.setVisibility(View.VISIBLE);
            url = Urls.URL_ROADSHOWVIDEO_VIEW + id+ "/" +"0"; //
            tv_web_title.setText(getIntent().getExtras().getString("title"));
        } else if (type.equals(WEBTYPE_HTML)) { // 打开H5网页

        } else if (type.equals(WEBTYPE_PROJECT_MATERIAL_DETAIL)) { // 项目材料预览
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
                    url = Urls.URL_INVESTMENTGUIDE_DETAIL + "/" + id+ "/" + userId;
                    ShareUtil.sharedSDK(this, investMentGuideTitle, investMentGuideBrief, url);
                } else if (type.equals(WEBTYPE_ROADSHOW_DETAILS)) { // 路演详情
                    String text = speaker + roadShowTime; // 这个test是分享出去时显示的标题及内容
                    url = Urls.URL_ROADSHOWVIDEO_VIEW + id+ "/" + userId;
                    ShareUtil.sharedSDK(this, roadShowDetailTitle, text, url);
                }
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
