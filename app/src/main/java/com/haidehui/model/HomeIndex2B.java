package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

// 首页
public class HomeIndex2B implements IMouldType {
	private MouldList<HomeIndex3B> list;
	private String title;
	private String bid; // 公告id
	private String guideId; // 最新的资讯id(新增)
	private String message; //
	private boolean flag;

	public MouldList<HomeIndex3B> getList() {
		return list;
	}

	public void setList(MouldList<HomeIndex3B> list) {
		this.list = list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getGuideId() {
		return guideId;
	}

	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
