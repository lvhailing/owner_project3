package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultMessageInfoBean implements IMouldType {
	private String check;
	private String code;
	private ResultMessageInfoContentBean data;
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

	public ResultMessageInfoContentBean getData() {
		return data;
	}

	public void setData(ResultMessageInfoContentBean data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
