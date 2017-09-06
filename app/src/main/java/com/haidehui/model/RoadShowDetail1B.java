package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 发现--路演详情
public class RoadShowDetail1B implements IMouldType {
	private String check;
	private String code;
	private RoadShowDetail2B data;
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

	public RoadShowDetail2B getData() {
		return data;
	}

	public void setData(RoadShowDetail2B data) {
		this.data = data;
	}
}
