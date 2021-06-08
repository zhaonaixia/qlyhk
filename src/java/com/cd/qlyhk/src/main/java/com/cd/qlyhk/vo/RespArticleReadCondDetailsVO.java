package com.cd.qlyhk.vo;

/**
 * 文章阅读情况详情返回
 * 
 * @author sailor
 *
 */
public class RespArticleReadCondDetailsVO {

	private String nick_name;
	private String user_name;
	private String headImgUrl;
	private String telphone;
	private String ewm_url;

	// 阅读文章数
	private int total_num;
	// 总阅读时长
	private long total_readtimes;
	// 分享文章数
	private int total_sharenum;
	// 当前文章的阅读时长
	private long read_time;
	// 当前文章的阅读次数
	private int read_num;
	// 当前文章的分享次数
	private int share_num;

	// 时长转换成时分秒的形式显示
	private String strTotalReadtimes;
	private String strReadTime;

	// 是否分享
	private String isshare;
	
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

	public int getTotal_sharenum() {
		return total_sharenum;
	}

	public void setTotal_sharenum(int total_sharenum) {
		this.total_sharenum = total_sharenum;
	}

	public int getRead_num() {
		return read_num;
	}

	public void setRead_num(int read_num) {
		this.read_num = read_num;
	}

	public int getShare_num() {
		return share_num;
	}

	public void setShare_num(int share_num) {
		this.share_num = share_num;
	}

	public long getTotal_readtimes() {
		return total_readtimes;
	}

	public void setTotal_readtimes(long total_readtimes) {
		this.total_readtimes = total_readtimes;
	}

	public long getRead_time() {
		return read_time;
	}

	public void setRead_time(long read_time) {
		this.read_time = read_time;
	}

	public String getStrTotalReadtimes() {
		return strTotalReadtimes;
	}

	public void setStrTotalReadtimes(String strTotalReadtimes) {
		this.strTotalReadtimes = strTotalReadtimes;
	}

	public String getStrReadTime() {
		return strReadTime;
	}

	public void setStrReadTime(String strReadTime) {
		this.strReadTime = strReadTime;
	}

	public String getIsshare() {
		return isshare;
	}

	public void setIsshare(String isshare) {
		this.isshare = isshare;
	}

}
