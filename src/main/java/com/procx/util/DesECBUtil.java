package com.procx.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES ECB对称加密 解密
 */
public class DesECBUtil {

    /**
     * 加密数据
     * @param str
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String str, String key)
            throws Exception {

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteMi = cipher.doFinal(str.getBytes());
        return byteMi;

    }

    /**
     * 解密数据
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] bytes, String key)
            throws Exception {

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] byteMi = cipher.doFinal(bytes);
        return new String(byteMi);

    }

    /**
     * 测试方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String clearText = "springsk";  //这里的数据长度必须为8的倍数
        String key = "1234567890ABCDEF";
        System.out.println("明文："+clearText+"\n密钥："+key);


        //加密
        clearText = Base64Util.encode(encrypt(clearText, key));
        System.out.println("加密后："+clearText);

        //加密
        String decryptText = decrypt(Base64Util.decode(""), key);
        System.out.println("解密后："+decryptText);
    }
}