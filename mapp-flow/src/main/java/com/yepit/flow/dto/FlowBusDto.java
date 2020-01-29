package com.yepit.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/9.
 */
@ApiModel
@Data
public class FlowBusDto implements Serializable {

    private static final long serialVersionUID = 8582205477813485934L;

    @ApiModelProperty(value = "创建客户经理id", name = "createUserId")
    private String createUserId;//创建客户经理名称

    @ApiModelProperty(value = "创建客户经理名称", name = "createUserName")
    private String createUserName;//创建客户经理id

    @ApiModelProperty(value = "流程ID", name = "flowProcessId")
    private String flowProcessId;//流程ID
    @ApiModelProperty(value = "任务节点ID", name = "taskId")
    private String taskId;//任务节点ID

    @ApiModelProperty(value = "当前节点处理人id", name = "doneUserId")
    private Long doneUserId;//当前节点处理人id

    @ApiModelProperty(value = "当前处理人姓名", name = "doneUserName")
    private String doneUserName;//当前处理人姓名

    @ApiModelProperty(value = "业务流程id", name = "busId")
    private Long busId;//业务流程id

    @ApiModelProperty(value = "处理结果0、拒绝 1、同意，2、补全资料", name = "result")
    private String result;//处理结果0、拒绝 1、同意，2、补全资料

    @ApiModelProperty(value = "备注信息", name = "remark")
    private String remark;//备注信息

    @ApiModelProperty(value = "详细的表单内容", name = "formDetail")
    private FlowTaskDetail formDetail;

}
