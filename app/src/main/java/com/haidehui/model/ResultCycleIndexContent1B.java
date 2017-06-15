package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

// 首页、发现轮播图共用
public class ResultCycleIndexContent1B implements IMouldType {
	private String check;
	private String code;
	private MouldList<ResultCycleIndex2B> data;
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

	public MouldList<ResultCycleIndex2B> getData() {
		return data;
	}

	public void setData(MouldList<ResultCycleIndex2B> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
