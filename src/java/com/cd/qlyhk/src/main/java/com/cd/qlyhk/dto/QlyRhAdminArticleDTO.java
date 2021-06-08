package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 查询文章列表请求DTO
 * @author sailor
 *
 */
public class QlyRhAdminArticleDTO {

	// 类别ID
	private Integer category_id;
	// 文章标题
	private String article_title;
	// 来源
	private String source;
	// 采集日期起（yyyy-MM-dd）
	private String collect_startDate;
	// 采集日期止（yyyy-MM-dd）
	private String collect_endDate;
	// 是否置顶（1表示是 0表示否）
	private String istop;
	// 审核人
	private String audit_user;
	// 审核状态
	private String audit_status;
	// 采集人名称
	private String user_name;
		
	// 分享数量起
	private int share_startNum;
	// 分享数量止
	private int share_endNum;
	// 阅读数量起
	private int read_startNum;
	// 阅读数数量止
	private int read_endNum;
	
	// 查询更多热点文章的月份（yyyy-MM）
	private String month;

	// 采集日期排序（R表示升序 D表示降序）
	private String collectDate_sort;
	// 分享数排序（R表示升序 D表示降序）
	private String shareNum_sort;
	// 阅读数排序（R表示升序 D表示降序）
	private String readNum_sort;
	
	// 1表示今日分享 2表示今日阅读
	private String fromIndex;
	
	/** 分页对象 */
	private PageQueryVO pageQueryVO;

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getAudit_user() {
		return audit_user;
	}

	public void setAudit_user(String audit_user) {
		this.audit_user = audit_user;
	}
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

	public int getShare_startNum() {
		return share_startNum;
	}

	public void setShare_startNum(int share_startNum) {
		this.share_startNum = share_startNum;
	}

	public int getShare_endNum() {
		return share_endNum;
	}

	public void setShare_endNum(int share_endNum) {
		this.share_endNum = share_endNum;
	}

	public int getRead_startNum() {
		return read_startNum;
	}

	public void setRead_startNum(int read_startNum) {
		this.read_startNum = read_startNum;
	}

	public int getRead_endNum() {
		return read_endNum;
	}

	public void setRead_endNum(int read_endNum) {
		this.read_endNum = read_endNum;
	}

	public String getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(String audit_status) {
		this.audit_status = audit_status;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCollectDate_sort() {
		return collectDate_sort;
	}

	public void setCollectDate_sort(String collectDate_sort) {
		this.collectDate_sort = collectDate_sort;
	}

	public String getShareNum_sort() {
		return shareNum_sort;
	}

	public void setShareNum_sort(String shareNum_sort) {
		this.shareNum_sort = shareNum_sort;
	}

	public String getReadNum_sort() {
		return readNum_sort;
	}

	public void setReadNum_sort(String readNum_sort) {
		this.readNum_sort = readNum_sort;
	}

	public String getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(String fromIndex) {
		this.fromIndex = fromIndex;
	}

}
