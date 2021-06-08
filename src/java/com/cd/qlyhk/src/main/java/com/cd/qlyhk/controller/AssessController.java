package com.cd.qlyhk.controller;


import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.AssessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/assess")
public class AssessController {

    private static final Logger logger = LoggerFactory.getLogger(AssessService.class);


    @Resource(name = AssessService.BEAN_ID)
    private AssessService assessService;

    /**
     * 文章及分享
     * @param map
     * @return
     */
    @RequestMapping("/as.do")
    @ResponseBody
    public Response finalByArticleAndStatistics(@RequestBody Map map){
        logger.info("*************文章及分享*************");
        Response response = assessService.finalByArticleAndStatistics(map);
        return response;
    }

    /**
     * 财锐早报
     * @param map
     * @return
     */
    @GetMapping("/bs.do")
    @ResponseBody
    public Response finalByMorning(@RequestBody Map map){
        Response response = assessService.finalByMorning(map);
        return response;
    }

    /**
     * 会员列表
     * @param map
     * @return
     */
    @GetMapping("/cs.do")
    @ResponseBody
    public Response finalByUser(@RequestBody Map map){
        return assessService.finalByUser(map);
    }


    /**
     * 月度热点文章
     * @param map
     * @return
     */
    @GetMapping("/ds.do")
    @ResponseBody
    public Response finalByArticle(@RequestBody Map map){
        return assessService.finalByArticle(map);
    }
}
