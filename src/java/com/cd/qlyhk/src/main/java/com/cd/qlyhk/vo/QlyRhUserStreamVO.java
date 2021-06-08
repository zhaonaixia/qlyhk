package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 用户流水记录VO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserStreamVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int id;
	// 用户ID
	private int user_id;
	// 流水日期
	private String stream_date;
	// 类型（10表示一级收益 20表示二级收益 30表示提现）
	private int stream_type;
	// 伙伴（对应的来源人）
	private String partner;
	// 金额
	private double amount;
	// 余额
	private double balance;
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getStream_date() {
		return stream_date;
	}

	public void setStream_date(String stream_date) {
		this.stream_date = stream_date;
	}

	public int getStream_type() {
		return stream_type;
	}

	public void setStream_type(int stream_type) {
		this.stream_type = stream_type;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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
