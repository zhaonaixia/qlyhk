package com.cd.qlyhk.dto;

/**
 * 部门DTO类
 * 
 * @author sailor_jsb
 *
 */
public class QlyRhOrgDeptDTO {

	// 组织ID
	private int org_id;
	// 公司ID
	private int company_id;
	// 公司名称
	private String company_name;
	// 上级部门ID
	private int parent_id;
	// 部门名称
	private String name;
	// 用户的openId
	private String openId;
	
	//旧的部门ID
	private int old_group_id;
	
	//部门ID
	private int group_id;

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getOrg_id() {
		return org_id;
	}

	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getOld_group_id() {
		return old_group_id;
	}

	public void setOld_group_id(int old_group_id) {
		this.old_group_id = old_group_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

}
