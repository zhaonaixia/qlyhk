package com.cd.qlyhk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.service.ITestService;
import com.cd.rdf.cache.redis.RedisService;

@RestController
@RequestMapping(value = "/openapi/test")
public class TestController {
 
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private RedisService redisService;
  
  @Resource(name = ITestService.BEAN_ID)
  private ITestService   testService;

  @RequestMapping(value = "/query")  
  public List<Map<String,Object>> query(){
    List<Map<String,Object>> result = testService.query();
    logger.debug("查询返回结果:{}",JSON.toJSONString(result));
    return result;       
  }

  @RequestMapping(value = "/setRedis")  
  public String setRedis(@RequestParam String value){
    String result = "";
    String key="0_test";
    redisService.set(key, value, 600);
    logger.debug("写入redis成功,key:{},value:{}",key,value);
    result = String.format("写入redis成功,key:%s,value:%s", key,value);
    return result;       
  }
 
  @RequestMapping(value = "/getRedis")  
  public String getRedis(){
    String result = "";
    String key="0_test";
    String value = redisService.get(key);
    logger.debug("读取redis成功,key:{},value:{}",key,value);
    result = String.format("读取redis成功,key:%s,value:%s", key,value);
    return result;       
  }
  
}
