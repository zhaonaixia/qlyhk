package com.cd.qlyhk.api.prov.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.service.impl.UserServiceImpl;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IOuterService.BEAN_ID)
public class OuterServiceImpl implements IOuterService {
	private static final Logger logger = LoggerFactory.getLogger(OuterServiceImpl.class);

	@Value("${yypt.api.url}")
	public String yyptApiUrl;
	
	@Autowired
	private IUserService userService;

	@Override
	public int addOrUpdateCust(int user_id, String customer_name, String phone, String unionid) throws Exception {
		// 客户名称唯一性校验
//		Long cust_id = checkCustomerNameUnique(customer_name);
		// 暂时不判断唯一性校验(原因是微信昵称本身也没有做这个校验,然后昵称里面含有特殊表情符号,无法准确判断)
		// 判断该用户是否已经在其他应用里面创建自然人客户了。
		int cust_id = this.checkRegistered(customer_name, phone, unionid);
		if (cust_id == 0) {
			logger.debug("*************新增客户信息*****************");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("customer_id", 0);
			jsonObject.put("customer_name", customer_name);
			jsonObject.put("customer_nature", 0);
//			jsonObject.put("social_credit_code", "914401989898989M1");
			jsonObject.put("area_code", 440000);
			jsonObject.put("app_channel", "QLY");
			jsonObject.put("customer_source", 3);
			jsonObject.put("login_name", customer_name);
			jsonObject.put("password", "111111");
			jsonObject.put("phone", phone);
			jsonObject.put("wechat_account", unionid);
			String jsonStr = JSONObject.toJSONString(jsonObject);

			logger.debug("jsonStr:{}", jsonStr);

			String httpUrl = yyptApiUrl + "customerManager/addOrUpdateCust.do";
			String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
			cust_id = getAddOrUpdateCust(result);
			
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("user_id", user_id);
//			paramMap.put("cust_id", cust_id);
//			userService.addCustId(paramMap);
		}
		
		if(cust_id != 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("user_id", user_id);
			paramMap.put("cust_id", cust_id);
			userService.addCustId(paramMap);
		}
		
		return cust_id;
	}
	
	@Override
	public int addOrUpdateCompany(String customer_name, String abbr_name, String company_code, String phone, String unionid) {
		int cust_id = 0;
		logger.debug("*************新增客户信息*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", 0);
		jsonObject.put("customer_name", customer_name);
		jsonObject.put("customer_nature", 1);
		jsonObject.put("social_credit_code", company_code);
		jsonObject.put("area_code", 440000);
		jsonObject.put("app_channel", "QLY");
		jsonObject.put("customer_source", 3);
		jsonObject.put("login_name", customer_name);
		jsonObject.put("password", "111111");
		jsonObject.put("phone", phone);
		jsonObject.put("wechat_account", unionid);
		jsonObject.put("abbreviation", abbr_name);
		String jsonStr = JSONObject.toJSONString(jsonObject);

		logger.debug("jsonStr:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/addOrUpdateCust.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		cust_id = getAddOrUpdateCust(result);
		return cust_id;
	}
	
	@Override
	public boolean checkCustomerCode(String company_code) {
		boolean flag = false;
		logger.debug("*************社会信用代码唯一性校验*****************");
		logger.debug("company_code:{}", company_code);
		
		String httpUrl = yyptApiUrl + "customerManager/checkSocialCreditCodeUnique.do?social_credit_code=" + company_code;
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doGet(httpUrl);
		logger.debug("result:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				flag = data.getBooleanValue("flag");
			}
		}
		
		return flag;
	}
	
	@Override
	public int checkRegistered(String user_name, String phone, String unionid) {
		int custId = 0;
		logger.debug("*************校验注册用户是否已经创建自然人*****************");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_name", user_name);
		jsonObject.put("phone", phone);
		jsonObject.put("wechat_account", unionid);
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonStr:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/memberRegistered.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("result:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				JSONObject person = data.getJSONObject("person");
				if(person != null) {
					custId = person.get("custId") == null ? 0 : person.getIntValue("custId");
				}
				
			}
		}
		
