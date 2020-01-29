package com.yepit.flow.service.atom.interfaces;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/26.
 */
public interface IAcitivityFlowService {

    /**
     * 添加用户信息
     */
    public void addUser(List<UserEntity> userEntityList) ;

    /**
     *  添加角色组
     *@author  Cavin
     **/
    public void addRole(List<GroupEntity> groupEntityList) ;

    /**
     *  添加用户与角色关系
     *@author  Cavin
     **/
    public void addUserAndRoleRel(String userId,String groupId);

    /**
     * 启动流程
     * @param bizId 业务id
     */
    public String startProcesses(String keyId,String bizId,Map<String, Object> variables);

    /**
     *  查询流程实例信息
     *@author  Cavin
     **/
    public ProcessInstance  getProcesses(String id)throws Exception;
    /**
     *    查询正在执行的组任务列表
     *@author  Cavin
     **/
    public  List<IdentityLink> findGroupCandidate(String taskId);
    /**
     *  根据用户id查询待办任务列表
     *@author  Cavin
     **/
    public List<Task> findTasksByUserId(String keyId,String userId);
    /**
     * 根据key值获取流程变量值
     *@author  Cavin
     **/
    public Object getVariable(String taskId,String variableName) throws Exception;
    /**
     *  查询组任务
     *@author  Cavin
     **/
    public void findGroupTaskList(String candidateUser);
    /**
     * 根据用户id 获取该用户在某个流程实例的能够接受的任务列表
     * @param userId
     * @return
     */
    public List<Task> findTaskByProcessAndCandidate(String userId,String processId)throws Exception;
    /**
     *  拾取任务，将组任务分给个人任务，指定任务的办理人字段
     *@author  Cavin
     **/

    public void claim(String taskId,String userId);
    /**
     *   被认领之后的任务可再扯回组任务（前提：之前这个任务是组任务），其他人员则还可以认领
     *@author  Cavin
     **/

    public void setAssignee(String taskId);

    /**
     *  组任务可以继续添加组参与人员
     *@author  Cavin
     **/
    public void addGroupUser(String taskId,String userId);

    /**
     *  组任务可以删除参与人员
     *@author  Cavin
     **/

    public void deleteGroupUser(String taskId,String userId);

    /**
     *  描述:任务审批 	（ 0：不通过/1：通过/2： 打回重新提交）
     */
    public void completeTask(String taskId,String userId,Map<String,Object> vars);
    /**
     *  描述:任务审批 ，增加备注信息	（ 0：不通过/1：通过/2： 打回重新提交）
     */
    public void completeTask(String taskId,String userId,Map<String,Object> vars,String remark,String id)throws Exception;
    /**
     * 更改业务流程状态#{ActivityDemoServiceImpl.updateBizStatus(execution,"tj")}
     * @param execution
     * @param status
     */
    public void updateBizStatus(DelegateExecution execution, String status);

    /**
     *  获取task实例
     *@author  Cavin
     **/
    public Task getTaskById(String taskId)throws Exception;

    /**
     *c查询当前人的组任务
     *@author  Cavin
     **/
    public void findMyGroupTask(String candidateUser);

    /**
     *  生成流程图
     * 首先启动流程，获取processInstanceId，替换即可生成
     *@author  Cavin
     **/
    public void queryProImg(String processInstanceId) throws Exception;

    /**
     * 流程图高亮显示
     * 首先启动流程，获取processInstanceId，替换即可生成
     * @throws Exception
     */
    public void queryProHighLighted(String processInstanceId) throws Exception;


}
