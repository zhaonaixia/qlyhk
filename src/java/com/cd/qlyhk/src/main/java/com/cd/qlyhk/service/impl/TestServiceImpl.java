package com.cd.qlyhk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.service.ITestService;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(ITestService.BEAN_ID)
public class TestServiceImpl  implements ITestService{
  
  @Autowired
  private IBaseDAO baseDAO;
  
  private final String mapperNamespace = TestServiceImpl.class.getName();
  
  public List<Map<String,Object>> query(){
    Map<String,Object> paramMap = new HashMap<String,Object>();
    List<Map<String,Object>> result = baseDAO.findListBy(mapperNamespace+".query", paramMap);
    return result;   
  }
  
}
