package com.cd.qlyhk.vo;

import java.util.Date;

public class QlyRhReptileQueueVO {
	// 文章ID
	private int id;
	// 类别ID
	private int category_id;
	// 文章URL
	private String content_url;
	// 是否已下载 1表示是 0表示否
	private String isload;
	// 创建者
	private String create_user;
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getContent_url() {
		return content_url;
	}

	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}

	public String getIsload() {
		return isload;
	}

	public void setIsload(String isload) {
		this.isload = isload;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
	}

	public Date getModify_datetime() {
		return modify_datetime;
	}

	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}

}
