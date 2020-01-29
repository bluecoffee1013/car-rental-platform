package com.yepit.flow.service.atom.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import com.yepit.flow.service.atom.interfaces.IAcitivityFlowService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityFLowServiceImpl")
public class ActivityFlowServiceImpl implements IAcitivityFlowService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityFlowServiceImpl.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    ProcessEngine processEngine;

    /**
     * 添加用户信息
     *  String id;
     *  String firstName;
     *  String lastName;
     *  String email;
     *  String password;
     */
    public void addUser(List<UserEntity> userEntityList) {
        /** 添加用户角色组 */
        IdentityService identityService = processEngine.getIdentityService();//
        if (null != userEntityList && userEntityList.size()>0) {
            for (UserEntity userEntity:userEntityList ) {
                identityService.saveUser(userEntity);
            }
        }

    }
    /**
     *  添加角色组
     *  String id;
     *  int revision;
     *  String name;
     *  String type;
     *@author  Cavin
     **/
    public void addRole(List<GroupEntity> groupEntityList) {
        /** 添加用户角色组 */
        IdentityService identityService = processEngine.getIdentityService();//
        if (null != groupEntityList && groupEntityList.size()>0) {
            for (GroupEntity groupEntity:groupEntityList) {
                identityService.saveGroup(groupEntity);
            }
        }

    }
    /**
     *  添加用户与角色关系
     *  String userId
     *  String groupId
     *@author  Cavin
     **/
    public void addUserAndRoleRel(String userId,String groupId) {
        /** 添加用户角色组 */
        IdentityService identityService = processEngine.getIdentityService();//

        // 建立用户和角色的关联关系
        identityService.createMembership(userId,groupId);

    }
    /**
     *  ** 拾取任务，将组任务分给个人任务，指定任务的办理人字段
     *  claim与setAssignee区别在于claim领取之后别人不可以再领取不然会报错而setAssignee则不然
     认领之后其他人员再查询时则不存在，而act_ru_task表中assignee字段为认领人‘a’，认领之后其他操作和一起都一样
     *  taskId 任务id
     *  userId 用户id
     *@author  Cavin
     **/
    public void claim(String taskId,String userId) {

        processEngine.getTaskService().claim(taskId, userId);
    }

    /**
     *   被认领之后的任务可再扯回组任务（前提：之前这个任务是组任务），其他人员则还可以认领
     *@author  Cavin
     **/
    public void setAssignee(String taskId){
        processEngine.getTaskService().setAssignee(taskId, null);

    }

    /**
     *  组任务可以继续添加组参与人员
     *@author  Cavin
     **/
    public void addGroupUser(String taskId,String userId){

        processEngine.getTaskService() .addCandidateUser(taskId, userId);

    }
    /**
     *  组任务可以删除参与人员
     *@author  Cavin
     **/

    public void deleteGroupUser(String taskId,String userId){

        processEngine.getTaskService().deleteCandidateUser(taskId, userId);

    }

    /**
     * 启动流程
     * @param bizId 业务id
     */
    public String startProcesses(String keyId,String bizId,Map<String, Object> variables) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(keyId, bizId,variables);//流程图id，业务表id
        return pi.getProcessInstanceId();
    }

    @Override
    public ProcessInstance getProcesses(String id) throws Exception {
        return  runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    /**查询正在执行的组任务列表*/

    public  List<IdentityLink> findGroupCandidate(String taskId){
        //任务ID

        List<IdentityLink> list = processEngine.getTaskService()//
                .getIdentityLinksForTask(taskId);

        if(list!=null && list.size()>0){
            for(IdentityLink identityLink:list){
                System.out.println("任务ID："+identityLink.getTaskId());
                System.out.println("流程实例ID："+identityLink.getProcessInstanceId());
                System.out.println("用户ID："+identityLink.getUserId());
                System.out.println("工作流角色ID："+identityLink.getGroupId());
                System.out.println("#########################################");
            }
        }
        return list;
    }
    /**
     * 根据用户id 获取该用户在某个流程实例的能够接受的任务列表
     * @param userId
     * @return
     */
    public List<Task> findTaskByProcessAndCandidate(String userId,String processId)throws Exception{
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processId).taskCandidateOrAssigned(userId).active();
        List<Task> resultTask = taskQuery.list();
        return resultTask;
    }

    /**查询组任务*/
    public void findGroupTaskList(String candidateUser){
        //任务办理人

        List<Task> list = processEngine.getTaskService()//
                .createTaskQuery()//
                .taskCandidateUser(candidateUser)//参与者，组任务查询
                .list();
        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID："+task.getId());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务的创建时间："+task.getCreateTime());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("#######################################");
            }
        }
    }

   /**
    *  根据用户id查询待办任务列表
    *@author  Cavin
    **/
    public List<Task> findTasksByUserId(String keyId,String userId) {
        List<Task> resultTask = taskService.createTaskQuery().processDefinitionKey(keyId).taskCandidateOrAssigned(userId).list();
        if (resultTask != null && resultTask.size() > 0) {
            for (Task task : resultTask) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }
        return resultTask;
    }

    @Override
    public Object getVariable(String taskId,String variableName) throws Exception {
        return taskService.getVariable(taskId,variableName);
    }

    /**
     *  描述:任务审批 	（ 0：不通过/1：通过/2： 打回重新提交）
     * @param taskId 任务id
     * @param userId 用户id
     */
    public void completeTask(String taskId,String userId,Map<String,Object> vars) {
        //获取流程实例
        taskService.claim(taskId, userId);

        taskService.complete(taskId, vars);
    }
    /**
        *  描述:任务审批,添加备注信息 	（ 0：不通过/1：通过/2： 打回重新提交）
            * @param taskId 任务id
     * @param userId 用户id
     */
    public void completeTask(String taskId,String userId,Map<String,Object> vars,String remark,String id) {
        //获取流程实例
        taskService.claim(taskId, userId);
        //添加备注信息
        taskService.addComment(taskId,id,remark);

        taskService.complete(taskId, vars);
    }
    /**
     * 更改业务流程状态
     * @param execution
     * @param status
     */
    public void updateBizStatus(DelegateExecution execution,String status) {
        String bizId = execution.getProcessBusinessKey();
        //根据业务id自行处理业务表
        System.out.println("业务表["+bizId+"]状态更改成功，状态更改为："+status);
    }

    @Override
    public Task getTaskById(String taskId) throws Exception {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public void findMyGroupTask(String candidateUser) {

        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                /** 查询条件（where部分） */
                .taskCandidateUser(candidateUser)// 组任务的办理人查询
                /** 排序 */
                .orderByTaskCreateTime().asc()// 使用创建时间的升序排列
                /** 返回结果集 */
                .list();// 返回列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }

    }

    /**
     *    生成流程图
     * 首先启动流程，获取processInstanceId，替换即可生成
     *@author  Cavin
     **/
    public void queryProImg(String processInstanceId) throws Exception {
        //获取历史流程实例
        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //根据流程定义获取输入流
        InputStream is = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        BufferedImage bi = ImageIO.read(is);
        File file = new File("demo2.png");
        if(!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(bi, "png", fos);
        fos.close();
        is.close();
        System.out.println("图片生成成功");

        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("userId").list();
        for(Task t : tasks) {
            System.out.println(t.getName());
        }
    }


    /**
     * 流程图高亮显示
     * 首先启动流程，获取processInstanceId，替换即可生成
     * @throws Exception
     */
    public void queryProHighLighted(String processInstanceId) throws Exception {
        //获取历史流程实例
        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();

        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity,highLightedActivitList);

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        //配置字体
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows,"宋体","微软雅黑","黑体",null,2.0);
        BufferedImage bi = ImageIO.read(imageStream);
        File file = new File("demo2.png");
        if(!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ImageIO.write(bi, "png", fos);
        fos.close();
        imageStream.close();
        System.out.println("图片生成成功");
    }
    /**
     * 获取需要高亮的线
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private List<String> getHighLightedFlows(
            ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1)
                            .getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances
                        .get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances
                        .get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(
                        activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl
                    .getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
                        .getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}
