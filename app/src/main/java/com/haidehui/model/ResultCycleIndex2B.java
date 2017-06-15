package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 首页、发现轮播图共用 (用于接收后台返回图片地址)
public class ResultCycleIndex2B implements IMouldType {
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}