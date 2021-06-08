package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 修改文章内容请求DTO
 * 
 * @author sailor
 *
 */
public class ReqReptileArticleDetailDTO {

	// 文章的UUID
	private String uuid;
	// 文章标题
	private String article_title;
	// 封面图片URL
	private String pic_url;
	// 文章内容
	private String article_content;
	// 描述
	private String description;
	// 修改人
	private String modify_user;
	// 修改时间
	private Date modify_datetime;

	private String openId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public Date getModify_datetime() {
		return modify_datetime;
	}

	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
