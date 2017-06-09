package com.haidehui.model;

import com.haidehui.network.types.IMouldType;

/**
 * 消息列表item bean
 * Created by hasee on 2017/6/8.
 */

public class ResultMessageContentBean implements IMouldType{

    private String name;
    private String num;
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
