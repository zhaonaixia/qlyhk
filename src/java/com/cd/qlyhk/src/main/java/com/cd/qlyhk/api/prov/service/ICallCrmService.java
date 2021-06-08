package com.cd.qlyhk.api.prov.service;

/**
 * 调用CRM系统服务接口定义
 * @author sailor
 *
 */
public interface ICallCrmService {

	final String BEAN_ID = "callCrmService";
	
	/**
	 * 新增或修改用户信息时，同步至微销官用户中心
	 * @param appId
	 * @param userJson
	 * @return
	 */
	boolean syncUserInfoToWXG(String appId, String openId,String userJson);
	
	/**
	 * 新增线索
	 * @param appId
	 * @param unionId
	 */
	void addClue(String appId, String unionId, String fromUnionId, String shareId);
	
	/**
	 * 获取个人的客户数
	 * @param appId
	 * @param unionId
	 * @return
	 */
	int getCustCount(String unionId);
	
	/**
	 * 设置微销官用户对应运营平台企业ID
	 * @param unionId
	 * @param custId
	 */
	void setUserCust(String unionId, int custId);
	
	/**
	 * 获取公司下的客户数
	 * @param unionId
	 * @return
	 */
	int getCompanyCustCount(String unionId);
}
