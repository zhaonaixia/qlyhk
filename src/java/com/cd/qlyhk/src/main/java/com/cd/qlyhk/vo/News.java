package com.cd.qlyhk.vo;

/**
 * 图文消息VO类
 * @author sailor
 *
 */
public class News {

	private String title; // 标题
	private String description; // 描述
	private String url; // 该图文的点击跳转链接
	private String picurl; // 图片的URL

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}
