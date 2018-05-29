package com.procx.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法 KHC 071018
 * 
 */
public class SecurityMD5 {

    // return byte[]
	private static byte[] getDigestPasswd(String password) {
		MessageDigest userNamePassword = null;
		byte[] digestPassword;
		try {
			/* 使用MD5加密算法 */
			userNamePassword = MessageDigest.getInstance("MD5");
			userNamePassword.update(password.getBytes());
			digestPassword = userNamePassword.digest();
			return digestPassword;
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Get SecurityPassword MessageDigest failed!!!");
			return null;
		}
	}

	public byte[] encode(byte[] input) {
		byte[] result = new byte[16];
		int len = input.length;
		int midLen = len / 2;
		mencpy(result, input, 0, midLen - 8, 8);
		mencpy(result, input, 8, midLen, 8);
		return result;
	}

	// 实现块拷贝,
	public void mencpy(byte[] output, byte[] input, int outBegin, int inBegin,
			int len) {
		int i;
		for (i = 0; i < len; i++) {
			output[outBegin + i] = input[inBegin + i];
		}
	}
	
	// ====================================================================== //
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

 	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToString(byte[] b) {
       StringBuffer resultSb = new StringBuffer();
       for (int i = 0; i < b.length; i++) {
    	   // 若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
    	   resultSb.append(byteToHexString(b[i]));
    	   // 使用本函数则返回加密结果的10进制数字字串，即全数字形式
    	   // resultSb.append(byteToNumString(b[i]));
       }
       return resultSb.toString();
	}
     
	// 10进制
	@SuppressWarnings("unused")
	private static String byteToNumString(byte b) {
		int _b = b;
		if (_b < 0) {
			_b = 256 + _b;
		}
		return String.valueOf(_b);
	}

	// 16进制
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// 文件内容MD5加密
	public static String getFile(String filepath) {
		FileInputStream fos = null;
		try {
			File file = new File(filepath);
			fos = new FileInputStream(file);
			java.security.MessageDigest alga = java.security.MessageDigest
					.getInstance("MD5");
			byte[] buffer = new byte[1024];// 设置填充大小
			int read;// 实际读取的字节数
			while ((read = fos.read(buffer, 0, 1024)) > 0) {
				alga.update(buffer, 0, read);
			}
			byte[] digesta = alga.digest();
			return byteArrayToString(digesta);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
		}
		return "";
	}
   
    // 字符串MD5加密
	public static String getString(String str) {
		return byteArrayToString(getDigestPasswd(str));
	}
	
	public static String getMD5(String message) {  
        MessageDigest messageDigest = null;  
        StringBuffer md5StrBuff = new StringBuffer();  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
            messageDigest.reset();  
            messageDigest.update(message.getBytes("UTF-8"));  
              
            byte[] byteArray = messageDigest.digest();  
            for (int i = 0; i < byteArray.length; i++)   
            {  
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
                else  
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
            }  
        } catch (Exception e) {  
            throw new RuntimeException();  
        }  
        return md5StrBuff.toString().toLowerCase();//字母大写  
    }  
     
    // test
    public static void main(String[] args) {
    	// SecurityMD5 md5encrypt = new SecurityMD5();
    	// System.out.println(SecurityMD5.getString("000"));
    	/*long start_time = System.currentTimeMillis();
    	System.out.println(start_time);
    	System.out.println(SecurityMD5.getFile("D:\\ISMP\\sync_user_batch.txt"));
    	System.out.println(System.currentTimeMillis()-start_time);*/
    	// System.out.println(MD5Encode("10000000"));
    	
    	 SecurityMD5 md5encrypt = new SecurityMD5();
    	 System.out.println(SecurityMD5.getMD5("6003040001201703171000019983\"SvcCont\":{\"staffCode\":\"ZTE\",\"channelNbr\":\"5000003239661\",\"commonRegionId\":\"8500100\",\"traceId\":\"6003040001201703171000019983\",\"certPhoneNumRel\":[{\"phoneNum\":\"18983628809\",\"certAddress\":\"重庆市璧山县东林大道44号2幢1单元3-2\",\"custName\":\"林永\",\"certNum\":\"500227198501011612\",\"certType\":\"2\",\"actionType\":\"10\",\"serviceType\":\"1000\",\"lanId\":\"8500100\"}]}6003040001CQTEST"));
    	
     }
   
}
