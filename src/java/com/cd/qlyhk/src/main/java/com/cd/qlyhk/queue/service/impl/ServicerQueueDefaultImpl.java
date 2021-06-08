package com.cd.qlyhk.queue.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.queue.service.ServicerQueue;
import com.cd.qlyhk.service.IMessageCenterService;
import com.cd.qlyhk.vo.SendMessageVO;

@Service(ServicerQueue.BEAN_ID)
public class ServicerQueueDefaultImpl implements ServicerQueue {

	private static final Logger logger = LoggerFactory.getLogger(ServicerQueueDefaultImpl.class);
	
//	private static final ServicerQueue instance = new ServicerQueueDefaultImpl();
	
	List<SendMessageVO> queue = new ArrayList<SendMessageVO>();

	ServicerQueueThread queueThread = new ServicerQueueThread(this);
	
	@Autowired
	private IMessageCenterService messageCenterService;

//	public static ServicerQueue getInstance() {
//		logger.info("**********************11111111***************************************");
//		return instance;
//	}

	private ServicerQueueDefaultImpl() {
//		logger.info("**********************22222222***************************************");
//		this.queueThread = new ServicerQueueThread(this);
//		this.queueThread.start();
//		logger.info("**********************消息队列线程启动*************************************** ");
	}
	
	public void addToQueue(SendMessageVO msgVO) {
		this.queue.add(msgVO);
		logger.info("**********************加入队列，当前队列数量*************************************** " + queue.size());
	}

	public void removeFromQueue(SendMessageVO msgVO) {
		if(queue.size() > 0) {
			this.queue.remove(msgVO);
			logger.info("**********************移除队列，当前队列数量*************************************** " + queue.size());
		}
	}

	public synchronized void checkQueue() {
		List<SendMessageVO> tempList = this.queue.stream().collect(Collectors.toList());
		for (int i = 0; i < tempList.size(); i++) {
			SendMessageVO msgVO = tempList.get(i);
			String ret = messageCenterService.sendMsg(msgVO);
			if("succs".equals(ret)) {
				removeFromQueue(msgVO);
			}
		}
	}
}
