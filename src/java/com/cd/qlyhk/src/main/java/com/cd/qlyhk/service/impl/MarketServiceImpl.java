package com.cd.qlyhk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhMarketDTO;
import com.cd.qlyhk.dto.QlyRhUserStreamDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.vo.QlyRhUserAccountVO;
import com.cd.qlyhk.vo.QlyRhUserStreamVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IMarketService.BEAN_ID)
public class MarketServiceImpl implements IMarketService {

	private static final Logger logger = LoggerFactory.getLogger(MarketServiceImpl.class);

	private final String mapperNamespace = MarketServiceImpl.class.getName();

	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private IUserService userService;
	
	@Override
	public void insertQlyRhUserStream(QlyRhUserStreamVO streamVO) {
		baseDAO.add(mapperNamespace + ".insertQlyRhUserStream", streamVO);
	}
	
	@Override
	public void updateQlyRhUserAccount(QlyRhUserAccountVO accountVO) {
		accountVO.setModify_datetime(new Date());
		int result = baseDAO.update(mapperNamespace + ".updateQlyRhUserAccount", accountVO);
		if(result == 0) {
			baseDAO.add(mapperNamespace + ".insertQlyRhUserAccount", accountVO);
		}
	}

	@Override
	public QlyRhUserAccountVO getQlyRhUserAccountVO(int userId) {
		return baseDAO.findOne(mapperNamespace + ".getQlyRhUserAccountVO", userId);
	}
	
	@Override
	public double getQlyRhUserStreamTotalIncome(Map<String, Object> param) {
		double result = 0;
		Double c = baseDAO.findOne(mapperNamespace + ".getQlyRhUserStreamTotalIncome", param);
		if(c != null) {
			result = c.doubleValue();
		}
		return result;
	}
	
	@Override
	public Response queryIncomeBalance(QlyRhUserStreamDTO streamDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(streamDTO.getOpenId());
		int userId = userVO.getUser_id();
		Date startDt = DateUtil.parseDateByFormat(streamDTO.getStream_month(), "yyyyMM");
		String start_date = DateUtil.formatDateByFormat(startDt, "yyyy-MM") + "-01";
		String end_date = DateUtil.getMonthEnd(start_date);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("start_date", start_date);
        paramMap.put("end_date", end_date);
        paramMap.put("stream_type", streamDTO.getStream_type());
        
     	QlyRhUserAccountVO accountVO = getQlyRhUserAccountVO(userId);
     	// 收益余额
     	double income_balance = 0;
     	if(accountVO != null) {
     		income_balance = accountVO.getIncome_balance();
     	}
     	// 累计收益
     	Map<String, Object> incomeMap = new HashMap<String, Object>();
     	incomeMap.put("total_type", "1");
     	incomeMap.put("userId", userId);
     	double total_income = getQlyRhUserStreamTotalIncome(incomeMap);
     	// 今日收益
     	Date dt = new Date();
     	String stream_date = DateUtil.formatDate(dt);
     	incomeMap.put("stream_date", stream_date);
     	double today_income = getQlyRhUserStreamTotalIncome(incomeMap);
     	
     	resp = queryQlyRhUserStreamList(paramMap, streamDTO.getPageQueryVO());
        
     	Map<String, Object> resultMap = (Map) resp.getData();
     	
        resultMap.put("total_income", total_income);
		resultMap.put("income_balance", income_balance);
		resultMap.put("today_income", today_income);
        resp.setData(resultMap);
        
		return resp;
	}
	
	@Override
	public Response queryPartnerMember(QlyRhUserStreamDTO streamDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(streamDTO.getOpenId());
		int userId = userVO.getUser_id();
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("openId", streamDTO.getOpenId());
        paramMap.put("user_name", streamDTO.getUser_name());

		//总数
        int totalRecord = 0;
        //查询总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryPartnerMemberCount", paramMap);
        
        PageQueryVO pageQueryVO = streamDTO.getPageQueryVO();
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        //收益余额列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	List<Map<String, Object>> tempList = baseDAO.findListByRange(mapperNamespace + ".queryPartnerMemberList", paramMap, page.getStart(), page.getLimit());
        	if(tempList != null && tempList.size() > 0) {
        		for(Map<String, Object> m : tempList) {
        			String partnerOpenId = (String) m.get("openId");
        			
        	     	Map<String, Object> incomeMap = new HashMap<String, Object>();
        	     	incomeMap.put("partnerOpenId", partnerOpenId);
        	     	incomeMap.put("userId", userId);
        	     	
        	     	double total_income = 0, income_1 = 0, income_2 = 0;
        	     	// 获取的一级收益
        	     	Double d1 = baseDAO.findOne(mapperNamespace + ".getPartnerTotalIncomeLevel1", incomeMap);
        			if(d1 != null) {
        				income_1 = d1.doubleValue();
        			}
        	     	
        	     	// 获取的二级收益
        			Double d2 = baseDAO.findOne(mapperNamespace + ".getPartnerTotalIncomeLevel2", incomeMap);
        			if(d2 != null) {
        				income_2 = d2.doubleValue();
        			}
        			// 从伙伴身上获取的总收益
        			total_income = income_1 + income_2;
        			
        			m.put("total_income", total_income);
        			list.add(m);
        		}
        		
        	}
        }
        
