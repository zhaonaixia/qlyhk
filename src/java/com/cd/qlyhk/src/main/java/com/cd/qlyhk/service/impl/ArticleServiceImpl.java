package com.cd.qlyhk.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cd.qlyhk.api.prov.service.IOuterService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.common.vo.Page;
import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.constants.ArticleConstants;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IMorningPaperService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IShareService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.vo.QlyRhArticleUselogVO;
import com.cd.qlyhk.vo.QlyRhArticleVO;
import com.cd.qlyhk.vo.QlyRhCompanyArticleVO;
import com.cd.qlyhk.vo.QlyRhCompanyVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleDetailVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhReptileQueueVO;
import com.cd.qlyhk.vo.QlyRhShareRecordVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhSysCategoryVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqArticleShareReadDTO;
import com.cd.qlyhk.vo.ReqReptileArticleDetailDTO;
import com.cd.qlyhk.vo.ReqReptileArticleInfoDTO;
import com.cd.qlyhk.vo.RespArticleReadCondDetailsVO;
import com.cd.qlyhk.vo.RespArticleReadCondVO;
import com.cd.qlyhk.vo.RespRecordConditionDetailsVO;
import com.cd.qlyhk.vo.RespRecordConditionVO;
import com.cd.qlyhk.vo.RespRecordReadArticleDetailsVO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(IArticleService.BEAN_ID)
public class ArticleServiceImpl implements IArticleService{
	
private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
	
	private final String mapperNamespace = ArticleServiceImpl.class.getName();
	
	@Value("${frontend.url}")
	public String frontendUrl;
	
	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private IMorningPaperService morningPaperService;
	
	@Autowired
	private IPubCommonService pubCommonService;
	
	@Autowired
	private IShareService shareService;
	
	@Autowired
	private IOuterService outerService;
	
	@Autowired
	private IMessageCenterService messageCenterService;
	
	@Override
	public Response getArticlesList(QlyRhArticleDTO articleDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer category_id = articleDTO.getCategory_id();
        String categoryType = "";
        if(category_id != null && category_id != 0) {
        	paramMap.put("category_id", category_id);
        	
        	QlyRhSysCategoryVO categoryVO = pubCommonService.getQlyRhSysCategoryVO(category_id);
        	if(categoryVO != null) {
        		categoryType = categoryVO.getType();
        	}
        }
//        paramMap.put("ispublic", "1");
        paramMap.put("audit_status", "02");
        paramMap.put("article_title", articleDTO.getArticle_title());
        //文章总数
        int totalRecord = 0;
        //分页对象
        PageQueryVO pageQueryVO = articleDTO.getPageQueryVO();
        Page page = null;
        //获客文章列表
        List<Map<String, Object>> articlesList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        if("02".equals(categoryType)) {// 02类型是品牌,因品牌是个抽象的词,需要关联具体用户所属公司或公司来展示文章。
        	String openId = articleDTO.getOpenId();
        	QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
//        	QlyRhCompanyVO companyVO = baseDAO.findOne(CompanyServiceImpl.class.getName() + ".getQlyRhCompanyByName", userVO.getCompany());
//        	paramMap.put("company_id", companyVO.getCompany_id());
        	Map<String, Object> companyMap = outerService.getCompanyInfo(userVO.getUnionid());
    		if(!companyMap.isEmpty()) {
    			paramMap.put("company_id", companyMap.get("company_id"));
    			
    			//查询文章总数
                totalRecord = baseDAO.findOne(mapperNamespace + ".queryCompanyArticlesListCount", paramMap);
                page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
                if (totalRecord > 0) {
                	list = baseDAO.findListByRange(mapperNamespace + ".queryCompanyArticlesList", paramMap, 
                			page.getStart(), page.getLimit());
                }
    		}
        	
        } else {
        	//查询文章总数
            totalRecord = baseDAO.findOne(mapperNamespace + ".queryArticlesListCount", paramMap);
            page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
            if (totalRecord > 0) {
            	list = baseDAO.findListByRange(mapperNamespace + ".queryArticlesList", paramMap, 
            			page.getStart(), page.getLimit());
            }
        }

        if(list != null && list.size() > 0) {
    		for(Map<String, Object> map : list) {
    			Map<String, Object> resMap = new HashMap<String, Object>();
    			
    			String articleId = (String) map.get("uuid"); 
    			String article_title = (String) map.get("article_title");
//				String article_url = (String) map.get("article_url");
				String pic_url = (String) map.get("pic_url");
				String description = (String) map.get("description");
				
				//总阅读人数
		        int total_reader = 0;
				total_reader = getArticleTotalRecord(articleId);
				//总分享次数
		        int total_share = 0;
				total_share = getArticleTotalShareNum(articleId);
				
				resMap.put("uuid", articleId);
				resMap.put("article_title", article_title);
//				resMap.put("article_url", article_url);
				resMap.put("pic_url", pic_url);
				resMap.put("description", description);
				resMap.put("total_reader", total_reader);
				resMap.put("total_share", total_share);
				
				articlesList.add(resMap);
    		}
        }
        
        resultMap.put("articlesList", articlesList);
        resultMap.put("page", page);
        resp.setData(resultMap);
        return resp;
	}
	
	@Override
	public int getTotalShareNum(String openId) {
		int totalShareNum = baseDAO.findOne(mapperNamespace + ".getTotalShareNum", openId);
		return totalShareNum;
	}

	@Override
	public int getCountRecordToday(String openId) {
		Date dt = new Date();
		String dateTime = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("openId", openId);
		paramMap.put("dateTime", dateTime);
		
		int totalReaderToday = baseDAO.findOne(mapperNamespace + ".getCountRecordToday", paramMap);
		return totalReaderToday;
	}
	
	@Override
	public int getTotalCountRecord(String openId) {
		int totalReader = baseDAO.findOne(mapperNamespace + ".getTotalCountRecord", openId);
		return totalReader;
	}
	
