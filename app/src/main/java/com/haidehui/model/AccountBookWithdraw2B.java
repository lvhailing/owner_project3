package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class AccountBookWithdraw2B implements IMouldType {
    private MouldList<AccountBookWithDraw3B> list;
    private String flag;
    private String msg;

    public MouldList<AccountBookWithDraw3B> getList() {
        return list;
    }

    public void setList(MouldList<AccountBookWithDraw3B> list) {
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