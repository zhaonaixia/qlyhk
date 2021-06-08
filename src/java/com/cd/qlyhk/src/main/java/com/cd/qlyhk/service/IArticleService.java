package com.cd.qlyhk.service;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhArticleUselogVO;
import com.cd.qlyhk.vo.QlyRhCompanyArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleDetailVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileQueueVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.ReqArticleShareReadDTO;
import com.cd.qlyhk.vo.ReqReptileArticleDetailDTO;
import com.cd.qlyhk.vo.ReqReptileArticleInfoDTO;

public interface IArticleService {
	
	final String BEAN_ID = "articleService";

	/**
	 * 查询获客文章列表
	 * @param articleDTO
	 * @return
	 */
	Response getArticlesList(QlyRhArticleDTO articleDTO);
	/**
	 * 文章阅读情况
	 * @param openId
	 * @return
	 */
	Response queryArticleReadCond(String openId);
	/**
	 * 文章阅读详情
	 * @param shareId
	 * @return
	 */
	Response queryArticleReadCondDetails(String shareId);
	
	/**
	 * 查询访客情况
	 * @param openId
	 * @return
	 */
	Response queryRecordCondition(String openId);
	
	/**
	 * 查询访客详情
	 * @param openId（当前用户的openId）
	 * @param visitorId（访客的openId）
	 * @return
	 */
	Response queryRecordConditionDetails(String openId, String visitorId);
	
	/**
	 * 查询访客访问某篇文章详情
	 * @param openId
	 * @param shareId
	 * @return
	 */
	Response queryRecordConditionArticleDetails(String openId, String shareId);
	
	/**
	 * 分享文章数（累计）
	 * @param openId
	 * @return
	 */
	int getTotalShareNum(String openId);
	
	/**
	 * 今日阅读数
	 * @param openId
	 * @return
	 */
	int getCountRecordToday(String openId);
	
	/**
	 * 总阅读数
	 * @param openId
	 * @return
	 */
	int getTotalCountRecord(String openId);
	
	/**
	 * 文章详情
	 * @param articleId
	 * @return
	 */
	Response getArticleInfo(String articleId);
	
	/**
	 * 文章总阅读数
	 * @param openId
	 * @return
	 */
	int getArticleTotalRecord(String articleId);
	
	/**
	 * 文章总分享数
	 * @param openId
	 * @return
	 */
	int getArticleTotalShareNum(String articleId);
	/**
	 * 新增文章
	 * @param articleInfoDTO
	 * @return
	 */
	Response addArticle(ReqReptileArticleInfoDTO articleInfoDTO);
	
	/**
	 * 获取推送文章
	 * @return
	 */
	String getPushArticle(String openId);
	
	/**
	 * 根据文章URL查询文章队列
	 * @param contentUrl
	 * @return
	 */
	QlyRhReptileQueueVO getQlyRhReptileQueueVO(String articleUrl);
	
	/**
	 * 新增查询文章队列
	 * @param vo
	 */
	void insertQlyRhReptileQueueVO(QlyRhReptileQueueVO vo);
	
	/**
	 * 更新文章队列标识
	 * @param vo
	 */
	void updateQlyRhReptileQueueVO(QlyRhReptileQueueVO vo);
	
	/**
	 * 查询文章队列
	 * @return
	 */
	List<QlyRhReptileQueueVO> queryFixedReptileQueues();
	
	/**
	 * 新增文章
	 * @param vo
	 */
	void insertQlyRhReptileArticleVO(QlyRhReptileArticleVO vo);
	
	/**
	 * 新增文章内容
	 * @param vo
	 */
	void insertQlyRhReptileArticleDetailVO(QlyRhReptileArticleDetailVO vo);
	
	
	/**
	 * 新增文章（公司）
	 * @param vo
	 */
	void insertQlyRhCompanyArticleVO(QlyRhCompanyArticleVO vo);
	
	
	/**
	 * 获取文章内容
	 * @param articleId
	 * @return
	 */
	Response getArticleDetailInfo(String articleId);
	
	/**
	 * 根据文章URL查询文章
	 * @param contentUrl
	 * @return
	 */
	QlyRhReptileArticleVO getQlyRhReptileArticleVO(String articleUrl);
	
	/**
	 * 获取我的文章
	 * @param openId
	 * @return
	 */
	Response queryMyArticle(String openId, String source);
	
	/**
	 * 获取我的文章（编辑）
	 * @param openId
	 * @return
	 */
	Response queryMyEditArticle(String openId);
	
