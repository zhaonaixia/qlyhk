package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 我的分享VO
 * 
 * @author sailor
 *
 */
public class QlyRhShareVO {

	// 分享ID
	private int share_id;
	// UUID
	private String uuid;
	// 文章的UUID
	private String article_uuid;
	// 文章的标题
	private String article_title;
	// 文章的URL
	private String article_url;
	// 分享者
	private String sharer;
	// 文章来源（可能是分享别人的文章）
	private String source;
	// 创建人
	private int create_user_id;
	// 创建时间
	private Date create_time;
	// 文章来源的UUID标题
	private String source_share_uuid;
	// 分享日期（yyyy-MM-dd）
	private String share_date;
	
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

	public int getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(int create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getSource_share_uuid() {
		return source_share_uuid;
	}

	public void setSource_share_uuid(String source_share_uuid) {
		this.source_share_uuid = source_share_uuid;
	}

	public String getShare_date() {
		return share_date;
	}

	public void setShare_date(String share_date) {
		this.share_date = share_date;
	}

}
