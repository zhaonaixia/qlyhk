package com.cd.qlyhk.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.dto.QlyRhSysAreaDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IMarketService;
import com.cd.qlyhk.service.IStatisticsService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.vo.QlyRhArticleStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMarketStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMonthStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMonthlyArticlesStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMonthlyReadStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMonthlyShareStatisticsVO;
import com.cd.qlyhk.vo.QlyRhMonthlyUsersStatisticsVO;
import com.cd.qlyhk.vo.QlyRhRecordStatisticsVO;
import com.cd.qlyhk.vo.QlyRhShareStatisticsVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhUserStatisticsVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IStatisticsService.BEAN_ID)
public class StatisticsServiceImpl implements IStatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
	
	private final String mapperNamespace = StatisticsServiceImpl.class.getName();
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IMarketService marketService;
	
	@Override
	public void executeReadArticleStatistics() {
		try {
			String statisticsDate = DateUtil.formatDate(new Date());
			List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
			if(userList != null && userList.size() > 0) {
				for(QlyRhUserVO user : userList) {
					String openId = user.getOpenId();
//					logger.debug("***********************开始统计【"+ openId +"】分享文章的阅读情况*******************************");
					
					List<QlyRhShareVO> shareList = articleService.queryShareArticles(openId);
					if(shareList != null && shareList.size() > 0) {
						
						for(QlyRhShareVO shareVO : shareList) {
							String share_uuid = shareVO.getUuid();
							int total_num = 0, total_readers = 0, share_num = 0, share_readers = 0;
							total_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalNumByUUID", share_uuid);
							total_readers = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadersByUUID", share_uuid);

							share_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getShareNum", share_uuid);
							share_readers = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getShareReaders", share_uuid);
							
//							Map<String, Object> paramMap = new HashMap<String, Object>();
//							paramMap.put("openId", openId);
//							paramMap.put("uuid", share_uuid);
							
							QlyRhShareStatisticsVO statisticsVO = new QlyRhShareStatisticsVO();
							statisticsVO.setStatistics_date(statisticsDate);
							statisticsVO.setShare_uuid(share_uuid);
							statisticsVO.setSharer(openId);
							statisticsVO.setTotal_num(total_num);
							statisticsVO.setTotal_readers(total_readers);
							statisticsVO.setShare_num(share_num);
							statisticsVO.setShare_readers(share_readers);
							statisticsVO.setModify_datetime(new Date());
							
							int result = baseDAO.update(mapperNamespace + ".updateShareStatistics", statisticsVO);
							if(result == 0) {
								baseDAO.add(mapperNamespace + ".insertShareStatistics", statisticsVO);
							}
							// 判断是已经统计过该用户分享的文章
							/*
							 * QlyRhShareStatisticsVO statisticsVO = baseDAO.findOne(mapperNamespace +
							 * ".queryShareStatistics", paramMap); if(statisticsVO != null) {
							 * statisticsVO.setStatistics_date(statisticsDate);
							 * statisticsVO.setTotal_num(total_num);
							 * statisticsVO.setTotal_readers(total_readers);
							 * statisticsVO.setShare_num(share_num);
							 * statisticsVO.setShare_readers(share_readers);
							 * statisticsVO.setModify_datetime(new Date()); baseDAO.update(mapperNamespace +
							 * ".updateShareStatistics", statisticsVO); } else { statisticsVO = new
							 * QlyRhShareStatisticsVO(); statisticsVO.setStatistics_date(statisticsDate);
							 * statisticsVO.setShare_uuid(share_uuid); statisticsVO.setSharer(openId);
							 * statisticsVO.setTotal_num(total_num);
							 * statisticsVO.setTotal_readers(total_readers);
							 * statisticsVO.setShare_num(share_num);
							 * statisticsVO.setShare_readers(share_readers); baseDAO.add(mapperNamespace +
							 * ".insertShareStatistics", statisticsVO); }
							 */
							
						}
//						logger.debug("***********************结束统计【"+ openId +"】分享文章的阅读情况*******************************");
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("统计分享文章的阅读情况异常，出错信息：" + e);
		}
	}

	@Override
	public void executeRecordStatistics() {
		try {
			String statisticsDate = DateUtil.formatDate(new Date());
			List<Map<String, Object>> userList = baseDAO.findListBy(mapperNamespace + ".queryRecordStatisticsUser");
			if(userList != null && userList.size() > 0) {
				for(Map<String, Object> user : userList) {
					String openId = (String) user.get("visitor_id");
//					logger.debug("***********************开始统计【"+ openId +"】访客情况*******************************");
					int total_num = 0, total_readtimes = 0, total_sharenum = 0;

					total_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalNumRead", openId);
					Integer iTotalReadtimes = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadTime", openId);
					if(iTotalReadtimes != null) {
						total_readtimes = iTotalReadtimes.intValue();
					}
					total_sharenum = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalShareNum", openId);
					
					QlyRhRecordStatisticsVO statisticsVO = new QlyRhRecordStatisticsVO();
					statisticsVO.setStatistics_date(statisticsDate);
					statisticsVO.setRecord_id(openId);
					statisticsVO.setTotal_num(total_num);
					statisticsVO.setTotal_reader_times(total_readtimes);
					statisticsVO.setTotal_share(total_sharenum);
					statisticsVO.setModify_datetime(new Date());
					
					int result = baseDAO.update(mapperNamespace + ".updateRecordStatistics", statisticsVO);
					if(result == 0) {
						baseDAO.add(mapperNamespace + ".insertRecordStatistics", statisticsVO);
					}
					
					// 判断是已经统计过该用户分享的文章
					/*
					 * QlyRhRecordStatisticsVO statisticsVO = baseDAO.findOne(mapperNamespace +
					 * ".queryRecordStatistics", openId); if(statisticsVO != null) {
					 * statisticsVO.setStatistics_date(statisticsDate);
					 * statisticsVO.setTotal_num(total_num);
					 * statisticsVO.setTotal_reader_times(total_readtimes);
					 * statisticsVO.setTotal_share(total_sharenum);
					 * statisticsVO.setModify_datetime(new Date()); baseDAO.update(mapperNamespace +
					 * ".updateRecordStatistics", statisticsVO); } else { statisticsVO = new
					 * QlyRhRecordStatisticsVO(); statisticsVO.setStatistics_date(statisticsDate);
					 * statisticsVO.setRecord_id(openId); statisticsVO.setTotal_num(total_num);
					 * statisticsVO.setTotal_reader_times(total_readtimes);
					 * statisticsVO.setTotal_share(total_sharenum); baseDAO.add(mapperNamespace +
					 * ".insertRecordStatistics", statisticsVO); }
					 */
//					logger.debug("***********************结束统计【"+ openId +"】访客情况*******************************");
				}
				
			}
		} catch(Exception e) {
			logger.error("统计访客情况异常，出错信息：" + e);
		}
		
	}
	
	@Override
	public void executeArticleShareAndReadStatistics() {
		try {
			String statisticsDate = DateUtil.formatDate(new Date());
			List<Map<String, Object>> articleList = baseDAO.findListBy(mapperNamespace + ".queryShareAndReadArticleUuid");
			if(articleList != null && articleList.size() > 0) {
				for(Map<String, Object> articleMap : articleList) {
					String article_uuid = (String) articleMap.get("article_uuid");
//					logger.debug("***********************开始统计【"+ article_uuid +"】文章分享及阅读情况*******************************");
					
					int total_num = 0, total_sharenum = 0;

					total_num = baseDAO.findOne(mapperNamespace + ".getTotalNumByArticleUUID", article_uuid);
					total_sharenum = baseDAO.findOne(mapperNamespace + ".getTotalShareNumByArticleUUID", article_uuid);
					
					QlyRhArticleStatisticsVO statisticsVO = new QlyRhArticleStatisticsVO();
					statisticsVO.setStatistics_date(statisticsDate);
					statisticsVO.setArticle_uuid(article_uuid);
					statisticsVO.setTotal_read_num(total_num);
					statisticsVO.setTotal_share_num(total_sharenum);
					statisticsVO.setModify_datetime(new Date());
					
					int result = baseDAO.update(mapperNamespace + ".updateArticleStatistics", statisticsVO);
					if(result == 0) {
						baseDAO.add(mapperNamespace + ".insertArticleStatistics", statisticsVO);
					}
//					logger.debug("***********************结束统计【"+ article_uuid +"】文章分享及阅读情况*******************************");
				}
			}
		} catch(Exception e) {
			logger.error("统计文章分享及阅读情况异常，出错信息：" + e);
		}
	}
	
	@Override
	public void executeUserShareAndReadStatistics() {
		try {
			String statisticsDate = DateUtil.formatDate(new Date());
			List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
			if(userList != null && userList.size() > 0) {
				for(QlyRhUserVO user : userList) {
					String openId = user.getOpenId();
//					logger.debug("***********************开始统计用户【"+ openId +"】分享及阅读情况*******************************");
					
					int total_num = 0, total_sharenum = 0, total_readtimes = 0;

					total_num = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalNumRead", openId);
					Integer iTotalReadtimes = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalReadTime", openId);
					if(iTotalReadtimes != null) {
						total_readtimes = iTotalReadtimes.intValue();
					}
					total_sharenum = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getTotalShareNum", openId);
					
					QlyRhUserStatisticsVO statisticsVO = new QlyRhUserStatisticsVO();
					statisticsVO.setStatistics_date(statisticsDate);
					statisticsVO.setOpenId(openId);
					statisticsVO.setTotal_read_num(total_num);
					statisticsVO.setTotal_share_num(total_sharenum);
					statisticsVO.setTotal_read_times(total_readtimes);
					statisticsVO.setModify_datetime(new Date());
					
					int result = baseDAO.update(mapperNamespace + ".updateUserStatistics", statisticsVO);
					if(result == 0) {
						baseDAO.add(mapperNamespace + ".insertUserStatistics", statisticsVO);
					}
//					logger.debug("***********************结束统计用户【"+ openId +"】分享及阅读情况*******************************");
				}
			}
		} catch(Exception e) {
			logger.error("统计用户分享及阅读情况异常，出错信息：" + e);
		}
	}
	
	@Override
	public void executeMonthlyShareStatistics() {
		try {
			String firstday = DateUtil.getFirstDayOfMonth();
			String lastday = DateUtil.getLastDayOfMonth();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("firstday", firstday);
			paramMap.put("lastday", lastday);
			
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryMonthlyShareList", paramMap);
			logger.debug("***********************开始统计月度分享时段情况*******************************" + list.size());
			Map<String, Object> hoursMap = getHoursMap(list);
			
			if(!hoursMap.isEmpty()) {
				String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
				int iStatisticsPeriod = Integer.parseInt(statistics_period);
				JSONObject dataJSON = new JSONObject(hoursMap);
				
				QlyRhMonthlyShareStatisticsVO statisticsVO = new QlyRhMonthlyShareStatisticsVO();
				statisticsVO.setStatistics_period(iStatisticsPeriod);
				statisticsVO.setData(dataJSON.toJSONString());
				statisticsVO.setModify_datetime(new Date());
				
				int result = baseDAO.update(mapperNamespace + ".updateMonthlyShareStatistics", statisticsVO);
				if(result == 0) {
					baseDAO.add(mapperNamespace + ".insertMonthlyShareStatistics", statisticsVO);
				}
				logger.debug("***********************结束统计月度分享时段情况*******************************");
			}
			
		} catch(Exception e) {
			logger.error("统计月度分享时段情况异常，出错信息：" + e);
		}
		
	}
	
	@Override
	public void executeMonthlyReadStatistics() {
		try {
			String firstday = DateUtil.getFirstDayOfMonth();
			String lastday = DateUtil.getLastDayOfMonth();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("firstday", firstday);
			paramMap.put("lastday", lastday);
			
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryMonthlyReadList", paramMap);
			logger.debug("***********************开始统计月度阅读时段情况*******************************" + list.size());
			Map<String, Object> hoursMap = getHoursMap(list);
			
			if(!hoursMap.isEmpty()) {
				String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
				int iStatisticsPeriod = Integer.parseInt(statistics_period);
				JSONObject dataJSON = new JSONObject(hoursMap);
				
				QlyRhMonthlyReadStatisticsVO statisticsVO = new QlyRhMonthlyReadStatisticsVO();
				statisticsVO.setStatistics_period(iStatisticsPeriod);
				statisticsVO.setData(dataJSON.toJSONString());
				statisticsVO.setModify_datetime(new Date());
				
				int result = baseDAO.update(mapperNamespace + ".updateMonthlyReadStatistics", statisticsVO);
				if(result == 0) {
					baseDAO.add(mapperNamespace + ".insertMonthlyReadStatistics", statisticsVO);
				}
				logger.debug("***********************结束统计月度阅读时段情况*******************************");
			}
			
		} catch(Exception e) {
			logger.error("统计月度阅读时段情况异常，出错信息：" + e);
		}
		
	}
	
	@Override
	public void executeMonthlyArticlesStatistics() {
		try {
			String firstday = DateUtil.getFirstDayOfMonth();
			String lastday = DateUtil.getLastDayOfMonth();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("firstday", firstday);
			paramMap.put("lastday", lastday);
			
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryMonthlyArticlesList", paramMap);
			logger.debug("***********************开始统计月度文章情况*******************************" + list.size());
			String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
			int iStatisticsPeriod = Integer.parseInt(statistics_period);
			
			if(list != null && list.size() > 0) {
				for(Map<String, Object> articleMap : list) {
					String article_uuid = (String) articleMap.get("article_uuid");
					Long total_num = (Long) articleMap.get("read_num");

					QlyRhMonthlyArticlesStatisticsVO statisticsVO = new QlyRhMonthlyArticlesStatisticsVO();
					statisticsVO.setStatistics_period(iStatisticsPeriod);
					statisticsVO.setArticle_uuid(article_uuid);
					statisticsVO.setTotal_read_num(total_num.intValue());
					statisticsVO.setTotal_share_num(0);
					statisticsVO.setModify_datetime(new Date());
					
					int result = baseDAO.update(mapperNamespace + ".updateMonthlyArticlesStatistics", statisticsVO);
					if(result == 0) {
						baseDAO.add(mapperNamespace + ".insertMonthlyArticlesStatistics", statisticsVO);
					}
					
				}
			}
			logger.debug("***********************结束统计月度文章情况*******************************");
		} catch(Exception e) {
			logger.error("统计月度文章情况异常，出错信息：" + e);
		}
		
	}
	
	@Override
	public void executeMonthlyUsersStatistics() {
		try {
			String firstday = DateUtil.getFirstDayOfMonth();
			String lastday = DateUtil.getLastDayOfMonth();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("firstday", firstday);
			paramMap.put("lastday", lastday);
			
			statisticsMonthlyUsersShare(paramMap);
			statisticsMonthlyUsersRead(paramMap);
			logger.debug("***********************结束统计月度用户情况*******************************");
		} catch(Exception e) {
			logger.error("统计月度用户情况异常，出错信息：" + e);
		}
	}
	
	private Map<String, Object> getHoursMap(List<Map<String, Object>> data) throws Exception {
		Map<String, Object> hoursMap = new HashMap<String, Object>();
		if(data != null && data.size() > 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			for(Map<String, Object> m : data) {
				String dateTime = (String) m.get("date_time");
				Date dt = df.parse(dateTime);
				String hours = DateUtil.formatDateByFormat(dt, "HH");
				int num = 0;
				if(hoursMap.get(hours) == null) {
					num++;
					hoursMap.put(hours, num);
				} else {
					num = (int) hoursMap.get(hours);
					num++;
					hoursMap.put(hours, num);
				}
			}
		}
		
		if(!hoursMap.isEmpty()) {
			// 补全其他时段为空的情况
			NumberFormat nf0 = new DecimalFormat("00");
			for(int i = 0; i < 24; i++) {
				String key = nf0.format(i);
				if(hoursMap.get(key) == null) {
					hoursMap.put(key, 0);
				}
			}
		}
		
		return hoursMap;
	}
	
	private void statisticsMonthlyUsersShare(Map<String, String> paramMap) {
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryMonthlyUsersShareList", paramMap);
		logger.debug("***********************开始统计月度用户情况1*******************************" + list.size());
		String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		int iStatisticsPeriod = Integer.parseInt(statistics_period);
		
		if(list != null && list.size() > 0) {
			for(Map<String, Object> articleMap : list) {
				String openId = (String) articleMap.get("openId");
				Long total_share_num = (Long) articleMap.get("share_num");

				QlyRhMonthlyUsersStatisticsVO statisticsVO = new QlyRhMonthlyUsersStatisticsVO();
				statisticsVO.setStatistics_period(iStatisticsPeriod);
				statisticsVO.setOpenId(openId);
				statisticsVO.setTotal_share_num(total_share_num.intValue());
				statisticsVO.setModify_datetime(new Date());
				
				int result = baseDAO.update(mapperNamespace + ".updateMonthlyUsersStatistics", statisticsVO);
				if(result == 0) {
					statisticsVO.setTotal_read_num(0);
					baseDAO.add(mapperNamespace + ".insertMonthlyUsersStatistics", statisticsVO);
				}
				
			}
		}
	}
	
	private void statisticsMonthlyUsersRead(Map<String, String> paramMap) {
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryMonthlyUsersReadList", paramMap);
		logger.debug("***********************开始统计月度用户情况2*******************************" + list.size());
		String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		int iStatisticsPeriod = Integer.parseInt(statistics_period);
		
		if(list != null && list.size() > 0) {
			for(Map<String, Object> articleMap : list) {
				String openId = (String) articleMap.get("openId");
				Long total_read_num = (Long) articleMap.get("read_num");

				QlyRhMonthlyUsersStatisticsVO statisticsVO = new QlyRhMonthlyUsersStatisticsVO();
				statisticsVO.setStatistics_period(iStatisticsPeriod);
				statisticsVO.setOpenId(openId);
				statisticsVO.setTotal_read_num(total_read_num.intValue());
				statisticsVO.setModify_datetime(new Date());
				
				int result = baseDAO.update(mapperNamespace + ".updateMonthlyUsersStatistics", statisticsVO);
				if(result == 0) {
					statisticsVO.setTotal_share_num(0);
					baseDAO.add(mapperNamespace + ".insertMonthlyUsersStatistics", statisticsVO);
				}
				
			}
		}
	}
	
	@Override
	public Response queryMonthlyShareList(int yearPeriodStart, int yearPeriodEnd) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 如果传入的参数是当前月份,则实时统计一次数据
		/** 2020-03-24注释，现在已改成每天统计一次。
		String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		int sPeriod = Integer.parseInt(statistics_period);
		if(yearPeriod == sPeriod) {
			logger.debug("********************开始统计月度分享时段分析************************");
			this.executeMonthlyShareStatistics();
		}**/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("yearPeriodStart", yearPeriodStart);
		paramMap.put("yearPeriodEnd", yearPeriodEnd);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		List<QlyRhMonthlyShareStatisticsVO> statisticsList = baseDAO.findListBy(mapperNamespace + ".queryMonthlyShareStatistics", paramMap);
		if(statisticsList != null && statisticsList.size() > 0) {
			NumberFormat nf = new DecimalFormat("00");
			for(QlyRhMonthlyShareStatisticsVO statisticsVO : statisticsList) {
				String jsonData = statisticsVO.getData();
				if(StringUtils.isNotBlank(jsonData)) {
					JSONObject jsonObj = JSONObject.parseObject(jsonData);
					for(int i = 0; i < 24; i++) {
						String key = nf.format(i);
						int iNum = 0;
						if(retMap.get(key) != null) {
							iNum = (int) retMap.get(key);
						}
						iNum += jsonObj.getIntValue(key);
						
						retMap.put(key, iNum);
					}
				}
			}
		}
		
//		if(statisticsVO == null) {
//			statisticsVO = new QlyRhMonthlyShareStatisticsVO();
//		}
		resultMap.put("shareTimes", retMap);
		result.setData(resultMap);
		
		return result;
	}
	
	@Override
	public Response queryMonthlyReadList(int yearPeriodStart, int yearPeriodEnd) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 如果传入的参数是当前月份,则实时统计一次数据
		/**2020-03-24注释，现在已改成每天统计一次。
		String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		int sPeriod = Integer.parseInt(statistics_period);
		if(yearPeriod == sPeriod) {
			logger.debug("********************开始统计月度阅读时段分析************************");
			this.executeMonthlyReadStatistics();
		}**/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("yearPeriodStart", yearPeriodStart);
		paramMap.put("yearPeriodEnd", yearPeriodEnd);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		List<QlyRhMonthlyReadStatisticsVO> statisticsList = baseDAO.findListBy(mapperNamespace + ".queryMonthlyReadStatistics", paramMap);
		if(statisticsList != null && statisticsList.size() > 0) {
			NumberFormat nf = new DecimalFormat("00");
			for(QlyRhMonthlyReadStatisticsVO statisticsVO : statisticsList) {
				String jsonData = statisticsVO.getData();
				if(StringUtils.isNotBlank(jsonData)) {
					JSONObject jsonObj = JSONObject.parseObject(jsonData);
					for(int i = 0; i < 24; i++) {
						String key = nf.format(i);
						int iNum = 0;
						if(retMap.get(key) != null) {
							iNum = (int) retMap.get(key);
						}
						iNum += jsonObj.getIntValue(key);
						
						retMap.put(key, iNum);
					}
				}
			}
		}
		
		resultMap.put("readTimes", retMap);
		result.setData(resultMap);
		
		return result;
	}
	
	@Override
	public Response getMonthTOPList(String month) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 如果传入的参数是当前月份,则实时统计一次数据
		/** 2020-03-24注释，现在已改成每天统计一次。
		String statistics_period = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		if(month.equals(statistics_period)) {
			logger.debug("********************开始统计月度TOP************************");
			this.executeMonthlyArticlesStatistics();
			this.executeMonthlyUsersStatistics();
		}**/
		
		// 热点文章TOP10列表
		List<Map<String, Object>> hotArticlesList = baseDAO.findListBy(mapperNamespace + ".queryHotArticlesList", month);
		// 分享达人TOP10列表
		List<Map<String, Object>> shareTalentsList = baseDAO.findListBy(mapperNamespace + ".queryShareTalentsList", month);
		// 阅读达人TOP10列表
		List<Map<String, Object>> readTalentsList = baseDAO.findListBy(mapperNamespace + ".queryReadTalentsList", month);
		
		if(hotArticlesList == null) {
			hotArticlesList = new ArrayList<Map<String, Object>>();
		}
		if(shareTalentsList == null) {
			shareTalentsList = new ArrayList<Map<String, Object>>();
		}
		if(readTalentsList == null) {
			readTalentsList = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("hotArticlesList", hotArticlesList);
		resultMap.put("shareTalentsList", shareTalentsList);
		resultMap.put("readTalentsList", readTalentsList);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getMoreHotArticlesList(QlyRhAdminArticleDTO articleDTO) {
		Response result = Response.getDefaulTrueInstance();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		String firstday = DateUtil.getFirstDayOfMonth();
//		String lastday = DateUtil.getLastDayOfMonth();
//		paramMap.put("firstday", firstday);
//		paramMap.put("lastday", lastday);
		
		String month = articleDTO.getMonth();
		if(month == null || month == "") {
			month = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		}
		
        Integer category_id = articleDTO.getCategory_id();
        if(category_id != null && category_id != 0) {
        	paramMap.put("category_id", category_id);
        }
        paramMap.put("month", month);
        paramMap.put("article_title", articleDTO.getArticle_title());
        paramMap.put("read_startNum", articleDTO.getRead_startNum());
        paramMap.put("read_endNum", articleDTO.getRead_endNum());
        
        result = getMoreHotArticlesList(paramMap, articleDTO.getPageQueryVO());
        return result;
	}

	private Response getMoreHotArticlesList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//热点文章总数
        int totalRecord = 0;
        //查询文章总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryMoreHotArticlesListCount", paramMap);
        
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        List<Map<String, Object>> hotArticlesList = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	hotArticlesList = baseDAO.findListByRange(mapperNamespace + ".queryMoreHotArticlesList", paramMap, 
        			page.getStart(), page.getLimit());
        }
		
//      String month = DateUtil.getMonth(new Date()) + "月";
//		resultMap.put("month", month);
		resultMap.put("hotArticlesList", hotArticlesList);
		resultMap.put("page", page);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getMoreShareTalentsList(QlyRhAdminUserDTO userDTO) {
		Response result = Response.getDefaulTrueInstance();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		String firstday = DateUtil.getFirstDayOfMonth();
//		String lastday = DateUtil.getLastDayOfMonth();
//		paramMap.put("firstday", firstday);
//		paramMap.put("lastday", lastday);
		
		String month = userDTO.getMonth();
		if(month == null || month == "") {
			month = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		}
		paramMap.put("month", month);
		paramMap.put("user_name", userDTO.getUser_name());
		paramMap.put("share_startNum", userDTO.getShare_startNum());
        paramMap.put("share_endNum", userDTO.getShare_endNum());
		
		result = getMoreShareTalentsList(paramMap, userDTO.getPageQueryVO());
		return result;
	}

	private Response getMoreShareTalentsList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//查询分享达人总数
        int totalRecord = 0;
        //查询分享达人总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryMoreShareTalentsListCount", paramMap);
        
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        List<Map<String, Object>> shareTalentsList = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	shareTalentsList = baseDAO.findListByRange(mapperNamespace + ".queryMoreShareTalentsList", paramMap, 
        			page.getStart(), page.getLimit());
        }
		
