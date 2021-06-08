package com.cd.qlyhk.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.constants.WXConstants;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.dto.ReqEnterprisePaymentDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IOrderService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.qlyhk.vo.QlyRhOrderVO;
import com.cd.qlyhk.vo.QlyRhPackageVO;
import com.cd.qlyhk.vo.QlyRhUserAccountVO;
import com.cd.qlyhk.vo.QlyRhUserOrderPackageVO;
import com.cd.qlyhk.vo.QlyRhUserStreamVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.QlyRhWithdrawLogVO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.qlyhk.wechatpay.utils.ResultEntity;
import com.cd.qlyhk.wechatpay.utils.TransfersDTO;
import com.cd.qlyhk.wechatpay.utils.WechatpayUtil;
import com.cd.rdf.base.dao.IBaseDAO;
import com.github.wxpay.sdk.WXPayUtil;

/**
 * 订单服务接口实现类
 * @author sailor
 *
 */
@Service(IOrderService.BEAN_ID)
public class OrderServiceImpl implements IOrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private final String mapperNamespace = OrderServiceImpl.class.getName();

	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOuterService outerService;
	
	@Value("${boss.package.id}")
	private int package_id;
	
	@Value("${message.memberOpen.templateId}")
	private String memberOpenTemplateId;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private IMarketService marketService;
	
	@Autowired
	private IMessageCenterService messageCenterService;

	@Override
	public Response queryQlyRhPackage(String propValue) {
		boolean local_debug_no_boss = false;
		if (StringUtils.isNotBlank(propValue)) {
			local_debug_no_boss = Boolean.parseBoolean(propValue);
		}
		
		Response result = Response.getDefaulTrueInstance();
		
		if(local_debug_no_boss == false) {
			// 调用运营平台接口,获取套餐列表
			logger.info("*********************调用运营平台接口，获取套餐列表**********************");
			List<Map<String, Object>> packList = outerService.getPackList();
			result.setData(packList);
		} else {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<QlyRhPackageVO> dataList = baseDAO.findListBy(mapperNamespace+".queryQlyRhPackage");
			
			if(dataList != null && dataList.size() > 0) {
				for(QlyRhPackageVO packageVO : dataList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("packageId", packageVO.getUuid());
					map.put("packageCode", packageVO.getCode());
					map.put("name", packageVO.getName());
					map.put("price", packageVO.getPrice());
					map.put("default_opt", packageVO.getDefault_opt());
					
					list.add(map);
				}
			}
			result.setData(list);
		}
		
		return result;
	}

	@Override
	public Response createOrder(String propValue, String packageId, String packageCode, String openId) {
		Response result = Response.getDefaulTrueInstance();
		QlyRhPackageVO packageVO = null;
		
		boolean local_debug_no_boss = false;
		if (StringUtils.isNotBlank(propValue)) {
			local_debug_no_boss = Boolean.parseBoolean(propValue);
		}
		
		if(local_debug_no_boss == false) {
			packageVO = new QlyRhPackageVO();
			packageVO.setPackage_id(package_id);
			// 调用运营平台接口,获取套餐列表
			List<Map<String, Object>> packList = outerService.getPackList();
			if(packList != null && packList.size() > 0) {
				for(Map<String, Object> packMap : packList) {
					String packCode = (String) packMap.get("packageCode");
					int month = (int) packMap.get("month");
					double price = (double) packMap.get("price");
					
					if(packCode.equals(packageCode)) {
						packageVO.setMonth(month);
						packageVO.setPrice(price);
					}
				}
			}
			
		} else {
			packageVO = baseDAO.findOne(mapperNamespace+".getQlyRhPackage", packageId);
		}
		// 检查套餐是否存在,不存在则返回失败
		if(packageVO == null) {
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("套餐不存在或者已删除");
			return result;
		}
		
		// 检查用户信息是否存在
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		if(userVO == null) {
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("用户不存在或者已删除");
			return result;
		}
		
		// 检查用户是否已经订购过试用套餐,只能订购一次
		/**2020-03-26注释，暂时不控制
		if(packageVO.getPrice() == 0.01) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userId", userVO.getUser_id());
			paramMap.put("price", packageVO.getPrice());
			int count = baseDAO.findOne(mapperNamespace+".getQlyRhOrderOntriaCount", paramMap);
			if(count > 0) {
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("您已订购过试用套餐，该套餐一个用户只能订购一次");
				return result;
			}
		}**/
		
		int userId = userVO.getUser_id();
		Date dt = new Date();
		long time = System.currentTimeMillis();
		String lsh = DateUtil.formatDateByFormat(dt, "yyyyMMdd") + String.valueOf(time);
		logger.info("***********订单流水号*********" + lsh);
		String startDate = "";
		// 先查询是否有有效订单
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("dateTime", DateUtil.formatDate(dt));
		String validOrderDate = baseDAO.findOne(mapperNamespace+".getMaxValidOrderDate", paramMap);
		if(validOrderDate != null) {
			startDate = validOrderDate;
			dt = DateUtil.parseDate(startDate);
		} else {
			startDate = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		}
		
		int month = packageVO.getMonth();
		Date tempDt = DateUtil.afterNMonth(dt, month);
		String endDate = DateUtil.formatDateByFormat(tempDt, "yyyy-MM-dd");
		
		QlyRhOrderVO orderVO = new QlyRhOrderVO();
		orderVO.setLsh(lsh);
		orderVO.setMoney(packageVO.getPrice());
		orderVO.setPay_status("0");// 未支付
		orderVO.setStart_date(startDate);
		orderVO.setEnd_date(endDate);
		orderVO.setStatus("01");
		orderVO.setCreate_user_id(userId);
		orderVO.setRemark(packageId);
		
		int orderType = getOrderType(orderVO.getMoney());
		orderVO.setOrder_type(orderType);
		
		baseDAO.add(mapperNamespace+".insertQlyRhOrder", orderVO);
		logger.info("***********新建订单成功*********" + orderVO.getOrder_id());
		
		QlyRhUserOrderPackageVO userOrderPackageVO = new QlyRhUserOrderPackageVO();
		userOrderPackageVO.setOrder_id(orderVO.getOrder_id());
		userOrderPackageVO.setPackage_id(packageVO.getPackage_id());
		userOrderPackageVO.setUser_id(userId);
		baseDAO.add(mapperNamespace+".insertQlyRhUserOrderPackage", userOrderPackageVO);
		logger.info("***********订单完成*********");
		
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("orderId", lsh);
		result.setData(retMap);
		
		return result;
	}
	
	@Override
	public QlyRhOrderVO getQlyRhOrderVO(String orderId) {
		return baseDAO.findOne(mapperNamespace+".getQlyRhOrder", orderId);
	}
	
	@Override
	public int updateOrderPayStatus(String payStatus, String orderId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("payStatus", payStatus);
		param.put("orderId", orderId);
		return baseDAO.update(mapperNamespace+".updateOrderPayStatus", param);
	}
	
	@Override
	public void updateOrderStatus(String status, String orderId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", status);
		param.put("orderId", orderId);
		baseDAO.update(mapperNamespace+".updateOrderStatus", param);
	}
	
	@Override
	public void orderFinish(String propValue, String openId, QlyRhOrderVO order) {
		boolean local_debug_no_boss = false;
		if (StringUtils.isNotBlank(propValue)) {
			local_debug_no_boss = Boolean.parseBoolean(propValue);
		}
	    
		if("1".equals(order.getPay_status())) {
			logger.info("**************订单已支付成功*************", order.getLsh());
			return ;
		}
		
		String ordersSn = order.getLsh();
		int updateRet = this.updateOrderPayStatus("1", ordersSn);
		if(updateRet > 0) {
			logger.info("**************订单状态更新成功*************", ordersSn);
			String isMember = userService.getIsMember(openId);
			
			QlyRhUserDTO userDTO = new QlyRhUserDTO();
			userDTO.setOpenId(openId);
			userDTO.setIsMember("1");
			userDTO.setOpen_date(DateUtil.formatDate(new Date()));
			userDTO.setMember_end_date(order.getEnd_date());
			userService.updateUserInfo(userDTO);
			
			logger.info("*************[orderFinish]清除缓存CS_cache_temp_user_key*************");
			// 清除缓存数据
			String cacheKey = String.format(DataCacheConst.CS_cache_temp_user_key, openId);
			dataCacheService.del(cacheKey);
			
			QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
    		int user_id = userVO.getUser_id();
			// 推送会员开通或者续期通知
			sendMemberOpenMsg(isMember, openId, user_id, order.getEnd_date());
			
			// 更新我的账户(其中账户等级，只有新开通会员的时候去重新获取，续期时如果新订单的金额大于原来的订单金额，则需要重新获取)
			int memberGrade = getMemberGrade(isMember, user_id, order.getMoney(), openId);
			QlyRhUserAccountVO accountVO = new QlyRhUserAccountVO();
			accountVO.setUser_id(user_id);
			accountVO.setMember_grade(memberGrade);
			if(memberGrade >= 2) {
				// 重新充值为黄金会员及以上，清除之前的错失收益
				accountVO.setMiss_income(0.0);
				
				// 推送资料（非白银会员就推送）
				sendMmeansMsg(openId);
			}
			
			marketService.updateQlyRhUserAccount(accountVO);
			
			// 检查是否有推荐人,有则更新用户收益。订单金额大于等于1才能参与分成
			if(StringUtils.isNotBlank(userVO.getRecommender()) && order.getMoney() >= 1) {
				calculationUserIncome(openId, userVO.getRecommender(), order.getMoney());
			}
			
			// 调用运营平台接口,通知订单已完成支付
			if(local_debug_no_boss == false) {
				String packageId = order.getRemark();
				double amount = order.getMoney();
				String effective_time_s = order.getStart_date();
				String effective_time_e = order.getEnd_date();
	    		String customer_name = userVO.getUser_name();
	    		String phone = userVO.getTelphone();
	    		String unionid = userVO.getUnionid();
	    		if(StringUtils.isBlank(phone)) {
	    			phone = "13600000000";
	    		}
	    		
				try {
					// 查询本地数据库是否已经存在cust_id
					Integer custId = userService.getCustId(user_id);
					Integer cust_id;
					if(custId != null) {
						cust_id = custId.intValue();
					} else {
						cust_id = outerService.addOrUpdateCust(user_id, customer_name, phone, unionid);
					}
					outerService.payOrderPack(packageId, cust_id, amount, effective_time_s, effective_time_e);
				} catch (Exception e) {
					logger.error("调用运营平台接口【订单支付】失败：" + e);
					this.updateOrderStatus("03", ordersSn);
				}
	    		
			}
			
			
		}
	}
	
	@Override
	public QlyRhOrderVO getQlyRhOrderLately(int userId) {
		return baseDAO.findOne(mapperNamespace+".getQlyRhOrderLately", userId);
	}
	
	@Override
	public double getOrderTotalAmount(int userId) {
		double m = 0;
		Double amount = baseDAO.findOne(mapperNamespace+".getOrderTotalAmount", userId);
		if(amount != null) {
			m = amount.doubleValue();
		}
		
		return m;
	}
	
	@Override
	public Response enterprisePayment(String appId, String mchId, String mchSecret, String certPath, String ip, ReqEnterprisePaymentDTO paymentDTO) {
		Response resp = Response.getDefaulTrueInstance();
//		Map<String, Object> resultMap = new HashMap<String, Object>();
		synchronized (OrderServiceImpl.class) {
			try {
				String openId = paymentDTO.getOpenId();
				String real_name = paymentDTO.getRealName();
				QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
	    		int userId = userVO.getUser_id();
				
				double pay_amount = paymentDTO.getMoney();
				double balance = 0.0;
				QlyRhUserAccountVO accountVO = marketService.getQlyRhUserAccountVO(userId);
				if(accountVO != null) {
					double income_balance = accountVO.getIncome_balance() == null ? 0.0 : accountVO.getIncome_balance();
					if(income_balance < pay_amount) {
						resp.setSuccess(false);
						resp.setCode("1");
						resp.setMessage("余额不足");
						return resp;
					}
					balance = income_balance - pay_amount;
					
					// 先把提现的金额减掉,并更新我的账户
					QlyRhUserAccountVO updateAccountVO = new QlyRhUserAccountVO();
					updateAccountVO.setUser_id(userId);
					updateAccountVO.setIncome_balance(balance);
					marketService.updateQlyRhUserAccount(updateAccountVO);
					logger.info("*************更新我的账户**************" + userId);
				}
				
				Date dt = new Date();
				long time = System.currentTimeMillis();
				String lsh = DateUtil.formatDateByFormat(dt, "yyyyMMdd") + String.valueOf(time);
				logger.info("*****************商户订单号****************" + lsh);
				
				
//				double amount = pay_amount;
//				amount = amount * 100;
//				NumberFormat nf0 = new DecimalFormat("0");
//				String money = nf0.format(amount);
//				logger.info("*****************将金额转成分****************" + money);
				
				// 接口请求参数
				TransfersDTO transfersDTO = new TransfersDTO();
				transfersDTO.setMch_appid(appId);
				transfersDTO.setMchid(mchId);
				transfersDTO.setOpenid(openId);
				transfersDTO.setPartner_trade_no(lsh);// 商户订单号
				transfersDTO.setCheck_name("FORCE_CHECK");// NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
				transfersDTO.setRe_user_name(real_name); // 收款用户真实姓名。
				transfersDTO.setAmount(pay_amount);// 企业付款金额，这里单位为元
				transfersDTO.setDesc("节日快乐");
				transfersDTO.setSpbill_create_ip(ip);
				
				ResultEntity iResult = WechatpayUtil.doTransfers(mchSecret, certPath, transfersDTO);
				String xmlStr = iResult.getData();
				
				String payment_no = "";
				if(iResult.isSuccess()) {
					Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
					payment_no = (String) map.get("payment_no");
				}
				
				// 新增一条提现流水
				if(StringUtils.isNotBlank(payment_no)) {
					QlyRhUserStreamVO streamVO = new QlyRhUserStreamVO();
					streamVO.setUser_id(userId);
					streamVO.setStream_date(DateUtil.formatDate(dt));
					streamVO.setStream_type(Constants.STREAM_TYPE_INCOME_L3);
					streamVO.setAmount(pay_amount);
					streamVO.setBalance(balance);
					streamVO.setRemark("提现");
					marketService.insertQlyRhUserStream(streamVO);
					logger.info("*************新增一条提现流水**************" + userId);
				} else {
					// 如果支付失败了，则回退之前提现的金额到余额里面
					balance = balance + pay_amount;
					QlyRhUserAccountVO updateAccountVO = new QlyRhUserAccountVO();
					updateAccountVO.setUser_id(userId);
					updateAccountVO.setIncome_balance(balance);
					marketService.updateQlyRhUserAccount(updateAccountVO);
					logger.info("*************更新我的账户**************" + userId);
				}
				
				QlyRhWithdrawLogVO logVO = new QlyRhWithdrawLogVO();
				logVO.setOpenId(openId);
				logVO.setPartner_trade_no(lsh);
				logVO.setReal_name(real_name);
				logVO.setAmount(pay_amount);
				logVO.setIp_address(ip);
				logVO.setReturn_data(xmlStr);
				
				if(StringUtils.isNotBlank(payment_no)) {
					logVO.setStatus("1");
					resp.setMessage("提交成功");
				} else {
					logVO.setStatus("0");
					resp.setCode("1");
					resp.setMessage("支付失败");
				}
				
				// 新增一条提现日志
				baseDAO.add(mapperNamespace+".insertQlyRhWithdrawLog", logVO);

			} catch (Exception e) {
				logger.error("*****************支付失败****************" + e);
				resp.setSuccess(false);
				resp.setCode("1");
				resp.setMessage("支付失败");
				return resp;
			}
		}
		
		return resp;
	}
	

	@Override
	public Response queryCashWithdrawal(String appId, String mchId, String mchSecret, String certPath, String partner_trade_no) {
		Response resp = Response.getDefaulTrueInstance();
		
		try {
			// 接口请求参数
			TransfersDTO transfersDTO = new TransfersDTO();
			transfersDTO.setMch_appid(appId);
			transfersDTO.setMchid(mchId);
			transfersDTO.setPartner_trade_no(partner_trade_no);// 商户订单号
			
			ResultEntity iResult = WechatpayUtil.doQueryTransfers(mchSecret, certPath, transfersDTO);
			String xmlStr = iResult.getData();
			
			String status = "";
			if(iResult.isSuccess()) {
				Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
				status = (String) map.get("status");
				
				if("SUCCESS".equals(status)) {
					resp.setCode("0");
					resp.setMessage("支付成功");
				} else {
					resp.setCode("1");
					resp.setMessage("支付失败");
				}
				
			} else {
				resp.setCode("1");
				resp.setMessage("支付失败");
			}
			
		} catch (Exception e) {
			logger.error("*****************支付失败****************" + e);
			resp.setSuccess(false);
			resp.setCode("1");
			resp.setMessage("支付失败");
			return resp;
		}
		
		return resp;
	}
	
	/**
	 * 推送开通或者续费通知
	 * @param isMember
	 * @param openId
	 * @param userId
	 * @param endDate
	 */
	private void sendMemberOpenMsg(String isMember, String openId, int userId, String endDate) {
		Date dt = new Date();
		Date endDt = DateUtil.parseDate(endDate);
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setMsgType(MessageConstant.MSG_TYPE_MBXX);
		msgVO.setTemplateId(memberOpenTemplateId);
		msgVO.setUrl("");
		
		JSONObject data = new JSONObject();
		JSONObject contentJson = new JSONObject();
		JSONObject memberNumJson = new JSONObject();
		JSONObject dataTimeJson = new JSONObject();
		JSONObject remarkJson = new JSONObject();
		if("1".equals(isMember)) {
			String content = "恭喜您，成功续费千里眼会员~";
			contentJson.put("value", content);
			contentJson.put("color", "#173177");

			String remarkContent = "您的会员服务已延长至" + DateUtil.formatDateByFormat(endDt, "yyyy年MM月dd日");

			remarkJson.put("value", remarkContent);
			remarkJson.put("color", "#173177");
		} else {
			String content = "恭喜您，成为会员~";
			contentJson.put("value", content);
			contentJson.put("color", "#173177");

			String remarkContent = "您已开通会员\n";
			remarkContent += "快去【谁看过我】功能\n";
			remarkContent += "获取您的意向客户吧~";

			remarkJson.put("value", remarkContent);
			remarkJson.put("color", "#173177");
		}
		NumberFormat nf = new DecimalFormat("00000000");
		String memberNumContent = nf.format(userId);
		memberNumJson.put("value", memberNumContent);
		memberNumJson.put("color", "#459ae9");
		
		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy年MM月dd日 HH:mm"));
		dataTimeJson.put("color", "#459ae9");
		
		data.put("first", contentJson);
		data.put("keyword1", memberNumJson);
		data.put("keyword2", dataTimeJson);
		data.put("remark", remarkJson);
		
		msgVO.setData(data.toJSONString());
		msgVO.setTouser(openId);
		
		messageCenterService.sendMsg(msgVO);
	}
	
	/**
	 * 推送资料福利信息
	 * @param openId
	 */
	private void sendMmeansMsg(String openId) {
		String content = "感谢您订购会员，送您2500+份企业管理制度模板\n" + 
				"百度网盘链接：https://pan.baidu.com/s/17oVTG6UGcvw1ttdpMg0G-A \n" + 
				"提取码：ltwd";
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setTouser(openId);
		msgVO.setContent(content);
		msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
		
		messageCenterService.sendMsg(msgVO);
	}
	
	/**
	 * 推送会员升阶信息
	 * @param openId
	 */
	private void sendMemberUpgradeMsg(int userId, String openId, int grade) {
		Date dt = new Date();
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setMsgType(MessageConstant.MSG_TYPE_MBXX);
		msgVO.setTemplateId(memberOpenTemplateId);
		msgVO.setUrl("");
		
		JSONObject data = new JSONObject();
		JSONObject contentJson = new JSONObject();
		JSONObject memberNumJson = new JSONObject();
		JSONObject dataTimeJson = new JSONObject();
		JSONObject remarkJson = new JSONObject();
		
		String content = "恭喜您，成功升级为千里眼";
		if(grade == 2) {
			content += "黄金";
		} else if(grade == 3) {
			content += "钻石";
		}
		content += "会员~";
		contentJson.put("value", content);
		contentJson.put("color", "#173177");

		NumberFormat nf = new DecimalFormat("00000000");
		String memberNumContent = nf.format(userId);
		memberNumJson.put("value", memberNumContent);
		memberNumJson.put("color", "#459ae9");
		
		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy年MM月dd日 HH:mm"));
		dataTimeJson.put("color", "#459ae9");
		
		String remarkContent = "快去【有谁看过】挖掘意向客户吧";
		remarkJson.put("value", remarkContent);
		remarkJson.put("color", "#173177");
		
		data.put("first", contentJson);
		data.put("keyword1", memberNumJson);
		data.put("keyword2", dataTimeJson);
		data.put("remark", remarkJson);
		
		msgVO.setData(data.toJSONString());
		msgVO.setTouser(openId);
		
		messageCenterService.sendMsg(msgVO);
	}
	
	/**
	 * 根据订单金额判断账户等级
	 * @param amount
	 * @return
	 */
	private int getMemberGrade(String isMember, int userId, double amount, String openId) {
		int grade = 0;
		
		if(amount >= 0.01 && amount < 88) {
			grade = 1;
		} else if(amount >= 88 && amount < 118) {
			grade = 2;
		} else if(amount >= 118 && amount < 288) {
			grade = 3;
		}
		
		if("1".equals(isMember)) {
			QlyRhUserAccountVO accountVO = marketService.getQlyRhUserAccountVO(userId);
			if(accountVO != null) {
				int member_grade = accountVO.getMember_grade().intValue();
				if(member_grade > grade) {
					grade = member_grade;
				} else if(grade >= 2){
					// 推送会员升阶通知
					sendMemberUpgradeMsg(userId, openId, grade);
				}
			}
			
		}
		
		return grade;
	}
	
	/**
	 * 计算用户收益
	 * @param openId
	 * @param recommender
	 * @param money
	 */
	private void calculationUserIncome(String openId, String recommender, double money) {
		float propt = 0, propt_2 = 0;
		// 他的上级伙伴
		QlyRhUserVO recdUserVO = dataCacheService.getQlyRhUserVO(recommender);
		QlyRhUserAccountVO accountVO = marketService.getQlyRhUserAccountVO(recdUserVO.getUser_id());
		if(accountVO != null) {
			int member_grade = accountVO.getMember_grade().intValue();
			if(member_grade == 2) {
				propt = (float) 0.15;
			} else if(member_grade == 3) {
				propt = (float) 0.2;
			}
			
			// 判断用户的会员是否已经到期或者是白银级别的，则不参与收益分成
			String isMember = userService.getIsMember(recommender);
			if("1".equals(isMember) && propt != 0) {
				// 一级伙伴的收益
				updateIncomeBalance(openId, recdUserVO.getUser_id(), money, propt, Constants.STREAM_TYPE_INCOME_L1, accountVO);
			} else {
				// 计算错失收益，按照最高等级来计算
				propt = (float) 0.2;
				updateMissIncome(recdUserVO.getUser_id(), money, propt, accountVO);
			}
			
			
		}
		
		String recd2Str = recdUserVO.getRecommender();
		// 他的上级的上级伙伴
		if(StringUtils.isNotBlank(recd2Str)) {
			QlyRhUserVO recd2UserVO = dataCacheService.getQlyRhUserVO(recd2Str);
			QlyRhUserAccountVO account2VO = marketService.getQlyRhUserAccountVO(recd2UserVO.getUser_id());
			if(account2VO != null) {
				int member_grade = account2VO.getMember_grade().intValue();
				if(member_grade == 2) {
					propt_2 = (float) 0.01;
				} else if(member_grade == 3) {
					propt_2 = (float) 0.02;
				}
				
				// 判断用户的会员是否已经到期或者是白银级别的，则不参与收益分成
				String isMember = userService.getIsMember(recd2Str);
				if("1".equals(isMember) && propt_2 != 0) {
					// 二级伙伴的收益
					updateIncomeBalance(openId, recd2UserVO.getUser_id(), money, propt_2, Constants.STREAM_TYPE_INCOME_L2, account2VO);
				} else {
					// 计算错失收益，按照最高等级来计算
					propt_2 = (float) 0.02;
					updateMissIncome(recd2UserVO.getUser_id(), money, propt_2, account2VO);
				}
				
			}
			
		}
		
	}
	
	private void updateIncomeBalance(String openId, int userId, double money, float propt, int stream_type, QlyRhUserAccountVO accountVO) {
		String currentDate = DateUtil.formatDate(new Date());
		double amount = money * propt;
		double income_balance = accountVO.getIncome_balance() == null ? 0.0 : accountVO.getIncome_balance();
		double balance = income_balance + amount;
		// 伙伴的收益
		QlyRhUserStreamVO streamVO = new QlyRhUserStreamVO();
		streamVO.setUser_id(userId);
		streamVO.setStream_date(currentDate);
		streamVO.setStream_type(stream_type);
		streamVO.setPartner(openId);
		streamVO.setAmount(amount);
		streamVO.setBalance(balance);
		streamVO.setRemark("收益");
		marketService.insertQlyRhUserStream(streamVO);
		logger.info("*************新增一条收益流水**************" + userId);
		
		// 更新我的账户
		QlyRhUserAccountVO updateAccountVO = new QlyRhUserAccountVO();
		updateAccountVO.setUser_id(userId);
		updateAccountVO.setIncome_balance(balance);
		marketService.updateQlyRhUserAccount(updateAccountVO);
		logger.info("*************更新我的账户**************" + userId);
	}
	
	private void updateMissIncome(int userId, double money, float propt, QlyRhUserAccountVO accountVO) {
		double amount = money * propt;
		double miss_income = accountVO.getMiss_income() == null ? 0.0 : accountVO.getMiss_income();
		double balance = miss_income + amount;
		
		// 更新我的账户
		QlyRhUserAccountVO updateAccountVO = new QlyRhUserAccountVO();
		updateAccountVO.setUser_id(userId);
		updateAccountVO.setMiss_income(balance);
		marketService.updateQlyRhUserAccount(updateAccountVO);
		logger.info("*************更新我的账户（错失收益）**************" + userId);
	}
	
	private int getOrderType(double money) {
		int orderType = 1;
		
		if(money == 88) {
			orderType = 2;
		} else if(money == 118 || money == 168) {
			orderType = 3;
		}
		
		return orderType;
	}
	
}
