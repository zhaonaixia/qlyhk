package com.cd.qlyhk.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.reptile.WeixinArticeCallable;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.ICompanyService;
import com.cd.qlyhk.service.IMorningPaperService;
import com.cd.qlyhk.service.IShareService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.utils.EventTypeUtil;
import com.cd.qlyhk.utils.SignUtil;
import com.cd.qlyhk.utils.WxEventRevertUtil;
import com.cd.qlyhk.utils.XMLParserUtil;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReceiveXml;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 微信回调控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/wx")
public class WxController {

	private static final Logger logger = LoggerFactory.getLogger(WxController.class);
	
	private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("sailor" + "-%d").setDaemon(true).build();
	
	@Value("${wx.app.token}")
	public String applicationToken;
	
	@Value("${wx.revert.req}")
	public String keywordResponse;
	
	@Value("${wx.revert.resp}")
	public String keywordContent;
	
	@Value("${frontend.url}")
	public String frontendUrl;
	
	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	@Value("${wx.appid}")
	public String appid;
	
	@Resource(name = IShareService.BEAN_ID)
	private IShareService   shareService;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService   userService;
	
	@Resource(name = IArticleService.BEAN_ID)
	private IArticleService articleService;
	
	@Resource(name = IMorningPaperService.BEAN_ID)
	private IMorningPaperService morningPaperService;
	
	@Resource(name = ICompanyService.BEAN_ID)
	private ICompanyService companyService;
	
