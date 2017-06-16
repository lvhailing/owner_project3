package com.haidehui.common;


public class Urls {
    //测试环境
    public static String URL_DEBUG = "http://192.168.1.86:82/";
    // 正式环境IP
    public static String URL_OFFICIAL = "https://jdhapp.cf360.com/";

    //代树理
    public static final String URL_DSL = "http://192.168.1.106:9999/overseasProperty-app/";

    //张殿洋
    public static final String URL_ZDY = "192.168.1.138:9999/junde-hui-app";

    //张亚磊
    public static final String URL_ZYL = "http://192.168.1.193:9999/overseas-app";

    // 邢玉洁
    public static final String URL_XYJ = "http://192.168.1.125:9999/overseas-app/";

    // 张殿洋
    public static final String URL_BB = "http://192.168.1.138:9999/overseas-app/";

    // 调试，上线时只需改此处环境即可
    private static String EC_HOST = URL_DSL;

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
    public static final String URL_SMS = EC_HOST + "user/mobile/send/verifycode";

    /**
     * 我的银行卡
     */
    public static final String URL_GETBANKLIST = EC_HOST
            + "account/bankcard/list";

    /**
     * 删除银行卡
     */
    public static final String URL_DELETEBANKLIST = EC_HOST
            + "bankcard/del";
     /**
     * 添加银行卡
     */
    public static final String URL_ADDBANKLIST = EC_HOST
            + "bankcard/add";


    /**
     * 短信类型
     */
    public static final String REGISTER = "register";       //  用户注册

    public static final String LOGINRET = "loginRet";       //  登录密码找回

    public static final String MOBILEBIND = "mobileBind";       //  绑定手机

    public static final String MOBILEEDIT = "mobileEdit";       //  修改手机

    public static final String ADDBANKCARD = "bankCardBind";       //  添加银行卡


    // */ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态

    public static final String ACTIVITY_SPLASH = "activity_splash";
    public static final String ACTIVITY_GESEDIT = "activity_gesedit";
    public static final String ACTIVITY_GESVERIFY = "activity_gesverify";
    public static final String ACTIVITY_ACCOUNTSET = "activity_accountset";
    public static final String ACTIVITY_ACCOUNT = "activity_account";
    public static final String ACTIVITY_CHANGE_GESTURE = "activity_change_gesture";

    //首页
    public static final String URL_HOME = EC_HOST + "index";
    // 首页--轮播图
    public static final String URL_HOME_ADVERTISE = EC_HOST + "home/advertise";
    // 发现-- 轮播图
    public static final String URL_DISCOVERY_ADVERTISE = EC_HOST + "discovery/advertise";
    // 发现-- 投资指南列表
    public static final String URL_INVESTMENTGUIDE_LIST = EC_HOST + "investmentguide/list";
    //意见反馈
    public static final String URL_ADVICE = EC_HOST + "problem/reply/save";
    //检查版本
    public static final String URL_CHECKVERSION = EC_HOST + "version/check";
    //佣金列表 以及 账本url
    public static final String URL_COMMISSION_LIST = EC_HOST + "account/userrewardrecord/commission/list";
    //活动奖励列表
    public static final String URL_AWARD_LIST = EC_HOST + "account/userrewardrecord/reward/list";
    //提现列表
    public static final String URL_WITHDRAW_LIST = EC_HOST + "account/withdrawcash/list";

}
