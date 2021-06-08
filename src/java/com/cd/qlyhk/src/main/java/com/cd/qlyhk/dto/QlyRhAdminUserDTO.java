package com.cd.qlyhk.dto;

import com.cd.qlyhk.common.vo.PageQueryVO;

public class QlyRhAdminUserDTO {
	// 姓名
	private String user_name;
	// 城市
	private String city;
	// 公司
	private String company;
	// 职位
	private String position;
	// 注册日期起（格式yyyy-MM-dd）
	private String register_startDate;
	// 注册日期止（格式yyyy-MM-dd）
	private String register_endDate;
	// 是否会员
	private String isMember;
	// 当天日期（格式yyyy-MM-dd）
	private String dateTime;
	// 查询今日新增用户日期（格式yyyy-MM-dd）
	private String create_datetime;
	// 查询今日开通会员日期（格式yyyy-MM-dd）
	private String open_date;

	// 阅读数量起
	private int read_startNum;
	// 阅读数量止
	private int read_endNum;
	// 分享数量起
	private int share_startNum;
	// 分享数量止
	private int share_endNum;

	// 消费金额
	private double money;

	// 查询更多达人的月份（yyyy-MM）
	private String month;

	// 阅读时长排序（R表示升序 D表示降序）
	private String readTimes_sort;
	// 分享数排序（R表示升序 D表示降序）
	private String shareNum_sort;
	// 阅读数排序（R表示升序 D表示降序）
	private String readNum_sort;

	// 是否关注公众号（“0”：否，“1”：是）
	private String subscribe;
	
	// 3表示累计访客数
	private String fromIndex;
		
	/** 分页对象 */
	private PageQueryVO pageQueryVO;

	public String getUser_name() {
		return user_name;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public int getRead_startNum() {
		return read_startNum;
	}

	public void setRead_startNum(int read_startNum) {
		this.read_startNum = read_startNum;
	}

	public int getRead_endNum() {
		return read_endNum;
	}

	public void setRead_endNum(int read_endNum) {
		this.read_endNum = read_endNum;
	}

	public int getShare_startNum() {
		return share_startNum;
	}

	public void setShare_startNum(int share_startNum) {
		this.share_startNum = share_startNum;
	}

	public int getShare_endNum() {
		return share_endNum;
	}

	public void setShare_endNum(int share_endNum) {
		this.share_endNum = share_endNum;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
	}

	public String getOpen_date() {
		return open_date;
	}

	public void setOpen_date(String open_date) {
		this.open_date = open_date;
	}

	public PageQueryVO getPageQueryVO() {
		return pageQueryVO;
	}

	public void setPageQueryVO(PageQueryVO pageQueryVO) {
		this.pageQueryVO = pageQueryVO;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getReadTimes_sort() {
		return readTimes_sort;
	}

	public void setReadTimes_sort(String readTimes_sort) {
		this.readTimes_sort = readTimes_sort;
	}

	public String getShareNum_sort() {
		return shareNum_sort;
	}

	public void setShareNum_sort(String shareNum_sort) {
		this.shareNum_sort = shareNum_sort;
	}

	public String getReadNum_sort() {
		return readNum_sort;
	}

	public void setReadNum_sort(String readNum_sort) {
		this.readNum_sort = readNum_sort;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(String fromIndex) {
		this.fromIndex = fromIndex;
	}

}
