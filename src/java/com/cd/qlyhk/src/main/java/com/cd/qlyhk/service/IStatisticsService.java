package com.cd.qlyhk.service;

import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.dto.QlyRhSysAreaDTO;
import com.cd.qlyhk.rest.Response;

/**
 * 统计数据服务接口
 * @author sailor
 *
 */
public interface IStatisticsService {

	final String BEAN_ID = "statisticsService";
	
	/**
	 * 执行阅读情况统计
	 */
	void executeReadArticleStatistics();
	
	/**
	 * 执行访客情况统计
	 */
	void executeRecordStatistics();
	
	/**
	 * 执行文章分享及阅读情况统计
	 */
	void executeArticleShareAndReadStatistics();
	
	/**
	 * 执行用户分享及阅读情况统计
	 */
	void executeUserShareAndReadStatistics();
	
	/**
	 * 执行月度分享时段情况统计
	 */
	void executeMonthlyShareStatistics();
	
	/**
	 * 执行月度阅读时段情况统计
	 */
	void executeMonthlyReadStatistics();
	
	
	/**
	 * 执行月度文章情况统计
	 */
	void executeMonthlyArticlesStatistics();
	
	/**
	 * 执行月度用户情况统计
	 */
	void executeMonthlyUsersStatistics();
	
	
	/**
	 * 查询月度分享时段分析列表
	 * @param yearPeriod
	 * @return
	 */
	Response queryMonthlyShareList(int yearPeriodStart, int yearPeriodEnd);
	
	/**
	 * 查询月度阅读时段分析列表
	 * @param yearPeriod
	 * @return
	 */
	Response queryMonthlyReadList(int yearPeriodStart, int yearPeriodEnd);

	/**
	 * 获取月度TOP10列表
	 * @return
	 */
	Response getMonthTOPList(String month);

	/**
	 * 获取更多热点文章列表
	 * @param adminArticleDTO 
	 * @return
	 */
	Response getMoreHotArticlesList(QlyRhAdminArticleDTO articleDTO);

	/**
	 * 获取更多分享达人列表
	 * @param userDTO
	 * @return
	 */
	Response getMoreShareTalentsList(QlyRhAdminUserDTO userDTO);

	/**
	 * 获取更多阅读达人列表
	 * @param userDTO
	 * @return
	 */
	Response getMoreReadTalentsList(QlyRhAdminUserDTO userDTO);

	/**
	 * 获取用户分析列表
	 * @return
	 */
	Response getUserAnalysisList();

	/**
	 * 获取注册用户城市分布列表
	 * @param areaVO
	 * @return
	 */
	Response getUserAreaDistributionList(QlyRhSysAreaDTO areaVO);
	
	/**
	 * 执行分销情况统计
	 */
	void executeMarketStatistics();
	
	/**
	 * 执行月度报表情况统计
	 */
	void executeMonthlyStatistics();
	
	/**
	 * 生成月度报表情况统计
	 */
	void generateMonthlyStatistics(String statisticsPeriod, String monthFirstDay, String monthLastDay);
	
	void syncMonthlyStatistics(String startPeriod, String endPeriod);
	
	/**
	 *查询月度报表情况统计列表
	 * @param areaVO
	 * @return
	 */
	Response queryMonthlyStatisticsList(String year);
}
