package com.cd.qlyhk.vo;
/**
 * 
 * @author sailor
 *
 */
public class ReceiveXml {
    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUser;
    /**
     * 开发者微信号
     */
    private String toUser;
    /**
     * 消息创建时间 （整型）
     */
    private String createTime;
    /**
     * 消息类型:text image link
     */
    private String msgType;
    /**
     * 文本消息内容
     */
    private String content;
    /**
     * 消息id，64位整型
     */
    private String msgId;
    /**
     * 图片消息才有，图片链接
     */
    private String picUrl;
    /**
     * 地理位置纬度
     */
    private String location_X;
    /**
     * 地理位置经度
     */
    private String location_Y;
    /**
     * 地图缩放大小
     */
    private String scale;
    /**
     * 地理位置信息
     */
    private String label;
    /**
     * 消息标题(链接消息 才有）
     */
    private String title;
    /**
     * 消息描述(链接消息 才有）
     */
    private String description;
    /**
     * 消息标题(链接消息 才有）
     */
    private String url;
    /**
     * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)
     */
    private String event;
    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    private String eventKey;
    /**
     * 扫描类型，一般是qrcode
     */
    private String scanType;
    /**
     * 扫描结果，即二维码对应的字符串信息
     */
    private String scanResult;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLocation_X() {
        return location_X;
    }

    public void setLocation_X(String location_X) {
        this.location_X = location_X;
    }

    public String getLocation_Y() {
        return location_Y;
    }

    public void setLocation_Y(String location_Y) {
        this.location_Y = location_Y;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public String toString() {
        return "ReceiveXml{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", createTime='" + createTime + '\'' +
                ", msgType='" + msgType + '\'' +
                ", content='" + content + '\'' +
                ", msgId='" + msgId + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", location_X='" + location_X + '\'' +
                ", location_Y='" + location_Y + '\'' +
                ", scale='" + scale + '\'' +
                ", label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", event='" + event + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", scanType='" + scanType + '\'' +
                ", scanResult='" + scanResult + '\'' +
                '}';
    }
}
