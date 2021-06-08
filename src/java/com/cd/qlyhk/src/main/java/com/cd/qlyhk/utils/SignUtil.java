package com.cd.qlyhk.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 签名校验工具类
 * @author sailor
 *
 */
public class SignUtil {

    /**
     * logger
     */
    private static final Logger logger = LogManager.getLogger(SignUtil.class);

    /**
     * 校验签名
     *
     * @param applicationToken 开发者中心配置的token值
     * @param signature        微信公众号传入签名值
     * @param timestamp        时间戳
     * @param nonce            随机数
     * @return
     */
    public static boolean checkSignature(String applicationToken, String signature, String timestamp,
                                         String nonce) {
        //从请求中（也就是微信服务器传过来的）拿到的token, timestamp, nonce
        String[] arr = new String[]{applicationToken, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            //将字节数组转成字符串
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("微信签名值校验，通过sha1加密异常，原因：{}", e.getMessage(), e);
        }

        logger.info("通过token+timestamp+nonce生成微信签名值={}", tmpStr);

        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将加密后的字节数组变成字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     *
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    /**
     * 排字典序
     *
     * @param a
     */
    private static void sort(String a[]) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }
}
