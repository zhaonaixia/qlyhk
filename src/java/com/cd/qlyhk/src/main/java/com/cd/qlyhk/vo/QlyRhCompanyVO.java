package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 公司信息VO类
 * 
 * @author sailor
 *
 */
public class QlyRhCompanyVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

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
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

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

	public String getAbbr_name() {
		return abbr_name;
	}

	public void setAbbr_name(String abbr_name) {
		this.abbr_name = abbr_name;
	}

}
