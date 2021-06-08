/*
 * 创建日期 2005-2-3
 *
 */
package com.cd.rdf.password;

/**
 * @author fanzhi
 * 
 * 处理密码的字符串工具类
 */
public class PasswordUtils {
    /**
     * 将字节转化为两位的十六进制数
     * 
     * @param ib
     *            8位的字节
     * @return 二位的十六进制数
     */
    private String byte2HEX(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    /**
     * 把字符数组变成十六进制的字符串
     * 
     * @param in
     *            byte数组
     * @return 十六进制的字符串
     */
    public static String byteArray2HEX(byte[] in) {
        PasswordUtils pu = new PasswordUtils();
        String outputString = "";
        for (int i = 0; i < in.length; i++) {
            outputString += pu.byte2HEX(in[i]);
        }
        return outputString;

    }

}