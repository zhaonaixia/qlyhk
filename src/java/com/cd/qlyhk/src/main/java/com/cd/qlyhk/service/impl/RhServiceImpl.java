package com.cd.qlyhk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.reptile.WeixinArticePipeline;
import com.cd.qlyhk.reptile.WeixinArticeProcessor;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IRhService;
import com.cd.qlyhk.vo.QlyRhReptileQueueVO;

import us.codecraft.webmagic.Spider;

/**
 * 获客服务接口实现类
 * 
 * @author sailor
 *
 */
@Service(IRhService.BEAN_ID)
public class RhServiceImpl implements IRhService {

	private static final Logger logger = LoggerFactory.getLogger(RhServiceImpl.class);

	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	@Autowired
	private IArticleService articleService;

	@Override
	public void timingTaskReptileArticle() {
		List<QlyRhReptileQueueVO> rqList = articleService.queryFixedReptileQueues();
		String[] urls = null;
		if(rqList != null && rqList.size() > 0) {
			urls = new String[rqList.size()];
			for(int i = 0; i < rqList.size(); i++) {
				QlyRhReptileQueueVO vo = rqList.get(i);
				urls[i] = vo.getContent_url();
			}
		}
		
		if(urls != null && urls.length > 0) {
			logger.info("*************************执行下载的文章url总数：{}", urls.length);
			handle(urls);
		}
	}
	
	private void handle(String[] urls) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("openId", "");
		param.put("filePath", articleImgUpload);
		param.put("fileUrlPath", articleImgUrl);
		
		Spider.create(new WeixinArticeProcessor()).addPipeline(new WeixinArticePipeline(param)).addUrl(urls).thread(1).run();
	}
}
