package com.cd.qlyhk.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IOrderService;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhOrderVO;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;

/**
 * 订单管理控制器
 * 
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/order")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Resource(name = IOrderService.BEAN_ID)
	private IOrderService orderService;

	@Value("${wx.appid}")
	public String appId;

	@Value("${wx.mchid}")
	public String mchId;

	@Value("${wx.mchsecret}")
	public String mchSecret;

	@Value("${wx.callback.url}")
	public String callbackUrl;
	
	@Value("${local.debug.no.boss}")
	public String propValue;

	/**
	 * 获取套餐列表接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getPackageList.do")
	@ResponseBody
	public Response getPackageList(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************获取套餐列表********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			result = orderService.queryQlyRhPackage(propValue);
		} catch (Exception e) {
			logger.error("获取套餐列表异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取套餐列表失败");
		}
		logger.info("获取套餐列表，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 新建订单。
	 * 
	 * @param request
	 * @param response
	 * @param packageId
	 * @param openId
	 * @return Response
	 */
	@RequestMapping("/newOrder.do")
	@ResponseBody
	public Response newOrder(HttpServletRequest request, HttpServletResponse response, @RequestParam String packageId, @RequestParam String packageCode,
			@RequestParam String openId) {
		logger.info("***************************新建订单********************************");
		logger.info("新建订单请求参数:{},{}", packageId, openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = orderService.createOrder(propValue, packageId, packageCode, openId);
		} catch (Exception e) {
			logger.error("新建订单异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("新建订单失败");
		}
		logger.info("新建订单，返回数据={}", JSON.toJSON(result));
		return result;

	}

	/**
	 * 调用统一下单接口。
	 * 
	 * @param request
	 * @param response
	 * @param packageId
	 * @param openId
	 * @return Response
	 */
	@RequestMapping("/payfor.do")
	@ResponseBody
	public Response payfor(HttpServletRequest request, HttpServletResponse response, @RequestParam String orderId,
			@RequestParam String openId) {
		logger.info("***************************调用统一下单接口********************************");
		logger.info("调用统一下单接口请求参数:{},{}", orderId, openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			// 根据orderId查询 订单
			QlyRhOrderVO order = orderService.getQlyRhOrderVO(orderId);
			if (order == null) {
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("订单不存在或者已删除");
				
				return result;
			}

			// 测试改为固定金额
//			BigDecimal amount = new BigDecimal(order.getMoney());
			
			// 拼接统一下单地址参数
			Map<String, String> paraMap = new HashMap<String, String>();

			// 获取请求ip地址
			String ip = request.getHeader("x-forwarded-for");

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}

			if (ip.indexOf(",") != -1) {
				String[] ips = ip.split(",");
				ip = ips[0].trim();
			}

			double amount = order.getMoney();
			amount = amount * 100;
			NumberFormat nf0 = new DecimalFormat("0");
			// 将金额转成分
//			String money = amount.multiply(new BigDecimal(100)).toString();
			String money = nf0.format(amount);
			logger.info("*****************将金额转成分****************" + money);
			
			paraMap.put("appid", appId);
			paraMap.put("mch_id", mchId);
			paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
			paraMap.put("body", "会员充值");
			paraMap.put("out_trade_no", orderId);// 订单号
			paraMap.put("total_fee", money); // 金额以（分）为单位
			paraMap.put("spbill_create_ip", ip);
			paraMap.put("notify_url", callbackUrl);// 此路径是微信服务器调用支付结果通知路径
			paraMap.put("trade_type", "JSAPI");
			paraMap.put("openid", openId);

//          Boolean useSandbox = false;// 是否是沙箱测试
//          WXPayConfigImpl config = new WXPayConfigImpl(useSandbox);
////          WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, useSandbox);
//          logger.info("生成签名:{}", config.getKey());
//          
//          String sign = WXPayUtil.generateSignature(paraMap, config.getKey(), WXPayConstants.SignType.MD5); //
			String sign = WXPayUtil.generateSignature(paraMap, mchSecret);// 商户密码
			paraMap.put("sign", sign);

			logger.info("调用统一下单接口请求参数:{}", paraMap);
			String xml = WXPayUtil.mapToXml(paraMap);// 将所有参数(map)转xml格式

			// 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
			String unifiedorder_url = WXPayConstants.UNIFIEDORDER_URL;

			String xmlStr = HttpRequestUtil.doPostXml(unifiedorder_url, xml);// 发送post请求"统一下单接口"返回预支付id:prepay_id
			logger.info("调用统一下单接口结果返回:{}", xmlStr);

			// 以下内容是返回前端页面的json数据
			String prepay_id = "";// 预支付id

			if (xmlStr.indexOf("SUCCESS") != -1) {
				Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
				prepay_id = (String) map.get("prepay_id");
			}
			Map<String, String> payMap = new HashMap<String, String>();

			payMap.put("appId", appId);
			payMap.put("timeStamp", WxUtil.getCurrentTimestamp() + "");
			payMap.put("nonceStr", WXPayUtil.generateNonceStr());
			payMap.put("signType", "MD5");
			payMap.put("package", "prepay_id=" + prepay_id);
			logger.info("=======prepay_id:" + prepay_id);

			if (StringUtils.isNotBlank(prepay_id)) {
				String paySign = WXPayUtil.generateSignature(payMap, mchSecret);
				payMap.put("paySign", paySign);

				result.setData(payMap);
			} else {
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("调用统一订单接口错误");
			}

		} catch (Exception e) {
			logger.error("调用统一下单接口异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("调用统一下单接口失败");
		}
		logger.info("调用统一下单接口，返回数据={}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 微信支付结果回调接口。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/callback.do")
	@ResponseBody
	public String callback(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************微信支付结果回调********************************");
		InputStream is = null;
		try {
			is = request.getInputStream();// 获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
			String xml = WxUtil.inputStream2String(is);
			logger.info("收到回调请求报文：{}", xml);
			Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);// 将微信发的xml转map

			if (notifyMap.get("return_code").equals("SUCCESS")) {
				if (notifyMap.get("result_code").equals("SUCCESS")) {
					String ordersSn = notifyMap.get("out_trade_no");// 商户订单号
					String amountpaid = notifyMap.get("total_fee");// 实际支付的订单金额:单位 分
					String openId = notifyMap.get("openid");
					logger.info("微信支付成功，返回参数：{},{}", ordersSn, amountpaid);
					//将分转换成元-实际支付金额:元
//                    BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);
					float amountPay = Float.parseFloat(amountpaid);
					amountPay = amountPay / 100;
					NumberFormat nf0 = new DecimalFormat("0.##");
					// 处理订单状态
					QlyRhOrderVO order = orderService.getQlyRhOrderVO(ordersSn);
					if(order != null) {
//						BigDecimal orderMoney = new BigDecimal(order.getMoney());
						String orderMoney = nf0.format(order.getMoney());
						String amountPayStr = nf0.format(amountPay);
						logger.info("判断实际支付的金额与订单金额是否一致：{},{}", amountPayStr, orderMoney);
						
						if(amountPayStr.equals(orderMoney)) {
							orderService.orderFinish(propValue, openId, order);
						}
						
					}
					
				}
			}

			// 告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
			response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
