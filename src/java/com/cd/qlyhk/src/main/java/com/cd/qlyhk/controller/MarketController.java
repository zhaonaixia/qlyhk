package com.cd.qlyhk.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.dto.QlyRhUserStreamDTO;
import com.cd.qlyhk.dto.ReqEnterprisePaymentDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IOrderService;

/**
 * 分销管理控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/market")
public class MarketController {

	private static final Logger logger = LoggerFactory.getLogger(MarketController.class);

	@Value("${wx.appid}")
	public String appId;

	@Value("${wx.mchid}")
	public String mchId;

	@Value("${wx.mchsecret}")
	public String mchSecret;
	
	@Value("${wx.certpath}")
	public String certPath;
	
	@Resource(name = IMarketService.BEAN_ID)
	private IMarketService   marketService;
	
	@Resource(name = IOrderService.BEAN_ID)
	private IOrderService orderService;
	
	/**
	 * 收益余额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/incomeBalance.do")
	@ResponseBody
	public Response incomeBalance(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhUserStreamDTO streamDTO) {
		logger.info("***************************收益余额********************************");
		logger.info("查询收益余额列表请求参数:"+ JSON.toJSONString(streamDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = marketService.queryIncomeBalance(streamDTO);
    		resp.setMessage("查询收益余额成功");
    		logger.info("***************************收益余额耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("收益余额信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("收益余额信息查询失败");
        }
		logger.info("收益余额，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 我的伙伴
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/partnerMember.do")
	@ResponseBody
	public Response partnerMember(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhUserStreamDTO streamDTO) {
		logger.info("***************************我的伙伴********************************");
		logger.info("查询我的伙伴列表请求参数:"+ JSON.toJSONString(streamDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = marketService.queryPartnerMember(streamDTO);
    		resp.setMessage("查询我的伙伴成功");
    		logger.info("***************************我的伙伴耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("我的伙伴信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("我的伙伴信息查询失败");
        }
		logger.info("我的伙伴，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 提现
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cashWithdrawal.do")
	@ResponseBody
	public Response cashWithdrawal(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqEnterprisePaymentDTO paymentDTO) {
		logger.info("***************************提现********************************");
		logger.info("提现请求参数:"+ JSON.toJSONString(paymentDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
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
    		resp = orderService.enterprisePayment(appId, mchId, mchSecret, certPath, ip, paymentDTO);
    		logger.info("***************************提现耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("提现失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("提现失败");
        }
		logger.info("提现，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 查询提现是否成功
	 * @param request
	 * @param response
	 * @param paymentDTO
	 * @return
	 */
	@RequestMapping("/queryCashWithdrawal.do")
	@ResponseBody
	public Response queryCashWithdrawal(HttpServletRequest request, HttpServletResponse response, @RequestParam String orderNo) {
		logger.info("***************************查询提现********************************");
		logger.info("查询提现请求参数:"+ orderNo);
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = orderService.queryCashWithdrawal(appId, mchId, mchSecret, certPath, orderNo);
    		logger.info("***************************查询提现耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("查询提现失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询提现失败");
        }
		logger.info("查询提现，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
}
