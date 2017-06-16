package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class ResultMyBankListContentBean implements IMouldType {

	private String count;
	private MouldList<ResultMyBankListContentItemBean>	list;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public MouldList<ResultMyBankListContentItemBean> getList() {
		return list;
	}

	public void setList(MouldList<ResultMyBankListContentItemBean> list) {
		this.list = list;
	}
}