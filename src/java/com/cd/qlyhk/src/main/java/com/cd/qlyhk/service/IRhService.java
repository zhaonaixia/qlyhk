package com.cd.qlyhk.service;

/**
 * 获客服务接口
 * @author sailor
 *
 */
public interface IRhService {

	final String BEAN_ID = "rhService";
	
	/**
	 * 定时任务下载文章
	 */
	void timingTaskReptileArticle();
}
