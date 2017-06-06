package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class HotHouse2B implements IMouldType {
    private MouldList<HotHouse3B> list;
    private String flag;
    private String msg;
    private int count;

    public MouldList<HotHouse3B> getList() {
        return list;
    }

    public void setList(MouldList<HotHouse3B> list) {
        this.list = list;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}