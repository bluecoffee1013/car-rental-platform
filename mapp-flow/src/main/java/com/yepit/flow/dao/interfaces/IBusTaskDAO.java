package com.yepit.flow.dao.interfaces;

import com.yepit.flow.bo.BusTask;
import com.yepit.flow.dto.TaskInfoDTO;

import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */
public interface IBusTaskDAO {
    public int saveBusTask(BusTask busTask)throws Exception;

    BusTask getBusTaskByBusId(Long busId)throws Exception;

    void updateBusTask(BusTask busTask)throws Exception;

    List<TaskInfoDTO> pageSearchUserHisTaskByUserId(Long assignUser, String status);
}
