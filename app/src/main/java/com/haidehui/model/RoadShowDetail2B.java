package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

/**
 * 发现--路演详情
 */
public class RoadShowDetail2B implements IMouldType {
    private String title; // 标题
    private String speaker; // 演讲嘉宾
    private String roadShowTime; // 发布时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getRoadShowTime() {
        return roadShowTime;
    }

    public void setRoadShowTime(String roadShowTime) {
        this.roadShowTime = roadShowTime;
    }
}

	
