package com.cd.qlyhk.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.queue.service.ServicerQueue;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IShareService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhUserClickArticleVO;
import com.cd.qlyhk.vo.ReqShareArticleDTO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.qlyhk.vo.WxRecordDTO;

/**
 * 微信分享控制器
 * 
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/wxshare")
public class ShareManageController {

	private static final Logger logger = LoggerFactory.getLogger(ShareManageController.class);

	@Value("${message.notice.templateId}")
	private String noticeTemplateId;

	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Value("${wx.appid}")
	public String appid;

	@Resource(name = IPubCommonService.BEAN_ID)
	private IPubCommonService pubCommonService;

	@Resource(name = IUserService.BEAN_ID)
	private IUserService userService;

	@Resource(name = IShareService.BEAN_ID)
	private IShareService shareService;

	@Resource(name = ServicerQueue.BEAN_ID)
	private ServicerQueue queueService;

	@Resource(name = IMessageCenterService.BEAN_ID)
	private IMessageCenterService messageCenterService;

	/**
	 * 微信分享。
	 * 
	 * @param request
	 * @param response
	 * @param localUrl
	 * @return Response
	 */
	@RequestMapping("/getReqParam.do")
	@ResponseBody
	public Response getReqParam(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String localUrl) {
		logger.info("***************************微信分享********************************");
		logger.info("微信分享请求参数:{}", localUrl);
		Response result = Response.getDefaulTrueInstance();
		JSONObject json = null;
		try {
			String access_token = pubCommonService.getCacheAccessToken();

			if (StringUtils.isNotBlank(access_token)) {
				String jsapi_ticket = pubCommonService.getCacheJsapiTicket(access_token);

				if (StringUtils.isNotBlank(jsapi_ticket)) {
					Map<String, String> ret = WxUtil.GetReqParamSign(jsapi_ticket, localUrl);
					if (!ret.isEmpty()) {
						result.setData(ret);
					}
				}

			}

		} catch (Exception e) {
			logger.error("微信分享异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("微信分享失败");
		}
		logger.info("微信分享，返回数据={}", JSON.toJSON(result));
		return result;

	}

	/**
	 * 获取微信用户信息（访客）。
	 * 
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/getWxUserinfo.do")
	@ResponseBody
	public Response getWxUserinfo(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取访客信息********************************");
		String code = request.getParameter("code");
		logger.info("获取访客信息请求参数:{}", code);
		Response result = Response.getDefaulTrueInstance();

		try {
			if (StringUtils.isBlank(code) || "null".equals(code)) {
				logger.error("获取访客信息异常，参数传入不正确");
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("获取访客信息失败");
				return result;
			}
			result = userService.getWxUserinfo(code, appid);
		} catch (Exception e) {
			logger.error("获取访客信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客信息失败");
		}
		logger.info("获取访客信息，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 页面访问。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/pageAccess.do")
	@ResponseBody
	public Response pageAccess(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WxRecordDTO weiXinRecordDTO) {
		logger.info("***************************访问页面********************************");
		logger.info("访问页面请求参数:{}", JSON.toJSONString(weiXinRecordDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			weiXinRecordDTO.setAppId(appid);
			shareService.insertRecord(weiXinRecordDTO);
		} catch (Exception e) {
			logger.error("访问记录异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("访问记录失败");
		}
		logger.info("访问页面，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 阅读时长更新。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/pageExit.do")
	@ResponseBody
	public Response pageExit(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid) {
		logger.info("***************************阅读时长更新********************************");
		logger.info("阅读时长更新请求参数:{}", uuid);
		Response result = Response.getDefaulTrueInstance();
		try {
			shareService.updateRecordQuitDate(uuid);
		} catch (Exception e) {
			logger.error("阅读时长更新异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("阅读时长更新失败");
		}

		logger.info("阅读时长更新，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 分享文章请求。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/shareArticle.do")
	@ResponseBody
	public Response shareArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReqShareArticleDTO reqShareArticleDTO) {
		logger.info("***************************分享文章********************************");
		logger.info("分享文章请求参数:{}", JSON.toJSONString(reqShareArticleDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			String uuid = UUID.randomUUID().toString();
			reqShareArticleDTO.setUuid(uuid);

			shareService.insertRhShare(reqShareArticleDTO);
		} catch (Exception e) {
			logger.error("微信分享文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("微信分享文章失败");
		}
		logger.info("分享文章，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 获取临时二维码URL。
	 * 
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/getTempCodeUrl.do")
	@ResponseBody
	public Response getTempCodeUrl(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取临时二维码URL********************************");
		String article_id = request.getParameter("article_id");
		logger.info("获取临时二维码URL请求参数：{}", article_id);
		Response result = shareService.getTempCodeUrl(article_id);

		logger.info("获取临时二维码URL，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 获取微信用户OpenId。
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @return Response
	 */
	@RequestMapping("/getOpenId.do")
	@ResponseBody
	public Response getOpenId(HttpServletRequest request, HttpServletResponse response, @RequestParam String code) {
		logger.info("***************************获取微信用户OpenId********************************");
		logger.info("获取微信用户OpenId请求参数:{}", code);
		Response result = Response.getDefaulTrueInstance();

		try {

			if (StringUtils.isBlank(code) || "null".equals(code)) {
				logger.error("获取微信用户OpenId异常，参数传入不正确");
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("获取微信用户OpenId失败");
				return result;
			}

			String data = WxUtil.GetOpenid(code);
			logger.info("获取微信用户OpenId返回结果:{}", data);
			JSONObject json = JSONObject.parseObject(data);
			String openid = json.getString("openid");
			logger.info("*************获取微信用户openid*************" + openid);

			if (StringUtils.isNotBlank(openid)) {
				Map<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("openId", openid);
				result.setData(retMap);
			}

			/*
			 * Map<String, Object> retMap = new HashMap<String, Object>();
			 * retMap.put("openId", "oSVdLv05qzm-bo26hSxuFBZV-EoU"); result.setData(retMap);
			 */
		} catch (Exception e) {
			logger.error("获取微信用户OpenId异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取微信用户OpenId失败");
		}
		logger.info("获取微信用户OpenId，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 根据文章标题查询该文章是否已经分享过了。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getArticleShareByUuid.do")
	@ResponseBody
	public Response getArticleShareByUuid(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReqShareArticleDTO reqShareArticleDTO) {
		logger.info("***************************分享文章ID********************************");
		logger.info("分享文章ID请求参数:{},{}", reqShareArticleDTO.getArticle_uuid(), reqShareArticleDTO.getOpenId());
		Response result = Response.getDefaulTrueInstance();
		try {
			result = shareService.getArticleShareByUuid(reqShareArticleDTO.getArticle_uuid(),
					reqShareArticleDTO.getOpenId());
		} catch (Exception e) {
			logger.error("获取文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章失败");
		}

		logger.info("获取分享文章ID，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 发送消息（测试）。
	 * 
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/testSendMsg.do")
	@ResponseBody
	public Response testSendMsg(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************发送消息（测试）********************************");
		Response result = Response.getDefaulTrueInstance();

		try {
//			SendMessageVO msgVO = new SendMessageVO();
//			msgVO.setTouser("oSVdLv05qzm-bo26hSxuFBZV-EoU");
//			msgVO.setContent("测试发送消息，回复你好");
//			msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
//			msgVO.setCreateTime(new Date());
//
//			if (msgVO.getMsgType() == MessageConstant.MSG_TYPE_MBXX) {
//				msgVO.setTemplateId(noticeTemplateId);
//				msgVO.setUrl("http://www.finway.com.cn");
//				JSONObject json = new JSONObject();
//				json.put("value", "测试发送消息，回复你好");
//				json.put("color", "#173177");
//
//				JSONObject data = new JSONObject();
//				data.put("content", json);
//				msgVO.setData(data.toJSONString());
//			}
//
//			queueService.addToQueue(msgVO);

//			SendMessageVO msgVO1 = new SendMessageVO();
//			msgVO1.setTouser("oSVdLv05qzm-bo26hSxuFBZV");
//			msgVO1.setContent("测试发送消息，回复你好");
//			msgVO1.setMsgType(MessageConstant.MSG_TYPE_PTXX);
//			msgVO1.setCreateTime(new Date());
//
//			queueService.addToQueue(msgVO1);

			// 发送客户数据通知消息
			Date dt = new Date();
			SendMessageVO msgVO = new SendMessageVO();
			msgVO.setMsgType(MessageConstant.MSG_TYPE_MBXX);
			msgVO.setTemplateId("Jg3zKln1xV9yZPQa_2F9j9_XhIOWgLpADe_3O8X-FDQ");
			msgVO.setUrl("");

			int num = 1000000;
			int num2 = 100000;
			String content = "昨天共有" + num + "位老师通过谁看过我功能，获取了" + num2 + "位准意向客户";
			JSONObject resultJson = new JSONObject();
			resultJson.put("value", content);
			resultJson.put("color", "#173177");

			JSONObject dataTimeJson = new JSONObject();
			dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy-MM-dd HH:mm:ss"));
			dataTimeJson.put("color", "#459ae9");

			JSONObject passengerFlowJson = new JSONObject();
			String passengerFlowContent = "昨天共有" + num + "位老师通过【文章-谁看过我】功能，获取了" + num2 + "位准客户";
			passengerFlowJson.put("value", passengerFlowContent);
			passengerFlowJson.put("color", "#459ae9");

			JSONObject remarkJson = new JSONObject();
			remarkJson.put("value", "点击下方【谁看过我】菜单，追踪客户");
			remarkJson.put("color", "#173177");

			JSONObject data = new JSONObject();
			data.put("first", resultJson);
			data.put("keyword1", dataTimeJson);
			data.put("keyword2", passengerFlowJson);
			data.put("remark", remarkJson);
			msgVO.setData(data.toJSONString());
			msgVO.setTouser("oSVdLv05qzm-bo26hSxuFBZV-EoU");
			messageCenterService.sendMsg(msgVO);
		} catch (Exception e) {
			logger.error("发送消息（测试）异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("发送消息（测试）失败");
		}
		logger.info("发送消息（测试），返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 获取用户信息（测试）。
	 * 
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/testSnsUserinfo.do")
	@ResponseBody
	public Response testSnsUserinfo(HttpServletRequest request, HttpServletResponse response) {
		Response result = Response.getDefaulTrueInstance();
		logger.info("***************************testSnsUserinfo********************************");
		String code = request.getParameter("code");
		logger.info("testSnsUserinfo:{}", code);

		String APP_ID = "wx08522d97f4739523";
		String SECRET = "f0f5354983ae8a67d42081be79561389";
		String openidUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%appid%&secret=%secret%&code=%code%&grant_type=authorization_code";
		openidUrl = openidUrl.replace("%appid%", APP_ID);
		openidUrl = openidUrl.replace("%secret%", SECRET);
		openidUrl = openidUrl.replace("%code%", code);

		String data = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
//		System.out.println(data);
		logger.info("data:{}", data);

		if (StringUtils.isNotBlank(data)) {
			JSONObject json = JSONObject.parseObject(data);
			String openid = json.getString("openid");
			String accessToken = json.getString("access_token");

			String openidUrl2 = "https://api.weixin.qq.com/sns/userinfo?access_token=%access_token%&openid=%openid%";
			openidUrl2 = openidUrl2.replace("%access_token%", accessToken);
			openidUrl2 = openidUrl2.replace("%openid%", openid);

			String result2 = HttpRequestUtil.doGet(openidUrl2);// 返回结果字符串
//			System.out.println(result2);
			logger.info("result2:{}", result2);
			result.setData(result2);
		}

		logger.info("testSnsUserinfo，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 微信页面跳转重定向
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/redirectPage.do")
	public void redirectPage(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************微信页面跳转重定向********************************");
		String code = request.getParameter("code");
		logger.info("***************************code********************************" + code);

		String rh_articleId = request.getParameter("rh_articleId");
		String articleId = request.getParameter("articleId");
		String userId = request.getParameter("userId");
		logger.info("request:{},{},{}", rh_articleId, articleId, userId);
		if (userId.indexOf("?type=weixin") > -1) {
			userId = userId.substring(0, userId.indexOf("?type=weixin"));
		}
		String localUrl = frontendUrl + "#/contents?rh_articleId=" + rh_articleId + "&articleId=" + articleId
				+ "&userId=" + userId + "&code=" + code;
		try {
			logger.info("***************************重定向URL********************************" + localUrl);
			response.sendRedirect(localUrl);
		} catch (IOException e) {
			logger.error("微信页面跳转重定向异常，具体异常信息为:{}", e.getMessage(), e);
		}
	}

	/**
	 * 获取用户信息（测试）。
	 * 
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/getClickArticleEvent.do")
	@ResponseBody
	public Response getClickArticleEvent(HttpServletRequest request, HttpServletResponse response) {
		Response result = Response.getDefaulTrueInstance();
		logger.info("***************************getClickArticleEvent********************************");

//		String s = request.getRemoteAddr();
//		System.out.println(s);
//
//		int s1 = request.getRemotePort();
//		System.out.println(s1);
//
//		String s2 = request.getRemoteUser();
//		System.out.println(s2);

		String s3 = request.getRequestedSessionId();
		System.out.println("session:" + s3);
		
		String ip = null;
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null) {
			// 对于通过多个代理的情况，最后IP为客户端真实IP,多个IP按照','分割
			int position = ip.indexOf(",");
			if (position > 0) {
				ip = ip.substring(0, position);
			}
		}
		System.out.println("ip：" + ip);
		
		logger.info("getClickArticleEvent，返回数据={}", JSON.toJSON(result));
		return result;
	}
	
	/**
	 * 用户点击文章详情。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/recordClickArticle.do")
	@ResponseBody
	public Response recordClickArticle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QlyRhUserClickArticleVO clickArticleVO) {
		logger.info("***************************用户点击文章详情********************************");
		logger.info("用户点击文章详情请求参数:{}", JSON.toJSONString(clickArticleVO));
		Response result = Response.getDefaulTrueInstance();
		try {
			String ip = null;
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if (ip != null) {
				// 对于通过多个代理的情况，最后IP为客户端真实IP,多个IP按照','分割
				int position = ip.indexOf(",");
				if (position > 0) {
					ip = ip.substring(0, position);
				}
			}
			clickArticleVO.setIp_address(ip);
			shareService.insertRecordClickArticle(clickArticleVO);
		} catch (Exception e) {
			logger.error("用户点击文章详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("用户点击文章详情记录失败");
		}
		logger.info("用户点击文章详情，返回数据={}", JSON.toJSON(result));
		return result;
	}
}
