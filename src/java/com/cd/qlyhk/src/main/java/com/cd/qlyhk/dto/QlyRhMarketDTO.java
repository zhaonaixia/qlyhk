package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

/**
 * 分销情况列表请求参数DTO类
 * 
 * @author sailor
 *
 */
public class QlyRhMarketDTO {

	// 用户openId
	private String openId;
	// 伙伴姓名
	private String user_name;
	// 会员类型：0表示非会员1表示白银会员2表示黄金会员3表示钻石会员
	private String member_grade;
	// 是否有上级：1表示有0表示没有
	private String is_recm;
	// 当前余额区间起
	private int ib_startNum;
	// 当前余额区间止
	private int ib_endNum;
	// 累计收益区间起
	private int ti_startNum;
	// 累计收益区间止
	private int ti_endNum;
	// 累计提现区间起
	private int tc_startNum;
	// 累计提现区间止
	private int tc_endNum;
	// 伙伴数区间起
	private int pn_startNum;
	// 伙伴数区间止
	private int pn_endNum;

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

	public String getMember_grade() {
		return member_grade;
	}

	public void setMember_grade(String member_grade) {
		this.member_grade = member_grade;
	}

	public String getIs_recm() {
		return is_recm;
	}

	public void setIs_recm(String is_recm) {
		this.is_recm = is_recm;
	}

	public int getIb_startNum() {
		return ib_startNum;
	}

	public void setIb_startNum(int ib_startNum) {
		this.ib_startNum = ib_startNum;
	}

	public int getIb_endNum() {
		return ib_endNum;
	}

	public void setIb_endNum(int ib_endNum) {
		this.ib_endNum = ib_endNum;
	}

	public int getTi_startNum() {
		return ti_startNum;
	}

	public void setTi_startNum(int ti_startNum) {
		this.ti_startNum = ti_startNum;
	}

	public int getTi_endNum() {
		return ti_endNum;
	}

	public void setTi_endNum(int ti_endNum) {
		this.ti_endNum = ti_endNum;
	}

	public int getTc_startNum() {
		return tc_startNum;
	}

	public void setTc_startNum(int tc_startNum) {
		this.tc_startNum = tc_startNum;
	}

	public int getTc_endNum() {
		return tc_endNum;
	}

	public void setTc_endNum(int tc_endNum) {
		this.tc_endNum = tc_endNum;
	}

	public int getPn_startNum() {
		return pn_startNum;
	}

	public void setPn_startNum(int pn_startNum) {
		this.pn_startNum = pn_startNum;
	}

	public int getPn_endNum() {
		return pn_endNum;
	}

	public void setPn_endNum(int pn_endNum) {
		this.pn_endNum = pn_endNum;
	}

}
