package com.yepit.flow.service.atom.impl;

import com.github.pagehelper.PageHelper;
import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTaskConfig;
import com.yepit.flow.bo.BusTaskCountDto;
import com.yepit.flow.common.constant.FlowConst;
import com.yepit.flow.dao.interfaces.IBusMilestoneDAO;
import com.yepit.flow.dto.BusMilestoneDTO;
import com.yepit.flow.mongo.entity.FlowTaskCollect;
import com.yepit.flow.service.atom.interfaces.IBusMileStoneService;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import com.yepit.mapp.framework.util.TimeUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6.
 */
@Service("busMileStoneServiceImpl")
public class BusMileStoneServiceImpl implements IBusMileStoneService {

    @Autowired
    IBusMilestoneDAO busMilestoneDAO;

    @Autowired
    private Datastore ds;

    @Override
    public int saveBusMileStone(Long busId, DelegateTask task) throws Exception {
        return 0;
    }

    @Override
    public int saveStartNode(Long busId, String taskId, String userName, Long userId) throws Exception {
        BusMilestone busMilestone = new BusMilestone();
        busMilestone.setAssigementUser(userId);
        busMilestone.setAssigementUserName(userName);
        busMilestone.setBusId(busId);
        busMilestone.setTaskId(taskId);
        busMilestone.setTaskDefId(FlowConst.FlowNodeId.UPLOAD_CUSTOMER_APPLY);
        busMilestone.setCurrentTask(FlowConst.FlowNode.UPLOAD_CUSTOMER_APPLY);
        busMilestone.setStartDate(TimeUtil.getSysDate());
        busMilestone.setEndDate(TimeUtil.getSysDate());
        busMilestone.setStatus(FlowConst.MileStoneStatus.END);
        busMilestoneDAO.saveBusMileStone(busMilestone);
        return 0;
    }

    @Override
    public BusMilestone queryMileStoneByCond(BusMilestone busMilestone) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("assigementUser", busMilestone.getAssigementUser());
        map.put("taskId", busMilestone.getTaskId());
        List<BusMilestone> list = busMilestoneDAO.queryMileStoneByCond(map);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public BusMilestone queryMileStoneByCond(Long busId, String taskDefId) throws Exception {

        List<BusMilestone> list = busMilestoneDAO.queryMileStoneByCond(busId, taskDefId);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<BusMilestone> listMilestoneByBusId(Long busId) throws Exception {

        List<BusMilestone> list = busMilestoneDAO.listMilestoneByBusId(busId);

        return list;
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) throws Exception {
        return busMilestoneDAO.deleteByPrimaryKey(id);
    }

    @Override
    public BaseResponse<PageResult<BusMilestoneDTO>> listTaskByUserId(Long userId, PageArg pageArg) throws Exception {
        BaseResponse<PageResult<BusMilestoneDTO>> resp = new BaseResponse<PageResult<BusMilestoneDTO>>();
        if (pageArg != null && pageArg.getPageNum() != -1) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }
        List<BusMilestoneDTO> dbResult = busMilestoneDAO.pageSearchTaskByUserId(userId);
        PageResult<BusMilestoneDTO> searchResult = new PageResult<BusMilestoneDTO>(dbResult);
        resp = resp.setSuccessfulResp("查询数据成功");
        resp.setResult(searchResult);
        return resp;
    }

    @Override
    public List<BusTaskConfig> listTaskConfig() throws Exception {
        return busMilestoneDAO.listTaskConfig();
    }

    @Override
    public BusMilestone getBusMilestone(Long id) throws Exception {
        return busMilestoneDAO.getBusMilestone(id);
    }

    @Override
    public BaseResponse<PageResult<BusMilestoneDTO>> getUserHisTaskByUserId(Long assignUser,PageArg pageArg) throws Exception {
        BaseResponse<PageResult<BusMilestoneDTO>> resp = new BaseResponse<PageResult<BusMilestoneDTO>>();
        if (pageArg != null && pageArg.getPageNum() != -1) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }
        List<BusMilestoneDTO> dbResult = busMilestoneDAO.pageSearchUserHisTaskByUserId(assignUser, FlowConst.BusTaskStatus.END);
        PageResult<BusMilestoneDTO> searchResult = new PageResult<BusMilestoneDTO>(dbResult);
        resp = resp.setSuccessfulResp("查询数据成功");
        resp.setResult(searchResult);
        return resp;
    }

    @Override
    public BusTaskCountDto myTaskCount(Long userId) throws Exception {
        Integer todoNumber = busMilestoneDAO.myTaskCount(userId, FlowConst.MileStoneStatus.DOING);
        if (null == todoNumber) {
            todoNumber = 0;
        }
        Integer doneNumber = busMilestoneDAO.myTaskCount(userId, FlowConst.MileStoneStatus.END);
        if (null == doneNumber) {
            doneNumber = 0;
        }
        BusTaskCountDto taskCountDto = new BusTaskCountDto();
        taskCountDto.setDoneNumber(doneNumber);
        taskCountDto.setTodoNumber(todoNumber);
        return taskCountDto;
    }

    @Override
    public BaseResponse<PageResult<BusMilestoneDTO>> myOnGoingTask(Long assignUser,PageArg pageArg) throws Exception {
        BaseResponse<PageResult<BusMilestoneDTO>> resp = new BaseResponse<PageResult<BusMilestoneDTO>>();
        if (pageArg != null && pageArg.getPageNum() != -1) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }
        List<BusMilestoneDTO> dbResult = busMilestoneDAO.pageSearchUserHisTaskByUserId(assignUser, FlowConst.BusTaskStatus.CREATE);
        PageResult<BusMilestoneDTO> searchResult = new PageResult<BusMilestoneDTO>(dbResult);
        resp = resp.setSuccessfulResp("查询数据成功");
        resp.setResult(searchResult);
        return resp;
    }

    @Override
    public BusMilestone getUpMilestone(Long busId) throws Exception {
        List<BusMilestone> milestones = busMilestoneDAO.listMilestoneByBusId(busId);
        if (null != milestones && milestones.size() > 1) {
            int leg = milestones.size() - 2;
            return milestones.get(leg);
        }
        return null;
    }


    @Override
    public void updateMileStone(BusMilestone updateMileStone) throws Exception {
        busMilestoneDAO.updateBusMileStone(updateMileStone);
    }

    @Override
    public List<BusMilestone> listMilestone(BusMilestone busMilestone) throws Exception {
        return busMilestoneDAO.listMilestoneByBusId(busMilestone.getBusId());
    }

    /**
     * 根据busid查询流程表单基本信息
     * @param busid
     * @return
     */
    private FlowTaskCollect getFlowFormInfo(Long busid){
        return ds.createQuery(FlowTaskCollect.class).filter(" _id",busid).get();
    }


    private List<BusMilestoneDTO> convert2Dto(List<BusMilestone> busMilestoneList){
        List<BusMilestoneDTO> dtoList = new ArrayList<BusMilestoneDTO>();
        if (busMilestoneList != null && busMilestoneList.size() > 0) {
            for (BusMilestone busMilestone : busMilestoneList) {
                BusMilestoneDTO dto = new BusMilestoneDTO();
                BeanUtils.copyProperties(busMilestone,dto);
                FlowTaskCollect flowTaskCollect = this.getFlowFormInfo(busMilestone.getBusId());
                if(flowTaskCollect != null){
                    dto.setCustomerName(flowTaskCollect.getCustomerName());
                    dto.setCustomerMobile(flowTaskCollect.getCustomerMobile());
                }
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
}
