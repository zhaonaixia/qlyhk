package com.cd.rdf.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 密码管理的实现类
 * 
 * @author fanzhi
 */
public class PasswordEncryptProviderMD5Impl implements PasswordEncryptProvider {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryptProviderMD5Impl.class);

    /**
     * @see com.cd.rdf.password.PasswordEncryptProvider#encrypt(String)
     */
    public String encrypt(String inputString) throws SecurityException {
        byte[] result = null;
        String outputString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error("没有找到MD5,请查看您的JDK版本");
            throw new SecurityException(e.getMessage());
        }
        return PasswordUtils.byteArray2HEX(result);
    }
}