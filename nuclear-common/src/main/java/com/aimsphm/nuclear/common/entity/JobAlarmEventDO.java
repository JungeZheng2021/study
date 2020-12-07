package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <报警事件实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_alarm_event")
@ApiModel(value = "报警事件实体")
public class JobAlarmEventDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5160324752664202338L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备类型", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "设备健康状态", notes = "")
    private Integer status;

    @ApiModelProperty(value = "持续运行时常", notes = "单位秒")
    private Long continuousRunningTime;

    @ApiModelProperty(value = "共计运行时常", notes = "单位是秒")
    private Long totalRunningTime;

}