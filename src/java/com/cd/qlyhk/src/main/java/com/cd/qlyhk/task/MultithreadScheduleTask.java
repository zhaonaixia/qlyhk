package com.cd.qlyhk.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.service.IRhService;
import com.cd.qlyhk.service.IStatisticsService;
import com.cd.qlyhk.utils.DateUtil;

/**
 * 
 * @author sailor
 *
 */
//@Component注解用于对那些比较中立的类进行注释；
//相对与在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class MultithreadScheduleTask {

	private static final Logger logger = LoggerFactory.getLogger(MultithreadScheduleTask.class);
	
	@Autowired
	private IMessageCenterService messageCenterService;
	
	@Autowired
	private IRhService rhService;
	
	@Autowired
	private IStatisticsService statisticsService;
	
    @Async
    @Scheduled(cron = "0 0 3 ? * *") //每天的上午3点触发
    public void reptileArticle() {
    	logger.debug("******************************下载文章定时任务开始********************************************");
    	rhService.timingTaskReptileArticle();
    	logger.debug("******************************下载文章定时任务结束********************************************");
    }

//    @Async
//    @Scheduled(cron = "0 30 7 ? * *") //每天上午7:30触发(0 30 7 ? * *)
//    public void dailyOneSign() {
//    	logger.debug("******************************每日一签定时任务开始********************************************");
//    	messageCenterService.sendDailyOneSignMsg();
//    	logger.debug("******************************每日一签定时任务结束********************************************");
//    }
    @Async
    @Scheduled(cron = "0 15 8 ? * *") //每天上午8:15触发(0 15 8 ? * *)
    public void recordMsg1() {
    	logger.debug("******************************recordMsg1********************************************");
    	messageCenterService.sendRecordMsg(1);
    }
    
    @Async
    @Scheduled(cron = "0 30 8 ? * *") //每天上午8:30触发(0 30 8 ? * *)
    public void morningPaper() {
    	logger.debug("******************************每日早报定时任务开始********************************************");
    	messageCenterService.sendMorningpaperArticles();
    	logger.debug("******************************每日早报定时任务结束********************************************");
    	
    	/**
    	final Calendar c = Calendar.getInstance();
		int d = c.get(Calendar.DATE);
		logger.debug("当前日期：" + d);
		if(d == 1 || d % 4 == 0) {
			logger.debug("******************************群发财税早报定时任务开始********************************************");
	    	messageCenterService.sendMassMorningpaperArticles();
	    	logger.debug("******************************群发财税早报定时任务结束********************************************");
		}**/
    }
    
	@Async
	@Scheduled(cron = "0 0 10 ? * *") //每天上午10:00触发
    public void passengerFlowDataTask() {
		logger.debug("******************************客流数据通知定时任务开始********************************************");
		// 能发送普通消息就发送普通消息,不能则发送模板消息
		try {
//			messageCenterService.sendPassengerFlowMsg();
		} catch(Exception e) {
			logger.error("客流数据通知定时任务出错：" + e);
		}
		
		logger.debug("******************************客流数据通知定时任务结束********************************************");
    }
	
	@Async
    @Scheduled(cron = "0 30 11 ? * *") //每天上午11:30触发(0 30 11 ? * *)
    public void recordMsg2() {
    	logger.debug("******************************recordMsg2********************************************");
    	messageCenterService.sendRecordMsg(2);
    }
	
	@Async
    @Scheduled(cron = "0 30 14 ? * *") //每天14:30触发(0 30 14 ? * *)
    public void recordMsg3() {
    	logger.debug("******************************recordMsg3********************************************");
    	messageCenterService.sendRecordMsg(3);
    }
	
	@Async
    @Scheduled(cron = "0 30 17 ? * *") //每天17:30触发(0 30 17 ? * *)
    public void recordMsg4() {
    	logger.debug("******************************recordMsg4********************************************");
    	messageCenterService.sendRecordMsg(4);
    }
	
