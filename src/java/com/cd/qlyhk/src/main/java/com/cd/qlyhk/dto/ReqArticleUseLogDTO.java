package com.cd.qlyhk.dto;

/**
 * 文章采用记录请求DTO
 * 
 * @author sailor
 *
 */
public class ReqArticleUseLogDTO {

	// 文章的UUID
	private String uuid;
	// 文章标题
	private String article_title;
	// 文章状态
	private String status;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
