package com.cd.qlyhk.api.prov.service;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.rest.Response;

public interface IOuterService {
	final String BEAN_ID = "outerService";
	
	/**
	 * 新增客户信息接口
	 * @param customer
	 * @return
	 */
	int addOrUpdateCust(int user_id, String customer_name, String phone, String unionid) throws Exception;
	
	/**
	 * 新增企业信息接口
	 * @param customer
	 * @return
	 */
	int addOrUpdateCompany(String customer_name, String abbr_name, String company_code, String phone, String unionid);
	
	/**
	 * 校验社会信用代码是否已经存在
	 * @param company_code
	 * @return
	 */
	boolean checkCustomerCode(String company_code);
	
	/**
	 * 校验注册用户是否已经加入公司
	 * @param phone
	 * @return
	 */
	int checkRegistered(String user_name, String phone, String unionid);
	
	/**
	 * 提供千里眼扫码支付接口
	 * @param packageId
	 * @param userId
	 * @param amount
	 * @param effective_time_e 
	 * @param effective_time_s 
	 * @return
	 */
	Response payOrderPack(String packageId, int cust_id, double amount, String effective_time_s, String effective_time_e);
	
	/**
	 * 获取套餐列表
	 * @return
	 */
	List<Map<String, Object>> getPackList();
	
	/**
	 * 根据客户id及app查询客户是否有订购增值服务
	 * @param customer_id
	 * @param app_code
	 * @param period
	 * @return
	 */
	int queryValidOrder(int customer_id, String app_code, String period);
	
	/**
	 * 客户名称唯一性校验
	 * @param customer_name
	 * @return
	 * @throws Exception 
	 */
	Long checkCustomerNameUnique(String customer_name) throws Exception;
	
	/**
	 * 修改客户信息接口
	 * @param cust_id
	 * @param user_name
	 * @param telphone
	 */
	void updateCustDetail(Integer cust_id, String user_name, String telphone);
	
	/**
	 * 获取企业列表
	 * @return
	 */
	List<Map<String, Object>> getCompanyList(String customerName);
	
	/**
	 * 获取企业部门列表
	 * @return
	 */
	List<Map<String, Object>> getCompanyGroupList(int companyId);
	
	/**
	 * 新增公司部门
	 * @param param
	 * @return
	 */
	int addCompanyGroup(Map<String, Object> param);
	
	/**
	 * 获取企业成员列表
	 * @return
	 */
	List<Map<String, Object>> getCompanyUserList(int companyId, int groupId);
	
	/**
	 * 邀请成员加入公司
	 * @param param
	 */
	int inviteMember(Map<String, Object> param);
	
	/**
	 * 查询关联的企业信息
	 * @param unionid
	 * @return
	 */
	Map<String, Object> getCompanyInfo(String unionid);
	
	/**
	 * 获取部门下的成员列表
	 * @return
	 */
	List<Map<String, Object>> getGroupUserList(int groupId, String user_name);
	
	/**
	 * 删除公司成员用户
	 * @param companyId
	 */
	boolean deleteCompanyUser(int companyId, String unionid);
	
	/**
	 * 修改成员所属部门信息
	 * @param old_group_id
	 * @param group_id
	 * @param unionid
	 * @return
	 */
	boolean groupChange(int old_group_id, int group_id, String unionid);
	
	/**
	 * 修改公司基础信息
	 * @param companyId
	 * @param customer_name
	 * @param abbr_name
	 * @param company_code
	 * @param unionid
	 * @return
	 */
	Map<String, String> updateCompanyInfo(int companyId, String customer_name, String abbr_name, String company_code, String unionid);
	
	/**
	 * 查询公司信息
	 * @param companyId
	 * @return
	 */
	Map<String, Object> getCompanyDetailInfo(int companyId);
}
