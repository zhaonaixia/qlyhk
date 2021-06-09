package com.cd.qlyhk.controller;


import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.Constants;
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
        Response response = Response.getDefaulTrueInstance();
        try {
            response = assessService.finalByArticleAndStatistics(map);
        } catch (Exception e) {
            logger.info("查询异常，异常信息为：{}",e.getMessage(),e);
            response.setCode(Constants.RESPONSE_CODE_FAIL);
            response.setMessage("查询文章异常");
        }
        logger.info("查询成功，返回的数据为：{}"+ JSON.toJSON(response));
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
        logger.info("***********************财锐早报***************************");
        Response response = Response.getDefaulTrueInstance();
        try {
            response = assessService.finalByMorning(map);
        } catch (Exception e) {
            logger.info("查询异常，异常信息是：{}",e.getMessage(),e);
            response.setCode(Constants.RESPONSE_CODE_FAIL);
            response.setMessage("查询异常");
        }
        logger.info("查询成功，返回的数据是={}"+JSON.toJSON(response));
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
        logger.info("******************会员列表*********************");
        Response response = Response.getDefaulTrueInstance();
        try {
            response = assessService.finalByUser(map);
        } catch (Exception e) {
            logger.info("查询异常，异常信息为：{}",e.getMessage(),e);
            response.setCode(Constants.RESPONSE_CODE_FAIL);
            response.setMessage("查询异常");
        }
        logger.info("查询成功，返回的数据是={}"+JSON.toJSON(response));
        return response;
    }


    /**
     * 月度热点文章
     * @param map
     * @return
     */
    @GetMapping("/ds.do")
    @ResponseBody
    public Response finalByArticle(@RequestBody Map map){
        logger.info("********************月度热点文章********************");
        Response response = Response.getDefaulTrueInstance();
        try {
            response = assessService.finalByArticle(map);
        } catch (Exception e) {
            logger.info("查询异常，异常信息为：{}",e.getMessage(),e);
            response.setCode(Constants.RESPONSE_CODE_FAIL);
            response.setMessage("查询异常");
        }
        logger.info("查询成功，返回的数据为={}"+JSON.toJSON(response));
        return response;
    }
}
