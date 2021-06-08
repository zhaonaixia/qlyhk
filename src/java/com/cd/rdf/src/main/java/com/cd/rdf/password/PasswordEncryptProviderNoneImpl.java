/*
 * 创建日期 2005-2-3
 *
 */
package com.cd.rdf.password;


/**
 * @author fanzhi
 * 
 * 不加密的实现，直接返回明文
 */
public class PasswordEncryptProviderNoneImpl implements PasswordEncryptProvider {

    /**
     * @see com.cd.rdf.password.PasswordEncryptProvider#encrypt(String)
     */
    public String encrypt(String inputString) throws SecurityException {
        return inputString;
    }

}