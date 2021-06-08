package com.cd.qlyhk.vo;

import java.util.Date;

public class QlyRhSubscribeVO {
	// 订阅ID
	private int subscribe_id;
	// 用户ID
	private int user_id;
	// 类别ID
	private int category_id;
	// 修改时间
	private Date modify_datetime;
	
	public int getSubscribe_id() {
		return subscribe_id;
	}
	public void setSubscribe_id(int subscribe_id) {
		this.subscribe_id = subscribe_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public Date getModify_datetime() {
		return modify_datetime;
	}
	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}
	
}
