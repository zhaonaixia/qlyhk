package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 月度文章情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhMonthlyArticlesStatisticsVO {

	// ID
	private int statistics_id;
	// 统计期间（yyyyMM）
	private int statistics_period;
	// 文章UUID
	private String article_uuid;
	// 阅读数
	private Integer total_read_num;
	// 分享数
	private Integer total_share_num;
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

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
	}

	public Integer getTotal_read_num() {
		return total_read_num;
	}

	public void setTotal_read_num(Integer total_read_num) {
		this.total_read_num = total_read_num;
	}

	public Integer getTotal_share_num() {
		return total_share_num;
	}

	public void setTotal_share_num(Integer total_share_num) {
		this.total_share_num = total_share_num;
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
