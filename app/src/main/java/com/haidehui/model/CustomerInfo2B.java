package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class CustomerInfo2B implements IMouldType {
    private MouldList<CustomerInfo3B> list;
    private String flag;
    private String msg;

    public MouldList<CustomerInfo3B> getList() {
        return list;
    }

    public void setList(MouldList<CustomerInfo3B> list) {
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

}