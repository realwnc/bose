package com.mids.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MyMD5Util
{
	public static String getMD5String(String str)
	{
		return DigestUtils.md5Hex(str);
	}
	
	public static String getMD5File(File file)
	{
		FileInputStream fileInputStream = null;
		try {
			MessageDigest MD5 = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				MD5.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(MD5.digest()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fileInputStream != null){
					fileInputStream.close();
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getMD5Byte(byte[] buffer)
	{
		try {
			MessageDigest MD5 = MessageDigest.getInstance("MD5");
			return new String(Hex.encodeHex(MD5.digest(buffer)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String md5Hex(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new String(new Hex().encode(digest));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return "";
		}
	}
	
	/**
	 * 加盐MD5加密
	 * <p>
	 * 
	 * @Title : getSaltMD5
	 *        </p>
	 *        <p>
	 * @Description : TODO
	 *              </p>
	 *              <p>
	 * @Author : HuaZai
	 *         </p>
	 *         <p>
	 * @Date : 2017年12月27日 上午11:21:00
	 *       </p>
	 */
	public static String getSaltMD5(String password) {
		// 生成一个16位的随机数
		Random random = new Random();
		StringBuilder sBuilder = new StringBuilder(16);
		sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
		int len = sBuilder.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sBuilder.append("0");
			}
		}
		// 生成最终的加密盐
		String Salt = sBuilder.toString();
		password = md5Hex(password + Salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = Salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}

	/**
	 * 验证加盐后是否和原文一致
	 * <p>
	 * 
	 * @Title : verifyMD5
	 *        </p>
	 *        <p>
	 * @Description : TODO
	 *              </p>
	 *              <p>
	 * @Author : HuaZai
	 *         </p>
	 *         <p>
	 * @Date : 2017年12月27日 下午2:22:22
	 *       </p>
	 */
	public static boolean getSaltverifyMD5(String password, String md5str) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5str.charAt(i);
			cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
			cs2[i / 3] = md5str.charAt(i + 1);
		}
		String Salt = new String(cs2);
		return md5Hex(password + Salt).equals(String.valueOf(cs1));
	}
	
	public static void main(String[] args) {
		String plaintext="wncheng";
		String commmd5str = MyMD5Util.getMD5String(plaintext);
		System.out.println("普通MD5：" + commmd5str);
		// 获取加盐后的MD5值
		String ciphertext = MyMD5Util.getSaltMD5(plaintext);
		System.out.println("加盐后MD5：" + ciphertext);
		System.out.println("是否是同一字符串:" + MyMD5Util.getSaltverifyMD5(plaintext, ciphertext));
		/*
		long beginTime = System.currentTimeMillis();
		File file = new File("D:/1/pdi-ce-7.0.0.0-24.zip");
		String md5 = MyMD5Util.getMD5File(file);
		long endTime = System.currentTimeMillis();
		System.out.println("MD5:" + md5 + "\n 耗时:" + ((endTime - beginTime) / 1000) + "s");
		*/
	}
}