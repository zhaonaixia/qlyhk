package com.cd.rdf.password;


/**
 * 
 * 密码管理的Handle
 */
public interface PasswordEncryptProvider {
    /**
     * 加密密码
     * 
     * @param inputString
     *            输入字符串
     * @return 加密码后的字符串
     * @throws SecurityException
     *             出错时抛出
     */
    public String encrypt(String inputString) throws SecurityException;

}