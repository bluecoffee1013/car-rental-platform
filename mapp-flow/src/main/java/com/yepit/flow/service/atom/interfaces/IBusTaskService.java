package com.yepit.flow.service.atom.interfaces;

import com.yepit.flow.bo.BusTask;
import com.yepit.flow.dto.TaskInfoDTO;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;

/**
 * Created by Administrator on 2018/11/7.
 */
public interface IBusTaskService {

    public BusTask getBusTaskByBusId(Long busId)throws Exception;

    /**
     *  更新任务
     *@author  Cavin
     **/
    void updateBusTask(BusTask busTask)throws Exception;

    /**
     * 分页查询用户参与过的任务
     * @param assignUser
     * @param status
     * @param pageArg
     * @return
     */
    BaseResponse<PageResult<TaskInfoDTO>> pageSearchUserHisTaskByUserId(Long assignUser, String status, PageArg pageArg);

}
