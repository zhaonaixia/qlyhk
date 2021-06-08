package com.cd.qlyhk.service;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhUserVO;
/**
 * 用户服务接口
 * @author sailor
 *
 */
public interface IUserService {

	final String BEAN_ID = "userService";
	
//	QlyRhUserVO queryQlyRhUser(Map<String, Object> paramMap);
	
	void insertQlyRhUser(QlyRhUserVO userVO);
	
	Response getWxUserinfo(String code, String appId);
	
	/**
	 * 
	 * @param openId
	 * @param recommender 推荐者
	 * @param appId 
	 * @return
	 */
	QlyRhUserVO getUserVOByOpenId(String openId, String recommender, String appId);

	/**
	 * 个人中心首页
	 * @param openId
	 * @return
	 */
	Response queryUserHomePage(String openId);
	/**
	 * 保存设置名片信息接口
	 * @param userDTO
	 * @return
	 */
	Response saveEditUserInfo(QlyRhUserDTO userDTO);
	
	int updateUserInfo(QlyRhUserDTO userDTO);

	/**
	 * 新增或者更新用户操作日志
	 * @param paramMap
	 */
	void addOrUpdateUserOperateLog(Map<String, Object> paramMap);
	
	List<QlyRhUserVO> queryQlyRhUsers();
	
	List<Map<String, Object>> queryCancelRestrictUsers(Map<String, Object> param);
	
	String getIsMember(String openId);
	
	void addCustId(Map<String, Object> paramMap);
	
	/**
	 * 用户登录
	 * @param loginName
	 * @param password
	 */
	Response loginUser(String loginName, String password);

	/**
	 * 查询用户列表
	 * @param userDTO
	 * @return
	 */
	Response getUsersList(QlyRhAdminUserDTO userDTO);
	
	/**
	 * 查询用户列表
	 * @param paramMap
	 * @param pageQueryVO
	 * @return
	 */
	Response queryUsersList(Map<String, Object> paramMap, PageQueryVO pageQueryVO);

	/**
	 * 根据openId查询个人信息
	 * @param openId
	 * @return
	 */
	Response getUserInfoByOpenId(String openId);

	/**
	 * 获取用户分享过的文章列表
	 * @param openId
	 * @return
	 */
	Response queryUserShareArticlesList(String openId);

	/**
	 * 获取用户阅读过的文章列表
	 * @param openId
	 * @return
	 */
	Response queryUserReadArticlesList(String openId);
	
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	QlyRhUserVO getQlyRhUserByToken(String token);
	
	/**
	 * 获取本地数据库的cust_id
	 * @param user_id
	 * @return
	 */
	Integer getCustId(int user_id);
	
	/**
	 * 获取未完善资料的用户列表
	 * @return
	 */
	List<QlyRhUserVO> queryNotPerfectInfoUsers();
	
	/**
	 * 获取会员续期的用户列表
	 * @return
	 */
	List<QlyRhUserVO> queryMembershipRenewalUsers();
	
	/**
	 * 更新个人资料信息（拉取微信最新的用户信息）
	 * @param openId
	 */
	void updateWxUserInfo(String openId, String appId);
	
	/**
	 * 查询用户的操作或推送消息记录
	 * @param openId
	 * @return
	 */
	Map<String, Object> queryQlyRhUserOperateLog(String openId);
	
	/**
	 * 同步会员到期日
	 */
	void syncUserMemberEndDate();
	
	/**
	 * 统计我的伙伴数
	 * @param paramMap
	 * @return
	 */
	int getQlyRhUserTotalPartner(Map<String, Object> paramMap);
	
	/**
	 * 同步用户的unionid
	 */
	void syncUserUnionid(String appId);
	
	/**
	 * 获取设置勿扰模式的用户列表
	 * @return
	 */
	List<QlyRhUserVO> queryDNDUsers();
	
	/**
	 * 根据unionid获取用户信息
	 * @param unionid
	 * @return
	 */
	QlyRhUserVO getQlyRhUserByUnionid(String unionid);
}
