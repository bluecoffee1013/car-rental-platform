package com.yepit.mapp.crp.service;

import com.yepit.mapp.crp.mongo.entity.CheckForm;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CheckFormService {

    @Autowired
    private Datastore ds;

    public BaseResponse addApplyInfo(){
        CheckForm checkForm = new CheckForm();
        checkForm.setOperatorId("1");
        checkForm.setCreateTime(new Date());
        checkForm.setStatus(1);
        ds.save(checkForm);
        return new BaseResponse().successful("操作成功");
    }
}
