package com.yepit.flow.controller;

import com.yepit.flow.common.annotation.AdminLogin;
import com.yepit.flow.dto.FlowTaskDto;
import com.yepit.flow.dto.UserTaskDto;
import com.yepit.flow.service.atom.interfaces.IAcitivityFlowService;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/25.
 */
@Api(tags = "Activitiy工作流业务")
@RestController
public class ActivitiyFlowController {
    @Autowired
    IAcitivityFlowService acitivityService;

    /**
     * 完成任务
     *
     * @author Cavin
     **/
    @ApiOperation(value = "完成任务")
    @RequestMapping(value = "/finalTask", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse finalTask(@RequestBody FlowTaskDto flowTaskDto) throws Exception {
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        //1、创建
        Map<String, Object> mapVar = new HashMap<>();
        mapVar.put("result", flowTaskDto.getResult());
        if (!StringUtils.isEmpty(flowTaskDto.getCustomerManager())) {
            mapVar.put("customerManager", flowTaskDto.getCustomerManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getFirstTrialManager())) {
            mapVar.put("firstTrialManager", flowTaskDto.getFirstTrialManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getSecondRiskManager())) {
            mapVar.put("secondRiskManager", flowTaskDto.getSecondRiskManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getThirdRiskManager())) {
            mapVar.put("thirdRiskManager", flowTaskDto.getThirdRiskManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getFinancialManager())) {
            mapVar.put("financialManager", flowTaskDto.getFinancialManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getChannelManager())) {
            mapVar.put("channelManager", flowTaskDto.getChannelManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getInsuranceManager())) {
            mapVar.put("insuranceManager", flowTaskDto.getInsuranceManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getFinalJudgment())) {
            mapVar.put("finalJudgment", flowTaskDto.getFinalJudgment());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getSendCarManager())) {
            mapVar.put("sendCarManager", flowTaskDto.getSendCarManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getAfterSaleManager())) {
            mapVar.put("afterSaleManager", flowTaskDto.getAfterSaleManager());
        }
        if (!StringUtils.isEmpty(flowTaskDto.getMakeLoans())) {
            mapVar.put("makeLoans", flowTaskDto.getMakeLoans());
        }
        try {
            acitivityService.completeTask(flowTaskDto.getTaskId(), flowTaskDto.getUserId(), mapVar);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }

        return response;
    }

    /**
     * 查询任务列表 userId必填
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询任务列表")
    @RequestMapping(value = "/listTask", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<List<UserTaskDto>> listTask(@RequestBody FlowTaskDto flowTaskDto) throws Exception {
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        //1、创建

        try {
            List<Task> tasks = acitivityService.findTasksByUserId("carRental", flowTaskDto.getUserId());
            if (null != tasks && tasks.size() > 0) {
                List<UserTaskDto> userTaskDtos = new ArrayList<>();
                for (Task task : tasks) {
                    UserTaskDto userTaskDto = new UserTaskDto();
                    userTaskDto.setTaskId(task.getId());
                    //获取任务名称
                    ProcessInstance pi = acitivityService.getProcesses(task.getExecutionId());
                    if (null != pi) userTaskDto.setBusinessName(pi.getBusinessKey());
                    userTaskDto.setAssigement(task.getAssignee());
                    if (null != task.getCreateTime()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateString = formatter.format(task.getCreateTime());
                        userTaskDto.setCreateDate(dateString);
                    }
                    //获取任务提交人
                    Object customerManager = acitivityService.getVariable(task.getId(), "customerManager");
                    if (null != customerManager) {
                        userTaskDto.setCreateCustomerManager(customerManager.toString());
                    }
                    userTaskDtos.add(userTaskDto);
                }
                response.setResult(userTaskDtos);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse(false, ResultCodeConstant.UNKNOWERR, ex.getMessage());
        }

        return response;
    }
}
