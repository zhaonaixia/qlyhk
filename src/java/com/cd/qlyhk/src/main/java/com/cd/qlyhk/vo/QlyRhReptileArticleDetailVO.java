package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 文章内容VO类
 * 
 * @author sailor
 *
 */
public class QlyRhReptileArticleDetailVO {

	private int id;
	// UUID
	private String article_uuid;
	// 文章内容
	private String article_content;
	// 修改人
	private String modify_user;
	// 修改时间
	private Date modify_datetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
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

}
