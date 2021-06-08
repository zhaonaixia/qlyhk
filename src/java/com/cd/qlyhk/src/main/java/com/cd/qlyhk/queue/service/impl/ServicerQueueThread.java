package com.cd.qlyhk.queue.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cd.qlyhk.queue.service.ServicerQueue;

public class ServicerQueueThread extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(ServicerQueueThread.class);
	ServicerQueue queue = null;
	
	public ServicerQueueThread(ServicerQueue queue) {
		this.queue = queue;
	}

	public void run() {
		logger.info("**********************消息队列线程启动*************************************** ");
		while (true) {
			try {
				logger.info("*************ServicerQueueThread**************" + System.currentTimeMillis());
				if (this.queue != null) {
					this.queue.checkQueue();
				}

				long time = 10 * 60 * 1000;
				Thread.sleep(time);// 休眠
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
