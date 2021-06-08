package com.cd.rdf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 反射实用工具类.扩充ReflectionUtils的功能.
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
public class ReflectionUtil {

  private final static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

  public static String[] commonFieldTypeNames = { "int", "long", "short", "double", "float", "char", "byte", "boolean",
          "java.lang.String", "java.lang.Integer", "java.lang.Long", "java.lang.Short", "java.lang.Double", "java.lang.Float",
          "java.lang.Byte", "java.lang.Boolean", "java.math.BigDecimal", "java.util.Date" };


  /**
   * 获取对象的属性值
   * 
   * @param obj 对象实例
   * @param propertyName 属性名
   * @return Object 属性值
   * @throws Exception
   */
  public static Object getPropertyValue(Object obj, String propertyName) throws Exception {
      Object result = null;
      Class<? extends Object> cls = obj.getClass();
      Field field = cls.getDeclaredField(propertyName);
      field.setAccessible(true);
      result = field.get(obj);
      return result;
  }

  /**
   * 设置对象的属性值
   * 
   * @param obj 对象实例
   * @param propertyName 属性名
   * @return Object 属性值
   * @return 返回值
   * @throws Exception
   */
  public static void setProperty(Object obj, String propertyName, Object value) throws Exception {
      Class<? extends Object> cls = obj.getClass();
      Field field = cls.getDeclaredField(propertyName);
      field.setAccessible(true);
      field.set(obj, value);
  }

  /**
   * 调用服务方法
   * 
   * @param obj 对象实例
   * @param methodName 对象的方法名
   * @param args 参数数组
   * @return Object
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  public static Object invokeMethod(Object obj, String methodName, Object[] args) throws Exception {
      Class[] argsClass = new Class[args.length];
      for (int i = 0, j = args.length; i < j; i++) {
          argsClass[i] = args[i].getClass();
      }
      Method method = obj.getClass().getMethod(methodName, argsClass);
      return method.invoke(obj, args);
  }

  /**
   * 获取最简类名(去除包名)
   * @param className
   * @return String
   */
  public static String getSimpleClassName(String className) {
      return className.substring(className.lastIndexOf(".") + 1);
  }

  /**
   * 判断指定对象中的指定属性名的类型是否属于自定义普通数据类型
   * @param obj 指定对象
   * @param fieldName 指定属性名
   * @return Boolean
   */
  public static Boolean isCommonTypeField(Object obj, String fieldName) {
      boolean result = false;
      try {
          Field field = obj.getClass().getDeclaredField(fieldName);
          String type = field.getGenericType().getTypeName();
          for (int i = 0; i < commonFieldTypeNames.length; i++) {
              if (commonFieldTypeNames[i].equals(type)) {
                  result = true;
                  break;
              }
          }
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage());
      }
      return result;
  }

  /**
   * 获取指定类的属性名列表
   * @param cls 对象类型
   * @param isGetAll 是否获取类的所有字段。true:获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段;false:获得某个类的所有的公共（public）的字段，包括父类中的字段。
   * @param ignoreFields String[] 要忽略的属性列表,不读取。
   * @return List<String>
   */
  @SuppressWarnings("rawtypes")
  public static List<String> getPropertiesName(Class cls, Boolean isGetAll, String[] ignoreFields) {
      List<String> result = new ArrayList<String>();
      Field[] fields = null;
      try {
          if (isGetAll)
              fields = cls.getDeclaredFields();
          else fields = cls.getFields();
          for (int i = 0; i < fields.length; i++) {
              if ((ignoreFields != null) && (ignoreFields.length > 0)) {
                  boolean needIgnore = false;
                  for (int j = 0; j < ignoreFields.length; j++) {
                      if (ignoreFields[j].equals(fields[i].getName())) {
                          needIgnore = true;
                          continue;
                      }
                  }
                  if (needIgnore == false)
                      result.add(fields[i].getName());
              } else {
                  result.add(fields[i].getName());
              }
          }
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage());
      }
      return result;
  }

  /**
   * 获取指定对象的属性名列表
   * @param className 指定类全名
   * @param isGetAll 是否获取类的所有字段。true:获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段;false:获得某个类的所有的公共（public）的字段，包括父类中的字段。
   * @param ignoreFields String[] 要忽略的属性列表,不读取。
   * @return List<String>
   */
  public static List<String> getPropertiesName(Object obj, Boolean isGetAll, String[] ignoreFields) {
      List<String> result = null;
      try {
          Class<? extends Object> cls = obj.getClass();
          result = getPropertiesName(cls, isGetAll, ignoreFields);
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage());
      }
      return result;
  }

  /**
   * 获取指定对象的属性名列表
   * @param className 指定类全名
   * @param isGetAll 是否获取类的所有字段。true:获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段;false:获得某个类的所有的公共（public）的字段，包括父类中的字段。
   * @param ignoreFields String[] 要忽略的属性列表,不读取。
   * @return List<String>
   */
  @SuppressWarnings("rawtypes")
  public static List<Field> getProperties(Class cls, Boolean isGetAll, String[] ignoreFields) {
      List<Field> result = new ArrayList<Field>();
      Field[] fields = null;
      try {
          if (isGetAll)
              fields = cls.getDeclaredFields();
          else fields = cls.getFields();
          for (int i = 0; i < fields.length; i++) {
              if ((ignoreFields != null) && (ignoreFields.length > 0)) {
                  boolean needIgnore = false;
                  for (int j = 0; j < ignoreFields.length; j++) {
                      if (ignoreFields[j].equals(fields[i].getName())) {
                          needIgnore = true;
                          continue;
                      }
                  }
                  if (needIgnore == false)
                      result.add(fields[i]);
              } else {
                  result.add(fields[i]);
              }
          }
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage());
      }
      return result;
  }
  
}
