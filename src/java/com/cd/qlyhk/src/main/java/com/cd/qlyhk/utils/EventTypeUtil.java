package com.cd.qlyhk.utils;

import com.cd.qlyhk.constants.XMLConstant;
import com.cd.qlyhk.vo.ReceiveXml;

/**
 * 微信事件类型工具类
 * @author sailor
 *
 */
public class EventTypeUtil {
    /**
     * 是否是订阅事件
     *
     * @param vo
     * @return
     */
    public static boolean isSubscribe(ReceiveXml vo) {
        if (XMLConstant.ReceiveMsgType.EVENT.equals(vo.getMsgType()) &&
                XMLConstant.WXMessageEvents.SUBSCRIBE.equals(vo.getEvent())) {//是事件，并且是订阅
            return true;
        }

        return false;
    }

    /**
     * 是否是取消订阅事件
     *
     * @param vo
     * @return
     */
    public static boolean isUnSubscribe(ReceiveXml vo) {
        if (XMLConstant.ReceiveMsgType.EVENT.equals(vo.getMsgType()) &&
                XMLConstant.WXMessageEvents.UN_SUBSCRIBE.equals(vo.getEvent())) return true;
        return false;
    }

    /**
     * 是否是自定义菜单
     *
     * @param vo
     * @return
     */
    public static boolean isClick(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.CLICK.equals(vo.getEvent())) return true;
        return false;
    }

    /**
     * 是否是文本消息
     *
     * @param vo
     * @return
     */
    public static boolean isTextMessge(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.TEXT.equals(vo.getMsgType())) return true;
        return false;
    }
    
    /**
     * 是否是图片消息
     *
     * @param vo
     * @return
     */
    public static boolean isImageMessge(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.IMAGE.equals(vo.getMsgType())) return true;
        return false;
    }
    
    /**
     * 是否是语音消息
     *
     * @param vo
     * @return
     */
    public static boolean isVoiceMessge(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.VOICE.equals(vo.getMsgType())) return true;
        return false;
    }

    /**
     * 是否是地图消息
     *
     * @param vo
     * @return
     */
    public static boolean isLocationMessge(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.LOCATION.equals(vo.getMsgType())) return true;
        return false;
    }

    /**
     * 是否是扫码推事件
     *
     * @param vo
     * @return
     */
    public static boolean isScanCodePush(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.SCANCODE_PUSH.equals(vo.getEvent())) return true;
        return false;
    }

    /**
     * 是否是扫码推事件且弹出“消息接收中”提示框的事件
     *
     * @param vo
     * @return
     */
    public static boolean isScanCodeWaitMsg(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.SCANCODE_WAITMSG.equals(vo.getEvent())) return true;
        return false;
    }

    /**
     * 扫描带参数二维码事件
     *
     * @param vo
     * @return
     */
    public static boolean isScanParamCode(ReceiveXml vo) {
        if (XMLConstant.WXMessageEvents.SCAN_PARAM_CODE.equals(vo.getEvent())) return true;
        return false;
    }
}
