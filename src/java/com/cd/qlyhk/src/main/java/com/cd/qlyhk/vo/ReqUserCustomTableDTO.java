package com.cd.qlyhk.vo;

/**
 * 用户自定义表格列显示请求DTO
 * 
 * @author sailor
 *
 */
public class ReqUserCustomTableDTO {

	// 用户ID
	private int user_id;
	// 模块代码
	private String module_code;
	// 分类名称
	private String name;
	// 查询条件组合
	private String data;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
