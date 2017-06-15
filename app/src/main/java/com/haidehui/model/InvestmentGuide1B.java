package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

// 发现--投资指南列表
public class InvestmentGuide1B implements IMouldType {
	private String check;
	private String code;
	private MouldList<InvestmentGuide3B> data;
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

	public MouldList<InvestmentGuide3B> getData() {
		return data;
	}

	public void setData(MouldList<InvestmentGuide3B> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
