package com.cd.qlyhk.vo;

import com.cd.rdf.base.BaseValueObject;

/**
 * 用户与公司关联表VO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserCompanyVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int id;
	// 公司ID
	private int company_id;
	// 用户ID
	private int user_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
