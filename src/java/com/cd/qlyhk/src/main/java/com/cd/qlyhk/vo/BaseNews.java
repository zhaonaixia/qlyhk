package com.cd.qlyhk.vo;

public class BaseNews {

	private String touser;
	
	private String msgtype;
	
	// 客服消息
	private Kfnews news;

	public Kfnews getNews() {
		return news;
	}

	public void setNews(Kfnews news) {
		this.news = news;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	
	
}
