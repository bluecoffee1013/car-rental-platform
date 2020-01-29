package com.yepit.flow.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/11/12.
 */
@ApiModel
@Data
public class UserTaskListDto {
    private Timestamp crateDate;
    private String taskName;
}
