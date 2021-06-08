package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 访客情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhRecordStatisticsVO {

	// ID
	private int statistics_id;
	// 统计日期
	private String statistics_date;
	// 访客ID
	private String record_id;
	// 总阅读文章数
	private int total_num;
	// 总阅读时长（以秒为单位
	private int total_reader_times;
	// 分享文章数
	private int total_share;
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

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getTotal_reader_times() {
		return total_reader_times;
	}

	public void setTotal_reader_times(int total_reader_times) {
		this.total_reader_times = total_reader_times;
	}

	public int getTotal_share() {
		return total_share;
	}

	public void setTotal_share(int total_share) {
		this.total_share = total_share;
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
