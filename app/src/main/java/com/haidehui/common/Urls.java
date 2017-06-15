package com.haidehui.common;


public class Urls {
    //测试环境
    public static String URL_DEBUG = "http://192.168.1.86:82/";
    // 正式环境IP
    public static String URL_OFFICIAL = "https://jdhapp.cf360.com/";
    //代树理
    public static final String URL_DSL = "http://192.168.1.106:9999/junde-hui-app/";

    //张殿洋
    public static final String URL_ZDY = "192.168.1.138:9999/junde-hui-app";

    //张亚磊
    public static final String URL_ZYL = "192.168.1.193:9999/junde-hui-app";

    // 邢玉洁
    public static final String URL_XYJ = "http://192.168.1.125:9999/junde-hui-app/";

    // 调试，上线时只需改此处环境即可
    private static String EC_HOST = URL_DEBUG;

    /**
     * 登出
     */
    public static final String URL_LOGINOFF = EC_HOST + "account/user/logoff";

    /**
     * 收支消息列表
     */
    public static final String URL_MESSAGE_INFO = EC_HOST + "";

    /**
     * 登陆
     */

    public static final String URL_LOGIN = EC_HOST + "android/user/login";
    /**
     * 发送手机短信
     */
    public static final String URL_SMS = EC_HOST
            + "user/mobile/send/verifycode";


    /**
     * 短信类型
     *
     */
    public static final String REGISTER = "register";       //  用户注册

    public static final String LOGINRET = "loginRet";       //  登录密码找回

    public static final String MOBILEBIND = "mobileBind";       //  绑定手机

    public static final String MOBILEEDIT = "mobileEdit";       //  修改手机



    //首页
    public static final String URL_HOME = EC_HOST + "index";
    // 轮播图
    public static final String URL_CYCLE_ADVERTISE_INDEX = EC_HOST + "turn/advertise";
    //意见反馈
    public static final String URL_ADVICE = EC_HOST + "problem/reply/save";
    //检查版本
    public static final String URL_CHECKVERSION = EC_HOST + "version/check";
}
