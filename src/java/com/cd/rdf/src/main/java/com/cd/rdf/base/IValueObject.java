package com.cd.rdf.base;

/**
 * <pre>
 * VO基类接口
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

public interface IValueObject extends java.io.Serializable {

    /**
     * 值对象转换成成String类型 用于记录日志时使用
     *
     * @return 对象转换出来的字符串
     */
    public String toString();

    /**
     * 值对象转换成XML类型 用于格式化输出值对象内容
     *
     * @return 值对象XML格式数据内容
     */
    public String toXml();

    /**
     * 值对象转换成成Json格式字符串 用于格式化输出值对象内容
     *
     * @return 对象转换出来的字符串
     */
    public String toJsonStr();

}
