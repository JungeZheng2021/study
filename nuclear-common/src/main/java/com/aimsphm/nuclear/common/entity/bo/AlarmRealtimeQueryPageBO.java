package com.aimsphm.nuclear.common.entity.bo;

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
public class AlarmRealtimeQueryPageBO {
    @ApiModelProperty(value = "当前页")
    private Integer current;
    @ApiModelProperty(value = "每页条目数")
    private Integer size;
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    @ApiModelProperty(value = "子系统id")
    private Long subSystemId;
    @ApiModelProperty(value = "报警类型")
    private Integer alarmType;

    @ApiModelProperty(value = "关联的tagId")
    private String tagId;

    @ApiModelProperty(value = "报警评价")
    private String evaluation;

    @ApiModelProperty(value = "报警开始时间")
    private Date alarmStartTime;
    @ApiModelProperty(value = "报警结束时间")
    private Date alarmEndTime;


    @ApiModelProperty(value = "排序方向 0 升序 1降序")
    private Integer order;
    @ApiModelProperty(value = "排序字段0:时间")
    private Integer orderColumn;
}
