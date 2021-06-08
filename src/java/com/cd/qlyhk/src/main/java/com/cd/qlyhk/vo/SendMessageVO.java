package com.cd.qlyhk.vo;

import java.util.Date;
/**
 * 发送消息VO
 * @author sailor
 *
 */
public class SendMessageVO {

	private String fromuser;// 发送人
	private String touser;// 接收人
	private String content;// 消息内容
	private int msgType; // 消息类型
	private String templateId; // 模板消息ID类型
	private String url; // 模板消息的URL
	private String data; // 模板消息内容JSON格式
	private Date createTime;// 发送时间

	public String getFromuser() {
		return fromuser;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
