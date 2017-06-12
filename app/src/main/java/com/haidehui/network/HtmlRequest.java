package com.haidehui.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.haidehui.common.Constants;
import com.haidehui.common.Urls;
import com.haidehui.model.HotHouse1B;
import com.haidehui.model.ResultCheckVersionBean;
import com.haidehui.model.ResultCycleIndexContent1B;
import com.haidehui.model.ResultLoginOffBean;
import com.haidehui.model.ResultSentSMSBean;
import com.haidehui.model.VersionMo;
import com.haidehui.network.http.SimpleHttpClient;
import com.haidehui.network.types.IMouldType;
import com.haidehui.uitls.DESUtil;
import com.haidehui.uitls.MD5;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HtmlRequest extends BaseRequester {

    public static String getResult(Map<String, Object> param) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(param));
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

    /**
     * 发送手机短信
     *
     * @param context
     * @param listener
     * @return
     */
    public static void sentSMS(final Context context, Map<String, Object> param, OnRequestListener listener) {
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

                if (isCancelled()||result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(result, ResultSentSMSBean.class);
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
    public static void sentMessageInfo(final Context context, Map<String, Object> param, OnRequestListener listener) {
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

                if (isCancelled()||result == null) {
                    return null;
                }
                try {
                    Gson json = new Gson();
                    ResultSentSMSBean b = json.fromJson(result, ResultSentSMSBean.class);
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
     * 登出
     *
     * @param context 上下文
     * @param listener  监听事件
     *
     */
    public static void loginoff(final Context context, Map<String, Object> param, OnRequestListener listener) {
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
    public static void getCycleIndex(final Context context, Map<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CYCLE_ADVERTISE_INDEX;

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
//                ResultCycleIndexContent1B b = gson.fromJson(result, ResultCycleIndexContent1B.class);
                return null;
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
        final String data = getResult(param);
        final String url = Urls.URL_CYCLE_ADVERTISE_INDEX;

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
    // 首页-- 最热房源列表数据
    public static void getHotHouseData(final Context context, Map<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CYCLE_ADVERTISE_INDEX;

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
                Gson gson = new Gson();
                HotHouse1B b = gson.fromJson(result, HotHouse1B.class);
                return b.getData();
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    public static void checkVersion(final Context context, Map<String, Object> param/*String type*/, OnRequestListener listener) {
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
     * @param context
     * @param param
     * @param listener
     */
    public static void getMineData(final Context context, Map<String, Object> param/*String type*/, OnRequestListener listener) {
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


}
