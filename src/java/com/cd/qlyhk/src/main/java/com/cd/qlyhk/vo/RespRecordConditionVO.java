package com.cd.qlyhk.vo;

/**
 * 访客情况返回
 * 
 * @author sailor
 *
 */
public class RespRecordConditionVO {

	// 用户OpenId
	private String openId;
	// 用户昵称
	private String nick_name;
	// 用户姓名
	private String user_name;
	// 用户头像
	private String headImgUrl;
	// 手机号码
	private String telphone;
	// 微信个人二维码URL
	private String ewm_url;

	// 阅读文章数
	private int total_num;
	// 总阅读时长
	private long total_readtimes;
	// 分享文章数
	private int total_sharenum;
	// 最近阅读时间
	private String lately_read_date;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getEwm_url() {
		return ewm_url;
	}

	public void setEwm_url(String ewm_url) {
		this.ewm_url = ewm_url;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public long getTotal_readtimes() {
		return total_readtimes;
	}

	public void setTotal_readtimes(long total_readtimes) {
		this.total_readtimes = total_readtimes;
	}

	public int getTotal_sharenum() {
		return total_sharenum;
	}

	public void setTotal_sharenum(int total_sharenum) {
		this.total_sharenum = total_sharenum;
	}

	public String getLately_read_date() {
		return lately_read_date;
	}

	public void setLately_read_date(String lately_read_date) {
		this.lately_read_date = lately_read_date;
	}

}
