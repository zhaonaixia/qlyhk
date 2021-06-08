package com.cd.rdf.base.api;

import com.cd.rdf.base.IValueObject;

/**
 * <pre>
 * 调用API返回结果接口，作为接口调用的要返回是否成功及错识信息，用于统一返回格式。
 * </pre>
 * @author awens
 * @version 1.00.00
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
public interface IApiResultObject extends IValueObject {
	  /**
	   * 获取接口是否执行成功
	   * @return boolean 接口是否执行成功
	   */
	  public boolean getSuccess();

	  /**
	   * 设置接口是否执行成功
	   * @param success boolean 接口是否执行成功
	   */
	  public void setSuccess(boolean success);

	  /**
	   * 获取状态码
	   * @return int 状态码:0或者200代表成功，其它表示失败
	   */
	  public int getCode();

	  /**
	   * 设置状态码
	   * @param code int 状态码:0或者200代表成功，其它表示失败
	   */
	  public void setCode(int code);

	  /**
	   * 获取信息描述
	   * @return String 状态码:0或者200代表成功，其它表示失败
	   */
	  public String getMessage();

	  /**
	   * 设置信息描述
	   * @param message String
	   */
	  public void setMessage(String message);

	//  public String getSysMessage();

	//  public void setSysMessage(String sysMessage);

	  /**
	   * 获取数据体
	   * @return Object 数据体，只有接口执行成功才返回数据，否则为null
	   */
	  public Object getData();

	  /**
	   * 设置数据体
	   * @param data Object 数据体
	   */
	  public void setData(Object data);

}
