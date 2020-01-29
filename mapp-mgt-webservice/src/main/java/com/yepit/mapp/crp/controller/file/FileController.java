package com.yepit.mapp.crp.controller.file;

import com.yepit.mapp.crp.common.annotation.AdminLogin;
import com.yepit.mapp.crp.common.annotation.SystemLoginUser;
import com.yepit.mapp.crp.dto.file.FileInfoDTO;
import com.yepit.mapp.crp.service.FileService;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.SimpleUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "文件处理")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传")
    @AdminLogin
    @PostMapping(value = "/fileUpload", produces = "application/json")
    @ResponseBody
    public BaseResponse<FileInfoDTO> uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile,
                                                @ApiIgnore @SystemLoginUser SimpleUserInfo uploadUser) {
        BaseResponse<FileInfoDTO> resp = fileService.uploadFile(uploadFile, uploadUser);
        return resp;
    }

    @ApiOperation(value = "文件删除")
    @AdminLogin
    @DeleteMapping(value = "/file/{fileId}", produces = "application/json")
    @ResponseBody
    public BaseResponse deleteFile(@PathVariable String fileId){
        return fileService.deleteFile(fileId);
    }

}