		return custId;
	}

	@Override
	public Response payOrderPack(String packageId, int cust_id, double amount, String effective_time_s, String effective_time_e) {
		logger.debug("*************新增订单*****************");
		Response resp = Response.getDefaulTrueInstance();
		JSONObject jsonObject = new JSONObject();
		Long pack_id = Long.parseLong(packageId);
		jsonObject.put("pack_id", pack_id);
		jsonObject.put("cust_id", cust_id);
		jsonObject.put("amount", amount);
		jsonObject.put("effective_time_s", effective_time_s);
		jsonObject.put("effective_time_e", effective_time_e);
		String jsonStr = JSONObject.toJSONString(jsonObject);

		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "agreementManager/payOrderPack.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("新增订单响应参数:{}", result);
		return resp;
	}

	@Override
	public List<Map<String, Object>> getPackList() {
		List<Map<String, Object>> packList = new ArrayList<Map<String, Object>>();

		String httpUrl = yyptApiUrl + "public/packList.do?pack_type=1&app_code=QLY";
		String result = HttpRequestUtil.doGet(httpUrl);

		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			JSONObject data = (JSONObject) jsonObj.get("data");
			JSONArray arr = data.getJSONArray("packList");

			if (arr != null && arr.size() > 0) {
				for (int i = 0; i < arr.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					JSONObject o = arr.getJSONObject(i);

					int pack_charging_way = o.getInteger("pack_charging_way");
					if (pack_charging_way == 1) { // 套餐按月
						double price = o.getDouble("price");
						double costPrice = price;

						// 1年
						costPrice = price * 12;
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "1年");
						map.put("packageCode", "TC002");
						map.put("price", 168.0);
						map.put("costPrice", costPrice);
						map.put("month", 12);
						map.put("default_opt", "false");
						packList.add(map);
						
						// 3个月
						costPrice = price * 3;
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "3个月");
						map.put("packageCode", "TC001");
						map.put("price", 88.0);
						map.put("costPrice", costPrice);
						map.put("month", 3);
						map.put("default_opt", "true");
						packList.add(map);

						// 试用一个月（测试使用）
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "1个月试用");
						map.put("packageCode", "TC004");
						map.put("price", 0.01);
						map.put("costPrice", costPrice);
						map.put("month", 1);
						map.put("default_opt", "false");
						packList.add(map);
						
						// 2年
						/*
						costPrice = price * 24;
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "2年");
						map.put("packageCode", "TC003");
						map.put("price", 168.0);
						map.put("costPrice", costPrice);
						map.put("month", 24);
						map.put("default_opt", "false");
						packList.add(map);*/
					}
					/*
					if (pack_charging_way == 2) { // 套餐按季
						double price = o.getDouble("price");
						// 1个季度
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "3个月");
						map.put("packageCode", "TC001");
						map.put("price", 88.0);
						map.put("costPrice", price);
						map.put("month", 3);
						map.put("default_opt", "true");
						packList.add(map);
					} else if (pack_charging_way == 3) { // 套餐按年
						double price = o.getDouble("price");
						map = new HashMap<String, Object>();
						// 1年
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "1年");
						map.put("packageCode", "TC002");
						map.put("price", 118.0);
						map.put("costPrice", price);
						map.put("month", 12);
						map.put("default_opt", "false");
						packList.add(map);

						// 2年
						price = price * 2;
						map = new HashMap<String, Object>();
						map.put("packageId", o.getInteger("pack_id"));
						map.put("name", "2年");
						map.put("packageCode", "TC003");
						map.put("price", 168.0);
						map.put("costPrice", price);
						map.put("month", 24);
						map.put("default_opt", "false");
						packList.add(map);
					}*/

				}
			}
		}

		return packList;
	}

	@Override
	public Long checkCustomerNameUnique(String customer_name) throws Exception {
		logger.debug("*************客户名称唯一性校验*****************");
		logger.debug("customer_name:{}", customer_name);
		String customerName = java.net.URLEncoder.encode(customer_name);
		
		String httpUrl = yyptApiUrl + "customerManager/checkCustomerNameUnique.do?customer_name=" + customerName;
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doGet(httpUrl);
		Long cust_id = getCustId(result);
		return cust_id;
	}

	public Long getCustId(String result) {
		logger.debug("*************客户名称唯一性校验响应参数*************" + result);
		JSONObject json = JSONObject.parseObject(result);
		JSONObject jsonData = json.getJSONObject("data");
		Long cust_id = jsonData.getLong("cust_id");
		return cust_id;
	}

	private int getAddOrUpdateCust(String result) {
		logger.debug("*************新增客户信息响应参数*************" + result);
		JSONObject json = JSONObject.parseObject(result);
		JSONObject jsonData = json.getJSONObject("data");
		int cust_id = jsonData.getIntValue("customer_id");
		logger.debug("cust_id:" + cust_id);
		return cust_id;
	}

	@Override
	public int queryValidOrder(int customer_id, String app_code, String period) {
		logger.debug("*************根据客户id及app查询客户是否有订购增值服务*************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", customer_id);
		jsonObject.put("app_code", app_code);
		jsonObject.put("period", period);
		String jsonStr = JSONObject.toJSONString(jsonObject);

		logger.debug("jsonStr:{}", jsonStr);

		String httpUrl = yyptApiUrl + "orderManager/queryValidOrderByDate.do?customer_id=" + customer_id + "&app_code="
				+ app_code + "&period=" + period;

		String resp = HttpRequestUtil.doPostJSON(httpUrl, "");
		int result = getResult(resp);
		return result;
	}

	private int getResult(String resp) {
		logger.debug("*************根据客户id及app查询客户是否有订购增值服务响应参数*************" + resp);
		JSONObject json = JSONObject.parseObject(resp);
		JSONObject jsonData = json.getJSONObject("data");
		int result = jsonData.getIntValue("result");
		logger.debug("result:" + result);
		return result;
	}

	@Override
	public void updateCustDetail(Integer custId, String user_name, String telphone) {
		logger.debug("*************修改客户信息*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", custId);
		jsonObject.put("customer_name", user_name);
		if(StringUtils.isBlank(telphone)) {
			telphone = "13600000000";
		}
		jsonObject.put("phone", telphone);
		String jsonStr = JSONObject.toJSONString(jsonObject);

		logger.debug("jsonStr:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/updateCustDetail.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("*************修改客户信息响应参数*************" + result);
		
	}
	
	@Override
	public List<Map<String, Object>> getCompanyList(String customerName) {
		logger.debug("*************获取公司列表*****************");
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		JSONObject jsonObject = new JSONObject();

		if(StringUtils.isNotBlank(customerName)) {
			jsonObject.put("customer_name", customerName);
		}
		
		jsonObject.put("customer_nature", 1);
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/custList.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("获取公司列表响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONArray arr = jsonObj.getJSONArray("data");
				if (arr != null && arr.size() > 0) {
					for (int i = 0; i < arr.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject o = arr.getJSONObject(i);
						
						map.put("company_id", o.getInteger("cust_id"));
						map.put("company_name", o.getString("cust_name"));
						map.put("abbreviation", o.getString("abbreviation"));
						map.put("company_code", o.getString("social_credit_code"));
						retList.add(map);
					}
				}
			}
			
		}
		
		return retList;
	}
	
	@Override
	public List<Map<String, Object>> getCompanyGroupList(int companyId) {
		logger.debug("*************获取公司部门列表*****************" + companyId);
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("id", companyId);
//		jsonObject.put("type", 2);
//		String jsonStr = JSONObject.toJSONString(jsonObject);
//		
//		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "groupManager/loadGroupTree.do?id=" + companyId + "&type=2";
		String result = HttpRequestUtil.doGet(httpUrl);
		logger.debug("获取公司部门列表响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONArray arr = jsonObj.getJSONArray("data");
				if (arr != null && arr.size() > 0) {
					for (int i = 0; i < arr.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject o = arr.getJSONObject(i);
						
						map.put("id", o.getInteger("group_id"));
						map.put("parent_id", o.getInteger("parent_group_id"));
						map.put("name", o.getString("name"));
						map.put("code", o.getString("code"));
						retList.add(map);
					}
				}
			}
			
		}
		
		return retList;
	}
	
	@Override
	public int addCompanyGroup(Map<String, Object> param) {
		logger.debug("*************新增公司部门*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("group_id", 0);
		jsonObject.put("parent_group_id", param.get("parent_id"));
		jsonObject.put("name", param.get("name"));
		jsonObject.put("sort", 1);
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "groupManager/addOrUpdateGroup.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("新增公司部门响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				return 1;
			}
			
		}
		
		return 0;
	}
	
	@Override
	public List<Map<String, Object>> getCompanyUserList(int companyId, int groupId) {
		logger.debug("*************获取公司成员列表*****************");
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cust_id", companyId);
		
		if(groupId != 0) {
			jsonObject.put("group_id", groupId);
		}
		
		jsonObject.put("is_page", "N");
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);
		
		String httpUrl = yyptApiUrl + "customerManager/memberList.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("获取公司成员列表响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				JSONArray arr = data.getJSONArray("dataList");
				if (arr != null && arr.size() > 0) {
					for (int i = 0; i < arr.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject o = arr.getJSONObject(i);
						
						map.put("group_name", o.getString("group_name"));
						map.put("admin", o.getString("admin"));
						map.put("unionid", o.getString("wechat_account"));
						retList.add(map);
					}
				}
			}
			
		}
		
		return retList;
	}
	
	@Override
	public int inviteMember(Map<String, Object> param) {
		logger.debug("*************邀请成员加入公司*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", param.get("company_id"));
		jsonObject.put("group_id", param.get("group_id"));
		jsonObject.put("customer_name", param.get("user_name"));
		jsonObject.put("phone", param.get("telphone"));
		jsonObject.put("wechat_account", param.get("unionid"));
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/inviteMember.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("邀请成员加入公司响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				return 1;
			}
			
		}
		
		return 0;
	}
	
	@Override
	public Map<String, Object> getCompanyInfo(String unionid) {
		logger.debug("*************获取关联的公司信息*****************" + unionid);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String httpUrl = yyptApiUrl + "customerManager/custDetailByApp.do?account=" + unionid;
		String result = HttpRequestUtil.doGet(httpUrl);
		logger.debug("获取关联的公司信息响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				String rt = data.get("result") == null ? "" : data.getString("result");
				int company_id = data.get("cust_id") == null ? 0 : data.getInteger("cust_id");
				String company_name = data.get("customer_name") == null ? "" : data.getString("customer_name");
				String abbreviation = data.get("abbreviation") == null ? "" : data.getString("abbreviation");
				String isadmin = data.get("admin") == null ? "false" : data.getString("admin");
				String group_name = data.get("group_name") == null ? "" : data.getString("group_name");
				String admin_user = data.get("wechat_account") == null ? "" : data.getString("wechat_account");
				
				if("true".equals(rt)) {
					resultMap.put("rt", rt);
					resultMap.put("company_id", company_id);
					resultMap.put("company_name", company_name);
					resultMap.put("abbreviation", abbreviation);
					resultMap.put("isadmin", isadmin);
					resultMap.put("group_name", group_name);
					resultMap.put("group_id", data.get("group_id"));
					resultMap.put("admin_user", admin_user);
				}
				
			}
			
		}
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> getGroupUserList(int groupId, String user_name) {
		logger.debug("*************获取部门成员列表*****************");
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("group_id", groupId);
		jsonObject.put("subGroup", "Y");
		
		if(StringUtils.isNotBlank(user_name)) {
			jsonObject.put("user_name", user_name);
		}
		
		jsonObject.put("is_page", "N");
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);
		
		String httpUrl = yyptApiUrl + "userManager/pagedResult.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("获取部门成员列表响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				JSONArray arr = data.getJSONArray("dataList");
				if (arr != null && arr.size() > 0) {
					for (int i = 0; i < arr.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject o = arr.getJSONObject(i);
						
						map.put("group_name", o.getString("group_name"));
						map.put("unionid", o.getString("wechat_account"));
						retList.add(map);
					}
				}
			}
			
		}
		
		return retList;
	}
	
	@Override
	public boolean deleteCompanyUser(int companyId, String unionid) {
		boolean flag = false;
		logger.debug("*************删除公司成员*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", companyId);
		jsonObject.put("operation", "2");
		jsonObject.put("wechat_account", unionid);
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);
		
		String httpUrl = yyptApiUrl + "customerManager/memberChange.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("删除公司成员响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean groupChange(int old_group_id, int group_id, String unionid) {
		boolean flag = false;
		logger.debug("*************修改所属部门信息*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("old_group_id", old_group_id);
		jsonObject.put("group_id", group_id);
		jsonObject.put("wechat_account", unionid);
		String jsonStr = JSONObject.toJSONString(jsonObject);
		
		logger.debug("jsonObject:{}", jsonStr);
		
		String httpUrl = yyptApiUrl + "groupManager/groupChange.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("修改所属部门信息响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	@Override
	public Map<String, String> updateCompanyInfo(int companyId, String customer_name, String abbr_name, String company_code, String unionid) {
		Map<String, String> resultMap = new HashMap<String, String>();
		logger.debug("*************修改公司信息*****************");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customer_id", companyId);
		
		if(StringUtils.isNotBlank(customer_name)) {
			jsonObject.put("customer_name", customer_name);
		}
		
		if(StringUtils.isNotBlank(abbr_name)) {
			jsonObject.put("abbreviation", abbr_name);
		}
		
		if(StringUtils.isNotBlank(company_code)) {
			jsonObject.put("social_credit_code", company_code);
		}
		
		if(StringUtils.isNotBlank(unionid)) {
			jsonObject.put("wechat_account", unionid);
		}
		
		String jsonStr = JSONObject.toJSONString(jsonObject);
		logger.debug("jsonObject:{}", jsonStr);
		
		String httpUrl = yyptApiUrl + "customerManager/updateCustInfo.do";
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("修改公司信息响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			String message = jsonObj.getString("message");
			
			resultMap.put("code", code);
			resultMap.put("message", message);
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> getCompanyDetailInfo(int companyId) {
		logger.debug("*************获取公司信息*****************" + companyId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("customer_id", companyId);
		
		String jsonStr = JSONObject.toJSONString(jsonObject);
		logger.debug("jsonObject:{}", jsonStr);

		String httpUrl = yyptApiUrl + "customerManager/custDetail.do?customer_id=" + companyId;
		logger.debug("httpUrl:{}", httpUrl);
		String result = HttpRequestUtil.doPostJSON(httpUrl, jsonStr);
		logger.debug("获取公司信息响应参数:{}", result);
		
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			String code = jsonObj.getString("code");
			if("0".equals(code)) {
				JSONObject data = jsonObj.getJSONObject("data");
				int company_id = data.get("customer_id") == null ? 0 : data.getInteger("customer_id");
				String company_name = data.get("customer_name") == null ? "" : data.getString("customer_name");
				String abbreviation = data.get("abbreviation") == null ? "" : data.getString("abbreviation");
				String social_credit_code = data.get("social_credit_code") == null ? "" : data.getString("social_credit_code");
				
				resultMap.put("company_id", company_id);
				resultMap.put("company_name", company_name);
				resultMap.put("abbreviation", abbreviation);
				resultMap.put("company_code", social_credit_code);
			}
			
		}
		
		return resultMap;
	}
}
