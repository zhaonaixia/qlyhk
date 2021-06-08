package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 企业发布文章DTO类
 * 
 * @author sailor
 *
 */
public class QlyRhReleaseArticleDTO {

	// 文章URL
	private String article_url;

	// 文章标题
	private String article_title;
	
	// 分享配文
	private String share_text;

	// 用户openId
	private String openId;

	private int company_id;
	
	/** 分页对象 */
	private PageQueryVO pageQueryVO;

	public String getArticle_url() {
		return article_url;
	}

	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

	public String getShare_text() {
		return share_text;
	}

	public void setShare_text(String share_text) {
		this.share_text = share_text;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

}
