package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class ExplainOrder2B implements IMouldType {
    private MouldList<ExplainOrder3B> list;
    private String count;
    private String flag;
    private String msg;

    public MouldList<ExplainOrder3B> getList() {
        return list;
    }

    public void setList(MouldList<ExplainOrder3B> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

}