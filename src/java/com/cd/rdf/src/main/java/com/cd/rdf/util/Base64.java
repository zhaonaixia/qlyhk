package com.cd.rdf.util;

/**
 * <p>
 * Base64工具。
 * </p>
 * 
 * @author <a href="mailto:egcs@21cn.com">Dongsheng Song</a>
 * @version $LastChangedRevision: 3 $
 */
public final class Base64 {

	/**
	 * 数字0x03常量定义。
	 */
	private static final int MAGIC_NUM_0X03 = 0x03;

	/**
	 * 数字0x04常量定义。
	 */
	private static final int MAGIC_NUM_0X04 = 0x04;

	/**
	 * 数字0x06常量定义。
	 */
	private static final int MAGIC_NUM_0X06 = 0x06;

	/**
	 * 数字0x0F常量定义。
	 */
	private static final int MAGIC_NUM_0X0F = 0x0F;

	/**
	 * 数字0x30常量定义。
	 */
	private static final int MAGIC_NUM_0X30 = 0x30;

	/**
	 * 数字0x3C常量定义。
	 */
	private static final int MAGIC_NUM_0X3C = 0x3C;

	/**
	 * 数字0x3F常量定义。
	 */
	private static final int MAGIC_NUM_0X3F = 0x3F;

	/**
	 * 数字0x7F常量定义。
	 */
	private static final int MAGIC_NUM_0X7F = 0x7F;

	/**
	 * 数字0xC0常量定义。
	 */
	private static final int MAGIC_NUM_0XC0 = 0xC0;

	/**
	 * 数字0xF0常量定义。
	 */
	private static final int MAGIC_NUM_0XF0 = 0xF0;

	/**
	 * 缺省构造函数。
	 */
	private Base64() {
	}

	/**
	 * 返回字节数组用BASE64格式编码的字符数组，不添加CR/LF等字符。
	 * 
	 * @param arg
	 *            字节数组
	 * @return 字符数组
	 */
	public static char[] encode(byte[] arg) {
		return encode(arg, 0, arg.length);
	}

	/**
	 * 返回BASE64字符串表示的字节数组。
	 * 
	 * @param arg
	 *            BASE64字符串
	 * @return 字节数组
	 */
	public static byte[] decode(String arg) {
		return decode(arg.toCharArray());
	}

	/**
	 * 返回用BASE64编码后字符数组的字节数组。
	 * 
	 * @param arg
	 *            字符数组
	 * @return 字节数组
	 */
	public static byte[] decode(char[] arg) {
		return decode(arg, 0, arg.length);
	}

	/**
	 * 返回Base64编码字符数组，后面可能有填充字符'='。
	 * 
	 * @param value
	 *            字节数组
	 * @param startIndex
	 *            开始索引(包含)
	 * @param endIndex
	 *            结束索引(不包含)
	 * @return 字符数组
	 */
	public static char[] encode(byte[] value, int startIndex, int endIndex) {

		if ((value == null) || (value.length < 1) || (startIndex >= endIndex)
				|| (startIndex < 0) || (endIndex > value.length)) {
			return new char[0];
		}

		int length = ((endIndex - startIndex) / MAGIC_NUM_0X03)
				* MAGIC_NUM_0X04;
		int n = (endIndex - startIndex) % MAGIC_NUM_0X03;
		int b1, b2, b3, i;

		if (n != 0) {
			length += MAGIC_NUM_0X04;
		}
		n = endIndex - n;
		char[] buf = new char[length];
		for (i = startIndex, length = 0; i < n; i += MAGIC_NUM_0X03, length += MAGIC_NUM_0X04) {
			b1 = value[i];
			b2 = value[i + 1];
			b3 = value[i + 2];
			buf[length] = BASE64_CHAR[(b1 >> 2) & MAGIC_NUM_0X3F];
			buf[length + 1] = BASE64_CHAR[((b1 << MAGIC_NUM_0X04) & MAGIC_NUM_0X30)
					| ((b2 >> MAGIC_NUM_0X04) & MAGIC_NUM_0X0F)];
			buf[length + 2] = BASE64_CHAR[((b2 << 2) & MAGIC_NUM_0X3C)
					| ((b3 >> MAGIC_NUM_0X06) & MAGIC_NUM_0X03)];
			buf[length + MAGIC_NUM_0X03] = BASE64_CHAR[b3 & MAGIC_NUM_0X3F];
		}
		switch (endIndex - n) {
		case 1:
			b1 = value[n];
			buf[length] = BASE64_CHAR[(b1 >> 2) & MAGIC_NUM_0X3F];
			buf[length + 1] = BASE64_CHAR[((b1 << MAGIC_NUM_0X04) & MAGIC_NUM_0X30)];
			buf[length + 2] = '=';
			buf[length + MAGIC_NUM_0X03] = '=';
			break;
		case 2:
			b1 = value[n];
			b2 = value[n + 1];
			buf[length] = BASE64_CHAR[(b1 >> 2) & MAGIC_NUM_0X3F];
			buf[length + 1] = BASE64_CHAR[((b1 << MAGIC_NUM_0X04) & MAGIC_NUM_0X30)
					| ((b2 >> MAGIC_NUM_0X04) & MAGIC_NUM_0X0F)];
			buf[length + 2] = BASE64_CHAR[((b2 << 2) & MAGIC_NUM_0X3C)];
			buf[length + MAGIC_NUM_0X03] = '=';
			break;
		default:
			break;
		}
		return buf;
	}

