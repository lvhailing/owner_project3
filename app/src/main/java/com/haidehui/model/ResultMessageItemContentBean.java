package com.haidehui.model;

import com.haidehui.network.types.IMouldType;

/**
 * 消息列表item bean
 * Created by hasee on 2017/6/8.
 */

public class ResultMessageItemContentBean implements IMouldType{

    /**
     * 公告
     */
    private String title;       //  公告标题
    private String description;     //  内容
    private String sendTime;        //  发送时间
    private String bulletinId;          //  公告编号
    private String readState;          //  是否阅读（yes:已阅读；no:未阅读）


    /**
     * 账本消息
     */
    private String busiPriv;        //
    private String busiType;        //
    private String content;         //
    private String createTime;      //
    private String id;          //
    private String status;      //
    private String topic;      //


    public String getBusiPriv() {
        return busiPriv;
    }

    public void setBusiPriv(String busiPriv) {
        this.busiPriv = busiPriv;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }
}
