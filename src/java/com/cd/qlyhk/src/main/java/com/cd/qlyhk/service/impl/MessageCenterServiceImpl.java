package com.cd.qlyhk.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.dto.QlyRhCompanyDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.ICompanyService;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IMorningPaperService;
import com.cd.qlyhk.service.IOrderService;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.QlyRhMessageRemindSetVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.SendMessageVO;

/**
 * 消息中心接口实现类
 * @author sailor
 *
 */
@Service(IMessageCenterService.BEAN_ID)
public class MessageCenterServiceImpl implements IMessageCenterService {

	private static final Logger logger = LoggerFactory.getLogger(MessageCenterServiceImpl.class);
	
	@Value("${message.passengerFlow.templateId}")
	private String passengerFlowTemplateId;
	
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Autowired
	private IPubCommonService pubCommonService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private IMorningPaperService  morningPaperService;
	
//	@Autowired
//	private IOrderService orderService;
	
	@Autowired
	private IDataCacheService dataCacheService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Override
	public String sendMsg(SendMessageVO vo) {
		String accessToken = pubCommonService.getCacheAccessToken();
		String result = null;
		
		if(vo.getMsgType() == MessageConstant.MSG_TYPE_PTXX) {
			result = WxUtil.SendMessage(accessToken, vo.getTouser(), vo.getContent());
		} else if(vo.getMsgType() == MessageConstant.MSG_TYPE_MBXX) {
			result = WxUtil.SendTemplateMessage(accessToken, vo.getTouser(), vo.getTemplateId(), vo.getUrl(), vo.getData());
		}
		
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			String errcode = json.getString("errcode");
			if("0".equals(errcode)) {
				result = "succs";
			}
		}
		
