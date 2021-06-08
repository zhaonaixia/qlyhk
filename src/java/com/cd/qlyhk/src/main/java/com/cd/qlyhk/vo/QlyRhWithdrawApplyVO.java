package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 提现申请VO类
 * 
 * @author sailor
 *
 */
public class QlyRhWithdrawApplyVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int id;
	// 用户openID
	private String openId;
	// 申请日期
	private String apply_date;
	// 提现金额
	private double amount;
	// 申请状态（01表示待审核 02表示审核通过 03表示审核未通过）
	private String apply_status;
	// 备注
	private String remark;
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getApply_date() {
		return apply_date;
	}

	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getApply_status() {
		return apply_status;
	}

	public void setApply_status(String apply_status) {
		this.apply_status = apply_status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}
