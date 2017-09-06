package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

/**
 * 发现--投资指南详情
 */
public class InvestmentGuideDetail2B implements IMouldType {
    private String title; // 标题
    private String brief; // 简介

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}

	
