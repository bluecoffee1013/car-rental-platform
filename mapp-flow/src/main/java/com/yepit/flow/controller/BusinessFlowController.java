package com.yepit.flow.controller;

import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTaskConfig;
import com.yepit.flow.bo.BusTaskCountDto;
import com.yepit.flow.common.annotation.AdminLogin;
import com.yepit.flow.common.annotation.SystemLoginUser;
import com.yepit.flow.common.constant.FlowConst;
import com.yepit.flow.common.constant.RoleTypeEnum;
import com.yepit.flow.dto.BusMilestoneDTO;
import com.yepit.flow.dto.FlowBusDto;
import com.yepit.flow.dto.FlowTaskDto;
import com.yepit.flow.dto.TaskInfoDTO;
import com.yepit.flow.service.atom.interfaces.IAcitivityFlowService;
import com.yepit.flow.service.atom.interfaces.IBusMileStoneService;
import com.yepit.flow.service.atom.interfaces.IBusTaskService;
import com.yepit.flow.service.atom.interfaces.IBusinessFlowService;
import com.yepit.mapp.framework.constant.ResultCodeConstant;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.dto.common.SimpleUserInfo;
import com.yepit.mapp.framework.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */
@Api(tags = "业务流程业务")
@RestController
public class BusinessFlowController {

    @Autowired
    IBusinessFlowService businessFlowService;

    @Autowired
    IBusMileStoneService busMileStoneService;

    @Autowired
    IBusTaskService busTaskService;

    @Autowired
    IAcitivityFlowService acitivityService;