	/**
	 * 返回用BASE64编码后字符数组的字节数组。
	 * <p>
	 * 如果有非法的Base64编码字符，截断到[0, 0x7F]；如果还不合法的话，使用缺省字符'/'代替， 也就是认为原始的6个二进制位是x03F
	 * </p>
	 * 
	 * @param value
	 *            字符数组
	 * @param startIndex
	 *            开始索引(包含)
	 * @param endIndex
	 *            结束索引(不包含)
	 * @return 字节数组
	 */
	public static byte[] decode(char[] value, int startIndex, int endIndex) {

		if ((value == null) || (value.length < 1) || (startIndex >= endIndex)
				|| (startIndex < 0) || (endIndex > value.length)
				|| (value[startIndex] == '=')) {
			return new byte[0];
		}
		int length, n, b1, b2, b3, b4, i;

		// 计算实际编码字符长度
		while (true) {
			if (value[--endIndex] != '=') {
				++endIndex;
				break;
			}
		}

		// 计算"整数"和"余数"
		length = (endIndex - startIndex) % MAGIC_NUM_0X04;
		if (length > 0) {
			length--;
		}
		length += ((endIndex - startIndex) / MAGIC_NUM_0X04) * MAGIC_NUM_0X03;
		byte[] buf = new byte[length];

		n = endIndex - (endIndex - startIndex) % MAGIC_NUM_0X04;
		for (i = startIndex, length = 0; i < n; i += MAGIC_NUM_0X04, length += MAGIC_NUM_0X03) {
			b1 = BASE64_BYTE[value[i] & MAGIC_NUM_0X7F];
			b2 = BASE64_BYTE[value[i + 1] & MAGIC_NUM_0X7F];
			b3 = BASE64_BYTE[value[i + 2] & MAGIC_NUM_0X7F];
			b4 = BASE64_BYTE[value[i + MAGIC_NUM_0X03] & MAGIC_NUM_0X7F];
			buf[length] = (byte) ((b1 << 2) | ((b2 >> MAGIC_NUM_0X04)));
			buf[length + 1] = (byte) (((b2 << MAGIC_NUM_0X04) & MAGIC_NUM_0XF0) | (b3 >> 2));
			buf[length + 2] = (byte) (((b3 << MAGIC_NUM_0X06) & MAGIC_NUM_0XC0) | b4);
		}

		switch (endIndex - n) {
		case 2:
			b1 = BASE64_BYTE[value[i] & MAGIC_NUM_0X7F];
			b2 = BASE64_BYTE[value[i + 1] & MAGIC_NUM_0X7F];
			buf[length] = (byte) ((b1 << 2) | ((b2 >> MAGIC_NUM_0X04)));
			break;
		case MAGIC_NUM_0X03:
			b1 = BASE64_BYTE[value[i] & MAGIC_NUM_0X7F];
			b2 = BASE64_BYTE[value[i + 1] & MAGIC_NUM_0X7F];
			b3 = BASE64_BYTE[value[i + 2] & MAGIC_NUM_0X7F];
			buf[length] = (byte) ((b1 << 2) | ((b2 >> MAGIC_NUM_0X04)));
			buf[length + 1] = (byte) (((b2 << MAGIC_NUM_0X04) & MAGIC_NUM_0XF0) | (b3 >> 2));
			break;
		default:
			break;
		}

		return buf;
	}

	/**
	 * BASE64字符数组。
	 */
	private static final char[] BASE64_CHAR = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

	/**
	 * BASE64字节数组。
	 */
	private static final byte[] BASE64_BYTE = { (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0x3E, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0x3F, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37,
			(byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B, (byte) 0x3C,
			(byte) 0x3D, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x01,
			(byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06,
			(byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
			(byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10,
			(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15,
			(byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0xFF,
			(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E,
			(byte) 0x1F, (byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23,
			(byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, (byte) 0x28,
			(byte) 0x29, (byte) 0x2A, (byte) 0x2B, (byte) 0x2C, (byte) 0x2D,
			(byte) 0x2E, (byte) 0x2F, (byte) 0x30, (byte) 0x31, (byte) 0x32,
			(byte) 0x33, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
			(byte) 0xFF };
}
