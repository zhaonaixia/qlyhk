package com.cd.rdf.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * redis访问服务类
 * </pre>
 * 
 * @author awens
 * @version 1.00.01
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public class SerializeUtil {

  private final static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

  public static byte[] serialize(Object object) {
    String errMsg = "";
    ObjectOutputStream oos = null;
    ByteArrayOutputStream baos = null;
    // 序列化
    baos = new ByteArrayOutputStream();
    try {
      oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
    } catch (IOException e) {
      errMsg = "序列化对象失败,错误原因:" + e.getMessage();
      logger.error(errMsg, e);
      throw new RuntimeException(errMsg, e);
    }
    byte[] bytes = baos.toByteArray();
    return bytes;
  }

  public static Object unserialize(byte[] bytes) {
    String errMsg = "";
    if (bytes == null) {
      return null;
    }
    ByteArrayInputStream bais = null;
    // 反序列化
    bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois;
    try {
      ois = new ObjectInputStream(bais);
    } catch (IOException e) {
      errMsg = "反序列化对象失败,错误原因:" + e.getMessage();
      logger.error(errMsg, e);
      throw new RuntimeException(errMsg, e);
    }
    try {
      return ois.readObject();
    } catch (Exception e) {
      errMsg = "反序列化对象失败,错误原因:" + e.getMessage();
      logger.error(errMsg, e);
      throw new RuntimeException(errMsg, e);
    }
  }
}