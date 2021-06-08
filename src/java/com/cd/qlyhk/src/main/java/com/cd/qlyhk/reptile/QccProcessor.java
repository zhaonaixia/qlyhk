package com.cd.qlyhk.reptile;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Author: 
 * @Date: 2020/4/22
 **/
public class QccProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        System.out.println(page.getHtml());
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setDomain("qcc.com")
                .setCharset("utf-8")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
    }

    public static void main(String[] args) {
        Spider.create(new QccProcessor()).addUrl("https://www.qcc.com/search?key=%E5%B9%BF%E4%B8%9C%E8%B4%A2%E9%81%93%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8").run();
    }

}
