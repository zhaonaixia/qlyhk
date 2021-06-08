package com.cd.qlyhk.wechatpay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.commons.cli.oss.KeyStores;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpRequestHandler {

	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;

	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;

	// 请求器的配置
	private static RequestConfig requestConfig;

	// HTTP请求器
	private static CloseableHttpClient httpClient;

	/**
	 * 加载证书
	 * 
	 * @param path
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static void initCert(String path, TransfersDTO transfer) throws IOException, KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
		// 拼接证书的路径
		KeyStore keyStore = KeyStores.getInstance("PKCS12", path, transfer.map());

		// 加载本地的证书进行https加密传输
		FileInputStream instream = new FileInputStream(new File(path));
		try {
			keyStore.load(instream, transfer.getMchid().toCharArray()); // 加载证书密码，默认为商户ID
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, transfer.getMchid().toCharArray())
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000).build();

	}

	/**
	 * 通过Https往API post xml数据
	 * 
	 * @param url    API地址
	 * @param xmlObj 要提交的XML数据对象
	 * @param path   当前目录，用于加载证书
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String httpsRequest(String url, String xmlObj, TransfersDTO model, String path) throws IOException,
			KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
		// 加载证书
		initCert(path, model);

		String result = null;

		HttpPost httpPost = new HttpPost(url);

		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		try {
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");

		} catch (ConnectionPoolTimeoutException e) {

		} catch (ConnectTimeoutException e) {

		} catch (SocketTimeoutException e) {

		} catch (Exception e) {

		} finally {
			httpPost.abort();
		}

		return result;
	}
}
