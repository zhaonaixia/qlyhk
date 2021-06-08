package com.cd.qlyhk.vo;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 用户自定义查询分类请求DTO
 * 
 * @author sailor
 *
 */
public class ReqUserCustomQueryDTO {

	private int id;
	// 用户ID
	private int user_id;
	// 模块代码
	private String module_code;
	// 分类名称
	private String name;
	// 查询条件组合
	private String data;
	
	/** 分页对象 */
	private PageQueryVO pageQueryVO;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

}
