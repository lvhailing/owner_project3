package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 发现--投资指南列表
 */
public class InvestmentGuide2B implements IMouldType {
    private MouldList<InvestmentGuide3B> list;
    private int count;

    public MouldList<InvestmentGuide3B> getList() {
        return list;
    }

    public void setList(MouldList<InvestmentGuide3B> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

	
