package com.cd.qlyhk.dto;

import java.util.List;

import com.cd.qlyhk.vo.QlyRhSysCategoryVO;

public class QlyRhSysCategoryDTO {
	
	List<QlyRhSysCategoryVO> categoryList;
	private String openId;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public List<QlyRhSysCategoryVO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<QlyRhSysCategoryVO> categoryList) {
		this.categoryList = categoryList;
	}
	
	
}