//      String month = DateUtil.getMonth(new Date()) + "月";
//		resultMap.put("month", month);
		resultMap.put("shareTalentsList", shareTalentsList);
		resultMap.put("page", page);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getMoreReadTalentsList(QlyRhAdminUserDTO userDTO) {
		Response result = Response.getDefaulTrueInstance();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
//		String firstday = DateUtil.getFirstDayOfMonth();
//		String lastday = DateUtil.getLastDayOfMonth();
//		paramMap.put("firstday", firstday);
//		paramMap.put("lastday", lastday);
		
		String month = userDTO.getMonth();
		if(month == null || month == "") {
			month = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		}
		paramMap.put("month", month);
		paramMap.put("user_name", userDTO.getUser_name());
		paramMap.put("read_startNum", userDTO.getRead_startNum());
        paramMap.put("read_endNum", userDTO.getRead_endNum());
		
		result = getMoreReadTalentsList(paramMap, userDTO.getPageQueryVO());
		return result;
	}

	private Response getMoreReadTalentsList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//查询阅读达人总数
        int totalRecord = 0;
        //查询阅读达人总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryMoreReadTalentsListCount", paramMap);
        
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        
        List<Map<String, Object>> readTalentsList = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	readTalentsList = baseDAO.findListByRange(mapperNamespace + ".queryMoreReadTalentsList", paramMap, 
        			page.getStart(), page.getLimit());
        }
		
