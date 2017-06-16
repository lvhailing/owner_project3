package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 发现--产品路演列表
 */
public class ProductRoadshow2B implements IMouldType {
    private MouldList<ProductRoadshow3B> list;
    private int count;

    public MouldList<ProductRoadshow3B> getList() {
        return list;
    }

    public void setList(MouldList<ProductRoadshow3B> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

	
