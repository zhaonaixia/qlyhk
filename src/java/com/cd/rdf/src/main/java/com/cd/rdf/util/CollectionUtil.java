package com.cd.rdf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cd.rdf.base.IValueObject;
import com.cd.rdf.util.ReflectionUtil;

/**
 * <pre>
 * 集合操作工具类.
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
public class CollectionUtil {

  protected static final Logger  logger  = LoggerFactory.getLogger(CollectionUtil.class);
  
  private CollectionUtil() {}

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Collection<T> collection) {
      return (T[]) collection.toArray();
  }

  public static boolean isEmpty(Collection<?> collection) {
      return collection == null || collection.isEmpty();
  }

  public static boolean isNotEmpty(Collection<?> collection) {
      return collection != null && !collection.isEmpty();
  }

  /**
   * list转map
   * @param list 集合
   * @param keyMethodName 主键key getXxx
   * @param c class
   * @return map对象
   */
  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> listToMap(List<V> list, String keyMethodName, Class<V> c) {
      Map<K, V> map = new HashMap<K, V>();
      if (list != null) {
          try {
              Method methodGetKey = c.getMethod(keyMethodName);
              for (int i = 0; i < list.size(); i++) {
                  V value = list.get(i);
                  K key = (K) methodGetKey.invoke(list.get(i));
                  map.put(key, value);
              }
          } catch (Exception e) {
              throw new IllegalArgumentException("field can't match the key!");
          }
      }
      return map;
  }
  

