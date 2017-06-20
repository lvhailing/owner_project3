package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultMessageInfoContentBean implements IMouldType {

	private String countNum;			//	账本未读消息数量
	private String bulletNum;			//	公告未读消息数量
	private String othersNum;			//	其它未读消息数量
	private String totalNum;			//	用户未读消息总数

	public String getCountNum() {
		return countNum;
	}

	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}

	public String getBulletNum() {
		return bulletNum;
	}

	public void setBulletNum(String bulletNum) {
		this.bulletNum = bulletNum;
	}

	public String getOthersNum() {
		return othersNum;
	}

	public void setOthersNum(String othersNum) {
		this.othersNum = othersNum;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
}