package com.yepit.mapp.crp.mapper.system;


import com.yepit.mapp.crp.domain.system.FileUpload;

public interface FileUploadMapper {
    int deleteByPrimaryKey(String fileId);

    int insert(FileUpload record);

    int insertSelective(FileUpload record);

    FileUpload selectByPrimaryKey(String fileId);

    int updateByPrimaryKeySelective(FileUpload record);

    int updateByPrimaryKey(FileUpload record);
}