//  /**
//   * <pre>
//   * 查找MapList中的map
//   * @param mapList List<Map<String, Object>>
//   * @param key String,map的Key
//   * @param value Object,map的Value
//   * @return Map<String, Object> 没有找到为null
//   * </pre>
//   */  
//  public static Map<String, Object> findMap(List<Map<String, Object>> mapList, String key, Object value){
//  
//    Map<String,Object> result = null;
//   
//    if ( (mapList!=null) && (mapList.size()>0) ){
//      boolean foundFlag = false;
//      for (Map<String,Object> map : mapList){
//        Object crntValue = map.get(key);
//        if ( value!=null && crntValue!=null ){
//          if(crntValue.equals(value))
//            foundFlag = true;
//          // < json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较
//          else if((value instanceof Number) && (crntValue instanceof Number) ){
//            if (crntValue.toString().equals(value.toString()))
//                foundFlag = true;
//          }
//          // > json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较
//
//          if (foundFlag==true){
//            result = map;
//            break;            
//          }
//        }
//      }
//    }
//    
//    return result;
//  }

  /**
   * <pre>
   * 查找MapList中的map
   * @param mapList List<Map<String, Object>>
   * @param key String,map的Key
   * @param value Object,map的Value
   * @param ignoreCase boolean,是否忽略大小写,用于字符串比较
   * @return Map<String, Object> 没有找到为null
   * </pre>
   */  
  public static Map<String, Object> findMap(List<Map<String, Object>> mapList, String key, Object value, boolean ignoreCase){
  
    Map<String,Object> result = null;
   
    if ( (mapList!=null) && (mapList.size()>0) ){
      boolean foundFlag = false;
      for (Map<String,Object> map : mapList){
        Object crntValue = map.get(key);
        if ( value!=null && crntValue!=null ){
          if  ( (value instanceof String) && (crntValue instanceof String) ) {
            // < 忽略大小写
            if ( ignoreCase ) {
              foundFlag = ((String)crntValue).equalsIgnoreCase((String) value);
            }
            // > 忽略大小写
            else {
              foundFlag = crntValue.equals(value);              
            }
          }
          else if(crntValue.equals(value))
            foundFlag = true;
          // < json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较
          else if((value instanceof Number) && (crntValue instanceof Number) ){
            if (crntValue.toString().equals(value.toString()))
                foundFlag = true;
          }
          // > json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较

          if (foundFlag==true){
            result = map;
            break;            
          }
        }
      }
    }
    
    return result;
  }

  
  /**
   * <pre>
   * 通过属性取查找对象List中的对象
   * @param objList List<Object>
   * @param propertyName String,对象的属性名
   * @param value Object,属性值
   * @param ignoreCase boolean,是否忽略大小写,用于字符串比较
   * @return <T extends Object> 没有找到为null
   * @throws Exception 
   * </pre>
   */  
  public static <T extends Object> Object findObject(List<T> objList, String propertyName, Object value, boolean ignoreCase) throws Exception{
      
    Object result = null;
    
    if ( (objList!=null) && (objList.size()>0) ){
      for ( Object obj : objList ){
        if (obj instanceof Map) {
          // Map<String,Object> map = (Map<String, Object>) obj;   // List<Map>调用findMap函数
        }
        else {
          Object crntValue = ReflectionUtil.getPropertyValue(obj, propertyName);
          if ( value!=null && crntValue!=null ){
            boolean foundFlag = false;
            if  ( (value instanceof String) && (crntValue instanceof String) ) {
              // < 忽略大小写
              if ( ignoreCase ) {
                foundFlag = ((String)crntValue).equalsIgnoreCase((String) value);
              }
              // > 忽略大小写
              else {
                foundFlag = crntValue.equals(value);              
              }
            }
            else if( crntValue.equals(value) ) {
              foundFlag = true;
            }            
            
            if ( foundFlag ) {
              result = obj;
              break;
            }
          }
          
        }
        
      }
    }
    
    return result;
  }

  /**
   * <pre>
   * 通过属性取查找对象List中的对象
   * @param objList List<Object>
   * @param propertyName String,对象的属性名
   * @param value Object,属性值
   * @param ignoreCase boolean,是否忽略大小写,用于字符串比较
   * @return List<T> 没有找到为null,否返回找到的对象列表
   * @throws Exception 
   * </pre>
   */  
  @SuppressWarnings("unchecked")
  public static <T extends Object> List<T> findObjectList(List<T> objList, String propertyName, Object value, boolean ignoreCase) throws Exception{
  
    List<T> result = null;
    
    if ( (objList!=null) && (objList.size()>0) ){
      for ( Object obj : objList ){
        Object crntValue = ReflectionUtil.getPropertyValue(obj, propertyName);
        if ( value!=null && crntValue!=null ){
          boolean foundFlag = false;
          if  ( (value instanceof String) && (crntValue instanceof String) ) {
            // < 忽略大小写
            if ( ignoreCase ) {
              foundFlag = ((String)crntValue).equalsIgnoreCase((String) value);
            }
            // > 忽略大小写
            else {
              foundFlag = crntValue.equals(value);              
            }
          }
          else if( crntValue.equals(value) ) {
            foundFlag = true;
          }            
          
          if ( foundFlag ) {
            if (result==null) result = new ArrayList<T>();
            result.add((T) obj);            
          }
        }
      }
    }
    
    if ( result!=null && result.size()>1 ) {
      System.out.println("findObjectList:" + JSON.toJSON(result));
    }
    
    return result;
  }
  
  /**
   * 按字符排序List<Map<String,Object>>
   * @param mapList List<Map<String, Object>>，要排序的列表
   * @param sortkey String，要排序的key
   * @return List<Map<String,Object>>,排序好的列表
   */  
  public static void sortMapListString(List<Map<String,Object>> mapList, final String sortkey) {

    Collections.sort(mapList,new Comparator<Map<String,Object>>(){
            public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
              int result = 0;
              result = arg0.get(sortkey).toString().compareTo(arg1.get(sortkey).toString());
              return result;
            }
        });
    
  }
  
  /**
   * 按Long排序List<Map<String,Object>>
   * @param mapList List<Map<String, Object>>，要排序的列表
   * @param sortkey String，要排序的key
   * @return List<Map<String,Object>>,排序好的列表
   */  
  public static void sortMapListLong(List<Map<String,Object>> mapList, final String sortkey) {
    
    Collections.sort(mapList,new Comparator<Map<String,Object>>(){
            public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
              int result = 0;
              Number num;
              Long value0, value1;
              value0 = Long.parseLong(arg0.get(sortkey).toString());
              value1 = Long.parseLong(arg1.get(sortkey).toString());
              result = value0.compareTo(value1);
              return result;
            }
        });       
  }

  /**
   * 按Integer排序List<Map<String,Object>>
   * @param mapList List<Map<String, Object>>，要排序的列表
   * @param sortkey String，要排序的key
   * @return List<Map<String,Object>>,排序好的列表
   */  
  public static void sortMapListInt(List<Map<String,Object>> mapList, final String sortkey) {
      Collections.sort(mapList,new Comparator<Map<String,Object>>(){
              public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
                int result = 0;
                Number num;
                Integer value0, value1;
                value0 =  Integer.parseInt(arg0.get(sortkey).toString());
                value1 = Integer.parseInt(arg1.get(sortkey).toString());
                result = value0.compareTo(value1);
                return result;
              }
          });       
  }

  /**
   * 对象转为Map
   *
   * @param obj Object
   * @return Map
   * @throws IllegalAccessException
   */
  public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
      Map<String, Object> map = new HashMap<>();
      Class<?> clazz = obj.getClass();
      //System.out.println(clazz);
      for (Field field : clazz.getDeclaredFields()) {
          field.setAccessible(true);
          String fieldName = field.getName();
          Object value = field.get(obj);
          map.put(fieldName, value);
      }
      return map;
  }
  
  /**
   * <pre>
   * 通过属性取查找对象List中的对象
   * @param mapList List<Object>
   * @param key String,对象的属性名
   * @param value Object,属性值
   * @param ignoreCase boolean,是否忽略大小写,用于字符串比较
   * @return List<T> 没有找到为null,否返回找到的对象列表
   * @throws Exception 
   * </pre>
   */  
  @SuppressWarnings("unchecked")
  public static List<Map<String,Object>> findMapList(List<Map<String,Object>> mapList, String key, Object value, boolean ignoreCase) throws Exception{
    List<Map<String,Object>> result = null;
    
    if ( (mapList!=null) && (mapList.size()>0) ){
      for (Map<String,Object> map : mapList){
        boolean foundFlag = false;
        Object crntValue = map.get(key);
        if ( value!=null && crntValue!=null ){
          if  ( (value instanceof String) && (crntValue instanceof String) ) {
            // < 忽略大小写
            if ( ignoreCase ) {
              foundFlag = ((String)crntValue).equalsIgnoreCase((String) value);
            }
            // > 忽略大小写
            else {
              foundFlag = crntValue.equals(value);              
            }
          }
          else if(crntValue.equals(value)) {
            foundFlag = true;            
          }            
          // < json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较
          else if((value instanceof Number) && (crntValue instanceof Number) ){
            if (crntValue.toString().equals(value.toString()))
                foundFlag = true;
          }
          // > json转map时,Long类型的数据会变为Integer,此时需要转为字符串比较
        }
        if (foundFlag) {
          if (result==null)  result = new ArrayList<Map<String,Object>>();
          result.add(map);
        }
      }
    }
    
    return result;
  }

  /**
   * <pre>
   * 通过属性取查找对象List中的对象
   * @param mapList List<Object>
   * @param key String,对象的属性名
   * @param value Object,属性值
   * @param ignoreCase boolean,是否忽略大小写,用于字符串比较
   * @return List<T> 没有找到为null,否返回找到的对象列表
   * @throws Exception 
   * </pre>
   */  
  /**
   * <pre>
   * Map查找，支持忽略大小写
   * @param map
   * @param key
   * @param ignoreCase
   * @return 
   * @return
   * @throws Exception
   * </pre>
   */
  public static <T> T getObjectByMap(Map<String,T> map, String key, boolean ignoreCase) {
    T result = null;
    //  Object value    
    try {
      if ( (map!=null)  ){
        if ( ignoreCase==false ) {
          result  =  map.get(key);
        }
        // < 忽略大小写
        else {
          for (Map.Entry<String, T> entry : map.entrySet()) {
            String crntKey = entry.getKey();
            if ( crntKey.equalsIgnoreCase(key) ) {
              result = (T) entry.getValue();
              break;
            }
          }        
        }
        // > 忽略大小写      
      }      
    } catch (Exception e) {
      logger.error("getObjectByMap查找对象失败,错误原因:{}");
      throw new RuntimeException("查找对象失败");
    }
        
    return (T) result;
  }
  
}
