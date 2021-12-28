package com.allsaints.music.utils;

import java.security.MessageDigest;


public class Md5Utils {

	private static final String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private Md5Utils() {}
	
	/**
	 * 将md5加密后的字符串转为16进制
	 * 
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 将字节转换为16进制字符串
	 * 
	 * @param b
	 *            将要转换的数字
	 * @return 返回的16进制数字
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDIGITS[d1] + HEXDIGITS[d2];

	}

	/**
	 * md5加密
	 * 
	 * @param origin
	 *            被加密的字符串
	 * @return 加密后的字符串
	 */
	public static String md5(String origin) {
		String encryptStr = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			encryptStr = byteArrayToHexString(md.digest(origin.getBytes()));
		} catch (Exception e) {
			// ignore
		}
		return encryptStr;
	}
	
}
