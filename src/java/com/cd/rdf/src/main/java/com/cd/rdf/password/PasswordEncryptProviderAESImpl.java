/*
 * 创建日期 2005-2-3
 *
 * 
 */
package com.cd.rdf.password;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author fanzhi
 * 
 * 密码加密的AES实现
 */
public class PasswordEncryptProviderAESImpl implements PasswordEncryptProvider {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryptProviderAESImpl.class);

    /**
     * @see com.cd.rdf.password.PasswordEncryptProvider#encrypt(String)
     */
    public String encrypt(String inputString) throws SecurityException {

        String encrypted = null;

        try {
            /* 初始化一个KeyGenerator */
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            /* 由明文得到key */
            keygen.init(new SecureRandom(inputString.getBytes()));
            SecretKey deskey = keygen.generateKey();

            /* 加密密码 */
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] en = cipher.doFinal(inputString.getBytes());
            /* 转换到十六进制串 */
            encrypted = PasswordUtils.byteArray2HEX(en);
        } catch (Exception e) {
            logger.warn("AES 加密出错，请检查您的jdk版本");
            throw new SecurityException(e.getMessage());
        }
        return encrypted;
    }

}