		logger.info("给{}发送消息：{}", vo.getTouser(), vo.getContent());
		logger.info("发送消息结果返回：{}", result);
		if(StringUtils.isNotBlank(result) && !"succs".equals(result)) {
			//考虑access_token失效了，需要删除缓存里面的重新获取
			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON != null && resultJSON.getIntValue("errcode") == 40001) {
				String errMsg = resultJSON.getString("errmsg");
				if (errMsg.contains("access_token is invalid")) {
					logger.info("*************[sendMsg]清除缓存AccessToken*************");
					dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
					dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
				}
			}
		}
		
		return result;
	}

	@Override
	public void sendTestMsg(String name) {
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setTouser("oSVdLv05qzm-bo26hSxuFBZV-EoU");
		msgVO.setContent("测试定时任务-" + name);
		msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
		msgVO.setCreateTime(new Date());
		
		sendMsg(msgVO);
	}
	
	
	@Override
	public void sendMorningMsg() {
		Date dt = new Date();
		String dateStr = DateUtil.formatDateByFormat(dt, "MM月dd日");
		
		List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String content = "  今天是" + dateStr + "\n\n";
				content += user.getUser_name() + "早上好！\n\n";
				content += "您定制的产品文章有更新哦！\n\n";
				content += "每一次分享，都会有收获~\n\n";
				content += "<a href='weixin://bizmsgmenu?msgmenucontent=查看&msgmenuid=2'>查看>></a>";
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(user.getOpenId());
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				msgVO.setCreateTime(dt);
				
				sendMsg(msgVO);
			}
		}
	}
	
	@Override
	public void sendCancelRestrictMsg() {
		Date dt = new Date();
		// 客户48小时时限即将到限的前1小时
		Date startDt = DateUtil.afterNHours(dt, -47);
		String startDate = DateUtil.formatDateByFormat(startDt, "yyyy-MM-dd HH:mm:ss");
		logger.info("查询快到48小时未互动的用户：" + startDate);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dateTime", startDate);
		
		List<Map<String, Object>> resultList = userService.queryCancelRestrictUsers(param);
		if(resultList != null && resultList.size() > 0) {
			for(Map<String, Object> map : resultList) {
				String ispush = map.get("ispush") == null ? "0" : (String) map.get("ispush");
				// 0表示未推送过此消息
				if("0".equals(ispush)) {
					String userName = map.get("user_name") == null ? "" : (String) map.get("user_name");
					String openId = map.get("openId") == null ? "" : (String) map.get("openId");
					String content = userName + "，您已经2天没有搭理我了\n\n";
					content += "由于微信平台规则的限制，您可能会无法收到我们推送给您的客户消息\n\n";
					content += "✤<a href='weixin://bizmsgmenu?msgmenucontent=取消限制&msgmenuid=1'>点我取消限制</a>\n";
					
					SendMessageVO msgVO = new SendMessageVO();
					msgVO.setTouser(openId);
					msgVO.setContent(content);
					msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
					msgVO.setCreateTime(dt);
					
					sendMsg(msgVO);
					
					Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("openId", openId);
                    updateMap.put("ispush", "1");
                    userService.addOrUpdateUserOperateLog(updateMap);
				}
				
			}
		}
	}
	
	@Override
	public void sendWeekSummaryMsg() {
		Date dt = new Date();
		// 上周累计分享数
		Date startDt = DateUtil.afterNDay(dt, -7);
		Date endDt = DateUtil.afterNDay(dt, -1);
		String startDate = DateUtil.formatDateByFormat(startDt, "yyyy-MM-dd");
		String endDate = DateUtil.formatDateByFormat(endDt, "yyyy-MM-dd");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("share_startDate", startDate);
		param.put("share_endDate", endDate);
		
		NumberFormat nf = new DecimalFormat("0.##");
		List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				param.put("openId", user.getOpenId());
				int totalShareNum = articleService.getTotalWeekShareNum(param);
				double d1 = totalShareNum;
				double d2 = 10;
				double bfb = d1/d2;
				if(bfb < 0) {
					bfb = 0;
				} if(bfb >= 100) {
					bfb = 99;
				}
				String content = "上周，您分享了"+ totalShareNum +"篇文章，获得了"+ totalShareNum +"次客户跟进机会，超过了全国"+ nf.format(bfb) +"%的销售人员。继续努力！\n\n";
				content += "获客从朋友圈开始，多分享优质文章，多互动，建立一个专业可信赖的形象，你一定会成功！\n\n";
				content += "点我【获客好文】，看最新鲜热辣的财税资讯\n";
				content += "👇👇👇";
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(user.getOpenId());
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				msgVO.setCreateTime(dt);
				
				sendMsg(msgVO);
			}
		}
	}
	
	@Override
	public void sendPassengerFlowMsg() {
		Date dt = new Date();
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setMsgType(MessageConstant.MSG_TYPE_MBXX);
		msgVO.setTemplateId(passengerFlowTemplateId);
		msgVO.setUrl("");
		
		int num = getRandomNumber(1000000);
		int num2 = getRandomNumber(100000);
		String content = "昨天有" + num + "老师通过【谁看过我】功能，获取了" + num2 + "位意向客户";
		JSONObject resultJson = new JSONObject();
		resultJson.put("value", content);
		resultJson.put("color", "#173177");
		
		JSONObject dataTimeJson = new JSONObject();
		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy-MM-dd HH:mm:ss"));
		dataTimeJson.put("color", "#459ae9");
		
		JSONObject passengerFlowJson = new JSONObject();
		String passengerFlowContent = "昨天有" + num + "老师通过【谁看过我】功能，获取了" + num2 + "位意向客户";
		passengerFlowJson.put("value", passengerFlowContent);
		passengerFlowJson.put("color", "#459ae9");
		
		JSONObject remarkJson = new JSONObject();
		remarkJson.put("value", "点我下方【谁看过我】菜单，获取意向客户");
		remarkJson.put("color", "#173177");
		
		JSONObject data = new JSONObject();
		data.put("first", resultJson);
		data.put("keyword1", dataTimeJson);
		data.put("keyword2", passengerFlowJson);
		data.put("remark", remarkJson);
		msgVO.setData(data.toJSONString());
		
		List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				msgVO.setTouser(user.getOpenId());
				sendMsg(msgVO);
			}
		}
	}
	
	@Override
	public void sendFollowUpAccuratelyMsg() {
		List<String> sendOpenIds = new ArrayList<String>();
		Date dt = new Date();
		List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String openId = user.getOpenId();
				//是否会员
				String isMember = userService.getIsMember(openId);
				
				if("0".equals(isMember)) {
					sendOpenIds.add(openId);
				}
			}
		}
		
		if(sendOpenIds.size() > 0) {
			for(String toOpenId : sendOpenIds) {
				String localUrl = frontendUrl + "#/buymember?userId=" + toOpenId;
				String content = " <a href='"+ localUrl +"'>不要错过这个精准发现意向客户的机会哦！</a>\n\n";
				content += " <a href='"+ localUrl +"'>开通会员，找到他>></a>";
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(toOpenId);
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				msgVO.setCreateTime(dt);
				
				sendMsg(msgVO);
			}
		}
	}
	
	@Override
	public void sendCreateArticleMsg(String openId, String articleId, String articleTitle) {
		String uuid = UUID.randomUUID().toString();
		String article_url = frontendUrl + "#/contents?rh_articleId=" + articleId + "&articleId=" + uuid + "&userId=" + openId + "&getInto=listGetInto";
		String content = "文章<a href='"+ article_url +"'>《" + articleTitle + "》</a>已加入您的名片\n\n";
		content += "点击分享>>>";
		
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setTouser(openId);
		msgVO.setContent(content);
		msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
		
		sendMsg(msgVO);
	}
	
	private int getRandomNumber(int t) {
		Random r = new Random();
		int num = r.nextInt(t);
		return num;
	}

	@Override
	public void sendMorningpaperArticles() {
		Date dt = new Date();
		String mp_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		
		int pushCount = morningPaperService.getMorningPaperArticleCount(mp_date);
		if(pushCount > 0) {
			String content = "今日财税头条。快看看吧~\n";
			content += "<a href='weixin://bizmsgmenu?msgmenucontent=看看头条&msgmenuid=3'>戳我看看</a>";
			
			List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
			if(userList != null && userList.size() > 0) {
				for(QlyRhUserVO user : userList) {
					SendMessageVO msgVO = new SendMessageVO();
					String openId = user.getOpenId();
					
					msgVO.setTouser(openId);
					msgVO.setContent(content);
					msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
					
					sendMsg(msgVO);
				}
			}
		}
		
	}
	
	@Override
	public void sendPerfectInfoMsg() {
		List<QlyRhUserVO> userList = userService.queryNotPerfectInfoUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String userInfoUrl = frontendUrl + "#/business?openId=" + user.getOpenId() + "&linkJump=xx";
				String content = "为了提升客户信任度，90%的老师都已经完善个人名片，请您尽快完善您的个人名片。\n";
				content += "<a href='"+ userInfoUrl +"'>点我去完善名片</a>";
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(user.getOpenId());
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				
				sendMsg(msgVO);
			}
		}
	}
	
	@Override
	public void sendMembershipRenewalMsg(String startDate, String endDate, String interval) {
		List<QlyRhUserVO> userList = userService.queryMembershipRenewalUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				int userId = user.getUser_id();
				String openId = user.getOpenId();
				
				// 查询用户最近一次订单
				boolean validOrder = false;
//				QlyRhOrderVO orderVO = orderService.getQlyRhOrderLately(userId);
				if(StringUtils.isNotBlank(user.getMember_end_date())) {
					logger.info("*****************会员续期判断************************{},{},{},{}", openId, startDate, endDate, user.getMember_end_date());
					validOrder = judgeOrderIfOverdue(startDate, endDate, user.getMember_end_date());
				}
				
				if(validOrder) {
					String buymemberUrl = frontendUrl + "#/buymember?userId=" + openId;
					String content = user.getUser_name() + "老师，您的会员资格即将到期，为了确保您跟进意向客户不中断，请您尽快续费！ \n\n";
					content += "<a href='"+ buymemberUrl +"'>点我去续费</a>";
					
					if("1".equals(interval)) {
						//判断是否已经推送过消息
						Map<String, Object> userOperateLog = userService.queryQlyRhUserOperateLog(openId);
						if(userOperateLog != null && !userOperateLog.isEmpty()) {
							String ispush_mr = userOperateLog.get("ispush_mr") == null ? "0" : (String) userOperateLog.get("ispush_mr");
							if("0".equals(ispush_mr)) {
								SendMessageVO msgVO = new SendMessageVO();
								msgVO.setTouser(openId);
								msgVO.setContent(content);
								msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
								
								sendMsg(msgVO);
								
								Map<String, Object> updateMap = new HashMap<String, Object>();
			                    updateMap.put("openId", openId);
			                    updateMap.put("ispush_mr", "1");
			                    userService.addOrUpdateUserOperateLog(updateMap);
							}
						}
					} else {
						SendMessageVO msgVO = new SendMessageVO();
						msgVO.setTouser(openId);
						msgVO.setContent(content);
						msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
						
						sendMsg(msgVO);
					}
				}
				
			}
		}
	}
	
	@Override
	public void sendNewSubscribeArticle() {
		String content = "工作一天辛苦啦，今日奉上几篇好文。快看看吧~\n";
		content += "<a href='weixin://bizmsgmenu?msgmenucontent=今日好文&msgmenuid=3'>戳我看看</a>";
		
		List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String openId = user.getOpenId();
				int userId = user.getUser_id();
				
				int pushCount = articleService.getPushSubscribeArticleCount(userId);
				if(pushCount > 0) {
					// 判断用户的消息设置里面，是否已经打开订阅文章推送开关
					QlyRhMessageRemindSetVO msgSetVO = pubCommonService.getMessageRemindSetVOByUserId(userId);
					String rec_sub_article = "";
					if(msgSetVO != null) {
						String push_type = msgSetVO.getPush_type();
						JSONArray push_settings = JSONObject.parseArray(push_type);
						for(int i = 0; i < push_settings.size(); i++) {
							JSONObject jsonObj = push_settings.getJSONObject(i);
							String subSet = jsonObj.getString("rec_sub_article");
							if(StringUtils.isNotBlank(subSet)) {
								rec_sub_article = subSet;
							}
						}
					}
					
					if("1".equals(rec_sub_article)) {
						SendMessageVO msgVO = new SendMessageVO();
						msgVO.setTouser(openId);
						msgVO.setContent(content);
						msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
						
						sendMsg(msgVO);
					}
					
				}
				
			}
		}
		
	}
	
	@Override
	public void sendNewTeamArticleMsg(int companyId) {
		String content = "您的公司有新文章发布，快看看吧~\n";
		content += "<a href='weixin://bizmsgmenu?msgmenucontent=公司文章&msgmenuid=3'>戳我看看</a>";
		/*
		List<Map<String, Object>> userList = companyService.queryTeamMemberList(companyId);
		if(userList != null && userList.size() > 0) {
			for(Map<String, Object> m : userList) {
				String openId = (String) m.get("openId");
				
				SendMessageVO msgVO = new SendMessageVO();
				msgVO.setTouser(openId);
				msgVO.setContent(content);
				msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
				
				sendMsg(msgVO);
			}
			
		}*/
		QlyRhCompanyDTO companyDTO = new QlyRhCompanyDTO();
		companyDTO.setCompany_id(companyId);
		Response resp = companyService.queryCompanyAllUser(companyDTO);
		if("0".equals(resp.getCode())) {
			Map<String, Object> resultMap = (Map) resp.getData();
			if(resultMap != null && !resultMap.isEmpty()) {
				List<Map<String, Object>> userList = (List) resultMap.get("staffList");
				
				if(userList != null && userList.size() > 0) {
					for(Map<String, Object> m : userList) {
						String openId = (String) m.get("openId");
						
						SendMessageVO msgVO = new SendMessageVO();
						msgVO.setTouser(openId);
						msgVO.setContent(content);
						msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
						
						sendMsg(msgVO);
						
					}
				}
				
			}
			
		}
	}
	
	@Override
	public void sendDailyOneSignMsg() {
		Date dt = new Date();
		String today = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
		
		String accessToken = pubCommonService.getCacheAccessToken();
		String materialResult = WxUtil.GetMaterialList(accessToken);
		String mediaId = getMediaId(materialResult, today);
		
		if(StringUtils.isNotBlank(mediaId)) {
			logger.info("*****************推送每日一签************************{}", today);
			List<QlyRhUserVO> userList = userService.queryQlyRhUsers();
			if(userList != null && userList.size() > 0) {
				for(QlyRhUserVO user : userList) {
					String openId = user.getOpenId();
					
					String result = WxUtil.SendImageMessage(accessToken, openId, mediaId);
					logger.debug("*****************推送每日一签结果************************{},{}", openId, result);
				}
			}
			
		}
	}
	
	@Override
	public void sendRecordMsg(int timeNode) {
		Date dt = new Date();
		List<QlyRhUserVO> userList = userService.queryDNDUsers();
		if(userList != null && userList.size() > 0) {
			for(QlyRhUserVO user : userList) {
				String openId = user.getOpenId();
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("openId", openId);
				
				String content = "";
				String connectionsUrl = frontendUrl + "#/connections?userId=" + openId;
				if(timeNode == 1) {// 早上8点15分
					Date yesterday = DateUtil.afterNDay(dt, -1);
					String startTime = DateUtil.formatDate(yesterday) + " 00:00:00";
					String endTime = DateUtil.formatDate(yesterday) + " 23:59:59";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "早上好，记得吃早餐哦~\n";
					content += "昨夜有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 2) {// 中午11点30分
					String startTime = DateUtil.formatDate(dt) + " 00:00:00";
					String endTime = DateUtil.formatDate(dt) + " 11:30:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "中午好，别忘了点外卖~\n";
					content += "上午有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 3) {// 下午14点30分
					String startTime = DateUtil.formatDate(dt) + " 11:30:00";
					String endTime = DateUtil.formatDate(dt) + " 14:30:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "下午好，喝杯茶提提神~\n";
					content += "中午有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 4) {// 下午17点30分
					String startTime = DateUtil.formatDate(dt) + " 14:30:00";
					String endTime = DateUtil.formatDate(dt) + " 17:30:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "快下班啦，回家路上注意安全~\n";
					content += "下午有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 5) {// 晚上19点30分
					String startTime = DateUtil.formatDate(dt) + " 17:30:00";
					String endTime = DateUtil.formatDate(dt) + " 19:30:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "晚上好，工作一天辛苦啦~\n";
					content += "刚刚有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 6) {// 晚上21点30分
					String startTime = DateUtil.formatDate(dt) + " 19:30:00";
					String endTime = DateUtil.formatDate(dt) + " 21:30:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "入夜了，今天过得怎么样~\n";
					content += "晚上有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else if(timeNode == 7) {// 晚上23点00分
					String startTime = DateUtil.formatDate(dt) + " 21:30:00";
					String endTime = DateUtil.formatDate(dt) + " 23:00:00";
					paramMap.put("startTime", startTime);
					paramMap.put("endTime", endTime);
					
					content = "晚安好梦，早些休息~\n";
					content += "睡前有#x#人看了您分享的文章，<a href='"+ connectionsUrl +"'>点击查看</a>详情";
				} else {
					return ;
				}
				
				int total_readers = articleService.getTotalReadersByTimeQuantum(paramMap);
				if(total_readers > 0) {
					content = content.replaceAll("#x#", String.valueOf(total_readers));
					SendMessageVO msgVO = new SendMessageVO();
					msgVO.setTouser(openId);
					msgVO.setContent(content);
					msgVO.setMsgType(MessageConstant.MSG_TYPE_PTXX);
					
					sendMsg(msgVO);
				}
			}
		}
		
	}
	
	private boolean judgeOrderIfOverdue(String startDate, String endDate, String orderEndDate) {
		boolean result = false;
		Date dt1 = DateUtil.parseDate(startDate);
		Date dt2 = DateUtil.parseDate(endDate);
		Date dt3 = DateUtil.parseDate(orderEndDate);

		long t1 = dt1.getTime();
		long t2 = dt2.getTime();
		long t3 = dt3.getTime();
		
		if(t3 >= t1 && t3 <= t2) {
			result = true;
		}
		
		return result;
	}
	
	private String getMediaId(String materialResult, String name) {
		String mediaId = "";
		
		if(StringUtils.isNotBlank(materialResult)) {
			JSONObject json = JSONObject.parseObject(materialResult);
			JSONArray arry = json.getJSONArray("item");
			for(int i = 0; i < arry.size(); i++) {
				JSONObject item = arry.getJSONObject(i);
				String media_name = item.getString("name");
				if(name.equals(media_name)) {
					mediaId = item.getString("media_id");
					break;
				}
			}
		}
		
		return mediaId;
	}
	
	@Override
	public void sendMassMorningpaperArticles() {
		Date dt = new Date();
		// 查询超过48小时未互动的用户
		Date startDt = DateUtil.afterNHours(dt, -48);
		String startDate = DateUtil.formatDateByFormat(startDt, "yyyy-MM-dd HH:mm:ss");
		logger.info("查询超过48小时未互动的用户：" + startDate);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dateTime", startDate);
		
		List<Map<String, Object>> resultList = userService.queryCancelRestrictUsers(param);
		if(resultList != null && resultList.size() > 0) {
			List<String> tousers = new ArrayList<String>();
			
			for(Map<String, Object> map : resultList) {
				String openId = map.get("openId") == null ? "" : (String) map.get("openId");
				tousers.add(openId);
			}
			
			String mp_date = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd");
			List<Map<String, Object>> articlesList = morningPaperService.queryMorningPaperArticleList(mp_date);
	        
			String content = "";
			if(articlesList != null && articlesList.size() > 0) {
				content += "今日财税头条\n";
				for(Map<String, Object> map : articlesList) {
//					String article_uuid = (String) map.get("article_uuid");
	    			String article_title = (String) map.get("article_title");
//	    			String articleId = UUID.randomUUID().toString();
					
					content += "✬" + article_title + "\n\n";
				}
				content += "👇👇👇 更多点我【今日好文】\n";
			}
			
			if(tousers.size() > 1 && StringUtils.isNotBlank(content)) {
				sendMassMsg(tousers, content);
			}
			
		}
	}
	
	@Override
	public String sendMassMsg(List<String> tousers, String content) {
		String accessToken = pubCommonService.getCacheAccessToken();
		String result = null;
		
		JSONObject json = new JSONObject();
		json.put("touser", tousers);
		json.put("msgtype", "text");
		
		JSONObject textJson = new JSONObject();
		textJson.put("content", content);
		json.put("text", textJson);
		
		logger.info("群发消息请求参数：{}", json.toString());
		result = WxUtil.SendMassMessage(accessToken, json.toString());
		logger.info("群发消息结果返回：{}", result);
		
		if(StringUtils.isNotBlank(result)) {
			JSONObject retJSON = JSONObject.parseObject(result);
			String errcode = retJSON.getString("errcode");
			if("0".equals(errcode)) {
				result = "succs";
			}
		}
		
		if(StringUtils.isNotBlank(result) && !"succs".equals(result)) {
			//考虑access_token失效了，需要删除缓存里面的重新获取
			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON != null && resultJSON.getIntValue("errcode") == 40001) {
				String errMsg = resultJSON.getString("errmsg");
				if (errMsg.contains("access_token is invalid")) {
					logger.info("*************[sendMsg]清除缓存AccessToken*************");
					dataCacheService.del(DataCacheConst.CS_cache_sys_token_key);
					dataCacheService.del(DataCacheConst.CS_cache_sys_ticket_key);
				}
			}
		}
		
		return result;
	}
}