    /**
     * 创建任务
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "开始创建任务")
    @RequestMapping(value = "/createTask", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> createTask(@ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        FlowTaskDto flowTaskDto = new FlowTaskDto();
        flowTaskDto.setUserId(currentUser.getUserId());
        flowTaskDto.setUserName(currentUser.getRealName());
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        try {
            String busId = businessFlowService.createTask(flowTaskDto);
            response.setResult(busId);
        } catch (Exception ex) {
            throw new ServiceException("创建任务异常:" + ex.getMessage());
        }
        return response;
    }

    /**
     * 客户经理提交申请材料
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "客户经理提交申请材料")
    @RequestMapping(value = "/uploadApply", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> uploadApply(@RequestBody FlowBusDto flowBusDto,
                                            @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        flowBusDto.setCreateUserId(currentUser.getUserId());
        flowBusDto.setCreateUserName(currentUser.getRealName());
        if (currentUser.getRoleId() == 6) {
            throw new ServiceException("您没有操作权限,请联系系统管理员");
        }
        String taskId = flowBusDto.getTaskId();
        Long busId = flowBusDto.getBusId();
        String flowProcessId = flowBusDto.getFlowProcessId();
        if (StringUtils.isBlank(flowProcessId) && (StringUtils.isBlank(taskId) && busId == null)) {
            throw new ServiceException("flowProccessID不能为空");
        }
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "提交资料成功");
        try {
            businessFlowService.uploadApply(flowBusDto);
        } catch (Exception ex) {
            throw new ServiceException("客户经理提交资料异常:" + ex.getMessage());
        }
        return response;
    }

    /**
     * 审批节点
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "审批节点")
    @RequestMapping(value = "/approvalNode", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> approvalNode(@RequestBody FlowBusDto flowBusDto,
                                             @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        if (currentUser.getRoleId() == 6) {
            throw new ServiceException("您没有操作权限,请联系系统管理员");
        }
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        flowBusDto.setDoneUserId(Long.parseLong(currentUser.getUserId()));
        flowBusDto.setDoneUserName(currentUser.getRealName());
        try {
            businessFlowService.approvalNode(flowBusDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException("提交资料异常:" + ex.getMessage());
        }
        return response;
    }

    /**
     * 查询任务节点
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询任务节点")
    @RequestMapping(value = "/test", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> test(@RequestBody FlowTaskDto flowTaskDto) throws Exception {
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        businessFlowService.test(flowTaskDto);

        return response;
    }

    /**
     * 查询具体任务执行过程
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询任务执行过程")
    @RequestMapping(value = "/listTaskMilestone", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<List<BusMilestone>> listMilestone(@RequestBody BusMilestone busMilestone) throws Exception {
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        List<BusMilestone> milestones = busMileStoneService.listMilestone(busMilestone);
        response.setResult(milestones);
        return response;
    }

    /**
     * 查询自己名下待办任务
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询自己名下待办任务")
    @RequestMapping(value = "/myTask", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<PageResult<BusMilestoneDTO>> listTaskByUserId(@RequestParam(defaultValue = "1") int pageNum,
                                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                                      @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        PageArg pageArg = new PageArg();
        pageArg.setPageNum(pageNum);
        pageArg.setPageSize(pageSize);
        BaseResponse<PageResult<BusMilestoneDTO>> resp = busMileStoneService.listTaskByUserId(Long.parseLong(currentUser.getUserId()), pageArg);
        return resp;
    }

    /**
     * 查询具体任务执行过程
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询具体任务执行过程")
    @RequestMapping(value = "/listTaskConfig", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<List<BusTaskConfig>> listTaskConfig() throws Exception {
        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        List<BusTaskConfig> milestones = busMileStoneService.listTaskConfig();
        response.setResult(milestones);
        return response;
    }

    /**
     * 查询任务节点详情
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询任务节点详情")
    @RequestMapping(value = "/taskDetail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<FlowBusDto> getTaskDetail(@PathVariable Long id) throws Exception {
        BaseResponse<FlowBusDto> response = new BaseResponse<FlowBusDto>().successful("查询任务节点详情成功");
        FlowBusDto milestones = businessFlowService.getTaskDetail(id);
        response.setResult(milestones);
        return response;
    }

    /**
     * 查询指定人员的历史任务详情
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询自己的的历史任务")
    @RequestMapping(value = "/myHisTask", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<PageResult<TaskInfoDTO>> getMyHisTask(@RequestParam(defaultValue = "1") int pageNum,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        PageArg pageArg = new PageArg();
        pageArg.setPageNum(pageNum);
        pageArg.setPageSize(pageSize);
        BaseResponse<PageResult<TaskInfoDTO>> resp = null;
        if (currentUser.getRoleId() == 6) {
            resp = busTaskService.pageSearchUserHisTaskByUserId(null,
                    FlowConst.BusTaskStatus.END, pageArg);
        } else {
            resp = busTaskService.pageSearchUserHisTaskByUserId(Long.valueOf(currentUser.getUserId()),
                    FlowConst.BusTaskStatus.END, pageArg);
        }
//        BaseResponse<PageResult<BusMilestoneDTO>> resp = busMileStoneService.getUserHisTaskByUserId(Long.valueOf(currentUser.getUserId()), pageArg);
        return resp;
    }

    /**
     * 查询用户下审批过的没有走完的流程
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "任务跟踪")
    @RequestMapping(value = "/myTaskTrack", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<PageResult<TaskInfoDTO>> myOnGoingTask(@RequestParam(defaultValue = "1") int pageNum,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {
        PageArg pageArg = new PageArg();
        pageArg.setPageNum(pageNum);
        pageArg.setPageSize(pageSize);
        BaseResponse<PageResult<TaskInfoDTO>> resp = null;
        if (currentUser.getRoleId() == 6) {
            resp = busTaskService.pageSearchUserHisTaskByUserId(null,
                    FlowConst.BusTaskStatus.CREATE, pageArg);
        } else {
            resp = busTaskService.pageSearchUserHisTaskByUserId(Long.valueOf(currentUser.getUserId()),
                    FlowConst.BusTaskStatus.CREATE, pageArg);
        }
        return resp;
    }

    /**
     * 查询指定人员的历史任务详情
     *
     * @author Cavin
     **/
    @AdminLogin
    @ApiOperation(value = "查询自己代办任务数和已办理任务数")
    @RequestMapping(value = "/myTaskCount", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<BusTaskCountDto> myTaskCount(@ApiIgnore @SystemLoginUser SimpleUserInfo currentUser) throws Exception {

        BaseResponse response = new BaseResponse(true, ResultCodeConstant.SUCCESSFUL, "");
        BusTaskCountDto taskCountDto = null;
        if (currentUser.getRoleId() == 6) {
            taskCountDto = busMileStoneService.myTaskCount(null);
        } else {
            taskCountDto = busMileStoneService.myTaskCount(Long.valueOf(currentUser.getUserId()));
        }

        response.setResult(taskCountDto);
        return response;
    }
}
