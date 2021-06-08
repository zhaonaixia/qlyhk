package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 用户点击文章详情记录表VO
 * 
 * @author sailor
 *
 */
public class QlyRhUserClickArticleVO {

	// id
	private int id;
	// 分享者
	private String sharer;
	// 阅读的分享文章
	private String share_uuid;
	// 文章的标题
	private String article_title;
	// 设备唯一标识
	private String unique_identification;
	// IP地址
	private String ip_address;
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

	public String getUnique_identification() {
		return unique_identification;
	}

	public void setUnique_identification(String unique_identification) {
		this.unique_identification = unique_identification;
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

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

}
