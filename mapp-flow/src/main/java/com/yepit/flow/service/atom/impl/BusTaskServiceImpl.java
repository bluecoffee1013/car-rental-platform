package com.yepit.flow.service.atom.impl;

import com.github.pagehelper.PageHelper;
import com.yepit.flow.bo.BusTask;
import com.yepit.flow.dao.interfaces.IBusTaskDAO;
import com.yepit.flow.dto.BusMilestoneDTO;
import com.yepit.flow.dto.TaskInfoDTO;
import com.yepit.flow.service.atom.interfaces.IBusTaskService;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/11/7.
 */
@Service("busTaskServiceImpl")
public class BusTaskServiceImpl implements IBusTaskService {

    @Autowired
    IBusTaskDAO busTaskDAO;

    @Override
    public BusTask getBusTaskByBusId(Long busId) throws Exception {
        return busTaskDAO.getBusTaskByBusId(busId);
    }

    @Override
    public void updateBusTask(BusTask busTask) throws Exception {
        busTaskDAO.updateBusTask(busTask);
    }

    /**
     * 分页查询用户参与过的任务
     *
     * @param assignUser
     * @param status
     * @return
     */
    @Override
    public BaseResponse<PageResult<TaskInfoDTO>> pageSearchUserHisTaskByUserId(Long assignUser, String status, PageArg pageArg) {
        BaseResponse<PageResult<TaskInfoDTO>> resp = new BaseResponse<PageResult<TaskInfoDTO>>().successful("查询数据成功");
        if (pageArg != null && pageArg.getPageNum() != -1) {
            PageHelper.startPage(pageArg.getPageNum(), pageArg.getPageSize());
        }
        List<TaskInfoDTO> dbResult = busTaskDAO.pageSearchUserHisTaskByUserId(assignUser, status);
        PageResult<TaskInfoDTO> searchResult = new PageResult<TaskInfoDTO>(dbResult);
        resp.setResult(searchResult);
        return resp;
    }
}
