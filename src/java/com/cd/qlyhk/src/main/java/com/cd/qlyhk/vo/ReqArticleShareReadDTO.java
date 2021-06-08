package com.cd.qlyhk.vo;

/**
 * 文章分享及阅读情况请求DTO
 * 
 * @author sailor
 *
 */
public class ReqArticleShareReadDTO {

	// 文章的UUID
	private String articleId;
	// 姓名
	private String user_name;
	// 公司
	private String company;
	// 职位
	private String position;
	// 城市
	private String city;
	// 注册日期起
	private String register_startDate;
	// 注册日期止
	private String register_endDate;

	// 分享日期起
	private String share_startDate;
	// 分享日期止
	private String share_endDate;

	// 阅读日期起
	private String read_startDate;
	// 阅读日期止
	private String read_endDate;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegister_startDate() {
		return register_startDate;
	}

	public void setRegister_startDate(String register_startDate) {
		this.register_startDate = register_startDate;
	}

	public String getRegister_endDate() {
		return register_endDate;
	}

	public void setRegister_endDate(String register_endDate) {
		this.register_endDate = register_endDate;
	}

	public String getShare_startDate() {
		return share_startDate;
	}

	public void setShare_startDate(String share_startDate) {
		this.share_startDate = share_startDate;
	}

	public String getShare_endDate() {
		return share_endDate;
	}

	public void setShare_endDate(String share_endDate) {
		this.share_endDate = share_endDate;
	}

	public String getRead_startDate() {
		return read_startDate;
	}

	public void setRead_startDate(String read_startDate) {
		this.read_startDate = read_startDate;
	}

	public String getRead_endDate() {
		return read_endDate;
	}

	public void setRead_endDate(String read_endDate) {
		this.read_endDate = read_endDate;
	}

}
