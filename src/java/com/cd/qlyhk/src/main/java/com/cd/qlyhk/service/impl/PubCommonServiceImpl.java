package com.cd.qlyhk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.TableConstants;
import com.cd.qlyhk.dto.PushTypeDTO;
import com.cd.qlyhk.dto.QlyRhMessageRemindSetDTO;
import com.cd.qlyhk.dto.QlyRhOrgDeptDTO;
import com.cd.qlyhk.dto.QlyRhSysCategoryDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhCompanyVO;
import com.cd.qlyhk.vo.QlyRhDefaultCustomTableVO;
import com.cd.qlyhk.vo.QlyRhMessageRemindSetVO;
import com.cd.qlyhk.vo.QlyRhOrgDeptVO;
import com.cd.qlyhk.vo.QlyRhSubscribeVO;
import com.cd.qlyhk.vo.QlyRhSysAreaVO;
import com.cd.qlyhk.vo.QlyRhSysCategoryVO;
import com.cd.qlyhk.vo.QlyRhUserCustomQueryVO;
import com.cd.qlyhk.vo.QlyRhUserCustomTableVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqArticleImgDTO;
import com.cd.qlyhk.vo.ReqUserCustomQueryDTO;
import com.cd.qlyhk.vo.ReqUserCustomTableDTO;
import com.cd.rdf.base.dao.IBaseDAO;
/**
 * 公共接口
 */
@Service(IPubCommonService.BEAN_ID)
public class PubCommonServiceImpl implements IPubCommonService{
	
	private static final Logger logger = LoggerFactory.getLogger(PubCommonServiceImpl.class);
	
	private final String mapperNamespace = PubCommonServiceImpl.class.getName();
	
	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IUserService userService;

	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private IOuterService outerService;
	
	@Autowired
	private IMarketService marketService;
	
	@Override
	public String getCacheAccessToken() {
		JSONObject json = null;
		String access_token = dataCacheService.get(DataCacheConst.CS_cache_sys_token_key);
		
    	if(StringUtils.isBlank(access_token)) {
    		access_token = WxUtil.GetToken();
    		logger.info("*************获取微信access_token*************" + access_token);
    		json = JSONObject.parseObject(access_token);
			access_token = json.getString("access_token");
			dataCacheService.set(DataCacheConst.CS_cache_sys_token_key, access_token, DataCacheConst.cache_sec_weixin_token);
    	}
    	
    	return access_token;
	}
	
	@Override
	public String getCacheJsapiTicket(String access_token) {
		JSONObject json = null;
		String jsapi_ticket = dataCacheService.get(DataCacheConst.CS_cache_sys_ticket_key);
		
		if(StringUtils.isBlank(jsapi_ticket)) {
			jsapi_ticket = WxUtil.GetTicket(access_token);
			logger.info("*************获取微信jsapi_ticket*************" + jsapi_ticket);
			json = JSONObject.parseObject(jsapi_ticket);
			
			if("0".equals(json.getString("errcode"))) {
				jsapi_ticket = json.getString("ticket");
				dataCacheService.set(DataCacheConst.CS_cache_sys_ticket_key, jsapi_ticket, DataCacheConst.cache_sec_weixin_token);
			}
		}
		
		return jsapi_ticket;
	}
	
