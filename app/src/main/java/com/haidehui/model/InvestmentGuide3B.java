package com.haidehui.model;


import com.haidehui.network.types.IMouldType;

// 发现--投资指南列表  接收后台返回数据
public class InvestmentGuide3B implements IMouldType {
	private String id;
	private String title; // 标题
	private String graphicDetails; //  图文详情
	private String source; //  来源
	private String picture; //  封面图片
	private String briefIntroduction; //  图文详情

	public String getBriefIntroduction() {
		return briefIntroduction;
	}

	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGraphicDetails() {
		return graphicDetails;
	}

	public void setGraphicDetails(String graphicDetails) {
		this.graphicDetails = graphicDetails;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
}
