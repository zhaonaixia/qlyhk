package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 分享的文章阅读情况VO
 * 
 * @author sailor
 *
 */
public class QlyRhShareRecordVO {

	// id
	private int id;
	// 分享ID
	private int share_id;
	private String uuid;
	// 访客ID（openId）
	private String visitor_id;
	// 访客名称
	private String user_name;
	// 头像
	private String headImgUrl;
	// 分享者
	private String sharer;
	// 阅读的分享文章
	private String share_uuid;
	// 文章的标题
	private String article_title;
	// 文章的URL
	private String article_url;
	// 访问时间
	private Date visit_date;
	// 阅读时间
	private Date quit_date;
	// 阅读时长
	private int read_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShare_id() {
		return share_id;
	}

	public void setShare_id(int share_id) {
		this.share_id = share_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVisitor_id() {
		return visitor_id;
	}

	public void setVisitor_id(String visitor_id) {
		this.visitor_id = visitor_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public String getShare_uuid() {
		return share_uuid;
	}

	public void setShare_uuid(String share_uuid) {
		this.share_uuid = share_uuid;
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

	public Date getVisit_date() {
		return visit_date;
	}

	public void setVisit_date(Date visit_date) {
		this.visit_date = visit_date;
	}

	public Date getQuit_date() {
		return quit_date;
	}

	public void setQuit_date(Date quit_date) {
		this.quit_date = quit_date;
	}

	public int getRead_time() {
		return read_time;
	}

	public void setRead_time(int read_time) {
		this.read_time = read_time;
	}

}
