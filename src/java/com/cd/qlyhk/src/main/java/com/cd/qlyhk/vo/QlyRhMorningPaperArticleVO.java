package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 财税早报文章VO类
 * @author li
 *
 */
public class QlyRhMorningPaperArticleVO {
	// ID
	private int id;
	// 文章UUID
	private String article_uuid;
	// 文章标题
	private String article_title;
	// 文章URL
	private String article_url;
	// 文章来源
	private String source;
	// 财税早报日期
	private String mp_date;
	// 排序序号
	private int order_index;
	// 创建人
	private String create_user;
	// 创建时间
	private Date create_datetime;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMp_date() {
		return mp_date;
	}
	public void setMp_date(String mp_date) {
		this.mp_date = mp_date;
	}
	public int getOrder_index() {
		return order_index;
	}
	public void setOrder_index(int order_index) {
		this.order_index = order_index;
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