	@Override
	public Response queryArticleReadCond(String openId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		
		try {
			Date dt = new Date();
			Date startDt = DateUtil.afterNDay(dt, -6);
			Date endDt = DateUtil.afterNDay(dt, 1);
			String startDate = DateUtil.formatDateByFormat(startDt, "yyyy-MM-dd");
			String endDate = DateUtil.formatDateByFormat(endDt, "yyyy-MM-dd");
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("openId", openId);
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			
			int totalReaderToday = getCountRecordToday(openId);
			int totalReaderSevenDay = baseDAO.findOne(mapperNamespace + ".getCountRecordSevenDay", paramMap);
			int totalReader = getTotalCountRecord(openId);
			
			List<RespArticleReadCondVO> dataList = new ArrayList<RespArticleReadCondVO>();
			List<QlyRhShareVO> shareList = queryShareArticles(openId);
			if(shareList != null && shareList.size() > 0) {
				for(QlyRhShareVO shareVO : shareList) {
					RespArticleReadCondVO readCondVO = new RespArticleReadCondVO();
					readCondVO.setSharer(shareVO.getSharer());
					readCondVO.setShare_uuid(shareVO.getUuid());
					readCondVO.setArticle_uuid(shareVO.getArticle_uuid());
					readCondVO.setArticle_title(shareVO.getArticle_title());
					
					String share_uuid = shareVO.getUuid();
					int total_num = 0, total_readers = 0, share_num = 0, share_readers = 0;
					// 2020-6-10注释
//					total_num = baseDAO.findOne(mapperNamespace + ".getTotalNumByUUID", share_uuid);
					total_readers = baseDAO.findOne(mapperNamespace + ".getTotalReadersByUUID", share_uuid);
					
//					Map<String, String> paramMap = new HashMap<String, String>();
//					paramMap.put("articleTitle", shareVO.getArticle_title());
//					paramMap.put("source", openId);
					
					// 2020-6-10注释
//					share_num = baseDAO.findOne(mapperNamespace + ".getShareNum", share_uuid);
					share_readers = baseDAO.findOne(mapperNamespace + ".getShareReaders", share_uuid);
					
					readCondVO.setTotal_num(total_num);
					readCondVO.setTotal_readers(total_readers);
					readCondVO.setShare_num(share_num);
					readCondVO.setShare_readers(share_readers);
					dataList.add(readCondVO);
				}
			}
			
			retMap.put("totalReaderToday", totalReaderToday);
			retMap.put("totalReaderSevenDay", totalReaderSevenDay);
			retMap.put("totalReader", totalReader);
			retMap.put("shareArticleList", dataList);
			
			result.setData(retMap);
		} catch (Exception e) {
			logger.error("查询文章阅读情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询文章阅读情况失败");
		}
		
		return result;
	}
	
	@Override
	public Response queryArticleReadCondDetails(String shareId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		
		try {
			QlyRhShareVO shareVO = shareService.getRhShareVO(shareId);
			int total_readers = baseDAO.findOne(mapperNamespace + ".getTotalReadersByUUID", shareId);
			int total_shares = baseDAO.findOne(mapperNamespace + ".getShareNum", shareId);
			
			List<RespArticleReadCondDetailsVO> dataList = new ArrayList<RespArticleReadCondDetailsVO>();
			List<Map<String, Object>> details = baseDAO.findListBy(mapperNamespace + ".queryArticleReadCondDetails", shareId);
			List<String> filterList = new ArrayList<String>();
			if(details != null && details.size() > 0) {
				for(Map<String, Object> m : details) {
					String openId = (String)m.get("visitor_id");
					if(filterList.contains(openId)) {
						continue ;
					} else {
						filterList.add(openId);
					}
					RespArticleReadCondDetailsVO readCondDetailsVO = new RespArticleReadCondDetailsVO();
					
					// 获取访客资料，因为存在多次修改昵称的情况，导致数据出现重复的。现在改成去最后一次昵称显示
					Map<String, Object> rmap = baseDAO.findOne(mapperNamespace + ".queryRhRecordInfoByVisitorId", openId);
	    			if(!rmap.isEmpty()) {
	    				readCondDetailsVO.setUser_name((String) rmap.get("user_name"));
	    				readCondDetailsVO.setHeadImgUrl((String) rmap.get("headImgUrl"));
	    				readCondDetailsVO.setTelphone((String) rmap.get("telphone"));
	    				readCondDetailsVO.setEwm_url((String) rmap.get("ewm_url"));
	    			}
					
					int total_num = 0, total_sharenum = 0, read_num = 0, share_num = 0;
					long total_readtimes = 0, read_time = 0;
					// 2020-6-10注释
//					total_num = baseDAO.findOne(mapperNamespace + ".getTotalNumRead", openId);
//					Integer iTotalReadtimes = baseDAO.findOne(mapperNamespace + ".getTotalReadTime", openId);
//					if(iTotalReadtimes != null) {
//						total_readtimes = iTotalReadtimes.longValue();
//					}
//					total_sharenum = getTotalShareNum(openId);
					
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("openId", openId);
					paramMap.put("shareId", shareId);
					
					Integer iReadtime = baseDAO.findOne(mapperNamespace + ".getReadTimeByUUID", paramMap);
					if(iReadtime != null) {
						read_time = iReadtime.longValue();
					}
					read_num = baseDAO.findOne(mapperNamespace + ".getReadNumByUUID", paramMap);
					share_num = baseDAO.findOne(mapperNamespace + ".getSourceShareNumByUUID", paramMap);
					
					readCondDetailsVO.setTotal_num(total_num);
					readCondDetailsVO.setTotal_readtimes(total_readtimes);
					readCondDetailsVO.setTotal_sharenum(total_sharenum);
					readCondDetailsVO.setRead_time(read_time);
					readCondDetailsVO.setRead_num(read_num);
					readCondDetailsVO.setShare_num(share_num);
//					readCondDetailsVO.setStrTotalReadtimes(DateUtil.secondToTime(total_readtimes));
//					readCondDetailsVO.setStrReadTime(DateUtil.secondToTime(read_time));
					
					//查询访客是否分享过该文章
					String isshare = "N";
					if(shareVO != null) {
						Map<String, String> paramMap2 = new HashMap<String, String>();
						paramMap2.put("articleUuid", shareVO.getArticle_uuid());
						paramMap2.put("openId", openId);
						QlyRhShareVO articleVO = baseDAO.findOne(ShareServiceImpl.class.getName() + ".getArticleShareByArticleUuid", paramMap2);
						if(articleVO != null) {
							isshare = "Y";
						}
					}
					readCondDetailsVO.setIsshare(isshare);
					dataList.add(readCondDetailsVO);
				}
			}
			
			retMap.put("totalReaders", total_readers);
			retMap.put("totalShares", total_shares);
			retMap.put("readCondDetails", dataList);
			
			result.setData(retMap);
		} catch (Exception e) {
			logger.error("查询文章阅读情况详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询文章阅读情况详情失败");
		}
		
		return result;
	}
	
	@Override
	public Response queryRecordCondition(String openId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		
		try {
			List<RespRecordConditionVO> dataList = new ArrayList<RespRecordConditionVO>();
			List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryRecordConditionList", openId);
			List<String> filterList = new ArrayList<String>();
			if(list != null && list.size() > 0) {
				for(Map<String, Object> m : list) {
					RespRecordConditionVO readCondVO = new RespRecordConditionVO();
					String visitorId = (String) m.get("visitor_id");
					if(filterList.contains(visitorId)) {
						continue ;
					} else {
						filterList.add(visitorId);
					}
					// 获取访客资料，因为存在多次修改昵称的情况，导致数据出现重复的。现在改成去最后一次昵称显示
					Map<String, Object> rmap = baseDAO.findOne(mapperNamespace + ".queryRhRecordInfoByVisitorId", visitorId);
	    			if(!rmap.isEmpty()) {
	        			readCondVO.setUser_name((String) rmap.get("user_name"));
		        		readCondVO.setHeadImgUrl((String) rmap.get("headImgUrl"));
		        		readCondVO.setTelphone((String) rmap.get("telphone"));
		        		readCondVO.setEwm_url((String) rmap.get("ewm_url"));
	    			}
					
					int total_num = 0, total_sharenum = 0;
					long total_readtimes = 0;
					total_num = baseDAO.findOne(mapperNamespace + ".getTotalNumRead", visitorId);
					
					Integer iTotalReadtimes = baseDAO.findOne(mapperNamespace + ".getTotalReadTime", visitorId);
					if(iTotalReadtimes != null) {
						total_readtimes = iTotalReadtimes.longValue();
					}
					total_sharenum = getTotalShareNum(visitorId);
					String latelyReadDate = baseDAO.findOne(mapperNamespace + ".getLatelyReadDate", visitorId);
					
					readCondVO.setOpenId(visitorId);
					readCondVO.setTotal_num(total_num);
					readCondVO.setTotal_readtimes(total_readtimes);
					readCondVO.setTotal_sharenum(total_sharenum);
					readCondVO.setLately_read_date(latelyReadDate);
					dataList.add(readCondVO);
				}
			}
			
			retMap.put("recordConditionList", dataList);
			
			result.setData(retMap);
		} catch (Exception e) {
			logger.error("查询访客情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询访客情况失败");
		}
		
		return result;
	}
	
