package com.cd.qlyhk.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.dto.QlyRhMessageRemindSetDTO;
import com.cd.qlyhk.dto.QlyRhOrgDeptDTO;
import com.cd.qlyhk.dto.QlyRhSysCategoryDTO;
import com.cd.qlyhk.reptile.WeixinArticePipeline;
import com.cd.qlyhk.reptile.WeixinArticeProcessor;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IOrderService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IStatisticsService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.JwtTokenUtil;
import com.cd.qlyhk.vo.Audience;
import com.cd.qlyhk.vo.QlyRhOrderVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqArticleImgDTO;

import io.jsonwebtoken.Claims;
import us.codecraft.webmagic.Spider;

/**
 * 公共控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/common")
public class PubCommonController {

	private static final Logger logger = LoggerFactory.getLogger(PubCommonController.class);
	
	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	@Value("${wx.appid}")
	public String appid;
	
	@Resource(name = IPubCommonService.BEAN_ID)
	private IPubCommonService  pubCommonService;
	
	@Resource(name = IOuterService.BEAN_ID)
	private IOuterService   outerService;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService userService;
	
	@Resource(name = IDataCacheService.BEAN_ID)
	private IDataCacheService dataCacheService;
	
	@Resource(name = IArticleService.BEAN_ID)
	private IArticleService  articleService;
	
	@Resource(name = IOrderService.BEAN_ID)
	private IOrderService orderService;
	
	@Resource(name = IStatisticsService.BEAN_ID)
	private IStatisticsService statisticsService;
	
	@Autowired
    private Audience audience;
	
	/**
	 * 获取文章类别列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getCategoryList.do")
    @ResponseBody
	public Response getCategoryList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required=false) String type
			, @RequestParam(required=false) String openId) {
		logger.info("***************************获取文章类别列表********************************");
		logger.info("{},{}", type, openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = pubCommonService.getCategoryList(type, openId);
        } catch (Exception e) {
            logger.error("获取文章类别列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("获取文章类别列表失败");
        }
		logger.info("获取文章类别列表，返回数据={}", JSON.toJSON(result));
        return result;
	}
	
	/**
	 * 获取已订阅类别
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSubscribeList.do")
    @ResponseBody
	public Response getSubscribeList(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取已订阅类别********************************");
		String userId = request.getParameter("userId");
		logger.info("获取已订阅类别请求参数:{}", userId);
		Response result = Response.getDefaulTrueInstance();
		try {
			String openId = userId;
			result = pubCommonService.getSubscribeList(openId);
			result.setMessage("获取已订阅类别数据成功");
		}catch (Exception e) {
			logger.error("获取已订阅类别异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取已订阅类别失败");
		}
		logger.info("获取已订阅类别数据，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 保存订阅文章
	 * @param request
	 * @param response
	 * @param categoryList
	 * @return
	 */
	@RequestMapping(value = "/saveSubscribeArticles.do")
    @ResponseBody
	public Response saveSubscribeArticles(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhSysCategoryDTO categoryDTO) {
		logger.info("***************************保存订阅文章********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			result = pubCommonService.saveSubscribeArticles(categoryDTO);
			result.setMessage("保存订阅文章成功");
		}catch (Exception e) {
			logger.error("保存订阅文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("保存订阅文章失败");
		}
		logger.info("保存订阅文章，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 获取消息提醒设置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getMessageRemindSet.do")
    @ResponseBody
	public Response getMessageRemindSet(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取消息提醒设置********************************");
		String userId = request.getParameter("userId");
		logger.info("获取消息提醒设置请求参数:{}", userId);
		Response result = Response.getDefaulTrueInstance();
		try {
			String openId = userId;
			result = pubCommonService.getMessageRemindSet(openId);
			result.setMessage("获取消息提醒设置成功");
		}catch (Exception e) {
			logger.error("获取消息提醒设置异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取消息提醒设置失败");
		}
		logger.info("获取消息提醒设置，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 保存消息提醒设置
	 * @param request
	 * @param response
	 * @param messageRemindSetVO
	 * @return
	 */
	@RequestMapping(value = "/saveMessageRemindSet.do")
    @ResponseBody
	public Response saveMessageRemindSet(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhMessageRemindSetDTO messageRemindSetDTO) {
		logger.info("***************************保存消息提醒设置********************************");
		logger.info("保存消息提醒设置请求参数："+ JSON.toJSONString(messageRemindSetDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = pubCommonService.saveMessageRemindSet(messageRemindSetDTO);
			result.setMessage("保存消息提醒设置成功");
		}catch (Exception e) {
			logger.error("保存消息提醒设置异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("保存消息提醒设置失败");
		}
		logger.info("保存消息提醒设置，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 人脉雷达
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/networkingRadar.do")
	@ResponseBody
	public Response networkingRadar(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************人脉雷达********************************");
		String openId = request.getParameter("userId");
		//String openId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
		logger.info("人脉雷达请求参数:{}", openId);
		Response resp = Response.getDefaulTrueInstance();
		try {
    		resp = pubCommonService.networkingRadar(openId);
    		resp.setMessage("成功");
        } catch (Exception e) {
            logger.error("人脉雷达信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode(Constants.RESPONSE_CODE_FAIL);
            resp.setMessage("人脉雷达信息查询失败");
        }
		logger.info("人脉雷达，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 获取行政区域列表（省、市）
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping("/queryAreaNonDistrict.do")
    @ResponseBody
	public Response queryAreaNonDistrict(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取行政区域列表（省、市）********************************");
	    Response result = Response.getDefaulTrueInstance();
	    try {
			result = pubCommonService.queryAreaNonDistrict();
			result.setMessage("获取行政区域列表（省、市）成功");
        } catch (Exception e) {
            logger.error("获取行政区域列表（省、市）失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取行政区域列表（省、市）失败");
        }
	    logger.debug("获取行政区域列表（省、市），返回数据={}", JSON.toJSON(result));
        return result;
	}
	
	/**
	 * 获取行政区域列表（区/县）
	 * @param request
	 * @param response
	 * @param areaCode
	 * @return
	 */
	@RequestMapping("/queryAreaDistrict.do")
    @ResponseBody
	public Response queryAreaDistrict(HttpServletRequest request, HttpServletResponse response, @RequestParam String areaCode) {
		logger.info("***************************获取行政区域列表（区/县）********************************");
		logger.info("获取行政区域列表（区/县）请求参数：areaCode={}", areaCode);
	    Response result = Response.getDefaulTrueInstance();
	    try {
			result = pubCommonService.queryAreaDistrict(areaCode);
			result.setMessage("获取行政区域列表（区/县）成功");
        } catch (Exception e) {
            logger.error("获取行政区域列表（区/县）失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取行政区域列表（区/县）失败");
        }
	    logger.debug("获取行政区域列表（区/县），返回数据={}", JSON.toJSON(result));
        return result;
	}
	
	@RequestMapping(value = "/uploadArticleImg.do")
    @ResponseBody
	public Response uploadArticleImg(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
		logger.info("***************************uploadArticleImg********************************");
		logger.info("上传图片请求参数:{}", id);
		Response resp = Response.getDefaulTrueInstance();
		try {
			String[] ids = id.split(",");
			ReqArticleImgDTO imgDTO = new ReqArticleImgDTO();
			imgDTO.setIds(ids);
			imgDTO.setFilePath(articleImgUpload);
			imgDTO.setFileUrlPath(articleImgUrl);
			resp = pubCommonService.uploadArticleImg(imgDTO);
        } catch (Exception e) {
            logger.error("上传文章图片失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("上传文章图片失败");
        }
		logger.info("上传文章图片成功，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
    /**
	 * 查询部门组织树
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryOrgDeptList.do")
    @ResponseBody
    public Response queryOrgDeptList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhOrgDeptDTO deptDTO) {
		logger.info("***************************查询部门组织树********************************");
		logger.info("查询部门组织树请求参数:{}", deptDTO.getCompany_id());
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = pubCommonService.queryOrgDeptList(deptDTO);
        } catch (Exception e) {
            logger.error("查询部门组织树失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询部门组织树失败");
        }
        logger.info("查询部门组织树，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询下级部门
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryDeptLowerLevel.do")
    @ResponseBody
    public Response queryDeptLowerLevel(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhOrgDeptDTO deptDTO) {
		logger.info("***************************查询下级部门********************************");
		logger.info("查询下级部门请求参数:{}", deptDTO.getCompany_name());
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = pubCommonService.queryDeptLowerLevel(deptDTO);
        } catch (Exception e) {
            logger.error("查询下级部门失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询下级部门失败");
        }
        logger.info("查询下级部门，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 新增部门
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/addDept.do")
    @ResponseBody
    public Response addDept(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhOrgDeptDTO deptDTO) {
		logger.info("***************************新增部门********************************");
		logger.info("新增部门请求参数:{}", deptDTO.getCompany_name());
        Response resp = Response.getDefaulTrueInstance();
        try {
//        	QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(deptDTO.getOpenId());
//        	deptDTO.setOrg_id(userVO.getCompany_id());
        	resp = pubCommonService.addDept(deptDTO);
        } catch (Exception e) {
            logger.error("新增部门失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("新增部门失败");
        }
        logger.info("新增部门，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改所属部门
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/groupChange.do")
    @ResponseBody
    public Response groupChange(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhOrgDeptDTO deptDTO) {
		logger.info("***************************修改所属部门********************************");
		logger.info("修改所属部门请求参数:{}", deptDTO.getCompany_name());
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = pubCommonService.groupChange(deptDTO);
        } catch (Exception e) {
            logger.error("修改所属部门失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改所属部门失败");
        }
        logger.info("修改所属部门，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
	
	@RequestMapping(value = "/testtest.do")
    @ResponseBody
	public Response testtest(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String packageId, @RequestParam String openId, @RequestParam float amount, 
			@RequestParam String effective_time_s, @RequestParam String effective_time_e) {
		logger.info("***************************测试测试测试********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			int user_id = userVO.getUser_id();
			String customer_name = userVO.getUser_name();
			String phone = userVO.getTelphone();
			if(StringUtils.isBlank(phone)) {
				phone = "13600000000";
			}
			//查询本地数据库是否已经存在cust_id
			Integer custId = userService.getCustId(user_id);
			Integer cust_id;
			if(custId != null) {
				cust_id = custId.intValue();
			} else {
				cust_id = outerService.addOrUpdateCust(user_id, customer_name, phone, null);
			}
//			result = outerService.payOrderPack(packageId, cust_id, amount, effective_time_s, effective_time_e);
		} catch (Exception e) {
			logger.error("测试异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("测试失败");
		}
		logger.info("测试，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	@RequestMapping(value = "/test.do")
    @ResponseBody
	public Response test(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.info("***************************测试测试测试********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			int user_id = userVO.getUser_id();
			String customer_name = userVO.getNick_name();
			String phone = userVO.getTelphone();
			if(StringUtils.isBlank(phone)) {
				phone = "13600000000";
			}
			//查询本地数据库是否已经存在cust_id
			Integer custId = userService.getCustId(user_id);
			Integer cust_id;
			if(custId != null) {
				cust_id = custId.intValue();
			} else {
				cust_id = outerService.addOrUpdateCust(user_id, customer_name, phone, null);
			}
			result.setData(cust_id);
		} catch (Exception e) {
			logger.error("测试异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("测试失败");
		}
		logger.info("测试，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 清除AccessToken
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/clearCacheAccessToken.do")
	@ResponseBody
	public Response clearCacheAccessToken(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************清除缓存AccessToken********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
			dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
        } catch (Exception e) {
            logger.error("清除缓存AccessToken失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("清除缓存AccessToken失败");
        }
		logger.info("清除缓存AccessToken，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	@RequestMapping(value = "/testReptile.do")
    @ResponseBody
	public Response testReptile(HttpServletRequest request, HttpServletResponse response, @RequestParam String url) {
		logger.info("***************************testReptile********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			System.out.println(url);
			String urls[] = {url};
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("openId", "");
			param.put("filePath", articleImgUpload);
			param.put("fileUrlPath", articleImgUrl);
			
    		Spider.create(new WeixinArticeProcessor()).addPipeline(new WeixinArticePipeline(param)).addUrl(urls).thread(1).run();
        } catch (Exception e) {
            logger.error("testReptile失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("testReptile失败");
        }
		logger.info("testReptile，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 这个接口只能用于内部测试
	 * @param request
	 * @param response
	 * @param openId
	 * @param ordersSn
	 * @return
	 */
	@RequestMapping(value = "/testOrderFinish.do")
    @ResponseBody
	public Response testOrderFinish(HttpServletRequest request, HttpServletResponse response
			, @RequestParam String openId, @RequestParam String ordersSn) {
		logger.info("testOrderFinish,{},{}", openId, ordersSn);
		Response resp = Response.getDefaulTrueInstance();
		try {
			// 处理订单状态
//			QlyRhOrderVO order = orderService.getQlyRhOrderVO(ordersSn);
//			if(order != null) {
//				orderService.orderFinish("true", openId, order);
//			}
        } catch (Exception e) {
            logger.error("testOrderFinish失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("testOrderFinish失败");
        }
		logger.info("testOrderFinish，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 同步用户的Unionid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syncUserUnionid.do")
    @ResponseBody
	public Response syncUserUnionid(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************syncUserUnionid********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			userService.syncUserUnionid(appid);
        } catch (Exception e) {
            logger.error("syncUserUnionid失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("syncUserUnionid失败");
        }
		logger.info("syncUserUnionid，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 同步用户的Unionid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syncMonthlyStatistics.do")
    @ResponseBody
	public Response syncMonthlyStatistics(HttpServletRequest request, HttpServletResponse response, @RequestParam String startPeriod
			, @RequestParam String endPeriod) {
		logger.info("***************************syncMonthlyStatistics********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			statisticsService.syncMonthlyStatistics(startPeriod, endPeriod);
        } catch (Exception e) {
            logger.error("syncMonthlyStatistics失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("syncMonthlyStatistics失败");
        }
		logger.info("syncMonthlyStatistics，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 获取JWT
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/getCRMParam.do")
    @ResponseBody
	public Response getCRMParam(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.info("***************************getCRMParam********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("unionId", userVO.getUnionid());
			String jwt = JwtTokenUtil.createJWT(jsonObj, audience);
			if(StringUtils.isNotBlank(jwt)) {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("jwt", jwt);
				resultMap.put("appid", appid);
				resp.setData(resultMap);
			}
			
        } catch (Exception e) {
            logger.error("getCRMParam失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("getCRMParam失败");
        }
		logger.info("getCRMParam，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 解析JWT
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/doParseJWT.do")
    @ResponseBody
	public Response doParseJWT(HttpServletRequest request, HttpServletResponse response, @RequestParam String token) {
		logger.info("***************************doParseJWT********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			if(audience == null) {
				BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
	            audience = (Audience) factory.getBean("audience");
			}
			
			// 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
			Claims claims = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
			logger.info("UnionId:{}", claims.getSubject());
        } catch (Exception e) {
            logger.error("doParseJWT失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("doParseJWT失败");
        }
		logger.info("doParseJWT，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 获取地理位置
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "/getLocation.do")
    @ResponseBody
	public Response getLocation(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId, 
			@RequestParam String latitude, @RequestParam String longitude) {
		logger.info("获取地理位置请求参数:{},{},{}", openId, latitude, longitude);
		Response resp = Response.getDefaulTrueInstance();
		try {
			resp = pubCommonService.getLocation(latitude, longitude);
        } catch (Exception e) {
            logger.error("获取地理位置失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("获取地理位置失败");
        }
		logger.info("获取地理位置，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
}
