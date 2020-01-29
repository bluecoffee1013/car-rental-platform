package com.yepit.mapp.framework.util.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianlong on 2018/1/28.
 */
public class RSAUtils {

    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_SHA1PRNG = "SHA1PRNG";
    private static final int KEY_SIZE = 1024;
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final String TRANSFORMATION = "RSA/None/NoPadding";

    /**
     * 解密 
     * 用私钥解密，解密字符串，返回字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        return new String(decryptByPrivateKey(BASE64Utils.decodeToByte(data), key));
    }

    /**
     * 加密 
     * 用公钥加密，加密字符串，返回用base64加密后的字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        return encryptByBytePublicKey(data.getBytes(), key);
    }
    /**
     * 加密 
     * 用公钥加密，加密byte数组，返回用base64加密后的字符串 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByBytePublicKey(byte[] data, String key) throws Exception {
        return BASE64Utils.encodeByte(encryptByPublicKey(data, key));
    }

    /**
     * 解密 
     * 用私钥解密 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = BASE64Utils.decodeToByte(key);//对私钥解密  
        /*取得私钥*/
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);  
        /*对数据解密*/
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());//相同的原文、公钥能生成相同的密文。如果使用Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());//相同的原文、公钥生成的密文不同  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 加密 
     * 用公钥加密 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = BASE64Utils.decodeToByte(key);//对公钥解密  
        /*取得公钥*/
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);  
        /*对数据加密*/
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, new BouncyCastleProvider());//相同的原文、公钥能生成相同的密文。如果使用Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());//相同的原文、公钥生成的密文不同  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥 
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return BASE64Utils.encodeByte(key.getEncoded());
    }

    /**
     * 取得公钥 
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return BASE64Utils.encodeByte(key.getEncoded());
    }

    /**
     * 初始化公钥和私钥 
     * @param seed
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey(String seed) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        SecureRandom random = SecureRandom.getInstance(ALGORITHM_SHA1PRNG);//如果使用SecureRandom random = new SecureRandom();//windows和linux默认不同，导致两个平台生成的公钥和私钥不同  
        random.setSeed(seed.getBytes());//使用种子则生成相同的公钥和私钥  
        keyPairGen.initialize(KEY_SIZE, random);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();//公钥  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();//私钥  

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

//    /**
//     * 使用示例
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String[] args) throws Exception {
//        String source = "loginName=qianlong";//原文
//        String seed = "abc123";//种子
//
//        System.out.println("原文：\n" + source);
//        Map<String, Object> keyMap = RSAUtils.initKey(seed);//初始化密钥
//        String publicKey = RSAUtils.getPublicKey(keyMap);//公钥
//        String privateKey = RSAUtils.getPrivateKey(keyMap);//私钥
//        System.out.println("公钥：\n" + publicKey);
//        System.out.println("私钥：\n" + privateKey);
//        String encodedStr = RSAUtils.encryptByPublicKey(source, publicKey);//加密
//        System.out.println("密文：\n" + encodedStr);
//        String decodedStr = RSAUtils.decryptByPrivateKey(encodedStr, privateKey);//解密
//        System.out.println("解密后的结果：\n" + decodedStr);
//    }
}
