package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 事业合伙人一级推荐列表
public class OneLevelRecommendation1B implements IMouldType {
	private String check;
	private String code;
	private OneLevelRecommendation2B data;
	private String msg;

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public OneLevelRecommendation2B getData() {
		return data;
	}

	public void setData(OneLevelRecommendation2B data) {
		this.data = data;
	}
}
