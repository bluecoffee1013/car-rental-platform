package com.yepit.flow.service.atom.interfaces;

import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTaskConfig;
import com.yepit.flow.bo.BusTaskCountDto;
import com.yepit.flow.dto.BusMilestoneDTO;
import com.yepit.mapp.framework.dto.common.BaseResponse;
import com.yepit.mapp.framework.dto.common.PageArg;
import com.yepit.mapp.framework.dto.common.PageResult;
import org.activiti.engine.delegate.DelegateTask;

import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */
public interface IBusMileStoneService {
    /**
     * 创建下一个节点
     *
     * @author Cavin
     **/
    public int saveBusMileStone(Long busId, DelegateTask task) throws Exception;

    /**
     * 保存开始节点
     *
     * @author Cavin
     **/
    public int saveStartNode(Long busId, String taskId, String userName, Long userId) throws Exception;

    /**
     * 查询当前执行状态
     *
     * @author Cavin
     **/
    BusMilestone queryMileStoneByCond(BusMilestone busMilestone) throws Exception;

    /**
     * 更新milestone
     *
     * @author Cavin
     **/
    void updateMileStone(BusMilestone updateMileStone) throws Exception;

    List<BusMilestone> listMilestone(BusMilestone busMilestone) throws Exception;

    public BusMilestone queryMileStoneByCond(Long busId, String taskDefId) throws Exception;

    public List<BusMilestone> listMilestoneByBusId(Long busId) throws Exception;

    public Integer deleteByPrimaryKey(Long id) throws Exception;

    BaseResponse<PageResult<BusMilestoneDTO>> listTaskByUserId(Long userId, PageArg pageArg) throws Exception;

    List<BusTaskConfig> listTaskConfig() throws Exception;

    BusMilestone getBusMilestone(Long id) throws Exception;

    BaseResponse<PageResult<BusMilestoneDTO>> getUserHisTaskByUserId(Long assignUser, PageArg pageArg) throws Exception;

    BusTaskCountDto myTaskCount(Long aLong) throws Exception;

    BaseResponse<PageResult<BusMilestoneDTO>> myOnGoingTask(Long assignUser, PageArg pageArg) throws Exception;

    /**
     * 根据当前节点的busId，获取上一节点信息
     *
     * @author 66578
     **/
    BusMilestone getUpMilestone(Long busId) throws Exception;
}
