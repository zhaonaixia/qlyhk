package com.cd.qlyhk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.ICallCrmService;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.dto.QlyRhMessageRemindSetDTO;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.ICompanyService;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IOrderService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhCompanyVO;
import com.cd.qlyhk.vo.QlyRhUserAccountVO;
import com.cd.qlyhk.vo.QlyRhUserStatisticsVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.qlyhk.vo.WxUserVO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IUserService.BEAN_ID)
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final String mapperNamespace = UserServiceImpl.class.getName();

	@Autowired
	private IBaseDAO baseDAO;

	@Autowired
	private IPubCommonService pubCommonService;

	@Autowired
	private IArticleService articleService;

	@Autowired
	private IOuterService outerService;

	@Value("${local.debug.no.boss}")
	public Boolean local_debug_no_boss;

	@Autowired
	private IDataCacheService dataCacheService;

	@Autowired
	private IMessageCenterService messageCenterService;
	
	@Autowired
	private IMarketService marketService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ICallCrmService callCrmService;
	
//	@Override
//	public QlyRhUserVO queryQlyRhUser(Map<String, Object> paramMap) {
//		return baseDAO.findOne(mapperNamespace+".queryQlyRhUser", paramMap);
//	}

	@Override
	public void insertQlyRhUser(QlyRhUserVO userVO) {
		baseDAO.add(mapperNamespace + ".insertQlyRhUser", userVO);
	}

	@Override
	public Response getWxUserinfo(String code, String appId) {
		Response result = Response.getDefaulTrueInstance();
		JSONObject json = null;
		try {
			String data = WxUtil.GetOpenid(code);
			logger.info("????????????OpenId????????????:" + data);
			json = JSONObject.parseObject(data);
			String openId = json.getString("openid");
			String accessToken = json.getString("access_token");
			logger.info("*************????????????openid???access_token*************{},{}", openId, accessToken);

			if (StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(accessToken)) {
				String userInfoStr = WxUtil.GetUnconcernedUserInfo(accessToken, openId);
				logger.info("*************????????????userinfo*************" + userInfoStr);

				JSONObject userJSON = JSONObject.parseObject(userInfoStr);
				String retOpenId = userJSON.getString("openid");

				if (userJSON != null && StringUtils.isNotBlank(retOpenId)) {

					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("openId", userJSON.getString("openid"));
					retMap.put("nick_name", userJSON.getString("nickname"));
					retMap.put("headImgUrl", userJSON.getString("headimgurl"));
					retMap.put("sex", userJSON.getInteger("sex"));
					int subscribe = 0;
//					if(userJSON.get("subscribe") != null) {
//						subscribe = userJSON.getIntValue("subscribe");
//					}
					
					QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
					if (userVO == null) {
						WxUserVO weiXinUser = new WxUserVO();
						weiXinUser.setOpenId(userJSON.getString("openid"));
						weiXinUser.setNickname(userJSON.getString("nickname"));
						weiXinUser.setSex(userJSON.getInteger("sex"));
						weiXinUser.setCountry(userJSON.getString("country"));
						weiXinUser.setProvince(userJSON.getString("province"));
						weiXinUser.setCity(userJSON.getString("city"));
						weiXinUser.setHeadImgUrl(userJSON.getString("headimgurl"));
						weiXinUser.setLanguage(userJSON.getString("language"));
						weiXinUser.setSubscribe(0);// ?????????
						weiXinUser.setUnionid(userJSON.getString("unionid"));
						
						addQlyRhUser(weiXinUser, openId, null, userInfoStr, appId);
					} else {
						subscribe = userVO.getSubscribe();
					}
					
					retMap.put("subscribe", subscribe);
					result.setData(retMap);
				}

			}

		} catch (Exception e) {
			logger.error("?????????????????????????????????" + e);
		}

		return result;
	}

	@Override
	public QlyRhUserVO getUserVOByOpenId(String openId, String recommender, String appId) {

		// ???????????????????????????????????????????????????
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		if (userVO == null) {
			String access_token = pubCommonService.getCacheAccessToken();

			if (StringUtils.isNotBlank(access_token)) {
				String userInfoStr = WxUtil.GetUserInfo(access_token, openId);
				logger.info("*************????????????userinfo*************" + userInfoStr);

				JSONObject userJSON = JSONObject.parseObject(userInfoStr);
				String retOpenId = userJSON.getString("openid");

				if (userJSON != null && StringUtils.isNotBlank(retOpenId)) {
					WxUserVO weiXinUser = new WxUserVO();
					weiXinUser.setOpenId(userJSON.getString("openid"));
					weiXinUser.setNickname(userJSON.getString("nickname"));
					weiXinUser.setSex(userJSON.getInteger("sex"));
					weiXinUser.setCountry(userJSON.getString("country"));
					weiXinUser.setProvince(userJSON.getString("province"));
					weiXinUser.setCity(userJSON.getString("city"));
					weiXinUser.setHeadImgUrl(userJSON.getString("headimgurl"));
					weiXinUser.setLanguage(userJSON.getString("language"));
					weiXinUser.setSubscribe(userJSON.getInteger("subscribe"));
					weiXinUser.setUnionid(userJSON.getString("unionid"));
					
					logger.info("********************scanQRCodeShareAarticle********************" + recommender);
					addQlyRhUser(weiXinUser, openId, recommender, userInfoStr, appId);
				} else if (userJSON != null && userJSON.getIntValue("errcode") == 40001) {
					String errMsg = userJSON.getString("errmsg");
					if (errMsg.contains("access_token is invalid")) {
						logger.info("*************[getUserVOByOpenId]????????????AccessToken*************");
						dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
						dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
					}
				}

			}
		} else {
			// ???????????????????????????????????????????????????????????????
			if(userVO.getSubscribe() == 0) {
				QlyRhUserDTO updateUser = new QlyRhUserDTO();
				updateUser.setOpenId(openId);
				updateUser.setSubscribe(1);
				
				this.updateUserInfo(updateUser);
			}
			
			// ??????unionid
			if(StringUtils.isBlank(userVO.getUnionid())) {
				String access_token = pubCommonService.getCacheAccessToken();

				if (StringUtils.isNotBlank(access_token)) {
					String userInfoStr = WxUtil.GetUserInfo(access_token, openId);
					logger.info("*************????????????userinfo*************" + userInfoStr);

					JSONObject userJSON = JSONObject.parseObject(userInfoStr);
					String retOpenId = userJSON.getString("openid");

					if (userJSON != null && StringUtils.isNotBlank(retOpenId)) {
						String unionid = userJSON.getString("unionid");
						if(StringUtils.isNotBlank(unionid)) {
							QlyRhUserDTO updateUser = new QlyRhUserDTO();
							updateUser.setOpenId(openId);
							updateUser.setUnionid(unionid);
							
							this.updateUserInfo(updateUser);
							
							logger.info("********************??????????????????????????????????????????********************" + openId);
							callCrmService.syncUserInfoToWXG(appId, openId, userInfoStr);
						}
						
					} else if (userJSON != null && userJSON.getIntValue("errcode") == 40001) {
						String errMsg = userJSON.getString("errmsg");
						if (errMsg.contains("access_token is invalid")) {
							logger.info("*************[getUserVOByOpenId]????????????AccessToken*************");
							dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
							dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
						}
					}

				}
			}
		}

		return userVO;
	}
	
	private void addQlyRhUser(WxUserVO weiXinUser, String openId, String recommender, String userInfoStr, String appId) {
		QlyRhUserVO userVO = wxUserTransformQlyRhUserVO(weiXinUser);
		userVO.setRecommender(recommender);
		insertQlyRhUser(userVO);
		logger.info("********************??????????????????????????????********************" + openId);
		
		QlyRhUserAccountVO accountVO = new QlyRhUserAccountVO();
		accountVO.setUser_id(userVO.getUser_id());
		accountVO.setMember_grade(0);
		accountVO.setIncome_balance(0.0);
		accountVO.setMiss_income(0.0);
		marketService.updateQlyRhUserAccount(accountVO);
		logger.info("********************?????????????????????********************" + openId);
		
		QlyRhMessageRemindSetDTO messageRemindSetDTO = new QlyRhMessageRemindSetDTO();
		messageRemindSetDTO.setUser_id(userVO.getUser_id());
		messageRemindSetDTO.setRemind_type("1");
		String push_type = "[{\"rec_hot_news\":\"1\"},{\"rec_sub_article\":\"1\"}]";
		messageRemindSetDTO.setPush_type(push_type);
		messageRemindSetDTO.setModify_datetime(new Date());
		pubCommonService.insertQlyRhMessageRemindSet(messageRemindSetDTO);
		
		logger.info("********************???????????????????????????********************" + openId);
		
		logger.info("********************??????????????????????????????????????????********************" + openId);
		callCrmService.syncUserInfoToWXG(appId, openId, userInfoStr);
	}

	private QlyRhUserVO wxUserTransformQlyRhUserVO(WxUserVO weiXinUser) {
		QlyRhUserVO newUser = new QlyRhUserVO();
		newUser.setOpenId(weiXinUser.getOpenId());
		newUser.setNick_name(weiXinUser.getNickname());
		newUser.setUser_name(weiXinUser.getNickname());
		newUser.setHeadImgUrl(weiXinUser.getHeadImgUrl());
		newUser.setSex(weiXinUser.getSex());
		newUser.setCountry(weiXinUser.getCountry());
		newUser.setProvince(weiXinUser.getProvince());
		newUser.setCity(weiXinUser.getCity());
		newUser.setLanguage(weiXinUser.getLanguage());
		newUser.setSubscribe(weiXinUser.getSubscribe());
		newUser.setIsMember("0");
		newUser.setUser_type("1");
		newUser.setRegister_date(DateUtil.formatDate(new Date()));
		newUser.setUnionid(weiXinUser.getUnionid());
		
		return newUser;
	}

	@Override
	public Response queryUserHomePage(String openId) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		int userId = userVO.getUser_id();
		// ???????????????
		int total_share = 0;
		total_share = articleService.getTotalShareNum(openId);
		// ???????????????
		int totalReaderToday = 0;
		totalReaderToday = articleService.getCountRecordToday(openId);
		// ???????????????
		int totalReader = 0;
