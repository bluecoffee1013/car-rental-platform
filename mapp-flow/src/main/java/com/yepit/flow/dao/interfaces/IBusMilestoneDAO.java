package com.yepit.flow.dao.interfaces;

import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusTaskConfig;
import com.yepit.flow.dto.BusMilestoneDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6.
 */
public interface IBusMilestoneDAO {
    public int saveBusMileStone(BusMilestone busMilestone)throws Exception;

    public int updateBusMileStone(BusMilestone busMilestone)throws Exception;

    List<BusMilestone> queryMileStoneByCond(Map<String, Object> map)throws Exception;

    List<BusMilestone> listMilestoneByBusId(Long busId)throws Exception;

    List<BusMilestone> queryMileStoneByCond(Long busId, String taskDefId)throws Exception;

    public Integer deleteByPrimaryKey(Long id) throws Exception;

    List<BusMilestone> listTaskByUserId(Long userId) throws Exception;

    List<BusMilestoneDTO> pageSearchTaskByUserId(Long userId) throws Exception;

    List<BusTaskConfig> listTaskConfig()throws Exception;

    BusMilestone getBusMilestone(Long id)throws Exception;

    List<BusMilestone> getUserHisTaskByUserId(Long assignUser,String status)throws Exception;

    List<BusMilestoneDTO> pageSearchUserHisTaskByUserId(Long assignUser,String status) throws Exception;

    Integer myTaskCount(Long userId, String end)throws Exception;

    List<BusMilestone> pageSearchMileStone(Long busId,String taskId)throws Exception;

}