	@Override
	public Response getCategoryList(String type, String openId) {
		Response result = Response.getDefaulTrueInstance();
		try {
			List<QlyRhSysCategoryVO> retList = new ArrayList<QlyRhSysCategoryVO>();
			
			List<QlyRhSysCategoryVO> categoryList = (List) dataCacheService.getRhSysCategoryList();
			if(categoryList != null && categoryList.size() > 0) {
				for(QlyRhSysCategoryVO vo : categoryList) {
					if(StringUtils.isBlank(type) && "03".equals(vo.getType())) {// 03类型的是财税早报
						continue ;
					} else {
						// 判断当前用户是否已经加入公司了，如果加入了公司就需要显示公司模块
						if(StringUtils.isNotBlank(openId)) {
//							Map<String, Object> userMap = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getUserInCompany", openId);
							QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
							Map<String, Object> companyMap = getCompanyMap(userVO.getUnionid());
							if(!companyMap.isEmpty()) {
								if("02".equals(vo.getType())) {
//									QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
//									QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", userVO.getCompany());
//									vo.setCategory_name(companyVO.getAbbr_name());
									vo.setCategory_name((String)companyMap.get("abbreviation"));
									
									retList.add(vo);
								} else {
									retList.add(vo);
								}
							} else {
								if("02".equals(vo.getType())) {
									// 非公司成员不可见“品牌”模块的文章
									continue ;
								}
								retList.add(vo);
							}
						} else {
							/*
							if("02".equals(vo.getType())) {
								// 非公司成员不可见“品牌”模块的文章
								continue ;
							}*/
							retList.add(vo);
						}
						
						
					}
				}
				
				result.setData(retList);
			}
			result.setMessage("获取文章类别列表数据成功");
		} catch (Exception e) {
			logger.error("获取文章类别列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章类别列表失败");
		}
		return result;
	}

