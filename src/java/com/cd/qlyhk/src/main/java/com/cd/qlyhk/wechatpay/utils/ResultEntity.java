
package com.cd.qlyhk.wechatpay.utils;

public class ResultEntity {
	private boolean success = true;

	private String msg = "执行成功";
	
	private String data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ResultEntity(boolean success) {
		this.success = success;
	}

	public ResultEntity(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "[success]=" + success + ",[msg]=" + msg + ",[data]=" + data;
	}
}
