package com.cd.qlyhk.vo;

import com.cd.rdf.base.BaseValueObject;

public class QlyRhSysAreaVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	// 区域代码
	private String areaCode;
	// 区域名称
	private String areaName;
	// 行政区划级次
	private int areaLevel;
	// 上级行政区域代码
	private String parentAreaCode;
	// 完整行政区划名称
	private String areaFullName;
	// 完整行政区划代码
	private String areaFullCode;
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getParentAreaCode() {
		return parentAreaCode;
	}

	public void setParentAreaCode(String parentAreaCode) {
		this.parentAreaCode = parentAreaCode;
	}

	public String getAreaFullName() {
		return areaFullName;
	}

	public void setAreaFullName(String areaFullName) {
		this.areaFullName = areaFullName;
	}

	public String getAreaFullCode() {
		return areaFullCode;
	}

	public void setAreaFullCode(String areaFullCode) {
		this.areaFullCode = areaFullCode;
	}

}
