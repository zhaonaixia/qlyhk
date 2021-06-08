package com.cd.rdf.util;

import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * <pre>
 * FastJson全局配置工具类
 * </pre>
 * 
 * @author awens
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public class FastJsonGlobalConfUtil{
    
  private static SerializeConfig globalInstance = null;
  
  public static synchronized SerializeConfig getGlobalInstance() {    
    if (globalInstance==null) {
      globalInstance = new SerializeConfig(true);
    }
    return globalInstance;
  }
  
}
