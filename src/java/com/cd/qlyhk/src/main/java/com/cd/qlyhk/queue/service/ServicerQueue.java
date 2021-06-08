package com.cd.qlyhk.queue.service;

import com.cd.qlyhk.vo.SendMessageVO;

/**
 * 队列接口
 * @author sailor
 *
 */
public abstract interface ServicerQueue {

	final String BEAN_ID = "queueService";
	
	public abstract void addToQueue(SendMessageVO msgVO);

	public abstract void removeFromQueue(SendMessageVO msgVO);

	public abstract void checkQueue();
}
