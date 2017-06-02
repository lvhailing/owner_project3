package com.haidehui.network;

import android.content.Context;

import java.util.HashMap;

public class BaseParams {
    public Context context;
    public BaseRequester.OnRequestListener requestListener;
//    public int from;
    public String url;
    private HashMap<String, Object> mMap = new HashMap<>();
    public Object result;

    public void sendResult() {
        context = null;
        if (requestListener != null) {
            requestListener.onRequestFinished(this);
        }
    }

    public BaseParams put(String key, Object value) {
        if (key != null) {
            mMap.put(key, value);
        }
        return this;
    }

    public Object get(String key) {
        return mMap.get(key);
    }
}
