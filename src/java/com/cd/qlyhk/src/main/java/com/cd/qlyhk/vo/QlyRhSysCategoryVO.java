package com.cd.qlyhk.vo;

import com.cd.rdf.base.BaseValueObject;

public class QlyRhSysCategoryVO extends BaseValueObject{
	
	private static final long	serialVersionUID = 1L;
	
	// 类别ID
	private int category_id;
	// 类别编码
	private String category_code;
	// 类别名称
	private String category_name;
	// 所属类型（01表示行业 02表示公司品牌）
	private String type;
	// 排序序号
	private int order_index;
	
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getOrder_index() {
		return order_index;
	}
	public void setOrder_index(int order_index) {
		this.order_index = order_index;
	}
	
}
