package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

// 事业合伙人一级推荐列表
public class OneLevelRecommendation2B implements IMouldType {
	private MouldList<OneLevelRecomendation3B> myRecommendList;
	private String total;

	public MouldList<OneLevelRecomendation3B> getMyRecommendList() {
		return myRecommendList;
	}

	public void setMyRecommendList(MouldList<OneLevelRecomendation3B> myRecommendList) {
		this.myRecommendList = myRecommendList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
