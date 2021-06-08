package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 分销情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhMarketStatisticsVO {

	// ID
	private int statistics_id;
	// 统计日期
	private String statistics_date;
	// 用户openId
	private String openId;
	// 总余额
	private Double total_income_balance;
	// 总收益
	private Double total_income;
	// 总提现
	private Double total_cash_out;
	// 总伙伴数
	private int total_partner_num;
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;

	public int getStatistics_id() {
		return statistics_id;
	}

	public void setStatistics_id(int statistics_id) {
		this.statistics_id = statistics_id;
	}

	public String getStatistics_date() {
		return statistics_date;
	}

	public void setStatistics_date(String statistics_date) {
		this.statistics_date = statistics_date;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Double getTotal_income_balance() {
		return total_income_balance;
	}

	public void setTotal_income_balance(Double total_income_balance) {
		this.total_income_balance = total_income_balance;
	}

	public Double getTotal_income() {
		return total_income;
	}

	public void setTotal_income(Double total_income) {
		this.total_income = total_income;
	}

	public Double getTotal_cash_out() {
		return total_cash_out;
	}

	public void setTotal_cash_out(Double total_cash_out) {
		this.total_cash_out = total_cash_out;
	}

	public int getTotal_partner_num() {
		return total_partner_num;
	}

	public void setTotal_partner_num(int total_partner_num) {
		this.total_partner_num = total_partner_num;
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
