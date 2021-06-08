package com.cd.qlyhk.vo;

import com.cd.rdf.base.BaseValueObject;

/**
 * 默认自定义表格列显示VO
 * 
 * @author sailor
 *
 */
public class QlyRhDefaultCustomTableVO extends BaseValueObject{

	private static final long	serialVersionUID = 1L;
	
	// ID
	private int id;
	// 模块代码
	private String module_code;
	// 字段名
	private String head_key;
	// 字段名称
	private String head_name;
	// 是否显示列 true 表示显示 false 表示隐藏
	private boolean isshow;
	// 是否勾选列 true 表示勾选 false 表示不勾选
	private boolean ischecked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getHead_key() {
		return head_key;
	}

	public void setHead_key(String head_key) {
		this.head_key = head_key;
	}

	public String getHead_name() {
		return head_name;
	}

	public void setHead_name(String head_name) {
		this.head_name = head_name;
	}

	public boolean isIsshow() {
		return isshow;
	}

	public void setIsshow(boolean isshow) {
		this.isshow = isshow;
	}

	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

}
