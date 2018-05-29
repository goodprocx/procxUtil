package com.procx.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {
	/**
	 * 将byte转base64数组
	 * @param binaryData
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(byte[] binaryData) throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(binaryData), "UTF-8");
	}
	
	/**
     * 将BASE64字符串恢复为二进制数据
     * @param base64String
     * @return
     * @throws UnsupportedEncodingException */
	public static byte[] decode(String base64String) throws UnsupportedEncodingException {
		return Base64.decodeBase64(base64String.getBytes("UTF-8"));
	}
}
