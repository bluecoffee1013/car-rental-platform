package com.yepit.mapp.framework.util.oss;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 阿里云OSS工具类
 */
@SuppressWarnings("unchecked")
@Component
public class AliyunOssUtils {

    private final static Logger LOGGER = LogManager.getLogger(AliyunOssUtils.class);

    @Value("${aliyun.oss.endpoint}")
    private static String endpoint = "oss-cn-shenzhen.aliyuncs.com";

    @Value("${aliyun.oss.accessKeyId}")
    private static String accessKeyId = "LTAIK96SgF85zXed";

    @Value("${aliyun.oss.accessKeySecret}")
    private static String accessKeySecret = "p56ul7DmZB0ZL1IKtdJl7MGMkQt0Vc";

    @Value("${aliyun.oss.bucketName}")
    private static String bucketName = "car-rental-oa";

    /**
     * 简单上传
     *
     * @param uploadFile
     * @param newFileName
     */
    public static void sampleUpload(File uploadFile, String newFileName) {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        System.out.println("Getting Started with OSS SDK for Java\n");

        try {

            /*
             * Determine whether the bucket exists
             */
            if (!ossClient.doesBucketExist(bucketName)) {
                /*
                 * Create a new OSS bucket
                 */
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            /*
             * Upload an object to your bucket
             */
            LOGGER.debug("Uploading a new object to OSS from a file\n");
            ossClient.putObject(new PutObjectRequest(bucketName, newFileName, uploadFile));

            ossClient.setObjectAcl(bucketName, newFileName, CannedAccessControlList.PublicRead);
            ossClient.setObjectAcl(bucketName, newFileName, CannedAccessControlList.Default);

            ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, newFileName);

            System.out.println("ACL:" + objectAcl.getPermission().toString());

        } catch (OSSException oe) {
            LOGGER.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            LOGGER.error("Error Message: " + oe.getErrorCode());
            LOGGER.error("Error Code:       " + oe.getErrorCode());
            LOGGER.error("Request ID:      " + oe.getRequestId());
            LOGGER.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            LOGGER.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            LOGGER.error("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
            uploadFile.delete();
        }
    }

    @SuppressWarnings("unchecked")
    public static void sampleUpload(InputStream uploadFileStream, String newFileName) {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        System.out.println("Getting Started with OSS SDK for Java\n");

        try {

            /*
             * Determine whether the bucket exists
             */
            if (!ossClient.doesBucketExist(bucketName)) {
                /*
                 * Create a new OSS bucket
                 */
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            /*
             * Upload an object to your bucket
             */
            LOGGER.debug("Uploading a new object to OSS from a file\n");
            ossClient.putObject(new PutObjectRequest(bucketName, newFileName, uploadFileStream));

            ossClient.setObjectAcl(bucketName, newFileName, CannedAccessControlList.PublicRead);
            ossClient.setObjectAcl(bucketName, newFileName, CannedAccessControlList.Default);

            ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, newFileName);

            System.out.println("ACL:" + objectAcl.getPermission().toString());

        } catch (OSSException oe) {
            LOGGER.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            LOGGER.error("Error Message: " + oe.getErrorCode());
            LOGGER.error("Error Code:       " + oe.getErrorCode());
            LOGGER.error("Request ID:      " + oe.getRequestId());
            LOGGER.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            LOGGER.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            LOGGER.error("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
    }

    /**
     * 删除oss中的文件
     *
     * @param key
     */
    public static void deleteFile(String key) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteObject(bucketName, key);
        } catch (Exception ex) {
            LOGGER.error("Error Message: " + ex.getMessage());
            throw new ServiceException("删除文件异常:" + ex.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }
}
