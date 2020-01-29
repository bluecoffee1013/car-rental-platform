package com.yepit.mapp.crp.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yepit.mapp.crp.mongo.entity.FlowInfo;
import com.yepit.mapp.crp.mongo.entity.FlowNode;
import com.yepit.mapp.framework.exception.ServiceException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FlowInfoService {

    @Autowired
    private Datastore ds;

    public void deleteAll(){
        Query<FlowInfo> queryCond = ds.createQuery(FlowInfo.class);
        ds.delete(queryCond);
    }

    public void flowInit(){
        Query<FlowInfo> queryCond = ds.createQuery(FlowInfo.class);
        ds.delete(queryCond);
        FlowInfo flowInfo = new FlowInfo();
        flowInfo.setFlowId(1);
        flowInfo.setFlowName("汽车租赁审批流程");
        ds.save(flowInfo);
    }

}
