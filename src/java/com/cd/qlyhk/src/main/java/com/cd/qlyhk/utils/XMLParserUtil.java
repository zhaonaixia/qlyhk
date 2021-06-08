package com.cd.qlyhk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cd.qlyhk.constants.XMLConstant;
import com.cd.qlyhk.vo.ReceiveXml;

/**
 * 开发者接收微信报文XML解析器工具类
 * @author sailor
 *
 */
public class XMLParserUtil {

    /**
     * logger
     */
    private static final Logger logger = LogManager.getLogger(XMLParserUtil.class);

    private XMLParserUtil() {

    }

    /**
     * 接收XML数据
     */
    public static String getXMLContent(HttpServletRequest request) {
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            request.setCharacterEncoding("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error("获取request返回的报异常，原因：{}", e.getMessage(), e);
        }
        // 取出发送用户
        String xmlS = sb.toString();
        logger.info("获取request返回报文:{}", xmlS);
        return xmlS;
    }


    /**
     * 解析xml里的属性值
     *
     * @param xmlS
     * @return
     */
    public static ReceiveXml parseWXXML(String xmlS) {
        ReceiveXml vo = new ReceiveXml();
        if (xmlS != null && !xmlS.equals("")) {
            String fromUser = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.FROMUSERNAME, true);

            String toUser = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.TOUSERNAME, true);

            String createTime = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.CREATETIME, false);
            if (createTime != null) {
                vo.setCreateTime(createTime);
            }

            String msgType = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.MSGTYPE, true);
            if (msgType != null) {
                vo.setMsgType(msgType);
            }

            String content = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.CONTENT, true);
            if (content != null) {
                vo.setContent(content);
            }

            String msgId = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.MSGID, false);
            if (msgId != null) {
                vo.setMsgId(msgId);
            }

            String picUrl = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.PICURL, true);
            if (picUrl != null) {
                vo.setPicUrl(picUrl);
            }

            String location_X = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.LOCATION_X, false);
            if (location_X != null) {
                vo.setLocation_X(location_X);
            }

            String location_Y = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.LOCATION_Y, false);
            if (location_Y != null) {
                vo.setLocation_Y(location_Y);
            }

            String scale = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.SCALE, false);
            if (scale != null) {
                vo.setScale(scale);
            }

            String label = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.LABEL, true);
            if (label != null) {
                vo.setLabel(label);
            }
            String title = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.TITLE, true);
            if (title != null) {
                vo.setTitle(title);
            }
            String description = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.DESCRIPTION, true);
            if (description != null) {
                vo.setDescription(description);
            }
            String url = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.URL, true);
            if (url != null) {
                vo.setUrl(url);
            }
            String event = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.EVENT, true);
            if (event != null) {
                vo.setEvent(event);
            }
            String eventKey = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.EVENTKEY, true);
            if (eventKey != null) {
                vo.setEventKey(eventKey);
            }
            String scanType = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.SCANTYPE, true);
            if (scanType != null) {
                vo.setScanType(scanType);
            }
            String scanResult = getXMLPropertyValue(xmlS, XMLConstant.XmlPropeties.SCANRESULT, true);
            if (scanResult != null) {
                vo.setScanResult(scanResult);
            }

            vo.setFromUser(fromUser);
            vo.setToUser(toUser);
        }
        return vo;
    }

    /**
     * 取得xml里的属性对应的值
     *
     * @param xmlS
     * @param propetyName
     * @param hasCDATAFlag
     * @return
     */
    private static String getXMLPropertyValue(String xmlS, String propetyName, boolean hasCDATAFlag) {
        String startS;
        String endS;
        if (hasCDATAFlag) {
            startS = "<" + propetyName + ">" + XMLConstant.CDATAFlag.FLAG_START;
            endS = XMLConstant.CDATAFlag.FLAG_END + "</" + propetyName + ">";
        } else {
            startS = "<" + propetyName + ">";
            endS = "</" + propetyName + ">";
        }
        int index_s = xmlS.indexOf(startS);
        int index_e = xmlS.indexOf(endS);
        if (index_s != -1) {
            return xmlS.substring(index_s + startS.length(), index_e);
        }
        return null;
    }
}
