package com.haidehui.network;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;
import com.haidehui.common.Urls;
import com.haidehui.model.AccountBookAward1B;
import com.haidehui.model.AccountBookCommission1B;
import com.haidehui.model.AccountBookWithdraw1B;
import com.haidehui.model.AwardDetails1B;
import com.haidehui.model.CommissionDetails1B;
import com.haidehui.model.CustomerDetails1B;
import com.haidehui.model.CustomerInfo1B;
import com.haidehui.model.ExplainOrder1B;
import com.haidehui.model.HomeIndex1B;
import com.haidehui.model.HotHouse1B;
import com.haidehui.model.HouseDetail1B;
import com.haidehui.model.HouseList1B;
import com.haidehui.model.InvestmentGuide1B;
import com.haidehui.model.InvestmentGuideDetail1B;
import com.haidehui.model.MineData1B;
import com.haidehui.model.OneLevelRecommendation1B;
import com.haidehui.model.OverseaProjectDetail1B;
import com.haidehui.model.OverseaProjectList1B;
import com.haidehui.model.PartnerIdentify1B;
import com.haidehui.model.ProductRoadshow1B;
import com.haidehui.model.RenGou1B;
import com.haidehui.model.RenGouDetails1B;
import com.haidehui.model.ResultCheckVersionBean;
import com.haidehui.model.ResultCycleIndexContent1B;
import com.haidehui.model.ResultLoginOffBean;
import com.haidehui.model.ResultMessageBean;
import com.haidehui.model.ResultMessageInfoBean;
import com.haidehui.model.ResultMyBankListBean;
import com.haidehui.model.ResultRecommendInfoBean;
import com.haidehui.model.ResultRecommendRecordBean;
import com.haidehui.model.ResultSentSMSBean;
import com.haidehui.model.ResultWithdrawInfoBean;
import com.haidehui.model.RoadShowDetail1B;
import com.haidehui.model.Splash1B;
import com.haidehui.model.SubmitCustomer1B;
import com.haidehui.model.SubmitPartnerIdentify1B;
import com.haidehui.model.Tracking1B;
import com.haidehui.model.TrackingDetails1B;
import com.haidehui.model.WithDrawDetails1B;
import com.haidehui.network.http.SimpleHttpClient;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.MD5;
import com.haidehui.uitls.PreferenceUtil;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class HtmlRequest extends BaseRequester {
    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        // cookieManager.removeSessionCookie();// 移除
        try {
            cookieManager.setCookie(url, DESUtil.decrypt(PreferenceUtil.getCookie()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 对入参进行升序排列
     *
     * @param map 无序的入参
     * @return （升序：如：a,b,c....）排序后的入参
     */
    public static Map<String, Object> sortMap(Map<String, Object> map) {
        if (map == null) {
            return new HashMap<>();
        } else if (map.isEmpty()) {
            return map;
        }

        Map<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        sortMap.putAll(map);

        return sortMap;
    }

    public static String getResult(Map<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
//        Log.i("hh", "排序后的入参为：" + sortMap);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);

        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
//            Log.i("hh", "传给后台的入参为：" + encrypt);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getResult(ArrayMap<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
//        Log.i("hh", "排序后的入参为：" + sortMap);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);

        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
//            Log.i("hh", "传给后台的入参为：" + encrypt);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getResultLinked(LinkedHashMap<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResultNoEncrypt(Map<String, Object> param) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(param));
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
            result = gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResultNoEncryptLinked(LinkedHashMap<String, Object> param) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(param));
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
            result = gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送手机短信
     *
     * @param context
     * @param listener
     * @return
     */
    public static void sentSMS(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SMS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 版本检查更新
     *
     * @param context  上线文
     * @param listener 监听事件
     */
    public static void checkVersion(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_VERSION_CHECK;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    ResultCheckVersionBean b = json.fromJson(result, ResultCheckVersionBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 意见反馈
     *
     * @param context
     * @param listener
     * @return
     */
    public static void advice(final Context context, ArrayMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ADAVICE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }


    /**
     * 找回密码
     *
     * @param context
     * @param listener
     * @return
     */
    public static void findPassword(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_FINDPASSWORD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }


    /**
     * 修改手机号一
     *
     * @param context
     * @param listener
     * @return
     */
    public static void changePhone(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CHANGEPHONE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 修改手机号三
     *
     * @param context
     * @param listener
     * @return
     */
    public static void changePhoneThird(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CHANGEPHONE_THIRD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 注册
     *
     * @param context
     * @param listener
     * @return
     */
    public static void signup(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SIGNUP;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 修改登录密码
     *
     * @param context
     * @param listener
     * @return
     */
    public static void changePassword(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CHANGEPASSWORD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }


    /**
     * 获取推荐主页信息
     *
     * @param context
     * @param listener
     * @return
     */
    public static void getRecommendInfo(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_GETRECOMMENDINFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    ResultRecommendInfoBean b = json.fromJson(data, ResultRecommendInfoBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 获取邀请记录信息
     *
     * @param context
     * @param listener
     * @return
     */
    public static void getRecommendRecord(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_GETRECOMMEND_RECORD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "获取邀请记录信息:" + data);
                    ResultRecommendRecordBean b = json.fromJson(data, ResultRecommendRecordBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 我的银行卡列表
     *
     * @param context
     * @param listener
     * @return
     */
    public static void getMyBankList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_GETBANKLIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultMyBankListBean b = json.fromJson(data, ResultMyBankListBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
//                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 删除银行卡
     *
     * @param context
     * @param listener
     * @return
     */
    public static void deleteBankList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_DELETEBANKLIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 新增银行卡
     *
     * @param context
     * @param listener
     * @return
     */
    public static void addBankCard(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResultLinked(param);
        final String url = Urls.URL_ADDBANKLIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 获取消息主页信息
     *
     * @param context
     * @param listener
     * @return
     */
    public static void getMessageInfo(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResultLinked(param);
        final String url = Urls.URL_MESSAGEINFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultMessageInfoBean b = json.fromJson(data, ResultMessageInfoBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }


    /**
     * 获取确认提现页面信息
     *
     * @param context
     * @param listener
     * @return
     */

    public static void getWithdrawInfo(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResultLinked(param);
        final String url = Urls.URL_GETWITHDRAWINFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultWithdrawInfoBean b = json.fromJson(data, ResultWithdrawInfoBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 确认提现
     *
     * @param context
     * @param listener
     * @return
     */

    public static void WithdrawConfirm(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResultLinked(param);
        final String url = Urls.URL_WITHDRAWCONFIRM;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {

                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 获取收支消息列表
     *
     * @param context
     * @param listener
     * @return
     */
    public static void sentMessageInfo(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MESSAGE_INFO_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultMessageBean b = json.fromJson(data, ResultMessageBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 获取公告消息列表
     *
     * @param context
     * @param listener
     * @return
     */
    public static void getMessageNotice(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MESSAGE_NOTICE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {

            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultMessageBean b = json.fromJson(data, ResultMessageBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 登出
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void loginoff(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_LOGINOFF;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);
                    ResultLoginOffBean b = json.fromJson(data, ResultLoginOffBean.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 首页-- 获取轮播图
     *
     * @param context  上下文
     * @param listener 监听
     * @return 返回数据
     */
    public static void getHomeCycleIndex(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_HOME_ADVERTISE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "首页轮播图:" + data);

                    Gson gson = new Gson();
                    ResultCycleIndexContent1B b = gson.fromJson(data, ResultCycleIndexContent1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 首页
     *
     * @param context  上下文
     * @param listener 监听
     * @return 返回数据
     */
    public static void getHomeData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INDEX;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "首页数据:" + data);

                    Gson gson = new Gson();
                    HomeIndex1B b = gson.fromJson(data, HomeIndex1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 首页-- 最热房源列表数据
    public static void getHotHouseData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INDEX_HOTLIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
                    Log.i("hh", "最热房源列表数据:" + result);

                    Gson gson = new Gson();
                    HotHouse1B b = gson.fromJson(result, HotHouse1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 首页-- 海外项目列表数据
    public static void getOverseaListData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_PROJECT_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
//                    Log.i("hh", "海外项目列表数据:" + result);

                    Gson gson = new Gson();
                    OverseaProjectList1B b = gson.fromJson(result, OverseaProjectList1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 房源详情页数据
    public static void getHouseDetailData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_HOUSE_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
                    Log.i("hh", "房源详情:" + result);

                    Gson gson = new Gson();
                    HouseDetail1B b = gson.fromJson(result, HouseDetail1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 海外项目详情页数据
    public static void getOverseaDetailData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_PROJECT_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
                    Log.i("hh", "海外项目详情:" + result);

                    Gson gson = new Gson();
                    OverseaProjectDetail1B b = gson.fromJson(result, OverseaProjectDetail1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 房源-- 房源列表数据
    public static void getHouseList(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_HOUSE_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
//                    Log.i("hh", "房源列表列表数据:" + result);

                    Gson gson = new Gson();
                    HouseList1B b = gson.fromJson(result, HouseList1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 发现-- 获取轮播图
     *
     * @param context  上下文
     * @param listener 监听
     * @return 返回数据
     */
    public static void getDiscoveryCycleIndex(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_DISCOVERY_ADVERTISE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
//                    Log.i("hh", "发现轮播图:" + data);

                    Gson gson = new Gson();
                    ResultCycleIndexContent1B b = gson.fromJson(data, ResultCycleIndexContent1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 发现-- 投资指南列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getInvestmentGuideListData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INVESTMENTGUIDE_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "投资指南列表数据:" + data);

                    Gson gson = new Gson();
                    InvestmentGuide1B b = gson.fromJson(data, InvestmentGuide1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取投资指南详情(接口)
     * @param context
     * @param param
     * @param listener
     */
    public static void getInvestmentGuideDetailData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INVESTMENTGUIDE_DETAIL_APP;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "投资指南详情数据:" + data);

                    Gson gson = new Gson();
                    InvestmentGuideDetail1B b = gson.fromJson(data, InvestmentGuideDetail1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 发现-- 路演列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getRoadShowListData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ROADSHOWVIDEO_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "路演列表数据:" + data);

                    Gson gson = new Gson();
                    ProductRoadshow1B b = gson.fromJson(data, ProductRoadshow1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    public static void getRoadShowDetailData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ROADSHOWVIDEO_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
//                    Log.i("hh", "路演详情数据:" + data);

                    Gson gson = new Gson();
                    RoadShowDetail1B b = gson.fromJson(data, RoadShowDetail1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 我的主页面
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getMineData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MINE_DATA;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
//                    Log.i("hh", "我的数据：" + data);
                    Gson gson = new Gson();
                    MineData1B b = gson.fromJson(data, MineData1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 账本主页数据
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getAccountBookData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_COMMISSION_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    AccountBookCommission1B b = gson.fromJson(data, AccountBookCommission1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 佣金收益列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getCommissionList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_COMMISSION_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    AccountBookCommission1B b = gson.fromJson(data, AccountBookCommission1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 活动奖励列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getAwardList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_AWARD_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    AccountBookAward1B b = gson.fromJson(data, AccountBookAward1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 提现记录列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getWithdrawList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_WITHDRAW_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    AccountBookWithdraw1B b = gson.fromJson(data, AccountBookWithdraw1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 客户信息列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getCustomerInfoList(final Context context, Map<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CUSTOMER_INFO_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    CustomerInfo1B b = gson.fromJson(data, CustomerInfo1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 佣金收益详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getCommissionDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_COMMISSION_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    CommissionDetails1B b = gson.fromJson(data, CommissionDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 提现记录详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getWithDrawDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_WITHDRAW_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    WithDrawDetails1B b = gson.fromJson(data, WithDrawDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 活动奖励详情详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getAwardDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_AWARD_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    AwardDetails1B b = gson.fromJson(data, AwardDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 获取   置业顾问认证
     *  （我的事业合伙人也调此接口）
     * @param context
     * @param param
     * @param listener
     */
    public static void getPartnerIdentify(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_PARTNER_IDENTIFY;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "事业合伙人认证:" + data);
                    Gson gson = new Gson();
                    PartnerIdentify1B b = gson.fromJson(data, PartnerIdentify1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 提交  置业顾问认证
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void submitPartnerIdentify(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SUBMIT_PARTNER_IDENTIFY;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "提交--事业合伙人认证:" + data);
                    Gson gson = new Gson();
                    SubmitPartnerIdentify1B b = gson.fromJson(data, SubmitPartnerIdentify1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 客户信息详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getCustomerInFoDetails(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CUSTOMER_INFO_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    CustomerDetails1B b = gson.fromJson(data, CustomerDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 新增客户信息
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getAddCustomerInFo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ADD_CUSTOMER_INFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 修改客户信息
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getEditCustomerInFo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_EDIT_CUSTOMER_INFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }
    /**
     * 删除客户跟踪
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void deleteCustomerTracking(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_DELETE_TRACKING;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }
    /**
     * 删除客户信息
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void deleteCustomerInFo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_DELETE_CUSTOMER_INFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 认购状态
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getRenGouState(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_GET_RENGOU;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    RenGou1B b = gson.fromJson(data, RenGou1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 认购详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getRenGouDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_GET_RENGOU_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    RenGouDetails1B b = gson.fromJson(data, RenGouDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 客户跟踪列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getTrackingList(final Context context, Map<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRACKING_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "客户跟踪列表:" + data);
                    Gson gson = new Gson();
                    Tracking1B b = gson.fromJson(data, Tracking1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 客户跟踪详情
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getTrackingDetails(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRACKING_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "客户跟踪详情:" + data);

                    Gson gson = new Gson();
                    TrackingDetails1B b = gson.fromJson(data, TrackingDetails1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 新增客户跟踪
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void submitTracking(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SUBMIT_TRACKING;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }
    /**
     * 修改客户跟踪
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void editTracking(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_EDIT_TRACKING;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }


    /**
     * 保存姓名
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void saveName(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SAVE_NAME;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     *  统计用户的登录
     * @param context
     * @param param  用户的id
     * @param listener
     */
    public static void openApp(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APP_OPEN;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Gson gson = new Gson();
                    Splash1B b = gson.fromJson(data, Splash1B.class);
                    return b;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     *  我的事业合伙人  一级推荐列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getOneLevelRecData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_RECOMMEND_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "一级推荐列表:" + data);

                    Gson gson = new Gson();
                    OneLevelRecommendation1B b = gson.fromJson(data, OneLevelRecommendation1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  预约说明会-列表
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getExplainOrderData(final Context context, Map<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_CUSTOMER_APPOINTMENT_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "预约说明会列表:" + data);
                    Gson gson = new Gson();
                    ExplainOrder1B b = gson.fromJson(data, ExplainOrder1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 预约说明会新增客户信息
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void getAddExplainOrderCustomerInFo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_CUSTOMER_APPOINTMENT_ADD_SAVE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "预约说明会新增客户调的接口：" + data);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 预约说明会-编辑
     * @param context
     * @param param
     * @param listener
     */
    public static void getEditExplainOrderCustomerInFo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_CUSTOMER_APPOINTMENT_EDIT_SAVE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "预约说明会修改客户信息时调的接口：" + data);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     * 预约说明会列表  删除客户信息
     *
     * @param context
     * @param param
     * @param listener
     */
    public static void deleteExplainOrderListItem(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_CUSTOMER_APPOINTMENT_DELETE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    String data = DESUtil.decrypt(result);
                    Log.i("hh", "预约说明会列表  删除客户信息：" + data);
                    Gson gson = new Gson();
                    SubmitCustomer1B b = gson.fromJson(data, SubmitCustomer1B.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }
}
