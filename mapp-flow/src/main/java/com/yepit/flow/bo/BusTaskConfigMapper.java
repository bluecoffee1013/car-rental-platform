package com.yepit.flow.bo;

import com.yepit.flow.bo.BusTaskConfig;
import java.util.List;

public interface BusTaskConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bus_task_config
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bus_task_config
     *
     * @mbg.generated
     */
    int insert(BusTaskConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bus_task_config
     *
     * @mbg.generated
     */
    BusTaskConfig selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bus_task_config
     *
     * @mbg.generated
     */
    List<BusTaskConfig> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bus_task_config
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BusTaskConfig record);
}