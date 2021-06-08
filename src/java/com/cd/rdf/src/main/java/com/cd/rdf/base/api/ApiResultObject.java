package com.cd.rdf.base.api;

import com.cd.rdf.base.BaseValueObject;

/**
*
* <pre>
* 调用API返回结果对象，作为接口调用的要返回是否成功及错识信息，用于统一返回格式
* </pre>
*
* @author awens shichangwen@ycs168.cn
* @version 1.00.00
*
*          <pre>
* 修改记录
*    修改后版本:     修改人：  修改日期:     修改内容:
* </pre>
*
*/
@SuppressWarnings("serial")
public class ApiResultObject extends BaseValueObject implements IApiResultObject{

  /** 状态码 */
  protected int code = 0; //0代表成功，其它表示失败

  /** 返回信息描述 */
  protected String message = "";

  /** 系统级返回的错误信息，执行方法出错时填写 */
  // protected String sysMessage;

  /** 返回的数据体 */
  protected Object data;

  /** 是否成功 */
  protected boolean success = true;

  public boolean getSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

//  public String getSysMessage() {
//    return sysMessage;
//  }
//
//  public void setSysMessage(String sysMessage) {
//    this.sysMessage = sysMessage;
//  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public static ApiResultObject getDefaulTrueInstance(){
    ApiResultObject result = new ApiResultObject();
    result.setSuccess(true);
    result.setCode(0);
    result.setMessage("");
    return result;
  }
  
}
