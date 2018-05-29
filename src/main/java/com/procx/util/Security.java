package com.procx.util;

import java.security.MessageDigest;

public class Security {
	private synchronized static byte[] encode(String origin) {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			hash = md.digest(origin.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hash;
	}
	
	public static String Md5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf.toString();
	} 


	public synchronized static String getPassword(String origin) {
		String result = "";
		byte[] hash = Security.encode(origin);
		for (int i = 0; i < hash.length; i++) {
			int itemp = hash[i] & 0xFF;
			if (itemp < 16)
				result += "0";
			result += Integer.toString(itemp, 16);
		}
		return result.toLowerCase();
	}

	public synchronized static boolean isPassword(String origin, String result) {
		if (Security.getPassword(origin).equals(result)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(Security.getPassword("420e37d2ea90dad27a2bf57fa78a3728seckey"));
	}
}