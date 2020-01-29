package com.yepit.flow.dao.impl;

import com.yepit.flow.bo.BusTask;
import com.yepit.flow.bo.BusTaskMapper;
import com.yepit.flow.dao.interfaces.IBusTaskDAO;
import com.yepit.flow.dto.TaskInfoDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */
@Service("busTaskDAOImpl")
public class BusTaskDAOImpl implements IBusTaskDAO {
    @Resource
    BusTaskMapper busTaskMapper;
    @Override
    public int saveBusTask(BusTask busTask) throws Exception {
        return busTaskMapper.insert(busTask);
    }

    @Override
    public BusTask getBusTaskByBusId(Long busId) throws Exception {
        return busTaskMapper.selectByPrimaryKey(busId);
    }

    @Override
    public void updateBusTask(BusTask busTask) throws Exception {
        busTaskMapper.updateByPrimaryKey(busTask);
    }

    @Override
    public List<TaskInfoDTO> pageSearchUserHisTaskByUserId(Long assignUser, String status) {
        return busTaskMapper.pageSearchUserHisTaskByUserId(assignUser,status);
    }
}
