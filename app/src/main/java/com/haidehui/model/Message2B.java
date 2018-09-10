package com.haidehui.model;

import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 消息列表
 * Created by hasee on 2017/6/8.
 */

public class Message2B implements IMouldType{

    private MouldList<Message3B> list;

    public MouldList<Message3B> getList() {
        return list;
    }

    public void setList(MouldList<Message3B> list) {
        this.list = list;
    }
}