//      String month = DateUtil.getMonth(new Date()) + "月";
//		resultMap.put("month", month);
		resultMap.put("readTalentsList", readTalentsList);
		resultMap.put("page", page);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getUserAnalysisList() {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String todayDate = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
		// 今日新增用户数
		Integer newUsersToday = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_datetime", todayDate);
		newUsersToday = baseDAO.findOne(mapperNamespace + ".queryUsersCount", paramMap);
		resultMap.put("newUsersToday", newUsersToday);
		
		// 累计用户数
		Integer allUsers = 0;
		allUsers = baseDAO.findOne(mapperNamespace + ".queryUsersCount");
		resultMap.put("allUsers", allUsers);
		
		// 今日开通会员数
		Integer newMembersToday = 0;
		String open_date = todayDate;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("open_date", open_date);
		newMembersToday = baseDAO.findOne(mapperNamespace + ".queryUsersCount", map);
		resultMap.put("newMembersToday", newMembersToday);		
		
		// 累计会员数
		Integer allMembers = 0;
		allMembers = baseDAO.findOne(mapperNamespace + ".queryAllMembersCount", todayDate);
		resultMap.put("allMembers", allMembers);
		
		// 今日分享文章数
		Integer todayShareNum = 0;
		todayShareNum = baseDAO.findOne(mapperNamespace + ".getTodayShareCount", todayDate);
		resultMap.put("todayShareNum", todayShareNum);
		
		// 今日阅读次数
		Integer todayReadNum = 0;
		todayReadNum = baseDAO.findOne(mapperNamespace + ".getTodayReadCount", todayDate);
		resultMap.put("todayReadNum", todayReadNum);
		
		// 累计访客数
		Integer allVisitor = 0;
		allVisitor = baseDAO.findOne(mapperNamespace + ".queryVisitorsCount");
		resultMap.put("allVisitor", allVisitor);
		
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getUserAreaDistributionList(QlyRhSysAreaDTO areaVO) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当天日期
		String dateTime = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
		paramMap.put("dateTime", dateTime);
		paramMap.put("areaCode", areaVO.getAreaCode());
		paramMap.put("areaLevel", areaVO.getAreaLevel());
		
		List<Map<String, Object>> citiesList = baseDAO.findListBy(mapperNamespace + ".queryUsersDistributionCount", paramMap);
		if(citiesList == null) {
			citiesList = new ArrayList<Map<String, Object>>();
		}
		// 前端传过来的用户数
		Integer usersCountParam = areaVO.getUsersCount();
		// 前端传过来的会员数
		Integer membersCountParam = areaVO.getMembersCount();
		
		// 首页打开界面时，如果用户没有设置城市区域的，默认放到其他里面 2020-6-18
		if("000000000".equals(areaVO.getAreaCode())) {
			usersCountParam = baseDAO.findOne(mapperNamespace + ".queryUsersCount");
			
			String todayDate = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
			membersCountParam = baseDAO.findOne(mapperNamespace + ".queryAllMembersCount", todayDate);
		}
		
		// 当前级别的用户总数
		int totalUsersCount = 0;
		// 当前级别的会员总数
		int totalMembersCount = 0;
		for(Map<String, Object> map : citiesList) {
			int usersCount = Integer.parseInt(map.get("usersCount").toString());
			int membersCount = Integer.parseInt(map.get("membersCount").toString());
			totalUsersCount += usersCount;
			totalMembersCount += membersCount;
		}
		int otherUsersCount = 0, otherMembersCount = 0;
		if(usersCountParam != null && usersCountParam != 0) {
			// 其他用户数
			otherUsersCount = usersCountParam - totalUsersCount;
		}
		if(membersCountParam != null) {
			// 其他会员数
			otherMembersCount = membersCountParam - totalMembersCount;
		}
		if(otherUsersCount > 0) {
			Map<String, Object> otherMap = new HashMap<String, Object>();
			otherMap.put("area_name", "其他");
			otherMap.put("area_code", "");
			otherMap.put("area_level", 4);
			otherMap.put("usersCount", otherUsersCount);
			otherMap.put("membersCount", otherMembersCount);
			citiesList.add(otherMap);
		}
		resultMap.put("citiesList", citiesList);	
		result.setData(resultMap);
		return result;
	}

	@Override
	public void executeMarketStatistics() {
		try {
			String statisticsDate = DateUtil.formatDate(new Date());
			List<Map<String, Object>> userList = baseDAO.findListBy(mapperNamespace + ".queryMarketUserList");
			if(userList != null && userList.size() > 0) {
				for(Map<String, Object> userMap : userList) {
					int user_id = (int) userMap.get("user_id");
					String openId = (String) userMap.get("openId");
//					logger.debug("***********************开始统计【"+ openId +"】分销情况*******************************");
					java.math.BigDecimal income_balance = userMap.get("income_balance") == null ? null : (java.math.BigDecimal) userMap.get("income_balance");
					double total_income_balance = 0;
					if(income_balance != null) {
						total_income_balance = income_balance.doubleValue();
					}
					
					// 累计收益
        	     	Map<String, Object> incomeMap = new HashMap<String, Object>();
        	     	incomeMap.put("total_type", "1");
        	     	incomeMap.put("userId", user_id);
        	     	double total_income = marketService.getQlyRhUserStreamTotalIncome(incomeMap);
					
        	     	// 累计提现
        	     	incomeMap.put("total_type", "2");
        	     	double total_cashOut = marketService.getQlyRhUserStreamTotalIncome(incomeMap);
        	     	
        	     	// 伙伴数
        	     	int partner_num = 0;
        	     	Map<String, Object> tpMap = new HashMap<String, Object>();
        			tpMap.put("openId", openId);
        			partner_num = userService.getQlyRhUserTotalPartner(tpMap);
        	     	
        			QlyRhMarketStatisticsVO statisticsVO = new QlyRhMarketStatisticsVO();
        			statisticsVO.setStatistics_date(statisticsDate);
    				statisticsVO.setOpenId(openId);
    				statisticsVO.setTotal_income_balance(total_income_balance);
    				statisticsVO.setTotal_income(total_income);
    				statisticsVO.setTotal_cash_out(total_cashOut);
    				statisticsVO.setTotal_partner_num(partner_num);
    				statisticsVO.setModify_datetime(new Date());
    				
    				int result = baseDAO.update(mapperNamespace + ".updateMarketStatistics", statisticsVO);
    				if(result == 0) {
    					baseDAO.add(mapperNamespace + ".insertMarketStatistics", statisticsVO);
    				}
        			
//					logger.debug("***********************结束统计【"+ openId +"】分销情况*******************************");
				}
			}
		} catch(Exception e) {
			logger.error("统计分销情况异常，出错信息：" + e);
		}
		
	}
	
	@Override
	public void executeMonthlyStatistics() {
		try {
			Date dt = new Date();
			Date lastMonthDt = DateUtil.afterNMonth(dt, -1);
			String statisticsPeriod = DateUtil.formatDateByFormat(lastMonthDt, "yyyyMM");
			String monthFirstDay = DateUtil.formatDateByFormat(lastMonthDt, "yyyy-MM") + "-01";
			String monthLastDay = DateUtil.getLastDayOfMonth(lastMonthDt);
			
			generateMonthlyStatistics(statisticsPeriod, monthFirstDay, monthLastDay);
		} catch(Exception e) {
			logger.error("统计月度报表情况异常，出错信息：" + e);
		}
	}
	
	@Override
	public void generateMonthlyStatistics(String statisticsPeriod, String monthFirstDay, String monthLastDay) {
		logger.debug("***********************统计月度报表情况*******************************{},{}", monthFirstDay, monthLastDay);
		// 月度新增用户数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("monthFirstDay", monthFirstDay);
		paramMap.put("monthLastDay", monthLastDay);
		Integer addUserNum = baseDAO.findOne(mapperNamespace + ".queryMonthAddUsersCount", paramMap);
		
		// 月度新增会员数
		Integer addMemberNum = baseDAO.findOne(mapperNamespace + ".queryMonthAddMembersCount", paramMap);
		
		// 月度阅读总次数
		Integer totalReadNum = baseDAO.findOne(mapperNamespace + ".queryMonthTotalReadNum", paramMap);
		
		// 月度分享文章数
		Integer totalShareNum = baseDAO.findOne(mapperNamespace + ".queryMonthTotalShareNum", paramMap);
		
		// 月度阅读财税早报文章总次数
		Integer totalMpReadNum = baseDAO.findOne(mapperNamespace + ".queryMonthTotalMpReadNum", paramMap);
		
		// 月度分享财税早报文章数
		Integer totalMpShareNum = baseDAO.findOne(mapperNamespace + ".queryMonthTotalMpShareNum", paramMap);
		
		// 月度购买白银会员次数
		paramMap.put("orderType", 1);
		Integer buyByhyNum = baseDAO.findOne(mapperNamespace + ".queryMonthBuyHyNum", paramMap);
		
		// 月度购买黄金会员次数
		paramMap.put("orderType", 2);
		Integer buyHjhyNum = baseDAO.findOne(mapperNamespace + ".queryMonthBuyHyNum", paramMap);
		
		// 月度购买钻石会员次数
		paramMap.put("orderType", 3);
		Integer buyZshyNum = baseDAO.findOne(mapperNamespace + ".queryMonthBuyHyNum", paramMap);
		
		// 月度最后一天时间点上有效会员数
		Integer memberValidNum = baseDAO.findOne(mapperNamespace + ".queryMonthMemberValidNum", paramMap);
					
		// 月度最后一天时间点上会员过期数
		Integer memberExpireNum = baseDAO.findOne(mapperNamespace + ".queryMonthMemberExpireNum", paramMap);
		
		QlyRhMonthStatisticsVO statisticsVO = new QlyRhMonthStatisticsVO();
		statisticsVO.setStatistics_period(statisticsPeriod);
		statisticsVO.setAdd_user_num(addUserNum);
		statisticsVO.setAdd_member_num(addMemberNum);
		statisticsVO.setTotal_read_num(totalReadNum);
		statisticsVO.setTotal_share_num(totalShareNum);
		statisticsVO.setTotal_mp_read_num(totalMpReadNum);
		statisticsVO.setTotal_mp_share_num(totalMpShareNum);
		statisticsVO.setBuy_byhy_num(buyByhyNum);
		statisticsVO.setBuy_hjhy_num(buyHjhyNum);
		statisticsVO.setBuy_zshy_num(buyZshyNum);
		statisticsVO.setMember_valid_num(memberValidNum);
		statisticsVO.setMember_expire_num(memberExpireNum);
		statisticsVO.setModify_datetime(new Date());
		
		int result = baseDAO.update(mapperNamespace + ".updateMonthStatistics", statisticsVO);
		if(result == 0) {
			baseDAO.add(mapperNamespace + ".insertMonthStatistics", statisticsVO);
		}
	}
	
	@Override
	public void syncMonthlyStatistics(String startPeriod, String endPeriod) {
		int iStart = Integer.parseInt(startPeriod);
		int iEnd = Integer.parseInt(endPeriod);
		for(int i = iStart; i<= iEnd; i++) {
			String period = String.valueOf(i);
			Date monthDt = DateUtil.parseDateByFormat(period, "yyyyMM");
			String monthFirstDay = DateUtil.formatDateByFormat(monthDt, "yyyy-MM") + "-01";
			String monthLastDay = DateUtil.getLastDayOfMonth(monthDt);
			
			generateMonthlyStatistics(period, monthFirstDay, monthLastDay);
		}
	}
	
	@Override
	public Response queryMonthlyStatisticsList(String year) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		NumberFormat nf = new DecimalFormat("00");
		for(int i = 1; i <= 12; i++) {
			String month = nf.format(i);
			String statistics_period = year + month;
			
			
			Map<String, Object> m = new HashMap<String, Object>();
			QlyRhMonthStatisticsVO statisticsVO = baseDAO.findOne(mapperNamespace + ".getMonthStatistics", statistics_period);
			if(statisticsVO != null) {
				m.put("add_user_num", statisticsVO.getAdd_user_num());
				m.put("add_member_num", statisticsVO.getAdd_member_num());
				m.put("total_read_num", statisticsVO.getTotal_read_num());
				m.put("total_share_num", statisticsVO.getTotal_share_num());
				m.put("total_mp_read_num", statisticsVO.getTotal_mp_read_num());
				m.put("total_mp_share_num", statisticsVO.getTotal_mp_share_num());
				m.put("buy_byhy_num", statisticsVO.getBuy_byhy_num());
				m.put("buy_hjhy_num", statisticsVO.getBuy_hjhy_num());
				m.put("buy_zshy_num", statisticsVO.getBuy_zshy_num());
				m.put("member_valid_num", statisticsVO.getMember_valid_num());
				m.put("member_expire_num", statisticsVO.getMember_expire_num());
				
			}
			resultMap.put(month, m);
		}
		
		result.setData(resultMap);
		result.setMessage("查询月度报表情况列表成功");
		
		return result;
	}
}
