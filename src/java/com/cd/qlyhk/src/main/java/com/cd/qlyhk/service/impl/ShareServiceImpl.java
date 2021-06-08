package com.cd.qlyhk.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.api.prov.service.ICallCrmService;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.constants.WXConstants;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IShareService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhShareRecordVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhUserClickArticleVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqShareArticleDTO;
import com.cd.qlyhk.vo.SendMessageVO;
import com.cd.qlyhk.vo.WxRecordDTO;
import com.cd.rdf.base.dao.IBaseDAO;

/**
 * 分享服务实现
 * @author sailor
 *
 */
@Service(IShareService.BEAN_ID)
public class ShareServiceImpl implements IShareService {

	private static final Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);
	
	private final String mapperNamespace = ShareServiceImpl.class.getName();
	
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Autowired
	private IBaseDAO baseDAO;
	
	@Autowired
	private IPubCommonService pubCommonService;
	
	@Autowired
	private IUserService userService;
	
//	@Autowired
//	private ServicerQueue queueService;
	
	@Autowired
	private IMessageCenterService messageCenterService;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private ICallCrmService callCrmService;
	
	@Override
	public void insertRecord(WxRecordDTO recordDTO) {
		
		if("false".equals(recordDTO.getSharer())) {
			logger.info("参数传入有误[pageAccess]");
			return ;
		}
		// 先判断文章的UUID是否存在，不存在则新增
		QlyRhShareVO rRhShareVO = null;
		if(StringUtils.isNotBlank(recordDTO.getArticle_id())) {
			String articleId = recordDTO.getArticle_id();
			rRhShareVO = getRhShareVO(articleId);
			if(rRhShareVO == null) {
				rRhShareVO = wxRecordTransformQlyRhShareVO(recordDTO);
				baseDAO.add(mapperNamespace+".insertRhShareVO", rRhShareVO);
			}
			recordDTO.setShare_id(rRhShareVO.getShare_id());
		}
		recordDTO.setRead_date(DateUtil.formatDate(new Date()));
		baseDAO.add(mapperNamespace+".insertRecord", recordDTO);
		
		// 记录访客分享（如果访客分享了别人分享的文章时，记录一次分享）
		if(StringUtils.isNotBlank(recordDTO.getVisitor_sharer()) && !"false".equals(recordDTO.getVisitor_sharer())) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("articleUuid", recordDTO.getRh_articleId());
			paramMap.put("openId", recordDTO.getVisitor_sharer());
			
			QlyRhShareVO articleVO = baseDAO.findOne(mapperNamespace + ".getArticleShareByArticleUuid", paramMap);
			if(articleVO == null) {
				String uuid = UUID.randomUUID().toString();
				QlyRhShareVO rhShareVO = new QlyRhShareVO();
				rhShareVO.setUuid(uuid);
				rhShareVO.setArticle_uuid(recordDTO.getRh_articleId());
				rhShareVO.setArticle_title(recordDTO.getArticle_title());
				rhShareVO.setArticle_url(recordDTO.getArticle_url());
				rhShareVO.setSharer(recordDTO.getVisitor_sharer());
				rhShareVO.setShare_date(DateUtil.formatDate(new Date()));
				baseDAO.add(mapperNamespace+".insertRhShareVO", rhShareVO);
			}
		}
		
		// 判断是否是自己阅读自己分享的文章,是则不提醒(如果访客信息为空,也不提醒,因为在打开分享页面进行分享的时候也会调用该接口)
		if(StringUtils.isNotBlank(recordDTO.getVisitor_id()) && !recordDTO.getVisitor_id().equals(recordDTO.getSharer())) {
			// 判断是否立即提醒
			isSendMessageRemind(recordDTO);
			
			// 新增线索
			QlyRhUserVO sharerUserVO = dataCacheService.getQlyRhUserVO(recordDTO.getSharer());
			QlyRhUserVO visitorUserVO = dataCacheService.getQlyRhUserVO(recordDTO.getVisitor_id());
			callCrmService.addClue(recordDTO.getAppId(), sharerUserVO.getUnionid(), visitorUserVO.getUnionid(), rRhShareVO.getUuid());
		}
		
	}

	@Override
	public void updateRecordQuitDate(String uuid) {
		Date dt = new Date();
		long readTime = 0;
		QlyRhShareRecordVO recordVO = baseDAO.findOne(mapperNamespace+".getRhShareRecord", uuid);
		if(recordVO != null) {
			Date visitDate = recordVO.getVisit_date();
			long time = dt.getTime() - visitDate.getTime();
			readTime = time / 1000; // 以秒为单位
		}
		
		/*
		 * if(readTime < 1) { readTime = 1; }
		 */
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uuid", uuid);
		param.put("quit_date", dt);
		param.put("readTime", readTime);
		baseDAO.update(mapperNamespace+".updateRecordQuitDate", param);
	}

	@Override
	public void insertRhShare(ReqShareArticleDTO reqShareArticleDTO) {
		baseDAO.add(mapperNamespace+".insertRhShare", reqShareArticleDTO);
	}
	
	@Override
	public Response getTempCodeUrl(String articleId) {
		Response result = Response.getDefaulTrueInstance();
		JSONObject json = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			String access_token = pubCommonService.getCacheAccessToken();
        	
        	if(StringUtils.isNotBlank(access_token)) {
        		//expire_seconds 以秒为单位
        		String jsonStr = "{\"expire_seconds\": 1800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+ articleId +"\"}}}";
        		String code_ticket = WxUtil.GetCodeTicket(access_token, jsonStr);
				logger.info("*************获取临时二维码ticket*************" + code_ticket);
				json = JSONObject.parseObject(code_ticket);
				String ticket = json.getString("ticket");
				
				if(json != null && StringUtils.isNotBlank(ticket)) {
//					code_ticket = json.getString("ticket");
					String codeUrl = WXConstants.ShowQrCode_URL + ticket;
					logger.info("*************获取临时二维码*************" + codeUrl);
					map.put("ewmUrl", codeUrl);
				} else if(json != null && json.getIntValue("errcode") == 40001) {
	    			String errMsg = json.getString("errmsg");
	    			if(errMsg.contains("access_token is invalid")) {
	    				logger.info("*************[getTempCodeUrl]清除缓存AccessToken*************");
	    				dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
	    				dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
	    			}
	    		}
        	}
        	
        	result.setData(map);
			
		} catch (Exception e) {
			logger.error("获取临时二维码URL异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取临时二维码URL失败");
		}
		
		return result;
	}
	
	@Override
	public QlyRhShareVO getRhShareVO(String shareUuid) {
		return baseDAO.findOne(mapperNamespace+".queryRhShare", shareUuid);
	}
	
	@Override
	public QlyRhShareVO scanQRCodeShareAarticle(String openId, String shareUuid, String appId) {
		QlyRhShareVO resultVO = null;
		QlyRhShareVO rhShareVO = getRhShareVO(shareUuid);
		if(rhShareVO != null) {
			// 判断扫码者与分享者是否是同一个人
			if(openId.equals(rhShareVO.getSharer())) {
				logger.info("********************已经分享过该文章********************" + shareUuid);
				resultVO = rhShareVO;
			} else {
				logger.info("********************邀请伙伴加入********************{},{}", openId, rhShareVO.getSharer());
				// 检查用户信息是否存在，不存在则新增
	    		QlyRhUserVO userVO = userService.getUserVOByOpenId(openId, rhShareVO.getSharer(), appId);
	    		
	    		Map<String, String> paramMap = new HashMap<String, String>();
	    		paramMap.put("articleUuid", rhShareVO.getArticle_uuid());
	    		paramMap.put("openId", openId);
	    		QlyRhShareVO articleVO = baseDAO.findOne(mapperNamespace + ".getArticleShareByArticleUuid", paramMap);
				if(articleVO == null) {
					String uuid = UUID.randomUUID().toString();
					// 新建分享VO类
					QlyRhShareVO newRhShareVO = new QlyRhShareVO();
					newRhShareVO.setUuid(uuid);
					newRhShareVO.setArticle_uuid(rhShareVO.getArticle_uuid());
					newRhShareVO.setArticle_title(rhShareVO.getArticle_title());
					newRhShareVO.setArticle_url(rhShareVO.getArticle_url());
					newRhShareVO.setSharer(openId);
					newRhShareVO.setSource(rhShareVO.getSharer());
					newRhShareVO.setSource_share_uuid(shareUuid);
					newRhShareVO.setShare_date(DateUtil.formatDate(new Date()));
					// 写入
					baseDAO.add(mapperNamespace+".insertRhShareVO", newRhShareVO);
					logger.info("********************新建一条分享记录********************" + newRhShareVO.getShare_id());
					resultVO = newRhShareVO;
				} else {
					resultVO = articleVO;
				}
				
			}
			
		}
		
		return resultVO;
	}
	

	
	@Override
	public Response getArticleShareByUuid(String articleUuid, String openId) {
		Response result = Response.getDefaulTrueInstance();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("articleUuid", articleUuid);
		paramMap.put("openId", openId);
		
		String articleId = "";
		QlyRhShareVO articleVO = baseDAO.findOne(mapperNamespace + ".getArticleShareByArticleUuid", paramMap);
		if(articleVO != null) {
			articleId = articleVO.getUuid();
		}
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("articleId", articleId);
		result.setData(retMap);
		
		return result;
	}
	
	private QlyRhShareVO wxRecordTransformQlyRhShareVO(WxRecordDTO recordDTO) {
		QlyRhShareVO rhShareVO = new QlyRhShareVO();
		rhShareVO.setUuid(recordDTO.getArticle_id());
		rhShareVO.setArticle_uuid(recordDTO.getRh_articleId());
		rhShareVO.setArticle_title(recordDTO.getArticle_title());
		rhShareVO.setArticle_url(recordDTO.getArticle_url());
		rhShareVO.setSharer(recordDTO.getSharer());
		rhShareVO.setShare_date(DateUtil.formatDate(new Date()));
		
		return rhShareVO;
	}
	
	private void isSendMessageRemind(WxRecordDTO recordDTO) {
		if(StringUtils.isNotBlank(recordDTO.getSharer())) {
			String openId = recordDTO.getSharer();
			
			Response data = pubCommonService.getMessageRemindSet(openId);
			if(data.getData() != null) {
				Map<String, Object> dataMap = (Map) data.getData();
				String remindType = (String) dataMap.get("remind_type");
				String localUrl = "";
				
				String article_title = recordDTO.getArticle_title();
				//是否会员
				String isMember = userService.getIsMember(openId);
				if("1".equals(isMember)) {
					localUrl = frontendUrl + "#/readdetails?share_uuid=" + recordDTO.getArticle_id() + "&article_title=" + URLEncoder.encode(article_title) + "&userId=" + openId;
					localUrl += "&article_id=" + recordDTO.getRh_articleId();
				} else {
					localUrl = frontendUrl + "#/connections?userId=" + openId;
				}
				
				// 文章名称长度超过15个字符，截取掉用省略号显示
				if(article_title.length() > 15) {
					article_title = article_title.substring(0, 15) + "...";
				}
				String msgContent = "有人阅读了您分享的文章《" + article_title + "》，<a href='"+ localUrl +"'>点击查看</a>>>>";
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(openId);
				msgVO.setContent(msgContent);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				msgVO.setCreateTime(new Date());
				
				if("1".equals(remindType)) {// 立即提醒
					messageCenterService.sendMsg(msgVO);
				} else {
					// 勿扰时间段设置（23:00至7:00）
//					boolean flag = belongCalendar("23:00", "7:00");
//					logger.info("勿扰时间段设置:" + flag);
//					if(flag) {
//						queueService.addToQueue(msgVO);
//					}
					
				}
			}
			
		}
	}
	
	private boolean belongCalendar(String strBeginTime, String strEndTime) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
		Date nowTime = null;
		Date beginTime = null;
		Date endTime = null;
		boolean flag = false;
		try {
			nowTime = df.parse(df.format(new Date()));
			beginTime = df.parse(strBeginTime);
			endTime = df.parse(strEndTime);
			
			Calendar date = Calendar.getInstance();
	        date.setTime(nowTime);

	        Calendar begin = Calendar.getInstance();
	        begin.setTime(beginTime);

	        Calendar end = Calendar.getInstance();
	        end.setTime(endTime);
	        
	        if(date.after(begin) || date.before(end)) {
	        	flag = false;
	        } else {
	        	flag = true;
	        }
		} catch (Exception e) {
			logger.error("日期判断出错:" + e);
		}
		
        return flag;
    }
	
	@Override
	public void insertRecordClickArticle(QlyRhUserClickArticleVO vo) {
		baseDAO.add(mapperNamespace+".insertRecordClickArticle", vo);
	}
}
