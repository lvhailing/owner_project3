package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 发现--产品路演列表  接收后台返回数据
public class ProductRoadshow3B implements IMouldType {
	private String id;
	private String count; // 产品总数
	private String videoName; //  路演视频名称
	private String speaker; //  演讲嘉宾
	private String videoPicture; //  视频图片
	private String editTime; //  发布时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getVideoPicture() {
		return videoPicture;
	}

	public void setVideoPicture(String videoPicture) {
		this.videoPicture = videoPicture;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
}
