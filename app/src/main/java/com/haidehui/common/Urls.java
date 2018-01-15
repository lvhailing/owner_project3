package com.haidehui.common;


public class Urls {
    //测试环境
    public static String URL_DEBUG = "http://192.168.1.34:83/";

    // 外网测试环境
    public static String URL_OUTER_NET_DEBUG = "http://123.126.102.219:21083/";

    // 正式环境IP
//    public static String URL_OFFICIAL = "https://hwapp.cf360.com/";
    public static String URL_OFFICIAL = "https://app.jdjufu.com/";  //( 再发版时用这个新的正式地址)

    //代树理
    public static final String URL_DSL = "http://192.168.1.106:9999/overseas-app/";

    //张亚磊
    public static final String URL_ZYL = "http://192.168.1.193:9999/overseas-app/";

    // 邢玉洁
    public static final String URL_XYJ = "http://192.168.1.125:9999/overseas-app/";

    // 张殿洋
    public static final String URL_BB = "http://192.168.1.138:9999/overseas-app/";

    // 冯艳敏
    public static final String URL_FYM = "http://192.168.1.164:9998/overseas-app/";

    // 调试，上线时只需改此处环境即可
    private static String EC_HOST = URL_DEBUG;

    /**
     * 登出
     */
    public static final String URL_LOGINOFF = EC_HOST + "account/logoff";

    /**
     * url
     */
    public static final String URL = EC_HOST + "";

    /**
     * 获取消息主页信息
     */
    public static final String URL_MESSAGEINFO = EC_HOST + "message/getUnreadCount";

    /**
     * 公告消息列表
     */
    public static final String URL_MESSAGE_NOTICE = EC_HOST + "message/bulletin/list";

    /**
     * 收支消息列表（账本）
     */
    public static final String URL_MESSAGE_INFO_LIST = EC_HOST + "user/message/list";

    /**
     * 登陆
     */

    public static final String URL_LOGIN = EC_HOST + "android/login";
    /**
     * 发送手机短信
     */
    public static final String URL_SMS = EC_HOST + "user/mobile/send/verifycode";
    /**
     * 版本更新
     */
    public static final String URL_VERSION_CHECK = EC_HOST + "version/check";
    /**
     * 意见反馈
     */
    public static final String URL_ADAVICE = EC_HOST + "problemFeedback/save";
    /**
     * 找回密码
     */
    public static final String URL_FINDPASSWORD = EC_HOST + "password/find";
    /**
     * 修改手机号
     */
    public static final String URL_CHANGEPHONE = EC_HOST + "account/user/password/valid";

    /**
     * 修改手机号三
     */
    public static final String URL_CHANGEPHONE_THIRD = EC_HOST + "account/mobile/verify";
    /**
     * 注册
     */
    public static final String URL_SIGNUP = EC_HOST + "android/register";

    /**
     * 注册协议
     */
    public static final String URL_SIGNUP_WEB_AGREEMENT = EC_HOST + "register/agreement";
    /**
     * 修改登录密码
     */
    public static final String URL_CHANGEPASSWORD = EC_HOST + "account/password/modify";
    /**
     * 获取推荐主页信息
     */
    public static final String URL_GETRECOMMENDINFO = EC_HOST + "account/recommendAppTo";
    /**
     * 获取邀请记录信息
     */
    public static final String URL_GETRECOMMEND_RECORD = EC_HOST + "account/recommendList";
    /**
     * 我的银行卡
     */
    public static final String URL_GETBANKLIST = EC_HOST + "account/bankcard/list";

    /**
     * 删除银行卡
     */
    public static final String URL_DELETEBANKLIST = EC_HOST + "bankcard/del";
    /**
     * 添加银行卡
     */
    public static final String URL_ADDBANKLIST = EC_HOST + "bankcard/add";

    /**
     * 公告详情
     */
    public static final String URL_NOTICEDETAIL = EC_HOST + "message/bulletin/detail?bulletinId=";

    /**
     * 其他消息详情
     */
    public static final String URL_OTHERDETAIL = EC_HOST + "message/others/";

    // 设置--服务协议
    public static final String URL_SERVICE_AGREEMENT = EC_HOST + "register/agreement";


    // 设置--关于我们
    public static final String URL_ABOUT_US = EC_HOST + "about/us";

    // 设置--版本号
    public static final String URL_VERSION = EC_HOST + "android/version?num=";

    public static final String URL_VIEW_PDF = EC_HOST + "view/pdf?name=";


    /**
     * 获取确认提现信息
     */
    public static final String URL_GETWITHDRAWINFO = EC_HOST + "account/withdrawcash/cashnum";

    /**
     * 确认提现
     */
    public static final String URL_WITHDRAWCONFIRM = EC_HOST + "withdrawcash/add";

    /**
     * 短信类型
     */
    public static final String REGISTER = "register";       //  用户注册

    public static final String LOGINRET = "loginRet";       //  登录密码找回

    public static final String MOBILEBIND = "mobileBind";       //  绑定手机

    public static final String MOBILEEDIT = "mobileEdit";       //  修改手机

    public static final String ADDBANKCARD = "bankCardBind";       //  添加银行卡

    public static final String WITHDRAW = "withdrawCashNum";       //  提现


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
    public static final String URL_INDEX = EC_HOST + "index";

    // 首页--轮播图
    public static final String URL_HOME_ADVERTISE = EC_HOST + "home/advertise";

