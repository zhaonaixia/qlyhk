/**
 * Copyright(c) cd Science & Technology Ltd.
 */
package com.cd.qlyhk.rest;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cd.qlyhk.constants.Constants;
import com.cd.rdf.base.BaseValueObject;

/**
 * <pre>
 * TODO。
 * </pre>
 *
 * @author
 * @date 2018年1月14日
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录 
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

@SuppressWarnings("serial")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Response extends BaseValueObject implements IRestApiResponse{

    /** 响应信息 */
    protected String  message;

    /** 响应编码 */
    protected String  code;

    /** 是否成功,默认是成功 */
    protected boolean success = true;

    /** 返回数据 */
    protected Object  data;


    public Response() {
        this.code = Constants.RESPONSE_CODE_SUCCESS;
        this.success = true;
        this.data = null;

    }

    public Response(String message) {
        this.message = message;

    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;

    }

    public Response(String code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public boolean isSuccess() {
//        return success;
//    }
  	public boolean getSuccess() {
          return success;
  	}

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public String toString() {
        StringBuffer sbJson = new StringBuffer("{\"code\":");
        sbJson.append(this.code).append(",\"message\":\"").append(this.message).append("\",").append("\"success\":").append(this.success)
                .append("}");
        return sbJson.toString();

    }

    public static Response getDefaulTrueInstance(){
    	Response result = new Response();
        result.setSuccess(true);
        result.setCode("0");
        result.setMessage("");
        result.setData(null);
        return result;
      }
    
}
