package com.yepit.mapp.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

/**
 * Created by qianlong on 2017/8/23.
 */
public class FileUtils {

    /**
     * 将文件转化成Base64串
     * @param path
     * @return
     */
    public static String fileToBase64(String path) {
        File file = new File(path);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.getEncoder().encodeToString(buffer);
        } catch (Exception e) {
            throw new RuntimeException("文件路径无效\n" + e.getMessage());
        }
    }

    /**
     * 将Base64 转换为file文件
     * @param base64
     * @param path
     * @return
     */
    public static boolean base64ToFile(String base64, String path) {
        byte[] buffer;
        try {
            buffer = Base64.getDecoder().decode(base64);
            FileOutputStream out = new FileOutputStream(path);
            out.write(buffer);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("base64字符串异常或地址异常\n" + e.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception{
//        String pdfPath = "/Users/qianlong/Documents/weblogic的集群与配置.pdf";
//        String base64Str = FileUtils.fileToBase64(pdfPath);
//        System.out.println(base64Str);
//
//    }
}
