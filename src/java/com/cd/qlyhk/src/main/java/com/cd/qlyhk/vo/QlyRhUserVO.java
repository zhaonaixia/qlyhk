package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 用户信息VO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int user_id;
	// 用户的标识
	private String openId;
	// 用户头像
	private String headImgUrl;
	// 昵称
	private String nick_name;
	// 姓名
	private String user_name;
	// 公司
	private String company;
	// 职位
	private String position;
	// 行业
	private String industry;
	// 用户的性别（1是男性，2是女性，0是未知）
	private int sex;
	// 用户所在国家
	private String country;
	// 用户所在省份
	private String province;
	// 用户所在城市
	private String city;
	// 用户的语言，简体中文为zh_CN
	private String language;
	// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
	private int subscribe;
	// 手机号码
	private String telphone;
	// 微信号
	private String wechat_number;
	// 个人微信二维码
	private String ewm_url;
	// 个人简介
	private String personal_profile;
	// 是否开通会员（1表示是 0表示否）
	private String isMember;
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;
	// 注册日期（yyyy-MM-dd）
	private String register_date;
	// 开通会员日期（yyyy-MM-dd）
	private String open_date;
	// 区域编码
	private String area_code;
	// 登录账号
	private String login_name;
	// 密码
	private String password;
	// 用户类型（1表示普通用户 2表示管理员用户）
	private String user_type;
	// 会员到期日期（yyyy-MM-dd）
	private String member_end_date;
	// 推荐者
	private String recommender;
	// 用户unionid
	private String unionid;
	
	// 非数据库字段
	// 公司ID
	private int company_id;
	// 部门ID
	private int dept_id;
	// 部门
	private String department;
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getWechat_number() {
		return wechat_number;
	}

	public void setWechat_number(String wechat_number) {
		this.wechat_number = wechat_number;
	}

	public String getEwm_url() {
		return ewm_url;
	}

	public void setEwm_url(String ewm_url) {
		this.ewm_url = ewm_url;
	}

	public String getPersonal_profile() {
		return personal_profile;
	}

	public void setPersonal_profile(String personal_profile) {
		this.personal_profile = personal_profile;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
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

	public String getRegister_date() {
		return register_date;
	}

	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}

	public String getOpen_date() {
		return open_date;
	}

	public void setOpen_date(String open_date) {
		this.open_date = open_date;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getMember_end_date() {
		return member_end_date;
	}

	public void setMember_end_date(String member_end_date) {
		this.member_end_date = member_end_date;
	}

	public String getRecommender() {
		return recommender;
	}

	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public int getDept_id() {
		return dept_id;
	}

	public void setDept_id(int dept_id) {
		this.dept_id = dept_id;
	}

}
