package com.yepit.flow.service.atom.impl;


import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTask;
import com.yepit.flow.common.constant.FlowConst;
import com.yepit.flow.dao.interfaces.IBusMilestoneDAO;
import com.yepit.flow.dto.AdminDTO;
import com.yepit.flow.service.atom.interfaces.IBusMileStoneService;
import com.yepit.flow.service.atom.interfaces.IBusTaskService;
import com.yepit.flow.service.atom.interfaces.ITaskListener;
import com.yepit.mapp.framework.util.TimeUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2018/10/30.
 */
@Service("taskListener")
public class TaskListenerImpl implements ITaskListener {
    @Autowired
    IBusMilestoneDAO busMilestoneDAO;
    @Autowired
    IBusMileStoneService busMileStoneService;
    @Autowired
    IBusTaskService busTaskService;

    @Value("${api.services.getTaskAssigner}")
    private String getTaskAssignerUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 指定个人任务和组任务的办理人
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = "张san";
        System.out.println(delegateTask.getId() + "****************" + assignee);
        //指定个人任务
        delegateTask.setAssignee(assignee);
    }

    @Override
    public void notifyBefore(DelegateTask delegateTask, Long userId, String userName, Long busId) throws Exception {


        //记录milestone状态
        BusMilestone busMilestone = busMileStoneService.queryMileStoneByCond(busId,delegateTask.getTaskDefinitionKey());
        if (null == busMilestone) {
            busMilestone = new BusMilestone();
        }
        //获取分配的客户id

        if (!FlowConst.FlowNodeId.UPLOAD_CUSTOMER_APPLY.equals(delegateTask.getTaskDefinitionKey())) {
            //获取分配的客户id
            String requestUrl = getTaskAssignerUrl + "?nodeName=" + delegateTask.getTaskDefinitionKey();
            AdminDTO assigner = restTemplate.getForObject(requestUrl,AdminDTO.class);

            if (null != assigner) {
                    userId = assigner.getAdminId();

                    userName = assigner.getRealname();
                }
                if (null == userId) {
                    userId = 0L;//userId不能为空
                }

            }

        if (FlowConst.FlowNodeId.UPLOAD_CUSTOMER_APPLY.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            if (null !=busId && busId>0) {
                busMilestone.setAssigementUser(userId);
                busMilestone.setAssigementUserName(userName);
                busMilestone.setCurrentTask(FlowConst.FlowNode.MODIFY_CUSTOMER_APPLY);
            }else {
                return;
            }

        } else if (FlowConst.FlowNodeId.FIRST_TRIAL.equals(delegateTask.getTaskDefinitionKey())) {

            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.FIRST_TRIAL);

        } else if (FlowConst.FlowNodeId.SECOND_TRIAL.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.SECOND_TRIAL);
        } else if (FlowConst.FlowNodeId.FACE_TRIAL.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.FACE_TRIAL);
        } else if (FlowConst.FlowNodeId.NOTIFY_FACE_SIGN.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.NOTIFY_FACE_SIGN);
        } else if (FlowConst.FlowNodeId.FACE_INTERVIEW.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.FACE_INTERVIEW);
        } else if (FlowConst.FlowNodeId.APPOINTMENT_VIST.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.APPOINTMENT_VIST);
        } else if (FlowConst.FlowNodeId.HOME_VISTING.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.HOME_VISTING);
        } else if (FlowConst.FlowNodeId.INSURANCE_AFFIRM.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.INSURANCE_AFFIRM);
        } else if (FlowConst.FlowNodeId.CAR_CONFIRM.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.CAR_CONFIRM);
        } else if (FlowConst.FlowNodeId.FINAL_JUDGMENT.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.FINAL_JUDGMENT);
        } else if (FlowConst.FlowNodeId.SEND_CAR.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.SEND_CAR);
        } else if (FlowConst.FlowNodeId.INSTALL_GPRS.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.INSTALL_GPRS);
        } else if (FlowConst.FlowNodeId.REFUND.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.REFUND);
        } else if (FlowConst.FlowNodeId.MAKE_LOANS.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.MAKE_LOANS);
        } else if (FlowConst.FlowNodeId.SEND_CAR_CM.equals(delegateTask.getTaskDefinitionKey())) {
            delegateTask.setAssignee(userId.toString());
            busMilestone.setAssigementUser(userId);
            busMilestone.setAssigementUserName(userName);
            busMilestone.setCurrentTask(FlowConst.FlowNode.SEND_CAR_CM);
        }


        busMilestone.setTaskId(delegateTask.getId());

        busMilestone.setBusId(busId);
        busMilestone.setStatus(FlowConst.MileStoneStatus.DOING);
        busMilestone.setStartDate(TimeUtil.getSysDate());
        busMilestone.setTaskDefId(delegateTask.getTaskDefinitionKey());

        busMilestoneDAO.saveBusMileStone(busMilestone);


    }

    @Override
    public void notifyAfter(DelegateTask delegateTask, Long userId, String userName, Long busId) throws Exception {
        BusMilestone busMilestone = new BusMilestone();
        busMilestone.setAssigementUser(Long.valueOf(delegateTask.getAssignee()));
        busMilestone.setTaskId(delegateTask.getId());
        busMilestone.setTaskDefId(delegateTask.getTaskDefinitionKey());
        busMilestone.setBusId(busId);
        BusMilestone updateMileStone = busMileStoneService.queryMileStoneByCond(busMilestone);
        if (null != updateMileStone) {
            updateMileStone.setEndDate(TimeUtil.getSysDate());
            updateMileStone.setStatus(FlowConst.MileStoneStatus.END);
            busMileStoneService.updateMileStone(updateMileStone);
        }


    }

    @Override
    public void endNode(Long userId, String userName, Long busId) throws Exception {


        //更新任务完成时间
        BusTask busTask = busTaskService.getBusTaskByBusId(busId);
        if (null != busTask) {
            busTask.setEndDate(TimeUtil.getSysDate());
            busTask.setStatus(FlowConst.BusTaskStatus.END);
            busTaskService.updateBusTask(busTask);
        }


    }
}
