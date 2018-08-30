package com.mids.common.datasign;

/**
 * RSA加密算法的演示验证
 * RSA是一种分组加密算法
 * 注意:密钥对采用的长度决定了加密块的长度，我这里取的是2048，即256byte
 * 由于加密块的长度固定为256，因此明文的长度至多为256 - 11 = 245byte
 * 我这里明文长度取的是128byte，密文长度为256byte，它适合于小文件加密
 */

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import com.mids.util.Base64Util;


public class DataSignUtil {

	/**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
   
    
    public  byte[] encodedData;

    
    /**
     * 获取公钥的key
     */
    private String PUBLIC_KEY = "RSAPublicKey";
    
    /**
     * 获取私钥的key
     */
    private String PRIVATE_KEY = "RSAPrivateKey";
    public Map<String, Object> keyMap = new HashMap<String, Object>(2);;
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public void genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
    }
    /**
     * <p>
     * 获取私钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public String getPrivateKey() throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Util.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public String getPublicKey()
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Util.encode(key.getEncoded());
    }
    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * 
     * @return
     * @throws Exception
     */
    public String sign(String param, String privateKey) throws Exception {
    	
    	byte[] data = encryptByPrivateKey(param.getBytes(), privateKey);
    	encodedData = data;
    	
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        
        String result = Base64Util.encode(signature.sign());
        
        return result;
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     * 
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     * 
     */
    public boolean verify(String publicKey, String sign) throws Exception {
    	
    	byte[] data = encodedData;
    	
        byte[] keyBytes = Base64Util.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        
        boolean flag = signature.verify(Base64Util.decode(sign));
        
        return flag;
    }

    
    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    
	 
    
    public static void main(String[] args) throws Exception {
		String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnD0F1MOSbY7JfMPIyfZynsPlBFSxLP+3lDAxm"+
    		"sO0ZhdmxprRpyWFSMk8vasb51JrzPr9L7Zw1u4Tg+MNcMkHFCgNr4RmCv73hCpK8LEoleHSrp+Ar"+
    		"5HijGElAtGJBh7YnSEcUeLGxojr1PAAToBTfgrArEMRbtLeCTxcN2tYHUQIDAQAB";
		String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKcPQXUw5Jtjsl8w8jJ9nKew+UEV"+
			"LEs/7eUMDGaw7RmF2bGmtGnJYVIyTy9qxvnUmvM+v0vtnDW7hOD4w1wyQcUKA2vhGYK/veEKkrws"+
			"SiV4dKun4CvkeKMYSUC0YkGHtidIRxR4sbGiOvU8ABOgFN+CsCsQxFu0t4JPFw3a1gdRAgMBAAEC"+
			"gYBAnpANSppC2O94DrDAgwIDg1sGp6/4c4QLovtxBWLRCotIQllqyfCRINCsYLsSjABbspvAPOq9"+
			"hglCFDdNBEuX0eXbqPD7ybJvNG7Z6oT6Rip1BXSp1YOMxT2oDwyr4zPbM2ICZuWGad+Ek6FpR6Od"+
			"zsT+2dgnkQI7FUGTIltoAQJBANFxvRMPCz2pA67OBUYCr/ez28o3hPUvkXHq5UMwNw4pbrTHi7X1"+
			"sK3G9d5nEV/tkUdH/YWwfS2I1LiH1gZX4cECQQDMMaZA2w55wgwBXXFEC795UC4gk0+PRiMxe5wg"+
			"dbE0S8M6UK3F5/W7Nv9QSa9e56ZElqz/42beLIQbsWh3N2mRAkB55ETSm7XCC+Qdzr7Y8OVyNlWa"+
			"yVbyzt1JvBfzU1MnRcutIFjZ7AQIDru21KsS9hLPtkEAlicz4edTAZL4nBrBAkBLtvlLwS5b7Z0g"+
			"AHMYZTbJl/aSs6zjloFEPu6Ehr9/gbxRaksVQE647cq8DaaOjVz0v95EQCELrvwJfL4cvppRAkBg"+
			"mF8+D8B4lleXNMILSGhHFOo7DS8Xsnu1OFeqa+LZ9QuLOZsXv6FV64WzikgDccvO9/YqFHyepG1x"+
			"D7iik5AA";

        String param = "id=1&name=张三";
        
        DataSignUtil naviSign = new DataSignUtil();
        naviSign.genKeyPair();
        String privatekey = naviSign.getPrivateKey();
        String publickey = naviSign.getPublicKey();
        String sign = naviSign.sign(param, privateKey);
        System.err.println("签名：" + sign);
        
        boolean status = naviSign.verify(publicKey, sign);
        System.err.println("签名验证结果：" + status);
    }

}
