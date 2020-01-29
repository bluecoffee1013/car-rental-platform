package com.yepit.flow.dto;

import com.yepit.flow.bo.BusMilestone;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/30.
 */
public class MyTaskTrackDto implements Serializable {
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<BusMilestone> getBusMilestoneList() {
        return busMilestoneList;
    }

    public void setBusMilestoneList(List<BusMilestone> busMilestoneList) {
        this.busMilestoneList = busMilestoneList;
    }

    private String taskName;
    private List<BusMilestone> busMilestoneList;
}
