package com.cd.qlyhk.reptile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import us.codecraft.webmagic.Spider;

public class WeixinArticeCallable implements Callable<Map<String, String>> {

	private String openId;
	private String articleImgUpload;
	private String articleImgUrl;
	private String[] urls;

	public WeixinArticeCallable(String openId, String articleImgUpload, String articleImgUrl, String[] urls) {
		this.openId = openId;
		this.articleImgUpload = articleImgUpload;
		this.articleImgUrl = articleImgUrl;
		this.urls = urls;
	}
	
	@Override
	public Map<String, String> call() throws Exception {
		Map<String, String> retMap = new HashMap<String, String>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("openId", openId);
		param.put("filePath", articleImgUpload);
		param.put("fileUrlPath", articleImgUrl);
		
		Spider.create(new WeixinArticeProcessor()).addPipeline(new WeixinArticePipeline(param)).addUrl(urls).thread(1).run();
		return retMap;
	}
}
