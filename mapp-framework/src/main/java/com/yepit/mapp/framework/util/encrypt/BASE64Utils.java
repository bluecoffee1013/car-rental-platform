package com.yepit.mapp.framework.util.encrypt;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by qianlong on 2018/1/28.
 */
public class BASE64Utils {

    /**
     * 加密
     * 字符串加密为字符串
     * @param source
     * @return
     */
    public static String encode(String source) {
        return encodeByte(source.getBytes());
    }

    /**
     * 解密
     * 字符串解密为字符串
     * @param source
     * @return
     */
    public static String decode(String source) {
        return new String(decodeToByte(source));
    }

    /**
     * 加密
     * 加密byte[]类型，密文为字符串
     * @param b
     * @return
     */
    public static String encodeByte(byte[] b) {
        return new String(new Base64().encode(b));
    }

    /**
     * 解密
     * 将字符串解密为byte[]类型
     * @param source
     * @return
     */
    public static byte[] decodeToByte(String source) {
        return new Base64().decode(source.getBytes());
    }

//    /**
//     * 使用示例
//     * @param args
//     */
//    public static void main(String[] args) {
//        String source = "12dfefDKLJKLKL464d中文f465as43f1a3 f46e353D1F34&*^$E65F46EF43456abcd54as56f00ef";
//
//        String encodedStr = BASE64Utils.encode(source);
//        System.out.println("BASE64加密结果：");
//        System.out.println(encodedStr);
//
//        String decodedStr = BASE64Utils.decode(encodedStr);
//        System.out.println("BASE64解密结果：");
//        System.out.println(decodedStr);
//    }
}