//		totalReader = articleService.getTotalCountRecord(openId);
		
//		long time = System.currentTimeMillis();
		// ????????????
		String isMember = getIsMember(openId);
//		logger.info("***************************???????????????????????????????????????????????????********************************" + (System.currentTimeMillis() - time));
		// ???????????????
		String end_date = userVO.getMember_end_date();
		
		// ??????????????????????????????
		String isFoundCompany = "0";
		String abbr_name = "", company_name = "";
		int companyId = 0;
		// ???????????????(??????)?????????
		String isCompanyAdmin = "0";
		
//		QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", userVO.getCompany());
		Map<String, Object> companyMap = outerService.getCompanyInfo(userVO.getUnionid());
		if(!companyMap.isEmpty()) {
			isFoundCompany = "1";
			companyId = (int) companyMap.get("company_id");
			company_name = (String) companyMap.get("company_name");
			abbr_name = (String) companyMap.get("abbreviation");
			
			if("true".equals(companyMap.get("isadmin"))) {
				isCompanyAdmin = "1";
			}
			
//			abbr_name = companyVO.getAbbr_name();
//			companyId = companyVO.getCompany_id();
			
			if(!company_name.equals(userVO.getCompany())) {
				// ???????????????????????????????????????
				QlyRhUserDTO updateUser = new QlyRhUserDTO();
				updateUser.setOpenId(openId);
				updateUser.setCompany(company_name);
				this.updateUserInfo(updateUser);
				
			}
			
		}
		
