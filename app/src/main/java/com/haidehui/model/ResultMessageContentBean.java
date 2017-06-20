package com.haidehui.model;

import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 消息列表
 * Created by hasee on 2017/6/8.
 */

public class ResultMessageContentBean implements IMouldType{

    private MouldList<ResultMessageItemContentBean> list;

    public MouldList<ResultMessageItemContentBean> getList() {
        return list;
    }

    public void setList(MouldList<ResultMessageItemContentBean> list) {
        this.list = list;
    }
}