	/**
     * 回调主入口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/main.do", produces = "application/json; charset=utf-8")
    public void main(HttpServletRequest request, HttpServletResponse response) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        response.setContentType("text/html; charset=utf-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        if (echostr != null) {
            logger.info("开发者中心接入请求参数：signature={},timestamp={},nonce={},echostr={}", signature, timestamp, nonce, echostr);
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (SignUtil.checkSignature(applicationToken, signature, timestamp, nonce)) {
                try {
                    response.getWriter().write(echostr);
                    response.getWriter().flush();
                    response.getWriter().close();
                } catch (IOException e) {
                    logger.error("开发者中心接入异常，原因：{}", e.getMessage(), e);
                }
            } else {
                logger.error("微信公众号调用开发者中心进行token校验失败");
            }
        } else {
        	String xmlS = XMLParserUtil.getXMLContent(request);
            ReceiveXml vo = XMLParserUtil.parseWXXML(xmlS);
            String openId = vo.getFromUser();// 发送方帐号（一个OpenID）
            String rawId = vo.getToUser();// 开发者微信号
            String xml = "";
            logger.info("微信推送的消息：{}" , vo.toString());

            try {
                if (EventTypeUtil.isSubscribe(vo)) {//订阅
                    logger.info("用户订阅：{}" , vo.toString());

                    if(vo.getEventKey().contains("qrscene") && !vo.getEventKey().contains("ci")){
                    	String eventKey = vo.getEventKey();
                    	String articleId = eventKey.split("_")[1];
                        //临时二维码关注推送文章
                    	QlyRhShareVO rhShareVO = shareService.scanQRCodeShareAarticle(openId, articleId, appid);
                    	
                    	if(rhShareVO != null) {
                    		xml = getRevertXml(vo, openId, articleId, rhShareVO);
                    	}
                        
                    } else if(vo.getEventKey().contains("qrscene") && vo.getEventKey().contains("ci")){
                        
                    } else {
                    	// 关注公众号
                		QlyRhUserVO userVO = userService.getUserVOByOpenId(openId, null, appid);
                        // 首次关注公众号推送一条消息
                    	xml = getFirstConcernRevertXml(vo);
                    }
                    
                    Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("openId", openId);
                    updateMap.put("msgType", vo.getMsgType());
                    updateMap.put("lastInteractTime", new Date());
                    updateMap.put("ispush", "0");
                    userService.addOrUpdateUserOperateLog(updateMap);
                } else if (EventTypeUtil.isUnSubscribe(vo)) {// 取消订阅
//                    wechatRelateService.deleteCustWechatRelate(vo.getFromUser());
                	QlyRhUserDTO updateUser = new QlyRhUserDTO();
    				updateUser.setOpenId(openId);
    				updateUser.setSubscribe(0);
    				
    				userService.updateUserInfo(updateUser);
    				
                    logger.info("用户取消订阅：{}" , vo.toString());
                } else if (EventTypeUtil.isClick(vo)) {// 菜单点击
                	logger.info("用户菜单点击：{}" , vo.toString());
                	
                	String eventKey = vo.getEventKey();
                	
                	if("V1001_TODAY_ARTICLE".equals(eventKey)) {
                		String replyContent = articleService.getPushArticle(openId);
                    	replyContent += "<a href='weixin://bizmsgmenu?msgmenucontent=换一批&msgmenuid=3'>✤✤✤换一批文章</a>";
                    	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                    	
                    	Map<String, Object> updateMap = new HashMap<String, Object>();
                        updateMap.put("openId", openId);
                        updateMap.put("msgType", eventKey);
                        updateMap.put("lastInteractTime", new Date());
                        updateMap.put("ispush", "0");
                        userService.addOrUpdateUserOperateLog(updateMap);
                	}
                } else if (EventTypeUtil.isScanParamCode(vo)) {//扫二维码事件
                    if(!vo.getEventKey().contains("ci")){
                        //临时二维码关注推送文章
                    	String eventKey = vo.getEventKey();
                    	QlyRhShareVO rhShareVO = shareService.scanQRCodeShareAarticle(openId, eventKey, appid);
                    	
                    	if(rhShareVO != null) {
                    		xml = getRevertXml(vo, openId, eventKey, rhShareVO);
                    	}
                    	
                    } else if(vo.getEventKey().contains("ci")){
                    	
                    }
                } else if ("VIEW".equals(vo.getEvent())) {//点击公众号的菜单链接

                } else if(EventTypeUtil.isTextMessge(vo)){// 文本消息
                	// 文本消息回复
                	String content = vo.getContent();
                    if(StringUtils.isNotBlank(keywordResponse) && StringUtils.isNotBlank(keywordContent)){
                        String[] keywordResponseArr = keywordResponse.split(",");
                        String[] keywordContentArr = keywordContent.split(",");
                        for (int i = 0; i < keywordResponseArr.length; i++){
                            if(keywordResponseArr[i].equals(content)){
                                xml = WxEventRevertUtil.revertTextBasicInfo(vo, keywordContentArr[i]);
                                break;
                            }
                        }
                        //图片回复
                    }
                    
                    if("查看".equals(content) || "取消限制".equals(content) || "换一批".equals(content)) {
                    	String replyContent = "";
                    	if("取消限制".equals(content)) {
                    		replyContent += "限制已取消，继续为您推送文章\n\n";
                    	}
                    	replyContent += articleService.getPushArticle(openId);
                    	replyContent += "<a href='weixin://bizmsgmenu?msgmenucontent=换一批&msgmenuid=3'>✤✤✤换一批文章</a>";
                    	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                    } else if(content.contains("https://mp.weixin.qq.com")) {
                    	String replyContent = "";
                    	synchronized (WxController.class) {
                    		logger.info("收到用户发送的文章地址：{}" , content);
                    		//是否会员
                			String isMember = userService.getIsMember(openId);
                			if("1".equals(isMember)) {
                				// 判断文章是否已经下载过了
                        		QlyRhReptileArticleVO articleVO = articleService.getQlyRhReptileArticleVO(content);
                        		if(articleVO != null) {
                        			logger.info("***********文章已下载*********************");
                        			
                        			String uuid = UUID.randomUUID().toString();
                        			String article_url = frontendUrl + "#/contents?rh_articleId=" + articleVO.getUuid() + "&articleId=" + uuid + "&userId=" + openId + "&getInto=listGetInto";
                        			replyContent = "文章<a href='"+ article_url +"'>《" + articleVO.getArticle_title() + "》</a>已加入您的名片\n\n";
                        			replyContent += "点击分享>>>";
                        			
                                	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                        		} else {
                        			String urls[] = {content};

                        			Callable<Map<String, String>> task = new WeixinArticeCallable(openId, articleImgUpload, articleImgUrl, urls);
                                	ExecutorService executorService = createFixedThreadPool();
                                	executorService.submit(task);
                                	
                                	replyContent += "文章创建中...";
                        			
                                	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                        		}
                			} else {
                				String localUrl = frontendUrl + "#/buymember?userId=" + openId;
                				replyContent += "很抱歉，<创建文章>功能仅对会员开放！\n\n";
                				replyContent += " <a href='"+ localUrl +"'>立即开通会员，享无限次创建文章>></a>";
                				xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                			}
                        	
                    	}
                    } else if("今日好文".equals(content)) {
                    	String replyContent = articleService.getPushSubscribeArticle(openId);
                    	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                    } else if("看看头条".equals(content)) {
                    	String replyContent = morningPaperService.getPushMorningPaperArticle(openId);
                    	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                    } else if("公司文章".equals(content)) {
                    	String replyContent = companyService.getPushNewTeamArticle(openId);
                    	xml = WxEventRevertUtil.revertTextBasicInfo(vo, replyContent);
                    }
                    
                    Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("openId", openId);
                    updateMap.put("msgType", vo.getMsgType());
                    updateMap.put("lastInteractTime", new Date());
                    updateMap.put("ispush", "0");
                    userService.addOrUpdateUserOperateLog(updateMap);
                } else if(EventTypeUtil.isImageMessge(vo)){ // 图片消息
                	Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("openId", openId);
                    updateMap.put("msgType", vo.getMsgType());
                    updateMap.put("lastInteractTime", new Date());
                    updateMap.put("ispush", "0");
                    userService.addOrUpdateUserOperateLog(updateMap);
                } else if(EventTypeUtil.isVoiceMessge(vo)){ // 语音消息
                	Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("openId", openId);
                    updateMap.put("msgType", vo.getMsgType());
                    updateMap.put("lastInteractTime", new Date());
                    updateMap.put("ispush", "0");
                    userService.addOrUpdateUserOperateLog(updateMap);
                } else {// 菜单回复
                	
                }
                if(StringUtils.isBlank(xml)){
//                    xml = WxEventRevertUtil.revertTextBasicInfo(vo, unifiedResponse);
                    xml = "success";
                }
                logger.info("返回微信内容：{}" , xml);

                response.getWriter().write(xml);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                logger.error("被动回复消息异常，原因：{}", e.getMessage(), e);
            } catch (Exception e) {
                logger.error("被动回复消息异常，原因：{}", e.getMessage(), e);
            }
        }
    }
    
    private String getRevertXml(ReceiveXml vo, String openId, String articleId, QlyRhShareVO rhShareVO) {
		/*
		 * String article_url = rhShareVO.getArticle_url();
		 * if(StringUtils.isNotBlank(rhShareVO.getSource()) &&
		 * !"0".equals(rhShareVO.getSource())) { article_url =
		 * article_url.replace("userId=" + rhShareVO.getSource(), "userId=" + openId); }
		 * else { article_url = article_url.replace("userId=", "userId=" + openId); } //
		 * 文章的UUID设置 article_url = article_url.replace("articleId=" + articleId,
		 * "articleId=" + rhShareVO.getUuid()); // 截取掉微信自动带上的参数
		 * if(article_url.indexOf("?type=weixin") > -1) { article_url =
		 * article_url.substring(0, article_url.indexOf("?type=weixin"));
		 * logger.info("article_url：{}" , article_url); }
		 */
    	String article_url = frontendUrl + "#/contents?rh_articleId=" + rhShareVO.getArticle_uuid() + "&articleId=" + rhShareVO.getUuid() + "&userId=" + openId + "&getInto=listGetInto";
//		article_url = URLEncoder.encode(article_url);
//		String redirectUrl = WxUtil.GetRedirectURL(article_url);
//		logger.info("redirectUrl：{}" , redirectUrl);
		
