package com.cd.qlyhk.dto;

/**
 * 企业支付请求参数DTO类
 * 
 * @author sailor
 *
 */
public class ReqEnterprisePaymentDTO {

	// 用户的openId
	private String openId;
	// 真实姓名
	private String realName;
	// 金额
	private double money;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

}
