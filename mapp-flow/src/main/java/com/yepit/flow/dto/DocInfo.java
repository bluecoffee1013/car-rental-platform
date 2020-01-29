package com.yepit.flow.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class DocInfo {

    /**
     * 文件ID
     */
    private String docId;

    /**
     * 文件类型
     */
    private String docType;

    /**
     * 文件下载url
     */
    private String link;

    /**
     * 文件名称
     */
    private String docName;
}
