package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 月度阅读时段情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhMonthlyReadStatisticsVO {

	// ID
	private int statistics_id;
	// 统计期间（yyyyMM）
	private int statistics_period;
	// 数据内容JSON格式
	private String data;
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

	public int getStatistics_period() {
		return statistics_period;
	}

	public void setStatistics_period(int statistics_period) {
		this.statistics_period = statistics_period;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
