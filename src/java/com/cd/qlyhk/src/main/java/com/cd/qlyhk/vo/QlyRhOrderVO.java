package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 订单VO类
 * 
 * @author sailor
 *
 */
public class QlyRhOrderVO {

	private int order_id;// 订单ID
	private String lsh;// 订单流水号
	private double money;// 订单金额
	private String pay_status; // 订单支付状态 0表示未支付 1表示已支付
	private String start_date;// 订单开始日期
	private String end_date;// 订单结束日期
	private String status;// 订单状态 01表示正常 02表示已过期 03表示订单同步失败
	private String remark;// 备注
	private int create_user_id;
	private Date create_time;
	private int modify_user_id;
	private Date modify_time;
	private int order_type;

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(int create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getModify_user_id() {
		return modify_user_id;
	}

	public void setModify_user_id(int modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

}
