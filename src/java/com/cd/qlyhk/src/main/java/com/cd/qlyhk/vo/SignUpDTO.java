package com.cd.qlyhk.vo;

/**
 * 报名VO类
 * 
 * @author sailor
 *
 */
public class SignUpDTO {

	private String companyName;
	private String userName;
	private String telphone;
	private String position;
	private int people_num;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getPeople_num() {
		return people_num;
	}

	public void setPeople_num(int people_num) {
		this.people_num = people_num;
	}

}
