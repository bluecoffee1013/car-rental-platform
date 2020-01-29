package com.yepit.mapp.crp.dto.file;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by qianlong on 2017/8/24.
 */
@Data
public class FileInfoDTO implements Serializable {

    private static final long serialVersionUID = 3940175898365866969L;

    private String fileId;

    private String fileName;

    private String link;

}
