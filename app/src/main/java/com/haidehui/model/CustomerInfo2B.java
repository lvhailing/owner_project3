package com.haidehui.model;


import com.haidehui.bean.ResultCustomerInfolistBean;
import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class CustomerInfo2B implements IMouldType {
    private MouldList<ResultCustomerInfolistBean> list;
    private String userInfoId;
    private String flag;
    private String msg;

    public MouldList<ResultCustomerInfolistBean> getList() {
        return list;
    }

    public void setList(MouldList<ResultCustomerInfolistBean> list) {
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

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }
}