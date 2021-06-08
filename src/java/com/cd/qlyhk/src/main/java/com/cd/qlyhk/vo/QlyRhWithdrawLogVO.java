package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 提现日志VO类
 * 
 * @author sailor
 *
 */
public class QlyRhWithdrawLogVO {

	// id
	private int id;
	// 用户OpenID
	private String openId;
	// 商户订单号
	private String partner_trade_no;
	// 收款用户姓名
	private String real_name;
	// 提现金额
	private Double amount;
	// IP地址
	private String ip_address;
	// 支付状态 （0表示支付失败 1表示支付成功）
	private String status;
	// 返回数据包
	private String return_data;

	private Date create_datetime;

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

	public String getPartner_trade_no() {
		return partner_trade_no;
	}

	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReturn_data() {
		return return_data;
	}

	public void setReturn_data(String return_data) {
		this.return_data = return_data;
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
