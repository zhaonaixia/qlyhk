package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 文章分享及阅读情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhArticleStatisticsVO {

	// ID
	private int statistics_id;
	// 统计日期
	private String statistics_date;
	// 文章唯一ID
	private String article_uuid;
	// 总分享次数
	private int total_share_num;
	// 总阅读次数
	private int total_read_num;
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

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
	}

	public int getTotal_share_num() {
		return total_share_num;
	}

	public void setTotal_share_num(int total_share_num) {
		this.total_share_num = total_share_num;
	}

	public int getTotal_read_num() {
		return total_read_num;
	}

	public void setTotal_read_num(int total_read_num) {
		this.total_read_num = total_read_num;
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
