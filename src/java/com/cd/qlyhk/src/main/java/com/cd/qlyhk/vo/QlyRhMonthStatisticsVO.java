package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 月度情况统计表VO
 * 
 * @author sailor
 *
 */
public class QlyRhMonthStatisticsVO {

	// ID
	private int statistics_id;
	// 统计月份(yyyyMM)
	private String statistics_period;
	// 新增用户数
	private int add_user_num;
	// 新增会员数
	private int add_member_num;
	// 总阅读数（次数）
	private int total_read_num;
	// 总分享数（文章数）
	private int total_share_num;
	// 财税早报总阅读数（次数）
	private int total_mp_read_num;
	// 财税早报总分享数（文章数）
	private int total_mp_share_num;
	// 购买白银会员数
	private int buy_byhy_num;
	// 购买黄金会员数
	private int buy_hjhy_num;
	// 购买钻石会员数
	private int buy_zshy_num;
	// 有效会员数
	private int member_valid_num;
	// 过期会员数
	private int member_expire_num;

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

	public String getStatistics_period() {
		return statistics_period;
	}

	public void setStatistics_period(String statistics_period) {
		this.statistics_period = statistics_period;
	}

	public int getAdd_user_num() {
		return add_user_num;
	}

	public void setAdd_user_num(int add_user_num) {
		this.add_user_num = add_user_num;
	}

	public int getAdd_member_num() {
		return add_member_num;
	}

	public void setAdd_member_num(int add_member_num) {
		this.add_member_num = add_member_num;
	}

	public int getTotal_read_num() {
		return total_read_num;
	}

	public void setTotal_read_num(int total_read_num) {
		this.total_read_num = total_read_num;
	}

	public int getTotal_share_num() {
		return total_share_num;
	}

	public void setTotal_share_num(int total_share_num) {
		this.total_share_num = total_share_num;
	}

	public int getTotal_mp_read_num() {
		return total_mp_read_num;
	}

	public void setTotal_mp_read_num(int total_mp_read_num) {
		this.total_mp_read_num = total_mp_read_num;
	}

	public int getTotal_mp_share_num() {
		return total_mp_share_num;
	}

	public void setTotal_mp_share_num(int total_mp_share_num) {
		this.total_mp_share_num = total_mp_share_num;
	}

	public int getBuy_byhy_num() {
		return buy_byhy_num;
	}

	public void setBuy_byhy_num(int buy_byhy_num) {
		this.buy_byhy_num = buy_byhy_num;
	}

	public int getBuy_hjhy_num() {
		return buy_hjhy_num;
	}

	public void setBuy_hjhy_num(int buy_hjhy_num) {
		this.buy_hjhy_num = buy_hjhy_num;
	}

	public int getBuy_zshy_num() {
		return buy_zshy_num;
	}

	public void setBuy_zshy_num(int buy_zshy_num) {
		this.buy_zshy_num = buy_zshy_num;
	}

	public int getMember_valid_num() {
		return member_valid_num;
	}

	public void setMember_valid_num(int member_valid_num) {
		this.member_valid_num = member_valid_num;
	}

	public int getMember_expire_num() {
		return member_expire_num;
	}

	public void setMember_expire_num(int member_expire_num) {
		this.member_expire_num = member_expire_num;
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
