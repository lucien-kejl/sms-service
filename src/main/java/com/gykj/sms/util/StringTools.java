package com.gykj.sms.util;

import java.io.ByteArrayOutputStream;

public class StringTools {

	/**
	 * 判断字符串是否为空，并返回字符串有效值，空返回null
	 */
	public static String checkNull(String str) {
		if (str == null) {
			return null;
		}
		String ret = str.trim();
		if (ret.length() <= 0) {
			return null;
		}
		if (ret.equalsIgnoreCase("null")) {
			return null;
		}
		return ret;
	}

	/**
	 * 判断object类型的字符串是否为空，并返回字符串有效值，空返回null
	 */
	public static String checkNull(Object obj) {
		if (obj == null) {
			return null;
		}
		String ret = null;
		if (obj instanceof String) {
			ret = (String) obj;
		} else {
			ret = obj.toString();
		}
		String retTrim = ret.trim();
		if (retTrim.length() <= 0) {
			return null;
		}
		if (retTrim.equalsIgnoreCase("null")) {
			return null;
		}
		return retTrim;
	}

	public static boolean isNullOrEmpty(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}

	public static boolean isNullOrEmpty(Object obj) {
		return null == obj || "".equals(obj);
	}


	public static boolean isNull(String value) {
		return value == null || "".equals(value.trim());
	}

	private static String hexString = "0123456789ABCDEF";

	public static String encode(String str) {
		byte[] bytes = str.getBytes();
		return encode(bytes);
	}

	public static String encode(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	public static String decode(String bytes) {
		return new String(decodeBytes(bytes));
	}

	public static byte[] decodeBytes(String bytes) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes
					.length() / 2);
			for (int i = 0; i < bytes.length(); i += 2) {
				baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
						.indexOf(bytes.charAt(i + 1))));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 定义下划线
	 */
	private static final char UNDERLINE = '_';

	/**
	 * String为空判断(不允许空格)
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * String不为空判断(不允许空格)
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * Byte数组为空判断
	 *
	 * @param bytes
	 * @return boolean
	 */
	public static boolean isNull(byte[] bytes) {
		// 根据byte数组长度为0判断
		return bytes == null || bytes.length == 0;
	}

	/**
	 * Byte数组不为空判断
	 *
	 * @param bytes
	 * @return boolean
	 */
	public static boolean isNotNull(byte[] bytes) {
		return !isNull(bytes);
	}

	/**
	 * 驼峰转下划线工具
	 *
	 * @param param
	 * @return java.lang.String
	 */
	public static String camelToUnderline(String param) {
		if (isNotBlank(param)) {
			int len = param.length();
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++) {
				char c = param.charAt(i);
				if (Character.isUpperCase(c)) {
					sb.append(UNDERLINE);
					sb.append(Character.toLowerCase(c));
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * 下划线转驼峰工具
	 *
	 * @param param
	 * @return java.lang.String
	 */
	public static String underlineToCamel(String param) {
		if (isNotBlank(param)) {
			int len = param.length();
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++) {
				char c = param.charAt(i);
				if (c == 95) {
					++i;
					if (i < len) {
						sb.append(Character.toUpperCase(param.charAt(i)));
					}
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * 在字符串两周添加''
	 *
	 * @param param
	 * @return java.lang.String
	 */
	public static String addSingleQuotes(String param) {
		return "\'" + param + "\'";
	}
}