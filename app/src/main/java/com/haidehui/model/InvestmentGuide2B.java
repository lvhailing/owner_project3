package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 服务--展示豪华邮轮列表
 */
public class InvestmentGuide2B implements IMouldType {
    private MouldList<InvestmentGuide3B> list;
    private String msg;
    private int count;

    public MouldList<InvestmentGuide3B> getList() {
        return list;
    }

    public void setList(MouldList<InvestmentGuide3B> list) {
        this.list = list;
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

	
