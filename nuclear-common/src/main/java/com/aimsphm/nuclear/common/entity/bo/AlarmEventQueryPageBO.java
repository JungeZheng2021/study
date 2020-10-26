package com.aimsphm.nuclear.common.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity.bo
 * @Description: <查询参数>
 * @Author: LUYI
 * @CreateDate: 2020/3/6 15:27
 * @UpdateUser: LUYI
 * @UpdateDate: 2020/3/6 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlarmEventQueryPageBO {
    @ApiModelProperty(value = "当前页")

    private Integer current;
    @ApiModelProperty(value = "每页条目数")
    private Integer size;
    @ApiModelProperty(value = "设备id")
    @JsonProperty(required = false)
    private String deviceId;
    @ApiModelProperty(value = "子系统id")
    @JsonProperty(required = false)
    private Long subSystemId;

    @ApiModelProperty(value = "报警类型")
    @JsonProperty(required = false)
    private Integer alarmType;
    @ApiModelProperty(value = "报警类别 前端传以,分割1 工况扰动\n" +
            "2 设备异常\n" +
            "3 传感器异常\n" +
            "4 潜在故障\n" +
            "5 虚假事件,\n" +
            "-1'其他'")
    @JsonProperty(required = false)
    private String reason;
    @ApiModelProperty(value = "报警状态")
    @JsonProperty(required = false)
    private Integer alarmStatus;
    @JsonProperty(required = false)
    @ApiModelProperty(value = "活动状态 0 停止 1 活动 2所有")
    private Integer active;
    @ApiModelProperty(value = "报警开始时间")
    @JsonProperty(required = false)
    private Date alarmStartTime;
    @ApiModelProperty(value = "报警结束时间")
    @JsonProperty(required = false)
    private Date alarmEndTime;

    @ApiModelProperty(value = "文本模糊条件")
    @JsonProperty(required = false)
    private String alarmContent;

    @ApiModelProperty(value = "报警备注 0：无备注 1：有备注 2：都有")
    @JsonProperty(required = false)
    private Integer remark;
    @ApiModelProperty(value = "报警级别，报警级别分隔符给后端比如‘1,2,4,5’")
    @JsonProperty(required = false)
    private String alarmLevel;

    @ApiModelProperty(value = "排序方向 0 升序 1降序")
    @JsonProperty(required = false)
    private Integer order;
    @ApiModelProperty(value = "排序字段0:时间 1：条数 2 报警级别")
    @JsonProperty(required = false)
    private Integer orderColumn;

    @ApiModelProperty(value = "tagId")
    @JsonProperty(required = false)
    private String tagId;
}
