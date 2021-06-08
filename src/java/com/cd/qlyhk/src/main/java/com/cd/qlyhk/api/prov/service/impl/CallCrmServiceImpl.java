package com.cd.qlyhk.api.prov.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.ICallCrmService;
import com.cd.qlyhk.utils.HttpRequestUtil;

@Service(ICallCrmService.BEAN_ID)
public class CallCrmServiceImpl implements ICallCrmService {
	
	private static final Logger logger = LoggerFactory.getLogger(CallCrmServiceImpl.class);
	
	@Value("${wxg.api.url}")
	public String wxgApiUrl;
	
	@Value("${crm.api.url}")
	public String crmApiUrl;
	
	@Override
	public boolean syncUserInfoToWXG(String appId, String openId, String userJson) {
		boolean reslut = false;
//		userJson = URLEncoder.encode(userJson);
		logger.debug("*************同步用户信息*****************");
		Map<String, String> param = new HashMap<String, String>();
		param.put("appId", appId);
		param.put("openId", openId);
		param.put("userJson", URLEncoder.encode(userJson));
		
		logger.debug("request:{}", param);
		
		String httpUrl = wxgApiUrl + "syncUser";
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doPostForm(httpUrl, param);
		logger.debug("*************同步用户信息响应参数*************" + result);
		
		
		return reslut;
	}
	
	@Override
	public void addClue(String appId, String unionId, String fromUnionId, String shareId) {
		logger.debug("*************新增线索信息*****************" + unionId);
		Map<String, String> param = new HashMap<String, String>();
		param.put("appId", appId);
		param.put("unionid", unionId);
		param.put("fromUnionid", fromUnionId);
		param.put("clueType", "8");
		param.put("serviceId", "0");
		param.put("articleId", shareId);
		
		logger.debug("request:{}", param);
		
		String httpUrl = crmApiUrl + "addClueByUnionid";
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doPostForm(httpUrl, param);
		logger.debug("*************新增线索信息响应参数*************" + result);
	}
	
	@Override
	public int getCustCount(String unionId) {
		int count = 0;
		logger.debug("*************获取个人客户数*****************" + unionId);
		String httpUrl = crmApiUrl + "getCustCountByUnionid?unionid=" + unionId;
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doGet(httpUrl);
		logger.debug("获取个人客户数响应参数:{}", result);
		
		if(StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String state = jsonObj.getString("state");
			if("ok".equals(state)) {
				count = jsonObj.getIntValue("data");
			}
		}
		
		return count;
	}
	
	@Override
	public void setUserCust(String unionId, int custId) {
		logger.debug("*************设置微销官用户对应运营平台企业ID*****************" + unionId);
		Map<String, String> param = new HashMap<String, String>();
		param.put("unionid", unionId);
		param.put("custId", String.valueOf(custId));
		
		logger.debug("request:{}", param);
		
		String httpUrl = wxgApiUrl + "setUserCust";
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doPostForm(httpUrl, param);
		logger.debug("*************设置微销官用户对应运营平台企业ID响应参数*************" + result);
	}
	
	@Override
	public int getCompanyCustCount(String unionId) {
		int count = 0;
		logger.debug("*************获取公司客户数*****************" + unionId);
		String httpUrl = crmApiUrl + "getAdminCustCount?unionid=" + unionId;
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doGet(httpUrl);
		logger.debug("获取公司客户数响应参数:{}", result);
		
		if(StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String state = jsonObj.getString("state");
			if("ok".equals(state)) {
				count = jsonObj.getIntValue("data");
			}
		}
		
		return count;
	}
}