//		if(userVO.getCompany_id() != 0) {
//			isCompanyAdmin = "1";
//		}
		
		// ????????????
		QlyRhUserAccountVO accountVO = marketService.getQlyRhUserAccountVO(userId);
		int member_grade = 0;
		// ???????????????????????????
		double income_balance = 0, miss_income = 0;
		if(accountVO != null) {
			member_grade = accountVO.getMember_grade();
			income_balance = accountVO.getIncome_balance();
			miss_income = accountVO.getMiss_income();
		}
		// ????????????
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("total_type", "1");
		paramMap.put("userId", userId);
		double total_income = marketService.getQlyRhUserStreamTotalIncome(paramMap);
		// ????????????
		Date dt = new Date();
//		Date startDt = DateUtil.afterNDay(dt, -1);
		String stream_date = DateUtil.formatDate(dt);
		paramMap.put("stream_date", stream_date);
		double today_income = marketService.getQlyRhUserStreamTotalIncome(paramMap);
		
		// ????????????
		int total_partner = 0, today_partner = 0;
		Map<String, Object> tpMap = new HashMap<String, Object>();
		tpMap.put("openId", openId);
		total_partner = getQlyRhUserTotalPartner(tpMap);
		
		tpMap.put("register_date", DateUtil.formatDate(dt));
		today_partner = getQlyRhUserTotalPartner(tpMap);
		
		// ???????????????
		int team_article = 0, team_member = 0, team_customer = 0;
		if(companyId != 0) {
			Map<String, Object> teamMap = new HashMap<String, Object>();
			teamMap.put("companyId", companyId);
			team_article = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".queryReleaseArticleCount", teamMap);