	@Override
	public Response queryRecordConditionDetails(String openId, String visitorId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		
		try {
    		RespRecordConditionDetailsVO recordCondDetailsVO = new RespRecordConditionDetailsVO();
    		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(visitorId);
    		if(userVO != null) {
    			recordCondDetailsVO.setNick_name(userVO.getNick_name());
        		recordCondDetailsVO.setUser_name(userVO.getUser_name());
        		recordCondDetailsVO.setCompany(userVO.getCompany());
        		recordCondDetailsVO.setPosition(userVO.getPosition());
        		recordCondDetailsVO.setHeadImgUrl(userVO.getHeadImgUrl());
        		recordCondDetailsVO.setTelphone(userVO.getTelphone());
        		recordCondDetailsVO.setEwm_url(userVO.getEwm_url());
    		} else {
    			// 访客未关注
    			Map<String, Object> rmap = baseDAO.findOne(mapperNamespace + ".queryRhRecordInfoByVisitorId", visitorId);
    			if(!rmap.isEmpty()) {
    				recordCondDetailsVO.setUser_name((String) rmap.get("user_name"));
        			recordCondDetailsVO.setHeadImgUrl((String) rmap.get("headImgUrl"));
    			}
    			
    		}
    		
    		int total_num = 0, total_sharenum = 0;
    		long total_readtimes = 0;
			total_num = baseDAO.findOne(mapperNamespace + ".getTotalNumRead", visitorId);
			
			Integer iTotalReadtimes = baseDAO.findOne(mapperNamespace + ".getTotalReadTime", visitorId);
			if(iTotalReadtimes != null) {
				total_readtimes = iTotalReadtimes.longValue();
			}
			total_sharenum = getTotalShareNum(visitorId);
			String latelyReadDate = baseDAO.findOne(mapperNamespace + ".getLatelyReadDate", visitorId);
			
			recordCondDetailsVO.setTotal_num(total_num);
			recordCondDetailsVO.setTotal_readtimes(total_readtimes);
			recordCondDetailsVO.setTotal_sharenum(total_sharenum);
			recordCondDetailsVO.setLately_read_date(latelyReadDate);
			
			List<RespRecordReadArticleDetailsVO> articleDetails = null;
			Map<String, RespRecordReadArticleDetailsVO> tempMap = new LinkedHashMap<String, RespRecordReadArticleDetailsVO>();
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("openId", openId);
			queryMap.put("visitorId", visitorId);
			
			List<QlyRhShareRecordVO> recordList = baseDAO.findListBy(mapperNamespace + ".queryRhShareRecordList", queryMap);
			if(recordList != null && recordList.size() > 0) {
				for(QlyRhShareRecordVO recordVO : recordList) {
					String key = recordVO.getArticle_title();
					RespRecordReadArticleDetailsVO articleDetailsVO = tempMap.get(key);
					if(articleDetailsVO == null) {
						articleDetailsVO = new RespRecordReadArticleDetailsVO();
					}
					
					int read_num = articleDetailsVO.getRead_num();
					long read_time = articleDetailsVO.getRead_time();
					int share_num = articleDetailsVO.getShare_num();
//					String read_date = DateUtil.formatDateByFormat(recordVO.getVisit_date(), "yyyy-MM-dd HH:mm:ss");
					read_num++;
					read_time += recordVO.getRead_time();
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("openId", visitorId);
					map.put("share_uuid", recordVO.getShare_uuid());
					
					if(share_num == 0) {
						share_num = baseDAO.findOne(mapperNamespace + ".getRecordShareNum", map);
					}
					
					articleDetailsVO.setArticle_title(recordVO.getArticle_title());
					articleDetailsVO.setShare_uuid(recordVO.getShare_uuid());
					articleDetailsVO.setRead_num(read_num);
					articleDetailsVO.setRead_time(read_time);
					articleDetailsVO.setShare_num(share_num);
//					articleDetailsVO.setRead_date(read_date);
					logger.info("key：" + key);
					tempMap.put(key, articleDetailsVO);
				}
			}
			logger.info("获取追踪访客情况数据耗时：" + (System.currentTimeMillis() - time) / 1000);
			
			long time1 = System.currentTimeMillis();
			if(!tempMap.isEmpty()) {
				// 整理每一篇文章当前访客的阅读详情
				Map<String, RespRecordReadArticleDetailsVO> tempMap2 = new LinkedHashMap<String, RespRecordReadArticleDetailsVO>();
				Iterator<Entry<String, RespRecordReadArticleDetailsVO>> iterator = tempMap.entrySet().iterator();
		        Entry<String, RespRecordReadArticleDetailsVO> entry;
		        while (iterator.hasNext()) {
		            entry = iterator.next();
		            String key = entry.getKey();
		            RespRecordReadArticleDetailsVO respDetailsVO = entry.getValue();
		            
		            List<Map<String, Object>> readDetails = getRCArticleDetails(visitorId, respDetailsVO.getShare_uuid());
		            respDetailsVO.setReadDetails(readDetails);
		            logger.info("key2：" + key);
		            tempMap2.put(key, respDetailsVO);
		        }
		        
		        articleDetails = new ArrayList<RespRecordReadArticleDetailsVO>(tempMap2.values());
			}
			logger.info("获取追踪访客情况详情数据耗时：" + (System.currentTimeMillis() - time1) / 1000);
			
			recordCondDetailsVO.setArticleDetails(articleDetails);
			retMap.put("recordCondDetails", recordCondDetailsVO);
			
			result.setData(retMap);
		} catch (Exception e) {
			logger.error("查询访客详情情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询访客详情失败");
		}
		
		return result;
	}
	
	@Override
	public Response queryRecordConditionArticleDetails(String openId, String shareId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Response result = Response.getDefaulTrueInstance();
		
		List<Map<String, Object>> dataList = getRCArticleDetails(openId, shareId);
		if(dataList != null && dataList.size() > 0) {
			retMap.put("RCArticleDetails", dataList);
			result.setData(retMap);
		}
		
		return result;
	}
	
