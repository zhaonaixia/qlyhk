package com.cd.qlyhk.service;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.dto.QlyRhMorningPaperArticleDTO;
import com.cd.qlyhk.dto.QlyRhReptileArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;

public interface IMorningPaperService {
	final String BEAN_ID = "morningPaperService";

	/**
	 * 获取财税早报清单
	 * @param mp_date 
	 * @return
	 */
	Response getMorningpaperArticles(String mp_date);
	
	/**
	 * 获取文章选择列表
	 * @param reptileArticleDTO
	 * @return
	 */
	Response getArticlesList(QlyRhReptileArticleDTO reptileArticleDTO);

	/**
	 * 保存添加文章列表
	 * @param morningPaperArticleDTO
	 * @param createUser 
	 * @return
	 */
	Response addMorningpaperArticles(QlyRhMorningPaperArticleDTO morningPaperArticleDTO, String createUser);

	/**
	 * 保存财税早报文章上移、下移
	 * @param morningPaperArticleDTO
	 * @param modifyUser
	 * @return
	 */
	Response updateMorningpaperArticles(QlyRhMorningPaperArticleDTO morningPaperArticleDTO, String modifyUser);

	/**
	 * 删除早报文章
	 * @param id
	 * @return
	 */
	Response delMorningpaperArticles(int id);

	/**
	 *  获取财税早报文章列表
	 * @param mp_date
	 * @return
	 */
	List<Map<String, Object>> queryMorningPaperArticleList(String mp_date);

	/**
	 * 将文章新增到财税早报清单列表
	 * @param articleVO
	 * @param mpDate
	 * @param userName
	 */
	void addMorningpaperArticle(QlyRhReptileArticleVO articleVO, String mpDate, String userName);
	
	/**
	 * 获取今日财税早报的总数
	 * @param mp_date
	 * @return
	 */
	int getMorningPaperArticleCount(String mp_date);
	
	/**
	 * 获取今日财税早报
	 * @return
	 */
	String getPushMorningPaperArticle(String openId);
}
