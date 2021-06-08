package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

public class QlyRhReptileArticleDTO {
	// 文章标题
	private String article_title;
	// 采集日期起（yyyy-MM-dd）
	private String collect_startDate;
	// 采集日期止（yyyy-MM-dd）
	private String collect_endDate;
	// 文章来源
	private String source;
	// 类别id
	private Integer category_id;
	
	/** 分页对象 */
	private PageQueryVO pageQueryVO;
	
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getCollect_startDate() {
		return collect_startDate;
	}
	public void setCollect_startDate(String collect_startDate) {
		this.collect_startDate = collect_startDate;
	}
	public String getCollect_endDate() {
		return collect_endDate;
	}
	public void setCollect_endDate(String collect_endDate) {
		this.collect_endDate = collect_endDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}
	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}
	
}
