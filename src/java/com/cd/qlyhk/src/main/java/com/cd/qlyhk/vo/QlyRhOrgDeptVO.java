package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 组织机构与部门关系VO类
 * 
 * @author sailor_jsb
 *
 */
public class QlyRhOrgDeptVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	/** ID */
	private long id;
	/** 代码 */
	private String code;
	/** 名称 */
	private String name;
	private long org_id;
	private String parent_code;
	private long parent_id;
	private int isleaf;
	private int level;
	private String fullid;
	private String fullcode;
	private String fullname;
	private int record_state;

	private Date create_datetime;

	private Date modify_datetime;
	/** 备注 */
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public String getParent_code() {
		return parent_code;
	}

	public void setParent_code(String parent_code) {
		this.parent_code = parent_code;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	public int getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFullid() {
		return fullid;
	}

	public void setFullid(String fullid) {
		this.fullid = fullid;
	}

	public String getFullcode() {
		return fullcode;
	}

	public void setFullcode(String fullcode) {
		this.fullcode = fullcode;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getRecord_state() {
		return record_state;
	}

	public void setRecord_state(int record_state) {
		this.record_state = record_state;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
