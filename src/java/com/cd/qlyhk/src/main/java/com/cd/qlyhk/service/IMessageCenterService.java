package com.cd.qlyhk.service;

import java.util.List;

import com.cd.qlyhk.vo.SendMessageVO;

/**
 * 消息中心接口
 * @author sailor
 *
 */
public interface IMessageCenterService {

	final String BEAN_ID = "msgService";
	
	/**
	 * 发送消息
	 * @param vo
	 */
	String sendMsg(SendMessageVO vo);
	
	/**
	 * 测试发送消息
	 * @param vo
	 */
	void sendTestMsg(String name);
	
	/**
	 * 推送早上好消息
	 */
	void sendMorningMsg();
	
	/**
	 * 推送取消限制消息
	 */
	void sendCancelRestrictMsg();
	
	/**
	 * 推送周总结消息
	 */
	void sendWeekSummaryMsg();
	
	/**
	 * 推送客流数据通知消息
	 */
	void sendPassengerFlowMsg();
	
	/**
	 * 推送精准跟进消息
	 */
	void sendFollowUpAccuratelyMsg();
	
	/**
	 * 推送生成文章链接消息
	 */
	void sendCreateArticleMsg(String openId, String articleId, String articleTitle);
	
	/**
	 * 推送财税早报文章
	 */
	void sendMorningpaperArticles();
	
	/**
	 * 推送完善资料消息
	 */
	void sendPerfectInfoMsg();
	
	/**
	 * 推送会员续期消息
	 */
	void sendMembershipRenewalMsg(String startDate, String endDate, String interval);
	
	/**
	 * 推送订阅文章
	 */
	void sendNewSubscribeArticle();
	
	/**
	 * 推送公司新文章
	 * @param companyId
	 */
	void sendNewTeamArticleMsg(int companyId);
	
	/**
	 * 推送每日一签消息
	 */
	void sendDailyOneSignMsg();
	
	/**
	 * 推送文章阅读情况消息
	 */
	void sendRecordMsg(int timeNode);
	
	/**
	 * 定时推送群发财税早报消息
	 */
	void sendMassMorningpaperArticles();
	
	/**
	 * 群发消息
	 * @param vo
	 */
	String sendMassMsg(List<String> tousers, String content);
}
