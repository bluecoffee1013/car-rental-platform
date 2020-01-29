package com.yepit.mapp.crp.mongo.entity;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

@Entity("oss_info")
@Data
public class OssInfo {

    @Id
    private String fileId;

    private String fileName;

    private String link;

    private String uploadUserId;

    private String uploadUserName;

    private Date uploadTime;
}
