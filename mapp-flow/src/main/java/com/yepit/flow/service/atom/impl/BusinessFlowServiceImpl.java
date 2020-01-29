package com.yepit.flow.service.atom.impl;


import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTask;
import com.yepit.flow.cache.AdminCache;
import com.yepit.flow.common.constant.FlowConst;
import com.yepit.flow.dao.interfaces.IBusMilestoneDAO;
import com.yepit.flow.dao.interfaces.IBusTaskDAO;
import com.yepit.flow.dto.*;
import com.yepit.flow.mongo.entity.FlowBusInfo;
import com.yepit.flow.mongo.entity.FlowTaskCollect;
import com.yepit.flow.mongo.entity.FlowTaskDetailInfo;
import com.yepit.flow.service.atom.interfaces.IAcitivityFlowService;
import com.yepit.flow.service.atom.interfaces.IBusMileStoneService;
import com.yepit.flow.service.atom.interfaces.IBusTaskService;
import com.yepit.flow.service.atom.interfaces.IBusinessFlowService;
import com.yepit.mapp.framework.exception.ServiceException;
import com.yepit.mapp.framework.util.TimeUtil;
import com.yepit.mapp.framework.util.idgenerate.DefaultIdGenerator;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/11/6.
 */
@Service("businessFlowServiceImpl")
public class BusinessFlowServiceImpl implements IBusinessFlowService {

    @Autowired
    IAcitivityFlowService acitivityService;

    @Autowired
    IBusTaskDAO busTaskDAO;

    @Autowired
    IBusMileStoneService busMileStoneService;

    @Autowired
    IBusTaskService busTaskService;

    @Autowired
    private Datastore ds;

    @Autowired
    private AdminCache adminCache;

    @Autowired
    private IBusMilestoneDAO busMilestoneDAO;

    /**
     * 创建任务节点
     *
     * @author Cavin
     **/
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    @Override
    public String createTask(FlowTaskDto flowTaskDto) throws Exception {

        String busName = flowTaskDto.getUserName() + "于" + TimeUtil.getSysDate() + " 提交申请";
        Long busId = TimeUtil.getSysDate().getTime();
        //1、创建初始化流程变量
        Map<String, Object> mapVar = new HashMap<>();
        mapVar.put("userId", flowTaskDto.getUserId());//必填
        mapVar.put("userName", flowTaskDto.getUserName());//提交人名称，必填
        mapVar.put("busId", 0);//提交人名称，必填

        //1、启动
        String flowId = acitivityService.startProcesses("carRental", busName, mapVar);

        return flowId;
    }

