package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultRecommendInfoBean implements IMouldType {
	private String check;
	private String code;
	private ResultRecommendInfoContentBean data;
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

	public ResultRecommendInfoContentBean getData() {
		return data;
	}

	public void setData(ResultRecommendInfoContentBean data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
