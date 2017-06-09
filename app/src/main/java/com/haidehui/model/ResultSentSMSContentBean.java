package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultSentSMSContentBean implements IMouldType {
	private String flag;
	private String result;
	private String message;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}