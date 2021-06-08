package com.cd.qlyhk.reptile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.test.Test;
import com.cd.qlyhk.utils.HttpRequestUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class WeixinArticeProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory.getLogger(WeixinArticeProcessor.class);
	
    private Site site = Site.me().setDomain("mp.weixin.qq.com")
            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
            .addHeader("Cache-Control","max-age=0")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0")
            .setRetryTimes(3).setSleepTime(5000);

    @Override
    public void process(Page page) {
    	logger.info("*****************************page**************************");
//    	if(page.getUrl().toString().contains("video_player")){
//    		System.out.println(page.getHtml());
//    		List<String> videoUrls = page.getHtml().$("#js_mpvedio video", "origin_src").all();
//    		System.out.println("***********************视频地址***************************************");
//    		System.out.println(videoUrls);
//    		
//    		page.putField("videoUrls", videoUrls);
//    	} else {
            final String title = page.getHtml().xpath("//div[@id='img-content']/h2/text()").get();
            final String source = page.getHtml().xpath("//div[@id='meta_content']/span/a/text()").get();
//            final String editDate = page.getHtml().xpath("//div[@id='meta_content']/em/text()").get();
//            final String metaContent = page.getHtml().xpath("//div[@id='meta_content']").get();
//            final String content = page.getHtml().xpath("//div[@id='js_content']").get();
            String htmlStr = page.getHtml().get();
            htmlStr = htmlStr.replaceAll("<!doctype html>", "");
            // 去掉文章表头
            String content = "";
            if(htmlStr.indexOf("<div id=\"img-content\"") > -1) {
            	String contentHeader = htmlStr.substring(0, htmlStr.indexOf("<div id=\"img-content\""));
                contentHeader += "<div id=\"img-content\">";
                if(htmlStr.indexOf("<div class=\"rich_media_content") > -1) {
                	String jsContent = htmlStr.substring(htmlStr.indexOf("<div class=\"rich_media_content"));
                    jsContent = jsContent.replaceFirst("visibility: hidden;", "");
                    String contentSnap = jsContent.replaceAll("data-src", "src").replaceAll("preview.html", "player.html");
                    
                    content = contentHeader + contentSnap;
                } else {
                	content = htmlStr;
                }
            } else {
            	content = htmlStr;
            }

//            String contentSnap = content.replaceAll("data-src", "src").replaceAll("preview.html", "player.html");
            
//            String filename = "D:/share/wx/" + System.currentTimeMillis() + ".txt";
//    		Test.WriteStringToFile5(filename, content);
            
            List<String> imgUrls = page.getHtml().$("#js_content img", "data-src").all();
            /*
            List<String> videoIframeUrls = page.getHtml().$("#js_content iframe[data-vidtype='2']", "data-src").all();
            List<String> videoUrls = new ArrayList<String>();
            if(videoIframeUrls.size() > 0) {
            	logger.info("*****************************video**************************");
//            	page.addTargetRequests(videoIframeUrls);
            	for(String item : videoIframeUrls) {
//            		System.out.println(item);
            		String vid = GetVal(item, "vid");
            		String reqUrl = "https://mp.weixin.qq.com/mp/videoplayer?action=get_mp_video_play_url&preview=0&__biz=&mid=&idx=&uin=&key=&pass_ticket=&wxtoken=&appmsg_token=&x5=0&f=json";
            		reqUrl += "&vid=" + vid;
            		
            		String result = HttpRequestUtil.doGet(reqUrl);
            		System.out.println(result);
            		JSONObject json = JSONObject.parseObject(result);
            		JSONArray url_info = json.getJSONArray("url_info");
            		if(url_info != null && url_info.size() > 0) {
            			JSONObject urlObj = url_info.getJSONObject(0);
            			String videoUrl = urlObj.getString("url");
            			item = item.replaceAll("&", "&amp;");
//            			System.out.println(item);
            			contentSnap = contentSnap.replace(item, videoUrl);
            			
            			videoUrls.add(videoUrl);
            		}
            	}
            	page.putField("videoUrls", videoUrls);
            	
            }*/

            page.putField("title", title);
            page.putField("source", source);
//            page.putField("editDate", editDate);
            page.putField("content", content);
//            page.putField("contentSnap", contentSnap);
            page.putField("imageUrls", imgUrls);
            
//    	}
    	
    }

    @Override
    public Site getSite() {
        return site;
    }
	/*
	 * public static void main(String[] args) {
	 * 
	 * String urls [] = { "https://mp.weixin.qq.com/s/GykCMEYhv5-pnEOnGLk_Ww"};
	 * 
	 * Spider.create(new WeixinArticeProcessor()) .addPipeline(new
	 * WeixinArticePipeline("123")) .addUrl(urls).thread(1).run(); }
	 */

    private String GetVal(String url, String name) {
    	String ret = "";
    	String regex = "(^|&)"+ name +"=([^&]*)(&|$)";
	    Pattern p = Pattern.compile(regex);
	    Matcher m = p.matcher(url);
	    while(m.find()){
	    	System.out.println(m.group());
	    	ret = m.group(2);
	    }
    	return ret;
    }
}
