package com.cd.qlyhk.utils;

import com.cd.qlyhk.vo.ReceiveXml;

import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;

/**
 * 微信事件回复工具类
 * @author sailor
 *
 */
public class WxEventRevertUtil {

    /**
     * 扫绑定客户联系人的二维码进入公众平台回复的消息
     * @param vo
     * @return
     */
    public static String revertQrCodeEvent(ReceiveXml vo) {
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle(vo.getTitle());
        item.setDescription(vo.getDescription());
        item.setPicUrl(vo.getPicUrl());
        item.setUrl(vo.getUrl());

        return revertBasicInfo(vo, item);
    }

    /**
     * 扫电子合同签署的二维码进入公众平台回复的消息
     * @param vo
     * @return
     
    public static String revertQrCodeEventByContract(ReceiveXml vo) {
        String eventKey = vo.getEventKey();
        if(eventKey.contains("qrscene")){
            eventKey = eventKey.replace("qrscene_", "");
        }
        JSONObject jsonObject = JSON.parseObject(eventKey);

        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle(PropertyUtil.getProperty("wxEventRevert.properties","mp.contractSign.title"));
        item.setDescription(PropertyUtil.getProperty("wxEventRevert.properties","mp.contractSign.description"));
        item.setPicUrl(PropertyUtil.getProperty("wxEventRevert.properties","mp.contractSign.picUrl"));
        item.setUrl(String.format(PropertyUtil.getProperty("wxEventRevert.properties","mp.main.url"), PropertyUtil.getProperty("wxEventRevert.properties","mp.contractSign.url") + jsonObject.get("ci") + "%26cust_id%3d" + jsonObject.get("k") + "%26linkman_id%3d" + jsonObject.get("l")));
        return revertBasicInfo(vo, item);
    }*/

    /**
     * 回复图文消息的基本信息
     * @param vo
     * @param item
     * @return
     */
    private static String revertBasicInfo(ReceiveXml vo, WxMpXmlOutNewsMessage.Item item) {
        WxMpXmlOutNewsMessage wxMpXmlOutNewsMessage = new WxMpXmlOutNewsMessage();
        wxMpXmlOutNewsMessage.setToUserName(vo.getFromUser());
        wxMpXmlOutNewsMessage.setFromUserName(vo.getToUser());
        wxMpXmlOutNewsMessage.setCreateTime(Long.valueOf(vo.getCreateTime()));
        wxMpXmlOutNewsMessage.setArticleCount(1);
        wxMpXmlOutNewsMessage.addArticle(item);
        return XStreamTransformer.toXml(WxMpXmlOutNewsMessage.class, wxMpXmlOutNewsMessage);
    }

    /**
     * 回复文本消息的基本信息
     * @param vo
     * @param content
     * @return
     */
    public static String revertTextBasicInfo(ReceiveXml vo, String content) {
        WxMpXmlOutTextMessage wxMpXmlOutTextMessage = new WxMpXmlOutTextMessage();
        wxMpXmlOutTextMessage.setToUserName(vo.getFromUser());
        wxMpXmlOutTextMessage.setFromUserName(vo.getToUser());
        wxMpXmlOutTextMessage.setCreateTime(Long.valueOf(vo.getCreateTime()));
        wxMpXmlOutTextMessage.setMsgType("text");
        wxMpXmlOutTextMessage.setContent(content);
        return XStreamTransformer.toXml(WxMpXmlOutTextMessage.class, wxMpXmlOutTextMessage);
    }

    /**
     * 回复图片消息的基本信息
     * @param vo
     * @param mediaId
     * @return
     
    public static String revertImageBasicInfo(ReceiveXml vo, String mediaId) {
        WxMpXmlOutImageMessage wxMpXmlOutImageMessage = new WxMpXmlOutImageMessage();
        wxMpXmlOutImageMessage.setToUserName(vo.getFromUser());
        wxMpXmlOutImageMessage.setFromUserName(vo.getToUser());
        wxMpXmlOutImageMessage.setCreateTime(Long.valueOf(vo.getCreateTime()));
        wxMpXmlOutImageMessage.setMsgType("image");
        wxMpXmlOutImageMessage.setMediaId(mediaId);
        return XStreamTransformer.toXml(WxMpXmlOutImageMessage.class, wxMpXmlOutImageMessage);
    }*/

    /**
     * 被关注回复消息
     * @param vo
     * @return
     */
    public static String revertBeFocusedMessage(ReceiveXml vo) {
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setTitle("测试");
        item.setDescription("测试123");
        item.setPicUrl("");
        item.setUrl("");
        return revertBasicInfo(vo, item);
    }
}
