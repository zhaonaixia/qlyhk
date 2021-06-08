package com.cd.qlyhk.vo;

/**
 * 阅读记录VO类
 * 
 * @author sailor
 *
 */
public class WxRecordDTO {

	private String uuid;
	private int share_id;
	private String visitor_id;
	private String user_name;
	private String headImgUrl;
	private String sharer;
	private String article_id;
	private String article_title;
	private String article_url;
	private String rh_articleId;
	//阅读日期（yyyy-MM-dd）
	private String read_date;
	
	// 访客分享
	private String visitor_sharer;
	
	private String appId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getShare_id() {
		return share_id;
	}

	public void setShare_id(int share_id) {
		this.share_id = share_id;
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

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
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

	public String getRh_articleId() {
		return rh_articleId;
	}

	public void setRh_articleId(String rh_articleId) {
		this.rh_articleId = rh_articleId;
	}

	public String getRead_date() {
		return read_date;
	}

	public void setRead_date(String read_date) {
		this.read_date = read_date;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVisitor_sharer() {
		return visitor_sharer;
	}

	public void setVisitor_sharer(String visitor_sharer) {
		this.visitor_sharer = visitor_sharer;
	}

}
