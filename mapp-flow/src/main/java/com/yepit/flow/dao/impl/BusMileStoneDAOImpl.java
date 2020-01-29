package com.yepit.flow.dao.impl;

import com.yepit.flow.bo.BusMilestone;
import com.yepit.flow.bo.BusMilestoneMapper;
import com.yepit.flow.bo.BusTaskConfig;
import com.yepit.flow.bo.BusTaskConfigMapper;
import com.yepit.flow.dao.interfaces.IBusMilestoneDAO;
import com.yepit.flow.dto.BusMilestoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/6.
 */
@Service("busMileStoneDAOImpl")
public class BusMileStoneDAOImpl implements IBusMilestoneDAO {
    @Resource
    BusMilestoneMapper busMilestoneMapper;
    @Resource
    BusTaskConfigMapper busTaskConfigMapper;
    @Override
    public int saveBusMileStone(BusMilestone busMilestone) throws Exception {
        return busMilestoneMapper.insert(busMilestone);
    }

    @Override
    public int updateBusMileStone(BusMilestone busMilestone) throws Exception {
        return busMilestoneMapper.updateByPrimaryKey(busMilestone);
    }

    @Override
    public List<BusMilestone> queryMileStoneByCond(Map<String, Object> map) throws Exception {
        return busMilestoneMapper.selectByCond(map);
    }

    @Override
    public List<BusMilestone> listMilestoneByBusId(Long busId) throws Exception {
        return busMilestoneMapper.listMilestoneByBusId(busId);
    }

    @Override
    public List<BusMilestone> queryMileStoneByCond(Long busId, String taskDefId) throws Exception {
        return busMilestoneMapper.queryMileStoneByBusIdAndTaskDefId(busId,taskDefId);
    }
    @Override
    public Integer deleteByPrimaryKey(Long id) throws Exception {
        return busMilestoneMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BusMilestone> listTaskByUserId(Long userId) throws Exception {
        return busMilestoneMapper.listTaskByUserId(userId);
    }

    @Override
    public List<BusMilestoneDTO> pageSearchTaskByUserId(Long userId) throws Exception {
        return busMilestoneMapper.pageSearchTaskByUserId(userId);
    }

    @Override
    public List<BusTaskConfig> listTaskConfig() throws Exception {
        return busTaskConfigMapper.selectAll() ;
    }

    @Override
    public BusMilestone getBusMilestone(Long id) throws Exception {
        return busMilestoneMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BusMilestone> getUserHisTaskByUserId(Long assignUser,String status) throws Exception {
        return busMilestoneMapper.getUserHisTaskByUserId(assignUser,status);
    }

    @Override
    public List<BusMilestoneDTO> pageSearchUserHisTaskByUserId(Long assignUser,String status) throws Exception {
        return busMilestoneMapper.pageSearchUserHisTaskByUserId(assignUser,status);
    }

    @Override
    public Integer myTaskCount(Long userId, String status) throws Exception {
        Long ret = busMilestoneMapper.myTaskCount(userId,status);
        if (null == ret) {
            return 0;
        }
        return ret.intValue();
    }

    @Override
    public List<BusMilestone> pageSearchMileStone(Long busId, String taskId) throws Exception {
        return busMilestoneMapper.pageSearchMileStone(busId,taskId);
    }
}
