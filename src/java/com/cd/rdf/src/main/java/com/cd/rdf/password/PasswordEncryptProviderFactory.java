/*
 * 创建日期 2005-2-3
 *
 *
 **/
package com.cd.rdf.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fanzhi
 * 
 * 密码加密提供的工厂类，根据配置文件生成不同的密码加密提供类的实例
 */
public class PasswordEncryptProviderFactory {
    /** 阻止生成工厂类的实例 */
    private PasswordEncryptProviderFactory() {

    }

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryptProviderFactory.class);

    /**
     * 单例模式的实例
     */
    private static PasswordEncryptProvider instance = null;

    public static PasswordEncryptProvider getPasswordEncryptProvider()
            throws SecurityException {
        String passwordEncryptClass = "com.cd.bovms.rdf.password.PasswordEncryptProviderMD5Impl";
        if (instance == null) {
            /* 使用类名为参数取得实例 */
            instance = getPasswordEncryptProvider(passwordEncryptClass);
        }
        return instance;
    }

    /**
     * 取得密码加密提供者的实例
     * 
     * @param passwordEncryptClass
     *            类名
     * @return 实例
     * @throws SecurityException
     *             出错时抛出
     */
    public static PasswordEncryptProvider getPasswordEncryptProvider(
            String passwordEncryptClass) throws SecurityException {
        if (instance == null) {
            /* 当实例为空时，创建一个新实例 */
            logger.info("密码提供者类:" + passwordEncryptClass);
            /* 生成密码提供者类的实例 */
            Object obj = null;
            try {
                obj = Class.forName(passwordEncryptClass).newInstance();

            } catch (InstantiationException e1) {
                logger.error("创建实例失败！");
                throw new SecurityException(e1.getMessage());
            } catch (IllegalAccessException e1) {
                logger.error("创建实例失败！");
                throw new SecurityException(e1.getMessage());
            } catch (ClassNotFoundException e1) {
                logger.error("密码提供者类没有找到！");
                throw new SecurityException(e1.getMessage());
            }
            if (obj instanceof PasswordEncryptProvider) {
                instance = (PasswordEncryptProvider) obj;
            } else {
                throw new SecurityException(
                        "密码提供者类没有实现PasswordEncryptProvider接口");
            }
        }
        return instance;
    }
}