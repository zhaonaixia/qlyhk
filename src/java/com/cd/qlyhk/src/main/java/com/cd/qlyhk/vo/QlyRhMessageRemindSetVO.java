package com.cd.qlyhk.vo;

import java.util.Date;
/**
 * 消息提醒设置VO类
 * @author li
 *
 */
public class QlyRhMessageRemindSetVO {
	// 用户ID
	private int user_id;
	// 谁看我提醒设置
	private String remind_type;
	// 订阅文章推送提醒设置
	private String push_type;
	// 修改时间
	private Date modify_datetime;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getRemind_type() {
		return remind_type;
	}
	public void setRemind_type(String remind_type) {
		this.remind_type = remind_type;
	}
	public String getPush_type() {
		return push_type;
	}
	public void setPush_type(String push_type) {
		this.push_type = push_type;
	}
	public Date getModify_datetime() {
		return modify_datetime;
	}
	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}
	
}