    // 首页--最热房源列表
    public static final String URL_INDEX_HOTLIST = EC_HOST + "index/hotlist";

    // 首页--海外项目列表
    public static final String URL_PROJECT_LIST = EC_HOST + "project/list";

    // 房源详情
    public static final String URL_HOUSE_DETAIL = EC_HOST + "house/detail";

    // 房源详情 分享
    public static final String URL_HOUSE_H5_DETAIL = EC_HOST + "house/h5/detail";

    // 海外项目详情
    public static final String URL_PROJECT_DETAIL = EC_HOST + "project/detail";

    // 海外项目详情 分享
    public static final String URL_PROJECT_H5_DETAIL = EC_HOST + "project/h5/detail/";

    // 房源
    public static final String URL_HOUSE_LIST = EC_HOST + "house/list";

    // 发现-- 轮播图
    public static final String URL_DISCOVERY_ADVERTISE = EC_HOST + "discovery/advertise";

    // 发现-- 投资指南列表
    public static final String URL_INVESTMENTGUIDE_LIST = EC_HOST + "investmentguide/list";

    // 发现-- 投资指南详情
    public static final String URL_INVESTMENTGUIDE_DETAIL = EC_HOST + "investmentguide/detail"; // 只用于加载H5
    public static final String URL_INVESTMENTGUIDE_DETAIL_APP = EC_HOST + "investmentguide/detail/app";// 返回分享时所需的标题和简介

    // 发现-- 路演列表
    public static final String URL_ROADSHOWVIDEO_LIST = EC_HOST + "roadshowvideo/list";

    // 发现-- 路演详情
    public static final String URL_ROADSHOWVIDEO_VIEW = EC_HOST + "roadshowvideo/view/"; // 只用于加载H5
    public static final String URL_ROADSHOWVIDEO_DETAIL = EC_HOST + "roadshowvideo/detail"; // 返回分享时所需的标题和简介


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

    //客户信息列表
    public static final String URL_CUSTOMER_INFO_LIST = EC_HOST + "account/trade/customer/list";

    //佣金收益详情
    public static final String URL_COMMISSION_DETAILS = EC_HOST + "account/userrewardrecord/commission/view";

    //提现记录详情
    public static final String URL_WITHDRAW_DETAILS = EC_HOST + "withdrawcash/view";

    //活动奖励详情
    public static final String URL_AWARD_DETAILS = EC_HOST + "account/userrewardrecord/reward/view";

    //置业顾问认证
    public static final String URL_PARTNER_IDENTIFY = EC_HOST + "account/user/checkInfo";

    //提交置业顾问认证
    public static final String URL_SUBMIT_PARTNER_IDENTIFY = EC_HOST + "account/user/checkInfoSubmit";

    //上传图片（事业合伙人认证）
    public static final String URL_SUBMIT_PHOTO = EC_HOST + "android/account/photo/upload";

    //客户信息详情
    public static final String URL_CUSTOMER_INFO_DETAILS = EC_HOST + "account/trade/customer/detail";

    //新增客户信息
    public static final String URL_ADD_CUSTOMER_INFO = EC_HOST + "account/trade/customer/add/save";

    //修改客户信息
    public static final String URL_EDIT_CUSTOMER_INFO = EC_HOST + "account/trade/customer/edit/save";

    //删除客户信息
    public static final String URL_DELETE_CUSTOMER_INFO = EC_HOST + "account/trade/customer/delete";

    //认购列表
    public static final String URL_GET_RENGOU = EC_HOST + "account/trade/subscribe/list";

    //认购详情
    public static final String URL_GET_RENGOU_DETAILS = EC_HOST + "account/trade/subscribe/detail";

    //客户跟踪列表
    public static final String URL_TRACKING_LIST = EC_HOST + "account/trade/customers/tracking/list";

    //客户跟踪详情
    public static final String URL_TRACKING_DETAILS = EC_HOST + "account/trade/customer/tracking";

    //新增客户跟踪
    public static final String URL_SUBMIT_TRACKING = EC_HOST + "account/trade/customer/tracking/save";

    //修改客户跟踪
    public static final String URL_EDIT_TRACKING = EC_HOST + "account/trade/customer/tracking/edit";

    //删除客户跟踪
    public static final String URL_DELETE_TRACKING = EC_HOST + "account/trade/customers/tracking/delete";

    //获取我的信息
    public static final String URL_MINE_DATA = EC_HOST + "account/userInfo";

    //保存姓名
    public static final String URL_SAVE_NAME = EC_HOST + "account/realName/modify";

    // 验证用户是否打开APP
    public static final String URL_APP_OPEN = EC_HOST + "android/app/open";

   //我的事业合伙人  一级推荐列表
    public static final String URL_ACCOUNT_RECOMMEND_LIST = EC_HOST + "account/myRecommendList";

   //预约说明会-列表
    public static final String URL_ACCOUNT_CUSTOMER_APPOINTMENT_LIST = EC_HOST + "account/customer/appointment/list";

    //预约说明会新增客户信息
    public static final String URL_ACCOUNT_CUSTOMER_APPOINTMENT_ADD_SAVE = EC_HOST + "account/customer/appointment/add/save";

    //预约说明会删除客户信息
    public static final String URL_ACCOUNT_CUSTOMER_APPOINTMENT_DELETE = EC_HOST + "account/customer/appointment/delete";
}