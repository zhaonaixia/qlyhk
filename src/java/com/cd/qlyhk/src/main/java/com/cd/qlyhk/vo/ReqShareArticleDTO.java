package com.cd.qlyhk.vo;

/**
 * 文章分享请求DTO
 * 
 * @author sailor
 *
 */
public class ReqShareArticleDTO {

	// 分享文章的UUID
	private String uuid;
	// 获客文章的UUID
	private String article_id;
	// 文章的标题
	private String article_title;
	// 文章的URL
	private String article_url;
	// 分享者
	private String sharer;
	// 文章来源（可能是分享别人的文章）
	private String source;
	// 获客文章的UUID
	private String article_uuid;
	
	private String openId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public String getArticle_url() {
		return article_url;
	}

	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
	}

}
