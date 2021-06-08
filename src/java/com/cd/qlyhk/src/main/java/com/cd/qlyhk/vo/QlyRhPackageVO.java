package com.cd.qlyhk.vo;

import java.util.Date;

/**
 * 套餐VO类
 * 
 * @author sailor
 *
 */
public class QlyRhPackageVO {

	private int package_id;
	private String uuid;
	private String code;
	private String name;
	private double price;
	private int month;
	private int record_state;
	private String default_opt;
	private int create_user_id;
	private Date create_time;
	private int modify_user_id;
	private Date modify_time;

	public int getPackage_id() {
		return package_id;
	}

	public void setPackage_id(int package_id) {
		this.package_id = package_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getRecord_state() {
		return record_state;
	}

	public void setRecord_state(int record_state) {
		this.record_state = record_state;
	}

	public int getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(int create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getModify_user_id() {
		return modify_user_id;
	}

	public void setModify_user_id(int modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getDefault_opt() {
		return default_opt;
	}

	public void setDefault_opt(String default_opt) {
		this.default_opt = default_opt;
	}

}
