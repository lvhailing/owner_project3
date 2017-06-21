package com.haidehui.model;


import com.haidehui.network.types.IMouldType;
import com.haidehui.network.types.MouldList;

public class ResultRecommendRecordContentBean implements IMouldType {

	private String total;

	private MouldList<ResultRecommendRecordItemContentBean>	recommendList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public MouldList<ResultRecommendRecordItemContentBean> getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(MouldList<ResultRecommendRecordItemContentBean> recommendList) {
		this.recommendList = recommendList;
	}
}