		String content = "您的第一篇文章：\n\n<a href='"+ article_url +"'>《" + rhShareVO.getArticle_title() + "》</a>\n\n点击领取";
//		vo.setTitle(rhShareVO.getArticle_title());
//		vo.setUrl(redirectUrl);
//		vo.setPicUrl("http://www.finway.com.cn/share/images/logo.png");
//		String xml = WxEventRevertUtil.revertQrCodeEvent(vo);
		String xml = WxEventRevertUtil.revertTextBasicInfo(vo, content);
		return xml;
    }
    
    public static ExecutorService createFixedThreadPool() {
        int poolSize = 5;
        int queueSize = 10;
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }
    
    private String getFirstConcernRevertXml(ReceiveXml vo) {
    	String userInfoUrl = frontendUrl + "#/business?openId=" + vo.getFromUser() + "&linkJump=xx";
    	String shareUrl = frontendUrl + "#/getarticle?openId=" + vo.getFromUser();
    	String createArticleUrl = frontendUrl + "#/articlesIndex";
    	String connectionsUrl = frontendUrl + "#/connections?userId=" + vo.getFromUser();
    	String buymemberUrl = frontendUrl + "#/buymember?userId=" + vo.getFromUser();
    	
    	/** 2020-07-01 注释 */
		String content = "欢迎您！财税千里眼，财税界获客第一神器，助您朋友千里阅文，顺风而来万客。每天为您推荐获客好文，分享转发自带您的专属名片。朋友圈谁看了，第一时间消息提醒，社交资源精准转化为意向客户！\n\n";
		content += "1.【个人名片】\n";
		content += "   完善个人信息，方便客户联系你！\n";
		content += "<a href='"+ userInfoUrl +"'>点我完善个人信息</a>\n\n";
		
		content += "2.【分享软文】\n";
		content += "   转发分享获客好文，自带你的名片！\n";
		content += "<a href='"+ shareUrl +"'>点我分享</a>\n\n";
		
		
		content += "3.【创建文章】\n";
		content += "   1分钟创建你的文章！\n";
		content += "<a href='"+ createArticleUrl +"'>点我创建文章</a>\n\n";
		
		
		content += "4.【谁看过我】\n";
		content += "   帮你挖掘朋友圈意向客户，分析客户意向需要！\n";
		content += "<a href='"+ connectionsUrl +"'>谁看过我</a>\n\n";
		
		
		content += "5.【开通会员】\n";
		content += "   开通会员，精准跟进，助你轻松签单！\n";
		content += "<a href='"+ buymemberUrl +"'>点我开通会员</a>\n\n";
		
		content += "6.【今日财税头条】\n";
		content += "   最新鲜热辣的财税资讯、营销爆文，助你轻松签单！\n\n";
		content += "👇👇👇\n";
		
//    	String content = "欢迎您！财税千里眼，财税界获客第一神器，助您朋友千里阅文，顺风而来万客。每天为您推荐获客好文，分享转发自带您的专属名片。朋友圈谁看了，第一时间消息提醒，社交资源精准转化为意向客户！"
//    			+ "<a href='weixin://bizmsgmenu?msgmenucontent=看看头条&msgmenuid=3'>开始分享>>></a>\n\n";
    	
		String xml = WxEventRevertUtil.revertTextBasicInfo(vo, content);
		return xml;
    }
}