    @Override
    public void test(FlowTaskDto flowTaskDto) throws Exception {
        BusMilestone busMilestone = new BusMilestone();
        busMilestone.setAssigementUser(Long.valueOf(flowTaskDto.getUserId()));
        busMilestone.setAssigementUserName(flowTaskDto.getUserName());
        busMilestone.setBusId(1541498927766L);
        busMileStoneService.queryMileStoneByCond(busMilestone);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    @Override
    public void approvalNode(FlowBusDto flowBusDto) throws Exception {
        Long userId = flowBusDto.getDoneUserId();
        String taskId = flowBusDto.getTaskId();
        String remark = flowBusDto.getRemark();
        String result = flowBusDto.getResult();
        //记录提交信息
        Task delegateTask = acitivityService.getTaskById(taskId);

        //更新处理结果
        BusMilestone busMilestone = new BusMilestone();
        busMilestone.setAssigementUser(userId);
        busMilestone.setTaskId(taskId);
        BusMilestone updateMilestone = busMileStoneService.queryMileStoneByCond(busMilestone);
        if (null != updateMilestone) {
            updateMilestone.setDoneResult(result);
            updateMilestone.setDoneRemark(remark);
            busMileStoneService.updateMileStone(updateMilestone);

            //保存流程节点信息到mongodb
            BusTask busTask = busTaskDAO.getBusTaskByBusId(updateMilestone.getBusId());
            FlowBusInfo flowBusInfo = new FlowBusInfo();
            BeanUtils.copyProperties(flowBusDto, flowBusInfo);
            flowBusInfo.setDoneTime(new Date());
            flowBusInfo.setTaskId(taskId);
            flowBusInfo.setCreateUserName(busTask.getCreateUserName());
            flowBusInfo.setCreateUserId(busTask.getCreateUserId().toString());
            flowBusInfo.setCreateTime(busTask.getCreateDate());
            flowBusInfo.setBusId(updateMilestone.getBusId());
            FlowTaskDetail formDetailInfo = flowBusDto.getFormDetail();
            if (formDetailInfo != null) {
                FlowTaskDetailInfo flowTaskDetailInfo = new FlowTaskDetailInfo();
                BeanUtils.copyProperties(formDetailInfo, flowTaskDetailInfo);
                flowBusInfo.setFormDetail(flowTaskDetailInfo);
            }
            flowBusInfo.setFlowNodeId(updateMilestone.getTaskDefId());
            flowBusInfo.setFlowNodeName(updateMilestone.getCurrentTask());
            String relDetailId = this.saveFormDetail(flowBusInfo);
            busMileStoneService.updateMileStone(updateMilestone);

            this.updateFlowTaskCollect(flowBusInfo);
        }
        //提交继续往下走
        Task task = acitivityService.getTaskById(taskId);
        Map map = new HashMap();
        map.put(FlowConst.FlowVariable.RESULT, result);
        acitivityService.completeTask(taskId, String.valueOf(userId), map, remark, task.getExecutionId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT, rollbackFor = {RuntimeException.class, ServiceException.class, Exception.class})
    @Override
    public void uploadApply(FlowBusDto flowBusDto) throws Exception {
        String flowId = flowBusDto.getFlowProcessId();
        String taskId = flowBusDto.getTaskId();
        Long busId = flowBusDto.getBusId();

        if (null == busId) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            busId = Long.valueOf(formatter.format(new Date()));
        }

        boolean updateFlag = true;
        if (null == taskId) {
            updateFlag = false;
        }

        String busName = flowBusDto.getCreateUserName() + "于" + TimeUtil.getFormattedDate(TimeUtil.getSysDate(), TimeUtil.YYYY_MM_DD_HH_MM_SS) + " 提交申请";

        //保存上传申请材料,保存到mongodb
        FlowBusInfo flowBusInfo = new FlowBusInfo();
        BeanUtils.copyProperties(flowBusDto, flowBusInfo);
        FlowTaskDetail formDetailInfo = flowBusDto.getFormDetail();
        BusMilestone oldBusMilestone = null;
        if (updateFlag) {
            //更新milestone状态
            List<BusMilestone> busMilestones = busMilestoneDAO.pageSearchMileStone(busId,taskId);
            if(busMilestones != null && busMilestones.size() > 0){
                oldBusMilestone = busMilestones.get(0);
                oldBusMilestone.setStatus("1");
                busMilestoneDAO.updateBusMileStone(oldBusMilestone);
            }
            BusTask busTask = busTaskDAO.getBusTaskByBusId(busId);
            if(busTask != null){
                if (null != formDetailInfo) {
                    busTask.setCustomerName(formDetailInfo.getCustomerName());
                    busTask.setCustomerMobile(formDetailInfo.getCustomerMobile());
                    busTaskDAO.updateBusTask(busTask);
                }
            }
//            busMileStoneService.queryMileStoneByCond(busId,taskId);
//            List<BusMilestone> busMilestone = busMileStoneService.listMilestoneByBusId(busId);
//            if (null != busMilestone && busMilestone.size() > 0) {
//                for (BusMilestone milestone : busMilestone){
//                    System.out.println("milestone.getId()="+milestone.getId());
//                    busMileStoneService.deleteByPrimaryKey(milestone.getId());
//                }
//            }
        } else {

            //2、保存任务信息
            BusTask busTask = new BusTask();
            busTask.setBusName(busName);
            busTask.setCreateUserName(flowBusDto.getCreateUserName());
            busTask.setCreateUserId(Long.valueOf(flowBusDto.getCreateUserId()));
            busTask.setCreateDate(TimeUtil.getSysDate());
            busTask.setFlowId(flowId);
            busTask.setBusId(busId);
            if (null != formDetailInfo) {
                busTask.setCustomerName(formDetailInfo.getCustomerName());
                busTask.setCustomerMobile(formDetailInfo.getCustomerMobile());
            }
            busTask.setStatus(FlowConst.BusTaskStatus.CREATE);
            busTaskDAO.saveBusTask(busTask);
        }

        //提交继续往下走
        if (StringUtils.isBlank(taskId)) {
            List<Task> taskList = acitivityService.findTaskByProcessAndCandidate(flowBusDto.getCreateUserId(), flowId);
            if (null != taskList && taskList.size() > 0) {
                taskId = taskList.get(0).getId();
            }
        }

        //保存上传申请材料,保存到mongodb
        flowBusInfo.setBusId(busId);
        flowBusInfo.setTaskId(taskId);
        flowBusInfo.setCreateTime(new Date());
        flowBusInfo.setDoneTime(new Date());
        flowBusInfo.setFlowNodeId(FlowConst.FlowNodeId.UPLOAD_CUSTOMER_APPLY);
        flowBusInfo.setFlowNodeName(FlowConst.FlowNode.UPLOAD_CUSTOMER_APPLY);

        if (formDetailInfo != null) {
            FlowTaskDetailInfo flowTaskDetailInfo = new FlowTaskDetailInfo();
            BeanUtils.copyProperties(formDetailInfo, flowTaskDetailInfo);
            flowBusInfo.setFormDetail(flowTaskDetailInfo);
        }
        String relDetailId = this.saveFormDetail(flowBusInfo);

        //3、保存keymilestone
        if(!updateFlag){
            busMileStoneService.saveStartNode(busId, taskId, flowBusDto.getCreateUserName(), Long.valueOf(flowBusDto.getCreateUserId()));
        }else{
            busMilestoneDAO.updateBusMileStone(oldBusMilestone);
        }


        //总是先删除原有的申请资料然后再添加
        ds.delete(ds.createQuery(FlowTaskCollect.class).filter("_id",busId));

        //保存流程汇总信息
        FlowTaskCollect flowTaskCollect = new FlowTaskCollect();
        BeanUtils.copyProperties(flowBusInfo, flowTaskCollect);
        if (formDetailInfo != null) {
            BeanUtils.copyProperties(formDetailInfo, flowTaskCollect);
        }
        ds.save(flowTaskCollect);

        Map map = new HashMap();
        map.put(FlowConst.FlowVariable.RESULT, "1");
        map.put(FlowConst.FlowVariable.BUSID, busId);
        acitivityService.completeTask(taskId, flowBusDto.getCreateUserId(), map);
    }

    /**
     * 保存表单详细信息
     *
     * @param flowBusInfo
     * @throws ServiceException
     */
    @Override
    public String saveFormDetail(FlowBusInfo flowBusInfo) throws ServiceException {
        String createUserId = flowBusInfo.getCreateUserId();
        if (StringUtils.isNotBlank(createUserId)) {
            AdminDTO creater = adminCache.getAdminById(createUserId);
            if (creater != null) {
                flowBusInfo.setCreateUserName(creater.getRealname());
            }
        }
        Long doneUserId = flowBusInfo.getDoneUserId();
        if (doneUserId != null) {
            AdminDTO doneUser = adminCache.getAdminById(doneUserId.toString());
            if (doneUser != null) {
                flowBusInfo.setCreateUserName(doneUser.getRealname());
            }
        }

        DefaultIdGenerator idGenerator = new DefaultIdGenerator();
        String doneId = idGenerator.next();
        flowBusInfo.setDoneId(doneId);
        ds.save(flowBusInfo);
        return doneId;
    }

    /**
     * 修改表单信息
     *
     * @param flowBusInfo
     * @return
     * @throws ServiceException
     */
    @Override
    public String updateFormDetail(FlowBusInfo flowBusInfo) throws ServiceException {
        List<FlowBusInfo> dbResult = ds.createQuery(FlowBusInfo.class)
                .filter("taskId =", flowBusInfo.getTaskId())
                .filter("busId =", flowBusInfo.getBusId())
                .asList();
        if (dbResult != null && dbResult.size() > 0) {
            String createUserId = flowBusInfo.getCreateUserId();
            if (StringUtils.isNotBlank(createUserId)) {
                AdminDTO creater = adminCache.getAdminById(createUserId);
                if (creater != null) {
                    flowBusInfo.setCreateUserName(creater.getRealname());
                }
            }
            Long doneUserId = flowBusInfo.getDoneUserId();
            if (doneUserId != null) {
                AdminDTO doneUser = adminCache.getAdminById(doneUserId.toString());
                if (doneUser != null) {
                    flowBusInfo.setCreateUserName(doneUser.getRealname());
                }
            }

            FlowBusInfo tmp = dbResult.get(0);
            flowBusInfo.setDoneId(tmp.getDoneId());
            ds.delete(flowBusInfo);
            ds.save(flowBusInfo);
            return tmp.getDoneId();
        }
        return null;
    }

    @Override
    public List<Task> listTaskByUserId(Long userId) throws Exception {
        List<Task> tasks = acitivityService.findTasksByUserId(FlowConst.FLOW_KEYID, String.valueOf(userId));
        return tasks;
    }

    @Override
    public FlowBusDto getTaskDetail(Long id) throws Exception {
        BusMilestone busMilestone = busMileStoneService.getBusMilestone(id);
        if (busMilestone == null) {
            throw new ServiceException("任务[" + id + "]不存在");
        }
        FlowBusDto flowBusDto = new FlowBusDto();
        //处理结果
        flowBusDto.setResult(busMilestone.getDoneResult());
        flowBusDto.setRemark(busMilestone.getDoneRemark());
        flowBusDto.setBusId(busMilestone.getBusId());
        flowBusDto.setTaskId(busMilestone.getTaskId());
        flowBusDto.setDoneUserId(busMilestone.getAssigementUser());
        flowBusDto.setDoneUserName(busMilestone.getAssigementUserName());
        flowBusDto.setFormDetail(new FlowTaskDetail());

        //处理表单信息
        Long busId = busMilestone.getBusId();
        String taskId = busMilestone.getTaskId();
        String taskDefId = busMilestone.getTaskDefId();
        FlowTaskCollect flowTaskCollect = ds.createQuery(FlowTaskCollect.class).filter("busId =", busId).get();
        List<FlowBusInfo> flowBusInfoList = ds.createQuery(FlowBusInfo.class).filter("busId =", busId)
                .filter("taskId =", taskId)
                .asList();
        if (flowBusInfoList != null && flowBusInfoList.size() > 0) {
            FlowBusInfo flowBusInfo = flowBusInfoList.get(0);
            if (flowBusInfo.getFormDetail() != null) {
                FlowTaskDetail formDetail = new FlowTaskDetail();
                BeanUtils.copyProperties(flowBusInfo.getFormDetail(), formDetail);
                flowBusDto.setFormDetail(formDetail);
            }
        }else if(StringUtils.isNotBlank(taskDefId) && FlowConst.FlowNodeId.UPLOAD_CUSTOMER_APPLY.equals(taskDefId)){
            if(flowTaskCollect != null){
                BeanUtils.copyProperties(flowTaskCollect,flowBusDto.getFormDetail());
                flowBusDto.getFormDetail().setDocList(flowTaskCollect.getDocList());
            }
        }

        //设置客户名称和电话
        if(flowTaskCollect != null){
            flowBusDto.getFormDetail().setCustomerName(flowTaskCollect.getCustomerName());
            flowBusDto.getFormDetail().setCustomerMobile(flowTaskCollect.getCustomerMobile());
        }

        return flowBusDto;
    }

    /**
     * 每次审核通过时更新总体信息
     *
     * @param flowBusInfo
     */
    private void updateFlowTaskCollect(FlowBusInfo flowBusInfo) {
        //只有审核通过才更新
        if (!"1".equals(flowBusInfo.getResult())) {
            return;
        }
        Long busId = flowBusInfo.getBusId();
        FlowTaskCollect flowTaskCollect = ds.createQuery(FlowTaskCollect.class).filter("_id =", busId).get();
        if (flowTaskCollect != null) {
            FlowTaskDetailInfo flowTaskDetail = flowBusInfo.getFormDetail();
            if (flowTaskDetail != null) {
                String customerName = flowTaskDetail.getCustomerName();
                if (StringUtils.isNotBlank(customerName)) {
                    flowTaskCollect.setCustomerName(customerName);
                }
                String customerMobile = flowTaskDetail.getCustomerMobile();
                if (StringUtils.isNotBlank(customerMobile)) {
                    flowTaskCollect.setCustomerMobile(customerMobile);
                }
                String appointmentTime = flowTaskDetail.getAppointmentTime();
                if (StringUtils.isNotBlank(appointmentTime)) {
                    flowTaskCollect.setAppointmentTime(customerMobile);
                }
                String appointmentPlace = flowTaskDetail.getAppointmentPlace();
                if (StringUtils.isNotBlank(appointmentPlace)) {
                    flowTaskCollect.setAppointmentPlace(appointmentPlace);
                }
                String homeVisitTime = flowTaskDetail.getHomeVisitTime();
                if (StringUtils.isNotBlank(homeVisitTime)) {
                    flowTaskCollect.setHomeVisitTime(homeVisitTime);
                }
                String homeVisitPlace = flowTaskDetail.getHomeVisitPlace();
                if (StringUtils.isNotBlank(homeVisitPlace)) {
                    flowTaskCollect.setHomeVisitPlace(homeVisitPlace);
                }
                String isInsurance = flowTaskDetail.getIsInsurance();
                if (StringUtils.isNotBlank(isInsurance)) {
                    flowTaskCollect.setIsInsurance(isInsurance);
                }
                Long deposit = flowTaskDetail.getDeposit();
                if (deposit != null) {
                    flowTaskCollect.setDeposit(deposit);
                }
            }

            //根据busId查询当前流程节点中所有状态为审核通过的文件列表并更新到汇总表
            List<DocInfo> allDocList = new ArrayList<>();
            List<FlowBusInfo> flowBusInfoList = ds.createQuery(FlowBusInfo.class).filter("result =", "1")
                    .filter("result =", null).asList();
            if (flowBusInfoList != null && flowBusInfoList.size() > 0) {
                for (FlowBusInfo flowBusObj : flowBusInfoList) {
                    if (flowBusInfo.getFormDetail() != null) {
                        List<DocInfo> docList = flowBusObj.getFormDetail().getDocList();
                        if (docList != null) {
                            allDocList.addAll(docList);
                        }
                    }
                }
                flowTaskCollect.setDocList(allDocList);
            }
            //先删除再创建
            ds.delete(ds.createQuery(FlowTaskCollect.class).filter("_id =", busId));
            flowTaskCollect.setBusId(busId);
            ds.save(flowTaskCollect);
        }
    }
}