//	@Async
//    @Scheduled(cron = "0 0 17 ? * *")
//    public void testSendMassMsg() {
//		logger.debug("******************************群发财税早报定时任务开始********************************************");
//    	messageCenterService.sendMassMorningpaperArticles();
//    	logger.debug("******************************群发财税早报定时任务结束********************************************");
//    }
	
	@Async
    @Scheduled(cron = "0 30 19 ? * *") //每天19:30触发(0 30 19 ? * *)
    public void recordMsg5() {
    	logger.debug("******************************recordMsg5********************************************");
    	messageCenterService.sendRecordMsg(5);
    }
	
	@Async
    @Scheduled(cron = "0 30 21 ? * *") //每天21:30触发(0 30 21 ? * *)
    public void recordMsg6() {
    	logger.debug("******************************recordMsg6********************************************");
    	messageCenterService.sendRecordMsg(6);
    }
	
	@Async
    @Scheduled(cron = "0 0 23 ? * *") //每天23:00触发(0 00 23 ? * *)
    public void recordMsg7() {
    	logger.debug("******************************recordMsg7********************************************");
    	messageCenterService.sendRecordMsg(7);
    }
	
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?") //每隔一个小时触发(0 0 0/1 * * ?)
    public void cancelRestrict() {
    	logger.debug("******************************取消限制定时任务开始********************************************");
    	messageCenterService.sendCancelRestrictMsg();
    	logger.debug("******************************取消限制定时任务结束********************************************");
    	
    	logger.debug("******************************分销情况统计定时任务开始********************************************");
    	statisticsService.executeMarketStatistics();
    	logger.debug("******************************分销情况统计定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 30 14 ? * MON-FRI") //每隔3天推送一次，每次在14:30触发(0 30 14 ? * MON-FRI)
    public void followUpAccurately() {
    	final Calendar c = Calendar.getInstance();
		int d = c.get(Calendar.DATE);
		logger.debug("当前日期：" + d);
		if(d == 1 || d % 3 == 0) {
			logger.debug("******************************精准跟进定时任务开始********************************************");
	    	messageCenterService.sendFollowUpAccuratelyMsg();
	    	logger.debug("******************************精准跟进定时任务结束********************************************");
		}
		
		Date cuurentDt = new Date();
		Date dt = DateUtil.afterNDay(cuurentDt, 15);
		String endDate = DateUtil.formatDate(dt);
		logger.debug("获取15天后过期的会员：" + endDate);
		logger.debug("******************************会员续费定时任务开始********************************************");
    	messageCenterService.sendMembershipRenewalMsg(null, endDate, "1");
    	logger.debug("******************************会员续费定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 10 12 ? * MON") //每周一中午12点10分触发
    public void weekSummary() {
    	logger.debug("******************************周总结定时任务开始********************************************");
    	messageCenterService.sendWeekSummaryMsg();
    	logger.debug("******************************周总结定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 0 10 ? * SUN") //SUN每周日上午10点
    public void perfectInfo() {
    	logger.debug("******************************完善资料定时任务开始********************************************");
    	messageCenterService.sendPerfectInfoMsg();
    	logger.debug("******************************完善资料定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 15 18 ? * *") //每天下午18点15分触发(0 15 18 ? * *)
    public void subscribeArticle() {
		logger.debug("******************************推送订阅文章定时任务开始********************************************");
    	messageCenterService.sendNewSubscribeArticle();
    	logger.debug("******************************推送订阅文章定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 0 20 ? * *") //每天的晚上8点触发
    public void membershipRenewal() {
    	Date cuurentDt = new Date();
		String startDate = DateUtil.formatDate(cuurentDt);
		
		Date dt = DateUtil.afterNDay(cuurentDt, 3);
		String endDate = DateUtil.formatDate(dt);
		logger.debug("获取3天后过期的会员：" + endDate);
		logger.debug("******************************会员续费定时任务开始********************************************");
    	messageCenterService.sendMembershipRenewalMsg(startDate, endDate, "2");
    	logger.debug("******************************会员续费定时任务结束********************************************");
    }
    
    @Async
    @Scheduled(cron = "0 0/10 * * * ?") //每天的晚上23点触发（0 0 23 ? * *）（ 0 0/5 * * * ?）
    public void shareAndRecordStatistics() {
    	logger.debug("******************************统计定时任务开始********************************************");
    	statisticsService.executeReadArticleStatistics();
    	statisticsService.executeRecordStatistics();
    	statisticsService.executeArticleShareAndReadStatistics();
    	statisticsService.executeUserShareAndReadStatistics();
    	logger.debug("******************************统计定时任务结束********************************************");
    }
    
    @Async
    //@Scheduled(cron = "0 30 23 28-31 * ?") //每月最后一天的晚上23点30触发（0 30 23 ? * *）（0 0/8 * * * ?）
    @Scheduled(cron = "0 0 22 ? * *")
    public void monthlyShareAndRradStatistics() {
    	/*
    	final Calendar c = Calendar.getInstance();
    	if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
    		//是最后一天
    		logger.debug("******************************统计月度分享和阅读时段定时任务开始********************************************");
        	statisticsService.executeMonthlyShareStatistics();
        	statisticsService.executeMonthlyReadStatistics();
        	logger.debug("******************************统计月度分享和阅读时段定时任务结束********************************************");
        	
        	logger.debug("******************************统计月度文章和用户情况定时任务开始********************************************");
        	statisticsService.executeMonthlyArticlesStatistics();
        	statisticsService.executeMonthlyUsersStatistics();
        	logger.debug("******************************统计月度文章和用户情况定时任务结束********************************************");
    	}*/
    	
    	logger.debug("******************************统计月度分享和阅读时段定时任务开始********************************************");
    	statisticsService.executeMonthlyShareStatistics();
    	statisticsService.executeMonthlyReadStatistics();
    	logger.debug("******************************统计月度分享和阅读时段定时任务结束********************************************");
    	
    	logger.debug("******************************统计月度文章和用户情况定时任务开始********************************************");
    	statisticsService.executeMonthlyArticlesStatistics();
    	statisticsService.executeMonthlyUsersStatistics();
    	logger.debug("******************************统计月度文章和用户情况定时任务结束********************************************");
    	
    }
    
    @Async
    //@Scheduled(cron = "0 0 0 1 * ?")//每月1号凌晨0点触发
    //@Scheduled(cron = "0 30 23 ? * *")
    @Scheduled(cron = "0 0 0 1 * ?")
    public void monthlyStatistics() {
    	logger.debug("******************************统计月度情况报表定时任务开始********************************************");
    	statisticsService.executeMonthlyStatistics();
    	logger.debug("******************************统计月度情况报表定时任务结束********************************************");
    }
   
}