	@Override
	public Response getArticleInfo(String articleId) {
		Response result = Response.getDefaulTrueInstance();
		QlyRhArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getArticleInfo", articleId);
		if(articleVO != null) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("article_title", articleVO.getArticle_title());
			retMap.put("article_url", articleVO.getArticle_url());
			retMap.put("pic_url", articleVO.getPic_url());
			retMap.put("description", articleVO.getDescription());
			result.setData(retMap);
		}
		
		return result;
	}
	
	@Override
	public int getArticleTotalRecord(String articleId) {
		int totalRecordNum = baseDAO.findOne(mapperNamespace + ".getArticleTotalRecord", articleId);
		return totalRecordNum;
	}
	
	@Override
	public int getArticleTotalShareNum(String articleId) {
		int totalShareNum = baseDAO.findOne(mapperNamespace + ".getArticleTotalShareNum", articleId);
		return totalShareNum;
	}
	
	@Override
	public String getPushArticle(String openId) {
		String result = "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("ispublic", "1");
        paramMap.put("audit_status", "02");
        //文章总数
        int totalRecord = baseDAO.findOne(mapperNamespace + ".queryArticlesListCount", paramMap);
        
        //分页对象
        PageQueryVO pageQueryVO = new PageQueryVO();
        // 总页数
        int totalPage = totalRecord / 6;
        logger.info("********************总页数**************************************：{}", totalPage);
        int currentPage = getRandomNumber(totalPage);
        if(currentPage <= 0) {
        	currentPage = 1;
        }
        logger.info("********************当前页**************************************：{}", currentPage);
        pageQueryVO.setCurrentPage(currentPage);
        pageQueryVO.setPageSize(6);
        
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        //获客文章列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".queryArticlesList", paramMap, 
        			page.getStart(), page.getLimit());
        	
        	if(list != null && list.size() > 0) {
        		for(Map<String, Object> map : list) {
        			String rh_articleId = (String) map.get("uuid");
        			String article_title = (String) map.get("article_title");
        			String articleId = UUID.randomUUID().toString();
					
					String localUrl = frontendUrl + "#/contents?rh_articleId=" + rh_articleId + "&articleId=" + articleId + "&userId=" + openId + "&getInto=listGetInto";
					result += "<a href='"+ localUrl +"'>✬" + article_title + "</a>\n\n";
					
        		}
            }
        }
		logger.info("********************推送文章*********************：{}", result);
		
		return result;
	}
	
	@Override
	public QlyRhReptileQueueVO getQlyRhReptileQueueVO(String articleUrl) {
		QlyRhReptileQueueVO reptileQueueVO = baseDAO.findOne(mapperNamespace + ".getQlyRhReptileQueueVO", articleUrl);
		return reptileQueueVO;
	}
	
	@Override
	public void insertQlyRhReptileQueueVO(QlyRhReptileQueueVO vo) {
		baseDAO.add(mapperNamespace + ".insertQlyRhReptileQueueVO", vo);
	}
	
	@Override
	public void updateQlyRhReptileQueueVO(QlyRhReptileQueueVO vo) {
		vo.setModify_datetime(new Date());
		baseDAO.add(mapperNamespace + ".updateQlyRhReptileQueueVO", vo);
	}
	
	@Override
	public List<QlyRhReptileQueueVO> queryFixedReptileQueues() {
		return baseDAO.findListBy(mapperNamespace + ".queryFixedReptileQueues");
	}
	
	@Override
	public void insertQlyRhReptileArticleVO(QlyRhReptileArticleVO vo) {
		baseDAO.add(mapperNamespace + ".insertQlyRhReptileArticleVO", vo);
	}
	
	@Override
	public void insertQlyRhReptileArticleDetailVO(QlyRhReptileArticleDetailVO vo) {
		baseDAO.add(mapperNamespace + ".insertQlyRhReptileArticleDetailVO", vo);
	}
	
	@Override
	public void insertQlyRhCompanyArticleVO(QlyRhCompanyArticleVO vo) {
		baseDAO.add(mapperNamespace + ".insertQlyRhCompanyArticleVO", vo);
	}
	
	@Override
	public Response getArticleDetailInfo(String articleId) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, Object> retMap = baseDAO.findOne(mapperNamespace + ".getArticleDetailInfo", articleId);
		if(retMap != null && !retMap.isEmpty()) {
			result.setData(retMap);
		}
		
		return result;
	}
	
	@Override
	public QlyRhReptileArticleVO getQlyRhReptileArticleVO(String articleUrl) {
		QlyRhReptileArticleVO reptileArticleVO = baseDAO.findOne(mapperNamespace + ".getQlyRhReptileArticleVO", articleUrl);
		return reptileArticleVO;
	}
	
	@Override
	public Response queryMyArticle(String openId, String source) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, String> param = new HashMap<String, String>();
		param.put("openId", openId);
		param.put("source", source);
		
		List<Map<String, Object>> dataList = baseDAO.findListBy(mapperNamespace + ".queryMyArticle", param);
		if(dataList != null && dataList.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("articleList", dataList);
			result.setData(retMap);
		}
		
		return result;
	}
	
	@Override
	public Response queryMyEditArticle(String openId) {
		Response result = Response.getDefaulTrueInstance();
		
		List<Map<String, Object>> dataList = baseDAO.findListBy(mapperNamespace + ".queryMyEditArticle", openId);
		if(dataList != null && dataList.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("articleList", dataList);
			result.setData(retMap);
		}
		
		return result;
	}
	
	@Override
	public void batchInsertArticleAndCategory(List<Map<String, Object>> list) {
		baseDAO.add(mapperNamespace + ".batchInsertArticleAndCategory", list);
	}
	
	private List<Map<String, Object>> getRCArticleDetails(String openId, String shareId) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("openId", openId);
		map.put("shareId", shareId);
		
		List<QlyRhShareRecordVO> recordList = baseDAO.findListBy(mapperNamespace + ".queryRhShareRecordArticleList", map);
		if(recordList != null && recordList.size() > 0) {
			for(QlyRhShareRecordVO recordVO : recordList) {
				Map<String, Object> d = new HashMap<String, Object>();
				
				d.put("read_date", DateUtil.formatDateByFormat(recordVO.getVisit_date(), "yyyy-MM-dd HH:mm:ss"));
				d.put("read_time", recordVO.getRead_time());
				dataList.add(d);
			}
		}
		
		return dataList;
	}
	
	private int getRandomNumber(int t) {
		Random r = new Random();
		int num = r.nextInt(t);
		return num;
	}
	
	
	@Override
	public Response queryAdminArticlesList(QlyRhAdminArticleDTO articleDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
		
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer category_id = articleDTO.getCategory_id();
        if(category_id != null && category_id != 0) {
        	paramMap.put("category_id", category_id);
        }
        paramMap.put("article_title", articleDTO.getArticle_title());
        paramMap.put("source", articleDTO.getSource());
        paramMap.put("istop", articleDTO.getIstop());
        paramMap.put("audit_user", articleDTO.getAudit_user());
        paramMap.put("collect_startDate", articleDTO.getCollect_startDate());
        paramMap.put("collect_endDate", articleDTO.getCollect_endDate());
        paramMap.put("collect_endDate", articleDTO.getCollect_endDate());
        paramMap.put("audit_status", articleDTO.getAudit_status());
        paramMap.put("user_name", articleDTO.getUser_name());
        paramMap.put("share_startNum", articleDTO.getShare_startNum());
        paramMap.put("share_endNum", articleDTO.getShare_endNum());
        paramMap.put("read_startNum", articleDTO.getRead_startNum());
        paramMap.put("read_endNum", articleDTO.getRead_endNum());
        paramMap.put("collectDate_sort", articleDTO.getCollectDate_sort());
        paramMap.put("shareNum_sort", articleDTO.getShareNum_sort());
        paramMap.put("readNum_sort", articleDTO.getReadNum_sort());
        
        
        resp = queryAdminArticlesList(paramMap, articleDTO.getPageQueryVO());
        return resp;
	}
	
	@Override
	public Response queryAdminArticlesList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//文章总数
        int totalRecord = 0;
        //查询文章总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryAdminArticlesListCount", paramMap);
        
        //分页对象
