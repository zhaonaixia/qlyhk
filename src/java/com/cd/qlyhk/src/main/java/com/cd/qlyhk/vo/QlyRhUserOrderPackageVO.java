package com.cd.qlyhk.vo;

/**
 * 用户与订单关联VO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserOrderPackageVO {

	private int user_id;
	private int order_id;
	private int package_id;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
	}

}
