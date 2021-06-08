package com.cd.qlyhk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhMorningPaperArticleDTO;
import com.cd.qlyhk.dto.QlyRhReptileArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMorningPaperService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.vo.QlyRhMorningPaperArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IMorningPaperService.BEAN_ID)
public class MorningPaperServiceImpl implements IMorningPaperService {
	private static final Logger logger = LoggerFactory.getLogger(MorningPaperServiceImpl.class);
	
	private final String mapperNamespace = MorningPaperServiceImpl.class.getName();
	
	@Value("${frontend.url}")
	public String frontendUrl;
	
	@Autowired
	private IBaseDAO baseDAO;

	@Override
	public Response getMorningpaperArticles(String mp_date) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> articlesList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> morningPaperArticlesList = queryMorningPaperArticleList(mp_date);
		if(morningPaperArticlesList != null && morningPaperArticlesList.size() > 0) {
			for(Map<String, Object> map : morningPaperArticlesList) {
				Map<String, Object> resMap = new HashMap<String, Object>();
				int id = (int) map.get("id");
				String article_uuid = (String) map.get("article_uuid");
				String article_title  = (String) map.get("article_title");
				String article_url = (String) map.get("article_url");
				String source = (String) map.get("source");
				int order_index = (int) map.get("order_index");
				Integer total_read_num = (Integer) map.get("total_read_num");
				Integer total_share_num = (Integer) map.get("total_share_num");
				if(total_read_num == null) {
					total_read_num = 0;
				}
				if(total_share_num == null) {
					total_share_num = 0;
				}
				resMap.put("id", id);
				resMap.put("article_uuid", article_uuid);
				resMap.put("article_title", article_title);
				resMap.put("article_url", article_url);
				resMap.put("source", source);
				resMap.put("order_index", order_index);
				resMap.put("total_read_num", total_read_num);
				resMap.put("total_share_num", total_share_num);
				
				articlesList.add(resMap);
			}
		} else {
			morningPaperArticlesList = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("articlesList", articlesList);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response getArticlesList(QlyRhReptileArticleDTO reptileArticleDTO) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer category_id = reptileArticleDTO.getCategory_id();
        if(category_id != null && category_id != 0) {
        	paramMap.put("category_id", category_id);
        }
        paramMap.put("article_title", reptileArticleDTO.getArticle_title());
        paramMap.put("collect_startDate", reptileArticleDTO.getCollect_startDate());
        paramMap.put("collect_endDate", reptileArticleDTO.getCollect_endDate());
        paramMap.put("source", reptileArticleDTO.getSource());
        
        //文章总数
        int totalRecord = 0;
        //查询文章总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".getArticlesListCount", paramMap);
        
        //分页对象
        PageQueryVO pageQueryVO = reptileArticleDTO.getPageQueryVO();
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        //文章选择列表
        List<Map<String, Object>> articlesList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".getArticlesList", paramMap, page.getStart(), page.getLimit());
        	if(list != null && list.size() > 0) {
        		for(Map<String, Object> map : list) {
    				Map<String, Object> resMap = new HashMap<String, Object>();
    				int article_id = (int) map.get("id");
    				String uuid = (String) map.get("uuid");
    				String article_title = (String) map.get("article_title");
    				String article_url = (String) map.get("article_url");
    				String source = (String) map.get("source");
    				String modify_user = (String) map.get("modify_user");
    				String collect_date = (String) map.get("collect_date");
    				String category_name = baseDAO.findOne(ArticleServiceImpl.class.getName() + ".getArticleCategoryName", uuid);
    				
    				resMap.put("article_id", article_id);
    				resMap.put("uuid", uuid);
    				resMap.put("article_title", article_title);
    				resMap.put("article_url", article_url);
    				resMap.put("source", source);
    				resMap.put("modify_user", modify_user);
    				resMap.put("collect_date", collect_date);
    				resMap.put("category_name", category_name);
    				
    				articlesList.add(resMap);
    			}
        	} else {
    			list = new ArrayList<Map<String, Object>>();
    		}
        }
		resultMap.put("articlesList", articlesList);
		resultMap.put("page", page);
		result.setData(resultMap);
		return result;
	}

	@Override
	public Response addMorningpaperArticles(QlyRhMorningPaperArticleDTO morningPaperArticleDTO, String createUser) {
		Response result = Response.getDefaulTrueInstance();
		String mp_date = morningPaperArticleDTO.getMp_date();
		if(mp_date == null || mp_date == "") {
			mp_date = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
		}
		// 判断是否已经超过5篇文章
		int c = baseDAO.findOne(mapperNamespace + ".getMorningpaperArticleCount", mp_date);
		List<QlyRhMorningPaperArticleVO> articlesList = morningPaperArticleDTO.getArticlesList();
		c = c + articlesList.size();
		if(c > 5) {
			result.setSuccess(false);
            result.setCode("1");
            result.setMessage("财税早报每日只采用五篇");
            return result;
		}
		
		Integer order_index = getMaxOrderIndex(mp_date);
		if(order_index == null) {
			order_index = 0;
		}
		List<Map<String, Object>> addarticlesList = new ArrayList<Map<String, Object>>();
		
		// 重复的文章标题
		List dupArticlesTList = new ArrayList();
		for(QlyRhMorningPaperArticleVO morningPaperArticleVO : articlesList) {
			order_index++;
			Map<String, Object> addParam = new HashMap<String, Object>();
			addParam.put("article_uuid", morningPaperArticleVO.getArticle_uuid());
			addParam.put("article_title", morningPaperArticleVO.getArticle_title());
			addParam.put("article_url", morningPaperArticleVO.getArticle_url());
			addParam.put("source", morningPaperArticleVO.getSource());
			addParam.put("mp_date", mp_date);
			addParam.put("order_index", order_index);
			addParam.put("create_user", createUser);
			addarticlesList.add(addParam);
			// 校验财税早报清单是否存在新添加的文章
			String duplicateArticleTitle = checkArticleUuidUnique(addParam);
			if(duplicateArticleTitle != "" && duplicateArticleTitle != null) {
				dupArticlesTList.add(duplicateArticleTitle);
			}
		}
		if(dupArticlesTList != null && dupArticlesTList.size() > 0) {
			Map<String, Object> resultParam = new HashMap<String, Object>();
			resultParam.put("dupArticlesTList", dupArticlesTList);
			result.setData(resultParam);
		} else {
			baseDAO.add(mapperNamespace + ".addMorningpaperArticles", addarticlesList);
			result.setMessage("保存添加文章列表成功");
		}
		return result;
	}

	private String checkArticleUuidUnique(Map<String, Object> addParam) {
		String duplicateArticleTitle = baseDAO.findOne(mapperNamespace + ".checkArticleUuidUnique", addParam);
		return duplicateArticleTitle;
	}

	private Integer getMaxOrderIndex(String mp_date) {
		logger.info("***************************查询当前最大的排序序号********************************");
		Integer order_index = baseDAO.findOne(mapperNamespace + ".getMaxOrderIndex", mp_date);
		logger.info("当前最大的排序序号为:{}", order_index);
		return order_index;
	}

	@Override
	public Response updateMorningpaperArticles(QlyRhMorningPaperArticleDTO morningPaperArticleDTO, String modifyUser) {
		Response result = Response.getDefaulTrueInstance();
		
		List<QlyRhMorningPaperArticleVO> articlesList = morningPaperArticleDTO.getArticlesList();
		for(QlyRhMorningPaperArticleVO morningPaperArticleVO : articlesList) {
			Map<String, Object> addParam = new HashMap<String, Object>();
			addParam.put("id", morningPaperArticleVO.getId());
			addParam.put("order_index", morningPaperArticleVO.getOrder_index());
			addParam.put("modify_user", modifyUser);
			addParam.put("modify_datetime", new Date());
			
			baseDAO.update(mapperNamespace + ".updateMorningpaperArticles", addParam);
		}
		return result;
	}

	@Override
	public Response delMorningpaperArticles(int id) {
		Response result = Response.getDefaulTrueInstance();
		baseDAO.delete(mapperNamespace + ".delMorningpaperArticles", id);
		return result;
	}

	@Override
	public List<Map<String, Object>> queryMorningPaperArticleList(String mp_date) {
		List<Map<String, Object>> morningPaperArticlesList = baseDAO.findListBy(mapperNamespace + ".queryMorningPaperArticleList", mp_date);
		return morningPaperArticlesList;
	}
	
	@Override
	public void addMorningpaperArticle(QlyRhReptileArticleVO articleVO, String mpDate, String userName) {
		String mp_date = DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd");
		if(StringUtils.isNotBlank(mpDate)) {
			mp_date = mpDate;
		}
		Integer order_index = getMaxOrderIndex(mp_date);
		if(order_index == null) {
			order_index = 1;
		} else {
			order_index++;
		}
		QlyRhMorningPaperArticleVO morningPaperArticleVO = new QlyRhMorningPaperArticleVO();
		morningPaperArticleVO.setArticle_uuid(articleVO.getUuid());
		morningPaperArticleVO.setArticle_title(articleVO.getArticle_title());
		morningPaperArticleVO.setArticle_url(articleVO.getArticle_url());
		morningPaperArticleVO.setSource(articleVO.getSource());
		morningPaperArticleVO.setOrder_index(order_index);
		morningPaperArticleVO.setMp_date(mp_date);
		morningPaperArticleVO.setCreate_user(userName);
		baseDAO.add(mapperNamespace + ".addMorningpaperArticle", morningPaperArticleVO);
	}
	
	@Override
	public int getMorningPaperArticleCount(String mp_date) {
		return baseDAO.findOne(mapperNamespace + ".queryMorningPaperArticleCount", mp_date);
	}
	
	@Override
	public String getPushMorningPaperArticle(String openId) {
		Date dt = new Date();
		String mp_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		
		List<Map<String, Object>> articlesList = this.queryMorningPaperArticleList(mp_date);
        
		String content = "今日财税头条\n";
		if(articlesList != null && articlesList.size() > 0) {
			for(Map<String, Object> map : articlesList) {
				String article_uuid = (String) map.get("article_uuid");
    			String article_title = (String) map.get("article_title");
    			String articleId = UUID.randomUUID().toString();
				
				String localUrl = frontendUrl + "#/contents?rh_articleId=" + article_uuid + "&articleId=" + articleId + "&userId=" + openId + "&getInto=listGetInto";
				content += "<a href='"+ localUrl +"'>✬" + article_title + "</a>\n\n";
			}
		}
		content += "👇👇👇 更多点我【获客好文】\n";
		logger.info("********************推送今日财税头条*********************：{}", content);
		
		return content;
	}
}
