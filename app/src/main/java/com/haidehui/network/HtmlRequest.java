package com.haidehui.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.haidehui.common.Urls;
import com.haidehui.model.AccountBookAward1B;
import com.haidehui.model.AccountBookAward3B;
import com.haidehui.model.AccountBookCommission1B;
import com.haidehui.model.AccountBookWithdraw1B;
import com.haidehui.model.AccountBookWithdraw2B;
import com.haidehui.model.HotHouse1B;
import com.haidehui.model.InvestmentGuide1B;
import com.haidehui.model.ResultCheckVersionBean;
import com.haidehui.model.ResultCycleIndexContent1B;
import com.haidehui.model.ResultLoginOffBean;
import com.haidehui.model.ResultMyBankListBean;
import com.haidehui.model.ResultSentSMSBean;
import com.haidehui.network.http.SimpleHttpClient;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.MD5;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlRequest extends BaseRequester {

    public static String getResult(Map<String, Object> param) {
        Gson gson = new Gson();
        String str_md5 = gson.toJson(param);
        String md5 = MD5.stringToMD5(str_md5);
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
            String encrypt = gson.toJson(map);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getResultLinked(LinkedHashMap<String, Object> param) {
        Gson gson = new Gson();
        String str_md5 = gson.toJson(param);
        String md5 = MD5.stringToMD5(str_md5);
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
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
                    ResultSentSMSBean b = json.fromJson(result, ResultSentSMSBean.class);
                    return b.getData();

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
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

                if (isCancelled()||result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultMyBankListBean b = json.fromJson(data, ResultMyBankListBean.class);
                    return b.getData();

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }finally {
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

                if (isCancelled()||result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }finally {
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

                if (isCancelled()||result == null) {
                    return null;
                }
                try {
                    String data = DESUtil.decrypt(result);
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(data, ResultSentSMSBean.class);
                    return b.getData();

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }finally {
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
     * 获取收支消息列表
     *
     * @param context
     * @param listener
     * @return
     */
    public static void sentMessageInfo(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MESSAGE_INFO;

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
                    ResultSentSMSBean b = json.fromJson(result, ResultSentSMSBean.class);
                    return b.getData();

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                } finally {
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


                if (isCancelled() || result == null) {
                    return null;
                }
                ResultLoginOffBean b = json.fromJson(result, ResultLoginOffBean.class);
                return b.getData();
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
    public static void getCycleIndex(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
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
                Log.i("hh", "首页轮播图数据:" + result);

                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    result = DESUtil.decrypt(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                ResultCycleIndexContent1B b = gson.fromJson(result, ResultCycleIndexContent1B.class);
                return b.getData();
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
    public static void getDiscoveryCycleIndex(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
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
                    result = DESUtil.decrypt(result);
                    Log.i("hh", "轮播图数据:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                ResultCycleIndexContent1B b = gson.fromJson(result, ResultCycleIndexContent1B.class);
                return b.getData();
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
    public static void getInvestmentGuideListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
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
                    result = DESUtil.decrypt(result);
                    Log.i("hh", "投资指南列表数据:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                InvestmentGuide1B b = gson.fromJson(result, InvestmentGuide1B.class);
                return b.getData();
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 首页-- 精品房源数据
    public static void getBoutiqueHouseData(final Context context, Map<String, Object> param, OnRequestListener listener) {
//        final String data = getResult(param);
//        final String url = Urls.URL_CYCLE_ADVERTISE_INDEX;
//
//        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
//            @Override
//            public Object doTask(BaseParams params) {
//                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
//                HttpEntity entity = null;
//                try {
//                    entity = new StringEntity(data);
//                } catch (UnsupportedEncodingException e1) {
//                    e1.printStackTrace();
//                }
//                client.post(url, entity);
//                String result = (String) client.getResult();
//                if (isCancelled() || result == null) {
//                    return null;
//                }
//                Gson gson = new Gson();
//                ResultCycleIndexContent1B b = gson.fromJson(result, ResultCycleIndexContent1B.class);
//                return b.getData();
//            }
//
//            @Override
//            public void onPostExecute(Object result, BaseParams params) {
//                params.result = result;
//                params.sendResult();
//            }
//
//        });
    }

    // 首页-- 最热房源列表数据
    public static void getHotHouseData(final Context context, Map<String, Object> param, OnRequestListener listener) {
//        final String data = getResult(param);
//        final String url = Urls.URL_CYCLE_ADVERTISE_INDEX;
//
//        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
//            @Override
//            public Object doTask(BaseParams params) {
//                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
//                HttpEntity entity = null;
//                try {
//                    entity = new StringEntity(data);
//                } catch (UnsupportedEncodingException e1) {
//                    e1.printStackTrace();
//                }
//                client.post(url, entity);
//                String result = (String) client.getResult();
//                if (isCancelled() || result == null) {
//                    return null;
//                }
//                Gson gson = new Gson();
//                HotHouse1B b = gson.fromJson(result, HotHouse1B.class);
//                return b.getData();
//            }
//
//            @Override
//            public void onPostExecute(Object result, BaseParams params) {
//                params.result = result;
//                params.sendResult();
//            }
//
//        });
    }


    public static void checkVersion(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CHECKVERSION;
//        final String data = HtmlLoadUtil.checkVersion(type);
//        final String url = Urls.URL_CHECKVERSION;

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
                Log.i("hh", "后台数据:" + result);
                if (isCancelled() || result == null) {
                    return null;
                }
                Gson gson = new Gson();
                ResultCheckVersionBean b = gson.fromJson(result, ResultCheckVersionBean.class);
//                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
//                Map<String, Object> map = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
//                }.getType());
//                VersionMo b = (VersionMo) map.get("data");
//
//                Map<String, Object> data = (Map<String, Object>) map.get("data");
//                String listStr = (String) data.get("list");
//                ArrayList<VersionMo> list = gson.fromJson(listStr, new TypeToken<ArrayList<VersionMo>>() {
//                }.getType());
                return b.getData();
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
    public static void getMineData(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
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
                    ResultCheckVersionBean b = gson.fromJson(data, ResultCheckVersionBean.class);
                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
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
    public static void getAccountBookData(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
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
    public static void getCommissionList(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
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
                    return  null;
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
    public static void getAwardList(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
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
                    return  null;
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
    public static void getWithdrawList(final Context context, LinkedHashMap<String, Object> param/*String type*/, OnRequestListener listener) {
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

}
