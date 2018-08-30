package com.mids.util.crypt;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.mids.util.Base64Util;

/**
 * @preserve public
 * @author wuchao
 * 
 */
public class MyDesUtils {

    private static final String ALGORITHM = "DES";

    private static final String CIPHER = "DES";

    private static final String DEFAULT_KEY = "Yjk_2oI7";
    // Des加密解密的密匙

    private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
    /**
     * data[]
     * 
     * @param data
     * @return
     */
    public static String encode(byte[] data) {

        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = (((data[i]) & 0x0ff) << 16) | (((data[i + 1]) & 0x0ff) << 8) | ((data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = (((data[i]) & 0x0ff) << 16) | (((data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = ((data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

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

    public static byte[] decode(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            // System.err.println("Error while decoding BASE64: " +
            // ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {

        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6)
                    + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }

    private static int decode(char c) {

        if (c >= 'A' && c <= 'Z')
            return (c) - 65;
        else if (c >= 'a' && c <= 'z')
            return (c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return (c) - 48 + 26 + 26;
        else
            switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
            }
    }

    public String encrypt(String strIn, String key, String encoding) throws Exception {

        return encryptDES(strIn, key, encoding);
    }

    public String decrypt(String strIn, String key, String encoding) throws Exception {

        return decryptDES(strIn, key, encoding);
    }

    private static String encryptDES(String encryptString, String encryptKey, String encoding) throws Exception {

        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes(encoding));
        
        return Base64Util.encode(encryptedData);
    }

    private static String decryptDES(String decryptString, String decryptKey, String encoding) throws Exception {

        byte[] byteMi = Base64Util.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData, encoding);
    }

    /** 使用BASE64时的字符集编码. */
    public static final String CHARSET = "UTF-8";

    /**
     * 先DEC加密，再用Base64转换 。JDK默认： ECB 加密模式和 PKCS#5 填充方式.
     * 
     * @param input
     *            the input
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String encryptDESBeas64(String input){

        try {
            input = encryptDES(input, DEFAULT_KEY, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * 先用Base64转换,再DEC解密 。JDK默认： ECB 加密模式和 PKCS#5 填充方式.
     * 
     * @param input
     *            the input
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String decryptBeas64DES(String input) {
    	String output = null;
        try {
        	output =  decryptDES(input, DEFAULT_KEY, CHARSET);
        } catch (Exception e) {
        	output = null;
            e.printStackTrace();           
        }
        return output;
    }
    
    public static String bytes2HexString(byte[] b) {  
        StringBuffer result = new StringBuffer();  
        String hex;  
        for (int i = 0; i < b.length; i++) {  
            hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            result.append(hex.toUpperCase());  
        }  
        return result.toString();  
    }
    
    
    public static void main(String args[]){
    	try {
    		String account ="13928980514";
    		account = MyDesUtils.encryptDESBeas64(account);
//    		account = des.encode(account.getBytes());
			System.out.println("account密文="+account);
			account = MyDesUtils.decryptBeas64DES(account);
			System.out.println("account明文="+account);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
