package com.cd.qlyhk.service;

import com.cd.qlyhk.rest.Response;

import java.util.Map;

public interface AssessService {

    final String BEAN_ID = "assessService";

    /**
     * 文章及分享
     * @param map
     * @return
     */
    Response finalByArticleAndStatistics(Map map);

    /**
     * 财锐早报
     * @param map
     * @return
     */
    Response finalByMorning(Map map);

    /**
     * 会员列表
     * @param map
     * @return
     */
    Response finalByUser(Map map);

    /**
     * 月度热点文章
     * @param map
     * @return
     */
    Response finalByArticle(Map map);
}
