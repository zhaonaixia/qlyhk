package com.cd.qlyhk.service;

import java.util.List;
import java.util.Map;

public interface ITestService {
 
  final String BEAN_ID = "testService";
    
  public List<Map<String,Object>> query();

}
