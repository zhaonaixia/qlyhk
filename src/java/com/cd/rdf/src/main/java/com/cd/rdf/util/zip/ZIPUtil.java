package com.cd.rdf.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.springframework.security.crypto.codec.Base64;

/**
 * <p>
 * ZIP解、压缩工具类。
 * </p>
 * 
 * @author <a href="mailto:chenqiang">chenqiang</a>
 * @version $LastChangedRevision: 4336 $
 */
public class ZIPUtil {
	private static final int BUFFER_SIZE = 2048;

	/**
	 * 解压缩BASE64之后的压缩数据
	 * 
	 * @param encdata
	 *            BASE64之后的压缩数据
	 * @return 解压缩之后的数据
	 * @throws IOException
	 *             IO异常
	 */
	public static String decompress(String encdata, boolean codebyB64)
			throws IOException {
		if (encdata == null) {
			return null;
		}
		if (codebyB64) {
			return new String(decompress(Base64.decode(encdata.getBytes(Charset
					.defaultCharset()))), Charset.defaultCharset());
		} else {
			return new String(decompress(encdata.getBytes(Charset
					.defaultCharset())), Charset.defaultCharset());
		}
	}

	/**
	 * 压缩字符串数据
	 * 
	 * @param data
	 *            字符串数据
	 * @return 压缩之后的数据的BASE64编码字符串
	 * @throws IOException
	 *             IO异常
	 */
	public static String compress(String data, boolean codebyB64)
			throws IOException {
		if (data == null) {
			return null;
		}
		if (codebyB64) {
			return new String(Base64.encode(compress(data.getBytes(Charset
					.defaultCharset()))), Charset.defaultCharset());
		} else {
			return new String(
					compress(data.getBytes(Charset.defaultCharset())),
					Charset.defaultCharset());
		}
	}

	/**
	 * 压缩字节数组形式的数据
	 * 
	 * @param encdata
	 *            字节数组形式的数据
	 * @return 压缩之后的字节数组
	 * @throws IOException
	 *             IO异常
	 */
	public static byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = null;
		DeflaterOutputStream deflaterOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			deflaterOutputStream = new DeflaterOutputStream(
					byteArrayOutputStream);
			deflaterOutputStream.write(data);
			deflaterOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} finally {
			if (deflaterOutputStream != null) {
				deflaterOutputStream.close();
			}
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
		}
	}

	/**
	 * 解压缩字节数组形式的数据
	 * 
	 * @param encdata
	 *            字节数组形式的数据
	 * @return 解压缩之后的字节数组
	 * @throws IOException
	 *             IO异常
	 */
	public static byte[] decompress(byte[] encdata) throws IOException {
		if (encdata == null) {
			return null;
		}

		InputStream inputStream = null;
		InflaterInputStream inflaterInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			inputStream = new ByteArrayInputStream(encdata);
			inflaterInputStream = new InflaterInputStream(inputStream);
			byteArrayOutputStream = new ByteArrayOutputStream();

			int count;
			byte[] data = new byte[BUFFER_SIZE];
			while ((count = inflaterInputStream.read(data, 0, BUFFER_SIZE)) != -1) {
				byteArrayOutputStream.write(data, 0, count);
			}

			return byteArrayOutputStream.toByteArray();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (inflaterInputStream != null) {
				inflaterInputStream.close();
			}
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
		}
	}
}
