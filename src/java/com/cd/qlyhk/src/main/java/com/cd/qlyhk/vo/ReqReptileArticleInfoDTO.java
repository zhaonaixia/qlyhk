package com.cd.qlyhk.vo;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

/**
 * 新增或修改文章信息请求DTO
 * 
 * @author sailor
 *
 */
public class ReqReptileArticleInfoDTO {

	// 文章的UUID
	private String uuid;
	// 文章标题
	private String article_title;
	// 文章封面图片
	private MultipartFile articleImg;

	private String categoryId;
	// 文章内容
	private String article_content;
	// 置顶（1表示是 0表示否）
	private String istop;
	
	// 分享配文
	private String share_text;
	
	private String openId;
	
	// 修改人
	private String modify_user;
	// 修改时间
	private Date modify_datetime;
	
	private String filePath;
	private String fileUrlPath;

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

	public MultipartFile getArticleImg() {
		return articleImg;
	}

	public void setArticleImg(MultipartFile articleImg) {
		this.articleImg = articleImg;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrlPath() {
		return fileUrlPath;
	}

	public void setFileUrlPath(String fileUrlPath) {
		this.fileUrlPath = fileUrlPath;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getShare_text() {
		return share_text;
	}

	public void setShare_text(String share_text) {
		this.share_text = share_text;
	}

}
