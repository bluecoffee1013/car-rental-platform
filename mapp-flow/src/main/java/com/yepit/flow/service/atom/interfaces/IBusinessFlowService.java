package com.yepit.flow.service.atom.interfaces;

import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.dto.FlowBusDto;
import com.yepit.flow.dto.FlowTaskDto;
import com.yepit.flow.mongo.entity.FlowBusInfo;
import com.yepit.flow.mongo.entity.FlowTaskCollect;
import com.yepit.mapp.framework.exception.ServiceException;
import org.activiti.engine.task.Task;
import org.apache.log4j.xml.SAXErrorHandler;

import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */
public interface IBusinessFlowService {
    public String createTask(FlowTaskDto taskDto) throws Exception;

    void test(FlowTaskDto flowTaskDto) throws Exception;

    void approvalNode(FlowBusDto flowBusDto) throws Exception;

    void uploadApply(FlowBusDto flowBusDto) throws Exception;

    /**
     * 保存表单详细信息
     *
     * @param flowBusInfo
     * @throws ServiceException
     */
    String saveFormDetail(FlowBusInfo flowBusInfo) throws ServiceException;

    /**
     * 修改表单信息
     *
     * @param flowBusInfo
     * @return
     * @throws ServiceException
     */
    String updateFormDetail(FlowBusInfo flowBusInfo) throws ServiceException;

    List<Task> listTaskByUserId(Long userId) throws Exception;

    FlowBusDto getTaskDetail(Long id) throws Exception;
}
