package com.cd.qlyhk.reptile;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.GetObjectRequest;

/**
 * 阿里云工具类
 * @author sailor
 *
 */
public class AliyunOssUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssUtils.class);

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    private static final String aliyunOssAk = "";
    private static final String aliyunOssSk = "";
    private static final String aliyunOssBucket = "";
    private static final String aliyunOssEndpoint = "";
    
    /**
     * 同步本地文件到阿里云OSS
     *
     * @param path
     * @param file
     * @return
     */
    public static void upload(String path, File file) {
        fixedThreadPool.execute(() -> {
            uploadsync(path, file);
        });
    }

    /**
     * 同步本地文件到阿里云OSS
     *
     * @param path
     * @param file
     * @return
     */
    public static boolean uploadsync(String path, File file) {

    	if (StringUtils.isBlank(path)) {
            return false;
        }

        path = removeFileSeparator(path);
        path = path.replace('\\', '/');

        String ossBucketName = aliyunOssBucket;
        OSSClient ossClient = newOSSClient();

        try {
            ossClient.putObject(ossBucketName, path, file);
            boolean success = ossClient.doesObjectExist(ossBucketName, path);
            if (!success) {
            	logger.error("aliyun oss upload error! path:" + path + "\nfile:" + file);
            }
            return success;

        } catch (Throwable e) {
        	logger.error("aliyun oss upload error!!!", e);
            return false;
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 如果文件以 / 或者 \ 开头，去除 / 或 \ 符号
     */
    private static String removeFileSeparator(String path) {
        while (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1, path.length());
        }
        return path;
    }

    /**
     * 同步 阿里云OSS 到本地
     *
     * @param path
     * @param toFile
     * @return
     */
    public static boolean download(String path, File toFile) {

        if (StringUtils.isBlank(path)) {
            return false;
        }

        path = removeFileSeparator(path);
        String ossBucketName = aliyunOssBucket;
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.getObject(new GetObjectRequest(ossBucketName, path), toFile);
            return true;
        } catch (Throwable e) {
        	logger.error("aliyun oss download error!!!  path:" + path + "   toFile:" + toFile, e);
            if (toFile.exists()) {
                toFile.delete();
            }
            return false;
        } finally {
            ossClient.shutdown();
        }
    }
    
    public static String upload(File toFile, String path){
    	if (StringUtils.isBlank(path)) {
            return "";
        }

        path = removeFileSeparator(path);
        String ossBucketName = aliyunOssBucket;
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.putObject(ossBucketName, path, toFile);
            // 设置URL过期时间为10年 3600l* 1000*24*365*10
         	Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            URL url = ossClient.generatePresignedUrl(ossBucketName, path, expiration);
			if (url != null) {
//				System.out.println(url.toString());
				path = url.toString();
			}
			logger.info("toFile:" + toFile);
            return path;
        } catch (Throwable e) {
        	logger.error("aliyun oss upload error!!!  path:" + path + "   toFile:" + toFile, e);
            if (toFile.exists()) {
                toFile.delete();
            }
            return "";
        } finally {
            ossClient.shutdown();
            toFile.delete();
        }
    }

    public static OSSClient newOSSClient() {
        String endpoint = aliyunOssEndpoint;
        String accessId = aliyunOssAk;
        String accessKey = aliyunOssSk;
        return new OSSClient(endpoint, new DefaultCredentialProvider(accessId, accessKey), null);
    }


}