//			team_member = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhUserTeamMember", teamMap);
			team_member = companyService.getCompanyAllUserNum(companyId);
			team_customer = callCrmService.getCompanyCustCount(userVO.getUnionid());
		}
		
		resultMap.put("headImgUrl", userVO.getHeadImgUrl());
		resultMap.put("user_name", userVO.getUser_name());
		resultMap.put("isMember", isMember);
		resultMap.put("total_share", total_share);
		resultMap.put("today_reader", totalReaderToday);
		resultMap.put("total_reader", totalReader);
		resultMap.put("isFoundCompany", isFoundCompany);
		resultMap.put("isCompanyAdmin", isCompanyAdmin);
		resultMap.put("company_id", companyId);
		resultMap.put("company_name", company_name);
		resultMap.put("abbr_name", abbr_name);
		resultMap.put("member_end_date", end_date);
		resultMap.put("member_grade", member_grade);
		resultMap.put("total_income", total_income);
		resultMap.put("income_balance", income_balance);
		resultMap.put("today_income", today_income);
		resultMap.put("miss_income", miss_income);
		resultMap.put("total_partner", total_partner);
		resultMap.put("today_partner", today_partner);
		resultMap.put("team_article", team_article);
		resultMap.put("team_member", team_member);
		resultMap.put("team_customer", team_customer);

		resp.setData(resultMap);
		return resp;
	}

	@Override
	public Response saveEditUserInfo(QlyRhUserDTO userDTO) {
		Response result = Response.getDefaulTrueInstance();
		/**
		 * 2020-04-09,????????????????????????????????????
		 */
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("openId", userDTO.getOpenId());
//		paramMap.put("user_name", userDTO.getUser_name());
		//???????????????????????????
//		String userName = checkUserNameUnique(paramMap);
//		if(userName != null) {
//			result.setSuccess(false);
//			result.setCode("1");
//			result.setMessage("????????????????????????????????????????????????????????????");
//			return result;
//		}
		
		String openId = userDTO.getOpenId();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		int user_id = userVO.getUser_id();
		
		updateUserInfo(userDTO);
		
		/**2020-6-16??????????????????????????????????????????
		// ??????????????????????????????
		if(userDTO.getDept_id() != 0) {
			pubCommonService.updateUserDept(user_id, userDTO.getDept_id());
		}
		
		if(!userDTO.getCompany().equals(userVO.getCompany())) {
			// ???????????????????????????????????????????????????(???????????????)
			// ?????????????????????????????????
			QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", userDTO.getCompany());
			if(userVO.getCompany_id() == 0 && companyVO != null) {
				// ????????????
				String content = "????????????"+ userDTO.getCompany() +"???????????????????????????????????????????????????????????????????????????????????????-????????????-??????????????????????????????????????????";
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(openId);
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				messageCenterService.sendMsg(msgVO);
			}
			// ????????????????????????????????????????????????????????????
			pubCommonService.deleteQlyRhUserDept(user_id);
		}
		*/
		
		if (local_debug_no_boss == false) {
			// ????????????????????????,??????????????????
			logger.info("*************????????????????????????,??????????????????*************");
			Integer cust_id = getCustId(user_id);
			if(cust_id != null) {
				try {
					outerService.updateCustDetail(cust_id, userDTO.getUser_name(), userDTO.getTelphone());
				} catch (Exception e) {
					logger.error("?????????????????????????????????????????????????????????" + e);
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	@Override
	public Integer getCustId(int user_id) {
		logger.info("*************????????????????????????cust_id*************");
		logger.info("??????cust_id???????????????" + user_id);
		Integer cust_id = baseDAO.findOne(mapperNamespace + ".getCustId", user_id);
		logger.info("??????cust_id???????????????" + cust_id);
		return cust_id;
	}

	private String checkUserNameUnique(Map<String, Object> paramMap) {
		logger.info("*************???????????????????????????*************");
		logger.info("??????????????????????????????????????????" + paramMap);
		String result = baseDAO.findOne(mapperNamespace + ".checkUserNameUnique", paramMap);
		logger.info("??????????????????????????????????????????" + result);
		return result;
	}

	@Override
	public int updateUserInfo(QlyRhUserDTO userDTO) {
		userDTO.setModify_datetime(new Date());

		int c = baseDAO.update(mapperNamespace + ".updateUserInfo", userDTO);
		
		logger.info("*************[updateUserInfo]????????????CS_cache_temp_user_key*************");
		// ??????????????????
		String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, userDTO.getOpenId());
		dataCacheService.del(cacheKey);
		
		return c;
	}

	@Override
	public void addOrUpdateUserOperateLog(Map<String, Object> paramMap) {
		int c = baseDAO.update(mapperNamespace + ".updateUserOperateLog", paramMap);
		if (c == 0) {
			baseDAO.add(mapperNamespace + ".insertUserOperateLog", paramMap);
		}
	}

	@Override
	public List<QlyRhUserVO> queryQlyRhUsers() {
		return baseDAO.findListBy(mapperNamespace + ".queryQlyRhUsers");
	}

	@Override
	public List<Map<String, Object>> queryCancelRestrictUsers(Map<String, Object> param) {
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryCancelRestrictUsers", param);
		return list;
	}

	@Override
	public String getIsMember(String openId) {
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
//		int user_id = userVO.getUser_id();
		// ????????????
		String isMember = "0";
		
		if("1".equals(userVO.getIsMember())) {
			String member_end_date = userVO.getMember_end_date();
			if(StringUtils.isNotBlank(member_end_date)) {
				Date dt = new Date();
				String dateStr = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
				Date curtDate = DateUtil.parseDate(dateStr);
				Date memberEndDate = DateUtil.parseDate(member_end_date);
				long t1 = curtDate.getTime();
				// ???????????????
				long t2 = memberEndDate.getTime();
				
				if(t2 >= t1) {
					isMember = "1";
				}
			}
			
		}
		/** 2020-03-10 ?????????????????????????????????????????????????????????
		if (local_debug_no_boss == false) {
			// ????????????????????????,???????????????????????????
			Integer customer_id = getCustId(user_id);
			if(customer_id != null) {
				String app_code = "QLY";

				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int date = calendar.get(Calendar.DATE);
				String period = year + "-" + (month < 10 ? "0" + month : month) + "-" + (date < 10 ? "0" + date : date);
				int ret = outerService.queryValidOrder(customer_id, app_code, period);
				if (ret == 1) {
					isMember = "1";
				} else {
					isMember = "0";
				}
			} else {
				isMember = "0";
			}
		} else {
			isMember = userVO.getIsMember();
		}**/
		return isMember;
	}

	@Override
	public void addCustId(Map<String, Object> paramMap) {
		logger.info("*************???????????????????????????????????????/??????????????????????????????cust_id*************");
		logger.info("???????????????????????????????????????/??????????????????????????????cust_id???????????????" + paramMap);
		baseDAO.add(mapperNamespace + ".addCustId", paramMap);	
	}
	
	@Override
	public Response loginUser(String loginName, String password) {
		Response result = Response.getDefaulTrueInstance();
		QlyRhUserVO userVO = getQlyRhUserVO(loginName, null);
		if(userVO == null) {
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("?????????????????????");
		} else {
			if(password.equalsIgnoreCase(userVO.getPassword())) {
				
				// ???????????????????????????
				String token = "T_" + UUID.randomUUID().toString();
				String cacheKey = String.format(DataCacheConst.CS_cache_user_session_key, token);
				String jsonStr = JSON.toJSONString(userVO);
				dataCacheService.set(cacheKey, jsonStr, DataCacheConst.cache_sec_user_session_data);
				
				Map<String, String> retMap = new HashMap<String, String>();
				retMap.put("token", token);
				result.setData(retMap);
				result.setMessage("??????????????????");
			} else {
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("??????????????????????????????");
			}
		}
		
		return result;
	}
	
	private QlyRhUserVO getQlyRhUserVO(String loginName, String password) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", loginName);
		paramMap.put("password", password);
		
		QlyRhUserVO userVO = baseDAO.findOne(mapperNamespace + ".getQlyRhUserVO", paramMap);
		return userVO;
	}

	@Override
	public Response getUsersList(QlyRhAdminUserDTO userDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Date dt = new Date();
		String dateTime = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("user_name", userDTO.getUser_name());
        paramMap.put("city", userDTO.getCity());
        paramMap.put("company", userDTO.getCompany());
        paramMap.put("position", userDTO.getPosition());
        paramMap.put("register_startDate", userDTO.getRegister_startDate());
        paramMap.put("register_endDate", userDTO.getRegister_endDate());
        paramMap.put("isMember", userDTO.getIsMember());
        paramMap.put("dateTime", dateTime);
        paramMap.put("read_startNum", userDTO.getRead_startNum());
        paramMap.put("read_endNum", userDTO.getRead_endNum());
        paramMap.put("share_startNum", userDTO.getShare_startNum());
        paramMap.put("share_endNum", userDTO.getShare_endNum());
        paramMap.put("money", userDTO.getMoney());
        paramMap.put("create_datetime", userDTO.getCreate_datetime());
        paramMap.put("open_date", userDTO.getOpen_date());
        paramMap.put("readTimes_sort", userDTO.getReadTimes_sort());
        paramMap.put("shareNum_sort", userDTO.getShareNum_sort());
        paramMap.put("readNum_sort", userDTO.getReadNum_sort());
        paramMap.put("subscribe", userDTO.getSubscribe());
        
        // ?????????????????????
        if(StringUtils.isNotBlank(userDTO.getFromIndex()) && "3".equals(userDTO.getFromIndex())) {
        	Map<String, Object> resultMap = new HashMap<String, Object>();
        	PageQueryVO pageQueryVO = userDTO.getPageQueryVO();
        	
        	//????????????
            int totalRecord = 0;
            //??????????????????
            totalRecord = baseDAO.findOne(mapperNamespace + ".queryUsersListCountFromIndex", paramMap);
            
            //????????????
            Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
            
            //????????????
            List<Map<String, Object>> usersList = new ArrayList<Map<String, Object>>();
            
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (totalRecord > 0) {
            	list = baseDAO.findListByRange(mapperNamespace + ".queryUsersListFromIndex", paramMap, 
            			page.getStart(), page.getLimit());
            	
            	if(list != null && list.size() > 0) {
            		for(Map<String, Object> map : list) {
            			int userId = (int) map.get("user_id");
            			double money = orderService.getOrderTotalAmount(userId);
            			map.put("money", money);
                		usersList.add(map);
            		}
                }
            }
            resultMap.put("bodyList", usersList);
            resultMap.put("page", page);
            resp.setData(resultMap);
            
        } else {
        	resp = queryUsersList(paramMap, userDTO.getPageQueryVO());
        }
        
        return resp;
	}

	@Override
	public Response queryUsersList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//????????????
        int totalRecord = 0;
        //??????????????????
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryUsersListCount", paramMap);
        
        //????????????
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        //??????????????????
//        List<Map<String, String>> headList = new ArrayList<Map<String, String>>();
//        String[] heads = TableConstants.USER_TABLE_HEAD;
//		headList = HeadUtil.getHeadList(heads);
//		resultMap.put("headList", headList);
		
        //????????????
        List<Map<String, Object>> usersList = new ArrayList<Map<String, Object>>();
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".queryUsersList", paramMap, 
        			page.getStart(), page.getLimit());
        	
        	if(list != null && list.size() > 0) {
        		for(Map<String, Object> map : list) {
        			int userId = (int) map.get("user_id");
        			double money = orderService.getOrderTotalAmount(userId);
        			map.put("money", money);
            		usersList.add(map);
        		}
            }
        }
        resultMap.put("bodyList", usersList);
        resultMap.put("page", page);
        resp.setData(resultMap);
        
        return resp;
	}
	
	@Override
	public Response getUserInfoByOpenId(String openId) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		int user_id = userVO.getUser_id();
		
		// ????????????
		String isMember = this.getIsMember(openId);
		
		// ??????????????????
		String end_date = userVO.getMember_end_date();
		
		int totalShareNum = 0, totalRecordNum = 0;
		QlyRhUserStatisticsVO userStatisticsVO = baseDAO.findOne(mapperNamespace + ".getQlyRhUserStatisticsVO", openId);
		if(userStatisticsVO != null) {
			totalShareNum = userStatisticsVO.getTotal_share_num();
		}
		Integer n = articleService.getTotalRecordConditionCount(openId);
		if(n != null) {
			totalRecordNum = n.intValue();
		}
		
		resultMap.put("headImgUrl", userVO.getHeadImgUrl());
		resultMap.put("ewm_url", userVO.getEwm_url());
		resultMap.put("user_name", userVO.getUser_name());
		resultMap.put("telphone", userVO.getTelphone());
		resultMap.put("position", userVO.getPosition());
		resultMap.put("company", userVO.getCompany());
		resultMap.put("industry", userVO.getIndustry());
		resultMap.put("city", userVO.getCity());
		resultMap.put("isMember", isMember);
		resultMap.put("end_date", end_date);
		resultMap.put("total_share_num", totalShareNum);
		resultMap.put("total_record_num", totalRecordNum);
		
		resp.setData(resultMap);
		return resp;
	}

	private String getOrderEndDate(int user_id) {
		logger.info("*************????????????????????????*************");
		String end_date = baseDAO.findOne(mapperNamespace + ".getOrderEndDate", user_id);
		logger.info("???????????????????????????{}", end_date);
		return end_date;
	}

	@Override
	public Response queryUserShareArticlesList(String openId) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		resultMap.put("openId", openId);
		resultMap.put("user_name", userVO.getUser_name());
		List<Map<String, Object>> articlesList = baseDAO.findListBy(mapperNamespace + ".queryUserShareArticlesList", openId);
		if(articlesList != null && articlesList.size() > 0) {
			for(Map<String, Object> m : articlesList) {
				String article_uuid = (String) m.get("article_uuid");
				String category_name = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getArticleCategoryName", article_uuid);
				
				m.put("category_name", category_name);
			}
		}
		if(articlesList == null || (articlesList.size() > 0 && articlesList.get(0) == null)) {
			articlesList = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("articlesList", articlesList);
		resp.setData(resultMap);
		return resp;
	}

	@Override
	public Response queryUserReadArticlesList(String openId) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		resultMap.put("openId", openId);
		resultMap.put("user_name", userVO.getUser_name());
		List<Map<String, Object>> articlesList = baseDAO.findListBy(mapperNamespace + ".queryUserReadArticlesList", openId);
		if(articlesList != null && articlesList.size() > 0) {
			for(Map<String, Object> m : articlesList) {
				String article_uuid = (String) m.get("article_uuid");
				String category_name = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getArticleCategoryName", article_uuid);
				
				m.put("category_name", category_name);
			}
		}
		if(articlesList == null || (articlesList.size() > 0 && articlesList.get(0) == null)) {
			articlesList = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("articlesList", articlesList);
		resp.setData(resultMap);
		return resp;
	}
	
	@Override
	public QlyRhUserVO getQlyRhUserByToken(String token) {
		String cacheKey = String.format(DataCacheConst.CS_cache_user_session_key, token);
		String cacheDataJson = dataCacheService.get(cacheKey);
		QlyRhUserVO result = null;
		if (StringUtils.isNotBlank(cacheDataJson)) {
			result = JSON.parseObject(cacheDataJson, QlyRhUserVO.class);
		}
		
		return result;
	}
	
	@Override
	public List<QlyRhUserVO> queryNotPerfectInfoUsers() {
		return baseDAO.findListBy(mapperNamespace + ".queryNotPerfectInfoUsers");
	}

	@Override
	public List<QlyRhUserVO> queryMembershipRenewalUsers() {
		return baseDAO.findListBy(mapperNamespace + ".queryMembershipRenewalUsers");
	}
	
	@Override
	public void updateWxUserInfo(String openId, String appId) {
		String access_token = pubCommonService.getCacheAccessToken();

		if (StringUtils.isNotBlank(access_token)) {
			String userInfoStr = WxUtil.GetUserInfo(access_token, openId);
			logger.info("*************????????????userinfo*************" + userInfoStr);

			JSONObject userJSON = JSONObject.parseObject(userInfoStr);
			String retOpenId = userJSON.getString("openid");

			if (userJSON != null && StringUtils.isNotBlank(retOpenId)) {
				QlyRhUserDTO userVO = new QlyRhUserDTO();
				userVO.setOpenId(openId);
				userVO.setHeadImgUrl(userJSON.getString("headimgurl"));
				
				this.updateUserInfo(userVO);
				
				logger.info("********************??????????????????????????????????????????********************" + openId);
				callCrmService.syncUserInfoToWXG(appId, openId, userInfoStr);
				
			} else if (userJSON != null && userJSON.getIntValue("errcode") == 40001) {
				String errMsg = userJSON.getString("errmsg");
				if (errMsg.contains("access_token is invalid")) {
					logger.info("*************[getUserVOByOpenId]????????????AccessToken*************");
					dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
					dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
				}
			}

		}
		
	}
	
	@Override
	public Map<String, Object> queryQlyRhUserOperateLog(String openId) {
		return (Map<String, Object>) baseDAO.findMapBy(mapperNamespace + ".queryQlyRhUserOperateLog", openId);
	}
	
	@Override
	public void syncUserMemberEndDate() {
		List<QlyRhUserVO> userList = this.queryMembershipRenewalUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				int userId = user.getUser_id();
				
				String end_date = this.getOrderEndDate(userId);
				
				if(StringUtils.isNotBlank(end_date)) {
					QlyRhUserDTO userDTO = new QlyRhUserDTO();
					userDTO.setOpenId(user.getOpenId());
					userDTO.setMember_end_date(end_date);
					this.updateUserInfo(userDTO);
				}
				
			}
		}
	}
	
	@Override
	public int getQlyRhUserTotalPartner(Map<String, Object> paramMap) {
		return baseDAO.findOne(mapperNamespace + ".getQlyRhUserTotalPartner", paramMap);
	}
	
	@Override
	public void syncUserUnionid(String appId) {
		String accessToken = pubCommonService.getCacheAccessToken();
		List<QlyRhUserVO> userList = baseDAO.findListBy(mapperNamespace + ".queryNullUnionidUsers");
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String openId = user.getOpenId();
				
				String userInfoStr = WxUtil.GetUserInfo(accessToken, openId);
				logger.info("*************[syncUserUnionid]????????????userinfo*************" + userInfoStr);
				
				JSONObject userJSON = JSONObject.parseObject(userInfoStr);
				String retOpenId = userJSON.getString("openid");

				if (userJSON != null && StringUtils.isNotBlank(retOpenId)) {
					String unionid = userJSON.getString("unionid");
					if(StringUtils.isNotBlank(unionid)) {
						QlyRhUserDTO userVO = new QlyRhUserDTO();
						userVO.setOpenId(openId);
						userVO.setUnionid(unionid);
						
						this.updateUserInfo(userVO);
						
						logger.info("********************??????????????????????????????????????????********************" + openId);
						callCrmService.syncUserInfoToWXG(appId, openId, userInfoStr);
					}
					
				}
			}
		}
		
	}
	
	@Override
	public List<QlyRhUserVO> queryDNDUsers() {
		return baseDAO.findListBy(mapperNamespace + ".queryDNDUsers");
	}
	
	@Override
	public QlyRhUserVO getQlyRhUserByUnionid(String unionid) {
		return baseDAO.findOne(mapperNamespace + ".getQlyRhUserByUnionid", unionid);
	}
}
