package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <设备状态实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-11
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_device_status")
@ApiModel(value = "设备状态实体")
public class JobDeviceStatusDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7783007188201340366L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备类型", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "设备健康状态", notes = "")
    private Integer status;

    @ApiModelProperty(value = "持续运行时常", notes = "单位毫秒")
    private Long continuousRunningTime;

    @ApiModelProperty(value = "共计运行时常", notes = "单位毫秒")
    private Long totalRunningTime;

    @ApiModelProperty(value = "状态持续时间", notes = "单位毫秒")
    private Long statusDuration;

    @ApiModelProperty(value = "状态结束时间", notes = "")
    private Date gmtEnd;

    @ApiModelProperty(value = "状态开始时间", notes = "")
    private Date gmtStart;

}