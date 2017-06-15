package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 发现--投资指南列表
public class InvestmentGuide1B implements IMouldType {
	private String check;
	private String code;
	private InvestmentGuide2B data;
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

	public InvestmentGuide2B getData() {
		return data;
	}

	public void setData(InvestmentGuide2B data) {
		this.data = data;
	}
}
