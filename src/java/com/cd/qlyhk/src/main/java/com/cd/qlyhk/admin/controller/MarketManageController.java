package com.cd.qlyhk.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.dto.QlyRhMarketDTO;
import com.cd.qlyhk.dto.QlyRhUserStreamDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMarketService;

/**
 * 分销管理控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/admin/market")
public class MarketManageController {

	private static final Logger logger = LoggerFactory.getLogger(MarketManageController.class);

	@Resource(name = IMarketService.BEAN_ID)
	private IMarketService   marketService;
	
	/**
	 * 分销列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/retail.do")
	@ResponseBody
	public Response retail(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhMarketDTO marketDTO) {
		logger.debug("***************************分销列表********************************");
		logger.debug("查询分销列表列表请求参数:"+ JSON.toJSONString(marketDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = marketService.queryRetailList(marketDTO);
    		resp.setMessage("查询分销列表成功");
    		logger.debug("***************************分销列表耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("分销列表信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("分销列表信息查询失败");
        }
		logger.debug("分销列表，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 穿透-伙伴
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/penetPartner.do")
	@ResponseBody
	public Response penetPartner(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhMarketDTO marketDTO) {
		logger.debug("***************************穿透-伙伴********************************");
		logger.debug("查询穿透-伙伴列表请求参数:"+ JSON.toJSONString(marketDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = marketService.queryRetailList(marketDTO);
    		resp.setMessage("查询穿透-伙伴成功");
    		logger.debug("***************************穿透-伙伴耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("穿透-伙伴信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("穿透-伙伴信息查询失败");
        }
		logger.debug("穿透-伙伴，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 穿透-余额
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/penetBalance.do")
	@ResponseBody
	public Response penetBalance(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhUserStreamDTO streamDTO) {
		logger.debug("***************************穿透-余额********************************");
		logger.debug("查询穿透-余额列表请求参数:"+ JSON.toJSONString(streamDTO));
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = marketService.queryPenetBalance(streamDTO);
    		logger.debug("***************************穿透-余额耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("穿透-余额信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("穿透-余额信息查询失败");
        }
		logger.debug("穿透-余额，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
}
