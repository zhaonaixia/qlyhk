package com.cd.qlyhk.service;

import com.cd.qlyhk.dto.QlyRhMessageRemindSetDTO;
import com.cd.qlyhk.dto.QlyRhOrgDeptDTO;
import com.cd.qlyhk.dto.QlyRhSysCategoryDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhMessageRemindSetVO;
import com.cd.qlyhk.vo.QlyRhSysCategoryVO;
import com.cd.qlyhk.vo.ReqArticleImgDTO;
import com.cd.qlyhk.vo.ReqUserCustomQueryDTO;
import com.cd.qlyhk.vo.ReqUserCustomTableDTO;

public interface IPubCommonService {
	
	final String BEAN_ID = "pubCommonService";
	
	/**
	 * 获取删除类别下的文章数
	 * @param category_id
	 * @return
	 */
	int selectArticleCategoryCount(int category_id);

	/**
	 * 获取缓存的AccessToken
	 * @return
	 */
	String getCacheAccessToken();
	
	/**
	 * 获取缓存的JsapiTicket
	 * @param access_token
	 * @return
	 */
	String getCacheJsapiTicket(String access_token);
	
	/**
	 * 获取文章类别列表接口
	 * @return
	 */
	Response getCategoryList(String type, String openId);
	/**
	 * 获取已订阅类别接口
	 * @param openId
	 * @return
	 */
	Response getSubscribeList(String openId);
	/**
	 * 保存订阅文章接口
	 * @param categoryList
	 * @return
	 */
	Response saveSubscribeArticles(QlyRhSysCategoryDTO categoryDTO);
	/**
	 * 获取消息提醒设置接口
	 * @param openId
	 * @return
	 */
	Response getMessageRemindSet(String openId);
	/**
	 * 保存消息提醒设置接口
	 * @param messageRemindSetVO
	 * @return
	 */
	Response saveMessageRemindSet(QlyRhMessageRemindSetDTO messageRemindSetDTO);
	/**
	 * 根据openId获取userId
	 * @param openId
	 * @return
	 */
	int getUserId(String openId);

	/**
	 * 人脉雷达
	 * @param openId
	 * @return
	 */
	Response networkingRadar(String openId);
	
	void insertQlyRhMessageRemindSet(QlyRhMessageRemindSetDTO vo);

	/**
	 * 获取行政区域列表（省、市）
	 * @return
	 */
	Response queryAreaNonDistrict();

	/**
	 * 获取行政区域列表（区/县）
	 * @param areaCode
	 * @return
	 */
	Response queryAreaDistrict(String areaCode);
	
	/**
	 * 查询用户自定义查询分类列表
	 * @param userId
	 * @param moduleCode
	 * @return
	 */
	Response queryUserCustomQuery(int userId, String moduleCode);
	
	/**
	 * 新增用户自定义查询分类
	 * @param vo
	 */
	void insertUserCustomQuery(ReqUserCustomQueryDTO vo);
	
	/**
	 * 根据用户自定义查询分类查询数据
	 * @param vo
	 * @return
	 */
	Response queryDataForUserCustomQuery(ReqUserCustomQueryDTO vo);
	
	/**
	 *  删除用户自定义查询分类
	 * @param customQueryId
	 * @param userId
	 * @return
	 */
	Response deleteUserCustomQuery(int customQueryId, int userId);
	
	/**
	 * 新增或修改用户自定义表格列显示
	 * @param vo
	 */
	void insertOrUpdateUserCustomTable(ReqUserCustomTableDTO vo);
	
	/**
	 * 查询用户自定义表格列显示列表
	 * @param userId
	 * @param moduleCode
	 * @return
	 */
	Response queryUserCustomTable(int userId, String moduleCode);
	
	/**
	 * 查询默认的表格列显示
	 * @param moduleCode
	 * @return
	 */
	Response queryDefaultCustomTable(String moduleCode);
	
	/**
	 * 删除类别列表
	 * @param category_id
	 * @return
	 */
	Response deleteSysCategory(int category_id);

	/**
	 * 添加类别列表
	 * @param sysCategoryDTO
	 * @return
	 */
	Response addSysCategoryList(QlyRhSysCategoryDTO sysCategoryDTO);
	
	/**
	 * 更新添加类别列表
	 * @param sysCategoryDTO
	 * @return
	 */
	Response updateSysCategory(QlyRhSysCategoryDTO sysCategoryDTO);
	
	/**
	 * 获取类别信息
	 * @param category_id
	 * @return
	 */
	QlyRhSysCategoryVO getQlyRhSysCategoryVO(int category_id);
	
	int getCategoryIdByType(String type);
	
	/**
	 * 获取消息提醒设置
	 * @param userId
	 * @return
	 */
	QlyRhMessageRemindSetVO getMessageRemindSetVOByUserId(int userId);
	
	/**
	 * 上传微信图片
	 * @param imgId
	 * @return
	 */
	Response uploadArticleImg(ReqArticleImgDTO imgDTO);
	
	/**
	 * 查询部门组织树
	 * @param parent_id
	 * @return
	 */
	Response queryOrgDeptTree(int org_id, int parent_id, int level);
	
	/**
	 * 查询部门列表
	 * @param deptDTO
	 * @return
	 */
	Response queryOrgDeptList(QlyRhOrgDeptDTO deptDTO);
	
	/**
	 * 查询下级部门
	 * @param deptDTO
	 * @return
	 */
	Response queryDeptLowerLevel(QlyRhOrgDeptDTO deptDTO);
	
	/**
	 * 新增部门
	 * @param deptDTO
	 * @return
	 */
	Response addDept(QlyRhOrgDeptDTO deptDTO);
	
	/**
	 * 更新用户与部门的关联
	 * @param userId
	 * @param deptId
	 */
	void updateUserDept(int userId, int deptId);
	
	/**
	 * 删除用户与部门的关联
	 * @param userId
	 */
	void deleteQlyRhUserDept(int userId);
	
	/**
	 * 修改所属部门
	 * @param deptDTO
	 * @return
	 */
	Response groupChange(QlyRhOrgDeptDTO deptDTO);
	
	/**
	 * 获取地理位置
	 * @param deptDTO
	 * @return
	 */
	Response getLocation(String latitude, String longitude);
}
