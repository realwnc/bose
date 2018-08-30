package com.mids.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    /**
     * @param bytes
     * @return
     */
    public static byte[] decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    public static byte[] decode(String content) {
        return Base64.decodeBase64(content.getBytes());
    }

    public static String decodeToStr(String content) {
        return new String(Base64.decodeBase64(content.getBytes()));
    }

    public static String decodeToStr(String content, String charset) throws Exception {
        return new String(Base64.decodeBase64(content.getBytes(charset)), charset);
    }

    /**
     * 二进制数据编码为BASE64字符串
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static String encode(String content) {
        return new String(Base64.encodeBase64(content.getBytes()));
    }

    public static String encode(String content, String charset) throws Exception {
        return new String(Base64.encodeBase64(content.getBytes(charset)));
    }

    public static void main(String args[]) {
        String str = "fdsfds";
        String encodestr = Base64Util.encode(str);
        System.out.println("encodestr:" + encodestr);
        String dcodestr = Base64Util.decodeToStr(encodestr);
        System.out.println("dcodestr:" + dcodestr);
    }
}
