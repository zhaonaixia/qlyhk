package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 用户收益及流水请求参数DTO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserStreamDTO {

	// 用户openId
	private String openId;
	// 月份
	private String stream_month;
	// 伙伴姓名
	private String user_name;
	// 1表示流水 2表示提现
	private String stream_type;

	/** 分页对象 */
	private PageQueryVO pageQueryVO;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getStream_type() {
		return stream_type;
	}

	public void setStream_type(String stream_type) {
		this.stream_type = stream_type;
	}

	public String getStream_month() {
		return stream_month;
	}

	public void setStream_month(String stream_month) {
		this.stream_month = stream_month;
	}

}
