package com.cd.qlyhk.vo;

import java.util.Date;

import com.cd.rdf.base.BaseValueObject;

/**
 * 我的账户VO类
 * 
 * @author sailor
 *
 */
public class QlyRhUserAccountVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int id;
	// 用户ID
	private int user_id;
	// 账户等级
	private Integer member_grade;
	// 收益余额
	private Double income_balance;
	// 备注
	private String remark;
	// 创建时间
	private Date create_datetime;
	// 修改时间
	private Date modify_datetime;
	// 错失收益
	private Double miss_income;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Integer getMember_grade() {
		return member_grade;
	}

	public void setMember_grade(Integer member_grade) {
		this.member_grade = member_grade;
	}

	public Double getIncome_balance() {
		return income_balance;
	}

	public void setIncome_balance(Double income_balance) {
		this.income_balance = income_balance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Double getMiss_income() {
		return miss_income;
	}

	public void setMiss_income(Double miss_income) {
		this.miss_income = miss_income;
	}

}
