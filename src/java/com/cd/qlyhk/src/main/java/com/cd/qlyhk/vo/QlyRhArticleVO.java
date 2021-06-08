package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.qlyhk.common.vo.PageQueryVO;

public class QlyRhArticleVO {
	// 文章ID
	private int article_id;
	// 类别ID
	private int category_id;
	// UUID
	private String uuid;
	// 文章标题
	private String article_title;
	// 文章URL
	private String article_url;
	// 文章图片
	private String pic_url;
	// 创建者
	private String creator;
	// 描述
	private String description;
	// 创建人
	private int create_user_id;
	// 创建时间
	private Date create_datetime;
	// 修改人
	private int modify_user_id;
	// 修改时间
	private Date modify_datetime;
	
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
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
	public String getArticle_url() {
		return article_url;
	}
	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(int create_user_id) {
		this.create_user_id = create_user_id;
	}
	public Date getCreate_datetime() {
		return create_datetime;
	}
	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
	}
	public int getModify_user_id() {
		return modify_user_id;
	}
	public void setModify_user_id(int modify_user_id) {
		this.modify_user_id = modify_user_id;
	}
	public Date getModify_datetime() {
		return modify_datetime;
	}
	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}
	
}
