package com.yepit.mapp.crp.service;

import com.yepit.mapp.crp.dto.file.FileInfoDTO;
import com.yepit.mapp.crp.mongo.entity.OssInfo;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.SimpleUserInfo;
import com.yepit.mapp.framework.exception.ServiceException;
import com.yepit.mapp.framework.util.idgenerate.SnowflakeIdWorker;
import com.yepit.mapp.framework.util.oss.AliyunOssUtils;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by qianlong on 2017/8/23.
 */
@Service
public class FileService {

    final static Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private Datastore ds;

    @Value("${aliyun.oss.baseUrl}")
    private String aliyunOssUrl;

    /**
     * 上传文件
     *
     * @param uploadFile
     * @param uploadUser
     * @return
     * @throws ServiceException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, Exception.class})
    public BaseResponse<FileInfoDTO> uploadFile(MultipartFile uploadFile, SimpleUserInfo uploadUser) throws ServiceException {
        if (uploadFile == null || uploadFile.isEmpty()) {
            throw new ServiceException("000001", "上传的文件不能为空");
        }

        //文件存储到指定磁盘上
        String fileName = uploadFile.getOriginalFilename();
        String filePostfix = fileName.substring(fileName.lastIndexOf("."), fileName.length());

        //获取新的文件名
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long fileId = idWorker.nextId();
        String newFileName = fileId + filePostfix;
        String newFilePath = "flow" + File.separator + newFileName;
        logger.info("Upload file to oss:" + newFilePath);

        //上传到阿里云OSS
        String fileLink = "";
        try {
            InputStream inputStream = uploadFile.getInputStream();
            AliyunOssUtils.sampleUpload(inputStream, newFilePath);
            fileLink = aliyunOssUrl + "/" + newFileName;
        } catch (Exception ex) {
            throw new ServiceException("上传文件到OSS异常:" + ex.getMessage());
        }

        //保存数据到mongodb
        OssInfo ossInfo = new OssInfo();
        ossInfo.setFileId(String.valueOf(fileId));
        ossInfo.setFileName(newFileName);
        ossInfo.setLink(fileLink);
        ossInfo.setUploadUserId(uploadUser.getUserId());
        ossInfo.setUploadUserName(uploadUser.getRealName());
        ossInfo.setUploadTime(new Date());
        ds.save(ossInfo);

        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        BeanUtils.copyProperties(ossInfo, fileInfoDTO);

        BaseResponse<FileInfoDTO> resp = new BaseResponse<FileInfoDTO>().successful("文件上传成功");
        resp.setResult(fileInfoDTO);
        return resp;
    }

    /**
     * 从OSS上删除文件
     *
     * @param fileId
     * @return
     */
    public BaseResponse deleteFile(String fileId) {
        OssInfo fileInfo = ds.createQuery(OssInfo.class).filter("_id =", fileId).get();
        if (fileInfo != null) {
            AliyunOssUtils.deleteFile("flow/" + fileInfo.getFileName());
            ds.delete(fileInfo);
        }
        return new BaseResponse().successful("删除文件成功");
    }
}
