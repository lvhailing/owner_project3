package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class Message1B implements IMouldType {
	private String check;
	private String code;
	private Message2B data;
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

	public Message2B getData() {
		return data;
	}

	public void setData(Message2B data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
