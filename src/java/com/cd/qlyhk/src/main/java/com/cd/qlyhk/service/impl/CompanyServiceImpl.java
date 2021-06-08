package com.cd.qlyhk.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import com.cd.qlyhk.api.prov.service.ICallCrmService;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.dto.QlyRhCompanyDTO;
import com.cd.qlyhk.dto.QlyRhOrgDeptDTO;
import com.cd.qlyhk.dto.QlyRhReleaseArticleDTO;
import com.cd.qlyhk.dto.QlyRhTeamArticleDTO;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.reptile.WeixinArticePipeline;
import com.cd.qlyhk.reptile.WeixinArticeProcessor;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.ICompanyService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateHelper;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.vo.QlyRhCompanyVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhUserCompanyVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.RespArticleReadCondDetailsVO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.rdf.base.dao.IBaseDAO;

import us.codecraft.webmagic.Spider;

@Service(ICompanyService.BEAN_ID)
public class CompanyServiceImpl implements ICompanyService {

	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	private final String mapperNamespace = CompanyServiceImpl.class.getName();

	@Value("${frontend.url}")
	public String frontendUrl;
	
	@Autowired
	private IBaseDAO baseDAO;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private IMessageCenterService messageCenterService;
	
	@Autowired
	private IPubCommonService pubCommonService;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IOuterService outerService;
	
	@Autowired
	private ICallCrmService callCrmService;
	
	@Override
	public Response insertQlyRhCompany(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		String company_name = vo.getCompany_name();
		String openId = vo.getOpenId();
		
		// 校验公司名称是否已存在
		QlyRhCompanyVO companyVO = baseDAO.findOne(mapperNamespace + ".getQlyRhCompanyByName", company_name);
		if(companyVO == null) {
			companyVO = new QlyRhCompanyVO();
			companyVO.setCompany_name(company_name);
			companyVO.setAbbr_name(vo.getAbbr_name());
			companyVO.setSocial_credit_code(vo.getSocial_credit_code());
			companyVO.setName(vo.getName());
			companyVO.setMobile_phone(vo.getMobile_phone());
			
			baseDAO.add(mapperNamespace + ".insertQlyRhCompany", companyVO);
			
			// 创建部门
			QlyRhOrgDeptDTO deptDTO = new QlyRhOrgDeptDTO();
			deptDTO.setName(vo.getAbbr_name());
			deptDTO.setOrg_id(companyVO.getCompany_id());
			Response addDeptRet = pubCommonService.addDept(deptDTO);
			
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			QlyRhUserCompanyVO userCompanyVO = new QlyRhUserCompanyVO();
			userCompanyVO.setCompany_id(companyVO.getCompany_id());
			userCompanyVO.setUser_id(userVO.getUser_id());
			
			baseDAO.add(mapperNamespace + ".insertQlyRhUserCompany", userCompanyVO);
			
			// 创建用户与部门的关联
			int deptId = 0;
			if("0".equals(addDeptRet.getCode())) {
				deptId = Integer.parseInt((String) addDeptRet.getData());
				pubCommonService.updateUserDept(userVO.getUser_id(), deptId);
			}
			
			// 更新用户信息
			QlyRhUserDTO updateUser = new QlyRhUserDTO();
			updateUser.setOpenId(openId);
			updateUser.setCompany(company_name);
			userService.updateUserInfo(updateUser);
			
			logger.info("*************[insertQlyRhCompany]清除缓存CS_cache_temp_user_key*************");
			// 清除缓存数据
			String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, openId);
			dataCacheService.del(cacheKey);
			
			// 发送消息
			String content = "创建公司成功，队长，快来增加一篇公司分享好文吧~";
			SendMessageVO msgVO = new SendMessageVO();
			msgVO.setTouser(openId);
			msgVO.setContent(content);
			msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
			messageCenterService.sendMsg(msgVO);
			
			// 给公司全部成员发送消息(被动加入时)
			Map<String, Object> paramMap = new HashMap<String, Object>();
	        paramMap.put("company_name", company_name);
	        paramMap.put("openId", openId);
	        
			List<Map<String, Object>> userList = baseDAO.findListBy(mapperNamespace + ".queryCompanyAllUser", paramMap);
			if(userList != null && userList.size() > 0) {
				for(Map<String, Object> m : userList) {
					String toOpenId = (String) m.get("openId");
					int user_id = (int) m.get("user_id");
					
					if(openId.equals(toOpenId)) {
						continue ;
					}
					
					if(deptId != 0) {
						// 被动加入的用户，需要初始化一条部门关联
						pubCommonService.updateUserDept(user_id, deptId);
					}
					
					content = "您的公司"+ company_name +"创建了好文分享公司，快来分享公司好文吧！（您可以在个人中心-设置名片-公司中修改名称脱离当前公司）";
					msgVO = new SendMessageVO();
					msgVO.setTouser(toOpenId);
					msgVO.setContent(content);
					msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
					messageCenterService.sendMsg(msgVO);
				}
			}
			
			resp.setMessage("创建公司成功");
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("公司名称已存在");
		}
		
