package com.procx.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	
//	public static final String LTE_PUBLIC_KEY = "PUBLIC_TOKEN_KEY";
//	public static final String LTE_PRIVATE_KEY = "TOKEN_8110000_KEY";
	
	public static final String LTE_PUBLIC_KEY = "#public@2015$KEY";
	public static final String LTE_PRIVATE_KEY = "*ChongQ@850!pz";
	
	public static void main(String[] args) throws Exception {
		String s = "123";
		System.out.println(AESUtil.aesDecryptByBytes(AESUtil.parseHexStr2Byte(s), LTE_PRIVATE_KEY));
	}
	
	static {
		if (null == Security.getProvider("SunJCE")) {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
		}

		if (null == Security.getProvider("SUN")) {
			Security.addProvider(new sun.security.provider.Sun());
		}
	}  
	
	/**
	 * * 加密 * *
	 * 
	 * @param content
	 *            需要加密的内容 *
	 * @param password
	 *            加密密码 *
	 * @return
	 */  
	public static byte[] encrypt(String content, String password) {  
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES","SunJCE");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG","SUN");
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);
//			kgen.init(128, new SecureRandom(password.getBytes("utf-8")));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES","SunJCE");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch (NoSuchPaddingException e) {  
			e.printStackTrace();  
		} catch (InvalidKeyException e) {  
			e.printStackTrace();  
		} catch (UnsupportedEncodingException e) {   
			e.printStackTrace();  
		} catch (IllegalBlockSizeException e) {  
			e.printStackTrace();  
		} catch (BadPaddingException e) {  
			e.printStackTrace();  
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}  
		return null;  
	}  
	/** 
	 ** 解密 
	 **  
	 ** @param encryptBytes 需要解密的内容 
	 ** @param decryptKey  加密密码 
	 ** @return 
	 **/
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
		KeyGenerator kgen = KeyGenerator.getInstance("AES","SunJCE");    
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG","SUN");
		secureRandom.setSeed(decryptKey.getBytes());
		kgen.init(128, secureRandom);
//		kgen.init(128, new SecureRandom(decryptKey.getBytes("utf-8")));
		Cipher cipher = Cipher.getInstance("AES","SunJCE");  
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
		byte[] decryptBytes = cipher.doFinal(encryptBytes);  
		return new String(decryptBytes);  
	}
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	*/  
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}  
	
	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}  
	
}
