package com.cd.qlyhk.dto;

/**
 * 热门文章推送设置DTO
 * @author li
 *
 */
public class PushTypeDTO {
	// 推荐热门资讯(0表示不勾选 1表示勾选)
	private String rec_hot_news;
	// 你订阅的文章(0表示不勾选 1表示勾选)
	private String rec_sub_article;
	public String getRec_hot_news() {
		return rec_hot_news;
	}
	public void setRec_hot_news(String rec_hot_news) {
		this.rec_hot_news = rec_hot_news;
	}
	public String getRec_sub_article() {
		return rec_sub_article;
	}
	public void setRec_sub_article(String rec_sub_article) {
		this.rec_sub_article = rec_sub_article;
	}
	
}