		return resp;
	}
	
	@Override
	public Response createCompany(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		
		// 社会信用代码唯一性校验
		boolean flag = outerService.checkCustomerCode(vo.getCompany_code());
		if(flag) {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("该社会信用代码已存在");
			return resp;
		}
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		// 校验注册用户是否已经加入公司
//		flag = outerService.checkRegistered(userVO.getUser_name(), userVO.getTelphone(), userVO.getUnionid());
//		if(flag) {
//			resp.setCode(Constants.RESPONSE_CODE_FAIL);
//			resp.setMessage("该手机号码已存在");
//			return resp;
//		}
		
		int cust_id = outerService.addOrUpdateCompany(vo.getCompany_name(), vo.getAbbr_name(), vo.getCompany_code(), userVO.getTelphone(), userVO.getUnionid());
		
		if(cust_id != 0) {
			// 调用微销官接口同步公司下的成员
			callCrmService.setUserCust(userVO.getUnionid(), cust_id);
			resp.setMessage("创建公司成功");
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("创建公司失败");
		}
		
		return resp;
	}
	
	@Override
	public Response joinCompany(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		/*String company_name = vo.getCompany_name();
		String openId = vo.getOpenId();
		
		Map<String, Object> retMap = baseDAO.findOne(mapperNamespace + ".getQlyRhCompanyByName", company_name);
		if(retMap != null && !retMap.isEmpty()) {
			String retOpenId = (String) retMap.get("openId");
			String retUserName = retMap.get("user_name") == null ? "" : (String) retMap.get("user_name");
			
			QlyRhUserDTO updateUser = new QlyRhUserDTO();
			updateUser.setOpenId(openId);
			updateUser.setCompany(company_name);
			userService.updateUserInfo(updateUser);
			
			logger.info("*************[joinCompany]清除缓存CS_cache_temp_user_key*************");
			// 清除缓存数据
			String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, openId);
			dataCacheService.del(cacheKey);
			
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			
			// 发送消息
			String content = userVO.getUser_name() + "您好，" + company_name + "公司欢迎您，进入获客好文-品牌开始分享";
			SendMessageVO msgVO = new SendMessageVO();
			msgVO.setTouser(openId);
			msgVO.setContent(content);
			msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
			messageCenterService.sendMsg(msgVO);
						
			content = "有新的伙伴" + userVO.getUser_name() + "加入了公司";
			msgVO = new SendMessageVO();
			msgVO.setTouser(retOpenId);
			msgVO.setContent(content);
			msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
			messageCenterService.sendMsg(msgVO);
			
			resp.setMessage("加入公司成功");
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("公司名称不存在");
		}*/
		String openId = vo.getOpenId();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("company_id", vo.getCompany_id());
		param.put("group_id", vo.getGroup_id());
		param.put("user_name", userVO.getUser_name());
		param.put("telphone", userVO.getTelphone());
		param.put("unionid", userVO.getUnionid());
		
		// 是否成功
		int issucc = outerService.inviteMember(param);
		if (issucc == 1) {
			// 调用微销官接口同步公司下的成员
			callCrmService.setUserCust(userVO.getUnionid(), vo.getCompany_id());
			resp.setMessage("邀请成员加入成功");
		} else {
			resp.setCode("1");
			resp.setMessage("邀请成员加入失败");
		}
		
		return resp;
	}
	
	@Override
	public Response getCompanyInfo(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		
		Map<String, Object> companyMap = outerService.getCompanyDetailInfo(vo.getCompany_id());
		if(!companyMap.isEmpty()) {
			resp.setData(companyMap);
			resp.setMessage("查询公司信息成功");
		}
		
		return resp;
	}
	
	@Override
	public Response editCompany(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		/**QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
		QlyRhCompanyVO companyVO = new QlyRhCompanyVO();
		companyVO.setCompany_id(userVO.getCompany_id());
		companyVO.setAbbr_name(vo.getAbbr_name());
		companyVO.setModify_datetime(new Date());
		baseDAO.update(mapperNamespace + ".updateQlyRhCompany", companyVO);
		
		if(StringUtils.isNotBlank(vo.getReceiverOpenId()) && !vo.getOpenId().equals(vo.getReceiverOpenId())) {
			// 公司转让
			teamTransfer(vo);
			logger.info("*************公司转让成功*************");
		}*/
		if(StringUtils.isBlank(vo.getCompany_name()) && StringUtils.isBlank(vo.getAbbr_name()) && StringUtils.isBlank(vo.getCompany_code())) {
			// 都没修改，直接返回
			resp.setMessage("修改公司信息成功");
			return resp;
		}
		
		// 是否成功
		Map<String, String> resultMap = outerService.updateCompanyInfo(vo.getCompany_id(), vo.getCompany_name(), vo.getAbbr_name(), vo.getCompany_code(), null);
		if(!resultMap.isEmpty()) {
			if("0".equals(resultMap.get("code"))) {
				resp.setMessage("修改公司信息成功");
			} else {
				resp.setCode("1");
				resp.setMessage(resultMap.get("message"));
			}
			
		} else {
			resp.setCode("1");
			resp.setMessage("修改公司信息失败");
		}
		
		return resp;
	}

	@Override
	public Response addReleaseArticle(QlyRhReleaseArticleDTO releaseArticleDTO, String articleImgUpload, String articleImgUrl) {
		Response resp = Response.getDefaulTrueInstance();
		String articleUrl = releaseArticleDTO.getArticle_url();
		String openId = releaseArticleDTO.getOpenId();
		
		if(articleUrl.contains("https://mp.weixin.qq.com")) {
//			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			int companyId = releaseArticleDTO.getCompany_id();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("articleUrl", articleUrl);
			paramMap.put("companyId", companyId);
			
			QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getQlyRhReleaseArticleVO", paramMap);
    		if(articleVO != null) {
    			logger.info("***********文章已发布*********************");
    			resp.setCode(Constants.RESPONSE_CODE_FAIL);
    			resp.setMessage("该文章已发布");
    		} else {
    			Map<String, Object> param = new HashMap<String, Object>();
    			param.put("openId", openId);
    			param.put("filePath", articleImgUpload);
    			param.put("fileUrlPath", articleImgUrl);
    			param.put("companyId", companyId);
    			param.put("shareText", releaseArticleDTO.getShare_text());
    			
    			String urls[] = {articleUrl};
    			Spider.create(new WeixinArticeProcessor()).addPipeline(new WeixinArticePipeline(param)).addUrl(urls).thread(1).run();
    			resp.setMessage("文章发布成功");
    			
    			// 推送公司新文章消息
        		messageCenterService.sendNewTeamArticleMsg(companyId);
    		}
    		
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("只能发布微信公众号的文章");
		}
		
		return resp;
	}
	
	@Override
	public Response queryReleaseArticleList(QlyRhReleaseArticleDTO releaseArticleDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(releaseArticleDTO.getOpenId());
		int companyId = releaseArticleDTO.getCompany_id();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("article_title", releaseArticleDTO.getArticle_title());
        paramMap.put("companyId", companyId);
        
		//文章总数
        int totalRecord = 0;
        //查询文章总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryReleaseArticleCount", paramMap);
        
        PageQueryVO pageQueryVO = releaseArticleDTO.getPageQueryVO();
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);

        //文章列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	List<Map<String, Object>> tempList = baseDAO.findListByRange(mapperNamespace + ".queryReleaseArticleList", paramMap, 
        			page.getStart(), page.getLimit());
        	if(tempList != null && tempList.size() > 0) {
        		// 查询该文章,公司成员分享的人数
        		Map<String, Object> teamShareData = getTeamShareData(companyId);
        		for(Map<String, Object> m : tempList) {
        			String uuid = (String) m.get("uuid");
        			
        			int read_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadersByArticleUUID", uuid);
        			String key = m.get("uuid") == null ? "" : (String) m.get("uuid");
        			String team_share_num = teamShareData.get(key) == null ? "0" : (String) teamShareData.get(key);
        			
        			m.put("read_num", read_num);
        			m.put("team_share_num", team_share_num);
        			list.add(m);
        		}
        		
        	}
        }
        resultMap.put("articleList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
    	resp.setMessage("查询公司文章列表成功");
        return resp;
	}
	
	@Override
	public Response queryCompanyStaffShareList(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
		List<String> unionids = new ArrayList<String>();
        Map<String, String> groupMap = new HashMap<String, String>();
        // 调用运营平台接口查询部门下的用户unionids
        List<Map<String, Object>> guList = outerService.getGroupUserList(vo.getGroup_id(), vo.getUser_name());
		if(guList != null && guList.size() > 0) {
			for(Map<String, Object> m : guList) {
				String unionid = (String) m.get("unionid");
				String group_name = (String) m.get("group_name");
				
				unionids.add(unionid);
				groupMap.put(unionid, group_name);
			}
		}
		
		//列表
        List<Map<String, Object>> staffList = new ArrayList<Map<String, Object>>();
        
        if(unionids.size() > 0) {
        	// 关联查询用户表
    		Map<String, Object> paramMap = new HashMap<String, Object>();
    		paramMap.put("unionidList", unionids);
    		List<Map<String, Object>> userList = baseDAO.findListBy(mapperNamespace + ".queryCompanyUsersByUnionid", paramMap);
            
    		if(userList != null && userList.size() > 0) {
        		for(Map<String, Object> map : userList) {
//        			String openId = (String) map.get("openId");
        			String unionid = (String) map.get("unionid");
        			String member_end_date = map.get("member_end_date") == null ? "" : (String) map.get("member_end_date");
        			
        			String remark = "";
        			if(StringUtils.isNotBlank(member_end_date)) {
        				remark = "会员到期日期：" + map.get("member_end_date");
        			} else {
        				remark = "暂无描述";
        			}
        			
        			int customer_num = callCrmService.getCustCount(unionid);
        			
        			map.put("remark", remark);
        			map.put("department", groupMap.get(unionid));
        			map.put("customer_num", customer_num);
        			
//        			map.remove("openId");
        			map.remove("unionid");
        			map.remove("member_end_date");
        			staffList.add(map);
        		}
    		}
        }
		
        resultMap.put("staffList", staffList);
//        resultMap.put("page", page);
        resp.setData(resultMap);
        resp.setMessage("查询成员管理列表成功");
        return resp;
	}
	
	@Override
	public Response queryCompanyAllUser(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();

//		Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("company_name", vo.getCompany_name());
//        paramMap.put("openId", vo.getOpenId());
//        
//		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryCompanyAllUser", paramMap);
//		if(list == null || (list.size() > 0 && list.get(0) == null)) {
//			list = new ArrayList<Map<String, Object>>();
//		}
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = outerService.getCompanyUserList(vo.getCompany_id(), vo.getGroup_id());
		if(list != null && list.size() > 0) {
			for(Map<String, Object> m : list) {
				String unionid = (String) m.get("unionid");
				
				QlyRhUserVO userVO = userService.getQlyRhUserByUnionid(unionid);
				if(userVO != null) {
					m.put("user_name", userVO.getUser_name());
					m.put("headImgUrl", userVO.getHeadImgUrl());
					m.put("openId", userVO.getOpenId());
					
					dataList.add(m);
				}
				
			}
			
			resultMap.put("staffList", dataList);
	        resp.setData(resultMap);
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("获取公司成员列表失败");
		}
		
        return resp;
	}
	
	@Override
	public Response teamTransfer(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
//		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		QlyRhUserVO receiverUserVO = dataCacheService.getQlyRhUserVO(vo.getReceiverOpenId());
		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("userId", userVO.getUser_id());
//        paramMap.put("receiverUserId", receiverUserVO.getUser_id());
//        
//        baseDAO.update(mapperNamespace + ".updateQlyRhUserCompany", paramMap);
//        
//        logger.info("*************[teamTransfer]清除缓存CS_cache_temp_user_key*************");
//		// 清除缓存数据
//		String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, vo.getOpenId());
//		dataCacheService.del(cacheKey);
//		
//		cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, vo.getReceiverOpenId());
//		dataCacheService.del(cacheKey);
		
		// 是否成功
		Map<String, String> resultMap = outerService.updateCompanyInfo(vo.getCompany_id(), null, null, null, receiverUserVO.getUnionid());
		if(!resultMap.isEmpty()) {
			if("0".equals(resultMap.get("code"))) {
				resp.setMessage("转让公司管理员成功");
			} else {
				resp.setCode("1");
				resp.setMessage("转让公司管理员失败");
			}
			
		} else {
			resp.setCode("1");
			resp.setMessage("转让公司管理员失败");
		}
		
		return resp;
	}
	
	private Map<String, Object> getTeamShareData(int companyId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<String> unionidList = queryUserUnionidByCompanyId(companyId);
		if(unionidList.size() > 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("unionidList", unionidList);
			paramMap.put("companyId", companyId);
			
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryTeamShareData", paramMap);
			if(list != null && list.size() > 0) {
				for(Map<String, Object> m : list) {
					String key = m.get("uuid") == null ? "" : (String) m.get("uuid");
					String team_share_num = m.get("team_share_num") == null ? "0" : m.get("team_share_num").toString();
					if(StringUtils.isNotBlank(key)) {
						resultMap.put(key, team_share_num);
					}
					
				}
			}
		}
		
		return resultMap;
	}
	
	private List<String> queryUserUnionidByCompanyId(int companyId) {
		List<String> result = new ArrayList<String>();
		
		List<Map<String, Object>> list = outerService.getCompanyUserList(companyId, 0);
		if(list != null && list.size() > 0) {
			for(Map<String, Object> m : list) {
				String unionid = (String) m.get("unionid");
				
				if(StringUtils.isNotBlank(unionid)) {
					result.add(unionid);
				}
				
			}
		}
		
		return result;
	}
	
	@Override
	public Response queryTeamArticleReadDetails(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();

//		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("company_name", userVO.getCompany());
        paramMap.put("articleId", vo.getArticleId());
        
        List<RespArticleReadCondDetailsVO> dataList = new ArrayList<RespArticleReadCondDetailsVO>();
		List<Map<String, Object>> details = baseDAO.findListBy(mapperNamespace + ".queryTeamArticleReadDetails", paramMap);
		List<String> filterList = new ArrayList<String>();
		if(details != null && details.size() > 0) {
			for(Map<String, Object> m : details) {
				String openId = (String)m.get("visitor_id");
				if(filterList.contains(openId)) {
					continue ;
				} else {
					filterList.add(openId);
				}
				RespArticleReadCondDetailsVO readCondDetailsVO = new RespArticleReadCondDetailsVO();
				
				// 获取访客资料，因为存在多次修改昵称的情况，导致数据出现重复的。现在改成去最后一次昵称显示
				Map<String, Object> rmap = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".queryRhRecordInfoByVisitorId", openId);
    			if(!rmap.isEmpty()) {
    				readCondDetailsVO.setUser_name((String) rmap.get("user_name"));
    				readCondDetailsVO.setHeadImgUrl((String) rmap.get("headImgUrl"));
    				readCondDetailsVO.setTelphone((String) rmap.get("telphone"));
    				readCondDetailsVO.setEwm_url((String) rmap.get("ewm_url"));
    			}
				
				int total_num = 0, total_sharenum = 0;
				long total_readtimes = 0;
//				total_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalNumRead", openId);
//				Integer iTotalReadtimes = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadTime", openId);
//				if(iTotalReadtimes != null) {
//					total_readtimes = iTotalReadtimes.longValue();
//				}
				Map<String, String> param1 = new HashMap<String, String>();
				param1.put("openId", openId);
				param1.put("article_uuid", vo.getArticleId());
				total_num = baseDAO.findOne(mapperNamespace + ".getCompanyReadNumByUUID", param1);
				Integer iTotalReadtimes = baseDAO.findOne(mapperNamespace + ".getCompanyReadTimeByUUID", param1);
				if(iTotalReadtimes != null) {
					total_readtimes = iTotalReadtimes.longValue();
				}
				
//				total_sharenum = articleService.getTotalShareNum(openId);
				total_sharenum = baseDAO.findOne(mapperNamespace + ".getCompanyShareNumByUUID", param1);
				
				readCondDetailsVO.setTotal_num(total_num);
				readCondDetailsVO.setTotal_readtimes(total_readtimes);
				readCondDetailsVO.setTotal_sharenum(total_sharenum);
//				readCondDetailsVO.setStrTotalReadtimes(DateUtil.secondToTime(total_readtimes));
				dataList.add(readCondDetailsVO);
			}
		}
		
		resultMap.put("detailList", dataList);
        resp.setData(resultMap);
        resp.setMessage("公司文章阅读详情列表成功");
        return resp;
	}
	
	@Override
	public Response queryTeamArticleShareDetails(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();

//		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
//		NumberFormat nf = new DecimalFormat("0000000000");
//		String deptId = nf.format(vo.getDeptId());
//		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("companyId", userVO.getCompany_id());
//        paramMap.put("company_name", userVO.getCompany());
//        paramMap.put("articleId", vo.getArticleId());
//        paramMap.put("deptId", deptId);
        
        // 查询已分享的列表
        List<Map<String, Object>> sharedList = new ArrayList<Map<String, Object>>();
        List<String> sharedOpenIds = new ArrayList<String>();
        /**
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryTeamArticleShareDetails", paramMap);
		if(list != null && list.size() > 0) {
    		for(Map<String, Object> map : list) {
    			String openId = (String) map.get("openId");
    			if(!sharedOpenIds.contains(openId)) {
    				sharedOpenIds.add(openId);
    			}
    			String latelyShareDate = (String) map.get("share_date");
    			// 分享日期格式转换，比如“3天前，一周前”
    			if(StringUtils.isNotBlank(latelyShareDate)) {
    				Date dt = DateUtil.parseDateByFormat(latelyShareDate, "yyyy-MM-dd HH:mm:ss");
        			latelyShareDate = DateHelper.getPastTime(dt);
    			} else {
    				latelyShareDate = "";
    			}
    			
    			map.put("total_share", 1);
    			map.put("lately_share_date", latelyShareDate);
    			sharedList.add(map);
    		}
		}
		**/
        List<String> unionids = new ArrayList<String>();
        Map<String, String> groupMap = new HashMap<String, String>();
        // 调用运营平台接口查询部门下的用户unionids
        List<Map<String, Object>> guList = outerService.getGroupUserList(vo.getGroup_id(), vo.getUser_name());
		if(guList != null && guList.size() > 0) {
			for(Map<String, Object> m : guList) {
				String unionid = (String) m.get("unionid");
				String group_name = (String) m.get("group_name");
				
				unionids.add(unionid);
				groupMap.put(unionid, group_name);
			}
		}
		
		// 关联查询用户表
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> userList = null;
		if(unionids.size() > 0) {
			paramMap.put("unionidList", unionids);
			userList = baseDAO.findListBy(mapperNamespace + ".queryCompanyUsersByUnionid", paramMap);
			
			// 查询已分享该公司文章的用户列表
			paramMap.put("article_uuid", vo.getArticleId());
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryCompanyShareDateByUUID", paramMap);
			if(list != null && list.size() > 0) {
	    		for(Map<String, Object> map : list) {
	    			String openId = (String) map.get("openId");
	    			String unionid = (String) map.get("unionid");
	    			if(!sharedOpenIds.contains(openId)) {
	    				sharedOpenIds.add(openId);
	    			}
	    			
	    			String latelyShareDate = (String) map.get("share_date");
	    			// 分享日期格式转换，比如“3天前，一周前”
	    			if(StringUtils.isNotBlank(latelyShareDate)) {
	    				Date dt = DateUtil.parseDateByFormat(latelyShareDate, "yyyy-MM-dd HH:mm:ss");
	        			latelyShareDate = DateHelper.getPastTime(dt);
	    			} else {
	    				latelyShareDate = "";
	    			}
	    			
	    			map.put("department", groupMap.get(unionid));
	    			map.put("total_share", 1);
	    			map.put("lately_share_date", latelyShareDate);
	    			
	    			map.remove("openId");
	    			map.remove("unionid");
	    			sharedList.add(map);
	    		}
			}
		}
		
		// 查询未分享该公司文章的的用户列表
		List<Map<String, Object>> notSharedList = new ArrayList<Map<String, Object>>();
		/**
		List<Map<String, Object>> allMemberList = baseDAO.findListBy(mapperNamespace + ".queryCompanyAllUser", paramMap);
		if(allMemberList != null && allMemberList.size() > 0) {
			for(Map<String, Object> map : allMemberList) {
				String dept_fullid = map.get("dept_fullid") == null ? "" : (String) map.get("dept_fullid");
				String openId = (String) map.get("openId");
				if(!sharedOpenIds.contains(openId) && dept_fullid.contains(deptId)) {
					map.put("total_share", 0);
	    			map.put("lately_share_date", "暂无");
	    			notSharedList.add(map);
    			}
				
			}
		}**/
		
		if(userList != null && userList.size() > 0) {
    		for(Map<String, Object> map : userList) {
    			String openId = (String) map.get("openId");
    			String unionid = (String) map.get("unionid");
    			
    			if(!sharedOpenIds.contains(openId)) {
    				map.put("department", groupMap.get(unionid));
					map.put("total_share", 0);
	    			map.put("lately_share_date", "暂无");
	    			
	    			map.remove("openId");
	    			map.remove("unionid");
	    			notSharedList.add(map);
    			}
    		}
		}
		
		resultMap.put("sharedList", sharedList);
		resultMap.put("notSharedList", notSharedList);
        resp.setData(resultMap);
    	resp.setMessage("公司文章分享详情列表成功");
        return resp;
	}
	
	@Override
	public Response queryTeamArticleReadCond(QlyRhTeamArticleDTO vo) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("companyId", userVO.getCompany_id());
        paramMap.put("openId", vo.getTeamMemberOpenId());
        
		try {
			QlyRhUserVO teamMemberUserVO = dataCacheService.getQlyRhUserVO(vo.getTeamMemberOpenId());
			int total_share = 0, total_read = 0, total_share_num = 0;
			// 是否会员
			String isMember = userService.getIsMember(vo.getTeamMemberOpenId());
			
			retMap.put("user_name", teamMemberUserVO.getUser_name());
			retMap.put("headImgUrl", teamMemberUserVO.getHeadImgUrl());
			retMap.put("telphone", teamMemberUserVO.getTelphone());
			retMap.put("ewm_url", teamMemberUserVO.getEwm_url());
			retMap.put("department", teamMemberUserVO.getDepartment());
			retMap.put("position", teamMemberUserVO.getPosition());
			retMap.put("isMember", isMember);
			
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<QlyRhShareVO> shareList = baseDAO.findListBy(mapperNamespace + ".queryShareTeamArticles", paramMap);
			if(shareList != null && shareList.size() > 0) {
				total_share = shareList.size();
				for(QlyRhShareVO shareVO : shareList) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("sharer", shareVO.getSharer());
					m.put("share_uuid", shareVO.getUuid());
					m.put("article_title", shareVO.getArticle_title());
					
					String share_uuid = shareVO.getUuid();
					int read_num = 0, share_num = 0;
					read_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadersByUUID", share_uuid);
//					share_num = baseDAO.findOne(mapperNamespace + ".getShareNum", share_uuid);
					total_read += read_num;
					total_share_num += share_num;
					
					m.put("read_num", read_num);
					m.put("share_num", share_num);
					dataList.add(m);
				}
			}
			retMap.put("total_share", total_share);
			retMap.put("total_read", total_read);
			retMap.put("total_share_num", total_share_num);
			retMap.put("shareArticleList", dataList);
			
			result.setData(retMap);
			result.setMessage("队员分享详情列表成功");
		} catch (Exception e) {
			logger.error("查询队员分享情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询队员分享情况失败");
		}
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> queryTeamMemberList(int companyId) {
		List<Map<String, Object>> result = null;
		result = baseDAO.findListBy(mapperNamespace + ".queryTeamMemberList", companyId);
		return result;
	}
	
	@Override
	public String getPushNewTeamArticle(String openId) {
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
//		QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", userVO.getCompany());
//        int companyId = companyVO.getCompany_id();
		int companyId = 0;
		Map<String, Object> companyMap = outerService.getCompanyInfo(userVO.getUnionid());
		if(!companyMap.isEmpty()) {
			companyId = (int) companyMap.get("company_id");
		}
        
        //公司新文章
        Map<String, Object> resultMap = baseDAO.findOne(mapperNamespace + ".getLatelyTeamArticle", companyId);
        
        String content = "公司新文章";
		if(resultMap != null && !resultMap.isEmpty()) {
			String article_uuid = (String) resultMap.get("article_uuid");
			String article_title = (String) resultMap.get("article_title");
			String articleId = UUID.randomUUID().toString();
			
			String localUrl = frontendUrl + "#/contents?rh_articleId=" + article_uuid + "&articleId=" + articleId + "&userId=" + openId + "&getInto=listGetInto";
			content += "<a href='"+ localUrl +"'>《" + article_title + "》</a>";
		}
		logger.info("********************推送公司新文章*********************：{}", content);
		
		return content;
	}
	
	@Override
	public Response queryCompanys(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		String company_name = vo.getCompany_name();
		
		List<Map<String, Object>> list = outerService.getCompanyList(company_name);
		if(list != null && list.size() > 0) {
			resp.setData(list);
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("获取公司列表失败");
		}
		
		return resp;
	}
	
	@Override
	public int getCompanyAllUserNum(int companyId) {
		int num = 0;
		List<Map<String, Object>> list = outerService.getCompanyUserList(companyId, 0);
		if(list != null && list.size() > 0) {
			num = list.size();
		}
		
		return num;
	}
	
	@Override
	public Response delCompanyUser(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
		boolean flag = outerService.deleteCompanyUser(vo.getCompany_id(), userVO.getUnionid());
		if(flag) {
			// 调用微销官接口同步删除公司下的成员
			callCrmService.setUserCust(userVO.getUnionid(), 0);
			resp.setMessage("删除公司成员成功");
		} else {
			resp.setCode(Constants.RESPONSE_CODE_FAIL);
			resp.setMessage("删除公司成员失败");
		}
		
		return resp;
	}
	
	@Override
	public Response getCompanyStaffInfo(QlyRhCompanyDTO vo) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> retMap = new HashMap<String, Object>();
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(vo.getOpenId());
		
		Map<String, Object> companyMap = outerService.getCompanyInfo(userVO.getUnionid());
		if(!companyMap.isEmpty()) {
			String admin_user_unionid = (String) companyMap.get("admin_user");
			QlyRhUserVO adminUserVO = userService.getQlyRhUserByUnionid(admin_user_unionid);
			
			retMap.put("company_name", companyMap.get("company_name"));
			retMap.put("abbreviation", companyMap.get("abbreviation"));
			retMap.put("group_name", companyMap.get("group_name"));
			retMap.put("group_id", companyMap.get("group_id"));
			retMap.put("admin_user_name", adminUserVO.getUser_name());
		}
		
		resp.setData(retMap);
		resp.setMessage("查询公司成员信息成功");
		
		return resp;
	}
}
