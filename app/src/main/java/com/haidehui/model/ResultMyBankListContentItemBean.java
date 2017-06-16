package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

public class ResultMyBankListContentItemBean implements IMouldType {

	private String bankCardNum;		//	银行卡号
	private String bankName;		//	银行名称
	private String id;		//	银行id
	private String realName;		//	用户姓名

	public String getBankCardNum() {
		return bankCardNum;
	}

	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}