	/**
	 * 批量新增文章与归属类别关系
	 * @param vo
	 */
	void batchInsertArticleAndCategory(List<Map<String, Object>> list);
	
	/**
	 * 查询文章列表（文章管理）
	 * @param articleDTO
	 * @return
	 */
	Response queryAdminArticlesList(QlyRhAdminArticleDTO articleDTO);
	
	/**
	 * 查询文章列表（文章管理）
	 * @param paramMap
	 * @return
	 */
	Response queryAdminArticlesList(Map<String, Object> paramMap, PageQueryVO pageQueryVO);
	
	/**
	 * 文章详情（文章管理）
	 * @param uuid
	 * @return
	 */
	Response queryAdminArticleInfo(String uuid);
	
	/**
	 * 更新文章详情表
	 * @param vo
	 */
	void updateReptileArticle(QlyRhReptileArticleVO vo);
	
	/**
	 * 查询文章所属类别列表
	 * @param uuid
	 * @return
	 */
	Response queryArticleCategoryList(String uuid);
	
	/**
	 * 修改文章所属类别，多个类别ID以,分割
	 * @param uuid
	 * @param categoryId
	 */
	void updateReptileArticleCategory(String uuid, String categoryId, String mpflag, String userName);
	
	/**
	 * 修改文章内容
	 * @param articleDetailDTO
	 */
	void updateReptileArticleDetail(ReqReptileArticleDetailDTO articleDetailDTO);
	
	/**
	 * 查询文章分享及阅读情况列表
	 * @param articleDTO
	 * @return
	 */
	Response queryAdminArticlesShareAndReadList(QlyRhAdminArticleDTO articleDTO);
	
	/**
	 * 查询文章分享及阅读情况列表
	 * @param paramMap
	 * @return
	 */
	Response queryAdminArticlesShareAndReadList(Map<String, Object> paramMap, PageQueryVO pageQueryVO);
	
	/**
	 * 查询分享过文章的用户情况列表
	 * @param shareReadDTO
	 * @return
	 */
	Response queryAdminArticlesShareUserList(ReqArticleShareReadDTO shareReadDTO);
	
	/**
	 * 查询阅读过文章的用户情况列表
	 * @param shareReadDTO
	 * @return
	 */
	Response queryAdminArticlesReadUserList(ReqArticleShareReadDTO shareReadDTO);
	
	/**
	 * 查询用户所有分享的文章列表
	 * @param openId
	 * @return
	 */
	List<QlyRhShareVO> queryShareArticles(String openId);
	
	/**
	 * 总访客数
	 * @param openId
	 * @return
	 */
	int getTotalRecordConditionCount(String openId);
	
	/**
	 * 修改文章信息。
	 * @param articleInfoDTO
	 * @return
	 */
	Response updateArticleInfo(ReqReptileArticleInfoDTO articleInfoDTO);
	
	/**
	 * 新增文章采用记录
	 * @param vo
	 */
	void insertArticleUselog(QlyRhArticleUselogVO vo);
	
	/**
	 * 获取推送订阅文章(记录数)
	 * @return
	 */
	int getPushSubscribeArticleCount(int userId);
	
	/**
	 * 获取推送订阅文章
	 * @return
	 */
	String getPushSubscribeArticle(String openId);
	
	/**
	 * 修改文章置顶标识
	 * @param uuid
	 * @param topFlag
	 * @param categoryId
	 */
	void updateArticleTop(String uuid, String topFlag, String categoryId, String userName);
	
	/**
	 * 保存移动端编辑的文章
	 * @param articleDetailDTO
	 * @return
	 */
	Response saveEditorArticle(ReqReptileArticleDetailDTO articleDetailDTO);
	
	/**
	 * 保存移动端修改的文章标题、封面图片等信息
	 * @param articleDetailDTO
	 * @return
	 
	Response saveEditorArticleInfo(ReqReptileArticleInfoDTO articleInfoDTO);*/
	
	/**
	 * 根据阅读时间段统计阅读人数
	 * @param paramMap
	 * @return
	 */
	int getTotalReadersByTimeQuantum(Map<String, Object> paramMap);
	
	/**
	 * 删除文章
	 * @param articleId
	 * @return
	 */
	Response delArticle(String uuid);
	
	/**
	 * 分享文章数（上周累计）
	 * @param paramMap
	 * @return
	 */
	int getTotalWeekShareNum(Map<String, Object> paramMap);
}