//        PageQueryVO pageQueryVO = articleDTO.getPageQueryVO();
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
        //文章列表表头
//        List<Map<String, String>> headList = new ArrayList<Map<String, String>>();
//        String[] heads = TableConstants.ARTICLE_TABLE_HEAD;
//		headList = HeadUtil.getHeadList(heads);
//		resultMap.put("headList", headList);
        //文章列表
//        List<Map<String, Object>> articlesList = new ArrayList<Map<String, Object>>();
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".queryAdminArticlesList", paramMap, 
        			page.getStart(), page.getLimit());
        	
        	if(list != null && list.size() > 0) {
    			for(Map<String, Object> m : list) {
    				String article_uuid = (String) m.get("uuid");
    				String category_name = baseDAO.findOne(mapperNamespace + ".getArticleCategoryName", article_uuid);
    				
    				m.put("category_name", category_name);
    			}
    		}
        	
//        	if(list != null && list.size() > 0) {
//        		String[] headKeys = heads[0].split(";");
//
//        		for(Map<String, Object> map : list) {
//        			Map<String, Object> resMap = new HashMap<String, Object>();
//            		for(String headkey : headKeys) {
//            			resMap.put(headkey, map.get(headkey));
//            		}
//					articlesList.add(resMap);
//					
//        		}
//            }
        }
        resultMap.put("bodyList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
        
        return resp;
	}

	@Override
	public Response queryAdminArticleInfo(String uuid) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = baseDAO.findOne(mapperNamespace + ".queryArticleInfo", uuid);
		if(resultMap != null && !resultMap.isEmpty()) {
			int share_num = resultMap.get("share_num") == null ? 0 :  (int) resultMap.get("share_num");
			int read_num = resultMap.get("read_num") == null ? 0 :  (int) resultMap.get("read_num");
			String mp_date = baseDAO.findOne(mapperNamespace + ".getArticleMpDate", uuid);
			if(StringUtils.isBlank(mp_date)) {
				mp_date = "";
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("article_title", (String) resultMap.get("article_title"));
			retMap.put("pic_url", (String) resultMap.get("pic_url"));
			retMap.put("article_content", (String) resultMap.get("article_content"));
			retMap.put("edit_date", (String) resultMap.get("edit_date"));
			retMap.put("collect_date", (String) resultMap.get("collect_date"));
			retMap.put("source", (String) resultMap.get("source"));
			retMap.put("audit_status", (String) resultMap.get("audit_status"));
			retMap.put("istop", (String) resultMap.get("istop"));
			retMap.put("category_name", (String) resultMap.get("category_name"));
			retMap.put("share_num", share_num);
			retMap.put("read_num", read_num);
			retMap.put("mp_date", mp_date);
			resp.setData(retMap);
			resp.setMessage("查询文章详情成功");
		} else {
			resp.setCode("1");
            resp.setMessage("该文章不存在，可能已经被删除");
		}
		return resp;
	}
	
	@Override
	public void updateReptileArticle(QlyRhReptileArticleVO vo) {
		vo.setModify_datetime(new Date());
		baseDAO.update(mapperNamespace + ".updateReptileArticle", vo);
	}
	
	@Override
	public Response queryArticleCategoryList(String uuid) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryArticleCategoryList", uuid);
		if(list == null || (list.size() > 0 && list.get(0) == null)) {
			list = new ArrayList<Map<String, Object>>();
		}
		retMap.put("categoryList", list);
		resp.setData(retMap);
		return resp;
	}
	
	@Override
	public void updateReptileArticleCategory(String uuid, String categoryId, String mpflag, String userName) {
		String[] categoryIds = categoryId.split(",");
		QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getReptileArticleVOByuuid", uuid);
		int articleId = articleVO.getId();
		
		baseDAO.delete(mapperNamespace + ".deleteArticleCategory", articleId);
		List<Map<String, Object>> addCategorys = new ArrayList<Map<String, Object>>();
		for(String id : categoryIds) {
			Map<String, Object> addParam = new HashMap<String, Object>();
	        addParam.put("article_id", articleId);
	        addParam.put("category_id", id);
	        addCategorys.add(addParam);
		}
		
		if(addCategorys.size() > 0) {
			batchInsertArticleAndCategory(addCategorys);
		}
		
		if("Y".equals(mpflag)) {
			// 如果勾选了财税早报，则加到财税早报清单里（财税早报10点之前加的，就放在当天，否则就放在第二天推送）
			Date dt = new Date();
			String currentHours = DateUtil.formatDateByFormat(dt, "HH");
			int iCurrentHours = Integer.parseInt(currentHours);
			if(iCurrentHours < 10) {
				morningPaperService.addMorningpaperArticle(articleVO, null, userName);
			} else {
				Date nextDt = DateUtil.afterNDay(dt, 1);
				String nextDay = DateUtil.formatDate(nextDt);
				morningPaperService.addMorningpaperArticle(articleVO, nextDay, userName);
			}
			
		}
	}
	
	@Override
	public void updateReptileArticleDetail(ReqReptileArticleDetailDTO articleDetailDTO) {
    	articleDetailDTO.setModify_datetime(new Date());
		baseDAO.update(mapperNamespace + ".updateReptileArticleDetail", articleDetailDTO);
	}
	
	@Override
	public Response queryAdminArticlesShareAndReadList(QlyRhAdminArticleDTO articleDTO) {
		Response resp = Response.getDefaulTrueInstance();
		
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer category_id = articleDTO.getCategory_id();
        if(category_id != null && category_id != 0) {
        	paramMap.put("category_id", category_id);
        }
        paramMap.put("article_title", articleDTO.getArticle_title());
        paramMap.put("source", articleDTO.getSource());
        paramMap.put("collect_startDate", articleDTO.getCollect_startDate());
        paramMap.put("collect_endDate", articleDTO.getCollect_endDate());
        paramMap.put("share_startNum", articleDTO.getShare_startNum());
        paramMap.put("share_endNum", articleDTO.getShare_endNum());
        paramMap.put("read_startNum", articleDTO.getRead_startNum());
        paramMap.put("read_endNum", articleDTO.getRead_endNum());
        paramMap.put("istop", articleDTO.getIstop());
        paramMap.put("audit_user", articleDTO.getAudit_user());
        paramMap.put("audit_status", articleDTO.getAudit_status());
        paramMap.put("user_name", articleDTO.getUser_name());
        
        // 从首页跳转过去
        if(StringUtils.isNotBlank(articleDTO.getFromIndex())) {
        	paramMap.clear();
        	Date dt = new Date();
    		String todayDate = DateUtil.formatDate(dt);
        	paramMap.put("todayDate", todayDate);
        	
        	Map<String, Object> resultMap = new HashMap<String, Object>();
        	PageQueryVO pageQueryVO = articleDTO.getPageQueryVO();
        	//文章总数
            int totalRecord = 0;
            //文章列表
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            //分页对象
            Page page = null;
            //查询文章总数
            if("2".equals(articleDTO.getFromIndex())) {
            	totalRecord = baseDAO.findOne(mapperNamespace + ".queryAdminArticlesReadCountFromIndex", paramMap);
            	
            	//分页对象
                page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
                if (totalRecord > 0) {
                	list = baseDAO.findListByRange(mapperNamespace + ".queryAdminArticlesReadListFromIndex", paramMap, 
                			page.getStart(), page.getLimit());
                	
                }
            } else {
            	totalRecord = baseDAO.findOne(mapperNamespace + ".queryAdminArticlesShareCountFromIndex", paramMap);
            	
            	//分页对象
                page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);
                if (totalRecord > 0) {
                	list = baseDAO.findListByRange(mapperNamespace + ".queryAdminArticlesShareListFromIndex", paramMap, 
                			page.getStart(), page.getLimit());
                	
                }
            }
            
            if(list != null && list.size() > 0) {
    			for(Map<String, Object> m : list) {
    				String article_uuid = (String) m.get("uuid");
    				String category_name = baseDAO.findOne(mapperNamespace + ".getArticleCategoryName", article_uuid);
    				
    				m.put("category_name", category_name);
    			}
    		}
            
            resultMap.put("bodyList", list);
            resultMap.put("page", page);
            resp.setData(resultMap);
        	
        	
        } else {
        	resp = queryAdminArticlesShareAndReadList(paramMap, articleDTO.getPageQueryVO());
        }
        
        return resp;
	}
	
	@Override
	public Response queryAdminArticlesShareAndReadList(Map<String, Object> paramMap, PageQueryVO pageQueryVO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//文章总数
        int totalRecord = 0;
        //查询文章总数
        totalRecord = baseDAO.findOne(mapperNamespace + ".queryAdminArticlesShareAndReadCount", paramMap);
        
        //分页对象
        Page page = new Page(pageQueryVO.getCurrentPage(), pageQueryVO.getPageSize(), totalRecord);

        //文章列表
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (totalRecord > 0) {
        	list = baseDAO.findListByRange(mapperNamespace + ".queryAdminArticlesShareAndReadList", paramMap, 
        			page.getStart(), page.getLimit());
        	
        	if(list != null && list.size() > 0) {
    			for(Map<String, Object> m : list) {
    				String article_uuid = (String) m.get("uuid");
    				String category_name = baseDAO.findOne(mapperNamespace + ".getArticleCategoryName", article_uuid);
    				
    				m.put("category_name", category_name);
    			}
    		}
        	
        }
        resultMap.put("bodyList", list);
        resultMap.put("page", page);
        resp.setData(resultMap);
        return resp;
	}
	
	@Override
	public Response queryAdminArticlesShareUserList(ReqArticleShareReadDTO shareReadDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryAdminArticlesShareUserList", shareReadDTO);
		if(list == null || (list.size() > 0 && list.get(0) == null)) {
			list = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("userList", list);
		resp.setData(resultMap);
		return resp;
	}
	
	@Override
	public Response queryAdminArticlesReadUserList(ReqArticleShareReadDTO shareReadDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = baseDAO.findListBy(mapperNamespace + ".queryAdminArticlesReadUserList", shareReadDTO);
		if(list == null || (list.size() > 0 && list.get(0) == null)) {
			list = new ArrayList<Map<String, Object>>();
		}
		resultMap.put("userList", list);
		resp.setData(resultMap);
		return resp;
	}
	
	@Override
	public List<QlyRhShareVO> queryShareArticles(String openId) {
		return baseDAO.findListBy(mapperNamespace + ".queryShareArticles", openId);
	}
	
	@Override
	public int getTotalRecordConditionCount(String openId) {
		int totalNum = baseDAO.findOne(mapperNamespace + ".getTotalRecordConditionCount", openId);
		return totalNum;
	}
	
	@Override
	public Response updateArticleInfo(ReqReptileArticleInfoDTO articleInfoDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		QlyRhReptileArticleVO articleVO = new QlyRhReptileArticleVO();
        if(articleInfoDTO.getArticleImg() != null) {
        	//使用UUID给图片重命名，并去掉四个“-”
            String uuidImg = UUID.randomUUID().toString().replaceAll("-", "");
        	MultipartFile articleImg = articleInfoDTO.getArticleImg();
        	double fileSize = articleImg.getSize() / 1048576;// M
            if(fileSize > 5) {
            	logger.error("***************文章图片大小超过5M****************" + fileSize);
            	resp.setCode("1");
            	resp.setSuccess(false);
            	resp.setMessage("二维码图片大小超过5M");
                return resp;
            }
            //获取文件的扩展名
            String ewmExt = FilenameUtils.getExtension(articleImg.getOriginalFilename());
            if(StringUtils.isBlank(ewmExt)) {
            	// 如果文件的扩展名为空，则默认为jpg格式
            	ewmExt = "jpg";
            }
            //根据日期来创建对应文件夹
            String datePath=new SimpleDateFormat("yyyyMMdd").format(new Date());
            String dirPath = "images/article" + File.separator + datePath + File.separator;
            String path = articleInfoDTO.getFilePath() + dirPath;
            //如果不存在,创建文件夹
            File f = new File(path);
            if(!f.exists()){
                f.mkdirs();
            }
            //以绝对路径保存重名命后的图片
            String absolutePath = path + uuidImg + "." + ewmExt;
            try {
				articleImg.transferTo(new File(absolutePath));
			} catch (Exception e) {
				logger.error("***************文章图片上传失败****************" + e);
				resp.setCode("1");
            	resp.setSuccess(false);
            	resp.setMessage("文章图片上传失败");
                return resp;
			}
            //把图片存储路径保存到数据库
            String picUrl = articleInfoDTO.getFileUrlPath() + dirPath + uuidImg + "." + ewmExt;
            articleVO.setPic_url(picUrl);
            resultMap.put("picUrl", picUrl);
        }
        
        if(StringUtils.isNotBlank(articleInfoDTO.getArticle_title())) {
        	articleVO.setArticle_title(articleInfoDTO.getArticle_title());
        }
        
        if(StringUtils.isNotBlank(articleInfoDTO.getShare_text())) {
        	articleVO.setShare_text(articleInfoDTO.getShare_text());
        }
        
        articleVO.setModify_user(articleInfoDTO.getModify_user());
        articleVO.setUuid(articleInfoDTO.getUuid());
        updateReptileArticle(articleVO);
        
        resp.setData(resultMap);
        resp.setMessage("修改文章信息成功");
		return resp;
	}
	
	@Override
	public Response addArticle(ReqReptileArticleInfoDTO articleInfoDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
    	String uuid = UUID.randomUUID().toString();
		QlyRhReptileArticleVO articleVO = new QlyRhReptileArticleVO();
		articleVO.setUuid(uuid);
		
        if(articleInfoDTO.getArticleImg() != null) {
        	//使用UUID给图片重命名，并去掉四个“-”
            String uuidImg = UUID.randomUUID().toString().replaceAll("-", "");
        	MultipartFile articleImg = articleInfoDTO.getArticleImg();
        	double fileSize = articleImg.getSize() / 1048576;// M
            if(fileSize > 5) {
            	logger.error("***************文章图片大小超过5M****************" + fileSize);
            	resp.setCode("1");
            	resp.setSuccess(false);
            	resp.setMessage("二维码图片大小超过5M");
                return resp;
            }
            //获取文件的扩展名
            String ewmExt = FilenameUtils.getExtension(articleImg.getOriginalFilename());
            if(StringUtils.isBlank(ewmExt)) {
            	// 如果文件的扩展名为空，则默认为jpg格式
            	ewmExt = "jpg";
            }
            //根据日期来创建对应文件夹
            String datePath=new SimpleDateFormat("yyyyMMdd").format(new Date());
            String dirPath = "images/article" + File.separator + datePath + File.separator;
            String path = articleInfoDTO.getFilePath() + dirPath;
            //如果不存在,创建文件夹
            File f = new File(path);
            if(!f.exists()){
                f.mkdirs();
            }
            //以绝对路径保存重名命后的图片
            String absolutePath = path + uuidImg + "." + ewmExt;
            try {
				articleImg.transferTo(new File(absolutePath));
			} catch (Exception e) {
				logger.error("***************文章图片上传失败****************" + e);
				resp.setCode("1");
            	resp.setSuccess(false);
            	resp.setMessage("文章图片上传失败");
                return resp;
			}
            //把图片存储路径保存到数据库
            String picUrl = articleInfoDTO.getFileUrlPath() + dirPath + uuidImg + "." + ewmExt;
            articleVO.setPic_url(picUrl);
            resultMap.put("picUrl", picUrl);
        }
        
        if(StringUtils.isNotBlank(articleInfoDTO.getArticle_title())) {
        	articleVO.setArticle_title(articleInfoDTO.getArticle_title());
        }
        
        if(StringUtils.isNotBlank(articleInfoDTO.getShare_text())) {
        	articleVO.setShare_text(articleInfoDTO.getShare_text());
        }
        
        articleVO.setIstop(articleInfoDTO.getIstop());
        articleVO.setAudit_status("02");
        articleVO.setIspublic("1");
        articleVO.setCollect_date(DateUtil.formatDate(new Date()));
        articleVO.setCreate_user(articleInfoDTO.getModify_user());
        
        this.insertQlyRhReptileArticleVO(articleVO);
        
        QlyRhReptileArticleDetailVO articleDetailVO = new QlyRhReptileArticleDetailVO();
        articleDetailVO.setArticle_uuid(articleVO.getUuid());
        articleDetailVO.setArticle_content(articleInfoDTO.getArticle_content());
        this.insertQlyRhReptileArticleDetailVO(articleDetailVO);
        
        // 修改文章所属类别
        if(StringUtils.isNotBlank(articleInfoDTO.getCategoryId())) {
        	this.updateReptileArticleCategory(articleVO.getUuid(), articleInfoDTO.getCategoryId(), "N", articleInfoDTO.getModify_user());
        }
        
        resp.setData(resultMap);
        resp.setMessage("新增文章成功");
		return resp;
	}
	
	@Override
	public void insertArticleUselog(QlyRhArticleUselogVO vo) {
		Date dt = new Date();
		String use_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		vo.setUse_date(use_date);
		vo.setOrder_index(1);
		
		baseDAO.add(mapperNamespace + ".insertArticleUselog", vo);
	}
	
	@Override
	public int getPushSubscribeArticleCount(int userId) {
		Date dt = new Date();
		String use_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("use_date", use_date);
        
        //获客文章列表
        int c = baseDAO.findOne(mapperNamespace + ".queryPushSubscribeArticleCount", paramMap);
		return c;
	}
	
	@Override
	public String getPushSubscribeArticle(String openId) {
		Date dt = new Date();
		String use_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		
		QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(openId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userVO.getUser_id());
        paramMap.put("use_date", use_date);
        
        //获客文章列表
        List<Map<String, Object>> articlesList = baseDAO.findListBy(mapperNamespace + ".queryPushSubscribeArticleList", paramMap);
        
        String content = "今日好文\n";
		if(articlesList != null && articlesList.size() > 0) {
			for(Map<String, Object> map : articlesList) {
				String article_uuid = (String) map.get("article_uuid");
    			String article_title = (String) map.get("article_title");
    			String articleId = UUID.randomUUID().toString();
				
				String localUrl = frontendUrl + "#/contents?rh_articleId=" + article_uuid + "&articleId=" + articleId + "&userId=" + openId + "&getInto=listGetInto";
				content += "<a href='"+ localUrl +"'>✬" + article_title + "</a>\n\n";
			}
		}
		content += "其他新鲜热辣的财税资讯，都在下面的【获客好文】哦\n";
		content += "👇👇👇 ";
		logger.info("********************推送订阅文章*********************：{}", content);
		
		return content;
	}
	
	@Override
	public void updateArticleTop(String uuid, String topFlag, String categoryId, String userName) {
		/**
		 * 
		 待需求确认，如何修改再调整。
		String[] categoryIds = categoryId.split(",");
		
		if(categoryIds.length > 0) {
			QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getReptileArticleVOByuuid", uuid);
			int articleId = articleVO.getId();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("articleId", articleId);
			paramMap.put("istop", topFlag);
			for(String cId : categoryIds) {
				paramMap.put("categoryId", cId);
				baseDAO.update(mapperNamespace + ".updateArticleTop", paramMap);
			}
		}*/
		
    	QlyRhReptileArticleVO articleVO = new QlyRhReptileArticleVO();
    	articleVO.setUuid(uuid);
    	articleVO.setIstop(topFlag);
    	articleVO.setModify_user(userName);
    	this.updateReptileArticle(articleVO);
	}
	
	@Override
	public Response saveEditorArticle(ReqReptileArticleDetailDTO articleDetailDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 文章的UUID
		String uuid = articleDetailDTO.getUuid();
		QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getReptileArticleVOByuuid", uuid);
		// 如果编辑的是公司文章,直接覆盖原来的文章内容
		if(articleVO.getCompany_id() == 0 && (StringUtils.isBlank(articleVO.getEditor()) || "N".equals(articleVO.getEditor()))) {
			// 复制一份新的文章
			QlyRhReptileArticleVO newArticleVO = new QlyRhReptileArticleVO();
	        newArticleVO.setUuid(UUID.randomUUID().toString());
	        newArticleVO.setArticle_title(articleDetailDTO.getArticle_title());
	        newArticleVO.setArticle_url(articleVO.getArticle_url());
	        newArticleVO.setPic_url(articleDetailDTO.getPic_url());
	        newArticleVO.setDescription(articleDetailDTO.getDescription());
	        newArticleVO.setCreate_user(articleDetailDTO.getOpenId());
	        newArticleVO.setAudit_status(ArticleConstants.ARTICLE_STATUS_PENDING);
	        newArticleVO.setIspublic("0");
	        newArticleVO.setSource(articleVO.getSource());
	        newArticleVO.setCollect_date(articleVO.getCollect_date());
	        newArticleVO.setIstop(ArticleConstants.ARTICLE_IS_NOTTOP);
	        newArticleVO.setEditor("Y");
	        this.insertQlyRhReptileArticleVO(newArticleVO);
	        
	        uuid = newArticleVO.getUuid();
	        QlyRhReptileArticleDetailVO articleDetailVO = new QlyRhReptileArticleDetailVO();
	        articleDetailVO.setArticle_uuid(newArticleVO.getUuid());
	        articleDetailVO.setArticle_content(articleDetailDTO.getArticle_content());
	        this.insertQlyRhReptileArticleDetailVO(articleDetailVO);
	        
	        List<Map<String, Object>> addCategorys = new ArrayList<Map<String, Object>>();
	        Map<String, Object> addParam = new HashMap<String, Object>();
	        addParam.put("article_id", newArticleVO.getId());
	        addParam.put("category_id", Constants.ARTICLE_DEFAULT_CATEGORYID);
	        addCategorys.add(addParam);
	        this.batchInsertArticleAndCategory(addCategorys);
		} else {
			QlyRhReptileArticleVO updateArticleVO = new QlyRhReptileArticleVO();
			updateArticleVO.setUuid(uuid);
			updateArticleVO.setPic_url(articleDetailDTO.getPic_url());
			updateArticleVO.setArticle_title(articleDetailDTO.getArticle_title());
			updateArticleVO.setDescription(articleDetailDTO.getDescription());
			updateArticleVO.setModify_user(articleDetailDTO.getOpenId());
			this.updateReptileArticle(updateArticleVO);
			
			this.updateReptileArticleDetail(articleDetailDTO);
		}
		
		sendSaveArticleMessage(articleDetailDTO.getArticle_title(), uuid, articleDetailDTO.getOpenId());
		
		resultMap.put("uuid", uuid);
		resp.setData(resultMap);
		resp.setMessage("修改文章成功");
		
		return resp;
	}
	
	private void sendSaveArticleMessage(String article_title, String rh_articleId, String openId) {
		String articleId = UUID.randomUUID().toString();
		String localUrl = frontendUrl + "#/contents?rh_articleId=" + rh_articleId + "&articleId=" + articleId + "&userId=" + openId + "&getInto=listGetInto";
		
		// 文章名称长度超过15个字符，截取掉用省略号显示
		if(article_title.length() > 15) {
			article_title = article_title.substring(0, 15) + "...";
		}
		String msgContent = "您编辑的文章《" + article_title + "》已保存，<a href='"+ localUrl +"'>点击查看</a>>>>";
		
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setTouser(openId);
		msgVO.setContent(msgContent);
		msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
		msgVO.setCreateTime(new Date());
		
		messageCenterService.sendMsg(msgVO);
	}
	
	/*
	@Override
	public Response saveEditorArticleInfo(ReqReptileArticleInfoDTO articleInfoDTO) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 文章的UUID
		String uuid = articleInfoDTO.getUuid();
		QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getReptileArticleVOByuuid", uuid);
		if(StringUtils.isBlank(articleVO.getEditor()) || "N".equals(articleVO.getEditor())) {
			// 复制一份新的文章
			QlyRhReptileArticleVO newArticleVO = new QlyRhReptileArticleVO();
	        newArticleVO.setUuid(UUID.randomUUID().toString());
	        newArticleVO.setArticle_title(articleVO.getArticle_title());
	        newArticleVO.setArticle_url(articleVO.getArticle_url());
	        newArticleVO.setPic_url(articleVO.getPic_url());
	        newArticleVO.setCreate_user(articleInfoDTO.getOpenId());
	        newArticleVO.setAudit_status(ArticleConstants.ARTICLE_STATUS_PENDING);
	        newArticleVO.setIspublic("0");
	        newArticleVO.setSource(articleVO.getSource());
	        newArticleVO.setCollect_date(articleVO.getCollect_date());
	        newArticleVO.setIstop(ArticleConstants.ARTICLE_IS_NOTTOP);
	        newArticleVO.setEditor("Y");
	        this.insertQlyRhReptileArticleVO(newArticleVO);
	        
	        uuid = newArticleVO.getUuid();
	        QlyRhReptileArticleDetailVO articleDetailVO = new QlyRhReptileArticleDetailVO();
	        articleDetailVO.setArticle_uuid(newArticleVO.getUuid());
	        
	        Map<String, Object> retMap = baseDAO.findOne(mapperNamespace + ".getArticleDetailInfo", articleInfoDTO.getUuid());
			if(retMap != null && !retMap.isEmpty()) {
				articleDetailVO.setArticle_content((String) retMap.get("article_content"));
			}
	        this.insertQlyRhReptileArticleDetailVO(articleDetailVO);
	        
	        List<Map<String, Object>> addCategorys = new ArrayList<Map<String, Object>>();
	        Map<String, Object> addParam = new HashMap<String, Object>();
	        addParam.put("article_id", newArticleVO.getId());
	        addParam.put("category_id", Constants.ARTICLE_DEFAULT_CATEGORYID);
	        addCategorys.add(addParam);
	        this.batchInsertArticleAndCategory(addCategorys);
		}
		
		// 替换新的文章UUID
		articleInfoDTO.setUuid(uuid);
		resp = this.updateArticleInfo(articleInfoDTO);
		resultMap = (Map) resp.getData();
		
		resultMap.put("uuid", uuid);
		resp.setData(resultMap);
		resp.setMessage("修改文章成功");
		
		return resp;
	}*/
	
	@Override
	public int getTotalReadersByTimeQuantum(Map<String, Object> paramMap) {
		return baseDAO.findOne(mapperNamespace + ".getTotalReadersByTimeQuantum", paramMap);
	}
	
	
	@Override
	public Response delArticle(String uuid) {
		Response resp = Response.getDefaulTrueInstance();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uuid", uuid);
		
		QlyRhReptileArticleVO articleVO = baseDAO.findOne(mapperNamespace + ".getReptileArticleVOByuuid", uuid);
		// 目前只能删除公司文章
		if(articleVO.getCompany_id() == 0) {
			resp.setCode("1");
        	resp.setSuccess(false);
        	resp.setMessage("非公司文章不能删除");
            return resp;
		}
		
		int c = baseDAO.delete(mapperNamespace + ".delArticle", paramMap);
		resp.setMessage("删除文章成功");
		return resp;
	}
	
	@Override
	public int getTotalWeekShareNum(Map<String, Object> paramMap) {
		int totalShareNum = baseDAO.findOne(mapperNamespace + ".getTotalWeekShareNum", paramMap);
		return totalShareNum;
	}
}