	@Override
	public Response getSubscribeList(String openId) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> subscribeList = new ArrayList<Map<String, Object>>();
			List<QlyRhSysCategoryVO> categoryList = baseDAO.findListBy(mapperNamespace + ".getSubscribeList", openId);
			if(categoryList != null && categoryList.size() > 0) {
				for(QlyRhSysCategoryVO categoryVO : categoryList) {
					Map<String, Object> resMap = new HashMap<String, Object>();
					resMap.put("category_id", categoryVO.getCategory_id());
					resMap.put("category_code", categoryVO.getCategory_code());
					resMap.put("category_name", categoryVO.getCategory_name());
					resMap.put("type", categoryVO.getType());
					subscribeList.add(resMap);
				}
				resultMap.put("subscribeList", subscribeList);
				result.setData(resultMap);
			}
			
		} catch (Exception e) {
			logger.error("获取已订阅类别列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取已订阅类别列表失败");
		}
		return result;
	}

	@Override
	public Response saveSubscribeArticles(QlyRhSysCategoryDTO categoryDTO) {
		Response result = Response.getDefaulTrueInstance();
		String openId = categoryDTO.getOpenId();
		int userId = getUserId(openId);
		List<QlyRhSysCategoryVO> categoryList = categoryDTO.getCategoryList();
		baseDAO.delete(mapperNamespace + ".deleteSubscribeArticles", userId);
		if(CollectionUtils.isNotEmpty(categoryList)) {
			for(int i = 0; i < categoryList.size(); i++) {
				QlyRhSysCategoryVO categoryVO = categoryList.get(i);
				
				QlyRhSubscribeVO subscribeVO = new QlyRhSubscribeVO();
				subscribeVO.setUser_id(userId);
				subscribeVO.setCategory_id(categoryVO.getCategory_id());
				subscribeVO.setModify_datetime(new Date());
				baseDAO.add(mapperNamespace + ".insertSubscribeArticles", subscribeVO);
			}
		}
		return result;
	}

	@Override
	public Response saveMessageRemindSet(QlyRhMessageRemindSetDTO messageRemindSetDTO) {
		Response result = Response.getDefaulTrueInstance();
		List<PushTypeDTO> push_settings = messageRemindSetDTO.getPush_settings();
		String push_type = JSON.toJSONString(push_settings);
		messageRemindSetDTO.setPush_type(push_type);
		messageRemindSetDTO.setModify_datetime(new Date());
		
		String openId = messageRemindSetDTO.getUserId();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		messageRemindSetDTO.setUser_id(userVO.getUser_id());
		
		int c = baseDAO.update(mapperNamespace + ".updateMessageRemindSet", messageRemindSetDTO);
		if(c == 0) {
			insertQlyRhMessageRemindSet(messageRemindSetDTO);
		}
		return result;
	}
	
	@Override
	public void insertQlyRhMessageRemindSet(QlyRhMessageRemindSetDTO messageRemindSetDTO) {
		baseDAO.add(mapperNamespace + ".insertQlyRhMessageRemindSet", messageRemindSetDTO);
	}

	@Override
	public Response getMessageRemindSet(String openId) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhMessageRemindSetVO messageRemindSetVO = baseDAO.findOne(mapperNamespace + ".getMessageRemindSet", openId);	
		if(messageRemindSetVO != null) {
			resultMap.put("remind_type", messageRemindSetVO.getRemind_type());
			
			String push_type = messageRemindSetVO.getPush_type();
			JSONArray push_settings = JSONObject.parseArray(push_type);
			
			resultMap.put("push_settings", push_settings);
			result.setData(resultMap);
		}
		return result;
	}

	@Override
	public int getUserId(String openId) {
		int userId = baseDAO.findOne(mapperNamespace + ".getUserId", openId);
		return userId;
	}

	@Override
	public Response networkingRadar(String openId) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//今日阅读数
        int totalReaderToday = 0;
        totalReaderToday = articleService.getCountRecordToday(openId);
		// 累计阅读数
		int totalReader = 0;
		totalReader = articleService.getTotalCountRecord(openId);
		//是否会员
		String isMember = userService.getIsMember(openId);
		
		resultMap.put("today_reader", totalReaderToday);
		resultMap.put("total_reader", totalReader);
		resultMap.put("isMember", isMember);
		resp.setData(resultMap);
		return resp;
	}

	@Override
	public Response queryAreaNonDistrict() {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<QlyRhSysAreaVO> sysAreaList = (List) dataCacheService.querySysAreaList();
			//省列表
			List<QlyRhSysAreaVO> provinces = new ArrayList<QlyRhSysAreaVO>();
			//市列表
			List<QlyRhSysAreaVO> cities = new ArrayList<QlyRhSysAreaVO>();
			for(QlyRhSysAreaVO sysAreaVO : sysAreaList) {
				int areaLevel = sysAreaVO.getAreaLevel();
				if(areaLevel == 1) {
					provinces.add(sysAreaVO);
				} else if(areaLevel == 2) {
					cities.add(sysAreaVO);
				}
			}
			if(provinces != null && provinces.size() > 0) {
				resultMap.put("provinces", provinces);
			}
			if(cities != null && cities.size() > 0) {
				resultMap.put("cities", cities);
			}
			result.setData(resultMap);
		} catch (Exception e) {
			logger.error("获取行政区域列表（省、市）异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取行政区域列表（省、市）失败");
		}
		return result;
	}

	@Override
	public Response queryAreaDistrict(String areaCode) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<QlyRhSysAreaVO> sysAreaList = (List) dataCacheService.querySysAreaList();
			//区/县列表
			List<QlyRhSysAreaVO> districts = new ArrayList<QlyRhSysAreaVO>();
			for(QlyRhSysAreaVO districtsVO : sysAreaList) {
				String cityCode = districtsVO.getParentAreaCode();
				if(cityCode.equals(areaCode)) {
					districts.add(districtsVO);
				}
			}
			if(districts != null && districts.size() > 0) {
				resultMap.put("districts", districts);
				result.setData(resultMap);
			}
		} catch (Exception e) {
			logger.error("获取行政区域列表（区/县）异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取行政区域列表（区/县）失败");
		}
		return result;
	}
	
	@Override
	public Response queryUserCustomQuery(int userId, String moduleCode) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//列表
        List<Map<String, Object>> customList = new ArrayList<Map<String, Object>>();
        
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
        paramMap.put("moduleCode", moduleCode);
        
		List<QlyRhUserCustomQueryVO> list = baseDAO.findListBy(mapperNamespace + ".queryUserCustomQuery", paramMap);
		if(list != null && list.size() > 0) {
			for(QlyRhUserCustomQueryVO vo : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", vo.getId());
				map.put("name", vo.getName());
				customList.add(map);
			}
		}
		resultMap.put("customList", customList);
		result.setData(resultMap);
		
		return result;
	}
	
	@Override
	public void insertUserCustomQuery(ReqUserCustomQueryDTO vo) {
		baseDAO.add(mapperNamespace + ".insertUserCustomQuery", vo);
	}
	
	@Override
	public Response queryDataForUserCustomQuery(ReqUserCustomQueryDTO vo) {
		Response result = Response.getDefaulTrueInstance();
		QlyRhUserCustomQueryVO customQueryVO = baseDAO.findOne(mapperNamespace + ".getUserCustomQueryVO", vo);
		
		if(customQueryVO != null) {
			String jsonStr = customQueryVO.getData();
			Map<String, Object> paramMap = JSONObject.parseObject(jsonStr);
			if(TableConstants.MODULE_CODE_ARTICLE.equals(customQueryVO.getModule_code())) {// 文章列表
				if(!paramMap.isEmpty()) {
					result = articleService.queryAdminArticlesList(paramMap, vo.getPageQueryVO());
				}
				
			} else if(TableConstants.MODULE_CODE_ARTICLE_SHARE.equals(customQueryVO.getModule_code())) {// 文章分享阅读情况列表
				if(!paramMap.isEmpty()) {
					result = articleService.queryAdminArticlesShareAndReadList(paramMap, vo.getPageQueryVO());
				}
				
			} else if(TableConstants.MODULE_CODE_USER.equals(customQueryVO.getModule_code())) {// 用户列表
				if(!paramMap.isEmpty()) {
					result = userService.queryUsersList(paramMap, vo.getPageQueryVO());
				}
				
			} else if(TableConstants.MODULE_CODE_MARKET.equals(customQueryVO.getModule_code())) {// 分销情况列表
				if(!paramMap.isEmpty()) {
					result = marketService.queryRetailList(paramMap, vo.getPageQueryVO());
				}
				
			}
			
		}
		
		return result;
	}
	
	@Override
	public Response deleteUserCustomQuery(int customQueryId, int userId) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("customQueryId", customQueryId);
        paramMap.put("userId", userId);
        
		int c = baseDAO.update(mapperNamespace + ".deleteUserCustomQuery", paramMap);
		if(c > 0) {
			result.setMessage("删除用户自定义查询分类成功");
		} else {
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("删除用户自定义查询分类失败");
		}
		
		return result;
	}
	
	@Override
	public void insertOrUpdateUserCustomTable(ReqUserCustomTableDTO vo) {
		int result = baseDAO.update(mapperNamespace + ".updateUserCustomTable", vo);
		if(result == 0) {
			baseDAO.add(mapperNamespace + ".insertUserCustomTable", vo);
		}
	}

	@Override
	public Response queryUserCustomTable(int userId, String moduleCode) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
        
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
        paramMap.put("moduleCode", moduleCode);
        
		QlyRhUserCustomTableVO customTableVO = baseDAO.findOne(mapperNamespace + ".getUserCustomTableVO", paramMap);
		JSONArray temArr = new JSONArray();
		if(customTableVO != null) {
			temArr = JSONArray.parseArray(customTableVO.getData());
			resultMap.put("headList", temArr);
			result.setData(resultMap);
		} else {
			result = queryDefaultCustomTable(moduleCode);
		}
		
		return result;
	}
	
	@Override
	public Response queryDefaultCustomTable(String moduleCode) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
        
		List<QlyRhDefaultCustomTableVO> list = (List) dataCacheService.queryRhDefaultCustomTableList(moduleCode);
		
		resultMap.put("headList", list);
		result.setData(resultMap);
		return result;
	}
	
	@Override
	public int selectArticleCategoryCount(int category_id) {
		Integer count = baseDAO.findOne(mapperNamespace + ".selectArticleCategoryCount", category_id);
		if(count == null) {
			count = 0;
		}
		logger.info("获取删除类别下的文章数:" + count);
		return count;
	}
	
	@Override
	public Response deleteSysCategory(int category_id) {
		Response result = Response.getDefaulTrueInstance();
		try {
			int count = selectArticleCategoryCount(category_id);
			if(count != 0) {
				baseDAO.delete(mapperNamespace + ".deleteArticleCategory", category_id);
			}
			baseDAO.delete(mapperNamespace + ".deleteSysCategory", category_id);
			logger.info("*************[deleteCategory]清除缓存CS_cache_sys_category_key*************");
			// 清除缓存数据
			String cacheKey = DataCacheConst.CS_cache_sys_category_key;
			dataCacheService.del(cacheKey);
			result.setMessage("删除类别列表成功");
		} catch (Exception e) {
			logger.error("删除类别列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("删除类别列表失败");
		}
		return result;
	}

	@Override
	public Response addSysCategoryList(QlyRhSysCategoryDTO sysCategoryDTO) {
		Response result = Response.getDefaulTrueInstance();
		try {
			List<QlyRhSysCategoryVO> categoryList = sysCategoryDTO.getCategoryList();
			if(CollectionUtils.isNotEmpty(categoryList)) {
				for(QlyRhSysCategoryVO categoryVO : categoryList) {
					baseDAO.add(mapperNamespace + ".addSysCategory", categoryVO);
				}
				logger.info("*************[addSysCategoryList]清除缓存CS_cache_sys_category_key*************");
				// 清除缓存数据
				String cacheKey = DataCacheConst.CS_cache_sys_category_key;
				dataCacheService.del(cacheKey);
				result.setMessage("添加类别列表成功");
			}
		} catch (Exception e) {
			logger.error("添加类别列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("添加类别列表失败");
		}
		return result;
	}
	
	@Override
	public Response updateSysCategory(QlyRhSysCategoryDTO sysCategoryDTO) {
		Response result = Response.getDefaulTrueInstance();
		try {
			List<QlyRhSysCategoryVO> categoryList = sysCategoryDTO.getCategoryList();
			if(CollectionUtils.isNotEmpty(categoryList)) {
				for(int i = 0, j = 1; i < categoryList.size(); i++,j++) {
					QlyRhSysCategoryVO categoryVO = categoryList.get(i);
					categoryVO.setOrder_index(j);
					baseDAO.update(mapperNamespace + ".updateSysCategory", categoryVO);
				}
				logger.info("*************[updateSysCategory]清除缓存CS_cache_sys_category_key*************");
				// 清除缓存数据
				String cacheKey = DataCacheConst.CS_cache_sys_category_key;
				dataCacheService.del(cacheKey);
				result.setMessage("更新类别列表成功");
			}
		} catch (Exception e) {
			logger.error("更新类别列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("更新类别列表失败");
		}
		return result;
	}
	
	@Override
	public QlyRhSysCategoryVO getQlyRhSysCategoryVO(int category_id) {
		return baseDAO.findOne(mapperNamespace + ".getQlyRhSysCategoryVO", category_id);
	}
	
	@Override
	public int getCategoryIdByType(String type) {
		int category_id = 1;
		List<QlyRhSysCategoryVO> categoryList = (List) dataCacheService.getRhSysCategoryList();
		if(categoryList != null && categoryList.size() > 0) {
			for(QlyRhSysCategoryVO vo : categoryList) {
				if(StringUtils.isNotBlank(type) && type.equals(vo.getType())) {
					category_id = vo.getCategory_id();
					break;
				}
			}
		}
		
		return category_id;
	}
	
	@Override
	public QlyRhMessageRemindSetVO getMessageRemindSetVOByUserId(int userId) {
		QlyRhMessageRemindSetVO messageRemindSetVO = baseDAO.findOne(mapperNamespace + ".getMessageRemindSetVOByUserId", userId);
		return messageRemindSetVO;
	}
	
	@Override
	public Response uploadArticleImg(ReqArticleImgDTO imgDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> urlList = new ArrayList<String>();
		
		String accessToken = this.getCacheAccessToken();
		String[] ids = imgDTO.getIds();
		if(ids != null && ids.length > 0) {
			for(String imgId : ids) {
				String imgUrl = WxUtil.GetImgUrl(accessToken, imgId, imgDTO.getFilePath(), imgDTO.getFileUrlPath());
				
				if(StringUtils.isNotBlank(imgUrl)) {
					urlList.add(imgUrl);
				}
			}
		}
		
		resultMap.put("url", urlList);
		resp.setData(resultMap);
		
		return resp;
	}
	
	@Override
	public Response queryOrgDeptTree(int org_id, int parent_id, int level) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> deptList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("org_id", org_id);
		if(parent_id != 0) {
			param.put("parent_id", parent_id);
		}
		
		if(level != 0) {
			param.put("level", level);
		}
		
		List<QlyRhOrgDeptVO> list = baseDAO.findListBy(mapperNamespace + ".queryOrgDeptTree", param);
		if(list != null && list.size() > 0) {
			for(QlyRhOrgDeptVO deptVO : list) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", deptVO.getId());
				m.put("parent_id", deptVO.getParent_id());
				m.put("code", deptVO.getCode());
				m.put("name", deptVO.getName());
				m.put("level", deptVO.getLevel());
				
				deptList.add(m);
			}
		}
		
		resultMap.put("deptList", deptList);
		resp.setData(resultMap);
		
		return resp;
	}
	
	@Override
	public Response queryOrgDeptList(QlyRhOrgDeptDTO deptDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = outerService.getCompanyGroupList(deptDTO.getCompany_id());
		if(list != null && list.size() > 0) {
			resultMap.put("deptList", list);
			resp.setData(resultMap);
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("获取公司部门列表失败");
		}
		/*
		 * QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName()
		 * + ".getQlyRhCompanyByName", deptDTO.getCompany_name()); if(companyVO == null)
		 * { resp.setCode("1"); resp.setMessage("输入的公司名称未创建公司"); return resp; }
		 * 
		 * resp = queryOrgDeptTree(companyVO.getCompany_id(), 0, 0);
		 * resp.setMessage("查询部门列表成功");
		 */
		return resp;
	}
	
	@Override
	public Response queryDeptLowerLevel(QlyRhOrgDeptDTO deptDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", deptDTO.getCompany_name());
		if(companyVO == null) {
			resp.setCode("1");
        	resp.setMessage("输入的公司名称未创建公司");
            return resp;
		}
		
		resp = queryOrgDeptTree(companyVO.getCompany_id(), deptDTO.getParent_id(), 0);
		resp.setMessage("查询部门列表成功");
		return resp;
	}
	
	@Override
	public Response addDept(QlyRhOrgDeptDTO deptDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		Map<String, Object> newOrgDept = new HashMap<String, Object>();
		newOrgDept.put("name", deptDTO.getName());
		newOrgDept.put("parent_id", deptDTO.getParent_id());
		
		// 是否成功
		int issucc = outerService.addCompanyGroup(newOrgDept);
		if (issucc == 1) {
			resp.setMessage("新增部门成功");
		} else {
			resp.setCode("1");
			resp.setMessage("新增部门失败");
		}
		
		return resp;
	}
	
	@Override
	public void updateUserDept(int userId, int deptId) {
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("userId", userId);
		update.put("deptId", deptId);
		int result = baseDAO.update(mapperNamespace + ".updateQlyRhUserDept", update);
		if(result == 0) {
			baseDAO.add(mapperNamespace + ".insertQlyRhUserDept", update);
		}
	}
	
	@Override
	public void deleteQlyRhUserDept(int userId) {
		baseDAO.add(mapperNamespace + ".deleteQlyRhUserDept", userId);
	}
	
	
	private Map<String, Object> getCompanyMap(String unionid) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		Map<String, Object> companyMap = outerService.getCompanyInfo(unionid);
		if(!companyMap.isEmpty()) {
			int companyId = (int) companyMap.get("company_id");
			String company_name = (String) companyMap.get("company_name");
			String abbreviation = (String) companyMap.get("abbreviation");
			String isadmin = (String) companyMap.get("isadmin");
			
			retMap.put("companyId", companyId);
			retMap.put("company_name", company_name);
			retMap.put("abbreviation", abbreviation);
			retMap.put("isadmin", isadmin);
		}
		
		return retMap;
	}
	
	@Override
	public Response groupChange(QlyRhOrgDeptDTO deptDTO) {
		Response resp = Response.getDefaulTrueInstance();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(deptDTO.getOpenId());
		
		// 是否成功
		boolean issucc = outerService.groupChange(deptDTO.getOld_group_id(), deptDTO.getGroup_id(), userVO.getUnionid());
		if (issucc) {
			resp.setMessage("修改所属部门成功");
		} else {
			resp.setCode("1");
			resp.setMessage("修改所属部门失败");
		}
		
		return resp;
	}
	
	@Override
	public Response getLocation(String latitude, String longitude) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		Map<String, String> addrMap = getAddressComponent(latitude, longitude);
		if(!addrMap.isEmpty()) {
			QlyRhSysAreaVO areaVO = null;
			String district = addrMap.get("district");
			String city = addrMap.get("city");
			String province = addrMap.get("province");
			
			if(StringUtils.isNotBlank(district)) {
				areaVO = baseDAO.findOne(mapperNamespace + ".getSysAreaByAreaName", district);
			}
			
			if(areaVO == null && StringUtils.isNotBlank(city)) {
				areaVO = baseDAO.findOne(mapperNamespace + ".getSysAreaByAreaName", city);
			}
			
			if(areaVO == null && StringUtils.isNotBlank(province)) {
				areaVO = baseDAO.findOne(mapperNamespace + ".getSysAreaByAreaName", province);
			}
			
			if(areaVO != null) {
				String areaName = areaVO.getAreaFullName();
				areaName = areaName.replaceAll("\\.", "-");
				resultMap.put("area_code", areaVO.getAreaCode());
				resultMap.put("area_name", areaName);
			}
			
		}
		resp.setData(resultMap);
		resp.setMessage("获取地理位置成功");
		
		return resp;
	}
	
	private Map<String, String> getAddressComponent(String latitude, String longitude) {
		Map<String, String> retMap = new HashMap<String, String>();
		
		try {
			String latlonStr = latitude + "," + longitude;
			String secr = "/ws/geocoder/v1?key=CAQBZ-HJXWR-T6LW2-WYPN2-UD353-GTB6F&location=" + latlonStr + "XuiMdFcGWZMx60jhyxBOJxf7lVjQivEd";
			String sg = DigestUtils.md5Hex(secr);

			String params = "key=CAQBZ-HJXWR-T6LW2-WYPN2-UD353-GTB6F&location="+ latlonStr +"&sig=" + sg.toLowerCase();
			String result = HttpRequestUtil.doGet("https://apis.map.qq.com/ws/geocoder/v1?" + params);
			logger.debug("获取地理位置返回结果：" + result);
			
			JSONObject jsonObject = JSONObject.parseObject(result);
			int status = (int) jsonObject.get("status");
			if (status == 0) {
				JSONObject resultJson = jsonObject.getJSONObject("result");
				JSONObject addrJson = resultJson.getJSONObject("address_component");
				String province = (String) addrJson.get("province");
				String city = (String) addrJson.get("city");
				String district = (String) addrJson.get("district");
				
				retMap.put("province", province);
				retMap.put("city", city);
				retMap.put("district", district);
			}

		} catch (Exception e) {
			logger.error("获取地理位置出错：: + e");
		}
		
		return retMap;
	}
	
	/*
	private JSONArray getDefaultCustomTable(String moduleCode) {
		JSONArray temArr = new JSONArray();
		
		if(TableConstants.MODULE_CODE_ARTICLE.equals(moduleCode)) {
			temArr = HeadUtil.getDefaulArticleHead();
		}
		
//		String[] defaultHead = null;
//		String[] defaultHeadName = null;
//		if(TableConstants.MODULE_CODE_ARTICLE.equals(moduleCode)) {
//			defaultHead = TableConstants.DEFAULT_ARTICLE_HEAD[0].split(";");
//			defaultHeadName = TableConstants.DEFAULT_ARTICLE_HEAD[1].split(";");
//		} else if(TableConstants.MODULE_CODE_USER.equals(moduleCode)) {
//			defaultHead = TableConstants.DEFAULT_USER_HEAD[0].split(";");
//			defaultHeadName = TableConstants.DEFAULT_USER_HEAD[1].split(";");
//		}
//		
//		if(defaultHead != null) {
//			for(int i = 0; i < defaultHead.length; i++) {
//				JSONObject jsonObj = new JSONObject();
//				jsonObj.put("head_key", defaultHead[i]);
//				jsonObj.put("head_name", defaultHeadName[i]);
//				temArr.add(jsonObj);
//			}
//		}
		
		return temArr;
	}*/
}