        resultMap.put("partnerList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
        
		return resp;
	}
	
	@Override
	public Response queryRetailList(QlyRhMarketDTO marketDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("recommender", marketDTO.getOpenId());
        paramMap.put("user_name", marketDTO.getUser_name());
        
        paramMap.put("member_grade", marketDTO.getMember_grade());
        paramMap.put("is_recm", marketDTO.getIs_recm());
        paramMap.put("ib_startNum", marketDTO.getIb_startNum());
        paramMap.put("ib_endNum", marketDTO.getIb_endNum());
        paramMap.put("ti_startNum", marketDTO.getTi_startNum());
        paramMap.put("ti_endNum", marketDTO.getTi_endNum());
        paramMap.put("tc_startNum", marketDTO.getTc_startNum());
        paramMap.put("tc_endNum", marketDTO.getTc_endNum());
        paramMap.put("pn_startNum", marketDTO.getPn_startNum());
        paramMap.put("pn_endNum", marketDTO.getPn_endNum());
        
        resp = queryRetailList(paramMap, marketDTO.getPageQueryVO());
        return resp;
	}
	
	@Override
	public Response queryRetailList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//总数
        int totalRecord = 0;
        //查询总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryRetailCount", paramMap);
        
        paramMap.put("orderName", pageQueryVO.getOrderName());
        paramMap.put("order", pageQueryVO.getOrder());
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        //收益余额列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	List<Map<String, Object>> tempList = baseDAO.findListByRange(mapperNamespace + ".queryRetailList", paramMap, page.getStart(), page.getLimit());
        	if(tempList != null && tempList.size() > 0) {
        		for(Map<String, Object> m : tempList) {
//        			int user_id = (int) m.get("user_id");
//        			String openId = (String) m.get("openId");
        			String superior_openId = m.get("recommender") == null ? "" : (String) m.get("recommender");
        			
        			double total_income_balance = 0.0, total_income = 0.0, total_cash_out = 0.0;
        			java.math.BigDecimal b_income_balance = m.get("total_income_balance") == null ? null : (java.math.BigDecimal) m.get("total_income_balance");
        			if(b_income_balance != null) {
        				total_income_balance = b_income_balance.doubleValue();
        			}
        			java.math.BigDecimal b_income = m.get("total_income") == null ? null: (java.math.BigDecimal) m.get("total_income");
        			if(b_income != null) {
        				total_income = b_income.doubleValue();
        			}
        			java.math.BigDecimal b_cash_out = m.get("total_cash_out") == null ? null : (java.math.BigDecimal) m.get("total_cash_out");
        			if(b_cash_out != null) {
        				total_cash_out = b_cash_out.doubleValue();
        			}
        			int total_partner_num = m.get("total_partner_num") == null ? 0 : (int) m.get("total_partner_num");
        			
        	     	// 上级用户
        	     	String superior_user_name = "", superior_headImgUrl = "";
        	     	if(StringUtils.isNotBlank(superior_openId)) {
        	     		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(superior_openId);
        	     		superior_user_name = userVO.getUser_name();
        	     		superior_headImgUrl = userVO.getHeadImgUrl();
        	     	}
        	     	
        	     	Map<String, Object> retMap = new HashMap<String, Object>();
        	     	retMap.put("openId", m.get("openId"));
        	     	retMap.put("user_name", m.get("user_name"));
        	     	retMap.put("headImgUrl", m.get("headImgUrl"));
        	     	retMap.put("member_grade", m.get("member_grade"));
        	     	retMap.put("income_balance", total_income_balance);
        	     	retMap.put("total_income", total_income);
        	     	retMap.put("total_cashOut", total_cash_out);
        	     	retMap.put("superior_user_name", superior_user_name);
        	     	retMap.put("superior_headImgUrl", superior_headImgUrl);
        	     	retMap.put("partner_num", total_partner_num);
        			
        			list.add(retMap);
        		}
        		
        	}
        }
		
		resultMap.put("retailList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
        
		return resp;
	}
	
	@Override
	public Response queryPenetBalance(QlyRhUserStreamDTO streamDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(streamDTO.getOpenId());
		int userId = userVO.getUser_id();
		Date startDt = DateUtil.parseDateByFormat(streamDTO.getStream_month(), "yyyyMM");
		String start_date = DateUtil.formatDateByFormat(startDt, "yyyy-MM") + "-01";
		String end_date = DateUtil.getMonthEnd(start_date);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("start_date", start_date);
        paramMap.put("end_date", end_date);
        paramMap.put("stream_type", streamDTO.getStream_type());
        
        resp = queryQlyRhUserStreamList(paramMap, streamDTO.getPageQueryVO());
        resp.setMessage("查询穿透-余额成功");
        
		return resp;
	}
	
	@Override
	public Response queryQlyRhUserStreamList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//总数
        int totalRecord = 0;
        //查询总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryIncomeBalanceCount", paramMap);
        
//        PageQueryVO pageQueryVO = streamDTO.getPageQueryVO();
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        //收益余额列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".queryIncomeBalanceList", paramMap, page.getStart(), page.getLimit());
        }
        
        resultMap.put("incomeBalanceList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
        
        return resp;
	}
}
