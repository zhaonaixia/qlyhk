package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 公司管理请求参数DTO类
 * 
 * @author sailor
 *
 */
public class QlyRhCompanyDTO {

	// 公司ID
	private int company_id;
	// 公司名称
	private String company_name;
	// 公司简称
	private String abbr_name;
	// 社会信用代码
	private String social_credit_code;
	// 法人代表名称
	private String name;
	// 手机号码
	private String mobile_phone;

	// 用户openId
	private String openId;
	// 员工姓名
	private String user_name;

	// 接收公司转让的用户openId
	private String receiverOpenId;

	// 文章UUID
	private String articleId;

	// 1表示按分享数从大到小排序，2表示按分享数从小到大排序，3表示按最后一次分享时间从近到远，4表示按最后一次分享时间从远到近
	private int orderType;

	private int group_id;

	// 统一社会信用代码
	private String company_code;

	private String appId;

	/** 分页对象 */
	private PageQueryVO pageQueryVO;

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getSocial_credit_code() {
		return social_credit_code;
	}

	public void setSocial_credit_code(String social_credit_code) {
		this.social_credit_code = social_credit_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAbbr_name() {
		return abbr_name;
	}

	public void setAbbr_name(String abbr_name) {
		this.abbr_name = abbr_name;
	}

	public String getReceiverOpenId() {
		return receiverOpenId;
	}

	public void setReceiverOpenId(String receiverOpenId) {
		this.receiverOpenId = receiverOpenId;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
