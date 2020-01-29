package com.yepit.mapp.crp.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class FlowNodeService {

    @Autowired
    private Datastore ds;

    public void flowNodeInit(){
        try{
            Query<FlowNode> queryCond = ds.createQuery(FlowNode.class);
            ds.delete(queryCond);
            Resource resource = new ClassPathResource("flowNodeDef.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            JsonParser parser = new JsonParser();//创建解析器
            JsonArray jsonArray = (JsonArray) parser.parse(br);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                FlowNode flowNode = new FlowNode();
                flowNode.setNodeId(jsonObject.get("nodeId").getAsLong());
                flowNode.setNodeName(jsonObject.get("nodeName").getAsString());
                flowNode.setNodeAliasName(jsonObject.get("nodeAliasName").getAsString());
                flowNode.setFlowId(jsonObject.get("flowId").getAsInt());
                flowNode.setRoleId(jsonObject.get("roleId").getAsLong());
                flowNode.setRoleName(jsonObject.get("roleName").getAsString());
                ds.save(flowNode);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new ServiceException("流程节点信息初始化失败");
        }
    }

    /**
     * 根据nodeId获取唯一对象
     * @param nodeId
     * @return
     */
    public FlowNode getFlowNode(Long nodeId){
        FlowNode flowNode = ds.createQuery(FlowNode.class)
                .filter("_id =",nodeId)
                .get();
        return flowNode;
    }
}
