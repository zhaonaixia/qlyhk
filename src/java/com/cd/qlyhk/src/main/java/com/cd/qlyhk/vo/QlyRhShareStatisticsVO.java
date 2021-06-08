package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 阅读情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhShareStatisticsVO {

	// ID
	private int statistics_id;
	// 统计日期
	private String statistics_date;
	// 分享文章UUID
	private String share_uuid;
	// 分享者
	private String sharer;
	// 总阅读次数
	private int total_num;
	// 总阅读人数
	private int total_readers;
	// 分享次数
	private int share_num;
	// 分享人数
	private int share_readers;
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

	public String getShare_uuid() {
		return share_uuid;
	}

	public void setShare_uuid(String share_uuid) {
		this.share_uuid = share_uuid;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getTotal_readers() {
		return total_readers;
	}

	public void setTotal_readers(int total_readers) {
		this.total_readers = total_readers;
	}

	public int getShare_num() {
		return share_num;
	}

	public void setShare_num(int share_num) {
		this.share_num = share_num;
	}

	public int getShare_readers() {
		return share_readers;
	}

	public void setShare_readers(int share_readers) {
		this.share_readers = share_readers;
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
